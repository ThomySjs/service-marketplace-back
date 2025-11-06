# Service Marketplace - Backend

## Descripción
API REST para marketplace de servicios cuyo propósito es brindar un medio centralizado para que emprendedores y PYMES puedan alcanzar a más clientes.

Este repositorio contiene el backend.

Para ver el repositorio del frontend, [Click aquí](https://github.com/ffleita/service-marketplace-front)

## Requisitos

- Java 17+ (el proyecto está configurado para Java 17 en `pom.xml`).
- Maven 3.6+ (o usar el wrapper `./mvnw`).
- MySQL (u otro servidor compatible) si quieres correr con la base de datos real.

> Nota: las propiedades del proyecto leen variables desde un archivo `.env` (ver sección Env). El servidor por defecto se expone en el puerto 8000.

## Preparar el entorno local

1. Clona el repositorio:

```bash
git clone <url-del-repo>
cd service-marketplace-back
```

2. Copia el archivo de ejemplo de variables de entorno y edítalo con tus valores:

```bash
# Copia el template a .env
cp .env.template .env

# Abre .env en tu editor favorito y completa las variables necesarias
# Por ejemplo:
# DB_URL=jdbc:mysql://localhost:3306/service_marketplace
# DB_USER=root
# DB_PASSWORD=secret
# MAIL_USERNAME=example@gmail.com
# MAIL_PASSWORD=app-password
# JWT_SECRET=algosecreto
# JWT_EXPIRATION=3600000
# CLOUDINARY_URL=cloudinary://key:secret@cloud
# CLOUDINARY_DEFAULT=https://.../default.png
# FRONT_URL=http://localhost:3000
```

Si no tienes un archivo `.env.template` en la raíz, crea un `.env` nuevo con las variables necesarias (ver `src/main/resources/application.properties` para la lista de variables usadas).

3. (Opcional) Levantar MySQL con Docker:

```bash
docker run --name sm-db -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=service_marketplace -p 3306:3306 -d mysql:8
```

Asegúrate de que `DB_URL`, `DB_USER` y `DB_PASSWORD` en tu `.env` coincidan con la base de datos levantada.

## Construir y ejecutar

1. Usando Maven (wrapper incluido):

```bash
# Construir
./mvnw clean package -DskipTests

# Ejecutar el JAR resultante
java -jar target/service-marketplace-back-0.0.1-SNAPSHOT.jar
```

2. Ejecutar desde el código (modo desarrollo):

```bash
# Ejecutar con maven
./mvnw spring-boot:run
```

3. Usando Docker (construir la imagen y ejecutar):

```bash
# Construir imagen
docker build -t service-marketplace-back:local .

# Ejecutar (exponer puerto 8000)
docker run --env-file .env -p 8000:8000 service-marketplace-back:local
```

## Endpoints y pruebas básicas

- La aplicación escucha por defecto en: http://localhost:8000
- Actuator health: http://localhost:8000/actuator/health
- Swagger/OpenAPI UI: por defecto bajo `/swagger-ui.html` o `/swagger-ui/index.html` según la versión de springdoc.

Para probar endpoints protegidos por seguridad, usa las rutas de autenticación existentes (`/auth/login`, `/auth/logout`) o la nueva ruta de admin si fue añadida.

## Tests

Para ejecutar los tests unitarios:

```bash
./mvnw test
```

## Variables de entorno usadas (resumen)

Las propiedades principales que debes definir en tu `.env` son (véase `src/main/resources/application.properties` para más detalles):

- DB_URL: URL JDBC de la base de datos (ej: jdbc:mysql://localhost:3306/service_marketplace)
- DB_USER
- DB_PASSWORD
- MAIL_USERNAME
- MAIL_PASSWORD
- JWT_SECRET
- JWT_EXPIRATION
- CLOUDINARY_URL
- CLOUDINARY_DEFAULT
- FRONT_URL

## Contribuir

1. Crea una rama con el prefijo `feature/` o `fix/`.
2. Haz commits descriptivos.
3. Abre un Pull Request cuando estés listo.

---
