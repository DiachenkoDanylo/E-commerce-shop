#Take from java jdk as our build
FROM eclipse-temurin:17.0.1_12-jdk AS build

ARG JAR_FILEsudo
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
#
#FROM node:22
#
#RUN npm install -g @angular/cli@16
#
#WORKDIR /
#RUN mkdir angular-app
#WORKDIR /angular-app
#
#ENV APP_NAME 'my-app'
#ENV ROUTING 'true'
#ENV STANDALONE 'true'
#ENV STRICT 'true'
#ENV STYLE 'css'
#
#CMD ng new $APP_NAME --routing=$ROUTING --standalone=$STANDALONE --strict=$STRICT --style=$STYLE \
#    && mv $APP_NAME/* . \
#    && rm -rf $APP_NAME \
#    && ng serve --host 0.0.0.0 --port 4200
#
#EXPOSE 4200