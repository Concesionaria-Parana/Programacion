package model;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Vehiculo {
    private int idVehiculo;
    private String vin;
    private int idModelo;
    private String nombreModelo;
    private int idMarca;
    private String nombreMarca;
    private int anioFabricacion;
    private int idSucursal;
    private String nombreSucursal;
    private int idColor;
    private String nombreColor;
    private int idCondicion;
    private String nombreCondicion;
    private int idTipoVehiculo;
    private String nombreTipoVehiculo;
    private int kilometraje;
    private BigDecimal precioVenta;

    public Vehiculo() {
    }

    public Vehiculo(
            int idVehiculo,
            String vin,
            int idModelo,
            String nombreModelo,
            int idMarca,
            String nombreMarca,
            int anioFabricacion,
            int idSucursal,
            String nombreSucursal,
            int idColor,
            String nombreColor,
            int idCondicion,
            String nombreCondicion,
            int idTipoVehiculo,
            String nombreTipoVehiculo,
            int kilometraje,
            BigDecimal precioVenta
    ) {
        this.idVehiculo = idVehiculo;
        this.vin = vin;
        this.idModelo = idModelo;
        this.nombreModelo = nombreModelo;
        this.idMarca = idMarca;
        this.nombreMarca = nombreMarca;
        this.anioFabricacion = anioFabricacion;
        this.idSucursal = idSucursal;
        this.nombreSucursal = nombreSucursal;
        this.idColor = idColor;
        this.nombreColor = nombreColor;
        this.idCondicion = idCondicion;
        this.nombreCondicion = nombreCondicion;
        this.idTipoVehiculo = idTipoVehiculo;
        this.nombreTipoVehiculo = nombreTipoVehiculo;
        this.kilometraje = kilometraje;
        this.precioVenta = precioVenta;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(int idModelo) {
        this.idModelo = idModelo;
    }

    public String getNombreModelo() {
        return nombreModelo;
    }

    public void setNombreModelo(String nombreModelo) {
        this.nombreModelo = nombreModelo;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public int getAnioFabricacion() {
        return anioFabricacion;
    }

    public void setAnioFabricacion(int anioFabricacion) {
        this.anioFabricacion = anioFabricacion;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public int getIdColor() {
        return idColor;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public String getNombreColor() {
        return nombreColor;
    }

    public void setNombreColor(String nombreColor) {
        this.nombreColor = nombreColor;
    }

    public int getIdCondicion() {
        return idCondicion;
    }

    public void setIdCondicion(int idCondicion) {
        this.idCondicion = idCondicion;
    }

    public String getNombreCondicion() {
        return nombreCondicion;
    }

    public void setNombreCondicion(String nombreCondicion) {
        this.nombreCondicion = nombreCondicion;
    }

    public int getIdTipoVehiculo() {
        return idTipoVehiculo;
    }

    public void setIdTipoVehiculo(int idTipoVehiculo) {
        this.idTipoVehiculo = idTipoVehiculo;
    }

    public String getNombreTipoVehiculo() {
        return nombreTipoVehiculo;
    }

    public void setNombreTipoVehiculo(String nombreTipoVehiculo) {
        this.nombreTipoVehiculo = nombreTipoVehiculo;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public abstract String getDetalleEspecifico();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehiculo)) return false;
        Vehiculo vehiculo = (Vehiculo) o;
        if (idVehiculo != 0 && vehiculo.idVehiculo != 0) {
            return idVehiculo == vehiculo.idVehiculo;
        }
        return Objects.equals(vin, vehiculo.vin);
    }

    @Override
    public int hashCode() {
        return idVehiculo != 0 ? Objects.hash(idVehiculo) : Objects.hash(vin);
    }
}