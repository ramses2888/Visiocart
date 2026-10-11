package org.example.controllers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.models.Producto;
import org.example.services.ProductoService;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProductosHandler implements HttpHandler {
    private final ProductoService productoService = new ProductoService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath(); // Ej: /productos o /productos/1

        try {
            if ("GET".equalsIgnoreCase(method)) {
                if (path.matches("^/productos/\\d+$")) {
                    int id = extraerIdDelPath(path);
                    Producto producto = productoService.obtenerPorId(id);
                    if (producto != null) {
                        enviarRespuesta(exchange, 200, gson.toJson(producto));
                    } else {
                        enviarRespuesta(exchange, 404, "{\"error\":\"Producto no encontrado\"}");
                    }
                } else {
                    List<Producto> productos = productoService.obtenerTodos();
                    enviarRespuesta(exchange, 200, gson.toJson(productos));
                }

            } else if ("POST".equalsIgnoreCase(method)) {
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                Producto nuevoProducto = gson.fromJson(reader, Producto.class);

                if (productoService.crear(nuevoProducto)) {
                    enviarRespuesta(exchange, 201, "{\"mensaje\":\"Producto creado exitosamente\"}");
                } else {
                    enviarRespuesta(exchange, 400, "{\"error\":\"No se pudo crear el producto. Verifica que el idCategoria sea válido.\"}");
                }

            } else if ("PUT".equalsIgnoreCase(method)) {
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                Producto productoActualizar = gson.fromJson(reader, Producto.class);

                if (path.matches("^/productos/\\d+$")) {
                    productoActualizar.setIdProducto(extraerIdDelPath(path));
                }

                if (productoService.actualizar(productoActualizar)) {
                    enviarRespuesta(exchange, 200, "{\"mensaje\":\"Producto actualizado exitosamente\"}");
                } else {
                    enviarRespuesta(exchange, 400, "{\"error\":\"No se pudo actualizar el producto\"}");
                }

            } else if ("DELETE".equalsIgnoreCase(method)) {
                if (path.matches("^/productos/\\d+$")) {
                    int id = extraerIdDelPath(path);
                    if (productoService.eliminar(id)) {
                        enviarRespuesta(exchange, 200, "{\"mensaje\":\"Producto eliminado exitosamente\"}");
                    } else {
                        enviarRespuesta(exchange, 400, "{\"error\":\"No se pudo eliminar el producto. Verifica que exista o no esté asociado a transacciones/detalles.\"}");
                    }
                } else {
                    enviarRespuesta(exchange, 400, "{\"error\":\"Debes especificar el ID del producto en la URL (ej: /productos/1)\"}");
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