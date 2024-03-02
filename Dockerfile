FROM eclipse-temurin:11-jdk-focal

VOLUME /tmp
COPY target/amala-mkoba.jar amalamkoba.jar
ENTRYPOINT [ "java","-jar", "amalamkoba.jar" ]