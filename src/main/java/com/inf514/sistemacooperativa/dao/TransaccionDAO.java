/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inf514.sistemacooperativa.dao;

import com.inf514.sistemacooperativa.modelo.Transacciones;
import com.inf514.sistemacooperativa.Cuenta; // Asegúrate de importar tu entidad Cuenta
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Usuario
 */
public class TransaccionDAO {
 
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("CooperativaPU");

    public void guardarTransaccion(Transacciones transaccion) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            // 1. VERIFICAR SI LA CUENTA EXISTE Y OBTENER SU OBJETO
            Cuenta cuenta = em.find(Cuenta.class, transaccion.getIdCuenta());
            if (cuenta == null) {
                throw new IllegalArgumentException("El ID de cuenta (" + transaccion.getIdCuenta() + ") no está registrado en el sistema.");
            }

            // 2. VALIDACIÓN DE REGLA DE NEGOCIO: Depósito o Retiro
            String tipoUpper = (transaccion.getTipo() != null) ? transaccion.getTipo().toUpperCase() : "";
            
            if (tipoUpper.contains("RETIRO")) {
                if (transaccion.getMonto() > cuenta.getSaldo()) {
                    throw new IllegalArgumentException("Fondos insuficientes. Saldo actual disponible: $" + cuenta.getSaldo());
                }
                // Restar al saldo de la cuenta
                cuenta.setSaldo(cuenta.getSaldo() - transaccion.getMonto());
                
            } else if (tipoUpper.contains("DEPÓSITO") || tipoUpper.contains("DEPOSITO")) {
                // Sumar al saldo de la cuenta
                cuenta.setSaldo(cuenta.getSaldo() + transaccion.getMonto());
            }

            // 3. GUARDAR LA TRANSACCIÓN Y ACTUALIZAR LA CUENTA
            if (transaccion.getIdTransaccion() == 0) {
                em.persist(transaccion);   // Nuevo registro (INSERT)
            } else {
                em.merge(transaccion);     // Actualización (UPDATE)
            }
            
            em.merge(cuenta); // Guardar el nuevo saldo actualizado en la tabla Cuenta
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Lanzamos el mensaje limpio para que lo atrape el JOptionPane de tu FrmTransacciones
            throw new RuntimeException(e.getMessage());
        } finally {
            em.close();
        }
    }

    public Transacciones buscarPorId(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Transacciones.class, id);
        } finally {
            em.close();
        }
    }

    public List<Transacciones> listarPorCuenta(int idCuenta) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT t FROM Transacciones t WHERE t.idCuenta = :idCuenta",
                    Transacciones.class)
                    .setParameter("idCuenta", idCuenta)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }
}