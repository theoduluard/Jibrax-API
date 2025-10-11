#!/bin/sh

########################################################
# DO NOT LAUNCH IT ALONE, USE jibrax-setup.sh INSTEAD #
########################################################

set -e

echo "⏳ Attente du démarrage de Keycloak..."
MAX_RETRIES=60
RETRY_COUNT=0

KEYCLOAK_URL="http://localhost:8080"
ADMIN_USER="admin"
ADMIN_PASSWORD="admin"
REALM_NAME="jibrax"

# Attendre que Keycloak soit prêt
until [ "$(curl -s -o /dev/null -w '%{http_code}' http://localhost:8080/)" = "302" ]; do
  RETRY_COUNT=$((RETRY_COUNT + 1))
  if [ $RETRY_COUNT -ge $MAX_RETRIES ]; then
    echo "❌ Timeout: Keycloak n'a pas démarré après 5 minutes"
    exit 1
  fi
  echo "Keycloak n'est pas encore prêt, nouvelle tentative dans 5s... ($RETRY_COUNT/$MAX_RETRIES)"
  sleep 5
done

echo "✅ Keycloak est prêt !"
echo "⏳ Attente supplémentaire de 10s pour stabilisation de l'API..."
sleep 10

# Fonction pour obtenir le token admin
get_admin_token() {
  curl -s -X POST "$KEYCLOAK_URL/realms/master/protocol/openid-connect/token" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=$ADMIN_USER" \
    -d "password=$ADMIN_PASSWORD" \
    -d 'grant_type=password' \
    -d 'client_id=admin-cli' | grep -o '"access_token":"[^"]*' | cut -d'"' -f4
}

echo "🔑 Récupération du token admin..."
TOKEN=$(get_admin_token)

# Attendre que le realm soit importé (avec retry)
echo "⏳ Vérification que le realm '$REALM_NAME' est importé..."
REALM_CHECK_COUNT=0
until curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM_NAME" \
  -H "Authorization: Bearer $TOKEN" | grep -q "\"realm\":\"$REALM_NAME\""; do
  REALM_CHECK_COUNT=$((REALM_CHECK_COUNT + 1))
  if [ $REALM_CHECK_COUNT -ge 30 ]; then
    echo "❌ Le realm '$REALM_NAME' n'a pas été importé"
    exit 1
  fi
  echo "En attente de l'import du realm... ($REALM_CHECK_COUNT/30)"
  sleep 2
done

echo "✅ Realm '$REALM_NAME' détecté !"

# Récupérer l'ID du client jibrax-api
echo "🔍 Récupération de l'ID du client 'jibrax-api'..."
CLIENT_ID=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM_NAME/clients" \
  -H "Authorization: Bearer $TOKEN" \
  | grep -o '"id":"[^"]*","clientId":"jibrax-api"' \
  | grep -o '"id":"[^"]*' \
  | cut -d'"' -f4)

if [ -z "$CLIENT_ID" ]; then
  echo "❌ Client 'jibrax-api' introuvable"
  exit 1
fi

echo "✅ Client trouvé: $CLIENT_ID"

# Récupérer le secret du client
echo "🔑 Récupération du secret du client..."
CLIENT_SECRET=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM_NAME/clients/$CLIENT_ID/client-secret" \
  -H "Authorization: Bearer $TOKEN" | grep -o '"value":"[^"]*' | cut -d'"' -f4)

if [ -z "$CLIENT_SECRET" ]; then
  echo "❌ Impossible de récupérer le secret du client"
  exit 1
fi

# Sauvegarder le secret dans un fichier
echo "💾 Sauvegarde du client secret..."
mkdir -p src/main/resources
cat > src/main/resources/application-secret.properties << EOF
# Keycloak Client Secret - Generated automatically
# DO NOT COMMIT THIS FILE TO VERSION CONTROL
KEYCLOAK_CLIENT_SECRET=$CLIENT_SECRET
EOF

chmod 600 src/main/resources/application-secret.properties
echo "✅ Client secret sauvegardé dans src/main/resources/application-secret.properties"

# Récupérer les IDs des groupes
echo "🔍 Récupération des IDs des groupes..."
ADMIN_GROUP=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM_NAME/groups" \
  -H "Authorization: Bearer $TOKEN" \
  | grep -o '"id":"[^"]*","name":"Administrators"' \
  | grep -o '"id":"[^"]*' \
  | cut -d'"' -f4)

MANAGER_GROUP=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM_NAME/groups" \
  -H "Authorization: Bearer $TOKEN" \
  | grep -o '"id":"[^"]*","name":"Managers"' \
  | grep -o '"id":"[^"]*' \
  | cut -d'"' -f4)

USER_GROUP=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM_NAME/groups" \
  -H "Authorization: Bearer $TOKEN" \
  | grep -o '"id":"[^"]*","name":"Users"' \
  | grep -o '"id":"[^"]*' \
  | cut -d'"' -f4)

# Fonction pour créer un utilisateur
create_user() {
  local USERNAME=$1
  local EMAIL=$2
  local FIRSTNAME=$3
  local LASTNAME=$4
  local PASSWORD=$5
  local ROLE=$6
  local GROUP=$7

  echo "➕ Création de l'utilisateur: $USERNAME"

  # Vérifier si l'utilisateur existe déjà
  EXISTING_USER=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users?username=$USERNAME&exact=true" \
    -H "Authorization: Bearer $TOKEN")

  if echo "$EXISTING_USER" | grep -q "\"username\":\"$USERNAME\""; then
    echo "⚠️  L'utilisateur $USERNAME existe déjà, passage..."
    return
  fi

  USER_ID=$(curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users" \
    -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
    -d "{
      \"username\": \"$USERNAME\",
      \"email\": \"$EMAIL\",
      \"firstName\": \"$FIRSTNAME\",
      \"lastName\": \"$LASTNAME\",
      \"enabled\": true,
      \"emailVerified\": true
    }" -i | grep -i "location:" | sed 's/.*\///' | tr -d '\r')

  if [ -z "$USER_ID" ]; then
    echo "❌ Erreur lors de la création de l'utilisateur $USERNAME"
    return
  fi

  # Définir le mot de passe
  curl -s -X PUT "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users/$USER_ID/reset-password" \
    -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
    -d "{
      \"type\": \"password\",
      \"value\": \"$PASSWORD\",
      \"temporary\": false
    }"

  # Assigner le rôle realm
  ROLE_DATA=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles/$ROLE" -H "Authorization: Bearer $TOKEN")

  curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users/$USER_ID/role-mappings/realm" \
    -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
    -d "[$ROLE_DATA]"

  # Ajouter au groupe
  curl -s -X PUT "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users/$USER_ID/groups/$GROUP" \
    -H "Authorization: Bearer $TOKEN"

  echo "✅ Utilisateur $USERNAME créé avec succès"
}

# Créer les utilisateurs
echo ""
echo "👥 Création des utilisateurs..."
create_user "alice.admin" "alice@jibrax.com" "Alice" "Admin" "alice123" "ADMIN" "$ADMIN_GROUP"
create_user "bob.manager" "bob@jibrax.com" "Bob" "Manager" "bob123" "MANAGER" "$MANAGER_GROUP"
create_user "charlie.user" "charlie@jibrax.com" "Charlie" "User" "charlie123" "USER" "$USER_GROUP"

echo ""
echo "✅ Configuration terminée avec succès !"
echo ""
echo "📋 Résumé:"
echo "   - Realm: $REALM_NAME"
echo "   - Utilisateurs créés: alice.admin, bob.manager, charlie.user"
echo "   - Client secret sauvegardé dans: src/main/resources/application-secret.properties"