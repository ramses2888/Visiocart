package org.example.controllers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.models.Cliente;
import org.example.services.ClienteService;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClientesHandler implements HttpHandler {
    private final ClienteService clienteService = new ClienteService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath(); // Ej: /clientes o /clientes/1

        try {
            if ("GET".equalsIgnoreCase(method)) {
                if (path.matches("^/clientes/\\d+$")) {
                    int id = extraerIdDelPath(path);
                    Cliente cliente = clienteService.obtenerPorId(id);
                    if (cliente != null) {
                        enviarRespuesta(exchange, 200, gson.toJson(cliente));
                    } else {
                        enviarRespuesta(exchange, 404, "{\"error\":\"Cliente no encontrado\"}");
                    }
                } else {
                    List<Cliente> clientes = clienteService.obtenerTodos();
                    enviarRespuesta(exchange, 200, gson.toJson(clientes));
                }

            } else if ("POST".equalsIgnoreCase(method)) {
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                Cliente nuevoCliente = gson.fromJson(reader, Cliente.class);

                if (clienteService.crear(nuevoCliente)) {
                    enviarRespuesta(exchange, 201, "{\"mensaje\":\"Cliente creado exitosamente\"}");
                } else {
                    enviarRespuesta(exchange, 400, "{\"error\":\"No se pudo crear el cliente\"}");
                }

            } else if ("PUT".equalsIgnoreCase(method)) {
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                Cliente clienteActualizar = gson.fromJson(reader, Cliente.class);

                if (path.matches("^/clientes/\\d+$")) {
                    clienteActualizar.setIdCliente(extraerIdDelPath(path));
                }

                if (clienteService.actualizar(clienteActualizar)) {
                    enviarRespuesta(exchange, 200, "{\"mensaje\":\"Cliente actualizado exitosamente\"}");
                } else {
                    enviarRespuesta(exchange, 400, "{\"error\":\"No se pudo actualizar el cliente\"}");
                }

            } else if ("DELETE".equalsIgnoreCase(method)) {
                if (path.matches("^/clientes/\\d+$")) {
                    int id = extraerIdDelPath(path);
                    if (clienteService.eliminar(id)) {
                        enviarRespuesta(exchange, 200, "{\"mensaje\":\"Cliente eliminado exitosamente\"}");
                    } else {
                        enviarRespuesta(exchange, 400, "{\"error\":\"No se pudo eliminar el cliente. Verifica que exista o no tenga restricciones de clave foránea.\"}");
                    }
                } else {
                    enviarRespuesta(exchange, 400, "{\"error\":\"Debes especificar el ID del cliente en la URL (ej: /clientes/1)\"}");
                }

            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            enviarRespuesta(exchange, 500, "{\"error\":\"Error interno del servidor: " + e.getMessage() + "\"}");
        }
    }

    private int extraerIdDelPath(String path) {
        String[] partes = path.split("/");
        return Integer.parseInt(partes[partes.length - 1]);
    }

    private void enviarRespuesta(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}