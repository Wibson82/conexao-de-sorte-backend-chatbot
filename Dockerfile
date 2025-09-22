# ============================================================================
# 🐳 DOCKERFILE MULTI-ESTÁGIO - CHATBOT JOGO DO BICHO
# ============================================================================
FROM amazoncorretto:25-alpine3.22 AS builder

# Instalar Maven
RUN apk add --no-cache maven
WORKDIR /build

COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .
RUN --mount=type=cache,target=/root/.m2 mvn -q -B -DskipTests dependency:go-offline || true

COPY src/ src/
RUN --mount=type=cache,target=/root/.m2 mvn -q -B -DskipTests clean package

FROM amazoncorretto:25-alpine3.22
RUN apk add --no-cache tzdata curl dumb-init && rm -rf /var/cache/apk/*
ENV TZ=America/Sao_Paulo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN addgroup -g 1001 -S appgroup \
 && adduser -u 1001 -S appuser -G appgroup
WORKDIR /app

COPY --from=builder --chown=appuser:appgroup /build/target/*.jar app.jar


HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8090/actuator/health || exit 1

USER appuser:appgroup
ENTRYPOINT ["dumb-init", "--", "java"]
CMD ["-jar", "app.jar"]

