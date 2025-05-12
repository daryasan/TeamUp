FROM gradle:7.6-jdk17 AS builder
WORKDIR /home/gradle/project
ENV GRADLE_OPTS="-Xmx2048m"
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean bootJar --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/chats.jar chats.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "chats.jar"]
