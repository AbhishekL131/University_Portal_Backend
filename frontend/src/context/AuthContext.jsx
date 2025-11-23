import { createContext, useState, useEffect, useContext } from 'react';
import api from '../api/axios';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('token'));
    const [role, setRole] = useState(localStorage.getItem('role'));

    useEffect(() => {
        if (token) {
            // In a real app, you might validate the token with the backend here
            // For now, we'll just assume it's valid if present
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        } else {
            delete api.defaults.headers.common['Authorization'];
        }
    }, [token]);

    const login = async (username, password, selectedRole) => {
        try {
            const endpoint = selectedRole === 'Admin' ? '/Login/adminLogin' : '/Login/facultyLogin';
            const payload = { userName: username, password };

            const response = await api.post(endpoint, payload);
            const jwt = response.data; // The backend returns the JWT string directly

            setToken(jwt);
            setRole(selectedRole);
            localStorage.setItem('token', jwt);
            localStorage.setItem('role', selectedRole);

            // Decode token or set user info if available. 
            // Since backend only returns JWT, we'll store basic info.
            setUser({ username, role: selectedRole });

            return true;
        } catch (error) {
            console.error("Login failed", error);
            throw error;
        }
    };

    const logout = () => {
        setUser(null);
        setToken(null);
        setRole(null);
        localStorage.removeItem('token');
        localStorage.removeItem('role');
    };

    return (
        <AuthContext.Provider value={{ user, token, role, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
