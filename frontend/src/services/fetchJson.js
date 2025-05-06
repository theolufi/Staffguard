// src/utils/fetchJson.js

const API = process.env.REACT_APP_API_URL || 'https://localhost:8443';

// Read the JWT out of localStorage
function getJwtToken() {
    return localStorage.getItem('jwt');
}

// Helper to GET JSON, sending the Bearer token automatically
export async function fetchJson(path) {
    const token = getJwtToken();
    const res = await fetch(`${API}${path}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            ...(token ? { 'Authorization': `Bearer ${token}` } : {})
        }
    });
    if (!res.ok) {
        throw new Error(`Error ${res.status} fetching ${path}`);
    }
    return res.json();
}

// Helper to send any JSON body (POST/PATCH/DELETE), sending Bearer token
export async function sendJson(path, { method, body }) {
    const token = getJwtToken();
    const res = await fetch(`${API}${path}`, {
        method,
        headers: {
            'Content-Type': 'application/json',
            ...(token ? { 'Authorization': `Bearer ${token}` } : {})
        },
        body: JSON.stringify(body)
    });
    if (!res.ok) {
        throw new Error(`Error ${res.status} ${method} ${path}`);
    }
    // parse JSON only if present
    return res.headers.get('Content-Type')?.includes('application/json')
        ? res.json()
        : undefined;
}
