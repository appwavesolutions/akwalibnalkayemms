# Akwalibnalkayemms

A comprehensive Spring Boot microservice for managing Islamic knowledge content including quotes, books, chapters, and ideas. This application serves as a digital library for Islamic scholarly works, particularly focusing on the works of Ibn Qayyim al-Jawziyya and other Islamic scholars.

## 🎯 Purpose

This microservice provides a structured way to organize and access Islamic knowledge through:
- **Wisdom Quotes**: Curated Islamic quotes with explanations and examples
- **Scholarly Books**: Management of Islamic books with detailed descriptions
- **Chapter Organization**: Structured chapters within each book
- **Ideas & Concepts**: Detailed ideas and concepts linked to specific books and chapters

## ✨ Features

### Core Functionality
- **Quote Management**: Complete CRUD operations for Islamic quotes with search capabilities
- **Book Management**: Manage Islamic scholarly books with definitions and metadata
- **Chapter Management**: Organize chapters within books with hierarchical structure
- **Idea Management**: Track detailed ideas and concepts associated with books and chapters
- **Advanced Search**: Full-text search across all content types
- **Pagination**: Efficient pagination for large datasets
- **RESTful API**: Well-structured REST endpoints with proper HTTP status codes

### Content Management
- **Multilingual Support**: Arabic content with proper UTF-8 encoding
- **Rich Content**: Support for long-form text, descriptions, and examples
- **Categorization**: Type-based categorization for quotes and content
- **Relationships**: Proper entity relationships between books, chapters, and ideas

## 🛠 Technology Stack

### Backend
- **Spring Boot 3.2.1** - Main framework
- **Java 17** - Programming language
- **Spring Data JPA** - Data persistence layer
- **Spring Security** - Authentication and authorization
- **Spring Web** - REST API development
- **Spring Validation** - Input validation

### Database
- **MySQL** - Primary database (production)
- **H2** - In-memory database (testing)

### Utilities & Libraries
- **MapStruct 1.5.5** - DTO mapping
- **Lombok 1.18.30** - Boilerplate code reduction
- **Jackson** - JSON serialization/deserialization
- **JWT (jjwt 0.11.5)** - Token-based authentication

### Documentation & Testing
- **SpringDoc OpenAPI 2.5.0** - API documentation (Swagger UI)
- **Spring Boot Test** - Testing framework
- **Spring Security Test** - Security testing
- **Testcontainers** - Integration testing
- **Awaitility** - Asynchronous testing

### Messaging (Legacy)
- **Apache Kafka 3.1.3** - Event streaming (configured but not actively used)

## 📚 Database Schema

### Core Entities

#### Quote
- `id` - Primary key
- `content` - Quote content (1000 chars)
- `description` - Detailed explanation (500 chars)
- `example` - Usage examples (1000 chars)
- `text` - Main quote text (1000 chars)
- `title` - Quote title (200 chars)
- `type` - Categorization type (Integer)

#### Book
- `id` - Primary key
- `title` - Book title (200 chars)
- `definition` - Book description (1000 chars)
- **Relationships**: One-to-many with Chapters and Ideas

#### Chapter
- `id` - Primary key
- `title` - Chapter title (200 chars)
- `book_id` - Foreign key to Book
- **Relationships**: Many-to-one with Book, One-to-many with Ideas

#### Idea
- `id` - Primary key
- `title` - Idea title (200 chars)
- `description` - Detailed description (1000 chars)
- `book_id` - Foreign key to Book
- `chapter_id` - Foreign key to Chapter
- **Relationships**: Many-to-one with Book and Chapter

## 🚀 API Endpoints

### Quotes API (`/api/quotes`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/` | Create new quote |
| `GET` | `/{id}` | Get quote by ID |
| `GET` | `/` | Get all quotes (paginated) |
| `PUT` | `/{id}` | Update quote |
| `DELETE` | `/{id}` | Delete quote |
| `GET` | `/search?keyword=` | Search quotes by keyword |
| `GET` | `/type/{type}` | Get quotes by type |
| `GET` | `/title?title=` | Get quotes by title |

### Books API (`/api/books`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/` | Create new book |
| `GET` | `/{id}` | Get book by ID |
| `GET` | `/` | Get all books (paginated) |
| `PUT` | `/{id}` | Update book |
| `DELETE` | `/{id}` | Delete book |
| `GET` | `/search?keyword=` | Search books by keyword |
| `GET` | `/title?title=` | Get book by title |

