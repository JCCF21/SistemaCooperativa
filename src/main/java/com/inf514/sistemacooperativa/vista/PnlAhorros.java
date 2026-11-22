package com.inf514.sistemacooperativa.vista;

import com.inf514.sistemacooperativa.dao.CuentaAhorroDAO;
import com.inf514.sistemacooperativa.dao.MiembroDAO;
import com.inf514.sistemacooperativa.modelo.CuentaAhorro;
import com.inf514.sistemacooperativa.modelo.Miembro;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class PnlAhorros extends javax.swing.JPanel {

    private final CuentaAhorroDAO cuentaDAO = new CuentaAhorroDAO();
    private final MiembroDAO miembroDAO = new MiembroDAO();
    private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.US);

    private DefaultTableModel modeloTabla;
    private List<CuentaAhorro> cuentasActuales = new ArrayList<>();

    public PnlAhorros() {
        initComponents();
        configurarLogicaAdicional();
        cargarDatos();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        pnlSuperior = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        pnlTarjetas = new javax.swing.JPanel();
        pnlTarjetaTotal = new javax.swing.JPanel();
        lblTitTotal = new javax.swing.JLabel();
        lblTotalAhorrado = new javax.swing.JLabel();
        pnlTarjetaCuentas = new javax.swing.JPanel();
        lblTitCuentas = new javax.swing.JLabel();
        lblTotalCuentas = new javax.swing.JLabel();
        pnlTarjetaSocio = new javax.swing.JPanel();
        lblTitSocio = new javax.swing.JLabel();
        lblSaldoSocioSeleccionado = new javax.swing.JLabel();
        pnlBusqueda = new javax.swing.JPanel();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        tablaAhorros = new javax.swing.JTable();
        pnlBotones = new javax.swing.JPanel();
        btnNuevaCuenta = new javax.swing.JButton();
        btnDepositar = new javax.swing.JButton();
        btnRetirar = new javax.swing.JButton();
        btnEstado = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();

        setBackground(new java.awt.Color(244, 239, 230));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new java.awt.BorderLayout(12, 12));

        pnlSuperior.setOpaque(false);
        pnlSuperior.setLayout(new java.awt.BorderLayout(10, 10));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(24, 75, 95));
        lblTitulo.setText("Gestión de Ahorros de Socios");
        pnlSuperior.add(lblTitulo, java.awt.BorderLayout.NORTH);

        pnlTarjetas.setOpaque(false);
        pnlTarjetas.setLayout(new java.awt.GridLayout(1, 3, 12, 0));

        pnlTarjetaTotal.setBackground(new java.awt.Color(255, 255, 255));
        pnlTarjetaTotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(24, 75, 95)), javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        pnlTarjetaTotal.setLayout(new java.awt.BorderLayout(4, 4));

        lblTitTotal.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblTitTotal.setForeground(java.awt.Color.DARK_GRAY);
        lblTitTotal.setText("Total Ahorrado (Activas)");
        pnlTarjetaTotal.add(lblTitTotal, java.awt.BorderLayout.NORTH);

        lblTotalAhorrado.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalAhorrado.setForeground(new java.awt.Color(24, 75, 95));
        lblTotalAhorrado.setText("$0.00");
        pnlTarjetaTotal.add(lblTotalAhorrado, java.awt.BorderLayout.CENTER);

        pnlTarjetas.add(pnlTarjetaTotal);

        pnlTarjetaCuentas.setBackground(new java.awt.Color(255, 255, 255));
        pnlTarjetaCuentas.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(0, 102, 102)), javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        pnlTarjetaCuentas.setLayout(new java.awt.BorderLayout(4, 4));

        lblTitCuentas.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblTitCuentas.setForeground(java.awt.Color.DARK_GRAY);
        lblTitCuentas.setText("Cuentas Registradas");
        pnlTarjetaCuentas.add(lblTitCuentas, java.awt.BorderLayout.NORTH);

        lblTotalCuentas.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalCuentas.setForeground(new java.awt.Color(0, 102, 102));
        lblTotalCuentas.setText("0");
        pnlTarjetaCuentas.add(lblTotalCuentas, java.awt.BorderLayout.CENTER);

        pnlTarjetas.add(pnlTarjetaCuentas);

        pnlTarjetaSocio.setBackground(new java.awt.Color(255, 255, 255));
        pnlTarjetaSocio.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(46, 125, 50)), javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        pnlTarjetaSocio.setLayout(new java.awt.BorderLayout(4, 4));

        lblTitSocio.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblTitSocio.setForeground(java.awt.Color.DARK_GRAY);
        lblTitSocio.setText("Total del Socio Seleccionado");
        pnlTarjetaSocio.add(lblTitSocio, java.awt.BorderLayout.NORTH);

        lblSaldoSocioSeleccionado.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblSaldoSocioSeleccionado.setForeground(new java.awt.Color(46, 125, 50));
        lblSaldoSocioSeleccionado.setText("$0.00");
        pnlTarjetaSocio.add(lblSaldoSocioSeleccionado, java.awt.BorderLayout.CENTER);

        pnlTarjetas.add(pnlTarjetaSocio);

        pnlSuperior.add(pnlTarjetas, java.awt.BorderLayout.CENTER);

        pnlBusqueda.setOpaque(false);
        pnlBusqueda.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));

        lblBuscar.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblBuscar.setText("Buscar socio / cédula / tipo:");
        pnlBusqueda.add(lblBuscar);

        txtBuscar.setColumns(26);
        txtBuscar.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlBusqueda.add(txtBuscar);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        pnlBusqueda.add(btnLimpiar);

        pnlSuperior.add(pnlBusqueda, java.awt.BorderLayout.SOUTH);

        add(pnlSuperior, java.awt.BorderLayout.NORTH);

        scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 195, 185)));

        tablaAhorros.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tablaAhorros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Cédula", "Socio", "Tipo de Cuenta", "Saldo Actual", "Fecha Apertura", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaAhorros.setRowHeight(26);
        tablaAhorros.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(tablaAhorros);

        add(scrollPane, java.awt.BorderLayout.CENTER);

        pnlBotones.setOpaque(false);
        pnlBotones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 6));

        btnNuevaCuenta.setBackground(new java.awt.Color(24, 75, 95));
        btnNuevaCuenta.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnNuevaCuenta.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevaCuenta.setText("Nueva Cuenta");
        btnNuevaCuenta.setFocusPainted(false);
        btnNuevaCuenta.setPreferredSize(new java.awt.Dimension(165, 36));
        btnNuevaCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaCuentaActionPerformed(evt);
            }
        });
        pnlBotones.add(btnNuevaCuenta);

        btnDepositar.setBackground(new java.awt.Color(46, 125, 50));
        btnDepositar.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnDepositar.setForeground(new java.awt.Color(255, 255, 255));
        btnDepositar.setText("Depositar");
        btnDepositar.setFocusPainted(false);
        btnDepositar.setPreferredSize(new java.awt.Dimension(165, 36));
        btnDepositar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnDepositar);

        btnRetirar.setBackground(new java.awt.Color(198, 40, 40));
        btnRetirar.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnRetirar.setForeground(new java.awt.Color(255, 255, 255));
        btnRetirar.setText("Retirar");
        btnRetirar.setFocusPainted(false);
        btnRetirar.setPreferredSize(new java.awt.Dimension(165, 36));
        btnRetirar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetirarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnRetirar);

        btnEstado.setBackground(new java.awt.Color(85, 85, 85));
        btnEstado.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnEstado.setForeground(new java.awt.Color(255, 255, 255));
        btnEstado.setText("Activar / Desactivar");
        btnEstado.setFocusPainted(false);
        btnEstado.setPreferredSize(new java.awt.Dimension(165, 36));
        btnEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstadoActionPerformed(evt);
            }
        });
        pnlBotones.add(btnEstado);

        btnActualizar.setBackground(new java.awt.Color(0, 102, 102));
        btnActualizar.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizar.setText("Actualizar");
        btnActualizar.setFocusPainted(false);
        btnActualizar.setPreferredSize(new java.awt.Dimension(165, 36));
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnActualizar);

        add(pnlBotones, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>                        

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        txtBuscar.setText("");
        cargarDatos();
    }                                          

    private void btnNuevaCuentaActionPerformed(java.awt.event.ActionEvent evt) {                                               
        abrirDialogoNuevaCuenta();
    }                                              

    private void btnDepositarActionPerformed(java.awt.event.ActionEvent evt) {                                             
        procesarMovimiento(true);
    }                                            

    private void btnRetirarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        procesarMovimiento(false);
    }                                          

    private void btnEstadoActionPerformed(java.awt.event.ActionEvent evt) {                                          
        alternarEstadoCuenta();
    }                                         

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {                                              
        cargarDatos();
    }                                             

    private void configurarLogicaAdicional() {
        modeloTabla = (DefaultTableModel) tablaAhorros.getModel();

        tablaAhorros.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaAhorros.getTableHeader().setBackground(new Color(24, 75, 95));
        tablaAhorros.getTableHeader().setForeground(Color.WHITE);

        tablaAhorros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarResumenSocioSeleccionado();
            }
        });

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarTabla();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarTabla();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarTabla();
            }
        });
    }

    public void cargarDatos() {
        try {
            cuentasActuales = cuentaDAO.obtenerCuentas();
            filtrarTabla();
            lblTotalAhorrado.setText(formatoMoneda.format(cuentaDAO.obtenerSaldoTotal()));
            lblTotalCuentas.setText(String.valueOf(cuentasActuales.size()));
            actualizarResumenSocioSeleccionado();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar las cuentas de ahorro:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void filtrarTabla() {
        String filtro = txtBuscar.getText().trim().toLowerCase();
        modeloTabla.setRowCount(0);

        for (CuentaAhorro c : cuentasActuales) {
            Miembro m = c.getMiembro();
            String nombre = m != null && m.getNombreCompleto() != null ? m.getNombreCompleto() : "";
            String cedula = m != null && m.getCedula() != null ? m.getCedula() : "";
            String tipo = c.getTipoCuenta() != null ? c.getTipoCuenta() : "";

            boolean coincide = filtro.isEmpty()
                    || nombre.toLowerCase().contains(filtro)
                    || cedula.toLowerCase().contains(filtro)
                    || tipo.toLowerCase().contains(filtro);

            if (coincide) {
                modeloTabla.addRow(new Object[]{
                    c.getId(),
                    cedula,
                    nombre,
                    tipo,
                    formatoMoneda.format(c.getSaldo()),
                    c.getFechaApertura(),
                    c.isActivo() ? "Activa" : "Inactiva"
                });
            }
        }
    }

    private void actualizarResumenSocioSeleccionado() {
        int fila = tablaAhorros.getSelectedRow();
        if (fila < 0) {
            lblSaldoSocioSeleccionado.setText("$0.00");
            return;
        }
        int idCuenta = (int) modeloTabla.getValueAt(fila, 0);
        CuentaAhorro seleccionada = buscarCuentaEnLista(idCuenta);
        if (seleccionada == null || seleccionada.getMiembro() == null) {
            lblSaldoSocioSeleccionado.setText("$0.00");
            return;
        }

        int idMiembro = seleccionada.getMiembro().getId();
        double sumaSocio = 0.0;
        for (CuentaAhorro c : cuentasActuales) {
            if (c.getMiembro() != null && c.getMiembro().getId() == idMiembro && c.isActivo()) {
                sumaSocio += c.getSaldo();
            }
        }
        lblSaldoSocioSeleccionado.setText(formatoMoneda.format(sumaSocio));
    }

    private CuentaAhorro getCuentaSeleccionada() {
        int fila = tablaAhorros.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una cuenta de ahorro en la tabla primero.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return null;
        }
        int idCuenta = (int) modeloTabla.getValueAt(fila, 0);
        return buscarCuentaEnLista(idCuenta);
    }

    private CuentaAhorro buscarCuentaEnLista(int idCuenta) {
        for (CuentaAhorro c : cuentasActuales) {
            if (c.getId() == idCuenta) {
                return c;
            }
        }
        return null;
    }

    private void abrirDialogoNuevaCuenta() {
        List<Miembro> miembros = miembroDAO.obtenerMiembros();
        if (miembros.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay socios registrados todavía. Registra un socio primero.",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JComboBox<MiembroItem> cmbMiembro = new JComboBox<>();
        for (Miembro m : miembros) {
            cmbMiembro.addItem(new MiembroItem(m));
        }

        String[] tiposCuenta = {"Ahorro Regular", "Ahorro Navideño", "Ahorro a Plazo", "Aportaciones"};
        JComboBox<String> cmbTipo = new JComboBox<>(tiposCuenta);
        JTextField txtSaldoInicial = new JTextField("0.00");

        JPanel panelForm = new JPanel(new GridLayout(0, 1, 6, 6));
        panelForm.add(new JLabel("Socio:"));
        panelForm.add(cmbMiembro);
        panelForm.add(new JLabel("Tipo de cuenta:"));
        panelForm.add(cmbTipo);
        panelForm.add(new JLabel("Depósito inicial:"));
        panelForm.add(txtSaldoInicial);

        int opcion = JOptionPane.showConfirmDialog(
                this,
                panelForm,
                "Abrir Nueva Cuenta de Ahorro",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                double saldoInicial = Double.parseDouble(txtSaldoInicial.getText().trim());
                if (saldoInicial < 0) {
                    JOptionPane.showMessageDialog(this, "El saldo inicial no puede ser negativo.");
                    return;
                }
                MiembroItem itemSeleccionado = (MiembroItem) cmbMiembro.getSelectedItem();
                String tipoSeleccionado = (String) cmbTipo.getSelectedItem();

                CuentaAhorro nueva = new CuentaAhorro(
                        itemSeleccionado.miembro,
                        tipoSeleccionado,
                        saldoInicial,
                        LocalDate.now(),
                        true
                );
                cuentaDAO.guardar(nueva);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Cuenta de ahorro creada correctamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ingresa un monto numérico válido para el saldo inicial.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al crear la cuenta:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void procesarMovimiento(boolean esDeposito) {
        CuentaAhorro cuenta = getCuentaSeleccionada();
        if (cuenta == null) {
            return;
        }

        String accion = esDeposito ? "Depositar en" : "Retirar de";
        String input = JOptionPane.showInputDialog(
                this,
                accion + " la cuenta (" + cuenta.getTipoCuenta() + ") de " + cuenta.getMiembro().getNombreCompleto()
                + "\nSaldo actual: " + formatoMoneda.format(cuenta.getSaldo())
                + "\n\nIngresa el monto:",
                esDeposito ? "Realizar Depósito" : "Realizar Retiro",
                JOptionPane.PLAIN_MESSAGE
        );

        if (input == null) {
            return;
        }

        try {
            double monto = Double.parseDouble(input.trim());
            if (esDeposito) {
                cuentaDAO.depositar(cuenta.getId(), monto);
            } else {
                cuentaDAO.retirar(cuenta.getId(), monto);
            }
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Movimiento realizado con éxito.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo completar", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void alternarEstadoCuenta() {
        CuentaAhorro cuenta = getCuentaSeleccionada();
        if (cuenta == null) {
            return;
        }
        try {
            cuentaDAO.cambiarEstado(cuenta.getId(), !cuenta.isActivo());
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cambiar el estado:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static class MiembroItem {

        private final Miembro miembro;

        MiembroItem(Miembro miembro) {
            this.miembro = miembro;
        }

        @Override
        public String toString() {
            return miembro.getNombreCompleto() + " (" + miembro.getCedula() + ")";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Prueba - Módulo de Ahorros");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new PnlAhorros());
            frame.setSize(950, 580);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnDepositar;
    private javax.swing.JButton btnEstado;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnNuevaCuenta;
    private javax.swing.JButton btnRetirar;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblSaldoSocioSeleccionado;
    private javax.swing.JLabel lblTitCuentas;
    private javax.swing.JLabel lblTitSocio;
    private javax.swing.JLabel lblTitTotal;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalAhorrado;
    private javax.swing.JLabel lblTotalCuentas;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlBusqueda;
    private javax.swing.JPanel pnlSuperior;
    private javax.swing.JPanel pnlTarjetaCuentas;
    private javax.swing.JPanel pnlTarjetaSocio;
    private javax.swing.JPanel pnlTarjetaTotal;
    private javax.swing.JPanel pnlTarjetas;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tablaAhorros;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration                   
}