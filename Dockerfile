# ============================================================
# Stage 1: Build the WAR file with Maven
# ============================================================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY WebContent ./WebContent

RUN mvn -q clean package

# ============================================================
# Stage 2: Run the WAR on Tomcat 10 (Jakarta EE 9+)
# ============================================================
FROM tomcat:10.1-jdk21-temurin

# Remove Tomcat's default sample apps to keep the image lean
RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/AI-Health-Assistant.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
