# Étape 1 : build de l'application
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .

RUN ./mvnw -Pprod -DskipTests -DskipClient=true clean package

# Étape 2 : image d'exécution minimale avec Java 17
FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp
WORKDIR /app

# Copier le JAR compilé depuis l'étape précédente
COPY --from=builder /app/target/*.jar app.jar

# Port utilisé par Spring Boot
EXPOSE 8080

# Commande pour lancer l’application
ENTRYPOINT ["java", "-jar", "app.jar"]
