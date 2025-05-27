FROM gradle:8.2-jdk17 AS builder
WORKDIR /home/gradle/project
COPY . .
RUN gradle :users:clean :users:bootJar --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /home/gradle/project/users/build/libs/users.jar users.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "users.jar"]
