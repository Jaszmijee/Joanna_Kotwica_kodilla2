# https://spring.io/guides/gs/spring-boot-docker/
FROM openjdk:17-jdk-alpine

ARG MYSQL_DB_URL
ENV MYSQL_DB_URL ${MYSQL_DB_URL?notset}

ARG TRELLO_KEY
ENV TRELLO_KEY ${TRELLO_KEY?notset}

ARG TRELLO_TOKEN
ENV TRELLO_TOKEN ${TRELLO_TOKEN?notset}

ARG TRELLO_NAME
ENV TRELLO_NAME ${TRELLO_NAME?notset}

WORKDIR /usr/src/app

COPY . .

RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser
RUN chown -R 1001:1001 /usr/src/app
RUN ls
USER 1001

EXPOSE 8080
RUN cat  /usr/src/app/src/main/resources/application-mogenius.properties >  /usr/src/app/src/main/resources/application.properties
RUN chmod +x gradlew
RUN echo ${MYSQL_DB_URL}
RUN ./gradlew build -PMYSQL_DB_URL="${MYSQL_DB_URL}" -PTRELLO_KEY="${TRELLO_KEY}" -PTRELLO_TOKEN="${TRELLO_TOKEN}" -PTRELLO_NAME="${TRELLO_NAME}"

ENTRYPOINT ["java","-jar","/usr/src/app/build/libs/tasks-0.0.1-SNAPSHOT.jar"]