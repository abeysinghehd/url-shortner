# URL Shortener API

A simple Spring Boot URL Shortener API that converts long URLs into short, manageable links.

## Features

- **URL Shortening**: Generate short URLs from long ones
- **URL Redirection**: Redirect from short URLs to original URLs
- **URL Information**: Get information about a shortened URL
- **In-Memory Storage**: Uses a thread-safe in-memory store for simplicity
- **URL Validation**: Validates URLs before shortening
- **Error Handling**: Proper error responses for various scenarios

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Maven

## API Endpoints

### 1. Shorten URL
- **Endpoint**: `POST /api/shorten`
- **Request Body**:
  ```json
  {
    "originalUrl": "https://www.example.com/some/long/path"
  }
  ```
- **Response**:
  ```json
  {
    "originalUrl": "https://www.example.com/some/long/path",
    "shortUrl": "http://localhost:8080/abc123"
  }
  ```

### 2. Redirect to Original URL
- **Endpoint**: `GET /{id}`
- **Response**: Redirects to the original URL

### 3. Get URL Information
- **Endpoint**: `GET /api/info/{id}`
- **Response**:
  ```json
  {
    "originalUrl": "https://www.example.com/some/long/path",
    "shortUrl": "http://localhost:8080/abc123",
    "createdAt": "2025-05-06T10:15:30"
  }
  ```

## Prerequisites

- Java 21 or higher
- Maven (or use the included Maven Wrapper)

## Running the Application

### *** Prerequisite: JDK 21

### Using Maven Wrapper

1. Clone the repository:
   ```bash
   git clone https://github.com/abeysinghehd/url-shortner.git
   cd url-shortner
   ```

2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

3. The application will start on port 8080: http://localhost:8080

### Using Maven

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/url-shortener.git
   cd url-shortener
   ```

2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

3. The application will start on port 8080: http://localhost:8080

### Using the JAR file

1. Build the JAR file:
   ```bash
   ./mvnw clean package
   ```

   On Windows:
   ```bash
   mvnw.cmd clean package
   ```

2. Run the JAR file:
   ```bash
   java -jar target/url-shortner-0.0.1-SNAPSHOT.jar
   ```

## Testing the Application

### Using curl

1. Shorten a URL:
   ```bash
   curl -X POST -H "Content-Type: application/json" \
        -d '{"originalUrl":"https://www.originenergy.com.au/electricity-gas/plans.html"}' \
        http://localhost:8080/api/shorten
   ```

2. Get URL information:
   ```bash
   curl http://localhost:8080/api/info/{id}
   ```
   Replace `{id}` with the ID generated in the previous step.

3. Redirect to original URL:
    - Open your web browser and navigate to `http://localhost:8080/{id}`
    - Or use curl:
      ```bash
      curl -L http://localhost:8080/{id}
      ```
   Replace `{id}` with the ID generated in the first step.

### Running Tests

Run all tests:
```bash
./mvnw test
```

On Windows:
```bash
mvnw.cmd test
```

## Design Considerations

- **URL Generation**: Using a random Base64 encoded string for short URL codes
- **Thread Safety**: Using ConcurrentHashMap for in-memory storage to handle concurrent requests
- **Idempotency**: Returning the same short URL for identical original URLs
- **Validation**: Validating URL format before shortening