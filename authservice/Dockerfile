FROM openjdk:16-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir /artifact/
EXPOSE 8901
ADD /target/authservice.jar /artifact/
CMD java -Djava.security.egd=file:/dev/./urandom -Djdk.tls.client.protocols=TLSv1.2 -Xdiag -jar /artifact/authservice.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Djdk.tls.client.protocols=TLSv1.2","-jar","/configserver/confserver.jar"]