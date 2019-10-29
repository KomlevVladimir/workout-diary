FROM openjdk:12-jdk-alpine
COPY build/libs/workoutdiary.jar workoutdiary.jar
ENTRYPOINT ["java","-jar", "workoutdiary.jar"]
EXPOSE 8080