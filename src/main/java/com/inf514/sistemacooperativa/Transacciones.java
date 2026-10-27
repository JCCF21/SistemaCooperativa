package com.inf514.sistemacooperativa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transacciones {

    private int idTransaccion;
    private int idCuenta;
    private String tipo; //  "DEPÓSITO", "RETIRO"...
    private double monto;
    private String fecha;
    private String descripcion;
    private Integer idAdmin;

    // Constructor vacío
    public Transacciones() {
        // Asignamos la fecha actual por defecto al crear el objeto
        this.fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // Constructor con todos los parámetros
    public Transacciones(int idTransaccion, int idCuenta, String tipo, double monto, String fecha, String descripcion, Integer idAdmin) {
        this.idTransaccion = idTransaccion;
        this.idCuenta = idCuenta;
        this.tipo = tipo;
        setMonto(monto); // Usamos el setter para validar que el monto sea positivo
        this.fecha = (fecha != null && !fecha.isEmpty()) ? fecha : LocalDate.now().toString();
        this.descripcion = descripcion;
        this.idAdmin = idAdmin;
    }

    // --- Métodos Getters y Setters ---

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        if (monto > 0) {
            this.monto = monto;
        } else {
            throw new IllegalArgumentException("El monto de la transacción debe ser mayor a cero.");
        }
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Integer idAdmin) {
        this.idAdmin = idAdmin;
    }

    // --- Método adicional para apoyo en reportes o consola ---
    @Override
    public String toString() {
        return "Transacción [" +
                "ID=" + idTransaccion +
                ", Cuenta=" + idCuenta +
                ", Tipo='" + tipo + '\'' +
                ", Monto=" + monto +
                ", Fecha='" + fecha + '\'' +
                ", Descripción='" + descripcion + '\'' +
                ']';
    }
}