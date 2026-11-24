# Proyecto 1: API REST con Spring Boot en memoria

Primer proyecto de aprendizaje de Spring Boot. Los clientes se guardan en una `ArrayList`: no necesita PostgreSQL y los cambios se pierden al reiniciar.

## Contenido

- Rutas GET para listar clientes y buscar por id o nombre.
- POST para añadir clientes, PUT para actualizar el email y DELETE para eliminarlos.
- Validaciones y respuestas HTTP 200, 201, 204, 400, 404 y 409.
- Controller y servicio separados, con inyección por constructor.
- DTO `CambiarEmailRequest` para recibir únicamente el email.
- Pruebas de las rutas con MockMvc; la lista se restaura antes de cada prueba.

## Ejecutar

Requiere Java 21. Desde esta carpeta:

```bash
./mvnw test
./mvnw spring-boot:run
```

El servidor responde en `http://localhost:8080`. Ejemplo: `GET /clientes`.
Para detenerlo, pulsa Ctrl+C en su terminal.

## Organización

`src/main/java/com/example/demo` contiene la aplicación y `src/test/java/com/example/demo` sus pruebas.

El Proyecto 2 está en la carpeta hermana `../demo-jpa`. Es una copia independiente para aprender persistencia sin perder esta versión funcional en memoria. `GestionEmpresa` es el proyecto anterior de Java y JDBC y permanece separado.
