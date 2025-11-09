import axios, { AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import { toast } from 'react-toastify';
import type {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
  Account,
  AccountCreationRequest,
  Transaction,
  DepositRequest,
  WithdrawRequest,
  TransferRequest,
  ApiResponse,
  UserResponse,
  UserUpdateRequest,
  UserCreationRequest,
} from '../types';

// Direct service URLs
const AUTH_SERVICE_URL = import.meta.env.VITE_AUTH_SERVICE_URL || 'http://localhost:8081/api';
const ACCOUNT_SERVICE_URL = import.meta.env.VITE_ACCOUNT_SERVICE_URL || 'http://localhost:8082/api';

// Create axios instance factory
const createAxiosInstance = (baseURL: string, requireAuth: boolean = true): AxiosInstance => {
  const instance = axios.create({
    baseURL,
    headers: {
      'Content-Type': 'application/json',
    },
    withCredentials: true, // Important for CORS with credentials
  });

  // Request interceptor to add auth token
  if (requireAuth) {
    instance.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        const token = localStorage.getItem('accessToken');
        if (token && config.headers) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );
  }

  // Response interceptor for error handling
  instance.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
        window.location.href = '/login';
        toast.error('Session expired. Please login again.');
      } else if (error.response?.data?.message) {
        toast.error(error.response.data.message);
      } else {
        toast.error('An error occurred. Please try again.');
      }
      return Promise.reject(error);
    }
  );

  return instance;
};

class ApiService {
  private authApi: AxiosInstance;
  private accountApi: AxiosInstance;

  constructor() {
    // Auth service - create two instances: one for public endpoints, one for authenticated
    this.authApi = createAxiosInstance(AUTH_SERVICE_URL, true); // User endpoints require auth
    // Account service requires authentication
    this.accountApi = createAxiosInstance(ACCOUNT_SERVICE_URL, true);
  }

  // Public auth endpoints (no token required)
  private get publicAuthApi() {
    return createAxiosInstance(AUTH_SERVICE_URL, false);
  }

  // Auth endpoints (public - no token required)
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response = await this.publicAuthApi.post<AuthResponse>('/auth/login', credentials);
    return response.data;
  }

  async register(userData: RegisterRequest): Promise<AuthResponse> {
    const response = await this.publicAuthApi.post<AuthResponse>('/auth/register', userData);
    return response.data;
  }

  // Account endpoints
  async getMyAccounts(): Promise<Account[]> {
    const response = await this.accountApi.get<ApiResponse<Account[]>>('/accounts/my-accounts');
    return response.data.data || [];
  }

  async getAccount(accountNumber: string): Promise<Account> {
    const response = await this.accountApi.get<ApiResponse<Account>>(`/accounts/${accountNumber}`);
    return response.data.data!;
  }

  async createAccount(accountData: AccountCreationRequest): Promise<Account> {
    const response = await this.accountApi.post<ApiResponse<Account>>('/accounts/my-account', accountData);
    return response.data.data!;
  }

  async getAccountTypes(): Promise<string[]> {
    const response = await this.accountApi.get<ApiResponse<string[]>>('/accounts/public/account-types');
    return response.data.data || [];
  }

  // Transaction endpoints
  async deposit(depositData: DepositRequest): Promise<Transaction> {
    const response = await this.accountApi.post<ApiResponse<Transaction>>('/transactions/deposit', depositData);
    return response.data.data!;
  }

  async withdraw(withdrawData: WithdrawRequest): Promise<Transaction> {
    const response = await this.accountApi.post<ApiResponse<Transaction>>('/transactions/withdraw', withdrawData);
    return response.data.data!;
  }

  async transfer(transferData: TransferRequest): Promise<Transaction> {
    const response = await this.accountApi.post<ApiResponse<Transaction>>('/transactions/transfer', transferData);
    return response.data.data!;
  }

  async getAccountTransactions(accountNumber: string): Promise<Transaction[]> {
    const response = await this.accountApi.get<ApiResponse<Transaction[]>>(
      `/transactions/account/${accountNumber}`
    );
    return response.data.data || [];
  }

  async getTransaction(transactionId: string): Promise<Transaction> {
    const response = await this.accountApi.get<ApiResponse<Transaction>>(`/transactions/${transactionId}`);
    return response.data.data!;
  }

  // User endpoints
  async getCurrentUser(): Promise<UserResponse> {
    const response = await this.authApi.get<UserResponse>('/users/me');
    return response.data;
  }

  async getUserByUsername(username: string): Promise<UserResponse> {
    const response = await this.authApi.get<UserResponse>(`/users/${username}`);
    return response.data;
  }

  async updateUser(username: string, userData: UserUpdateRequest): Promise<UserResponse> {
    const response = await this.authApi.put<UserResponse>(`/users/${username}`, userData);
    return response.data;
  }

  // Admin endpoints
  async getAllUsers(): Promise<UserResponse[]> {
    const response = await this.authApi.get<UserResponse[]>('/users');
    return response.data;
  }

  async createUser(userData: UserCreationRequest): Promise<UserResponse> {
    const response = await this.authApi.post<UserResponse>('/users', userData);
    return response.data;
  }

  async deleteUser(username: string): Promise<void> {
    await this.authApi.delete(`/users/${username}`);
  }

  async deactivateUser(username: string): Promise<void> {
    await this.authApi.patch(`/users/${username}/deactivate`);
  }

  async activateUser(username: string): Promise<void> {
    await this.authApi.patch(`/users/${username}/activate`);
  }

  // Admin account endpoints
  async getAllAccounts(): Promise<Account[]> {
    const response = await this.accountApi.get<ApiResponse<Account[]>>('/accounts/all');
    return response.data.data || [];
  }

  async updateAccountStatus(accountNumber: string, isActive: boolean): Promise<Account> {
    const response = await this.accountApi.put<ApiResponse<Account>>(
      `/accounts/${accountNumber}/status?isActive=${isActive}`
    );
    return response.data.data!;
  }
}

export const apiService = new ApiService();

