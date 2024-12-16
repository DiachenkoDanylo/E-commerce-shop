#Take from java jdk as our build
FROM eclipse-temurin:17.0.1_12-jdk AS build

ARG JAR_FILE
WORKDIR /build
#adding our 0001-snapshot etc to /build directory as application.jar file
ADD $JAR_FILE application.jar
RUN java -Djarmode=layertools -jar application.jar extract --destination extracted
#extraction our applayers to get fasterdeploy

FROM eclipse-temurin:17.0.1_12-jdk

VOLUME /tmp
WORKDIR /application
#Copying our layers to extacted dir
COPY --from=build /build/extracted/dependencies .
COPY --from=build /build/extracted/spring-boot-loader .
COPY --from=build /build/extracted/snapshot-dependencies .
COPY --from=build /build/extracted/application .
#execution
ENTRYPOINT exec java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher ${0} ${@}
