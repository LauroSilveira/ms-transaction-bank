# ms-transaction-bank

Java microservice for managing bank transactions (transfers, payments and transaction queries).

## Overview

This project implements a lightweight service for recording, validating and querying financial transactions between accounts. It is designed to be simple, testable, and ready for integration in a microservices architecture.

## Key features

- Record transactions (send/receive)
- Basic transaction validation (accounts, amounts, status)
- Query transactions by account, ID and date range
- Designed for integration with Kafka for event streaming and CDC

## Technologies

- Java 11+ (or a compatible version)
- Frameworks: Spring Boot (recommended)
- Database: Postgres (via Docker)
- Messaging & CDC: Apache Kafka, Kafka Connect, Debezium (Postgres)
- Observability / UI: Kafka UI (for browsing topics)
- Build: Maven or Gradle
- Containerization: Docker / Docker Compose

> Note: adjust `pom.xml` and Docker images/versions to match the actual project configuration.

## Requirements

- JDK 11 or newer
- Maven or Gradle
- Docker & Docker Compose (to run Postgres, Kafka, Connect, Debezium, etc.)

## Local installation and run

1. Clone the repository:

   git clone https://github.com/LauroSilveira/ms-transaction-bank.git
   cd ms-transaction-bank

2. Build the project (Maven):

   mvn clean package

3. Run:

   java -jar target/ms-transaction-bank-0.0.1-SNAPSHOT.jar

4. The API will be available at http://localhost:8080 (adjust as configured)

## Docker / Docker Compose 

This project expects Postgres to run in Docker for local development. The system also integrates with Kafka, Kafka Connect and Debezium for CDC and uses a Kafka UI to inspect topics. Below is a simple example docker-compose snippet to start Postgres plus a Kafka stack — adapt versions and configuration as needed.

```yaml
name: ms-transaction-back

services:
  debezium:
    image: debezium/postgres:17-alpine
    container_name: debezium
    environment:
      POSTGRES_DB: transaction_db
      POSTGRES_USER: laurocorreia
      POSTGRES_PASSWORD: admin
    ports:
      - '5432:5432'
    volumes:
      - postgres_data:/var/lib/postgresql/data

  broker:
    image: apache/kafka:4.3.1
    hostname: broker
    container_name: broker
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT,CONTROLLER:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://broker:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_NODE_ID: 1
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@broker:29093
      KAFKA_LISTENERS: PLAINTEXT://broker:29092,CONTROLLER://broker:29093,PLAINTEXT_HOST://0.0.0.0:9092
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_LOG_DIRS: /tmp/kraft-combined-logs
      CLUSTER_ID: MkU3OEVBNTcwNTJENDM2Qk
      KAFKA_LOG_RETENTION_MS: 500000 # 5 minutes
      KAFKA_LOG_RETENTION_BYTES: 104857600 # 100MB retention time, optional
      KAFKA_LOG_SEGMENT_BYTES: 10485760 # 10MB
      KAFKA_LOG_RETENTION_CHECK_INTERVAL_MS: 60000 # 1 minute before delete

  schema-registry:
    image: confluentinc/cp-schema-registry:8.2.2
    container_name: schema-registry
    ports:
      - "8085:8081"
    environment:
      SCHEMA_REGISTRY_HOST_NAME: schema-registry
      SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS: PLAINTEXT://broker:29092
      SCHEMA_REGISTRY_LISTENERS: http://0.0.0.0:8081
    depends_on:
      - broker
  kafka-connect:
    build:
      context: .
      dockerfile: Dockerfile-kafka-connect
    container_name: kafka_connect
    ports:
      - "8083:8083"
    environment:
      CONNECT_BOOTSTRAP_SERVERS: broker:29092
      CONNECT_REST_PORT: 8083
      CONNECT_REST_ADVERTISED_HOST_NAME: kafka_connect
      CONNECT_GROUP_ID: kafka-connect-group
      CONNECT_CONFIG_STORAGE_TOPIC: connect-configs
      CONNECT_OFFSET_STORAGE_TOPIC: connect-offsets
      CONNECT_STATUS_STORAGE_TOPIC: connect-status
      CONNECT_CONFIG_STORAGE_REPLICATION_FACTOR: 1
      CONNECT_OFFSET_STORAGE_REPLICATION_FACTOR: 1
      CONNECT_STATUS_STORAGE_REPLICATION_FACTOR: 1
      CONNECT_KEY_CONVERTER: io.confluent.connect.avro.AvroConverter
      CONNECT_VALUE_CONVERTER: io.confluent.connect.avro.AvroConverter
      CONNECT_KEY_CONVERTER_SCHEMA_REGISTRY_URL: http://schema-registry:8081
      CONNECT_VALUE_CONVERTER_SCHEMA_REGISTRY_URL: http://schema-registry:8081
      CONNECT_PLUGIN_PATH: /usr/share/java,/usr/share/confluent-hub-components
    depends_on:
      - broker
      - schema-registry

  kafka-ui:
    image: kafbat/kafka-ui:latest
    container_name: kafka-ui
    ports:
      - "8081:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: broker:29092
      KAFKA_CLUSTERS_0_SCHEMAREGISTRY: http://schema-registry:8081
      KAFKA_CLUSTERS_0_KAFKACONNECT_0_NAME: connect
      KAFKA_CLUSTERS_0_KAFKACONNECT_0_ADDRESS: http://kafka-connect:8083
    depends_on:
      - broker

volumes:
  postgres_data:
```

# Send a request to a new transaction

Notice I am using Postman environment variables and also using this pre-request to create LocalDateTime without UTC cone. 
`
const now = new Date().toISOString().slice(0, -1); 
pm.variables.set("localDateTimeNow", now);
`
```curl
postman request POST 'http://localhost:8080/transfer' \
  --header 'Content-Type: application/json' \
  --body '{
    "transactionId": "35e1f53a-c16f-4c43-aed8-54a2dd3adcff",
    "description": "Transferencia de Tasha Fay",
    "amount": "463.59",
    "transferAt": "{{localDateTimeNow}}",
    "status": "PENDING",
    "transactionType": "CREDIT"
}'
```