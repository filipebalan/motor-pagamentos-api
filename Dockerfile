# 1. Define a imagem base: Um Linux super leve (Alpine) com o Java 21 já instalado
FROM eclipse-temurin:21-jdk-alpine

# 2. Informa qual porta o contêiner vai expor para o mundo externo
EXPOSE 8080

# 3. Cria uma variável para facilitar o caminho do arquivo compilado
ARG JAR_FILE=target/*.jar

# 4. Copia o arquivo .jar compilado da sua máquina para dentro do contêiner chamando-o de app.jar
COPY ${JAR_FILE} app.jar

# 5. O comando exato que o contêiner vai executar quando for ligado
ENTRYPOINT ["java", "-jar", "/app.jar"]