### Query Parameters
- `page` - Page number (default: 0)
- `size` - Page size (default: 10)
- `sortBy` - Sort field (default: id)
- `sortDir` - Sort direction: asc/desc (default: asc)

## 🗄 Sample Data

The application comes with pre-loaded sample data including:

### Books (10 Islamic scholarly works)
1. **زاد المعاد في هدي خير العباد** - Comprehensive guide on Prophetic biography
2. **مدارج السالكين** - Spiritual journey through stations of worship
3. **إعلام الموقعين** - Principles of Islamic jurisprudence
4. **الداء والدواء** - Treatment of spiritual and psychological diseases
5. **روضة المحبين** - Love and its concept in Islam
6. **طريق الهجرتين** - Migration to God and His Messenger
7. **أحكام أهل الذمة** - Rules for dealing with non-Muslims
8. **الروح وحقيقتها** - The nature and reality of the soul
9. **مفتاح دار السعادة** - Keys to true happiness
10. **شفاء العليل** - Divine decree, destiny, and wisdom

### Content Statistics
- **67+ Quotes** with detailed explanations and examples
- **120+ Chapters** across all books
- **1000+ Ideas** with comprehensive descriptions
- **Arabic Content** with proper Islamic terminology

## 🏃‍♂️ Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (for production)
- Git

### Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd akwalibnalkayemms
   ```

2. **Database Setup**
   ```sql
   CREATE DATABASE akwalibnalkayemms;
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - **API Base URL**: http://localhost:9090
   - **Swagger UI**: http://localhost:9090/swagger-ui.html
   - **Health Check**: http://localhost:9090/actuator/health

### Production Deployment

1. **Configure production database** in `application-prod.properties`
2. **Build the application**
   ```bash
   mvn clean package -Pprod
   ```
3. **Run the JAR file**
   ```bash
   java -jar target/akwalibnalkayemms-0.0.1-SNAPSHOT.jar
   ```

## ⚙️ Configuration

### Application Profiles

#### Development (`application-dev.properties`)
- **Database**: Local MySQL (localhost:3306)
- **Username**: root
- **Password**: admin123
- **Security**: Basic auth (admin/secret123)
- **Kafka**: Configured but not actively used

#### Production (`application-prod.properties`)
- **Database**: Remote MySQL server
- **Security**: Production-ready configuration
- **Logging**: Optimized for production

#### Test (`application-test.properties`)
- **Database**: H2 in-memory database
- **Security**: Test configuration

### Key Configuration Properties
```properties
# Server
server.port=9090

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/akwalibnalkayemms
spring.datasource.username=root
spring.datasource.password=admin1231

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Security
spring.security.user.name=admin
spring.security.user.password=secret123
```

## 🧪 Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=QuoteControllerTest

# Run integration tests
mvn test -Dtest=*IntegrationTest
```

### Test Coverage
- **Unit Tests**: Service layer and utility classes
- **Integration Tests**: Controller endpoints and database operations
- **Security Tests**: Authentication and authorization
- **Kafka Tests**: Event handling (if enabled)

## 📖 API Documentation

The application provides comprehensive API documentation through Swagger UI:

- **Interactive API Explorer**: Test endpoints directly from the browser
- **Request/Response Examples**: Detailed examples for all endpoints
- **Schema Definitions**: Complete data models and DTOs
- **Authentication**: Security requirements and token usage

Access Swagger UI at: http://localhost:9090/swagger-ui.html

## 🔧 Development

### Project Structure
```
src/
├── main/
│   ├── java/com/renault/akwalibnalkayemms/
│   │   ├── controller/     # REST controllers
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── exception/     # Custom exceptions
│   │   ├── mapper/        # MapStruct mappers
│   │   ├── model/         # JPA entities
│   │   ├── repository/    # Data repositories
│   │   └── service/       # Business logic
│   └── resources/
│       ├── application*.properties  # Configuration files
│       └── messages.properties      # Internationalization
└── test/                  # Test classes
```

### Key Design Patterns
- **Repository Pattern**: Data access abstraction
- **DTO Pattern**: Data transfer between layers
- **Builder Pattern**: Entity construction (via Lombok)
- **Mapper Pattern**: Entity-DTO conversion (via MapStruct)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- **Ibn Qayyim al-Jawziyya** - For the profound Islamic wisdom and knowledge
- **Spring Boot Community** - For the excellent framework and ecosystem
- **Open Source Contributors** - For the various libraries and tools used

---

**Note**: This application is designed for educational and research purposes in Islamic studies. The content is based on classical Islamic scholarly works and should be used in accordance with Islamic principles and guidelines.