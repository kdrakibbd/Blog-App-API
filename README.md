# 📝 Blog Application API

A comprehensive RESTful API for a blog platform built with Spring Boot, featuring authentication, user management, posts, comments, likes, and saved posts functionality.

## ✨ Features

- 🔐 **Authentication & Authorization** - JWT-based authentication with role-based access control
- 👥 **User Management** - Complete CRUD operations for user profiles
- 📂 **Category Management** - Organize posts with categories
- 📰 **Post Management** - Create, read, update, and delete blog posts with pagination and search
- 💬 **Comments** - Add, update, and delete comments on posts
- ❤️ **Likes System** - Like posts and track like counts
- 🔖 **Saved Posts** - Bookmark posts for later reading
- 🖼️ **Image Upload** - Cloudinary integration for image storage (user profile & post images)
- 📄 **API Documentation** - Interactive Swagger UI documentation

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| **Spring Boot** | Backend framework |
| **Spring Data JPA** | Database ORM |
| **MySQL** | Relational database |
| **Spring Security** | Security framework |
| **JWT (JSON Web Token)** | Authentication mechanism |
| **Swagger/OpenAPI** | API documentation |
| **Cloudinary** | Cloud-based image storage |

## 🔑 Security

### Role-Based Access Control
- **USER** - Standard user privileges (create posts, comments, likes)
- **ADMIN** - Administrative privileges (manage all resources)

### Authentication Flow
1. Register new user via `/api/v1/auth/register`
2. Login to receive JWT access token via `/api/v1/auth/login`
3. Include token in `Authorization` header: `Bearer <your_token>`
4. Refresh token when expired via `/api/v1/auth/refresh-token`

## 📚 API Endpoints

### 🔐 Authentication (`/api/v1/auth`)

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/register` | Register new user | Public |
| POST | `/login` | User login | Public |
| POST | `/refresh-token` | Refresh JWT token | Public |

### 👤 User Management (`/api/v1/users`)

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/users` | Get all users | Authenticated |
| GET | `/users/{userId}` | Get user by ID | Authenticated |
| PUT | `/users` | Update current user | Authenticated |
| PUT | `/users/image` | Upload user profile image | Authenticated |
| DELETE | `/users/{userId}` | Delete user | Admin |
| DELETE | `/users/image` | Delete user profile image | Authenticated |

### 📂 Categories (`/api/v1/categories`)

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/categories` | Create category | Admin |
| GET | `/categories` | Get all categories | Public |
| GET | `/categories/{categoryId}` | Get category by ID | Public |
| PUT | `/categories/{categoryId}` | Update category | Admin |
| DELETE | `/categories/{categoryId}` | Delete category | Admin |

### 📰 Posts (`/api/v1/posts`)

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/category/{categoryId}/posts` | Create new post with image | Authenticated |
| GET | `/posts` | Get all posts (paginated) | Public |
| GET | `/posts/{postId}` | Get post by ID | Public |
| GET | `/posts/my` | Get current user's posts | Authenticated |
| GET | `/posts/search/{keyword}` | Search posts by title | Public |
| GET | `/category/{categoryId}/posts` | Get posts by category | Public |
| GET | `/user/{userId}/posts` | Get posts by user | Public |
| PUT | `/category/{categoryId}/posts/{postId}` | Update post with image | Author/Admin |
| DELETE | `/posts/{postId}` | Delete post | Author/Admin |

**Note:** Create and Update endpoints accept `multipart/form-data` for image upload.

**Query Parameters for Pagination & Sorting:**
- `pageNumber` (default: 0)
- `pageSize` (default: 10)
- `sortBy` (default: postId)
- `sortDir` (default: asc)

### 💬 Comments (`/api/v1/comments`)

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/post/{postId}/comments` | Add comment to post | Authenticated |
| PUT | `/comments/{commentId}` | Update comment | Author/Admin |
| DELETE | `/comments/{commentId}` | Delete comment | Author/Admin |

### ❤️ Likes (`/api/v1/post/{postId}/likes`)

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/post/{postId}/likes` | Add/remove like | Authenticated |
| GET | `/post/{postId}/likes` | Get like count | Public |

### 🔖 Saved Posts (`/api/v1/posts`)

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/posts/{postId}/save` | Save/unsave post | Authenticated |
| GET | `/posts/save` | Get saved posts | Authenticated |

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Spring Boot 3.0+
- MySQL 8.4 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/kdrakibbd/Blog-App-API
cd bBlog-App-API
```

2. **Configure database**

Create a MySQL database and update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blog_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Configure Cloudinary**

Add Cloudinary credentials to `application.properties`:
```properties
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

4. **Configure JWT**
```properties
application.security.jwt.secret-key=your_jwt_secret_key
application.security.jwt.access-token-expiration=86400000
application.security.jwt.refresh-token-expiration=604800000
```

5. **Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📖 API Documentation

Once the application is running, access the interactive Swagger documentation at:

```
http://localhost:8080/swagger-ui.html
```










