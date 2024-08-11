# 스프링 부트 프로젝트 빌드 및 jar 파일 생성
FROM openjdk:17-jdk-slim
CMD ["./gradlew", "clean", "build"]
ARG JAR_FILE_PATH=build/libs/TravelDay-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]