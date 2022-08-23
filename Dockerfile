FROM ibm-semeru-runtimes:open-8-jdk-focal as builder

COPY src /project/src
COPY gradle /project/gradle
COPY build.gradle.kts /project/build.gradle.kts
COPY settings.gradle.kts /project/settings.gradle.kts
COPY gradle.properties /project/gradle.properties
COPY gradlew /project/gradlew

ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"

RUN cd /project && \
    ./gradlew distTar && \
    tar -xf build/distributions/*.tar && \
    mv quotebot* quotebot && \
    :

FROM ibm-semeru-runtimes:open-8-jre-focal

COPY --from=builder /project/quotebot /quotebot

ENTRYPOINT ["/quotebot/bin/quotebot"]
