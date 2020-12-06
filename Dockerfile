FROM frolvlad/alpine-java:jdk8.202.08-slim
COPY target/mail-1.0.jar /
CMD ["java", "-jar", "mail-1.0.jar"]
