package com.inf514.sistemacooperativa.dao;

import com.inf514.sistemacooperativa.modelo.Miembro;
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
            throw e; // la vista muestra el error en lugar de decir que se guardó
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

    // ------------------------------------------------------------------
    // Miembros (entidad Miembro; ya no existe MiembroDAO)
    // ------------------------------------------------------------------

    /** Lista todos los miembros registrados. */
    public List<Miembro> listarMiembros() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Miembro m ORDER BY m.nombreCompleto",
                            Miembro.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** Busca un miembro por nombre completo (sin distinguir mayúsculas). Null si no existe. */
    public Miembro buscarMiembroPorNombre(String nombre) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Miembro> r = em.createQuery(
                            "SELECT m FROM Miembro m WHERE LOWER(TRIM(m.nombreCompleto)) = :nombre",
                            Miembro.class)
                    .setParameter("nombre", nombre.trim().toLowerCase())
                    .setMaxResults(1)
                    .getResultList();
            return r.isEmpty() ? null : r.get(0);
        } finally {
            em.close();
        }
    }

    // ------------------------------------------------------------------
    // Presupuestos / gastos asociados a miembros
    // ------------------------------------------------------------------

    /** Presupuestos y gastos activos que están asociados a algún miembro. */
    public List<Presupuesto> listarPresupuestosDeMiembros() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Presupuesto p JOIN FETCH p.miembro WHERE p.activo = true",
                            Presupuesto.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** Elimina todos los presupuestos adicionales y gastos de un miembro. */
    public void eliminarPresupuestosDeMiembro(int miembroId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Presupuesto p WHERE p.miembro.id = :id")
                    .setParameter("id", miembroId)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }
}