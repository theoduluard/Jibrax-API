async function loadMenu() {
    try {
        const response = await fetch('/front/slide_menu.html');
        const menuHtml = await response.text();

        const main = document.querySelector('main.card');
        if (main) {
            main.insertAdjacentHTML('afterbegin', menuHtml);
            initMenuToggle();
        }
    } catch (error) {
        console.error('Erreur lors du chargement du menu:', error);
    }
}

function initMenuToggle() {
    const menuToggle = document.getElementById('menuToggle');
    const sidebar = document.getElementById('sidebar');

    if (menuToggle && sidebar) {
        menuToggle.addEventListener('click', function() {
            sidebar.classList.toggle('active');
            menuToggle.classList.toggle('active');
        });

        document.addEventListener('click', function(e) {
            if (!menuToggle.contains(e.target) && !sidebar.contains(e.target)) {
                sidebar.classList.remove('active');
                menuToggle.classList.remove('active');
            }
        });
    }
}

document.addEventListener('DOMContentLoaded', loadMenu);