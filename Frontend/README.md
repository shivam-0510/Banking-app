# Banking Application - Frontend

A modern React + TypeScript frontend for the Banking Application microservices backend.

## Features

- 🔐 User Authentication (Login/Register)
- 💳 Account Management (Create, View, List)
- 💰 Transaction Management (Deposit, Withdraw, Transfer)
- 📊 Dashboard with account overview
- 📱 Responsive design with Tailwind CSS
- 🔒 Protected routes with JWT authentication
- ⚡ Fast development with Vite

## Tech Stack

- **React 18** - UI library
- **TypeScript** - Type safety
- **React Router** - Navigation
- **Axios** - HTTP client
- **Tailwind CSS** - Styling
- **React Toastify** - Notifications
- **Lucide React** - Icons
- **Vite** - Build tool

## Getting Started

### Prerequisites

- Node.js 18+ and npm/yarn
- Backend services running (API Gateway on port 8080)

### Installation

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:3000`

### Build for Production

```bash
npm run build
```

The production build will be in the `dist` directory.

## Project Structure

```
Frontend/
├── src/
│   ├── components/      # Reusable components
│   │   ├── Layout.tsx
│   │   └── ProtectedRoute.tsx
│   ├── context/        # React context providers
│   │   └── AuthContext.tsx
│   ├── pages/          # Page components
│   │   ├── Login.tsx
│   │   ├── Register.tsx
│   │   ├── Dashboard.tsx
│   │   ├── Accounts.tsx
│   │   ├── CreateAccount.tsx
│   │   ├── AccountDetail.tsx
│   │   ├── Transactions.tsx
│   │   ├── Deposit.tsx
│   │   ├── Withdraw.tsx
│   │   └── Transfer.tsx
│   ├── services/       # API services
│   │   └── api.ts
│   ├── types/          # TypeScript types
│   │   └── index.ts
│   ├── App.tsx         # Main app component
│   ├── main.tsx        # Entry point
│   └── index.css       # Global styles
├── package.json
├── tsconfig.json
├── vite.config.ts
└── tailwind.config.js
```

## API Configuration

The frontend is configured to communicate directly with the microservices:
- **Auth Service**: `http://localhost:8081/api` (for login/register)
- **Account Service**: `http://localhost:8082/api` (for accounts and transactions)

This is configured in:
- `src/services/api.ts` - API service configuration with separate axios instances for each service

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

## Features Overview

### Authentication
- User registration with validation
- Login with username/email
- JWT token management
- Automatic token refresh
- Protected routes

### Account Management
- View all accounts
- Create new accounts (Savings, Checking, Credit, Loan, Investment)
- View account details
- Account balance and status

### Transactions
- Deposit funds
- Withdraw funds
- Transfer between accounts
- View transaction history
- Filter transactions by account

### Dashboard
- Account overview
- Total balance summary
- Quick access to accounts
- Account statistics

## Environment Variables

You can create a `.env` file to customize the service URLs:

```
VITE_AUTH_SERVICE_URL=http://localhost:8081/api
VITE_ACCOUNT_SERVICE_URL=http://localhost:8082/api
```

## Notes

- The frontend communicates directly with microservices (bypassing API Gateway)
- Auth Service should be running on port 8081
- Account Service should be running on port 8082
- CORS is configured in both services to allow requests from `http://localhost:3000`
- JWT tokens are stored in localStorage
- The app automatically redirects to login if the token expires
- `withCredentials: true` is set in axios to support CORS with credentials

