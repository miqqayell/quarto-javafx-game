// profile.js
import { gamesJson } from '../db/db.js';

const games = JSON.parse(gamesJson);
const username = localStorage.getItem('quartoUser');
if (!username) window.location.href = '../index.html';

document.getElementById('profileName').textContent = username;

// 1) Filter & sort by date descending
const userGames = games
    .filter(g => g.username === username)
    .sort((a, b) => new Date(b.start_date) - new Date(a.start_date));

// 2) Keep only the first 10
const recentGames = userGames.slice(0, 10);

// 3) Stats still use the full set
const played = userGames.length;
const wins   = userGames.filter(g => g.is_win).length;
const losses = played - wins;
const winPct = played ? Math.round((wins / played) * 100) : 0;

document.getElementById('statPlayed').textContent = played;
document.getElementById('statWins').textContent   = wins;
document.getElementById('statLosses').textContent = losses;
document.getElementById('statWinPct').textContent = winPct + '%';

// 4) Render just those 10 rows
const tbody = document.getElementById('gamesTableBody');
tbody.innerHTML = '';  // clear any existing rows
recentGames.forEach(g => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
    <td>${g.id}</td>
    <td>${new Date(g.start_date).toLocaleString()}</td>
    <td>${g.game_outcome}</td>
    <td>${g.total_moves}</td>
    <td>${g.avg_move_duration}</td>
  `;
    tbody.appendChild(tr);
});
