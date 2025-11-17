package main;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConexion {

    public static void main(String[] args) {
        System.out.println("Probando conexión a la base de datos...");

        try (Connection conn = DatabaseConnection.getConnection()) {

            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexión establecida correctamente.");
                System.out.println("URL: " + conn.getMetaData().getURL());
                System.out.println("Usuario: " + conn.getMetaData().getUserName());
            } else {
                System.out.println("La conexión se obtuvo pero aparece cerrada.");
            }

        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos:");
            e.printStackTrace();
        }
    }
}
