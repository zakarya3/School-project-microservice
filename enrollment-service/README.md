# 📚 Enrollment Service

Service de gestion des inscriptions des étudiants aux cours dans le projet School Management System.

## 🎯 Fonctionnalités

- ✅ Inscription d'étudiants aux cours
- ✅ Gestion des statuts d'inscription (PENDING, ACTIVE, COMPLETED, DROPPED, CANCELLED)
- ✅ Vérification des doublons d'inscription
- ✅ Communication avec student-service et course-service via Feign
- ✅ Sécurisation OAuth2/JWT avec Keycloak
- ✅ Documentation Swagger/OpenAPI
- ✅ Gestion des erreurs globale
- ✅ Validation des données

## 🏗️ Architecture

### Entités
- **Enrollment** : Entité principale représentant une inscription
  - `id` : Identifiant unique
  - `studentId` : Référence vers l'étudiant
  - `courseId` : Référence vers le cours
  - `enrollmentDate` : Date d'inscription
  - `status` : Statut (EnrollmentStatus enum)
  - `notes` : Notes optionnelles
  - `createdAt` / `updatedAt` : Horodatage

### DTOs
- **EnrollmentRequestDto** : Création d'inscription
- **EnrollmentResponseDto** : Réponse avec détails enrichis
- **EnrollmentUpdateDto** : Mise à jour
- **StudentDto** / **CourseDto** : Données des services externes

### Services
- **EnrollmentService** : Interface du service
- **EnrollmentServiceImpl** : Implémentation avec logique métier

### Clients Feign
- **StudentClient** : Communication avec student-service
- **CourseClient** : Communication avec course-service

## 🔌 API Endpoints

### Inscriptions

```http
POST   /api/enrollments
GET    /api/enrollments
GET    /api/enrollments/{id}
GET    /api/enrollments/student/{studentId}
GET    /api/enrollments/course/{courseId}
GET    /api/enrollments/status/{status}
PUT    /api/enrollments/{id}
PATCH  /api/enrollments/{id}/cancel
DELETE /api/enrollments/{id}
GET    /api/enrollments/check?studentId={id}&courseId={id}
GET    /api/enrollments/course/{courseId}/count
GET    /api/enrollments/health
```

### Exemple de requête

**Créer une inscription**
```json
POST /api/enrollments
{
  "studentId": 1,
  "courseId": 2,
  "notes": "Inscription validée"
}
```

**Réponse**
```json
{
  "id": 1,
  "studentId": 1,
  "studentName": "John Doe",
  "courseId": 2,
  "courseName": "Java Advanced",
  "enrollmentDate": "2026-03-12T10:30:00",
  "status": "ACTIVE",
  "notes": "Inscription validée",
  "createdAt": "2026-03-12T10:30:00",
  "updatedAt": "2026-03-12T10:30:00"
}
```

## 🔐 Sécurité

- **Authentification** : OAuth2 avec JWT via Keycloak
- **Authorization** : Bearer token requis pour tous les endpoints sauf `/health`
- **Propagation JWT** : Les tokens sont automatiquement propagés aux services externes via Feign

## 🗄️ Base de données

**PostgreSQL** - Base : `enrollment_db`

### Table: enrollments

| Colonne          | Type         | Description                    |
|------------------|--------------|--------------------------------|
| id               | BIGSERIAL    | Clé primaire                   |
| student_id       | BIGINT       | Référence étudiant             |
| course_id        | BIGINT       | Référence cours                |
| enrollment_date  | TIMESTAMP    | Date d'inscription             |
| status           | VARCHAR      | Statut (ENUM)                  |
| notes            | TEXT         | Notes optionnelles             |
| created_at       | TIMESTAMP    | Date de création               |
| updated_at       | TIMESTAMP    | Date de modification           |

## ⚙️ Configuration

### application.yaml

