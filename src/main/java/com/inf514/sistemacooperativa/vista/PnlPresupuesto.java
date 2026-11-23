package com.inf514.sistemacooperativa.vista;

import com.inf514.sistemacooperativa.dao.MiembroDAO;
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
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel de gestión de presupuestos del usuario que inició sesión.
 * Se construye por código (no usa .form de NetBeans).
 * autor@EAdon
 */
public class PnlPresupuesto extends JPanel {

    private static final Color COLOR_OSCURO = new Color(24, 75, 95);
    private static final Color COLOR_CLARO = new Color(221, 238, 249);
    private static final String FORMATO_FECHA = "dd/MM/yyyy";

    // Interés: I = C * i * t, con i = 8% anual, t = 1 año, capitalización anual
    private static final BigDecimal TASA_INTERES = new BigDecimal("0.08");
    private static final int ANIOS = 1;

    private final Usuario usuario;
    private final PresupuestoDAO dao = new PresupuestoDAO();
    private final MiembroDAO miembroDAO = new MiembroDAO();
    private final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

    // id del presupuesto en edición (0 = nuevo)
    private int idEnEdicion = 0;

    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtMonto;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalPresupuestado;
    private JLabel lblTotalGastado;
    private JLabel lblTotalDisponible;
    private JLabel lblModo;
    private JTable tablaMiembros;
    private DefaultTableModel modeloMiembros;
    private JLabel lblTotalMiembros;

    public PnlPresupuesto(Usuario usuario) {
        this.usuario = usuario;
        construirInterfaz();
        cargarTabla();
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
        JTabbedPane pestanias = new JTabbedPane();
        pestanias.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pestanias.addTab("Presupuestos", crearPanelTabla());
        pestanias.addTab("Miembros y proyección", crearPanelMiembros());
        add(pestanias, BorderLayout.CENTER);
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

        lblModo = new JLabel("Nuevo presupuesto");
        lblModo.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        c.gridx = 2; c.gridy = 0; c.gridwidth = 2;
        panel.add(lblModo, c);
        c.gridwidth = 1;

        txtNombre = new JTextField(18);
        txtDescripcion = new JTextField(18);
        txtMonto = new JTextField(12);
        txtFechaInicio = new JTextField(10);
        txtFechaFin = new JTextField(10);

        agregarCampo(panel, c, 1, 0, "Nombre:", txtNombre);
        agregarCampo(panel, c, 1, 2, "Descripción:", txtDescripcion);
        agregarCampo(panel, c, 2, 0, "Monto total (RD$):", txtMonto);
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

    private JScrollPane crearPanelTabla() {
        String[] columnas = {"ID", "Nombre", "Descripción", "Total", "Gastado", "Disponible", "Inicio", "Fin"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(24);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setBackground(COLOR_OSCURO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccionEnFormulario();
            }
        });
        return new JScrollPane(tabla);
    }

