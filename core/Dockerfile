FROM gradle:7.6-jdk17 AS builder
WORKDIR /home/gradle/project
COPY . .
RUN chmod +x gradlew

RUN ./gradlew clean bootJar --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/core.jar core.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "core.jar"]
