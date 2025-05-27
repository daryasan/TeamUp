FROM gradle:8.2-jdk17 AS builder
WORKDIR /home/gradle/project
COPY . .
WORKDIR /home/gradle/project/chats
RUN gradle :chats:clean :chats:bootJar --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /home/gradle/project/chats/build/libs/chats.jar chats.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "chats.jar"]
