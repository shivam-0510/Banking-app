# Account Service

## Overview
The Account Service is responsible for managing bank accounts and transactions in the Banking Application. It provides APIs for creating and managing accounts, as well as processing financial transactions like deposits, withdrawals, and transfers.

## Features
- Account Management
  - Create different types of accounts (Savings, Checking, Credit, Loan, Investment)
  - Retrieve account details
  - Update account status
  - Query accounts by user
- Transaction Processing
  - Deposit funds
  - Withdraw funds
  - Transfer funds between accounts
  - Retrieve transaction history
- Security
  - JWT-based authentication
  - Role-based access control
  - Account ownership validation

## Technical Details
- Built with Spring Boot 3.5.4
- Uses Spring Security for authentication and authorization
- Implements JWT for secure API access
- PostgreSQL database for data persistence
- Spring Data JPA for database access
- Swagger/OpenAPI for API documentation

## API Endpoints

### Account APIs
- `POST /api/accounts` - Create a new account
- `GET /api/accounts/{accountNumber}` - Get account by number
- `GET /api/accounts/user/{userId}` - Get accounts by user ID
- `GET /api/accounts/user/{userId}/paginated` - Get paginated accounts by user ID
- `PUT /api/accounts/{accountNumber}/status` - Update account status
- `GET /api/accounts/my-accounts` - Get current user's accounts

### Transaction APIs
- `POST /api/transactions/deposit` - Deposit funds
- `POST /api/transactions/withdraw` - Withdraw funds
- `POST /api/transactions/transfer` - Transfer funds
- `GET /api/transactions/{transactionId}` - Get transaction by ID
- `GET /api/transactions/account/{accountNumber}` - Get transactions by account
- `GET /api/transactions/account/{accountNumber}/paginated` - Get paginated transactions by account
- `GET /api/transactions/account/{accountNumber}/date-range` - Get transactions by date range

## Security Configuration
The service uses JWT tokens for authentication. The security configuration includes:
- JWT token validation
- Role-based access control for endpoints
- Method-level security for service methods

## Running the Service
1. Ensure PostgreSQL is running and the `banking_account_db` database is created
2. Configure the application properties if needed
3. Run the service using Maven: `mvn spring-boot:run`
4. Access the API documentation at: `http://localhost:8083/swagger-ui.html`

## Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Cloud Netflix Eureka Client
- PostgreSQL Driver
- JJWT (JSON Web Token)
- Lombok
- Springdoc OpenAPI
