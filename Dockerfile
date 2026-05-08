# ─── Stage 1: Build ────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy pom.xml FIRST — dependency layer gets cached
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source AFTER — cache bust here is acceptable
COPY src ./src
RUN mvn clean package -DskipTests -q

# ─── Stage 2: Runtime (much smaller) ──────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy ONLY the JAR from Stage 1 — nothing else
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Exec form ensures Java receives OS signals correctly
ENTRYPOINT ["java", "-jar", "app.jar"]