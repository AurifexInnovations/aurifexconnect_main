# File: `Dockerfile`
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /workspace
COPY ERP/pom.xml ERP/pom.xml
COPY ERP/src ERP/src
RUN mvn -B -f ERP/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre AS runtime
ENV JAVA_OPTS=""
# copy the built jar (wildcard ok if only one jar is produced)
COPY --from=builder /workspace/ERP/target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 8080
# use sh -lc so ${PORT} expands and use exec so JVM receives signals
ENTRYPOINT ["sh","-lc","exec java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
