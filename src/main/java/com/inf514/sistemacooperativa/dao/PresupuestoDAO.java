package com.inf514.sistemacooperativa.dao;

import com.inf514.sistemacooperativa.modelo.Presupuesto;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.NoResultException;

// @author EAdon

public class PresupuestoDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("CooperativaPU");

    // Guardar o actualizar presupuesto
    public void guardarPresupuesto(Presupuesto presupuesto) {

        if (presupuesto == null) {
            throw new IllegalArgumentException(
                    "El presupuesto no puede ser nulo.");
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            if (presupuesto.getId() == 0) {
                em.persist(presupuesto);
            } else {
                em.merge(presupuesto);
            }

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();

        } finally {
            em.close();
        }
    }

    // Buscar presupuesto por ID
    public Presupuesto buscarPorId(int id) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT p FROM Presupuesto p "
                                    + "JOIN FETCH p.usuario "
                                    + "WHERE p.id = :id",
                            Presupuesto.class)
                    .setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {

            return null;

        } finally {
            em.close();
        }
    }

    // Listar presupuestos activos de un usuario
    public List<Presupuesto> listarPorUsuario(int usuarioId) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT p FROM Presupuesto p "
                                    + "JOIN FETCH p.usuario "
                                    + "WHERE p.usuario.id = :usuarioId "
                                    + "AND p.activo = true",
                            Presupuesto.class)
                    .setParameter("usuarioId", usuarioId)
                    .getResultList();

        } catch (Exception e) {

            e.printStackTrace();
            return Collections.emptyList();

        } finally {
            em.close();
        }
    }

    // Eliminar presupuesto (eliminación lógica)
    public void eliminarPresupuesto(int id) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Presupuesto p = em.find(Presupuesto.class, id);

            if (p != null) {
                p.setActivo(false);
            }

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();

        } finally {
            em.close();
        }
    }
}