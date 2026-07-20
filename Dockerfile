FROM maven:3.9.6-amazoncorretto AS build
WORKDIR /build
COPY pom.xml .

RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre-alpine
WORKDIR /ms-transaction-bank
COPY --from=build /build/target/ms-transfer-bank.jar ms-transfer-bank.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ms-transfer-bank.jar"]
