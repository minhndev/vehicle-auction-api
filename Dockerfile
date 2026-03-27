FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]

FROM rabbitmq:3.13-management

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

RUN curl -L https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/v3.13.0/rabbitmq_delayed_message_exchange-3.13.0.ez > /plugins/rabbitmq_delayed_message_exchange-3.13.0.ez

RUN rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange