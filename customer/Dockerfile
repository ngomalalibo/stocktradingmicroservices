FROM openjdk:16-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir -p /artifact
EXPOSE 8081
ADD /target/customer.jar /artifact/
CMD java -Djava.security.egd=file:/dev/./urandom -Djdk.tls.client.protocols=TLSv1.2 -Xdiag -jar /artifact/customer.jar
#ENTRYPOINT ["java","-jar","/configserver/confserver.jar"]