# 📦 API de Inventario (Backend)

Sistema de gestión de inventario desarrollado con **Java** y **Spring Boot**.
Permite a las empresas administrar sus productos de forma eficiente, segura y escalable.

## 🚀 Funcionalidades Principales
* **Gestión de Productos:** Crear, Leer, Actualizar y Borrar (CRUD).
* **Buscador Inteligente:** Filtros por nombre ignorando mayúsculas/minúsculas.
* **Validaciones:** Control de errores para precios negativos o datos faltantes.
* **Persistencia:** Base de datos real conectada (PostgreSQL).

## 🛠️ Tecnologías Usadas
* **Lenguaje:** Java 17+
* **Framework:** Spring Boot 3
* **Base de Datos:** PostgreSQL
* **Herramientas:** Maven, Git & GitHub
* **Testing:** Postman / HTTP Client

## 📋 Cómo probar este proyecto
1. Clonar el repositorio.
2. Configurar la base de datos en `application.properties`.
3. Ejecutar la aplicación con `mvn spring-boot:run`.
4. Acceder a los endpoints en `http://localhost:8080/api/productos`.

## 🔐 Seguridad y Autenticación

El sistema cuenta con una capa de seguridad robusta implementada con **Spring Security**, garantizando la protección de los datos y el acceso controlado a los recursos:

* **Autenticación Stateless:** Implementación de arquitectura basada en tokens **JWT (JSON Web Tokens)**, eliminando la necesidad de mantener sesiones en el servidor.
* **Protección de Credenciales:** Uso del algoritmo **BCrypt** para el hashing de contraseñas, asegurando que ninguna clave se almacene en texto plano.
* **Control de Acceso:** Configuración de un `SecurityFilterChain` personalizado para restringir el acceso a los endpoints de la API, permitiendo únicamente peticiones autenticadas (exceptuando el inicio de sesión).
* **Servicio de Detalles de Usuario:** Integración de `UserDetailsService` para una validación personalizada contra la base de datos PostgreSQL.

---

Desarrollado con ❤️ por [Jonathan Diaz](https://github.com/JonathanDiazDev)