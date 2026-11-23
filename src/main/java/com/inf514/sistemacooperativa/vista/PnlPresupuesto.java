package com.inf514.sistemacooperativa.vista;

import com.inf514.sistemacooperativa.dao.PresupuestoDAO;
import com.inf514.sistemacooperativa.modelo.Miembro;
import com.inf514.sistemacooperativa.modelo.Presupuesto;
import com.inf514.sistemacooperativa.modelo.Usuario;
import com.inf514.sistemacooperativa.utilidades.ConfiguracionGlobal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel de presupuesto de los miembros de la cooperativa.
 * Se construye por código (no usa .form de NetBeans).
 *
 * Reglas:
 *  - Aporte inicial de cada miembro = % de su ingreso mensual
 *    (7% si < 30,000; 9% de 30,000 a 70,000; 12% si > 70,000).
 *  - Presupuesto actual (C) = aporte + presupuestos adicionales registrados en las barras de arriba.
 *  - Presupuesto con interés = C * (1 + i)^t, con i = 8% anual y t = 1 año.
 *  - "Presupuestado" suma el presupuesto con interés de todos los miembros activos.
 *  - "Disponible" suma solo el 40% de C (sin interés) menos lo gastado.
 *
 * autor@EAdon
 */
public class PnlPresupuesto extends JPanel {

    private static final Color COLOR_OSCURO = new Color(24, 75, 95);
    private static final Color COLOR_CLARO = new Color(221, 238, 249);
    private static final String FORMATO_FECHA = "dd/MM/yyyy";
    private static final int ANIOS = 1;

    // Índices de columnas de la tabla de miembros
    private static final int COL_NOMBRE = 2;

    private final Usuario usuario;
    private final PresupuestoDAO dao = new PresupuestoDAO();
    private final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtMonto;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JLabel lblTotalPresupuestado;
    private JLabel lblTotalGastado;
    private JLabel lblTotalDisponible;
    private JTable tablaMiembros;
    private DefaultTableModel modeloMiembros;
    private JLabel lblTotalMiembros;

    /** Cálculo de presupuesto de un miembro. */
    private static class ResumenMiembro {
        Miembro miembro;
        BigDecimal ingreso;
        BigDecimal porcentaje;
        BigDecimal aporte;
        BigDecimal actual;
        BigDecimal conInteres;
        BigDecimal gastado;
        BigDecimal disponible;
    }

    public PnlPresupuesto(Usuario usuario) {
        this.usuario = usuario;
        construirInterfaz();
        cargarMiembros();
    }

