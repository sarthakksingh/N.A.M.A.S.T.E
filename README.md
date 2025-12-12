NAMASTE Terminology Backend
Spring Boot backend for a clinical terminology service that unifies AYUSH (NAMASTE) concepts with ICD‑11 (TM2 and Biomedicine). It exposes APIs for code search, concept management, mappings, and CSV import.

Features
Manage code systems (NAMASTE, ICD11_TM2, ICD11_BIOMED, …)

Store concepts with code, display name, description, status

Maintain mappings between concepts across code systems

Full‑text code search API returning unified suggestions

CSV bulk import of concepts per code system

PostgreSQL persistence with JPA/Hibernate

Tech Stack
Java 21+ (you use JDK 24)

Spring Boot 4.x

Spring Web (REST)

Spring Data JPA (Hibernate)

Spring Security (currently configured to permitAll for dev)

PostgreSQL 18

Maven

Getting Started
Prerequisites
Java 21+ installed

Maven installed

PostgreSQL running (e.g. v18 on localhost:5433)

A database created, e.g. ayush_terminology

sql
CREATE DATABASE ayush_terminology;
Configuration
src/main/resources/application.properties:

text
spring.application.name=namaste

spring.datasource.url=jdbc:postgresql://localhost:5433/ayush_terminology
spring.datasource.username=postgres
spring.datasource.password=hehedb
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

server.port=8080
server.servlet.context-path=/
If you keep data.sql for seeding, ensure you’ve either:

Run it once manually, or

Use idempotent inserts (e.g. ON CONFLICT (name) DO NOTHING).

Run the application
bash
mvn spring-boot:run
Application should start on:

http://localhost:8080

Domain Model
CodeSystem

id, name, version, isActive, …

Concept

id, code, displayName, description, codeSystem, isActive

Mapping

id, fromConcept, toConcept, mappingType (EQUIVALENT/NARROWER/BROADER), confidence

Relations:

One CodeSystem → many Concepts

One Concept → many Mappings (as source or target)

REST Endpoints (current core)
Code Search
GET /api/v1/codes/search

Query params:

q (required): search term (name, code, keyword)

limit (optional, default e.g. 10): max results

Example:

text
GET /api/v1/codes/search?q=diabetes&limit=10
Response (shape):

json
{
  "suggestions": [
    {
      "baseConcept": {
        "id": 1,
        "code": "N456",
        "displayName": "Madhumeha",
        "codeSystem": "NAMASTE"
      },
      "mappings": [
        {
          "codeSystem": "ICD11_BIOMED",
          "code": "5A11",
          "mappingType": "EQUIVALENT",
          "confidence": 0.95
        }
      ]
    }
  ],
  "count": 1
}
CSV Import (Admin)
(Adapt to your actual mapping)

POST /api/v1/admin/code-systems/{codeSystemId}/concepts/import

Content-Type: multipart/form-data

Body:

file: CSV file (code,displayName,description)

Example:

code_system_id from code_systems table (e.g. NAMASTE).

Code Systems / Mappings
Add sections here describing any extra endpoints you’ve already implemented, e.g.:

GET /api/v1/code-systems

GET /api/v1/mappings?fromConceptId=&toConceptId=

Security
For development, all endpoints are currently configured as open:

java
http.csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
Later, you can enable authentication and role-based access for admin features (CSV import, mapping edits, etc.).

Database Seeding (Optional)
If using data.sql:

Place file in src/main/resources/data.sql.

Either:

Run it once manually in PostgreSQL, or

Configure spring.sql.init.mode=always and make inserts idempotent.

Example code system insert:

sql
INSERT INTO code_systems (name, version, is_active)
VALUES ('NAMASTE', '1.0', true)
ON CONFLICT (name) DO NOTHING;
Development Notes
JPA entities live under com.ayush.terminology.namaste.domain (adjust to real package).

Repositories under repository, services under service, controllers under controller.

Use Postman or similar to test endpoints during development.
