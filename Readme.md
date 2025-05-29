# Microservicios de Gestión de Pedidos

Este proyecto es una implementación de una arquitectura de microservicios diseñada para manejar de manera eficiente y escalable las operaciones de autenticación, pedidos, productos y pagos. Se basa en el ecosistema Spring Boot, comunicándose a través de APIs REST y utilizando bases de datos MySQL para la persistencia de datos.

## 🚀 Tecnologías Clave

* **Lenguaje:** Java 21+
* **Framework:** Spring Boot 3.x
* **Base de Datos:** MySQL
* **Persistencia:** Spring Data JPA / Hibernate
* **Comunicación:** RESTful APIs
* **Gestor de Dependencias:** Maven
* **Otras librerías:** Lombok (para un código más limpio)

## 📦 Componentes del Proyecto

El proyecto está modularizado en los siguientes microservicios principales:

* **`AuthService`**: Encargado de la gestión de usuarios, autenticación y autorización. Provee tokens de seguridad para que otros servicios puedan validar las identidades y permisos de los usuarios.
* **`OrderService`**: Gestiona la lógica central de los pedidos: creación, cancelación, seguimiento de estado y listado. Se integra con `ProductService` para obtener detalles de los productos.
* **`ProductService`**: Es el encargado de la información de los productos, incluyendo su registro y consulta. Actúa como la fuente autoritativa para los datos de los productos.
* **`PaymentService`**: Maneja todas las transacciones de pago, coordinando con el `OrderService` para asociar los pagos a pedidos específicos.

## 💡 Conceptos Arquitectónicos

Este proyecto ejemplifica una arquitectura de microservicios con:

* **Servicios Independientes:** Cada microservicio puede ser desarrollado, desplegado y escalado de forma autónoma.
* **Bases de Datos por Servicio:** Cada microservicio posee su propia base de datos, garantizando el desacoplamiento y la soberanía de datos.
* **Comunicación Síncrona (REST):** Los servicios se comunican a través de llamadas HTTP síncronas para realizar operaciones entre ellos.

