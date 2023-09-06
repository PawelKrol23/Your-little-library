FROM eclipse-temurin:17-jre-alpine AS builder
WORKDIR app
ADD maven/${project.build.finalName}.jar ./
RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract

FROM eclipse-temurin:17-jre-alpine
WORKDIR app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
