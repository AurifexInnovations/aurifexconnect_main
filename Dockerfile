# File: `Dockerfile`
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /workspace
COPY ERP/pom.xml ERP/pom.xml
COPY . /workspace
RUN mvn -B -e -f ERP/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre AS runtime
ENV JAVA_OPTS=""
# copy the built jar (rename to a stable path)
COPY --from=builder /workspace/ERP/target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 8080
# ensure Spring Boot reads Heroku's $PORT and JVM receives signals
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]