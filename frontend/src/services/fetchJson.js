// src/utils/fetchJson.js

const API = process.env.REACT_APP_API_URL || 'https://localhost:8443';

// Read the JWT out of localStorage
function getJwtToken() {
    return localStorage.getItem('jwt');
}

// Core request function: sends JWT on every call, handles JSON bodies and parsing
async function request(path, { method = 'GET', body } = {}) {
    const token = getJwtToken();

    const headers = {
        'Accept': 'application/json',
        ...(body != null ? { 'Content-Type': 'application/json' } : {}),
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
    };

    const res = await fetch(`${API}${path}`, {
        method,
        headers,
        body: body != null ? JSON.stringify(body) : undefined,
    });

    if (!res.ok) {
        throw new Error(`Error ${res.status} ${method} ${path}`);
    }

    // If there's no JSON to parse (e.g. DELETE returns 204), just return
    const ct = res.headers.get('Content-Type') || '';
    if (!ct.includes('application/json')) {
        return;
    }

    return res.json();
}

// Public helpers
export const fetchJson = (path) => request(path);
export const sendJson = (path, { method, body }) => request(path, { method, body });
