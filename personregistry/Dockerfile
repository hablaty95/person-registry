# BUILD STAGE - Dependency resolution
FROM eclipse-temurin:17-jdk-jammy as dependencies

WORKDIR /build

# Copy Maven wrapper and configuration
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/
COPY pom.xml .

# Download dependencies with Docker cache optimization
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

# BUILD STAGE - Application packaging
FROM dependencies as package

WORKDIR /build

COPY ./src src/

# Build application and create JAR file
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

# EXTRACT STAGE - Layer extraction for optimized Docker image
FROM package as extract

WORKDIR /build

# Use Spring Boot layer tools for optimized Docker image
RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

# FINAL STAGE - Runtime environment
FROM eclipse-temurin:17-jre-jammy AS final

# Create non-privileged user for security
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser

# Create application directory
WORKDIR /app

# Copy extracted application layers
COPY --from=extract /build/target/extracted/dependencies/ ./
COPY --from=extract /build/target/extracted/spring-boot-loader/ ./
COPY --from=extract /build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract /build/target/extracted/application/ ./

# Set proper ownership
RUN chown -R appuser:appuser /app

USER appuser

# Expose Spring Boot application port
EXPOSE 8080

# Start application with Spring Boot JarLauncher
ENTRYPOINT [ "java", "org.springframework.boot.loader.JarLauncher" ]