    private JPanel crearPanelMiembros() {
        String[] columnas = {"ID", "Cédula", "Nombre Completo", "Ocupación", "Estado",
                "Presupuesto actual", "Presupuesto con interés (8%)"};
        modeloMiembros = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaMiembros = new JTable(modeloMiembros);
        tablaMiembros.setRowHeight(24);
        tablaMiembros.getTableHeader().setReorderingAllowed(false);
        tablaMiembros.getTableHeader().setBackground(COLOR_OSCURO);
        tablaMiembros.getTableHeader().setForeground(Color.WHITE);

        // Las dos columnas de dinero (a la derecha de la tabla) alineadas a la derecha
        DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
        derecha.setHorizontalAlignment(SwingConstants.RIGHT);
        tablaMiembros.getColumnModel().getColumn(5).setCellRenderer(derecha);
        tablaMiembros.getColumnModel().getColumn(6).setCellRenderer(derecha);

        JLabel nota = new JLabel("  Interés: I = C · i · t  (i = 8% anual, t = 1 año, capitalización anual)");
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
    // Datos
    // ------------------------------------------------------------------
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal gastado = BigDecimal.ZERO;

        List<Presupuesto> lista = dao.listarPorUsuario(usuario.getId());
        for (Presupuesto p : lista) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getDescripcion(),
                    formatoMoneda.format(p.getMontoTotal()),
                    formatoMoneda.format(p.getMontoGastado()),
                    formatoMoneda.format(p.getMontoDisponible()),
                    p.getFechaInicio() != null ? sdf.format(p.getFechaInicio()) : "",
                    p.getFechaFin() != null ? sdf.format(p.getFechaFin()) : ""
            });
            total = total.add(p.getMontoTotal());
            gastado = gastado.add(p.getMontoGastado());
        }

        lblTotalPresupuestado.setText("Presupuestado: RD$ " + formatoMoneda.format(total));
        lblTotalGastado.setText("Gastado: RD$ " + formatoMoneda.format(gastado));
        lblTotalDisponible.setText("Disponible: RD$ " + formatoMoneda.format(total.subtract(gastado)));
    }

    private void cargarMiembros() {
        modeloMiembros.setRowCount(0);
        BigDecimal totalActual = BigDecimal.ZERO;
        BigDecimal totalConInteres = BigDecimal.ZERO;
        try {
            List<Miembro> miembros = miembroDAO.obtenerMiembros();
            for (Miembro m : miembros) {
                BigDecimal actual = presupuestoActual(m);
                BigDecimal conInteres = calcularConInteres(actual);
                modeloMiembros.addRow(new Object[]{
                        m.getId(),
                        m.getCedula(),
                        m.getNombreCompleto(),
                        m.getOcupacion(),
                        m.isActivo() ? "Activo" : "Inactivo",
                        "RD$ " + formatoMoneda.format(actual),
                        "RD$ " + formatoMoneda.format(conInteres)
                });
                totalActual = totalActual.add(actual);
                totalConInteres = totalConInteres.add(conInteres);
            }
        } catch (Exception ex) {
            error("No se pudieron cargar los miembros: " + ex.getMessage());
        }
        lblTotalMiembros.setText("Total actual: RD$ " + formatoMoneda.format(totalActual)
                + "   |   Total con interés: RD$ " + formatoMoneda.format(totalConInteres));
    }

    /**
     * Presupuesto actual (C) de un miembro: cada miembro ingresa con un monto fijo.
     * Cuando exista la sección de aportes, aquí se sumarán los aportes del miembro.
     */
    private BigDecimal presupuestoActual(Miembro m) {
        return ConfiguracionGlobal.PRESUPUESTO_INICIAL_MIEMBRO;
    }

    /**
     * Capital con interés: C + I, donde I = C * i * t.
     * Con capitalización anual el monto es C * (1 + i)^t (para t = 1 año equivale a C + C*i).
     */
    private BigDecimal calcularConInteres(BigDecimal capital) {
        BigDecimal factor = BigDecimal.ONE.add(TASA_INTERES).pow(ANIOS);
        return capital.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Presupuesto p = dao.buscarPorId(id);
        if (p == null) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
        idEnEdicion = p.getId();
        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion() == null ? "" : p.getDescripcion());
        txtMonto.setText(p.getMontoTotal().toPlainString());
        txtFechaInicio.setText(p.getFechaInicio() != null ? sdf.format(p.getFechaInicio()) : "");
        txtFechaFin.setText(p.getFechaFin() != null ? sdf.format(p.getFechaFin()) : "");
        lblModo.setText("Editando: " + p.getNombre());
    }

    private void limpiarFormulario() {
        idEnEdicion = 0;
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtMonto.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        lblModo.setText("Nuevo presupuesto");
        tabla.clearSelection();
        txtNombre.requestFocus();
    }

    // ------------------------------------------------------------------
    // Acciones
    // ------------------------------------------------------------------
    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String montoTxt = txtMonto.getText().trim().replace(",", "");

        if (nombre.isEmpty()) {
            aviso("El nombre del presupuesto es obligatorio.");
            return;
        }

        BigDecimal monto;
        try {
            monto = new BigDecimal(montoTxt);
        } catch (NumberFormatException ex) {
            aviso("Ingrese un monto total válido (ej. 25000.50).");
            return;
        }
        if (monto.signum() <= 0) {
            aviso("El monto total debe ser mayor que cero.");
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
            if (idEnEdicion == 0) {
                Presupuesto nuevo = new Presupuesto(usuario, nombre,
                        descripcion.isEmpty() ? null : descripcion, monto, inicio, fin);
                dao.guardarPresupuesto(nuevo);
                info("Presupuesto creado correctamente.");
            } else {
                Presupuesto existente = dao.buscarPorId(idEnEdicion);
                if (existente == null) {
                    aviso("El presupuesto ya no existe.");
                    limpiarFormulario();
                    cargarTabla();
                    return;
                }
                if (monto.compareTo(existente.getMontoGastado()) < 0) {
                    aviso("El monto total no puede ser menor a lo ya gastado (RD$ "
                            + formatoMoneda.format(existente.getMontoGastado()) + ").");
                    return;
                }
                existente.setNombre(nombre);
                existente.setDescripcion(descripcion.isEmpty() ? null : descripcion);
                existente.setMontoTotal(monto);
                existente.setFechaInicio(inicio);
                existente.setFechaFin(fin);
                dao.guardarPresupuesto(existente);
                info("Presupuesto actualizado correctamente.");
            }
            limpiarFormulario();
            cargarTabla();
        } catch (Exception ex) {
            error("No se pudo guardar el presupuesto: " + ex.getMessage());
        }
    }

    private void registrarGasto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            aviso("Seleccione un presupuesto de la tabla primero.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Presupuesto p = dao.buscarPorId(id);
        if (p == null) {
            aviso("El presupuesto ya no existe.");
            cargarTabla();
            return;
        }

        String entrada = JOptionPane.showInputDialog(this,
                "Disponible: RD$ " + formatoMoneda.format(p.getMontoDisponible())
                        + "\nMonto del gasto a registrar:",
                "Registrar gasto - " + p.getNombre(), JOptionPane.QUESTION_MESSAGE);
        if (entrada == null) {
            return; // canceló
        }

        try {
            BigDecimal gasto = new BigDecimal(entrada.trim().replace(",", ""));
            p.registrarGasto(gasto); // valida > 0 y que no exceda lo disponible
            dao.guardarPresupuesto(p);
            info("Gasto registrado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (NumberFormatException ex) {
            aviso("Ingrese un monto válido.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            aviso(ex.getMessage());
        } catch (Exception ex) {
            error("No se pudo registrar el gasto: " + ex.getMessage());
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            aviso("Seleccione un presupuesto de la tabla primero.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = String.valueOf(modeloTabla.getValueAt(fila, 1));

        int r = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el presupuesto:\n" + nombre + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.eliminarPresupuesto(id);
            limpiarFormulario();
            cargarTabla();
        } catch (Exception ex) {
            error("No se pudo eliminar el presupuesto: " + ex.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Utilidades
    // ------------------------------------------------------------------
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