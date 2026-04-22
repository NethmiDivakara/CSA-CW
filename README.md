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

1. Clone Repository
git clone https://github.com/NethmiDivakara/SmartCampusAPI.git

2. Open in NetBeans
Ensure Jakarta EE 10 plugin is installed
Open project as Maven project

3. Build Project
Right click → Clean and Build

4. Deploy Server
Run on GlassFish 8

5. Access API
http://localhost:8080/SmartCampusAPI/api/v1



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
A1.In JAX-RS, the default lifecycle for resource classes (like RoomResource or SensorResource) is per-request. This means the runtime creates a new instance of the class for every incoming HTTP request and discards it after the response is sent.
Because instances are not shared between requests, we cannot store our campus data (Rooms/Sensors) as simple variables inside the resource classes. Instead, we implemented a Singleton DataStore. This ensures that every per-request resource instance accesses the same central memory. To prevent data loss or "race conditions" where two requests try to update the same list at once, we used ConcurrentHashMap. This thread-safe structure allows multiple threads to read and write simultaneously without corrupting the in-memory data.

A2.Hypermedia allows an API to be self-discovering. By including links in the "Discovery" endpoint, client developers do not need to hard-code every URL path. If the API structure evolves or versioning changes, the client can simply follow the links provided in the JSON response. This reduces the dependency on static documentation and makes the API more resilient to change.

Part 2: Room Management
A1.Returning only IDs minimizes network bandwidth, making the response "lighter." However, it forces the client to make a new HTTP request for every single room ID to get its metadata (the "N+1 problem"), which increases total latency. Returning full objects increases the payload size but allows the client to display all room details in a single "trip" to the server, significantly improving the user experience for client-side processing.

A2.Yes, the DELETE operation is idempotent. When the client sends the first request, the server finds the room, deletes it, and returns 204 No Content. If the client sends the exact same request again, the room is already gone, so the server returns 404 Not Found. While the status codes are different, the state of the server (the room being deleted) remains exactly the same regardless of how many times the request is repeated.

Part 3: Sensor Operations & Integrity
A1.The @Consumes(MediaType.APPLICATION_JSON) annotation acts as a guard. If a client sends application/xml or text/plain, JAX-RS will automatically intercept the request and return an HTTP 415 Unsupported Media Type error. The resource method will never be executed, protecting the business logic from attempting to process incompatible data formats.

A2.Path parameters are used to identify a specific resource or hierarchy (e.g., a specific sensor ID). Query parameters are used for filtering or searching a collection. Using /type/CO2 implies that "type" and "CO2" are levels in a physical hierarchy, which is semantically incorrect. Query parameters are superior because they are optional and allow clients to combine multiple filters (like ?type=CO2&status=ACTIVE) without creating complex, rigid URL structures.

Part 4: Deep Nesting with Sub-Resources
A1.The Sub-Resource Locator pattern allows us to delegate logic to dedicated classes like SensorReadingResource. In a large API, defining every nested path (like sensors/{id}/readings/{rid}) in one massive class creates a "God Object" that is hard to maintain. By delegating, we separate concerns: SensorResource manages sensor metadata, while SensorReadingResource handles historical data. This modularity makes the code cleaner, easier to test, and more scalable.

Part 5: Advanced Error Handling & Logging
A1.An HTTP 404 error usually means the URL or endpoint itself was not found. When a client sends a valid JSON payload but refers to a roomId that doesn't exist, the "entity" itself is unprocessable. HTTP 422 (Unprocessable Entity) tells the client: "I understood your request and the URL is correct, but the data you provided is logically invalid." This distinction helps developers debug whether they have a broken link (404) or a data relationship error (422).

A2.Exposing stack traces provides an attacker with a "map" of your server’s internal structure. It reveals the exact class names, method names, line numbers, and—most dangerously—the versions of libraries (like Jersey or the JDK) you are using. Attackers can use this information to search for "known vulnerabilities" in those specific versions to launch a targeted exploit.

A3.Logging is a "cross-cutting concern." Using ContainerRequestFilter and ContainerResponseFilter allows us to centralize logging in one single class. If we manually inserted log statements in every resource method, the code would be cluttered and hard to maintain. A filter ensures that every request and response is logged automatically, providing consistent observability across the entire API with a single piece of code.
