FROM openjdk:17
WORKDIR /opt/app
COPY . .
RUN ./gradlew build
CMD [ "./gradlew", "run" ]