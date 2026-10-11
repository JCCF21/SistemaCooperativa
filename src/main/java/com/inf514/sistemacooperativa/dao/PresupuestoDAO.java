package com.inf514.sistemacooperativa.dao;

import com.inf514.sistemacooperativa.modelo.Presupuesto;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

//@author EAdom

public class PresupuestoDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("CooperativaPU");

    public void guardarPresupuesto(Presupuesto presupuesto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (presupuesto.getId() == 0) {
                em.persist(presupuesto);   // nuevo: INSERT
            } else {
                em.merge(presupuesto);     // existente: UPDATE
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

    public Presupuesto buscarPorId(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Presupuesto.class, id);
        } finally {
            em.close();
        }
    }

    public List<Presupuesto> listarPorUsuario(int usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Presupuesto p WHERE p.usuario.id = :usuarioId AND p.activo = true",
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

    public void eliminarPresupuesto(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Presupuesto p = em.find(Presupuesto.class, id);
            if (p != null) {
                em.remove(p);
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
