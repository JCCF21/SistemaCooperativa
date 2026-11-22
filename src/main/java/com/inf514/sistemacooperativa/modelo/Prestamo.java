package com.inf514.sistemacooperativa.modelo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "miembro_id", nullable = false)
    private Miembro miembro;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Miembro getMiembro() {
        return miembro;
    }

    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
    }

    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(BigDecimal montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public int getCantidadCuotas() {
        return cantidadCuotas;
    }

    public void setCantidadCuotas(int cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
    }

    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public BigDecimal getInteresTotal() {
        return interesTotal;
    }

    public void setInteresTotal(BigDecimal interesTotal) {
        this.interesTotal = interesTotal;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public BigDecimal getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(BigDecimal cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Column(name = "monto_solicitado", nullable = false,
            precision = 15, scale = 2)
    private BigDecimal montoSolicitado;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Column(name = "cantidad_cuotas", nullable = false)
    private int cantidadCuotas;

    @Column(name = "tasa_interes", nullable = false,
            precision = 5, scale = 2)
    private BigDecimal tasaInteres;

    @Column(name = "interes_total", nullable = false,
            precision = 15, scale = 2)
    private BigDecimal interesTotal;

    @Column(name = "total_pagar", nullable = false,
            precision = 15, scale = 2)
    private BigDecimal totalPagar;

    @Column(name = "cuota_mensual", nullable = false,
            precision = 15, scale = 2)
    private BigDecimal cuotaMensual;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "En espera";

    public Prestamo() {
    }
}