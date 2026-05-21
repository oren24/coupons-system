import axios from "axios";

// Same base as api.ts
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const http = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

// Attach token from localStorage if present (token stored as 'Bearer <token>')
const storedToken = typeof window !== 'undefined' ? localStorage.getItem('token') : null;
if (storedToken) {
    http.defaults.headers.common['Authorization'] = storedToken;
}

// Response interceptor: handle auth errors globally
http.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.status === 401) {
            // Clear token and optionally redirect to login
            try {
                localStorage.removeItem('token');
            } catch (e) {
                // ignore
            }
            // You may want to programmatically navigate to login page here
            // window.location.href = '/';
        }
        return Promise.reject(error);
    }
);

export default http;

