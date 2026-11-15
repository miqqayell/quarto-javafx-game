// script.js
import { usersJson } from '../db/users.js';
const users = JSON.parse(usersJson);

// Element references
const authLi       = document.getElementById('authLi');
const authModal    = document.getElementById('authModal');
const closeBtn     = document.querySelector('.close[data-close="authModal"]');
const showLogin    = document.getElementById('showLogin');
const showRegister = document.getElementById('showRegister');
const loginForm    = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');

// === NAV UPDATE & LOGOUT ===
// === NAV UPDATE & LOGOUT ===
// === NAV UPDATE & LOGOUT ===
function updateNav() {
    const user = localStorage.getItem('quartoUser');
    if (user) {
        authLi.innerHTML = `
            <a href="../html/profile.html" class="nav-user">Hello, ${user}</a>
            <button class="btn logout-btn">Logout</button>
        `;
        document.querySelector('.logout-btn').addEventListener('click', () => {
            localStorage.removeItem('quartoUser');
            updateNav();
        });
    } else {
        authLi.innerHTML = `<a href="#" id="authBtn">Sign In / Up</a>`;
        document.getElementById('authBtn').addEventListener('click', openAuthModal);
    }
}



// === MODAL OPEN/CLOSE ===
function openAuthModal(e) {
    e.preventDefault();
    authModal.classList.add('show');
}
function closeModal() {
    authModal.classList.remove('show');
}
authLi.addEventListener('click', e => {
    if (e.target.id === 'authBtn') openAuthModal(e);
});
closeBtn.addEventListener('click', closeModal);
authModal.addEventListener('click', e => {
    if (e.target === authModal) closeModal();
});

// === TAB TOGGLE ===
showLogin.addEventListener('click', () => {
    showLogin.classList.add('active');
    showRegister.classList.remove('active');
    loginForm.style.display    = 'flex';
    registerForm.style.display = 'none';
});
showRegister.addEventListener('click', () => {
    showRegister.classList.add('active');
    showLogin.classList.remove('active');
    registerForm.style.display = 'flex';
    loginForm.style.display    = 'none';
});

// === LOGIN HANDLER ===
loginForm.addEventListener('submit', e => {
    e.preventDefault();
    const username = document.getElementById('loginUsername').value.trim();
    const password = document.getElementById('loginPassword').value.trim();
    const user = users.find(u => u.username === username && u.password === password);

   if (user) {
        localStorage.setItem('quartoUser', user.username);
        localStorage.setItem('quartoRole', user.role);
        updateNav();
        closeModal();
        alert(`Welcome, ${user.username}!`);
    } else {
        alert('Invalid username or password');
    }
});

// === REGISTER HANDLER ===
registerForm.addEventListener('submit', e => {
    e.preventDefault();
    const username = document.getElementById('regUsername').value.trim();
    const password = document.getElementById('regPassword').value.trim();
    const exists = users.some(u => u.username === username);

    if (exists) {
        alert('Username already exists');
    } else {
        users.push({ username, password, role: 'player' });
        localStorage.setItem('quartoUser', username);
        localStorage.setItem('quartoRole', 'player');
        updateNav();
        closeModal();
        alert('Registration successful!');
    }
});



// === INITIALIZE NAV ON LOAD ===
updateNav();
