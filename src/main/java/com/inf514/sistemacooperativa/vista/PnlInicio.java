
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.inf514.sistemacooperativa.vista;

import com.inf514.sistemacooperativa.dao.CuentaAhorroDAO;
import com.inf514.sistemacooperativa.dao.MiembroDAO;
import com.inf514.sistemacooperativa.dao.PrestamoDAO;
import com.inf514.sistemacooperativa.dao.PresupuestoDAO;
import com.inf514.sistemacooperativa.modelo.CuentaAhorro;
import com.inf514.sistemacooperativa.modelo.Miembro;
import com.inf514.sistemacooperativa.modelo.Prestamo;
import com.inf514.sistemacooperativa.modelo.Presupuesto;
import com.inf514.sistemacooperativa.utilidades.ConfiguracionGlobal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PnlInicio extends JPanel {

    // Paleta de colores del sistema
    private static final Color COLOR_FONDO = new Color(244, 239, 230);
    private static final Color COLOR_OSCURO = new Color(24, 75, 95);
    private static final Color COLOR_CLARO = new Color(221, 238, 249);
    private static final Color COLOR_VERDE = new Color(46, 139, 111);
    private static final Color COLOR_ROJO = new Color(196, 77, 77);
    private static final Color COLOR_GRIS_OSCURO = new Color(52, 73, 94);
    private static final Color COLOR_BORDE = new Color(220, 220, 220);

    private final MiembroDAO miembroDAO = new MiembroDAO();
    private final CuentaAhorroDAO ahorroDAO = new CuentaAhorroDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final PresupuestoDAO presupuestoDAO = new PresupuestoDAO();

    private final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

    // Labels de las 4 tarjetas KPI
    private JLabel lblSociosValor, lblSociosSub;
    private JLabel lblAhorrosValor, lblAhorrosSub;
    private JLabel lblPrestamosValor, lblPrestamosSub;
    private JLabel lblGananciaValor, lblGananciaSub;

    // Tabla de préstamos pendientes
    private JTable tblPendientes;
    private DefaultTableModel modeloPendientes;

    // Labels del Resumen de Presupuesto
    private JLabel lblAporteBaseValor;
    private JLabel lblConInteresValor;
    private JLabel lblDisponibleValor;
    private JLabel lblGastadoValor;

    public PnlInicio() {
        construirInterfaz();
        cargarDatos();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_FONDO);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        // 1. Parte superior: Título + Botón Actualizar + 4 Tarjetas KPI
        JPanel pnlSuperior = new JPanel(new BorderLayout(0, 15));
        pnlSuperior.setOpaque(false);
        pnlSuperior.add(crearEncabezado(), BorderLayout.NORTH);
        pnlSuperior.add(crearPanelKPIs(), BorderLayout.CENTER);

        // 2. Parte central: Dos columnas (Tabla a la izquierda, Resumen a la derecha)
        JPanel pnlCentral = new JPanel(new BorderLayout(18, 0));
        pnlCentral.setOpaque(false);
        pnlCentral.add(crearPanelPendientes(), BorderLayout.CENTER);
        pnlCentral.add(crearPanelResumenPresupuesto(), BorderLayout.EAST);

        add(pnlSuperior, BorderLayout.NORTH);
        add(pnlCentral, BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("Resumen General de la Cooperativa");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_OSCURO);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(COLOR_OSCURO);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActualizar.setPreferredSize(new Dimension(125, 34));
        btnActualizar.addActionListener(e -> cargarDatos());

        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(btnActualizar, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelKPIs() {
        JPanel grid = new JPanel(new GridLayout(1, 4, 15, 0));
        grid.setOpaque(false);
        grid.setPreferredSize(new Dimension(0, 105));

        lblSociosValor = new JLabel("0");
        lblSociosSub = new JLabel("0 inactivos");
        grid.add(crearTarjetaKPI("Socios Activos", lblSociosValor, lblSociosSub, COLOR_VERDE));

        lblAhorrosValor = new JLabel("RD$ 0.00");
        lblAhorrosSub = new JLabel("0 cuentas activas");
        grid.add(crearTarjetaKPI("Total Ahorrado", lblAhorrosValor, lblAhorrosSub, COLOR_OSCURO));

        lblPrestamosValor = new JLabel("RD$ 0.00");
        lblPrestamosSub = new JLabel("0 en espera");
        grid.add(crearTarjetaKPI("Capital Prestado", lblPrestamosValor, lblPrestamosSub, COLOR_ROJO));

        lblGananciaValor = new JLabel("RD$ 0.00");
        lblGananciaSub = new JLabel("Intereses proyectados");
        grid.add(crearTarjetaKPI("Ganancia Estimada", lblGananciaValor, lblGananciaSub, COLOR_GRIS_OSCURO));

        return grid;
    }

    private JPanel crearTarjetaKPI(String titulo, JLabel lblValor, JLabel lblSub, Color colorBorde) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(new CompoundBorder(
                new MatteBorder(0, 5, 0, 0, colorBorde),
                new EmptyBorder(12, 14, 12, 12)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitulo.setForeground(new Color(90, 90, 90));

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValor.setForeground(Color.BLACK);
        lblValor.setBorder(new EmptyBorder(6, 0, 6, 0));

        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(110, 110, 110));

        tarjeta.add(lblTitulo);
        tarjeta.add(lblValor);
        tarjeta.add(lblSub);
        return tarjeta;
    }

    private JPanel crearPanelPendientes() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDE, 1),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel lblTitulo = new JLabel("Solicitudes de Préstamo Pendientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.BLACK);

        String[] columnas = {"ID", "Cédula", "Miembro", "Monto", "Cuotas", "Estado"};
        modeloPendientes = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPendientes = new JTable(modeloPendientes);
        tblPendientes.setRowHeight(26);
        tblPendientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPendientes.getTableHeader().setReorderingAllowed(false);
        tblPendientes.getTableHeader().setBackground(COLOR_OSCURO);
        tblPendientes.getTableHeader().setForeground(Color.WHITE);
        tblPendientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Colorear la columna "Estado" en amarillo suave
        tblPendientes.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(table.getFont().deriveFont(Font.BOLD));
                if (!isSelected) {
                    setBackground(new Color(255, 240, 194));
                    setForeground(new Color(137, 102, 0));
                }
                return this;
            }
        });

        // Anchos proporcionales
        tblPendientes.getColumnModel().getColumn(0).setPreferredWidth(45);
        tblPendientes.getColumnModel().getColumn(1).setPreferredWidth(110);
        tblPendientes.getColumnModel().getColumn(2).setPreferredWidth(160);
        tblPendientes.getColumnModel().getColumn(3).setPreferredWidth(110);
        tblPendientes.getColumnModel().getColumn(4).setPreferredWidth(60);
        tblPendientes.getColumnModel().getColumn(5).setPreferredWidth(90);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(new JScrollPane(tblPendientes), BorderLayout.CENTER);
        return card;
    }

    private JPanel crearPanelResumenPresupuesto() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(330, 0));
        card.setBorder(new LineBorder(COLOR_BORDE, 1));

        // Encabezado celeste
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_CLARO);
        header.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel lblTitulo = new JLabel("Resumen de Presupuesto");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(COLOR_OSCURO);
        header.add(lblTitulo, BorderLayout.WEST);

        // Cuerpo con las 4 filas
        JPanel cuerpo = new JPanel(new GridLayout(4, 1, 0, 0));
        cuerpo.setBackground(Color.WHITE);
        cuerpo.setBorder(new EmptyBorder(6, 16, 10, 16));

        lblAporteBaseValor = new JLabel("RD$ 0.00");
        lblConInteresValor = new JLabel("RD$ 0.00");
        lblDisponibleValor = new JLabel("RD$ 0.00");
        lblGastadoValor = new JLabel("RD$ 0.00");

        cuerpo.add(crearFilaResumen("Aporte Base", lblAporteBaseValor, true));
        cuerpo.add(crearFilaResumen("Con Interés (8%)", lblConInteresValor, true));
        cuerpo.add(crearFilaResumen("Disponible (40%)", lblDisponibleValor, true));
        cuerpo.add(crearFilaResumen("Gastado", lblGastadoValor, false));

        card.add(header, BorderLayout.NORTH);
        card.add(cuerpo, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearFilaResumen(String etiqueta, JLabel lblValor, boolean conDivisor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setBackground(Color.WHITE);
        if (conDivisor) {
            fila.setBorder(new MatteBorder(0, 0, 1, 0, new Color(235, 235, 235)));
        }

        JLabel lblNombre = new JLabel(etiqueta);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNombre.setForeground(new Color(60, 60, 60));

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblValor.setForeground(Color.BLACK);
        lblValor.setHorizontalAlignment(SwingConstants.RIGHT);

        fila.add(lblNombre, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        return fila;
    }

    // ------------------------------------------------------------------
    // Carga de datos en tiempo real desde la base de datos
    // ------------------------------------------------------------------
    private void cargarDatos() {
        try {
            // 1. Miembros
            List<Miembro> miembros = miembroDAO.obtenerMiembros();
            int activos = 0;
            int inactivos = 0;
            for (Miembro m : miembros) {
                if (m.isActivo()) {
                    activos++;
                } else {
                    inactivos++;
                }
            }
            lblSociosValor.setText(String.valueOf(activos));
            lblSociosSub.setText(inactivos + (inactivos == 1 ? " inactivo" : " inactivos"));

            // 2. Ahorros
            double totalAhorrado = ahorroDAO.obtenerSaldoTotal();
            int cuentasActivas = 0;
            for (CuentaAhorro c : ahorroDAO.obtenerCuentas()) {
                if (c.isActivo()) {
                    cuentasActivas++;
                }
            }
            lblAhorrosValor.setText("RD$ " + formatoMoneda.format(totalAhorrado));
            lblAhorrosSub.setText(cuentasActivas + (cuentasActivas == 1 ? " cuenta activa" : " cuentas activas"));

            // 3. Préstamos (Capital prestado + Tabla de pendientes + Intereses)
            List<Prestamo> prestamos = prestamoDAO.obtenerPrestamos();
            BigDecimal capitalPrestado = BigDecimal.ZERO;
            BigDecimal interesesPrestamos = BigDecimal.ZERO;
            int enEspera = 0;

            modeloPendientes.setRowCount(0);
            for (Prestamo p : prestamos) {
                String estado = p.getEstado() != null ? p.getEstado() : "";
                if ("Aprobado".equalsIgnoreCase(estado)) {
                    if (p.getMontoSolicitado() != null) {
                        capitalPrestado = capitalPrestado.add(p.getMontoSolicitado());
                    }
                    if (p.getInteresTotal() != null) {
                        interesesPrestamos = interesesPrestamos.add(p.getInteresTotal());
                    }
                } else if ("En espera".equalsIgnoreCase(estado)) {
                    enEspera++;
                    String cedula = p.getMiembro() != null ? p.getMiembro().getCedula() : "";
                    String nombre = p.getMiembro() != null ? p.getMiembro().getNombreCompleto() : "";
                    String montoStr = p.getMontoSolicitado() != null
                            ? "RD$ " + formatoMoneda.format(p.getMontoSolicitado())
                            : "RD$ 0.00";
                    modeloPendientes.addRow(new Object[]{
                        p.getId(),
                        cedula,
                        nombre,
                        montoStr,
                        p.getCantidadCuotas(),
                        estado
                    });
                }
            }
            lblPrestamosValor.setText("RD$ " + formatoMoneda.format(capitalPrestado));
            lblPrestamosSub.setText(enEspera + " en espera");

            // 4. Presupuesto (misma lógica de PnlPresupuesto para miembros activos)
            Map<Integer, BigDecimal> extras = new HashMap<>();
            Map<Integer, BigDecimal> gastos = new HashMap<>();
            for (Presupuesto pr : presupuestoDAO.listarPresupuestosDeMiembros()) {
                if (pr.getMiembro() != null) {
                    int idM = pr.getMiembro().getId();
                    extras.merge(idM, pr.getMontoTotal(), BigDecimal::add);
                    gastos.merge(idM, pr.getMontoGastado(), BigDecimal::add);
                }
            }

            BigDecimal totalActual = BigDecimal.ZERO;
            BigDecimal totalConInteres = BigDecimal.ZERO;
            BigDecimal totalGastado = BigDecimal.ZERO;
            BigDecimal totalDisponible = BigDecimal.ZERO;

            for (Miembro m : miembros) {
                if (!m.isActivo()) {
                    continue;
                }
                BigDecimal ingreso = BigDecimal.valueOf(m.getIngresoMensual());
                BigDecimal porc = porcentajeAporte(ingreso);
                BigDecimal aporte = ingreso.multiply(porc).setScale(2, RoundingMode.HALF_UP);
                BigDecimal actual = aporte.add(extras.getOrDefault(m.getId(), BigDecimal.ZERO));
                BigDecimal conInteres = actual.multiply(BigDecimal.ONE.add(ConfiguracionGlobal.TASA_INTERES))
                        .setScale(2, RoundingMode.HALF_UP);
                BigDecimal gastado = gastos.getOrDefault(m.getId(), BigDecimal.ZERO);
                BigDecimal disponible = actual.multiply(ConfiguracionGlobal.PORCENTAJE_DISPONIBLE)
                        .setScale(2, RoundingMode.HALF_UP).subtract(gastado);

                totalActual = totalActual.add(actual);
                totalConInteres = totalConInteres.add(conInteres);
                totalGastado = totalGastado.add(gastado);
                totalDisponible = totalDisponible.add(disponible);
            }

            lblAporteBaseValor.setText("RD$ " + formatoMoneda.format(totalActual));
            lblConInteresValor.setText("RD$ " + formatoMoneda.format(totalConInteres));
            lblDisponibleValor.setText("RD$ " + formatoMoneda.format(totalDisponible));
            lblGastadoValor.setText("RD$ " + formatoMoneda.format(totalGastado));

            // 5. Ganancia Estimada = Intereses de préstamos aprobados + Interés (8%) del presupuesto
            BigDecimal interesPresupuesto = totalConInteres.subtract(totalActual);
            BigDecimal gananciaTotal = interesesPrestamos.add(interesPresupuesto);
            lblGananciaValor.setText("RD$ " + formatoMoneda.format(gananciaTotal));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar el resumen general: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal porcentajeAporte(BigDecimal ingreso) {
        if (ingreso.compareTo(ConfiguracionGlobal.SALARIO_LIMITE_BAJO) < 0) {
            return ConfiguracionGlobal.APORTE_SALARIO_BAJO;
        }
        if (ingreso.compareTo(ConfiguracionGlobal.SALARIO_LIMITE_ALTO) <= 0) {
            return ConfiguracionGlobal.APORTE_SALARIO_MEDIO;
        }
        return ConfiguracionGlobal.APORTE_SALARIO_ALTO;
    }
}