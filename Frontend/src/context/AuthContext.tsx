import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { apiService } from '../services/api';
import type { User, AuthResponse, LoginRequest, RegisterRequest } from '../types';
import { toast } from 'react-toastify';
import { getRolesFromToken } from '../utils/jwt';

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  isAdmin: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  register: (userData: RegisterRequest) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    // Check if user is already logged in
    const storedUser = localStorage.getItem('user');
    const token = localStorage.getItem('accessToken');
    
    if (storedUser && token) {
      try {
        const userData = JSON.parse(storedUser);
        setUser(userData);
        // Check if user is admin from token
        const roles = getRolesFromToken(token);
        setIsAdmin(roles.includes('ADMIN') || roles.includes('ROLE_ADMIN'));
        if (roles.length > 0) {
          userData.roles = roles;
          setUser(userData);
        }
      } catch (error) {
        console.error('Error parsing stored user:', error);
        localStorage.removeItem('user');
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
      }
    }
    setIsLoading(false);
  }, []);

  const login = async (credentials: LoginRequest) => {
    try {
      const response: AuthResponse = await apiService.login(credentials);
      
      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);
      
      const userData: User = {
        username: response.username,
        email: response.email,
        firstName: undefined, // Will be loaded from user details
        lastName: undefined, // Will be loaded from user details
      };
      
      localStorage.setItem('user', JSON.stringify(userData));
      setUser(userData);
      
      // Load user details to get firstName and lastName
      try {
        const userDetails = await apiService.getCurrentUser();
        const roles = getRolesFromToken(response.accessToken);
        const updatedUserData: User = {
          ...userData,
          firstName: userDetails.firstName,
          lastName: userDetails.lastName,
          roles: roles,
        };
        setIsAdmin(roles.includes('ADMIN') || roles.includes('ROLE_ADMIN'));
        localStorage.setItem('user', JSON.stringify(updatedUserData));
        setUser(updatedUserData);
      } catch (error) {
        // If fetching user details fails, continue with basic user data
        console.error('Failed to load user details:', error);
        const roles = getRolesFromToken(response.accessToken);
        setIsAdmin(roles.includes('ADMIN') || roles.includes('ROLE_ADMIN'));
      }
      
      toast.success('Login successful!');
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Login failed. Please try again.';
      toast.error(errorMessage);
      throw error;
    }
  };

  const register = async (userData: RegisterRequest) => {
    try {
      const response: AuthResponse = await apiService.register(userData);
      
      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);
      
      const newUser: User = {
        username: response.username,
        email: response.email,
        firstName: userData.firstName,
        lastName: userData.lastName,
      };
      
      localStorage.setItem('user', JSON.stringify(newUser));
      setUser(newUser);
      
      // Load full user details to ensure we have all information
      try {
        const userDetails = await apiService.getCurrentUser();
        const roles = getRolesFromToken(response.accessToken);
        const updatedUserData: User = {
          ...newUser,
          firstName: userDetails.firstName || newUser.firstName,
          lastName: userDetails.lastName || newUser.lastName,
          roles: roles,
        };
        setIsAdmin(roles.includes('ADMIN') || roles.includes('ROLE_ADMIN'));
        localStorage.setItem('user', JSON.stringify(updatedUserData));
        setUser(updatedUserData);
      } catch (error) {
        // If fetching user details fails, continue with registration data
        console.error('Failed to load user details:', error);
        const roles = getRolesFromToken(response.accessToken);
        setIsAdmin(roles.includes('ADMIN') || roles.includes('ROLE_ADMIN'));
      }
      
      toast.success('Registration successful!');
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Registration failed. Please try again.';
      toast.error(errorMessage);
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    setUser(null);
    setIsAdmin(false);
    toast.info('Logged out successfully');
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        isLoading,
        isAdmin,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

