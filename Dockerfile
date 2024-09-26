
# Stage 0, "build-stage", based on gradle, to build and compile Java
FROM public.ecr.aws/docker/library/gradle:8-jdk21 AS build-stage
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build -x test --no-daemon

# Stage 1, based on Java 
# FROM public.ecr.aws/amazoncorretto/amazoncorretto:21
# COPY --from=build-stage /home/gradle/src/build/libs/*.jar app.jar
# ENV JAVA_TOOL_OPTIONS "--enable-preview"
# ENTRYPOINT ["java","-jar","/app.jar"]


# i think works with war file
FROM public.ecr.aws/amazoncorretto/amazoncorretto:21
COPY --from=build-stage /home/gradle/src/build/libs/*.war app.war
ENV JAVA_TOOL_OPTIONS="--enable-preview"
ENTRYPOINT ["java","-jar","/app.war"]
