// API Configuration
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export const API_ENDPOINTS = {
  USERS_LOGIN: `${API_BASE_URL}/users/login`,
  USERS_REGISTER: `${API_BASE_URL}/users`,
  USERS_UPDATE: `${API_BASE_URL}/users`,
  USERS_LOGOUT: `${API_BASE_URL}/users/logout`,
  USERS_GET: `${API_BASE_URL}/users`,
  COUPONS_GET: `${API_BASE_URL}/coupons`,
  COUPONS_CREATE: `${API_BASE_URL}/coupons`,
  COUPONS_UPDATE: `${API_BASE_URL}/coupons`,
  COUPONS_DELETE: `${API_BASE_URL}/coupons`,
  COMPANIES_GET: `${API_BASE_URL}/companies`,
  CATEGORIES_GET: `${API_BASE_URL}/categories`,
  PURCHASES_GET: `${API_BASE_URL}/purchases`,
};

export default API_ENDPOINTS;
