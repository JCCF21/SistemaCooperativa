package com.inf514.sistemacooperativa.dao;

import com.inf514.sistemacooperativa.modelo.CuentaAhorro;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class CuentaAhorroDAO {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("CooperativaPU");

    public void guardar(CuentaAhorro cuenta) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void modificar(CuentaAhorro cuenta) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public CuentaAhorro buscarPorId(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(CuentaAhorro.class, id);
        } finally {
            em.close();
        }
    }

    public List<CuentaAhorro> obtenerCuentas() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<CuentaAhorro> query = em.createQuery(
                    "SELECT c FROM CuentaAhorro c JOIN FETCH c.miembro m ORDER BY m.nombreCompleto, c.tipoCuenta",
                    CuentaAhorro.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<CuentaAhorro> buscarPorMiembro(int idMiembro) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<CuentaAhorro> query = em.createQuery(
                    "SELECT c FROM CuentaAhorro c WHERE c.miembro.id = :idMiembro",
                    CuentaAhorro.class
            );
            query.setParameter("idMiembro", idMiembro);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void depositar(int idCuenta, double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del depósito debe ser mayor que cero.");
        }
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            CuentaAhorro cuenta = em.find(CuentaAhorro.class, idCuenta);
            if (cuenta == null) {
                throw new IllegalArgumentException("Cuenta de ahorro no encontrada.");
            }
            if (!cuenta.isActivo()) {
                throw new IllegalStateException("La cuenta de ahorro está inactiva.");
            }
            cuenta.setSaldo(cuenta.getSaldo() + monto);
            em.merge(cuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void retirar(int idCuenta, double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del retiro debe ser mayor que cero.");
        }
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            CuentaAhorro cuenta = em.find(CuentaAhorro.class, idCuenta);
            if (cuenta == null) {
                throw new IllegalArgumentException("Cuenta de ahorro no encontrada.");
            }
            if (!cuenta.isActivo()) {
                throw new IllegalStateException("La cuenta de ahorro está inactiva.");
            }
            if (cuenta.getSaldo() < monto) {
                throw new IllegalArgumentException("Saldo insuficiente en la cuenta.");
            }
            cuenta.setSaldo(cuenta.getSaldo() - monto);
            em.merge(cuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void cambiarEstado(int idCuenta, boolean nuevoEstado) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            CuentaAhorro cuenta = em.find(CuentaAhorro.class, idCuenta);
            if (cuenta != null) {
                cuenta.setActivo(nuevoEstado);
                em.merge(cuenta);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public double obtenerSaldoTotal() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT COALESCE(SUM(c.saldo), 0.0) FROM CuentaAhorro c WHERE c.activo = true",
                    Double.class
            );
            Double total = query.getSingleResult();
            return total != null ? total : 0.0;
        } finally {
            em.close();
        }
    }
}