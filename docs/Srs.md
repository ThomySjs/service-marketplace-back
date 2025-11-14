# Especificación de Requisitos de Software (ERS)
**Plataforma de Servicios para Emprendedores y PYMES - Service Marketplace**
**Autores:** Fleitas Facundo, Sejas Thomas

---

## 1. Introducción

### 1.1 Propósito
El propósito de este documento es describir de manera detallada los requisitos funcionales y no funcionales de la Plataforma de Servicios para Emprendedores y PYMES, cuyo objetivo principal es aumentar la exposición y el alcance digital de pequeños negocios y emprendedores.

### 1.2 Alcance
La plataforma permitirá a emprendedores y PYMES registrar sus servicios y ponerlos a disposición de potenciales clientes en un entorno centralizado.

En su versión inicial, los usuarios podrán:
* Publicar, editar y eliminar sus servicios.
* Navegar y filtrar servicios sin estar registrados.
* Registrarse como usuario gratuito o pago.

En futuras versiones se incorporarán funcionalidades de reservas, pagos online y calificaciones de servicios.

### 1.3 Personal involucrado

| Nombre | Rol | Categoría profesional | Responsabilidades | Información de contacto | Aprobación |
| --- | --- | --- | --- | --- | --- |
| Thomas Sejas | Desarrollador | Técnico en programación web | Desarrollar funcionalidades en el backend y frontend | Thomysejas@hotmail.com | Aprobado por Agustín Bustos (Docente) |
| Facundo Fleitas | Desarrollador | Técnico en programación web | Desarrollar funcionalidades en el backend y frontend | facundoezequielfleitas96@gmail.com | Aprobado por Agustín Bustos (Docente) |

### 1.4 Definiciones, acrónimos y abreviaturas
* **CRUD:** Create, Read, Update, Delete.
* **MVP:** Producto Mínimo Viable.
* **API:** Interfaz de Programación de Aplicaciones.
* **UI/UX:** Interfaz y experiencia de usuario.
* **GCP:** Google Cloud Platform.

### 1.5 Referencias

| Referencia | Titulo | Ruta | Fecha | Autor |
| --- | --- | --- | --- | --- |
| [IEEE-830] | IEEE Std 830-1998- Recommended Practice for Software Requirements Specifications | https://ieeexplore.ieee.org/document/720574 | 1998 | IEEE Computer Society |

### 1.6 Visión general del documento
Este documento está organizado para proporcionar una especificación completa y trazable de los requerimientos del sistema. A continuación, se describe la estructura del SRS y el contenido principal de cada sección, para que el lector pueda localizar rápida y eficientemente la información de su interés.

**Sección 1- Introducción**
Contiene el propósito del documento, el alcance del sistema, las personas involucradas, definiciones, referencias y la visión general del documento (esta sección). Proporciona el contexto necesario para entender el resto del SRS.

**Sección 2- Descripción general del sistema**
Presenta una visión a alto nivel del sistema, sus funciones principales, los actores, restricciones generales y dependencias externas.

**Sección 3- Requisitos de interfaz**
Describe los requisitos relativos a las interfaces de usuario, hardware, software y de comunicación. Cada requisito se documenta con su número identificador, nombre, prioridad, fuente y una tabla resumen.

**Sección 4- Requisitos funcionales**
Lista y detalla todos los requisitos funcionales numerados (por ejemplo, RF 4.1, RF 4.2, ...), con su descripción, precondiciones, entradas, salidas y criterios de aceptación.

**Sección 5- Requisitos no funcionales**
Incluye requisitos de rendimiento, seguridad, usabilidad, fiabilidad, compatibilidad, mantenibilidad, y otros atributos de calidad.

**Sección 6- Requisitos de diseño y restricciones**
Especifica restricciones técnicas, estándares obligatorios, plataformas soportadas y criterios de diseño que deben cumplirse.

**Sección 7- Anexos**
Documentación adicional: glosario completo, modelos de datos, diagramas, listas de referencia, historial de revisiones y plantillas utilizadas.

#### Cómo usar este documento
* **Para desarrollo:** seguir la lista de requisitos funcionales y no funcionales como fuente de trabajo priorizada.
* **Para pruebas:** usar los criterios de aceptación y las descripciones de cada requisito para diseñar casos de prueba.
* **Para validación:** consultar la sección de aprobaciones y el historial de revisiones para verificar cambios autorizados.

---

## 2. Descripción general

