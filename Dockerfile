# Stage 1: Build the application
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
# Dùng eclipse-temurin:17-jdk-jammy thay cho openjdk:17-jdk-slim (đã cũ/lỗi)
FROM eclipse-temurin:17-jdk-jammy

# Install Nginx
RUN apt-get update && apt-get install -y nginx && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY nginx.conf /etc/nginx/nginx.conf

# Setup start script
RUN echo '#!/bin/bash\n\
sed -i "s/listen 80;/listen $PORT;/" /etc/nginx/nginx.conf\n\
service nginx start\n\
java -jar app.jar' > /app/start.sh && chmod +x /app/start.sh

# Render provides the PORT env var, which Nginx will use.
# API runs on 8090 (internal), Socket on 3001 (internal).
# Nginx listens on $PORT (public) and routes traffic.

ENTRYPOINT ["/app/start.sh"]
