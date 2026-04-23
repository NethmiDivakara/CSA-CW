SmartCampus API
A robust, scalable RESTful API designed to manage and monitor smart university infrastructure. This project provides a centralized system for tracking campus rooms, integrated sensors, and real-time environmental data.

Project Overview
The SmartCampus API is built using the latest Jakarta EE 10 standards to provide a seamless interface for campus administrators and automated systems. It focuses on high availability, clean architectural separation, and professional error handling.

Key Features
 Room Management (Create, Read, Delete with safety rules)
 Sensor Management (Registration, validation, filtering)
 Sensor Readings (Historical tracking per sensor)
 Advanced Error Handling (Custom exception mappers)
 Sub-Resource Design (Sensor → Readings structure)
 API Discovery Endpoint (HATEOAS-style navigation)
 In-memory storage using HashMap / ArrayList (No database used)

Tech Stack
 Java 21 (LTS)
 Jakarta EE 10 (JAX-RS / Jersey)
 GlassFish 8.0.0
 Maven 3.x
 In-memory Data Structures (HashMap, ArrayList)
 Git & GitHub

Project Structure
com.mycompany.smartcampusapi.models
→ POJOs (Room, Sensor, SensorReading) + Exception classes

com.mycompany.smartcampusapi.resources
→ REST API endpoints (Rooms, Sensors, Readings)

com.mycompany.smartcampusapi.filters
→ Logging filter (Request & Response logging)

com.mycompany.smartcampusapi
→ Core configuration (ApplicationConfig, DataStore, Exception Mappers)


How to Run the Project
1. Build Project
Run the following command in the root directory:

Bash
mvn clean install

2. Deploy to Tomcat
Open NetBeans Services tab.

Right-click Apache Tomcat or TomEE and select Start.

Right-click your project and select Deploy.



API Endpoints
Discovery Endpoint
GET /api/v1

 Room Endpoints
GET    /api/v1/rooms
POST   /api/v1/rooms
GET    /api/v1/rooms/{id}
DELETE /api/v1/rooms/{id}

 Sensor Endpoints
GET    /api/v1/sensors
GET    /api/v1/sensors?type=CO2
POST   /api/v1/sensors
GET    /api/v1/sensors/{id}

 Sensor Readings
GET    /api/v1/sensors/{sensorId}/readings
POST   /api/v1/sensors/{sensorId}/readings

  CURL Test Cases
1️⃣ Get API Info
curl http://localhost:8080/SmartCampusAPI/api/v1

2️⃣ Create Room
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"LIB-301","name":"Library Study Room","capacity":50}'

3️⃣ Get All Rooms
curl http://localhost:8080/SmartCampusAPI/api/v1/rooms

4️⃣ Create Sensor
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":25,"roomId":"LIB-301"}'

5️⃣ Add Sensor Reading
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"value":30}'


Report Answers
Part 1: Service Architecture & Setup
A1.In JAX-RS, the default lifecycle is per-request. A new instance is created for every HTTP request and destroyed after. To persist data, a Singleton DataStore was used. To prevent race conditions, we utilize ConcurrentHashMap, which allows thread-safe operations in a multi-threaded environment.

A2.HATEOAS (Hypermedia) makes an API self-descriptive. By providing links in the response, client developers are decoupled from the hardcoded URL structure, allowing the API to evolve without breaking clients.

Part 2: Room Management
A1.Returning only IDs saves bandwidth but causes the "N+1 problem" (multiple requests for metadata). Returning full objects increases payload but allows the client to display all data in one trip.

A2.DELETE is idempotent. The first request returns 204 No Content (deletion). Subsequent requests return 404 Not Found (already gone), but the server state remains identical.

Part 3: Sensor Operations & Integrity
A1.@Consumes acts as a guard. If a client sends an incorrect format (e.g., XML), JAX-RS returns HTTP 415 Unsupported Media Type immediately.

A2.Query parameters are superior for filtering (e.g., ?type=CO2) because they are optional and combinable. Path parameters imply a physical hierarchy (e.g., /type/CO2), which is semantically incorrect for searches.

Part 4: Deep Nesting with Sub-Resources
A1.The Sub-Resource Locator pattern prevents "God Classes." It delegates reading logic to SensorReadingResource, separating historical data management from sensor metadata.

Part 5: Advanced Error Handling & Logging
A1.HTTP 422 (Unprocessable Entity) is used when the JSON is valid but a reference (like roomId) does not exist. This is semantically more accurate than a 404, which implies the URL itself is wrong.

A2.Exposing stack traces reveals internal class structures and specific library versions (e.g., Jersey 3.x, JDK 21). Attackers can use this to identify known vulnerabilities (CVEs) or orchestrate DoS attacks.


A3.Logging is a cross-cutting concern. By using LoggingFilter, we apply the DRY (Don’t Repeat Yourself) principle, ensuring all requests and status codes are logged automatically without cluttering resource methods.
