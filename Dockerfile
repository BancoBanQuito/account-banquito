FROM eclipse-temurin:17-jdk-focal

COPY account/target/account-0.0.1-SNAPSHOT.jar account-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/account-0.0.1-SNAPSHOT.jar"]
EXPOSE 80
