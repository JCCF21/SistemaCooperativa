package com.inf514.sistemacooperativa.modelo;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author jfria
 */
@Entity
@Table(name= "miembros")
public class Miembro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(unique = true, nullable = false, length = 15)
    private String cedula;
    
    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;
    
    @Column(length = 15)
    private String telefono;
    
    @Column(length = 100)
    private String correo;
    
    @Column(length = 250)
    private String direccion;
    
    @Column(name = "fecha_nacimiento" )
    private LocalDate fechaNacimiento; // Cambiado a LocalDate
    
    @Column(length = 100)
    private String ocupacion;
    
    @Column(name = "ingreso_mensual")
    private double ingresoMensual;
    
    @Column(length = 100)
    private String beneficiario; // Agregado para coincidir con el registro
    
    @Column(nullable = false)
    private boolean activo;
    
    public Miembro(){
        
    }

    // CONSTRUCTOR SIN ID (Exactamente el que necesita tu ventana DlgNuevoMiembro)
    public Miembro(String cedula, String nombreCompleto, String telefono, String correo, String direccion, LocalDate fechaNacimiento, String ocupacion, double ingresoMensual, String beneficiario, boolean activo) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.ocupacion = ocupacion;
        this.ingresoMensual = ingresoMensual;
        this.beneficiario = beneficiario;
        this.activo = activo;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public double getIngresoMensual() {
        return ingresoMensual;
    }

    public void setIngresoMensual(double ingresoMensual) {
        this.ingresoMensual = ingresoMensual;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }
            
}