```yaml
server:
  port: 8700

spring:
  application:
    name: enrollment-service
  datasource:
    url: jdbc:postgresql://localhost:5433/enrollment_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8081/realms/school

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

student-service:
  url: http://localhost:8500

course-service:
  url: http://localhost:8600
```

## 🚀 Démarrage

### Prérequis
- Java 17
- PostgreSQL (via Docker)
- Keycloak (via Docker)
- Eureka Discovery Server
- Student Service
- Course Service

### Lancer le service

```bash
cd enrollment-service
./mvnw spring-boot:run
```

Ou avec Maven installé :

```bash
mvn spring-boot:run
```

## 📖 Documentation

Une fois le service démarré, accédez à :

- **Swagger UI** : http://localhost:8700/swagger-ui.html
- **OpenAPI JSON** : http://localhost:8700/v3/api-docs

## 🔄 Statuts d'inscription

| Statut    | Description                          |
|-----------|--------------------------------------|
| PENDING   | En attente de validation             |
| ACTIVE    | Inscription active                   |
| COMPLETED | Cours terminé                        |
| DROPPED   | Étudiant a abandonné le cours        |
| CANCELLED | Inscription annulée                  |

## 🛠️ Technologies utilisées

- Spring Boot 3.2.5
- Spring Cloud 2023.0.3
- Spring Data JPA
- Spring Security OAuth2
- OpenFeign
- PostgreSQL
- Lombok
- Swagger/OpenAPI 3
- Jakarta Validation

## 📝 Logs

Le service utilise SLF4J avec Logback. Les logs incluent :
- Création d'inscriptions
- Vérifications de doublons
- Appels aux services externes
- Erreurs et exceptions

## 🧪 Tests

```bash
./mvnw test
```

## 📦 Build

```bash
./mvnw clean package
```

Le JAR sera créé dans `target/enrollment-service-0.0.1-SNAPSHOT.jar`

## 🐳 Docker

```bash
docker build -t enrollment-service:latest .
docker run -p 8700:8700 enrollment-service:latest
```

## 🤝 Intégration avec les autres services

### Student Service
- Vérifie l'existence de l'étudiant avant inscription
- Récupère les détails de l'étudiant pour enrichir les réponses

### Course Service
- Vérifie l'existence du cours avant inscription
- Récupère les détails du cours pour enrichir les réponses

### API Gateway
- Toutes les requêtes passent par la gateway sur le port 8085
- Route : `/enrollments/**` → `enrollment-service:8700`

## 📊 Métriques et Monitoring

- **Health Check** : `/api/enrollments/health`
- **Actuator** : Peut être ajouté pour plus de métriques

## 🔧 Développement

### Structure du code

```
enrollment-service/
├── src/main/java/com/example/enrollmentservice/
│   ├── EnrollmentServiceApplication.java
│   ├── entities/
│   │   ├── Enrollment.java
│   │   └── EnrollmentStatus.java
│   ├── dto/
│   │   ├── EnrollmentRequestDto.java
│   │   ├── EnrollmentResponseDto.java
│   │   ├── EnrollmentUpdateDto.java
│   │   ├── StudentDto.java
│   │   └── CourseDto.java
│   ├── repository/
│   │   └── EnrollmentRepository.java
│   ├── service/
│   │   ├── EnrollmentService.java
│   │   └── impl/
│   │       └── EnrollmentServiceImpl.java
│   ├── controller/
│   │   └── EnrollmentController.java
│   ├── client/
│   │   ├── StudentClient.java
│   │   └── CourseClient.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   ├── FeignConfig.java
│   │   └── OpenApiConfig.java
│   └── exception/
│       ├── ResourceNotFoundException.java
│       ├── DuplicateEnrollmentException.java
│       ├── ErrorResponse.java
│       └── GlobalExceptionHandler.java
└── src/main/resources/
    └── application.yaml
```

## 📄 License

Ce projet fait partie du School Management System.

---

**Développé avec ❤️ pour le projet School Management System**