    // ------------------------------------------------------------------
    // Construcción de la interfaz
    // ------------------------------------------------------------------
    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelMiembros(), BorderLayout.CENTER);
        add(crearPanelResumen(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_CLARO);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Presupuestos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2; c.weightx = 0;
        panel.add(titulo, c);

        JLabel lblModo = new JLabel("Agregar presupuesto a un miembro registrado");
        lblModo.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        c.gridx = 2; c.gridy = 0; c.gridwidth = 2;
        panel.add(lblModo, c);
        c.gridwidth = 1;

        txtNombre = new JTextField(18);
        txtDescripcion = new JTextField(18);
        txtMonto = new JTextField(12);
        txtFechaInicio = new JTextField(10);
        txtFechaFin = new JTextField(10);

        agregarCampo(panel, c, 1, 0, "Nombre del miembro:", txtNombre);
        agregarCampo(panel, c, 1, 2, "Descripción:", txtDescripcion);
        agregarCampo(panel, c, 2, 0, "Monto (RD$):", txtMonto);
        agregarCampo(panel, c, 2, 2, "Fecha inicio (dd/MM/yyyy):", txtFechaInicio);
        agregarCampo(panel, c, 3, 2, "Fecha fin (dd/MM/yyyy):", txtFechaFin);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        JButton btnGuardar = crearBoton("Guardar");
        JButton btnNuevo = crearBoton("Nuevo / Limpiar");
        JButton btnGasto = crearBoton("Registrar gasto");
        JButton btnEliminar = crearBoton("Eliminar");
        botones.add(btnGuardar);
        botones.add(btnNuevo);
        botones.add(btnGasto);
        botones.add(btnEliminar);

        c.gridx = 0; c.gridy = 4; c.gridwidth = 4;
        c.insets = new Insets(10, 6, 0, 6);
        panel.add(botones, c);

        btnGuardar.addActionListener(e -> guardar());
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGasto.addActionListener(e -> registrarGasto());
        btnEliminar.addActionListener(e -> eliminar());

        return panel;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints c, int fila, int col,
                              String etiqueta, JTextField campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        c.gridx = col; c.gridy = fila; c.weightx = 0;
        panel.add(lbl, c);
        c.gridx = col + 1; c.weightx = 1;
        panel.add(campo, c);
    }

    private JButton crearBoton(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(COLOR_OSCURO);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return b;
    }

    private JPanel crearPanelMiembros() {
        String[] columnas = {"ID", "Cédula", "Nombre Completo", "Estado", "Ingreso mensual",
                "% aporte", "Presupuesto actual", "Presupuesto con interés (8%)", "Disponible (40%)"};
        modeloMiembros = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaMiembros = new JTable(modeloMiembros);
        tablaMiembros.setRowHeight(24);
        tablaMiembros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMiembros.getTableHeader().setReorderingAllowed(false);
        tablaMiembros.getTableHeader().setBackground(COLOR_OSCURO);
        tablaMiembros.getTableHeader().setForeground(Color.WHITE);

        // Al seleccionar un miembro, su nombre pasa a la barra de arriba
        tablaMiembros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaMiembros.getSelectedRow();
                if (fila != -1) {
                    Object nombre = modeloMiembros.getValueAt(fila, COL_NOMBRE);
                    txtNombre.setText(nombre == null ? "" : nombre.toString());
                }
            }
        });

        // Columnas numéricas (ingreso, %, presupuestos, disponible) alineadas a la derecha
        DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
        derecha.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int col = 4; col <= 8; col++) {
            tablaMiembros.getColumnModel().getColumn(col).setCellRenderer(derecha);
        }

        JLabel nota = new JLabel("<html>&nbsp;Aporte: 7% del ingreso mensual si es menor de 30,000; 9% de 30,000 a 70,000; "
                + "12% si supera 70,000.<br>&nbsp;Interés: I = C · i · t  (i = 8% anual, t = 1 año, capitalización anual)"
                + "  ·  Disponible = 40% del presupuesto actual (sin interés) − gastos</html>");
        nota.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        lblTotalMiembros = new JLabel();
        lblTotalMiembros.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 5));
        pie.setBackground(COLOR_CLARO);
        pie.add(lblTotalMiembros);

        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);
        panel.add(nota, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaMiembros), BorderLayout.CENTER);
        panel.add(pie, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 5));
        panel.setBackground(COLOR_CLARO);
        panel.setPreferredSize(new Dimension(100, 40));
        lblTotalPresupuestado = new JLabel();
        lblTotalGastado = new JLabel();
        lblTotalDisponible = new JLabel();
        for (JLabel l : new JLabel[]{lblTotalPresupuestado, lblTotalGastado, lblTotalDisponible}) {
            l.setFont(new Font("Segoe UI", Font.BOLD, 13));
            panel.add(l);
        }
        return panel;
    }

    // ------------------------------------------------------------------
    // Cálculos
    // ------------------------------------------------------------------

    /** Porcentaje de aporte según el ingreso mensual. */
    private BigDecimal porcentajeAporte(BigDecimal ingreso) {
        if (ingreso.compareTo(ConfiguracionGlobal.SALARIO_LIMITE_BAJO) < 0) {
            return ConfiguracionGlobal.APORTE_SALARIO_BAJO;      // < 30,000
        }
        if (ingreso.compareTo(ConfiguracionGlobal.SALARIO_LIMITE_ALTO) <= 0) {
            return ConfiguracionGlobal.APORTE_SALARIO_MEDIO;     // 30,000 a 70,000
        }
        return ConfiguracionGlobal.APORTE_SALARIO_ALTO;          // > 70,000
    }

    /**
     * Capital con interés: C + I, donde I = C * i * t.
     * Con capitalización anual el monto es C * (1 + i)^t (para t = 1 año equivale a C + C*i).
     */
    private BigDecimal calcularConInteres(BigDecimal capital) {
        BigDecimal factor = BigDecimal.ONE.add(ConfiguracionGlobal.TASA_INTERES).pow(ANIOS);
        return capital.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    private ResumenMiembro resumenDe(Miembro m, BigDecimal extras, BigDecimal gastado) {
        ResumenMiembro r = new ResumenMiembro();
        r.miembro = m;
        r.ingreso = BigDecimal.valueOf(m.getIngresoMensual()); // en Miembro es double
        r.porcentaje = porcentajeAporte(r.ingreso);
        r.aporte = r.ingreso.multiply(r.porcentaje).setScale(2, RoundingMode.HALF_UP);
        r.actual = r.aporte.add(extras);
        r.conInteres = calcularConInteres(r.actual);
        r.gastado = gastado;
        r.disponible = r.actual.multiply(ConfiguracionGlobal.PORCENTAJE_DISPONIBLE)
                .setScale(2, RoundingMode.HALF_UP).subtract(gastado);
        return r;
    }

    /** Calcula el presupuesto de todos los miembros registrados. */
    private List<ResumenMiembro> calcularResumenes() {
        Map<Integer, BigDecimal> extras = new HashMap<>();
        Map<Integer, BigDecimal> gastos = new HashMap<>();
        for (Presupuesto p : dao.listarPresupuestosDeMiembros()) {
            int id = p.getMiembro().getId();
            extras.merge(id, p.getMontoTotal(), BigDecimal::add);
            gastos.merge(id, p.getMontoGastado(), BigDecimal::add);
        }
        List<ResumenMiembro> lista = new ArrayList<>();
        for (Miembro m : dao.listarMiembros()) {
            lista.add(resumenDe(m,
                    extras.getOrDefault(m.getId(), BigDecimal.ZERO),
                    gastos.getOrDefault(m.getId(), BigDecimal.ZERO)));
        }
        return lista;
    }

    // ------------------------------------------------------------------
    // Datos
    // ------------------------------------------------------------------
    private void cargarMiembros() {
        modeloMiembros.setRowCount(0);
        BigDecimal totalActual = BigDecimal.ZERO;
        BigDecimal totalConInteres = BigDecimal.ZERO;
        BigDecimal totalGastado = BigDecimal.ZERO;
        BigDecimal totalDisponible = BigDecimal.ZERO;
        try {
            for (ResumenMiembro r : calcularResumenes()) {
                Miembro m = r.miembro;
                modeloMiembros.addRow(new Object[]{
                        m.getId(),
                        m.getCedula(),
                        m.getNombreCompleto(),
                        m.isActivo() ? "Activo" : "Inactivo",
                        "RD$ " + formatoMoneda.format(r.ingreso),
                        r.porcentaje.movePointRight(2).intValue() + "%",
                        "RD$ " + formatoMoneda.format(r.actual),
                        "RD$ " + formatoMoneda.format(r.conInteres),
                        "RD$ " + formatoMoneda.format(r.disponible)
                });
                // Solo los miembros activos suman a los totales
                if (m.isActivo()) {
                    totalActual = totalActual.add(r.actual);
                    totalConInteres = totalConInteres.add(r.conInteres);
                    totalGastado = totalGastado.add(r.gastado);
                    totalDisponible = totalDisponible.add(r.disponible);
                }
            }
        } catch (Exception ex) {
            error("No se pudieron cargar los miembros: " + ex.getMessage());
        }

        lblTotalMiembros.setText("Total actual: RD$ " + formatoMoneda.format(totalActual)
                + "   |   Total con interés: RD$ " + formatoMoneda.format(totalConInteres));
        lblTotalPresupuestado.setText("Presupuestado: RD$ " + formatoMoneda.format(totalConInteres));
        lblTotalGastado.setText("Gastado: RD$ " + formatoMoneda.format(totalGastado));
        lblTotalDisponible.setText("Disponible: RD$ " + formatoMoneda.format(totalDisponible));
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtMonto.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        tablaMiembros.clearSelection();
        txtNombre.requestFocus();
    }

    /**
     * Busca en los miembros registrados a la persona escrita en la barra "Nombre del miembro".
     * Si no existe (o está inactiva) muestra un aviso y devuelve null.
     */
    private Miembro obtenerMiembroDelFormulario() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            aviso("Escriba el nombre del miembro.");
            return null;
        }
        try {
            Miembro m = dao.buscarMiembroPorNombre(nombre);
            if (m == null) {
                aviso("La persona \"" + nombre + "\" no se encuentra entre los miembros registrados.");
                return null;
            }
            if (!m.isActivo()) {
                aviso("El miembro \"" + m.getNombreCompleto() + "\" está inactivo.");
                return null;
            }
            return m;
        } catch (Exception ex) {
            error("No se pudo consultar los miembros: " + ex.getMessage());
            return null;
        }
    }

    // ------------------------------------------------------------------
    // Acciones
    // ------------------------------------------------------------------
    private void guardar() {
        Miembro miembro = obtenerMiembroDelFormulario();
        if (miembro == null) {
            return;
        }

        String descripcion = txtDescripcion.getText().trim();
        String montoTxt = txtMonto.getText().trim().replace(",", "");

        BigDecimal monto;
        try {
            monto = new BigDecimal(montoTxt);
        } catch (NumberFormatException ex) {
            aviso("Ingrese un monto válido (ej. 25000.50).");
            return;
        }
        if (monto.signum() <= 0) {
            aviso("El monto debe ser mayor que cero.");
            return;
        }

        Date inicio;
        Date fin;
        try {
            inicio = parsearFecha(txtFechaInicio.getText());
            fin = parsearFecha(txtFechaFin.getText());
        } catch (ParseException ex) {
            aviso("Las fechas deben tener formato dd/MM/yyyy (ej. 15/11/2026) o dejarse vacías.");
            return;
        }
        if (inicio != null && fin != null && fin.before(inicio)) {
            aviso("La fecha fin no puede ser anterior a la fecha de inicio.");
            return;
        }

        try {
            Presupuesto nuevo = new Presupuesto(usuario, miembro,
                    limitar("Presupuesto - " + miembro.getNombreCompleto(), 100),
                    descripcion.isEmpty() ? null : descripcion, monto, inicio, fin);
            dao.guardarPresupuesto(nuevo);
            info("Presupuesto agregado a " + miembro.getNombreCompleto() + ".");
            limpiarFormulario();
            cargarMiembros();
        } catch (Exception ex) {
            error("No se pudo guardar el presupuesto: " + ex.getMessage());
        }
    }

    private void registrarGasto() {
        Miembro miembro = obtenerMiembroDelFormulario();
        if (miembro == null) {
            return;
        }

        try {
            ResumenMiembro resumen = null;
            for (ResumenMiembro r : calcularResumenes()) {
                if (r.miembro.getId() == miembro.getId()) {
                    resumen = r;
                    break;
                }
            }
            if (resumen == null) {
                aviso("La persona \"" + miembro.getNombreCompleto() + "\" no se encuentra entre los miembros registrados.");
                return;
            }

            String entrada = JOptionPane.showInputDialog(this,
                    "Disponible de " + miembro.getNombreCompleto() + ": RD$ "
                            + formatoMoneda.format(resumen.disponible)
                            + "\nMonto del gasto a registrar:",
                    "Registrar gasto", JOptionPane.QUESTION_MESSAGE);
            if (entrada == null) {
                return; // canceló
            }

            BigDecimal gasto = new BigDecimal(entrada.trim().replace(",", ""));
            if (gasto.signum() <= 0) {
                aviso("El monto debe ser mayor que cero.");
                return;
            }
            if (gasto.compareTo(resumen.disponible) > 0) {
                aviso("El gasto excede el presupuesto disponible.");
                return;
            }

            String descripcion = txtDescripcion.getText().trim();
            Presupuesto registro = new Presupuesto(usuario, miembro,
                    limitar("Gasto - " + miembro.getNombreCompleto(), 100),
                    descripcion.isEmpty() ? null : descripcion, BigDecimal.ZERO, null, null);
            registro.setMontoGastado(gasto);
            dao.guardarPresupuesto(registro);
            info("Gasto registrado correctamente.");
            limpiarFormulario();
            cargarMiembros();
        } catch (NumberFormatException ex) {
            aviso("Ingrese un monto válido.");
        } catch (Exception ex) {
            error("No se pudo registrar el gasto: " + ex.getMessage());
        }
    }

    private void eliminar() {
        Miembro miembro = obtenerMiembroDelFormulario();
        if (miembro == null) {
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar los presupuestos agregados y los gastos registrados de:\n"
                        + miembro.getNombreCompleto() + "?\n"
                        + "(Su aporte base según el ingreso mensual no se elimina.)",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.eliminarPresupuestosDeMiembro(miembro.getId());
            limpiarFormulario();
            cargarMiembros();
        } catch (Exception ex) {
            error("No se pudo eliminar: " + ex.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Utilidades
    // ------------------------------------------------------------------
    private String limitar(String texto, int max) {
        return texto.length() <= max ? texto : texto.substring(0, max);
    }

    private Date parsearFecha(String texto) throws ParseException {
        texto = texto.trim();
        if (texto.isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
        sdf.setLenient(false);
        return sdf.parse(texto);
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void info(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}