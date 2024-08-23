
# README DialoSoft Backend



## Running the Application

### How to Run a Spring Boot Application with Gradle

To run a Spring Boot application using Gradle, follow these steps:

1. Open a terminal or command prompt.

2. Navigate to the root directory of your Spring Boot project.

3. (Optional) Compile the project:
   It's generally not necessary to compile separately as the run command will do this, but if you want to ensure everything compiles correctly first:

```
gradle compile
```
   
Or using the wrapper:
   
```
./gradlew compile
```

4. Run the application using one of the following commands:

   a) Using the Gradle wrapper (recommended):
   - On Unix-based systems (Linux, macOS):
     ```
     ./gradlew bootRun
     ```
   - On Windows:
     ```
     gradlew.bat bootRun
     ```

   b) If Gradle is installed globally on your system:
   ```
    gradle bootRun
   ```

5. Gradle will compile your project (if not done already), download necessary dependencies, and run the Spring Boot application.

6. Wait for the application to start. You'll see logs in the console indicating that the application is running.

### Build and Start Containers

```bash
docker-compose up --build
```

Add the `-d` flag to run containers in detached mode:

```bash
docker-compose up --build -d
```

**Check the Status of Containers**

Use the following command to check the status of running containers:

```bash
docker-compose ps
```

**Access the Application**

**temporally**: Once the containers are running, you can access the application through 
the exposed ports specified in `docker-compose.yml`. 

For example, gateway is exposed on port 8080, you can access it at [http://localhost:8080](http://localhost:8080).

**Stop and Remove Containers**

To stop and remove all containers, networks, and volumes defined in `docker-compose.yml`, run:

```bash
docker-compose down
```

To also remove built images, add the `--rmi all` option:

```bash
docker-compose down --rmi all
```
