package com.example.demo;


// Modelo de datos compartido en este ejercicio por las entradas y las respuestas HTTP.


public class Cliente {

    private int id;
    private String nombre;
    private String email;

   
    // Constructor que usamos al crear clientes con new Cliente(id, nombre, email).
    public Cliente(int id, String nombre, String email) {

        
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    // El conversor JSON puede crear un objeto vacío y asignar después sus propiedades.
    // Al principio, int vale 0 y las referencias String valen null.
    public Cliente() {

    }

    // Getters: permiten leer los atributos privados. El conversor los usa para producir
    // las propiedades id, nombre y email de la respuesta JSON con esta configuración.
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    // Setters: permiten asignar los datos recibidos en el JSON. No guardan nada en una BD.
    // Las validaciones del ejemplo están en el controller, no en estos setters.
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
