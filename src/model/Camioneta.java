package model;

import java.math.BigDecimal;

public class Camioneta extends Vehiculo {
    private String carroceria;
    private String traccion;

    public Camioneta() {
        super();
    }

    public Camioneta(
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
            BigDecimal precioVenta,
            String carroceria,
            String traccion
    ) {
        super(
                idVehiculo,
                vin,
                idModelo,
                nombreModelo,
                idMarca,
                nombreMarca,
                anioFabricacion,
                idSucursal,
                nombreSucursal,
                idColor,
                nombreColor,
                idCondicion,
                nombreCondicion,
                idTipoVehiculo,
                nombreTipoVehiculo,
                kilometraje,
                precioVenta
        );
        this.carroceria = carroceria;
        this.traccion = traccion;
    }

    public String getCarroceria() {
        return carroceria;
    }

    public void setCarroceria(String carroceria) {
        this.carroceria = carroceria;
    }

    public String getTraccion() {
        return traccion;
    }

    public void setTraccion(String traccion) {
        this.traccion = traccion;
    }

    @Override
    public String getDetalleEspecifico() {
        return "Carrocería: " + carroceria + " | Tracción: " + traccion;
    }
}
