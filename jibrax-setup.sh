#!/bin/bash
set -e

LOCK_FILE=".jibrax-setup.lock"
SETUP_DONE_FILE=".jibrax-setup.done"

echo "🚀 Configuration de l'environnement Jibrax..."
echo ""

# Vérifier si le setup a déjà été effectué avec succès
if [ -f "$SETUP_DONE_FILE" ]; then
    echo "⚠️  Le setup a déjà été effectué !"
    echo ""
    read -p "Voulez-vous reconfigurer Keycloak ? (cela supprimera les données existantes) [y/N]: " -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "❌ Setup annulé."
        exit 0
    fi
    echo "🔄 Suppression de la configuration existante..."
    docker-compose down -v
    rm -f "$SETUP_DONE_FILE"
fi

# Vérifier si le script est déjà en cours d'exécution
if [ -f "$LOCK_FILE" ]; then
    LOCK_PID=$(cat "$LOCK_FILE")
    if ps -p "$LOCK_PID" > /dev/null 2>&1; then
        echo "❌ Le script est déjà en cours d'exécution (PID: $LOCK_PID)"
        echo "   Si vous êtes sûr qu'aucun setup n'est en cours, supprimez le fichier: $LOCK_FILE"
        exit 1
    else
        echo "⚠️  Fichier verrou obsolète détecté, suppression..."
        rm -f "$LOCK_FILE"
    fi
fi

# Créer le fichier verrou avec le PID actuel
echo $$ > "$LOCK_FILE"

# Fonction de nettoyage en cas d'erreur ou d'interruption
cleanup() {
    EXIT_CODE=$?
    echo ""
    if [ $EXIT_CODE -ne 0 ]; then
        echo "❌ Le setup a échoué. Le fichier verrou a été supprimé."
        echo "   Vous pouvez relancer le script."
    fi
    rm -f "$LOCK_FILE"
    exit $EXIT_CODE
}

# Capturer les signaux d'interruption
trap cleanup EXIT INT TERM

# Vérifier que docker-compose.yml existe
if [ ! -f "docker-compose.yml" ]; then
    echo "❌ Erreur: docker-compose.yml n'existe pas !"
    exit 1
fi

# Rendre le script exécutable
chmod +x keycloak-setup.sh

echo "📦 Démarrage des conteneurs..."
docker-compose up -d

echo ""
echo "⏳ Attente de l'initialisation de Keycloak..."
echo "   (Cela peut prendre 1-2 minutes)"
echo ""

# Attendre que keycloak-setup se termine
./keycloak-setup.sh &
PID=$!
wait $PID
echo "✅ Script terminé !"

# Marquer le setup comme terminé avec succès
touch "$SETUP_DONE_FILE"

echo ""
echo "✅ Configuration terminée !"
echo ""
echo "📋 Services disponibles:"
echo "   🔐 Keycloak Admin: http://localhost:8080/admin"
echo "       └─ Credentials: admin / admin"
echo ""
echo "   🗄️  PostgreSQL: localhost:5432"
echo "       └─ Database: jibrax"
echo "       └─ User: admin / admin123"
echo ""
echo "   🖥️  pgAdmin: http://localhost:8081"
echo "       └─ Credentials: admin@admin.com / admin123"
echo ""
echo "   👥 Realm: jibrax"
echo "       └─ alice.admin / alice123"
echo "       └─ bob.manager / bob123"
echo "       └─ charlie.user / charlie123"
echo ""
echo "   🔧 Client: jibrax-api"
echo "       └─ Consultez les logs pour récupérer le Client Secret"
echo ""
echo "💡 Pour reconfigurer, relancez ce script et répondez 'y' à la question."
echo ""