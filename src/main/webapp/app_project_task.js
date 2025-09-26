// État global
let projects = [];
let tasks = [];
let users = [];
let teams = [];

// Initialisation
document.addEventListener('DOMContentLoaded', function() {
    loadInitialData();
    setupEventListeners();
});

async function loadInitialData() {
    try {
        // Charger les utilisateurs et équipes pour les sélects
        const [usersResponse, teamsResponse] = await Promise.all([
            axios.get('/user').catch(() => ({data: []})),
            axios.get('/team').catch(() => ({data: []}))
        ]);

        users = usersResponse.data || [];
        teams = teamsResponse.data || [];

        populateSelects();
        loadProjects();
        loadTasks();
    } catch (error) {
        console.error('Erreur lors du chargement initial:', error);
    }
}

function populateSelects() {
    // Populate project leader select
    const projectLeaderSelect = document.getElementById('projectLeader');
    projectLeaderSelect.innerHTML = '<option value="">Sélectionner un chef de projet</option>';

    users.forEach(user => {
        projectLeaderSelect.innerHTML += `<option value="${user.id}">${user.firstname} ${user.lastname}</option>`;
    });

    teams.forEach(team => {
        projectLeaderSelect.innerHTML += `<option value="${team.id}">${team.username} (Équipe)</option>`;
    });

    // Populate task assignee select
    const taskAssigneeSelect = document.getElementById('taskAssignee');
    taskAssigneeSelect.innerHTML = '<option value="">Sélectionner un utilisateur/équipe</option>';

    users.forEach(user => {
        taskAssigneeSelect.innerHTML += `<option value="${user.id}">${user.firstname} ${user.lastname}</option>`;
    });

    teams.forEach(team => {
        taskAssigneeSelect.innerHTML += `<option value="${team.id}">${team.username} (Équipe)</option>`;
    });
}

function setupEventListeners() {
    document.getElementById('projectForm').addEventListener('submit', handleProjectSubmit);
    document.getElementById('taskForm').addEventListener('submit', handleTaskSubmit);
}

async function handleProjectSubmit(e) {
    e.preventDefault();
    const submitBtn = e.target.querySelector('button[type="submit"]');
    const originalText = submitBtn.textContent;

    try {
        submitBtn.textContent = 'Création...';
        submitBtn.disabled = true;

        const formData = {
            name: document.getElementById('projectName').value,
            description: document.getElementById('projectDescription').value,
            startDate: document.getElementById('projectStartDate').value,
            endDate: document.getElementById('projectEndDate').value,
            projectLeaderId: document.getElementById('projectLeader').value || null
        };

        await axios.post('/project', formData);
        showNotification('Projet créé avec succès !', 'success');
        document.getElementById('projectForm').reset();
        loadProjects();
        populateTaskProjectSelect();

    } catch (error) {
        console.error(error);
        showNotification('Erreur lors de la création du projet', 'error');
    } finally {
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
    }
}

async function handleTaskSubmit(e) {
    e.preventDefault();
    const submitBtn = e.target.querySelector('button[type="submit"]');
    const originalText = submitBtn.textContent;

    try {
        submitBtn.textContent = 'Création...';
        submitBtn.disabled = true;

        const formData = {
            title: document.getElementById('taskTitle').value,
            description: document.getElementById('taskDescription').value,
            priority: document.getElementById('taskPriority').value,
            status: document.getElementById('taskStatus').value,
            dueDate: document.getElementById('taskDueDate').value,
            projectId: document.getElementById('taskProject').value || null,
            assignedId: document.getElementById('taskAssignee').value || null
        };

        await axios.post('/task', formData);
        showNotification('Tâche créée avec succès !', 'success');
        document.getElementById('taskForm').reset();
        loadTasks();

    } catch (error) {
        console.error(error);
        showNotification('Erreur lors de la création de la tâche', 'error');
    } finally {
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
    }
}

async function loadProjects() {
    try {
        const response = await axios.get('/project');
        projects = response.data || [];
        displayProjects();
        populateTaskProjectSelect();
    } catch (error) {
        console.error('Erreur lors du chargement des projets:', error);
    }
}

async function loadTasks() {
    try {
        const response = await axios.get('/task');
        tasks = response.data || [];
        displayTasks();
    } catch (error) {
        console.error('Erreur lors du chargement des tâches:', error);
    }
}

function populateTaskProjectSelect() {
    const taskProjectSelect = document.getElementById('taskProject');
    taskProjectSelect.innerHTML = '<option value="">Sélectionner un projet</option>';

    projects.forEach(project => {
        taskProjectSelect.innerHTML += `<option value="${project.id}">${project.name}</option>`;
    });
}

function displayProjects() {
    const container = document.getElementById('projectsContainer');

    if (!projects.length) {
        container.innerHTML = `
                    <div class="empty-state">
                        <div class="empty-state-icon">📁</div>
                        <p>Aucun projet pour le moment. Créez votre premier projet !</p>
                    </div>
                `;
        return;
    }

    container.innerHTML = projects.map(project => `
                <div class="item-card">
                    <div class="item-header">
                        <div>
                            <div class="item-title">${project.name}</div>
                            <div class="item-meta">
                                ${project.startDate ? `Début: ${formatDate(project.startDate)}` : ''}
                                ${project.endDate ? ` - Fin: ${formatDate(project.endDate)}` : ''}
                            </div>
                        </div>
                    </div>
                    ${project.description ? `<p>${project.description}</p>` : ''}
                    ${project.projectLeader ? `<div class="task-project">Chef: ${project.projectLeader.firstname || project.projectLeader.username}</div>` : ''}
                </div>
            `).join('');
}

function displayTasks() {
    const container = document.getElementById('tasksContainer');

    if (!tasks.length) {
        container.innerHTML = `
                    <div class="empty-state">
                        <div class="empty-state-icon">✓</div>
                        <p>Aucune tâche pour le moment. Créez votre première tâche !</p>
                    </div>
                `;
        return;
    }

    container.innerHTML = tasks.map(task => `
                <div class="item-card priority-${task.priority.toLowerCase()}">
                    <div class="item-header">
                        <div>
                            <div class="item-title">${task.title}</div>
                            <div class="item-meta">
                                ${task.dueDate ? `Échéance: ${formatDate(task.dueDate)}` : ''}
                                ${task.assigned ? ` - Assigné à: ${task.assigned.firstname || task.assigned.username}` : ''}
                            </div>
                        </div>
                        <span class="status-badge status-${task.status.toLowerCase().replace('_', '')}">${getStatusText(task.status)}</span>
                    </div>
                    ${task.description ? `<p>${task.description}</p>` : ''}
                    ${task.project ? `<div class="task-project">Projet: ${task.project.name}</div>` : ''}
                </div>
            `).join('');
}

function switchTab(tabName) {
    // Update tab buttons
    document.querySelectorAll('.tab').forEach(tab => tab.classList.remove('active'));
    event.target.classList.add('active');

    // Update tab content
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
    document.getElementById(tabName).classList.add('active');
}

function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString('fr-FR');
}

function getStatusText(status) {
    const statusMap = {
        'TODO': 'À faire',
        'IN_PROGRESS': 'En cours',
        'DONE': 'Terminé'
    };
    return statusMap[status] || status;
}

function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    document.body.appendChild(notification);

    setTimeout(() => {
        notification.remove();
    }, 4000);
}