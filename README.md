# SpringBoot-JWT-Refresh-Token_Spring-Security_6.1.3


This repository implements a Spring Boot application with JWT (JSON Web Token) authentication and refresh token functionality, including role-based authorization. Users can register, log in, and access different parts of the application based on their roles. It also provides a mechanism to refresh JWT tokens when they expire.

## Getting Started

### Prerequisites

Make sure you have the following prerequisites installed on your system:

- Java Development Kit (JDK)
- MySQL Database
- Git

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Haris-Ahmed07/SpringBoot-JWT-Refresh-Token_Spring-Security_6.1.3.git
   ```

2. Configure the Database:

   - Open `src/main/resources/application.properties`.
   - Modify the database configurations to match your setup (e.g., database URL, username, and password).

3. Set JWT Configuration (Optional):

   - Open `src/main/resources/application.properties`.
   - You can edit the JWT token validity and secret key according to your needs.

### Usage

1. Register a User:

   - Send a **GET** request to `localhost:8080/add-user` with a JSON object in the body to register a user. You can choose between `ROLE_ADMIN` or `ROLE_USER` for the role.

   Example JSON body:
   ```json
   {
       "username": "user1",
       "password": "123",
       "role": "ROLE_ADMIN"
   }
   ```

2. Log in:

   - Send a **POST** request to `localhost:8080/login` with a JSON object in the body to log in. You will receive a JWT token and a refresh token.

   Example JSON body:
   ```json
   {
       "username": "user1",
       "password": "123"
   }
   ```

   Example response:
   ```json
   {
       "jwtToken": "YOUR_JWT_TOKEN",
       "refreshToken": "YOUR_REFRESH_TOKEN",
       "username": "user1"
   }
   ```

3. Access Protected Endpoints:

   - To access protected endpoints, include the JWT token in the `Authorization` header as follows:

     ```
     Key: Authorization
     Value: Bearer YOUR_JWT_TOKEN
     ```

   You can access the following endpoints based on your role:

   - `localhost:8080/user`
   - `localhost:8080/admin`

4. Refresh Token:

   - When your JWT token expires (default validity is 5 hours), use the refresh token to obtain a new JWT token. Send a **POST** request to `localhost:8080/refresh` with the refresh token in the JSON body.

   Example JSON body:
   ```json
   {
       "refreshToken": "YOUR_REFRESH_TOKEN"
   }
   ```

   This will provide you with a new JWT token valid for the next 5 hours. The refresh token itself is valid for 10 hours.

5. Get All Users:

   - Send a **GET** request to `localhost:8080/getall` to access all user data.

### Database

User data and refresh tokens are stored in a MySQL database named `jwt_refresh_token`. You can configure the database connection in the `application.properties` file.

## Configuration

The following configurations can be adjusted in `application.properties`:

- **Database Configuration:**
  - `spring.datasource.driver-class-name`: Database driver class name.
  - `spring.datasource.url`: Database connection URL.
  - `spring.datasource.username`: Database username.
  - `spring.datasource.password`: Database password.
  - `spring.jpa.hibernate.ddl-auto`: Hibernate DDL strategy.
  - `spring.jpa.properties.hibernate.dialect`: Hibernate dialect.

- **JWT Token Configuration:**
  - `jwt.token.validity`: JWT token validity in milliseconds (default: 5 hours).
  - `refresh.token.validity`: Refresh token validity in milliseconds (default: 10 hours).
  - `secret.key`: Secret key for JWT token generation.

## Contributors

- [Haris-Ahmed07](https://github.com/Haris-Ahmed07)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.