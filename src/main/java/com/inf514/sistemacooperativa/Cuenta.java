/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inf514.sistemacooperativa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "cuenta") 
public class Cuenta implements Serializable {

    @Id
    @Column(name = "id_cuenta")
    private int idCuenta;

    @Column(name = "saldo")
    private double saldo;

    // Constructor vacío 
    public Cuenta() {
    }

    // Constructor con parámetros
    public Cuenta(int idCuenta, double saldo) {
        this.idCuenta = idCuenta;
        this.saldo = saldo;
    }

    // --- Getters y Setters ---

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Cuenta [ID=" + idCuenta + ", Saldo=" + saldo + "]";
    }
}