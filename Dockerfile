FROM openjdk:8-jdk-alpine
MAINTAINER moggi
WORKDIR /
ADD target/quiz.jar app.jar
ADD quizmini.db quizmini.db
EXPOSE 8089
ENTRYPOINT ["java", "-jar"]
CMD ["app.jar"]