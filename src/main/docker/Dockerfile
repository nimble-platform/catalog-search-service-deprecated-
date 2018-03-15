FROM nimbleplatform/nimble-base
MAINTAINER Salzburg Research <nimble-srfg@salzburgresearch.at>

# copy resource folder
COPY /resources /resources
RUN ls /resources

VOLUME /tmp
ARG finalName
ENV JAR '/'$finalName
ARG port
ADD $finalName $JAR
RUN touch $JAR
ENV PORT 8090
EXPOSE $PORT
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "catalog-search-service.jar"]