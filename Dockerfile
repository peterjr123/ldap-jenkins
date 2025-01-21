FROM openjdk:23
WORKDIR /opt/app
COPY . .
RUN ./gradlew build
CMD [ "./gradlew", "run" ]