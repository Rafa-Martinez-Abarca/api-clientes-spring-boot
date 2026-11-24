package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Carga la aplicación y sus componentes para la prueba.
@SpringBootTest
// Pide a Spring que asigne el objeto MockMvc al atributo.
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClienteService clienteService;

    @BeforeEach
    void prepararPrueba() {
        clienteService.reiniciarClientes();
    }

    @Test
    void obtenerClienteExistente() throws Exception {

        // Realiza la petición simulada.
        mockMvc.perform(get("/clientes/1"))
                // comprueba la respuesta: estado 200 y propiedad JSON nombre igual a "Leo". $
                // representa la raíz del JSON.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Leo"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void obtenerClienteNoExistente() throws Exception {

        // Simula una petición GET para buscar el cliente con ID 999.
        mockMvc.perform(get("/clientes/999"))
                // Comprueba que la respuesta HTTP sea 404 (Not Found).
                .andExpect(status().isNotFound());

    }

    @Test
    void insertaCliente() throws Exception {

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "id": 100,
                                "nombre": "carlos",
                                "email": "carlos@gmail.com"
                            }
                        """))
                .andExpect(status().isCreated());

    }

    @Test
    void insertaClienteIncorrecto() throws Exception {

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "id": 100,
                                "nombre": "carlos",
                                "email": ""
                            }
                        """))
                .andExpect(status().isBadRequest());

    }

    @Test
    void insertaClienteConElMismoId() throws Exception {

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "id": 200,
                                "nombre": "carlos",
                                "email": "carlos@gmail.com"
                            }
                        """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "id": 200,
                                "nombre": "carlos",
                                "email": "carlos@gmail.com"
                            }
                        """))
                .andExpect(status().isConflict());

    }

    @Test
    void borrarCliente() throws Exception {
        // 1. Eliminamos el cliente 1 y esperamos 204.
        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());
        // 2. Intentamos buscarlo y esperamos 404.
        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isNotFound());

    }

    @Test
    void borrarClienteNoExistente() throws Exception {

        mockMvc.perform(delete("/clientes/122"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarEmail() throws Exception {
        // Modificamos el email del cliente 1.
        mockMvc.perform(put("/clientes/1/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {

                                "email": "carlos@gmail.com"
                            }
                        """)).andExpect(status().isNoContent());
        // Consultamos el cliente para comprobar el cambio.
        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("carlos@gmail.com"));

    }

    @Test
    void actualizarEmailConIdNoexitente() throws Exception {

        mockMvc.perform(put("/clientes/111/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {

                                "email": "carlos@gmail.com"
                            }
                        """)).andExpect(status().isNotFound());

    }

    @Test
    void actualizarEmailConCampoVacio() throws Exception {

        mockMvc.perform(put("/clientes/1/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {

                                "email": ""
                            }
                        """)).andExpect(status().isBadRequest());

    }

    @Test
    void borrarClienteDosVeces() throws Exception {

    // Primera petición: eliminamos el cliente 2.
       mockMvc.perform(delete("/clientes/2"))

    .andExpect(status().isNoContent());
    // Segunda petición: intentamos eliminarlo otra vez.
    mockMvc.perform(delete("/clientes/2"))
    .andExpect(status().isNotFound());

    // Comprobamos que el cliente ya no existe.
    mockMvc.perform(get("/clientes/2"))
    .andExpect(status().isNotFound());

    }

    @Test 
    void obtenerListaClientes() throws Exception {

        //Que la respuesta HTTP es 200 OK
        mockMvc.perform(get("/clientes"))
        .andExpect(status().isOk());

        //Que la lista contiene 3 clientes
        mockMvc.perform(get("/clientes"))
        .andExpect(jsonPath("$.length()").value(3));

        //Que el segundo cliente se llama "David"
        mockMvc.perform(get("/clientes"))
        .andExpect(jsonPath("$[1].nombre").value("David"));
    }
}
