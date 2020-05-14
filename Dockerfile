FROM gradle:6.3.0-jdk8 as builder
USER root
COPY . .
RUN gradle --no-daemon build

FROM gcr.io/distroless/java
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/libs/fint-oneroster-rest-provider-*.jar /data/fint-oneroster-rest-provider.jar
CMD ["/data/fint-oneroster-rest-provider.jar"]