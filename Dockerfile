FROM node:18 as ngbuilder

WORKDIR /app

COPY /mini-project-client .

RUN npm ci
RUN npm i -g @angular/cli
RUN ng build

FROM maven:3-eclipse-temurin-17 as mvnbuilder

WORKDIR /app

COPY mini-project-server/mvnw .
COPY mini-project-server/pom.xml .
COPY mini-project-server/src src
COPY --from=ngbuilder /app/dist/mini-project-client /app/src/main/resources/static/

RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17

WORKDIR /app

COPY --from=mvnbuilder /app/target/*.jar app.jar

ENV PORT=8080

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar /app/app.jar