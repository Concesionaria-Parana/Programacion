package db;

import config.ConfigEnv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión JDBC hacia Microsoft SQL Server utilizando parámetros de ConfigEnv.
 */
public class ConexionSQLServer {
    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private ConexionSQLServer() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            return obtenerConexion();
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC: " + DRIVER_CLASS, e);
        }
    }

    public static Connection obtenerConexion() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVER_CLASS);

        String host = ConfigEnv.get("DB_HOST", "localhost");
        String port = ConfigEnv.get("DB_PORT", "1433");
        String dbName = ConfigEnv.get("DB_NAME", "ConcesionariaParanaDB");
        String user = ConfigEnv.get("DB_USER", "");
        String password = ConfigEnv.get("DB_PASSWORD", "");
        String encrypt = ConfigEnv.get("DB_ENCRYPT", "false");
        String trustCert = ConfigEnv.get("DB_TRUST_CERT", "true");

        String url = String.format(
            "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=%s;trustServerCertificate=%s;",
            host, port, dbName, encrypt, trustCert
        );

        return DriverManager.getConnection(url, user, password);
    }
}