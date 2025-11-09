# Banking Application Microservices Platform

A comprehensive full-stack banking application built with microservices architecture, featuring user authentication, account management, and administrative capabilities.

## 🏗️ Architecture Overview

This application follows a microservices architecture pattern with the following components:

### Backend Services
- **API Gateway** - Routes requests and handles authentication
- **Auth Service** - Manages user authentication and authorization
- **Account Service** - Handles account operations and transactions
- **Eureka Server** - Service discovery and registration

### Frontend
- **React Application** - Modern, responsive user interface
- **Admin Dashboard** - Comprehensive administrative interface
- **User Portal** - Customer-facing banking interface

## 🛠️ Technology Stack

### Backend
- **Java 21** - Primary programming language
- **Spring Boot 3.3.0** - Microservices framework
- **Spring Cloud** - Microservices patterns and service discovery
- **Spring Security** - Authentication and authorization
- **PostgreSQL** - Primary database
- **Maven** - Build and dependency management
- **JWT** - Token-based authentication

### Frontend
- **React 18** - UI framework
- **TypeScript** - Type-safe JavaScript
- **Vite** - Build tool and development server
- **Tailwind CSS** - Styling framework
- **Axios** - HTTP client
- **React Router** - Client-side routing

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21** or higher
- **Node.js 18** or higher
- **PostgreSQL 14** or higher
- **Maven 3.8** or higher
- **Docker** (optional, for containerized deployment)

## 🚀 Quick Start

### 1. Database Setup

Create the required databases:

```bash
# Connect to PostgreSQL
psql -U postgres

# Create databases
CREATE DATABASE banking_auth_db;
CREATE DATABASE banking_account_db;

# Exit PostgreSQL
\q
```

### 2. Backend Setup

Navigate to the backend directory and build all services:

```bash
cd Backend
mvn clean install
```

Start the services in the following order:

```bash
# Start Eureka Server (Service Discovery)
cd eureka-server
mvn spring-boot:run

# Start Auth Service (Port 8081)
cd ../auth-service
mvn spring-boot:run

# Start Account Service (Port 8082)
cd ../account-service
mvn spring-boot:run

# Start API Gateway (Port 8080)
cd ../api-gateway
mvn spring-boot:run
```

### 3. Frontend Setup

Navigate to the frontend directory:

```bash
cd ../Frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

The frontend will be available at `http://localhost:3000`

## 🔐 Default Access

### User Registration
- Navigate to `http://localhost:3000/register`
- Create a new account with your email and password

### Admin Access
To access the admin dashboard, you need to assign yourself the ADMIN role:

1. Follow the instructions in [ADMIN_ACCESS_GUIDE.md](ADMIN_ACCESS_GUIDE.md)
2. Login with your admin-enabled account
3. Access the admin dashboard at `http://localhost:3000/admin`

## 📁 Project Structure

```
Banking-app/
├── Backend/
│   ├── eureka-server/          # Service Discovery
│   ├── api-gateway/            # API Gateway
│   ├── auth-service/           # Authentication Service
│   ├── account-service/        # Account Management Service
│   └── pom.xml                 # Parent POM
├── Frontend/
│   ├── src/
│   │   ├── components/         # React Components
│   │   ├── pages/             # Page Components
│   │   ├── services/          # API Services
│   │   ├── context/           # React Context
│   │   ├── types/             # TypeScript Types
│   │   └── utils/             # Utility Functions
│   ├── package.json
│   └── vite.config.ts
├── logs/                       # Application Logs
└── README.md
```

## 🔧 Configuration

### Backend Configuration
Each service has its own `application.properties` file located in:
```
Backend/{service-name}/src/main/resources/application.properties
```

Key configurations:
- **Database connection** - PostgreSQL settings
- **Port assignments** - Each service runs on different ports
- **Eureka registration** - Service discovery configuration
- **JWT settings** - Authentication token configuration

### Frontend Configuration
Frontend configuration is in:
```
Frontend/vite.config.ts
```

API endpoints are configured in:
```
Frontend/src/services/api.ts
```

## 🧪 Testing

### Backend Testing
```bash
cd Backend
mvn test
```

### Frontend Testing
```bash
cd Frontend
npm run test
```

## 📊 Monitoring & Logs

Application logs are stored in the `logs/` directory:
- `account-service.log` - Account service logs
- `account-service-error.log` - Account service error logs
- `auth-service.log` - Auth service logs
- `auth-service-error.log` - Auth service error logs

## 🔒 Security Features

- **JWT-based authentication** - Secure token-based authentication
- **Role-based authorization** - USER, ADMIN, and MANAGER roles
- **Password encryption** - BCrypt password hashing
- **CORS protection** - Cross-origin request protection
- **Input validation** - Server-side validation
- **SQL injection prevention** - Parameterized queries

## 🎯 Features

### User Features
- User registration and login
- Account creation and management
- Balance inquiry
- Transaction history
- Profile management

### Admin Features
- User management (create, update, delete)
- Account management (activate/deactivate)
- System statistics and analytics
- Role assignment
- Account search and filtering

## 🐛 Troubleshooting

### Common Issues

1. **Services not starting**: Check port availability and database connection
2. **Frontend build errors**: Ensure Node.js version compatibility
3. **Database connection issues**: Verify PostgreSQL is running and databases exist
4. **Authentication failures**: Check JWT secret configuration
5. **CORS errors**: Verify API Gateway configuration

### Debug Mode
Enable debug logging by adding to application properties:
```properties
logging.level.root=DEBUG
logging.level.com.banking=DEBUG
```

## 🚀 Deployment

### Production Build

Backend:
```bash
cd Backend
mvn clean package
java -jar {service-name}/target/{service-name}-1.0.0.jar
```

Frontend:
```bash
cd Frontend
npm run build
npm run preview
```

### Docker Deployment
Docker configuration files are available for containerized deployment.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new features
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For support and questions:
- Check the troubleshooting section
- Review application logs
- Examine the GitHub issues
- Contact the development team

---

**Note**: This is a development project for educational purposes. For production use, additional security measures, monitoring, and scalability considerations should be implemented.
