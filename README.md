# SCM - Smart Contact Management System

SCM is a Spring Boot-based application that allows users to register, add contacts, and chat with their contacts in a seamless and efficient manner. This system enables secure communication and contact management.

## Features
- **User Registration & Authentication**: Secure signup and login using OAuth 2.0.
- **Add & Manage Contacts**: Users can save, edit, and delete contacts.
- **One-on-One Chat**: Users can chat with their saved contacts.
- **Message Status**: Track messages as Sent, Delivered, and Read.
- **Real-Time Communication**: WebSockets for instant messaging.
- **PostgreSQL Database**: Store user details, contacts, and chat messages.
- **OAuth 2.0 Security**: Secure authentication with OAuth 2.0.

## Technologies Used
- **Spring Boot** - Backend framework
- **PostgreSQL** - Relational database
- **Spring Security with OAuth 2.0** - Authentication & authorization
- **Hibernate/JPA** - ORM for database interaction
- **Thymeleaf** - Template engine for UI
- **WebSockets** - Real-time chat functionality
- **Spring MVC** - Request handling and navigation

## Installation
### Prerequisites
- Install **Java 17+**
- Install **PostgreSQL** and set up a database
- Install **Maven**

### Steps to Run the Application
1. Clone the repository:
   ```bash
   git clone <repository_url>
   cd scm
   ```
2. Configure the database and OAuth 2.0 settings in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   
   # OAuth 2.0 Configuration
   spring.security.oauth2.client.registration.google.client-id=your_client_id
   spring.security.oauth2.client.registration.google.client-secret=your_client_secret
   ```
3. Install dependencies and build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. Open the application in your browser:
   ```
   http://localhost:8080/
   ```

## Pages & Routing (Thymeleaf)
- **Home Page (`/`)** - Displays contacts and chat options
- **Login Page (`/login`)** - User authentication via OAuth 2.0
- **Registration Page (`/register`)** - Sign up new users

## Contributions
Contributions are welcome! Feel free to submit issues or pull requests.
---
Happy coding! 🚀

