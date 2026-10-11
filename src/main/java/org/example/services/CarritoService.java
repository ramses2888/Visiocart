package org.example.services;

import org.example.models.CarritoInteligente;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarritoService {
    public List<CarritoInteligente> obtenerTodos() {
        List<CarritoInteligente> carritos = new ArrayList<>();
        String sql = "SELECT * FROM carrito_inteligente";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CarritoInteligente c = new CarritoInteligente(
                        rs.getInt("id_carrito"),
                        rs.getInt("estado_bateria"),
                        rs.getString("estado_operativo"),
                        (Integer) rs.getObject("id_cliente")
                );
                carritos.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carritos;
    }
}