FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app


COPY build.gradle settings.gradle gradlew* ./
COPY gradle ./gradle

RUN ./gradlew dependencies --no-daemon || true

COPY . .


RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8071

ENTRYPOINT ["java", "-jar", "app.jar"]
