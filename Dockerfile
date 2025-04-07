FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY . .

COPY wait-for-postgres.sh /wait-for-postgres.sh

RUN chmod +x /wait-for-postgres.sh

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine as runner

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

COPY --from=builder /wait-for-postgres.sh /wait-for-postgres.sh
RUN chmod +x /wait-for-postgres.sh

EXPOSE 8080

ENTRYPOINT ["/wait-for-postgres.sh", "java", "-jar", "app.jar"]
