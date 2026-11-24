package com.example.demo;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

// Spring detecta esta clase y gestiona su instancia para poder inyectarla en el controller.
// Por defecto es compartida dentro de la aplicación (singleton), no se crea por cada petición.
@Service
public class ClienteService {
    
    private ArrayList<Cliente> listaClientes = new ArrayList<>();

   
    // Se ejecuta al construir el servicio, no cada vez que llega una petición.
    public ClienteService() {

        Cliente cliente1 = new Cliente(1, "Leo", "asd");
        Cliente cliente2 = new Cliente(2, "David", "asdasd");
        Cliente cliente3 = new Cliente(3, "Pedro", "asfasfsaf");

        listaClientes.add(cliente1);
        listaClientes.add(cliente2);
        listaClientes.add(cliente3);
    }

    // Método Java normal: busca por atributo id, no por posición de la lista.
    // Devuelve el objeto encontrado o null. No decide estados HTTP: eso lo hace el controller.
    public Cliente mostraClientePorId(int id) {
      for (Cliente cliente : listaClientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
        
    }

    // Devuelve la lista existente, no una copia: quien la reciba podría modificarla.
    public ArrayList<Cliente> listaCompleta() {
        return listaClientes;
    }

    // Construye una lista de coincidencias de nombre completo, ignorando mayúsculas.
    // El controller ha comprobado antes que el texto no esté vacío.
    public ArrayList<Cliente> buscarClientePorNombre(String nombre) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        for (Cliente cliente : listaClientes) {
            if (nombre.equalsIgnoreCase(cliente.getNombre())) {
                clientes.add(cliente);
            }
        }
        // Fuera del bucle para devolver TODAS las coincidencias. Si no hay, la lista está vacía.
        return clientes;
    }

   
    public Cliente addCliente(Cliente cliente) {

        // En la versión en memoria, null indica un id duplicado.
        if (mostraClientePorId(cliente.getId()) != null) {
            return null;
        }
        listaClientes.add(cliente);
        return cliente;
    }

    // true = eliminado; false = no encontrado. El controller convierte false a 404.
    public boolean borrarCliente(int id) {

        Cliente clienteEsperado = null;
        for (Cliente cliente : listaClientes) {
            if (cliente.getId() == id) {

                clienteEsperado = cliente;
                // break sale del bucle; continúa el código que hay después del for.
                break;
            }

        }
        if (clienteEsperado != null) {
            // Eliminamos DESPUÉS del recorrido para no modificar la lista durante el for-each.
            listaClientes.remove(clienteEsperado);
            return true;
        } else {
            return false;
        }

    }

    // El controller valida el email antes de llamar. Buscamos el objeto guardado y lo modificamos.
    public boolean cambiarEmail(int id, String email) {
        Cliente clienteEncootrado = null;
        for (Cliente cliente : listaClientes) {
            if (cliente.getId() == id) {
                clienteEncootrado = cliente;
                // break sale del bucle; continúa el código que hay después del for.
                break;
            }
        }
        if (clienteEncootrado != null) {
            // Es la misma referencia que está en la lista: el siguiente GET verá el email nuevo.
            clienteEncootrado.setEmail(email);
            return true;
        } else {
            return false;
        }

    }

    public void reiniciarClientes() {
        listaClientes.clear();

        listaClientes.add(new Cliente(1, "Leo", "asd"));
        listaClientes.add(new Cliente(2, "David", "asdasd"));
        listaClientes.add(new Cliente(3, "Pedro", "asfasfsaf"));
    }
}
