package com.example.demo;

// Objeto de respuesta de /saludo. No es un controller y no recibe peticiones.
public class Saludo {
   

    private String mensaje;
    private String nombre;

    // Guarda los dos datos que recibe; cada argumento corresponde a un atributo distinto.
    public Saludo(String mensaje, String nombre){
        this.mensaje = mensaje;
        this.nombre = nombre;
    }

    // Este getter expone la propiedad "mensaje" al convertir el objeto a JSON.
    public String getMensaje() {
        return mensaje;
    }

    // Permite cambiar el dato en Java; no es necesario para devolver este objeto como JSON.
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    // Expone la propiedad "nombre" en la respuesta JSON.
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}

