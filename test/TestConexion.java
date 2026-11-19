package test;

import config.ConfigEnv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConexion {
    public static void main(String[] args) {
        String host = ConfigEnv.get("DB_HOST", "localhost");
        String port = ConfigEnv.get("DB_PORT", "1433");
        String dbName = ConfigEnv.get("DB_NAME", "concesionaria");
        String user = ConfigEnv.get("DB_USER", "sa");
        String password = ConfigEnv.get("DB_PASSWORD", "");
        String encrypt = ConfigEnv.get("DB_ENCRYPT", "false");
        String trustCert = ConfigEnv.get("DB_TRUST_CERT", "true");

        String servidor = (port != null && !port.isBlank() && !host.contains("\\"))
                ? host + ":" + port
                : host;

        String url = String.format(
                "jdbc:sqlserver://%s;databaseName=%s;encrypt=%s;trustServerCertificate=%s;",
                servidor, dbName, encrypt, trustCert
        );

        System.out.println("Probando URL: " + url);
        System.out.println("Usuario: " + user);

        try (Connection cn = DriverManager.getConnection(url, user, password)) {
            System.out.println("CONEXION OK");
            System.out.println("Base conectada: " + cn.getCatalog());
        } catch (SQLException e) {
            System.err.println("ERROR SQL: " + e.getMessage());
            System.err.println("ErrorCode: " + e.getErrorCode() + " | SQLState: " + e.getSQLState());
        }
    }
}