## Despliegue de la aplicación
Tanto el backend como el frontend estan desplegados en un cluster de Kubernetes de Google Cloud Platform.  
Ademas, se utiliza una instancia de MySQL para la base de datos relacional.

### Proceso de despliegue
1. Se generan los builds de las imagenes de Docker tanto del backend como del frontend. Los comandos usados son:
```bash
docker login
docker build -t {usuario/nombre_imagen:version} {ruta_a_dockerfile}
```
2. Se suben las imagenes a un repositorio en Docker Hub.
```bash
docker push {usuario/nombre_imagen:version}
```
3. A traves de archivos de manifiesto de Kubernetes se crean los deployments y services necesarios para correr ambas aplicaciones.
```bash
kubectl apply -f {ruta_a_manifiesto}.yaml 
```

### Servicios desplegados
1. MySQL: Base de datos relacional. Se usa una imagen Docker oficial de MySQL.
2. Backend: API REST desarrollada en Spring Boot.
3. Frontend: Aplicación web desarrollada en React.