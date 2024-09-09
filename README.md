### Capstone Project Template: Reactive Microservice API for Real-time Notifications

---

### **Project Overview**

**Project Name**: `Real-time Notifications Microservice`

This capstone project will guide you through building a **Reactive Microservice API** for a **Real-time Notifications System** using **Spring WebFlux**, **Server-Sent Events (SSE)**, and **Reactive Programming** with **RxJava**. The goal is to deploy the microservice in the cloud (using Docker and Kubernetes or another cloud platform) and write comprehensive unit tests using **JUnit 5** and **Mockito**.


**Objective**: Build a fully reactive microservice that provides real-time notifications using **Server-Sent Events (SSE)** and **Spring WebFlux**. The microservice will expose APIs that notify clients in real-time whenever an event occurs. These notifications are to be streamed using **SSE**, and the system will support cloud deployment with Docker and Kubernetes. Additionally, unit tests will be written using **JUnit 5** and **Mockito**.

---

### **Technologies & Tools**

- **Backend Framework**: Spring Boot (with Spring WebFlux)
- **Reactive Programming**: Spring WebFlux, RxJava 3
- **Real-time Streaming**: Server-Sent Events (SSE)
- **Unit Testing**: JUnit 5, Mockito
- **Cloud Deployment**: Docker, Kubernetes, and a cloud provider (AWS, GCP, Azure)
- **Database**: In-memory (H2 for local), MongoDB (Reactive MongoDB) for production
- **CI/CD**: GitHub Actions or Jenkins for continuous integration

---

### **Functional Requirements**

1. **Notification API**:
   - Create notifications for users.
   - Fetch notifications for users.
   - Stream real-time notifications using Server-Sent Events (SSE).

2. **User Management API**:
   - Create a user.
   - Fetch user details.

3. **Real-time Notification Streaming**:
   - As soon as an event happens (e.g., a new message, comment, etc.), notify subscribed users in real-time.

---

### **Architecture Overview**

- **Reactive API**: Spring WebFlux for reactive APIs, using **Flux** and **Mono** for reactive streams.
- **SSE**: Use **Server-Sent Events** to push real-time notifications to clients.
- **Event Triggering**: When an event occurs (e.g., new message, comment), notify users via real-time streams.
- **Database**: Use **Reactive MongoDB** for non-blocking database operations.
- **Deployment**: Deploy using Docker and Kubernetes.

---

### **Project Structure**

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.notificationservice
│   │   │       ├── config
│   │   │       ├── controller
│   │   │       ├── dto
│   │   │       ├── entity
│   │   │       ├── repository
│   │   │       ├── service
│   │   │       ├── events
│   │   │       └── utils
│   └── test
│       ├── java
│       │   └── com.example.notificationservice
│       │       ├── controller
│       │       ├── service
│       │       └── repository
├── Dockerfile
├── application.yml
└── pom.xml
```

---

### **Step-by-Step Implementation**

#### 1. **Create Spring Boot Project**

Initialize a **Spring Boot** project with the following dependencies:
- **Spring WebFlux** for reactive APIs.
- **Reactive MongoDB** for reactive database operations.
- **Lombok** to minimize boilerplate code.
- **RxJava** for additional reactive utilities.
- **JUnit 5** and **Mockito** for unit testing.

#### 2. **Define Entities and DTOs**

- **User Entity**
```java
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;

    // Getters, Setters, Constructors
}
```

- **Notification Entity**
```java
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private String userId;
    private String message;
    private Instant timestamp;

    // Getters, Setters, Constructors
}
```

- **DTOs** for API request/response models:
  - `UserDto`, `NotificationDto`.

#### 3. **Create Reactive Repositories**

- **Reactive User Repository**
```java
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
```

- **Reactive Notification Repository**
```java
@Repository
public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {
    Flux<Notification> findByUserId(String userId);
}
```

#### 4. **Service Layer: Implement Business Logic Using Functional and Reactive Programming**

- **UserService** (Reactive with Mono)
```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id);
    }
}
```

- **NotificationService** (Reactive with Flux and Mono)
```java
@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Mono<Notification> createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Flux<Notification> getNotificationsForUser(String userId) {
        return notificationRepository.findByUserId(userId);
    }
}
```

#### 5. **Controller Layer: Expose Reactive Endpoints**

- **NotificationController**
```java
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public Mono<Notification> createNotification(@RequestBody NotificationDto notificationDto) {
        Notification notification = new Notification(UUID.randomUUID().toString(), notificationDto.getUserId(), notificationDto.getMessage(), Instant.now());
        return notificationService.createNotification(notification);
    }

    @GetMapping(value = "/stream/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Notification>> streamNotifications(@PathVariable String userId) {
        return notificationService.getNotificationsForUser(userId)
            .map(notification -> ServerSentEvent.builder(notification).build());
    }
}
```

#### 6. **Server-Sent Events (SSE) Implementation**

SSE allows the server to push real-time notifications to clients over HTTP. In the above `NotificationController`, we use **ServerSentEvent** to stream notifications in real-time to the user.

#### 7. **Unit Testing with JUnit 5 and Mockito**

- **NotificationServiceTest** (Reactive Testing with JUnit 5)

```java
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void testCreateNotification() {
        Notification notification = new Notification("1", "user1", "New message", Instant.now());
        Mockito.when(notificationRepository.save(notification)).thenReturn(Mono.just(notification));

        Mono<Notification> result = notificationService.createNotification(notification);
        StepVerifier.create(result)
            .expectNext(notification)
            .verifyComplete();
    }
}
```

- **NotificationControllerTest** (Mock WebFlux requests)

```java
@WebFluxTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private NotificationService notificationService;

    @Test
    void testStreamNotifications() {
        Notification notification = new Notification("1", "user1", "New message", Instant.now());
        Mockito.when(notificationService.getNotificationsForUser("user1")).thenReturn(Flux.just(notification));

        webTestClient.get()
            .uri("/notifications/stream/user1")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Notification.class)
            .hasSize(1)
            .contains(notification);
    }
}
```

#### 8. **Cloud Deployment (Docker and Kubernetes)**

- **Dockerfile** to package the microservice as a container:
```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/notification-service.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

- **Kubernetes Deployment YAML**:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: notification-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: notification-service
  template:
    metadata:
      labels:
        app: notification-service
    spec:
      containers:
        - name: notification-service
          image: your-docker-image:latest
          ports:
            - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: notification-service
spec:
  type: LoadBalancer
  ports:
    - port: 80
      targetPort: 8080
  selector:
    app: notification-service
```

#### 9. **CI/CD Pipeline (GitHub Actions Example)**

Automate the build, test, and deployment of your microservice:

```yaml
name: Java CI with Maven

on:
  push:
    branches:

 [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 17
      uses: actions/setup-java@v1
      with:
        java-version: '17'
    - name: Build with Maven
      run: mvn clean install
    - name: Test with Maven
      run: mvn test
    - name: Build Docker Image
      run: docker build . -t your-docker-repo/notification-service:latest
    - name: Push to Docker Hub
      run: docker push your-docker-repo/notification-service:latest
```

---

### **Conclusion**

This project demonstrates your knowledge of:
- **Reactive Programming** with **Spring WebFlux** and **RxJava**.
- **Real-time streaming** using **Server-Sent Events (SSE)**.
- Cloud deployment with **Docker** and **Kubernetes**.
- Unit testing with **JUnit 5** and **Mockito** for reactive systems.
  
By completing this project, you will have an end-to-end real-time microservice API showcasing everything you’ve learned in **functional and reactive programming**, and you’ll be able to deploy it on the cloud, ready for production use.
