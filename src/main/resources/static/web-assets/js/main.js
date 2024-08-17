function activeMenu() {
    let path = window.location.pathname;

    const menuItems = document.querySelectorAll('#main-menu, .nav-menu, .nav-item a');
    menuItems.forEach(function (menu) {
        const href = menu.getAttribute('href');

        if (href === '/') {
            // Nếu path là "/" thì chỉ active cho menu có href là "/"
            if (path === href) {
                menu.classList.add('active-menu');
            }
        } else {
            // Với các menu khác, kiểm tra xem path có bắt đầu bằng href không
            if (path.startsWith(href)) {
                menu.classList.add('active-menu');
            }
        }
    });
}

activeMenu();



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

