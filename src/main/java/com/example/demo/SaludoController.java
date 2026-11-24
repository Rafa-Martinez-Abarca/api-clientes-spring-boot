package com.example.demo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;

// Spring detecta esta clase como controller. Sus métodos devuelven el cuerpo de la respuesta.
// En este ejemplo, los objetos se convierten a JSON y los String se envían como texto.
// Recorrido: petición HTTP -> controller -> servicio -> respuesta HTTP.
@RestController
public class SaludoController {

    // Dependencia del controller. final impide reasignar esta referencia; no hace inmutable al servicio.
    private final ClienteService clienteService;

    // Inyección por constructor: Spring crea el servicio y lo entrega al crear este controller.
    // Como hay un solo constructor, no necesitamos @Autowired ni new ClienteService() aquí.
    public SaludoController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // GET /saludo?nombre=Laura: RequestParam recoge el dato después del ?.
    // Si falta el parámetro o está vacío, defaultValue proporciona "mundo".
    @GetMapping("/saludo")
    public Saludo saludar(@RequestParam(name = "nombre", defaultValue = "mundo") String nombre) {
        // Dos argumentos distintos: mensaje fijo "Hola" y nombre recibido en la petición.
        // La respuesta será, por ejemplo: {"mensaje":"Hola","nombre":"Laura"}.
        Saludo saludo = new Saludo("Hola", nombre);
        return saludo;
    }

    // Un String devuelto directamente se envía como texto, sin crear un objeto JSON.
    @GetMapping("/despedida")
    public String despedida() {
        return "Adios, mundo";
    }

    // GET /clientes/2: PathVariable toma el id de la ruta, no de un parámetro tras ?.
    // Spring convierte el segmento a int. Si recibe "hola", responde 400 antes de entrar aquí.
    @GetMapping("/clientes/{id}")
    public Cliente mostrarCliente(@PathVariable("id") int id) {
        Cliente cliente = clienteService.mostraClientePorId(id);

        // El servicio devuelve null si no existe. El controller lo traduce a HTTP 404.
        // throw termina este recorrido; no se alcanza el return que hay después.
        if (cliente == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        return cliente;
    }

    // La lista se transforma en un array JSON [...]. No hace falta recorrerla para devolverla.
    // Sin otra anotación de estado, una respuesta correcta aquí usa 200 OK.
    @GetMapping("/clientes")
    public ArrayList<Cliente> obtenerTodosLosClientes() {
        return clienteService.listaCompleta();

    }

    // GET /clientes/buscar?nombre=Leo. Sin este parámetro obligatorio, Spring responde 400.
    // Si llega nombre=, el parámetro existe pero está vacío: lo validamos con isBlank().
    @GetMapping("/clientes/buscar")
    public ArrayList<Cliente> obtenerClientePorNombre(@RequestParam("nombre") String nombre) {

        if (nombre.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nombre vacío");
        }

        // Sin coincidencias se devuelve [] con 200: una búsqueda válida puede tener cero resultados.
        return clienteService.buscarClientePorNombre(nombre);

    }

    // POST /clientes recibe JSON en el cuerpo (Content-Type: application/json).
    // RequestBody convierte ese JSON a Cliente; no lee datos de la URL.
    // CREATED significa 201: se creó el cliente. Se mantiene el cliente como cuerpo de respuesta.
    @PostMapping("/clientes")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente aniadirCliente(@RequestBody Cliente cliente) {
        // Validamos ANTES de modificar la lista. null significa ausencia del valor.
        // isBlank() detecta texto vacío o solo espacios. || no evalúa la derecha si la izquierda es true.
        // Esta comprobación no valida el formato del correo ni su unicidad.
        if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email vacío");
        }

        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nombre vacío");
        }

        // El id debe ser positivo: también rechazamos cero.
        // Un id explícitamente null no cabe en int y puede ser rechazado al leer el JSON.
        if (cliente.getId() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Id negativo");
        }

        Cliente cliente2 = clienteService.addCliente(cliente);

        // Aquí null significa id duplicado, según el contrato de addCliente: respondemos 409.
        // Si no es null, el servicio ya ha añadido el cliente y lo devolvemos.
        if (cliente2 == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un cliente con ese id");
        }
        return cliente2;
    }

    // DELETE /clientes/2. 204 NO_CONTENT indica éxito sin cuerpo; no se devuelve JSON.
    // ResponseStatusException mantiene el 404 si el servicio no encuentra al cliente.
    @DeleteMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrarCliente(@PathVariable int id) {
        boolean eliminado = clienteService.borrarCliente(id);
        if (eliminado == false) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }

    }

    // PUT /clientes/2/email con {"email":"nuevo@empresa.com"}.
    // El id viene de la URL: aquí solo utilizamos el email del objeto recibido en el cuerpo.
    // 204 indica actualización correcta sin cuerpo. Datos inválidos -> 400; cliente ausente -> 404.
    @PutMapping("/clientes/{id}/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void CambiarEmailRequest(@PathVariable int id, @RequestBody CambiarEmailRequest solicitud) {
        // Validamos ANTES de modificar la lista. null significa ausencia del valor.
        // isBlank() detecta texto vacío o solo espacios. || no evalúa la derecha si la izquierda es true.
        // Esta comprobación no valida el formato del correo ni su unicidad.
        if (solicitud.getEmail() == null || solicitud.getEmail().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email vacío");
        }

        boolean encontrado = clienteService.cambiarEmail(id, solicitud.getEmail());
        if (encontrado == false) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }

    }
}
