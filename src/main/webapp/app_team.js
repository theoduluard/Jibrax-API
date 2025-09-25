$(document).ready(function () {
    $("#showTeams").on("click", loadAndDisplayTeams);
    $("#validTeam").on("click", handleTeamCreation);
});

/**
 * Charge et affiche les équipes
 */
async function loadAndDisplayTeams() {
    const container = $("#teamsContainer");
    container.empty().html("Chargement des équipes...");

    try {
        const response = await axios.get("/team");
        const teams = response.data;

        if (!teams || teams.length === 0) {
            container.html("<p>Aucune équipe trouvée.</p>");
            return;
        }

        container.empty();

        teams.forEach(team => {
            const membersCount = team.teamMembers ? team.teamMembers.length : 0;

            const card = `
                <div class="team-card">
                    <img src="data:image/png;base64,${team.image || ""}" 
                         alt="Logo de ${team.username}" />
                    <div class="team-info">
                        <strong>${team.username}</strong>
                        <p>Membres : ${membersCount}</p>
                    </div>
                </div>
            `;

            container.append(card);
        });
    } catch (error) {
        console.error("❌ Erreur lors du chargement des équipes :", error);
        container.html("<p>Erreur lors du chargement des équipes.</p>");
    }
}

/**
 * Création d'une équipe
 */
async function handleTeamCreation(e) {
    e.preventDefault();
    const button = $("#validTeam");
    const originalText = button.text();

    if (!validateTeamForm()) {
        return;
    }

    try {
        setButtonState(button, true, "Création...");
        const teamData = await collectTeamFormData();
        const response = await axios.post('/team', teamData);
        console.log("✅ Équipe créé :", response.data);
        showNotification("Équipe créée avec succès !", "success");
        resetTeamForm();

    } catch (error) {
        console.error("❌ Erreur lors de la création :", error);
        handleTeamCreationError(error);
    } finally {
        setButtonState(button, false, originalText);
    }
}

/**
 * Validation du formulaire équipe
 */
function validateTeamForm() {
    const teamName = $("#teamName").val().trim();
    if (!teamName) {
        showNotification("Le nom de l'équipe est requis.", "error");
        return false;
    }
    return true;
}

/**
 * Collecte et préparation des données équipe
 */
async function collectTeamFormData() {
    const teamName = $("#teamName").val().trim();
    const teamData = {
        username: teamName,
        image: null
    };

    const file = $("#image")[0]?.files[0];
    if (file) {
        if (!validateImage(file)) {
            throw new Error("Format d'image invalide ou taille trop importante (max 5MB)");
        }
        teamData.image = await convertImageToBase64(file);
    }

    return teamData;
}

/**
 * Gestion des erreurs lors de la création d'équipe
 */
function handleTeamCreationError(error) {
    let message = "Erreur lors de la création de l'équipe";

    if (error.message && !error.response) {
        message = error.message;
    } else if (error.response?.data?.message) {
        message = error.response.data.message;
    } else if (error.response?.status) {
        switch (error.response.status) {
            case 400:
                message = "Nom d'équipe invalide.";
                break;
            case 409:
                message = "Ce nom d'équipe est déjà utilisé.";
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
 * Remet à zéro le formulaire équipe
 */
function resetTeamForm() {
    $("#teamName").val('');
    $("#teamImage").val('');
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