FROM openjdk:11-slim-buster

WORKDIR /app

ARG ORIGINAL_JAR_FILE=./build/libs/payment-service-1.0.0.jar

COPY ${ORIGINAL_JAR_FILE} payment-service.jar

CMD ["java", "-jar", "/app/payment-service.jar"]
