
package com.inf514.sistemacooperativa.dao;

import com.inf514.sistemacooperativa.modelo.Prestamo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import com.inf514.sistemacooperativa.modelo.Miembro;
import javax.persistence.LockModeType;

public class PrestamoDAO {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("CooperativaPU");

    // Guardar una nueva solicitud de préstamo
    public void guardar(Prestamo prestamo) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

Miembro miembro = em.find(
        Miembro.class,
        prestamo.getMiembro().getId(),
        LockModeType.PESSIMISTIC_WRITE);

if (miembro == null || !miembro.isActivo()) {

    throw new IllegalStateException(
            "El miembro está inactivo o no existe. "
            + "No puede solicitar préstamos.");
}

prestamo.setMiembro(miembro);

em.persist(prestamo);

            em.getTransaction().commit();

        } catch (RuntimeException e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    // Obtener todos los préstamos registrados
    public List<Prestamo> obtenerPrestamos() {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Prestamo p ORDER BY p.id ASC",
                    Prestamo.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    // Buscar un préstamo por su ID
    public Prestamo buscarPorId(int id) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Prestamo.class, id);

        } finally {
            em.close();
        }
        
        
    }
    public boolean cambiarEstado(int id, String nuevoEstado) {

    if (!"Aprobado".equals(nuevoEstado)
            && !"Rechazado".equals(nuevoEstado)) {
        return false;
    }

    EntityManager em = emf.createEntityManager();

    try {
        em.getTransaction().begin();

        Prestamo prestamo = em.find(
                Prestamo.class, id,
                javax.persistence.LockModeType.PESSIMISTIC_WRITE);

        if (prestamo == null
                || !"En espera".equals(prestamo.getEstado())) {

            em.getTransaction().rollback();
            return false;
        }

        if ("Aprobado".equals(nuevoEstado)) {

    Miembro miembro = prestamo.getMiembro();

    em.refresh(miembro, LockModeType.PESSIMISTIC_WRITE);

    if (!miembro.isActivo()) {

        throw new IllegalStateException(
                "No se puede aprobar el préstamo "
                + "porque el miembro está inactivo.");
    }
}
        prestamo.setEstado(nuevoEstado);

        em.getTransaction().commit();

        return true;

    } catch (RuntimeException e) {

        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }

        throw e;

    } finally {
        em.close();
    }
}
    
    public void modificar(Prestamo datosNuevos) {

    EntityManager em = emf.createEntityManager();

    try {
        em.getTransaction().begin();

        Prestamo prestamo = em.find(
                Prestamo.class,
                datosNuevos.getId(),
                LockModeType.PESSIMISTIC_WRITE);

        if (prestamo == null) {
            throw new IllegalStateException(
                    "El préstamo no existe.");
        }

        if (!"En espera".equals(prestamo.getEstado())) {
            throw new IllegalStateException(
                    "Solo se pueden modificar solicitudes en espera.");
        }

        Miembro miembro = prestamo.getMiembro();

        em.refresh(miembro, LockModeType.PESSIMISTIC_WRITE);

        if (!miembro.isActivo()) {
            throw new IllegalStateException(
                    "No se puede modificar el préstamo "
                    + "porque el miembro está inactivo.");
        }

        prestamo.setMontoSolicitado(datosNuevos.getMontoSolicitado());
        prestamo.setCantidadCuotas(datosNuevos.getCantidadCuotas());
        prestamo.setTasaInteres(datosNuevos.getTasaInteres());
        prestamo.setInteresTotal(datosNuevos.getInteresTotal());
        prestamo.setTotalPagar(datosNuevos.getTotalPagar());
        prestamo.setCuotaMensual(datosNuevos.getCuotaMensual());

        em.getTransaction().commit();

    } catch (RuntimeException e) {

        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }

        throw e;

    } finally {
        em.close();
    }
}
}
