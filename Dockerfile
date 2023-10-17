FROM gradle:jdk17 as build
WORKDIR /app
COPY . .
RUN rm -rf choc-ui
RUN ./gradlew build  -x test --no-daemon

FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/server/build/libs/server-*.jar app.jar
RUN ls -la
ENV OPENAI_HOST "https://api.openai.com/"
ENV OPENAI_API_KEY ""
CMD ["java", "-jar", "app.jar"]
EXPOSE 18080