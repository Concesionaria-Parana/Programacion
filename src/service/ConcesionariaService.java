package service;

import db.ConexionSQLServer;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Automovil;
import model.Camioneta;
import model.Cliente;
import model.Motocicleta;
import model.Vehiculo;

public class ConcesionariaService {

    private static final String SQL_INSERT_VEHICULO =
            "INSERT INTO vehiculo " +
            "(vin, id_modelo, id_sucursal, id_color, id_condicion, id_tipo_vehiculo, kilometraje, precio_venta) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_AUTOMOVIL =
            "INSERT INTO automovil (id_vehiculo, carroceria, cantidad_puertas) VALUES (?, ?, ?)";

    private static final String SQL_INSERT_CAMIONETA =
            "INSERT INTO camioneta (id_vehiculo, carroceria, traccion) VALUES (?, ?, ?)";

    private static final String SQL_INSERT_MOTOCICLETA =
            "INSERT INTO motocicleta (id_vehiculo, tipo_motocicleta, cilindrada) VALUES (?, ?, ?)";

    private static final String SQL_SELECT_VEHICULOS =
            "SELECT v.id_vehiculo, v.vin, v.id_modelo, mo.denominacion AS nombre_modelo, " +
            "mo.id_marca, ma.nombre AS nombre_marca, mo.anio_fabricacion, " +
            "v.id_sucursal, s.nombre AS nombre_sucursal, " +
            "v.id_color, co.nombre AS nombre_color, " +
            "v.id_condicion, cond.nombre AS nombre_condicion, " +
            "v.id_tipo_vehiculo, tv.nombre AS nombre_tipo_vehiculo, " +
            "v.kilometraje, v.precio_venta, " +
            "a.carroceria AS auto_carroceria, a.cantidad_puertas AS auto_puertas, " +
            "ca.carroceria AS cam_carroceria, ca.traccion AS cam_traccion, " +
            "m.tipo_motocicleta AS moto_tipo, m.cilindrada AS moto_cilindrada " +
            "FROM vehiculo v " +
            "INNER JOIN modelo mo ON v.id_modelo = mo.id_modelo " +
            "INNER JOIN marca ma ON mo.id_marca = ma.id_marca " +
            "INNER JOIN sucursal s ON v.id_sucursal = s.id_sucursal " +
            "INNER JOIN color co ON v.id_color = co.id_color " +
            "INNER JOIN condicion cond ON v.id_condicion = cond.id_condicion " +
            "INNER JOIN tipo_vehiculo tv ON v.id_tipo_vehiculo = tv.id_tipo_vehiculo " +
            "LEFT JOIN automovil a ON v.id_vehiculo = a.id_vehiculo " +
            "LEFT JOIN camioneta ca ON v.id_vehiculo = ca.id_vehiculo " +
            "LEFT JOIN motocicleta m ON v.id_vehiculo = m.id_vehiculo " +
            "ORDER BY v.id_vehiculo DESC";

    private static final String SQL_INSERT_CLIENTE =
            "INSERT INTO cliente (tipo_documento, numero_documento, nombre_completo, domicilio, telefono, email) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_CLIENTES =
            "SELECT id_cliente, tipo_documento, numero_documento, nombre_completo, domicilio, telefono, email " +
            "FROM cliente ORDER BY nombre_completo";

    public int registrarVehiculo(Vehiculo vehiculo) throws SQLException {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo.");
        }
        if (!(vehiculo instanceof Automovil)
                && !(vehiculo instanceof Camioneta)
                && !(vehiculo instanceof Motocicleta)) {
            throw new IllegalArgumentException("Tipo de vehículo no soportado: " + vehiculo.getClass().getSimpleName());
        }

        Connection cn = null;
        try {
            cn = ConexionSQLServer.getConnection();
            cn.setAutoCommit(false);

            int idGenerado;
            try (PreparedStatement psVehiculo = cn.prepareStatement(SQL_INSERT_VEHICULO, Statement.RETURN_GENERATED_KEYS)) {
                psVehiculo.setString(1, vehiculo.getVin());
                psVehiculo.setInt(2, vehiculo.getIdModelo());
                psVehiculo.setInt(3, vehiculo.getIdSucursal());
                psVehiculo.setInt(4, vehiculo.getIdColor());
                psVehiculo.setInt(5, vehiculo.getIdCondicion());
                psVehiculo.setInt(6, vehiculo.getIdTipoVehiculo());
                psVehiculo.setInt(7, vehiculo.getKilometraje());
                psVehiculo.setBigDecimal(8, vehiculo.getPrecioVenta());

                psVehiculo.executeUpdate();

                try (ResultSet keys = psVehiculo.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No se obtuvo el ID generado para el vehículo.");
                    }
                    idGenerado = keys.getInt(1);
                }
            }