### 2.1 Perspectiva del producto
El sistema será una aplicación web con arquitectura cliente-servidor.
El frontend (React + Vite) se comunicará con el backend (Spring Boot) mediante una API REST.
La persistencia se realizará en MySQL y el despliegue tentativo será mediante contenedores Docker en Google Cloud Platform (GCP).

### 2.2 Funciones del producto
* Registro y autenticación de usuarios.
* Gestión de servicios (CRUD).
* Visualización pública y filtrado de servicios.
* Administración de planes (gratuito/pago).
* Panel de control del usuario.

### 2.3 Características de los usuarios

| Tipo de Usuario | Descripción | Capacidades principales |
| --- | --- | --- |
| Visitante | Usuario no registrado. | Navegar y filtrar servicios. |
| Emprendedor / PYME (gratuito) | Usuario registrado sin plan pago. | Gestionar 1 servicio activo. |
| Emprendedor / PYME (premium) | Usuario con plan pago. | Gestionar hasta 3 servicios activos. |
| Administrador | Usuario con permisos elevados. | Moderar contenido y gestionar usuarios. |

### 2.4 Restricciones
* Los usuarios gratuitos podrán mantener solo 1 servicio activo.
* Los usuarios pagos podrán tener hasta 3 servicios activos.
* La aplicación funcionará inicialmente solo en entorno web.
* Requiere conexión a Internet.

### 2.5 Suposiciones y dependencias
* Los usuarios disponen de un navegador moderno.
* Los servicios se verificarán mediante correo electrónico.
* Dependencia de un proveedor externo para pagos en línea (futuro).

---

## 3. Requisitos específicos

### 3.1 Requisitos funcionales

| Código | Requisito | Descripción |
| --- | --- | --- |
| RF-01 | Registro de usuarios | Permitir la creación de una cuenta con email y contraseña. |
| RF-02 | Autenticación | Permitir iniciar y cerrar sesión. |
| RF-03 | CRUD de servicios | Crear, leer, actualizar y eliminar servicios. |
| RF-04 | Listado público | Permitir a los visitantes ver y filtrar servicios. |
| RF-05 | Gestión de planes | Permitir elegir entre plan gratuito o pago. |
| RF-06 | Panel de usuario | Mostrar y gestionar servicios del usuario. |
| RF-07 | Roles | Diferenciar entre usuario estándar y administrador. |
| RF-08 | Administración de categorías | Permitir al administrador gestionar categorías. |

### 3.2 Requisitos no funcionales

| Código | Tipo | Descripción |
| --- | --- | --- |
| RNF-01 | Rendimiento | Las páginas deben cargar en menos de 3 segundos. |
| RNF-02 | Seguridad | Las contraseñas deben almacenarse encriptadas (bcrypt). |
| RNF-03 | Escalabilidad | Soportar al menos 100 usuarios simultáneos. |
| RNF-04 | Usabilidad | Interfaz intuitiva y adaptada a móviles. |
| RNF-05 | Mantenibilidad | Código estructurado bajo principios SOLID. |
| RNF-06 | Disponibilidad | Uptime mínimo del 95% mensual. |
| RNF-07 | Compatibilidad | Compatible con los principales navegadores. |

---

## 4. Criterios de aceptación del MVP
* Un usuario no registrado puede visualizar y filtrar servicios.
* Un usuario registrado puede crear, editar y eliminar servicios.
* La aplicación está desplegada y accesible públicamente.
* Se cuenta con al menos 10 usuarios activos.

---

## 5. Cronograma de desarrollo

| Etapa | Descripción | Duración estimada |
| --- | --- | --- |
| Inicio | Configuración del entorno y dependencias. | 1 semana |
| Desarrollo | Implementación de CRUD y autenticación. | 3 semanas |
| Integración | Sistema de planes y control de acceso. | 2 semanas |
| Cierre | Documentación, testeo y despliegue. | 1 semana |

Fecha estimada de entrega del MVP: Finales de noviembre de 2025.

---

## 6. Criterios de éxito
* Interfaz clara y amigable.
* Sistema estable y funcional.
* Emprendedores y PYMES pueden publicar sus servicios.
* Aumento de visibilidad y alcance digital.

---

## 7. Impacto esperado
* Aumento de la competitividad de pequeños negocios.
* Impulso a la digitalización e inclusión tecnológica.
* Acceso más sencillo de los clientes a servicios locales.
* Potencial de expansión a otras regiones o países.