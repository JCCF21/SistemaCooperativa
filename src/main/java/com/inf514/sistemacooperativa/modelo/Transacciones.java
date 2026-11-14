/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inf514.sistemacooperativa.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "transacciones") // Nombre exacto de mi tabla (XAMPP)
public class Transacciones implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Si el ID es autoincrementable en la base de datos
    @Column(name = "id_transaccion")
    private int idTransaccion;

    @Column(name = "id_cuenta")
    private int idCuenta;

    @Column(name = "tipo")
    private String tipo; // "DEPÓSITO", "RETIRO"...

    @Column(name = "monto")
    private double monto;

    @Column(name = "fecha")
    private String fecha;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "id_admin")
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

    // --- Método toString  ---
    @Override
    public String toString() {
        return "Transacción:" +
                "• ID: " + idTransaccion + "\n" +
                "• Cuenta: " + idCuenta + "\n" +
                "• Tipo: " + tipo + "\n" +
                "• Monto: " + monto + "\n" +
                "• Fecha: " + fecha + "\n" +
                "• Descripción: " + descripcion;
    }
}
