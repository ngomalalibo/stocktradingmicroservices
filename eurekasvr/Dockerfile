FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir -p /artifact
EXPOSE 8761
ADD /target/eurekaserver.jar /artifact/
CMD java -jar /artifact/eurekaserver.jar
#ENTRYPOINT ["java","-jar","/configserver/confserver.jar"]