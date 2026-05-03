FROM openjdk:17-ea-17-oracle

RUN groupadd -r appgroup && useradd -r -g appgroup appuser

ARG JAR_FILE

COPY --chown=appuser:appgroup build/libs/${JAR_FILE} /home/appuser/petralib.jar

USER appuser
WORKDIR /home/appuser

ENTRYPOINT ["java", "-jar", "petralib.jar" ]