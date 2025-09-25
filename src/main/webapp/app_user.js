$(document).ready(function () {
    loadTeams();

    $("#validUser").on("click", handleUserCreation);
    $("#showUsers").on("click", loadAndDisplayUsers);
});

/**
 * Charge la liste des équipes depuis l'API
 */
async function loadTeams() {
    const teamSelect = $("#team");

    try {
        teamSelect.prop('disabled', true);
        teamSelect.html('<option value="">Chargement des équipes...</option>');

        const response = await axios.get('/team');
        const teams = response.data;

        teamSelect.empty();

        if(teams.length > 0) {
            teamSelect.append('<option value=""> Pas d\'équipe </option>');

            teams.forEach(team => {
                teamSelect.append(`<option value="${team.id}">${team.username}</option>`);
            });
        }

        teamSelect.prop('disabled', false);

    } catch (error) {
        setTimeout(() => {
            teamSelect.html('<option value="">-- Aucune équipe connue --</option>');
            teamSelect.prop('disabled', true);
        }, 2000);
    }
}

/**
 * Gère la création d'un utilisateur
 */
async function handleUserCreation(e) {
    e.preventDefault();

    const button = $("#valid");
    const originalText = button.text();

    if (!validateForm()) {
        return;
    }

    try {
        setButtonState(button, true, "Création...");
        const userData = await collectFormData();
        const response = await axios.post('/user', userData);
        console.log("✅ Utilisateur créé :", response.data);
        showNotification("Utilisateur créé avec succès !", "success");
        resetForm();

    } catch (error) {
        console.error("❌ Erreur lors de la création :", error);
        handleCreationError(error);
    } finally {
        setButtonState(button, false, originalText);
    }
}

/**
 * Collecte et prépare les données du formulaire
 */
async function collectFormData() {
    const teamValue = $("#team").val();

    const userData = {
        firstname: $("#firstname").val().trim(),
        lastname: $("#lastname").val().trim(),
        username: $("#username").val().trim(),
        email: $("#email").val().trim(),
        password: $("#password").val(),
        team: teamValue ? Number(teamValue) : null,
        image: null
    };

    const file = $("#image")[0].files[0];
    if (file) {
        if (!validateImage(file)) {
            throw new Error("Format d'image invalide ou taille trop importante (max 5MB)");
        }

        userData.image = await convertImageToBase64(file);
    }

    return userData;
}

/**
 * Valide le formulaire côté client
 */
function validateForm() {
    const requiredFields = ['firstname', 'lastname', 'username', 'email', 'password'];
    const errors = [];

    // Vérifier les champs requis
    requiredFields.forEach(field => {
        const value = $(`#${field}`).val().trim();
        if (!value) {
            errors.push(`Le champ ${getFieldLabel(field)} est requis`);
        }
    });

    // Validation email
    const email = $("#email").val().trim();
    if (email && !isValidEmail(email)) {
        errors.push("Format d'email invalide");
    }

    // Validation mot de passe
    const password = $("#password").val();
    if (password && password.length < 6) {
        errors.push("Le mot de passe doit contenir au moins 6 caractères");
    }

    if (errors.length > 0) {
        showNotification(errors.join('\n'), "error");
        return false;
    }

    return true;
}

/**
 * Gère les erreurs de création
 */
function handleCreationError(error) {
    let message = "Erreur lors de la création de l'utilisateur";

    if (error.message && !error.response) {
        message = error.message;
    } else if (error.response?.data?.message) {
        message = error.response.data.message;
    } else if (error.response?.status) {
        switch (error.response.status) {
            case 400:
                message = "Données invalides. Vérifiez vos informations.";
                break;
            case 409:
                message = "Cet email est déjà utilisé.";
                break;
            case 500:
                message = "Erreur interne du serveur. Réessayez plus tard.";
                break;
            default:
                message = `Erreur ${error.response.status}: ${error.response.statusText}`;
        }
    }

    showNotification(message, "error");
}

/**
 * Remet à zéro le formulaire après création réussie
 */
function resetForm() {
    $("#firstname, #lastname, #username, #email, #password").val('');
    $("#image").val('');
    $("#team").val('');
    $(".form-group").removeClass('has-error has-success');
}

/**
 * Charge et affiche les utilisateurs
 */
function loadAndDisplayUsers() {
    axios.get("/user")
        .then(function (response) {
            const users = response.data;
            const container = $("#usersContainer");
            container.empty();

            if (users.length === 0) {
                container.append("<p>Aucun utilisateur trouvé.</p>");
                return;
            }

            users.forEach(user => {
                const teamBadge = user.teamName ?
                    `<span class="team-badge">Équipe ${user.teamName}</span>` :
                    '<span class="no-team-badge">Aucune équipe</span>';

                const card = `
                    <div class="user-card">
                        <img src="data:image/png;base64,${user.image || ""}" alt="Photo de ${user.username}" />
                        <div class="user-info">
                            <strong>${user.firstname} ${user.lastname}</strong>
                            <span>@${user.username}</span>
                            <span>Email: ${user.email}</span>
                            ${teamBadge}
                        </div>
                    </div>
                `;
                container.append(card);
            });
        })
        .catch(function (error) {
            console.error(error);
            alert("Erreur lors de la récupération des utilisateurs.");
        });
}

/**
 * Utilitaires
 */
function getFieldLabel(field) {
    const labels = {
        firstname: 'Prénom',
        lastname: 'Nom',
        username: "Nom d'utilisateur",
        email: 'Email',
        password: 'Mot de passe'
    };
    return labels[field] || field;
}

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Valide une image
 */
function validateImage(file) {
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
    const maxSize = 5 * 1024 * 1024;

    if (!allowedTypes.includes(file.type)) {
        return false;
    }

    if (file.size > maxSize) {
        return false;
    }

    return true;
}


/**
 * Affiche une notification à l'utilisateur
 */
function showNotification(message, type = "info") {
    if (typeof toastr !== 'undefined') {
        toastr[type](message);
    } else {
        if (type === "success") {
            alert(`✅ ${message}`);
        } else {
            alert(`❌ ${message}`);
        }
    }
}

/**
 * Gère l'état du bouton (actif/inactif)
 */
function setButtonState(button, disabled, text) {
    button.prop('disabled', disabled);
    if (text) {
        button.text(text);
    }

    if (disabled) {
        button.addClass('loading');
    } else {
        button.removeClass('loading');
    }
}

/**
 * Convertit une image en base64
 */
function convertImageToBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();

        reader.onload = function(event) {
            const base64Image = event.target.result.split(",")[1];
            resolve(base64Image);
        };

        reader.onerror = function() {
            reject(new Error("Erreur lors de la lecture de l'image"));
        };

        reader.readAsDataURL(file);
    });
}