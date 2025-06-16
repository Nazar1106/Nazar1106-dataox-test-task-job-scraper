# DataOx Job Scraper

A Java-based job scraping application that collects job postings from the Techstars job portal and provides filtering, sorting, and enrichment via HTML content extraction.

## Features

- Scrapes job data from Techstars API
- Enriches each job with full HTML description, location, tags, and publish date
- Saves structured data to a MySQL database
- Supports filtering and sorting of job postings via REST API
- Dockerized setup with optional SQL dump import
- Swagger UI for exploring and testing the API

## Technologies Used

- Java 17
- Spring Boot 3
- Hibernate (JPA)
- MySQL 8
- Docker & Docker Compose
- Maven
- OkHttp & Jackson (for JSON)
- Jsoup & Playwright (for HTML parsing)
- Swagger (OpenAPI UI)

## How to Run the Project

### Prerequisites

Before starting, make sure you have the following installed:

- Java 17+
- Maven 3.8+
- Git (to clone the project)
- MySQL Server (local or Docker)
- (Optional) Docker (to run the project using containers)

### 1. Clone the Repository

  ```bash
git clone https://github.com/Nazar1106/Nazar1106-dataox-test-task-job-scraper.git
cd dataox-job-parser
```
### 2. Configure the Database

#### Option A: Use Local MySQL

Create the database manually:

```sql
CREATE DATABASE job_scraper_db;
```

Update the DB config in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/job_scraper_db
spring.datasource.username=mate
spring.datasource.password=12345
spring.jpa.hibernate.ddl-auto=update
```

(Optional) Import sample data:

```bash
mysql -u mate -p job_scraper_db < dump/job_postings_dump.sql
```

#### Option B: Run MySQL and App via Docker

Build and start the application:

```bash
docker compose up --build -d
```

Access the application at:

```
http://localhost:8081
```

Note: MySQL will be available at `localhost:3307` if needed externally.

### 3. Build the Project (without Docker)

```bash
mvn clean install
```

### 4. Run the Application (without Docker)

```bash
mvn spring-boot:run
```

The application will start at:

```
http://localhost:8080
```

Make sure your local MySQL is running and properly configured.

### 5. Use the API

Example endpoint to filter jobs:

```
GET http://localhost:8080/api/scraper/jobs?function=Java%20Developer
```

Or if running via Docker:

```
GET http://localhost:8081/api/scraper/jobs?function=Java%20Developer
```

Scrape jobs dynamically by function (e.g., "Python Developer"):

```
GET http://localhost:8080/api/scraper/jobs?function=Java%20Developer
```

Or with Docker:

```
GET http://localhost:8080/api/scraper/jobs?function=Java%20Developer
```

This endpoint will trigger scraping and return jobs that match the specified function in real-time.

SQL Dump
Full SQL dump (schema + example jobs):
The full SQL dump (schema + example job data) is available [here] (https://drive.google.com/file/d/1ekhYYdYsw5YRbg1tLRnkVIz0mTBFgjq9/view?usp=sharing)