FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/hive-0.0.1-SNAPSHOT-standalone.jar /hive/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/hive/app.jar"]
