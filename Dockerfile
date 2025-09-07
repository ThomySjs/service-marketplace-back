# Imagen base con Java 24 y bash disponible
FROM eclipse-temurin:24-jdk

# Directorio de trabajo
WORKDIR /app

# Copiamos el JAR de la aplicación
COPY target/service-marketplace-back-0.0.6-SNAPSHOT.jar app.jar

# Exponemos el puerto de la app
EXPOSE 8000

# Comando para arrancar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
