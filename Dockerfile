FROM gradle:7.2-jdk17 as builder
USER root
COPY . .
RUN gradle --no-daemon build

FROM gcr.io/distroless/java17
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/libs/fint-oneroster-rest-provider-*.jar /data/app.jar
CMD ["/data/app.jar"]