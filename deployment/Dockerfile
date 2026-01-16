FROM amazoncorretto:21-alpine AS build

WORKDIR /app-service

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test --no-daemon

FROM amazoncorretto:21-alpine AS production

WORKDIR /app

ENV TZ America/Bogota

COPY --from=build /app-service/application/app-service/build/libs/*.jar  app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar", "/app/app.jar"]