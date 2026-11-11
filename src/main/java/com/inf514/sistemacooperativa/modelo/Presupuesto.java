package com.inf514.sistemacooperativa.modelo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Presupuesto asignado a un usuario del sistema.
 *
 * @author EAdon
 */
@Entity
@Table(name = "presupuestos")
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relación: muchos presupuestos pertenecen a un usuario
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "monto_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal montoTotal = BigDecimal.ZERO;

    @Column(name = "monto_gastado", nullable = false, precision = 15, scale = 2)
    private BigDecimal montoGastado = BigDecimal.ZERO;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_fin")
    private Date fechaFin;

    private boolean activo = true;

    public Presupuesto() {
    }

    public Presupuesto(Usuario usuario, String nombre, String descripcion,
            BigDecimal montoTotal, Date fechaInicio, Date fechaFin) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.montoTotal = montoTotal;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // ---- Lógica de negocio ----

    @Transient
    public BigDecimal getMontoDisponible() {
        return montoTotal.subtract(montoGastado);
    }

    /** Registra un gasto; lanza excepción si excede el monto disponible. */
    public void registrarGasto(BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        if (monto.compareTo(getMontoDisponible()) > 0) {
            throw new IllegalStateException("El gasto excede el presupuesto disponible.");
        }
        montoGastado = montoGastado.add(monto);
    }

    // ---- Getters y setters ----

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getMontoGastado() {
        return montoGastado;
    }

    public void setMontoGastado(BigDecimal montoGastado) {
        this.montoGastado = montoGastado;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
