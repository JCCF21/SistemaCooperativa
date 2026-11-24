package com.inf514.sistemacooperativa.dao;

import com.inf514.sistemacooperativa.modelo.PagoPrestamo;
import com.inf514.sistemacooperativa.modelo.Prestamo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;

public class PagoPrestamoDAO {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("CooperativaPU");
//...
    // Obtener el total pagado de un préstamo
    public BigDecimal obtenerTotalPagado(int prestamoId) {

        EntityManager em = emf.createEntityManager();

        try {
            BigDecimal total = em.createQuery(
                    "SELECT SUM(p.montoPagado) FROM PagoPrestamo p "
                    + "WHERE p.prestamo.id = :id",
                    BigDecimal.class)
                    .setParameter("id", prestamoId)
                    .getSingleResult();

            return total != null ? total : BigDecimal.ZERO;

        } finally {
            em.close();
        }
    }

    // Obtener la cantidad de cuotas pagadas
    public int obtenerCuotasPagadas(int prestamoId) {

        EntityManager em = emf.createEntityManager();

        try {
            Long cuotas = em.createQuery(
                    "SELECT SUM(p.cantidadCuotas) FROM PagoPrestamo p "
                    + "WHERE p.prestamo.id = :id",
                    Long.class)
                    .setParameter("id", prestamoId)
                    .getSingleResult();

            return cuotas != null ? cuotas.intValue() : 0;

        } finally {
            em.close();
        }
    }

    // Obtener el historial de pagos de un préstamo
    public List<PagoPrestamo> obtenerPagos(int prestamoId) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM PagoPrestamo p "
                    + "WHERE p.prestamo.id = :id "
                    + "ORDER BY p.id ASC",
                    PagoPrestamo.class)
                    .setParameter("id", prestamoId)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    // Registrar un nuevo pago
    public void registrarPago(int prestamoId, int cantidadCuotas) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Prestamo prestamo = em.find(
                    Prestamo.class,
                    prestamoId,
                    LockModeType.PESSIMISTIC_WRITE);

            if (prestamo == null) {
                throw new IllegalStateException(
                        "El préstamo no existe.");
            }

            if (!"Aprobado".equals(prestamo.getEstado())) {
                throw new IllegalStateException(
                        "Solo se pueden pagar préstamos aprobados.");
            }

            Long cuotasRegistradas = em.createQuery(
                    "SELECT SUM(p.cantidadCuotas) FROM PagoPrestamo p "
                    + "WHERE p.prestamo.id = :id",
                    Long.class)
                    .setParameter("id", prestamoId)
                    .getSingleResult();

            int cuotasPagadas = cuotasRegistradas != null
                    ? cuotasRegistradas.intValue() : 0;

            int cuotasRestantes =
                    prestamo.getCantidadCuotas() - cuotasPagadas;

            if (cantidadCuotas <= 0
                    || cantidadCuotas > cuotasRestantes) {

                throw new IllegalArgumentException(
                        "La cantidad de cuotas no es válida.");
            }

            BigDecimal totalPagado = em.createQuery(
                    "SELECT SUM(p.montoPagado) FROM PagoPrestamo p "
                    + "WHERE p.prestamo.id = :id",
                    BigDecimal.class)
                    .setParameter("id", prestamoId)
                    .getSingleResult();

            if (totalPagado == null) {
                totalPagado = BigDecimal.ZERO;
            }

            BigDecimal saldoPendiente =
                    prestamo.getTotalPagar().subtract(totalPagado);

            BigDecimal montoPago = prestamo.getCuotaMensual()
                    .multiply(BigDecimal.valueOf(cantidadCuotas));

            // Ajustar el último pago por diferencias de redondeo
            if (cantidadCuotas == cuotasRestantes) {
                montoPago = saldoPendiente;
            }

            if (montoPago.signum() <= 0
                    || montoPago.compareTo(saldoPendiente) > 0) {

                throw new IllegalStateException(
                        "El monto del pago no es válido.");
            }

            PagoPrestamo pago = new PagoPrestamo();

            pago.setPrestamo(prestamo);
            pago.setFechaPago(LocalDate.now());
            pago.setCantidadCuotas(cantidadCuotas);
            pago.setMontoPagado(montoPago);

            em.persist(pago);

            BigDecimal nuevoSaldo =
                    saldoPendiente.subtract(montoPago);

            // Cambiar el estado cuando se liquide el préstamo
            if (nuevoSaldo.signum() == 0) {
                prestamo.setEstado("Pagado");
            }

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
