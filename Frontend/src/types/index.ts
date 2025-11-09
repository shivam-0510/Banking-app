export interface User {
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  roles?: string[];
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  username: string;
  email: string;
  success: boolean;
}

export interface LoginRequest {
  usernameOrEmail: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName?: string;
  lastName?: string;
}

export interface Account {
  id: string;
  accountNumber: string;
  accountType: string;
  balance: number;
  currency: string;
  status?: string; // Deprecated - use isActive instead
  active: boolean;
  userId: string;
  createdAt?: string;
  updatedAt?: string;
  dailyTransactionLimit?: number;
  dailyWithdrawalLimit?: number;
  interestRate?: number;
  overdraftLimit?: number;
  minimumBalance?: number;
}

export interface AccountCreationRequest {
  accountType: string;
  initialDeposit: number;
  currency: string;
  dailyTransactionLimit?: number;
  dailyWithdrawalLimit?: number;
  interestRate?: number;
  overdraftLimit?: number;
  minimumBalance?: number;
}

export interface Transaction {
  id: string;
  transactionId: string;
  accountId: string;
  accountNumber: string;
  amount: number;
  transactionType: string;
  status: string;
  sourceAccountNumber?: string;
  destinationAccountNumber?: string;
  referenceNumber?: string;
  description?: string;
  transactionDate: string;
  balanceAfterTransaction: number;
}

export interface DepositRequest {
  accountNumber: string;
  amount: number;
  description?: string;
  referenceNumber?: string;
}

export interface WithdrawRequest {
  accountNumber: string;
  amount: number;
  description?: string;
  referenceNumber?: string;
}

export interface TransferRequest {
  sourceAccountNumber: string;
  destinationAccountNumber: string;
  amount: number;
  description?: string;
  referenceNumber?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  timestamp?: string;
}

export interface UserResponse {
  userId: string;
  firstName?: string;
  lastName?: string;
  email: string;
  phoneNumber?: string;
  dateOfBirth?: string;
  address?: string;
  city?: string;
  state?: string;
  zipCode?: string;
  country?: string;
  profilePicture?: string;
  createdAt?: string;
  updatedAt?: string;
  active: boolean;
  preferences?: UserPreferencesResponse;
}

export interface UserUpdateRequest {
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  address?: string;
  city?: string;
  state?: string;
  zipCode?: string;
  country?: string;
  profilePicture?: string;
}

export interface UserPreferencesResponse {
  notificationEmail?: boolean;
  notificationSms?: boolean;
  notificationPush?: boolean;
  twoFactorAuth?: boolean;
}

export interface UserCreationRequest {
  username: string;
  email: string;
  password: string;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  roles?: string[];
}

