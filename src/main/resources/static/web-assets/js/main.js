
function activeMenu() {
    let path = window.location.pathname;

    const menuItems = document.querySelectorAll('#main-menu, .nav-menu, .nav-item a')
    menuItems.forEach(function (menu) {
        if (menu.getAttribute('href') === path) {
            menu.classList.add('active-menu')
        }
    })
}

activeMenu()

// sticky
const header = document.getElementById('header');
const mainMenu = document.getElementById('main-menu');
const stickyOffset = mainMenu.offsetTop;

function stickyNav() {
    if (window.pageYOffset >= stickyOffset) {
        mainMenu.classList.add('sticky');
    } else {
        mainMenu.classList.remove('sticky');
    }
}

window.onscroll = function() {
    stickyNav();
};

