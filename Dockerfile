FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q -DskipTests package

# JRE minimo: modulos essenciais do Spring Boot + java.desktop (a carga
# reflexiva de java.beans/PropertyEditor em @Value exige esse modulo -
# jdeps puro nao enxerga). jdk.zipfs e pelo nested-jar do Spring Boot.
RUN JAR=$(ls target/web-backend-*.jar | head -n 1 || ls target/*.jar | head -n 1) \
    && MODULES="java.base,java.compiler,java.logging,java.xml,java.naming,java.instrument,java.management,java.security.jgss,java.desktop,java.sql,java.transaction.xa,jdk.unsupported,jdk.crypto.ec,jdk.zipfs,jdk.net" \
    && echo "Modulos: $MODULES" \
    && jlink \
        --add-modules "$MODULES" \
        --strip-debug \
        --no-man-pages \
        --no-header-files \
        --compress zip-6 \
        --output /jre \
    && (rm -f /jre/lib/libawt*.so /jre/lib/libfontmanager.so /jre/lib/libjavajpeg.so /jre/lib/libsplashscreen.so /jre/lib/libawt_xawt.so /jre/lib/libawt_lwawt.so 2>/dev/null || true)

FROM alpine:3.20
RUN addgroup -S app && adduser -S app -G app \
    && mkdir -p /app/uploads && chown -R app:app /app
WORKDIR /app
COPY --chown=app:app --from=build /jre /opt/jre
COPY --chown=app:app --from=build /app/target/*.jar /app/app.jar
USER app
ENV PATH="/opt/jre/bin:${PATH}"
EXPOSE 8080
ENTRYPOINT ["/opt/jre/bin/java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]
