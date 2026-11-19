package model;

import java.math.BigDecimal;

public class Automovil extends Vehiculo {
    private String carroceria;
    private int cantidadPuertas;

    public Automovil() {
        super();
    }

    public Automovil(
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
            int cantidadPuertas
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
        this.cantidadPuertas = cantidadPuertas;
    }

    public String getCarroceria() {
        return carroceria;
    }

    public void setCarroceria(String carroceria) {
        this.carroceria = carroceria;
    }

    public int getCantidadPuertas() {
        return cantidadPuertas;
    }

    public void setCantidadPuertas(int cantidadPuertas) {
        this.cantidadPuertas = cantidadPuertas;
    }

    @Override
    public String getDetalleEspecifico() {
        return "Carrocería: " + carroceria + " | Puertas: " + cantidadPuertas;
    }
}