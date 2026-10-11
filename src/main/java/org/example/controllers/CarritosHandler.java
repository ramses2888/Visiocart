package org.example.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.models.CarritoInteligente;
import org.example.services.CarritoService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CarritosHandler implements HttpHandler {
    private final CarritoService carritoService = new CarritoService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            List<CarritoInteligente> carritos = carritoService.obtenerTodos();

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < carritos.size(); i++) {
                CarritoInteligente c = carritos.get(i);
                json.append(String.format(
                        "{\"idCarrito\":%d, \"estadoBateria\":%d, \"estadoOperativo\":\"%s\", \"idCliente\":%s}",
                        c.getIdCarrito(), c.getEstadoBateria(), c.getEstadoOperativo(), c.getIdCliente()
                ));
                if (i < carritos.size() - 1) json.append(",");
            }
            json.append("]");

            byte[] responseBytes = json.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseBytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }
}