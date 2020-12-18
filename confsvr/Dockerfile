FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir /artifact
EXPOSE 8888
ADD /target/confserver.jar /artifact/
CMD java -jar /artifact/confserver.jar
#ENTRYPOINT ["java","-jar","/configserver/confserver.jar"]