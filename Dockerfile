FROM gcr.io/distroless/java17-debian12@sha256:6d25f6a8d826f7472f18dc32a9c439d2f245713df4029e5ecec19378eaaf952c
COPY target/ms-document-submission-manager-*.jar /app.jar

COPY --from=pik94420.live.dynatrace.com/linux/oneagent-codemodules:java / /
ENV LD_PRELOAD /opt/dynatrace/oneagent/agent/lib64/liboneagentproc.so

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
