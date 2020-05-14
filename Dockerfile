FROM gradle:5.4.1-jdk8-alpine as builder
USER root
COPY . .
RUN gradle --no-daemon build

FROM gcr.io/distroless/java
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/libs/fint-oneroster-rest-provider-*.jar /data/fint-oneroster-rest-provider.jar
CMD ["/data/fint-oneroster-rest-provider.jar"]