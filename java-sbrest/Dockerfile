FROM openjdk:8-jdk-alpine
# RUN apk update && apk add bash
COPY target/ColorNinjaServer-1.0-SNAPSHOT-jar-with-dependencies.jar ./
VOLUME /logs
EXPOSE 8080
ENTRYPOINT ["java","-cp","ColorNinjaServer-1.0-SNAPSHOT-jar-with-dependencies.jar", "com.colorninja.server.SocketGameServer", "-Dappname=colorninja","-Dport=8080"]
