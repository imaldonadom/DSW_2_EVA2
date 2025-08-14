package com.ipss.demo; // Asegúrate de que el paquete coincida con el de tu aplicación principal

import org.springframework.web.bind.annotation.GetMapping; // Importa para mapear solicitudes HTTP GET
import org.springframework.web.bind.annotation.RestController; // Importa para definir esta clase como un controlador REST

// @RestController es una anotación de Spring que combina @Controller y @ResponseBody.
// Indica que esta clase es un controlador y que los valores de retorno de sus métodos
// deben ser directamente escritos en el cuerpo de la respuesta HTTP.
@RestController
public class HomeController {

    // @GetMapping("/") mapea las solicitudes HTTP GET a la URL raíz de tu aplicación.
    // Cuando alguien acceda a http://localhost:8080/, este método se ejecutará.
    @GetMapping("/")
    public String home() {
        // Este método devuelve una cadena de texto. Spring Boot la enviará
        // directamente al navegador como la respuesta HTTP.
        return "<h1>¡Hola desde mi aplicación Spring Boot y mi HomeController!</h1>";
    }

    // Puedes añadir más métodos para diferentes rutas, por ejemplo:
    @GetMapping("/saludo")
    public String saludo() {
        return "Un saludo cordial a todos los usuarios.";
    }

    // O incluso un método que devuelva un JSON (ideal para APIs):
    @GetMapping("/info")
    public MiObjeto getInfo() {
        return new MiObjeto("Juan", 30, "Desarrollador");
    }

    // Clase interna simple para demostrar la devolución de JSON
    static class MiObjeto {
        private String nombre;
        private int edad;
        private String ocupacion;

        public MiObjeto(String nombre, int edad, String ocupacion) {
            this.nombre = nombre;
            this.edad = edad;
            this.ocupacion = ocupacion;
        }

        // Getters (necesarios para que Spring pueda serializar a JSON)
        public String getNombre() { return nombre; }
        public int getEdad() { return edad; }
        public String getOcupacion() { return ocupacion; }
    }
}