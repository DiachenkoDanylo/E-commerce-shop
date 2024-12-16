# E-Commerce Backend

## Overview
This is a backend application for an e-commerce store. 
The application supports functionality for handling users, products, categories, reviews, orders, wishlists, and imageDtos, enabling full CRUD operations 
and offering JWT-based authentication and authorization.

## Features
- User Authentication & Authorization (JWT)
- Product, Category, Order, Review, Wishlist Management
- RESTful API for client interaction
- Image handling service for product and category image
- Dockerized for ease of local deployment
- Search and filtering capabilities
- Order tracking and history

## Technologies Used
- **Java 17**
- **Spring Boot**: Main framework for REST API
- **Spring Security**: Authentication and Authorization
- **JWT**: Secure token-based authentication
- **Hibernate**: ORM for database interactions
- **Flyway**: Database migrations
- **MapStruct**: DTO mappings
- **PostgreSQL**: Database
- **Docker**: Containerization for deployment
- **Maven**: Project management and dependency management

## Prerequisites
- **Java 17** or higher
- **Docker** and **Docker Compose** (for deployment)
- **Maven** (for dependency management)

## Getting Started

### Cloning the Repository
#### You need to have :
- **Docker**

Using Bash or Idea terminal 

```bash
git clone https://github.com/DiachenkoDanylo/E-commerce-shop.git
cd E-commerce-shop
mvn clean package
docker-compose up --build
```

### Swagger 
- Swagger-UI : http://localhost:8080/swagger-ui/index.html
- Authentication :/login \
\
ADMIN \
login : client3 \
password: pass1 \
\
CLIENT \
login : client1 \
password: pass1