            if (vehiculo instanceof Automovil) {
                Automovil auto = (Automovil) vehiculo;
                try (PreparedStatement psAuto = cn.prepareStatement(SQL_INSERT_AUTOMOVIL)) {
                    psAuto.setInt(1, idGenerado);
                    psAuto.setString(2, auto.getCarroceria());
                    psAuto.setInt(3, auto.getCantidadPuertas());
                    psAuto.executeUpdate();
                }
            } else if (vehiculo instanceof Camioneta) {
                Camioneta cam = (Camioneta) vehiculo;
                try (PreparedStatement psCam = cn.prepareStatement(SQL_INSERT_CAMIONETA)) {
                    psCam.setInt(1, idGenerado);
                    psCam.setString(2, cam.getCarroceria());
                    psCam.setString(3, cam.getTraccion());
                    psCam.executeUpdate();
                }
            } else {
                Motocicleta moto = (Motocicleta) vehiculo;
                try (PreparedStatement psMoto = cn.prepareStatement(SQL_INSERT_MOTOCICLETA)) {
                    psMoto.setInt(1, idGenerado);
                    psMoto.setString(2, moto.getTipoMotocicleta());
                    psMoto.setInt(3, moto.getCilindrada());
                    psMoto.executeUpdate();
                }
            }

            cn.commit();
            vehiculo.setIdVehiculo(idGenerado);
            return idGenerado;
        } catch (SQLException ex) {
            if (cn != null) {
                try {
                    cn.rollback();
                } catch (SQLException ignored) {
                }
            }
            throw ex;
        } finally {
            if (cn != null) {
                try {
                    cn.setAutoCommit(true);
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public List<Vehiculo> listarVehiculos() throws SQLException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        try (Connection cn = ConexionSQLServer.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_SELECT_VEHICULOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                vehiculos.add(mapearVehiculo(rs));
            }
        }
        return vehiculos;
    }

    public int registrarCliente(Cliente cliente) throws SQLException {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }

        try (Connection cn = ConexionSQLServer.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERT_CLIENTE, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getTipoDocumento());
            ps.setString(2, cliente.getNumeroDocumento());
            ps.setString(3, cliente.getNombreCompleto());
            ps.setString(4, cliente.getDomicilio());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getEmail());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int idGenerado = keys.getInt(1);
                    cliente.setIdCliente(idGenerado);
                    return idGenerado;
                }
            }
        }
        return 0;
    }

    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection cn = ConexionSQLServer.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_SELECT_CLIENTES);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setTipoDocumento(rs.getString("tipo_documento"));
                c.setNumeroDocumento(rs.getString("numero_documento"));
                c.setNombreCompleto(rs.getString("nombre_completo"));
                c.setDomicilio(rs.getString("domicilio"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                clientes.add(c);
            }
        }
        return clientes;
    }

    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        int idVehiculo = rs.getInt("id_vehiculo");
        String vin = rs.getString("vin");
        int idModelo = rs.getInt("id_modelo");
        String nombreModelo = rs.getString("nombre_modelo");
        int idMarca = rs.getInt("id_marca");
        String nombreMarca = rs.getString("nombre_marca");
        int anioFabricacion = rs.getInt("anio_fabricacion");
        int idSucursal = rs.getInt("id_sucursal");
        String nombreSucursal = rs.getString("nombre_sucursal");
        int idColor = rs.getInt("id_color");
        String nombreColor = rs.getString("nombre_color");
        int idCondicion = rs.getInt("id_condicion");
        String nombreCondicion = rs.getString("nombre_condicion");
        int idTipoVehiculo = rs.getInt("id_tipo_vehiculo");
        String nombreTipoVehiculo = rs.getString("nombre_tipo_vehiculo");
        int kilometraje = rs.getInt("kilometraje");
        BigDecimal precioVenta = rs.getBigDecimal("precio_venta");

        if (idTipoVehiculo == 1) {
            return new Automovil(
                    idVehiculo, vin, idModelo, nombreModelo, idMarca, nombreMarca,
                    anioFabricacion, idSucursal, nombreSucursal, idColor, nombreColor,
                    idCondicion, nombreCondicion, idTipoVehiculo, nombreTipoVehiculo,
                    kilometraje, precioVenta,
                    rs.getString("auto_carroceria"),
                    rs.getInt("auto_puertas")
            );
        } else if (idTipoVehiculo == 2) {
            return new Camioneta(
                    idVehiculo, vin, idModelo, nombreModelo, idMarca, nombreMarca,
                    anioFabricacion, idSucursal, nombreSucursal, idColor, nombreColor,
                    idCondicion, nombreCondicion, idTipoVehiculo, nombreTipoVehiculo,
                    kilometraje, precioVenta,
                    rs.getString("cam_carroceria"),
                    rs.getString("cam_traccion")
            );
        } else {
            return new Motocicleta(
                    idVehiculo, vin, idModelo, nombreModelo, idMarca, nombreMarca,
                    anioFabricacion, idSucursal, nombreSucursal, idColor, nombreColor,
                    idCondicion, nombreCondicion, idTipoVehiculo, nombreTipoVehiculo,
                    kilometraje, precioVenta,
                    rs.getString("moto_tipo"),
                    rs.getInt("moto_cilindrada")
            );
        }
    }
}