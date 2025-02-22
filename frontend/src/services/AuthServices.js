import axios from 'axios';

export const AuthServices = {
  logout() {
    
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('roles');

  },

  isTokenExpired() {
    const token = localStorage.getItem('token');
    if (!token) return true;
  
    const payload = JSON.parse(atob(token.split('.')[1])); // Decodifica el JWT
    const expirationTime = payload.exp * 1000; // Convertir a milisegundos
    return expirationTime < Date.now();
  }

};
export default { AuthServices: AuthServices };