# Stage 1: 빌드 환경 (가벼운 OpenJDK)
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Gradle 캐시 최적화 (테스트 생략)
RUN chmod +x gradlew && \
    ./gradlew clean build --no-daemon -x test

# Stage 2: 실행 환경 (Alpine 기반 경량 JRE)
FROM eclipse-temurin:17-jre-alpine

EXPOSE 8080
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

# 메모리 최적화 실행 (Alpine 호환성 주의)
ENTRYPOINT ["java", "-XX:+UseSerialGC", "-Xmx128m", "-jar", "app.jar"]