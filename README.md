# 📦 API REST de Gestión de Inventario y Pedidos

API backend robusta diseñada para administrar el flujo de ventas e inventario de un restaurante/comercio. Incluye seguridad JWT, manejo de transacciones atómicas y protección contra condiciones de carrera.

## 🚀 Características Principales

* **Autenticación Segura:** Login con **Spring Security** y **JWT** (JSON Web Tokens).
* **Gestión de Inventario:** CRUD completo de productos con validaciones de stock.
* **Sistema de Pedidos Inteligente:**
    * Validación de stock en tiempo real.
    * **Transacciones Atómicas:** Previene errores de consistencia si falla un paso del pedido.
    * **Protección de Concurrencia:** Consultas optimizadas (`@Query` atómica) para evitar condiciones de carrera cuando múltiples usuarios venden el mismo producto simultáneamente.
* **Control de Acceso (RBAC):** Diferenciación de permisos (aunque configurado para empleados autenticados actualmente).
* **Documentación:** Integración con **Swagger/OpenAPI** para visualización de endpoints.

## 🛠️ Tecnologías Utilizadas

* **Java 17**
* **Spring Boot 3** (Web, Security, Data JPA, Validation)
* **Base de Datos:** H2 (Modo dev) / PostgreSQL (Producción)
* **Herramientas:** Maven, Lombok, Postman

## 🔌 Endpoints Principales

| Método | Endpoint | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/login` | Autenticación de usuarios y obtención de Token | Público |
| `GET` | `/api/productos` | Listado de productos con stock actual | Autenticado |
| `POST` | `/api/productos` | Crear un nuevo producto | Autenticado |
| `POST` | `/api/pedidos` | Registrar una venta (descuenta stock automáticamente) | Autenticado |

## ⚙️ Instalación y Ejecución

1.  Clonar el repositorio:
    ```bash
    git clone https://github.com/JonathanDiazDev/inventario-backend
    ```
2.  Configurar variables de entorno (si aplica) en `application.properties`.
3.  Ejecutar el proyecto:
    ```bash
    ./mvnw spring-boot:run
    ```
4.  Acceder a la documentación Swagger:
    * `http://localhost:8080/swagger-ui.html`

## 🧪 Pruebas

El sistema cuenta con validaciones de negocio como:
* Impedir ventas si `cantidad_solicitada > stock_actual`.
* Mensajes de error descriptivos para el cliente ("Solo quedan X unidades").
Desarrollado con ❤️ por [Jonathan Diaz](https://github.com/JonathanDiazDev)