# Dockerfile
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /workspace

# copy the module directory so its pom and sources exist
COPY ERP /workspace/ERP

# build the ERP module directly (batch mode, show errors, skip tests)
RUN mvn -B -e -f ERP/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre AS runtime
ENV JAVA_OPTS=""

COPY --from=builder /workspace/ERP/target/*.jar /app/app.jar
WORKDIR /app

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]