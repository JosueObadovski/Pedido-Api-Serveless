# 1. Usar a imagem base do OpenJDK 21
FROM eclipse-temurin:21-jdk-alpine

# 2. Define o diretório de trabalho
WORKDIR /app

# 3. Copia o arquivo JAR para dentro do container
COPY pedidos-api-0.0.1-SNAPSHOT.jar /app.jar

# 4. Expõe a porta 8081
EXPOSE 8081

# 5. Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app.jar"]
