FROM frolvlad/alpine-java:jdk8.202.08-slim
COPY target/mail-1.1.jar /
CMD ["java", "-jar", "mail-1.1.jar"]
