# Plataforma de Servicios para Emprendedores y PYMES

## 📌 Introducción
### Objetivo principal
Brindar una herramienta para aumentar la exposición y el alcance de emprendedores y PYMES.

---

## 🚩 Problemática por resolver
### Situación actual
- Emprendedores y PYMES con poca visibilidad online.  
- Dificultad para atraer clientes fuera de redes sociales.  
- Ausencia de plataformas accesibles que centralicen servicios.  

### Impacto del problema
- Pérdida de competitividad.  
- Mantenimiento de medios de difusión obsoletos.  
- Pérdida de ganancias por falta de exposición.  

---

## 💡 Propuesta de solución
- Espacio unificado para publicar servicios.  
- Servicios con descripción y datos de contacto.  
- *(Opcional futuro)* Sistema de reservas y pagos online.  

---

## 🎯 Alcance del proyecto
### Versión inicial
- Publicación de servicios.
- Contacto directo entre clientes y proveedores.

### Futuro
- Reservas online.
- Agenda integrada.
- Pagos online.
- Calificaciones y reseñas.

---

## 👥 Interesados

1. **Emprendedores y PYMES**
   - Principales usuarios de la plataforma.
   - Necesitan visibilidad y un canal para llegar a clientes fuera de redes sociales.
   - **Impacto:** Alto (beneficiarios directos).

2. **Clientes / Consumidores**
   - Personas que buscan servicios y productos.
   - Obtendrán un espacio unificado y confiable para encontrar proveedores.
   - **Impacto:** Medio-Alto (mejora la experiencia de búsqueda y acceso a servicios).

---

## 🌍 Impacto del proyecto

- Aumenta la competitividad de emprendedores y PYMES.  
- Reduce pérdidas por falta de visibilidad.  
- Potencial de generar nuevos ingresos mediante reservas y pagos online.  
- Mejora el acceso a la digitalización para negocios pequeños.  
- Promueve la inclusión digital de pequeños negocios.  
- Facilita el acceso de clientes a servicios confiables en un solo lugar.  
- Posibilidad de crecer con funcionalidades avanzadas (agenda, calificaciones, pagos).  
- Potencial de expansión a otras zonas.  

---

## 🏗️ Arquitectura general del sistema
### Front-end
- **Tecnología**: React con Vite.  
- **Comunicación**: API REST mediante peticiones HTTP.  

### Back-end
- **Tecnología**: Java con Spring Boot.  
- **Arquitectura**: Basada en capas (controller, service, repository, domain).  
- **Principios**: SOLID.  
- **Persistencia**: ORM (JPA / Hibernate).  

### Base de datos
- **Motor**: MySQL.  

### (Opcional) Contenedores
- Docker.  
- Docker Compose para orquestación local.  

### (Opcional) Despliegue
- Tentativamente en Google Cloud Platform (GCP).  
- Kubernetes (clúster de pods).  

---