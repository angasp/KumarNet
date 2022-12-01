FROM maven:3.8-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app/
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17.0.2
COPY .env . 
COPY --from=build /home/app/target/KumarNet*.jar /usr/local/lib/KumarNet.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/KumarNet.jar"]
