package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.controllers.CarritosHandler;
import org.example.controllers.ClientesHandler;
import org.example.controllers.ProductosHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Registro de Endpoints para VisioCart
        server.createContext("/clientes", new ClientesHandler());
        server.createContext("/productos", new ProductosHandler());
        server.createContext("/carritos", new CarritosHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Servidor VisioCart corriendo en http://localhost:8080");
        System.out.println("Endpoints disponibles:");
        System.out.println(" - GET http://localhost:8080/clientes");
        System.out.println(" - GET http://localhost:8080/productos");
        System.out.println(" - GET http://localhost:8080/carritos");
    }
}