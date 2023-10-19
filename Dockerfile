FROM openjdk:20-jdk-oraclelinux7
COPY arachni-classifier-api/target/arachni-classifier.jar arachni-classifier.jar

ENV JAVA_OPTS = ""

EXPOSE 8080
CMD ["sh", "-c", "java -jar $JAVA_OPTS arachni-classifier.jar"]