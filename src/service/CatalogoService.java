package service;

import db.ConexionSQLServer;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CatalogoService {

    public List<Map<String, Object>> listarSucursales() throws SQLException {
        String sql = "SELECT id_sucursal, codigo, nombre, direccion FROM sucursal ORDER BY nombre";
        return ejecutarConsultaMapa(sql);
    }

    public List<Map<String, Object>> listarTiposVehiculo() throws SQLException {
        String sql = "SELECT id_tipo_vehiculo, nombre FROM tipo_vehiculo ORDER BY id_tipo_vehiculo";
        return ejecutarConsultaMapa(sql);
    }

    public List<Map<String, Object>> listarCondiciones() throws SQLException {
        String sql = "SELECT id_condicion, nombre FROM condicion ORDER BY id_condicion";
        return ejecutarConsultaMapa(sql);
    }

    public List<Map<String, Object>> listarColores() throws SQLException {
        String sql = "SELECT id_color, nombre, gama FROM color ORDER BY gama, nombre";
        return ejecutarConsultaMapa(sql);
    }

    public List<Map<String, Object>> listarMarcas() throws SQLException {
        String sql = "SELECT id_marca, nombre, pais_origen FROM marca ORDER BY nombre";
        return ejecutarConsultaMapa(sql);
    }

    public List<Map<String, Object>> listarModelos(Integer idMarca, Integer idTipoVehiculo) throws SQLException {
        StringBuilder sb = new StringBuilder(
                "SELECT m.id_modelo, m.id_marca, ma.nombre AS nombre_marca, " +
                "m.id_tipo_vehiculo, tv.nombre AS nombre_tipo_vehiculo, " +
                "m.denominacion, m.anio_fabricacion, m.precio_lista " +
                "FROM modelo m " +
                "INNER JOIN marca ma ON m.id_marca = ma.id_marca " +
                "INNER JOIN tipo_vehiculo tv ON m.id_tipo_vehiculo = tv.id_tipo_vehiculo " +
                "WHERE 1 = 1 "
        );

        List<Object> params = new ArrayList<>();
        if (idMarca != null) {
            sb.append("AND m.id_marca = ? ");
            params.add(idMarca);
        }
        if (idTipoVehiculo != null) {
            sb.append("AND m.id_tipo_vehiculo = ? ");
            params.add(idTipoVehiculo);
        }
        sb.append("ORDER BY ma.nombre, m.denominacion, m.anio_fabricacion DESC");

        List<Map<String, Object>> filas = new ArrayList<>();
        try (Connection cn = abrirConexion();
             PreparedStatement ps = cn.prepareStatement(sb.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new LinkedHashMap<>();
                    fila.put("id_modelo", rs.getInt("id_modelo"));
                    fila.put("id_marca", rs.getInt("id_marca"));
                    fila.put("nombre_marca", rs.getString("nombre_marca"));
                    fila.put("id_tipo_vehiculo", rs.getInt("id_tipo_vehiculo"));
                    fila.put("nombre_tipo_vehiculo", rs.getString("nombre_tipo_vehiculo"));
                    fila.put("denominacion", rs.getString("denominacion"));
                    fila.put("anio_fabricacion", rs.getInt("anio_fabricacion"));
                    BigDecimal precioLista = rs.getBigDecimal("precio_lista");
                    fila.put("precio_lista", precioLista);
                    filas.add(fila);
                }
            }
        }
        return filas;
    }

    private List<Map<String, Object>> ejecutarConsultaMapa(String sql) throws SQLException {
        List<Map<String, Object>> filas = new ArrayList<>();
        try (Connection cn = abrirConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    fila.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                filas.add(fila);
            }
        }
        return filas;
    }

    private Connection abrirConexion() throws SQLException {
        try {
            return ConexionSQLServer.obtenerConexion();
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC de SQL Server.", e);
        }
    }
}