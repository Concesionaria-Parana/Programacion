package model;

import java.math.BigDecimal;

public class Motocicleta extends Vehiculo {
    private String tipoMotocicleta;
    private int cilindrada;

    public Motocicleta() {
        super();
    }

    public Motocicleta(
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
            String tipoMotocicleta,
            int cilindrada
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
        this.tipoMotocicleta = tipoMotocicleta;
        this.cilindrada = cilindrada;
    }

    public String getTipoMotocicleta() {
        return tipoMotocicleta;
    }

    public void setTipoMotocicleta(String tipoMotocicleta) {
        this.tipoMotocicleta = tipoMotocicleta;
    }

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    @Override
    public String getDetalleEspecifico() {
        return "Tipo: " + tipoMotocicleta + " | Cilindrada: " + cilindrada + " cc";
    }
}