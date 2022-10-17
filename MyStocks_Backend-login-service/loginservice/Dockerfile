FROM maven:3.8-jdk-11 as builder

WORKDIR /app
COPY ./pom.xml .
COPY ./src /src
COPY ./ .
RUN mvn package -DskipTests

FROM openjdk:11
EXPOSE 8081
COPY --from=builder /app/target/*.jar /app.jar
ENTRYPOINT ["sh", "-c","java -jar /app.jar"]
