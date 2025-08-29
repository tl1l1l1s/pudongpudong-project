FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
COPY gradle gradle
COPY src src

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jre-jammy
ENV TZ=Asia/Seoul
EXPOSE 8080

COPY --from=builder /app/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]