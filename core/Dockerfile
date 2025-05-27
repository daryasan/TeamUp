FROM gradle:8.2-jdk17 AS builder
WORKDIR /home/gradle/project
COPY . .
RUN gradle :core:clean :core:bootJar --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /home/gradle/project/core/build/libs/core.jar core.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "core.jar"]
