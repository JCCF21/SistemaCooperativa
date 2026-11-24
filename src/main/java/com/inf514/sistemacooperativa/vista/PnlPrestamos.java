/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.inf514.sistemacooperativa.vista;

import com.inf514.sistemacooperativa.dao.PrestamoDAO;
import com.inf514.sistemacooperativa.modelo.Prestamo;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.util.regex.Pattern;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import com.inf514.sistemacooperativa.dao.PagoPrestamoDAO;
import java.math.BigDecimal;


/**
 *
 * @author jfria
 */
public class PnlPrestamos extends javax.swing.JPanel {
    
    
private final PrestamoDAO prestamoDAO = new PrestamoDAO();
private final PagoPrestamoDAO pagoPrestamoDAO = new PagoPrestamoDAO();


public PnlPrestamos() {
    
    
    initComponents();
    cargarPrestamos();
    configurarBuscador();
    colorearEstados();

   
}
private void cargarPrestamos() {

    DefaultTableModel modelo =
            (DefaultTableModel) tblPrestamos.getModel();

    modelo.setRowCount(0);

    List<Prestamo> prestamos = prestamoDAO.obtenerPrestamos();

    for (Prestamo p : prestamos) {

        Object cuotasPagadas = "—";
        Object totalPagado = "—";
        Object saldoPendiente = "—";

        if ("Aprobado".equals(p.getEstado())
                || "Pagado".equals(p.getEstado())) {

            int cuotas = pagoPrestamoDAO.obtenerCuotasPagadas(
                    p.getId());

            BigDecimal pagado = pagoPrestamoDAO.obtenerTotalPagado(
                    p.getId());

            BigDecimal saldo = p.getTotalPagar().subtract(pagado);

            cuotasPagadas = cuotas + " de " + p.getCantidadCuotas();

            totalPagado = pagado.setScale(2);

            saldoPendiente = saldo.setScale(2);
        }

        modelo.addRow(new Object[]{
            p.getId(),
            p.getMiembro().getCedula(),
            p.getMiembro().getNombreCompleto(),
            p.getMontoSolicitado(),
            p.getCantidadCuotas(),
            p.getTasaInteres(),
            p.getInteresTotal(),
            p.getTotalPagar(),
            p.getCuotaMensual(),
            p.getFechaSolicitud(),
            p.getEstado(),
            cuotasPagadas,
            totalPagado,
            saldoPendiente
        });
    }
}
private void configurarBuscador() {

    DefaultTableModel modelo =
            (DefaultTableModel) tblPrestamos.getModel();

    TableRowSorter<DefaultTableModel> sorter =
            new TableRowSorter<>(modelo);

    tblPrestamos.setRowSorter(sorter);

    txtBuscarPrestamo.getDocument().addDocumentListener(
            new DocumentListener() {

        private void filtrar() {

            String texto = txtBuscarPrestamo.getText().trim();

            if (texto.isEmpty()
                    || texto.equals("Buscar por Nombre, ID o cédula...")) {

                sorter.setRowFilter(null);

            } else {

                sorter.setRowFilter(
                        RowFilter.regexFilter(
                                "(?i)" + Pattern.quote(texto),
                                0, 1, 2
                        )
                );
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            filtrar();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            filtrar();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            filtrar();
        }
    });
}
private void colorearEstados() {

    tblPrestamos.getColumnModel().getColumn(10)
            .setCellRenderer(new DefaultTableCellRenderer() {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            super.getTableCellRendererComponent(
                    table, value, isSelected,
                    hasFocus, row, column);

            setHorizontalAlignment(JLabel.CENTER);
            setFont(table.getFont().deriveFont(Font.BOLD));

            setBackground(table.getBackground());
            setForeground(table.getForeground());

            String estado = String.valueOf(value);

            switch (estado) {

                case "Aprobado":
                    setBackground(new Color(213, 238, 220));
                    setForeground(new Color(33, 107, 53));
                    break;

                case "Rechazado":
                    setBackground(new Color(248, 215, 218));
                    setForeground(new Color(161, 36, 48));
                    break;

                case "En espera":
                    setBackground(new Color(255, 240, 194));
                    setForeground(new Color(137, 102, 0));
                    break;
                 
                case "Pagado":
                    setBackground(new Color(214, 230, 250));
                    setForeground(new Color(36, 89, 154));
                    break;
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }

            return this;
        }
    });
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPrestamos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnNuevaSolicitud = new javax.swing.JButton();
        btnAprobar = new javax.swing.JButton();
        btnRechazar = new javax.swing.JButton();
        PnlBuscador = new javax.swing.JPanel();
        txtBuscarPrestamo = new javax.swing.JTextField();
        btnModificar = new javax.swing.JButton();
        btnRegistrarPago = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(244, 239, 230));

        tblPrestamos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID prestamo", "Cedula", "Miembro", "Monto RD$", "Cuotas", "Tasa (%)", "Interes (RD$)", "Total a Pagar", "Cuota Mensual", "Fecha", "Estado", "Cuotas pagadas", "Total pagado (RD$)", "Saldo pendiente (RD$)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPrestamos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tblPrestamos);
        if (tblPrestamos.getColumnModel().getColumnCount() > 0) {
            tblPrestamos.getColumnModel().getColumn(0).setPreferredWidth(85);
            tblPrestamos.getColumnModel().getColumn(1).setPreferredWidth(120);
            tblPrestamos.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblPrestamos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblPrestamos.getColumnModel().getColumn(4).setPreferredWidth(55);
            tblPrestamos.getColumnModel().getColumn(5).setPreferredWidth(70);
            tblPrestamos.getColumnModel().getColumn(6).setPreferredWidth(100);
            tblPrestamos.getColumnModel().getColumn(7).setPreferredWidth(120);
            tblPrestamos.getColumnModel().getColumn(8).setPreferredWidth(120);
            tblPrestamos.getColumnModel().getColumn(9).setPreferredWidth(100);
            tblPrestamos.getColumnModel().getColumn(10).setPreferredWidth(100);
            tblPrestamos.getColumnModel().getColumn(11).setPreferredWidth(120);
            tblPrestamos.getColumnModel().getColumn(12).setPreferredWidth(145);
            tblPrestamos.getColumnModel().getColumn(13).setPreferredWidth(155);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 863, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(24, 75, 95));

        jPanel3.setBackground(new java.awt.Color(221, 238, 249));

        jLabel1.setForeground(new java.awt.Color(24, 75, 95));
        jLabel1.setText("GESTION DE PRESTAMOS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(34, 34, 34))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(351, 351, 351)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        btnNuevaSolicitud.setBackground(new java.awt.Color(24, 75, 95));
        btnNuevaSolicitud.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNuevaSolicitud.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevaSolicitud.setText("Nueva solicitud");
        btnNuevaSolicitud.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnNuevaSolicitud.setFocusPainted(false);
        btnNuevaSolicitud.setIconTextGap(8);
        btnNuevaSolicitud.setPreferredSize(new java.awt.Dimension(170, 40));
        btnNuevaSolicitud.addActionListener(this::btnNuevaSolicitudActionPerformed);

        btnAprobar.setBackground(new java.awt.Color(46, 139, 111));
        btnAprobar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAprobar.setForeground(new java.awt.Color(255, 255, 255));
        btnAprobar.setText("Aprobar");
        btnAprobar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAprobar.setFocusPainted(false);
        btnAprobar.setIconTextGap(8);
        btnAprobar.setPreferredSize(new java.awt.Dimension(170, 40));
        btnAprobar.addActionListener(this::btnAprobarActionPerformed);

        btnRechazar.setBackground(new java.awt.Color(196, 77, 77));
        btnRechazar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRechazar.setForeground(new java.awt.Color(255, 255, 255));
        btnRechazar.setText("Rechazar");
        btnRechazar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRechazar.setFocusPainted(false);
        btnRechazar.setIconTextGap(8);
        btnRechazar.setPreferredSize(new java.awt.Dimension(170, 40));
        btnRechazar.addActionListener(this::btnRechazarActionPerformed);

        PnlBuscador.setBackground(new java.awt.Color(255, 255, 255));

        txtBuscarPrestamo.setForeground(new java.awt.Color(153, 153, 153));
        txtBuscarPrestamo.setText("Buscar por Nombre, ID o cédula...");
        txtBuscarPrestamo.setBorder(null);
        txtBuscarPrestamo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarPrestamoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarPrestamoFocusLost(evt);
            }
        });

        javax.swing.GroupLayout PnlBuscadorLayout = new javax.swing.GroupLayout(PnlBuscador);
        PnlBuscador.setLayout(PnlBuscadorLayout);
        PnlBuscadorLayout.setHorizontalGroup(
            PnlBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtBuscarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        PnlBuscadorLayout.setVerticalGroup(
            PnlBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtBuscarPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        btnModificar.setBackground(new java.awt.Color(206, 146, 52));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("Modificar");
        btnModificar.setToolTipText("Modificar solicitud de préstamo");
        btnModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnModificar.setFocusPainted(false);
        btnModificar.setIconTextGap(8);
        btnModificar.setPreferredSize(new java.awt.Dimension(170, 40));
        btnModificar.addActionListener(this::btnModificarActionPerformed);

        btnRegistrarPago.setBackground(new java.awt.Color(37, 99, 165));
        btnRegistrarPago.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRegistrarPago.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarPago.setText("Registrar pago");
        btnRegistrarPago.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrarPago.setFocusPainted(false);
        btnRegistrarPago.addActionListener(this::btnRegistrarPagoActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PnlBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnNuevaSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnRechazar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnAprobar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRegistrarPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(btnNuevaSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAprobar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnRechazar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRegistrarPago, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarPrestamoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarPrestamoFocusGained
        if (txtBuscarPrestamo.getText().equals("Buscar por Nombre, ID o cédula...")) {
    txtBuscarPrestamo.setText("");
    txtBuscarPrestamo.setForeground(java.awt.Color.BLACK);
}
    }//GEN-LAST:event_txtBuscarPrestamoFocusGained

    private void txtBuscarPrestamoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarPrestamoFocusLost
        if (txtBuscarPrestamo.getText().trim().isEmpty()) {
    txtBuscarPrestamo.setText("Buscar por Nombre, ID o cédula...");
    txtBuscarPrestamo.setForeground(java.awt.Color.GRAY);
}
    }//GEN-LAST:event_txtBuscarPrestamoFocusLost

    private void btnNuevaSolicitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaSolicitudActionPerformed
       java.awt.Frame padre = (java.awt.Frame)
        javax.swing.SwingUtilities.getWindowAncestor(this);

DlgNuevoPrestamo dialogo = new DlgNuevoPrestamo(padre, true);

dialogo.setLocationRelativeTo(this);
dialogo.setVisible(true);
cargarPrestamos();
    }//GEN-LAST:event_btnNuevaSolicitudActionPerformed

    private void btnAprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAprobarActionPerformed
        int fila = tblPrestamos.getSelectedRow();

if (fila == -1) {
    JOptionPane.showMessageDialog(this,
            "Seleccione una solicitud de préstamo.");
    return;
}

int filaModelo = tblPrestamos.convertRowIndexToModel(fila);

int id = (int) tblPrestamos.getModel().getValueAt(filaModelo, 0);

String estado = tblPrestamos.getModel()
        .getValueAt(filaModelo, 10).toString();

if (!estado.equals("En espera")) {
    JOptionPane.showMessageDialog(this,
            "Solo se pueden aprobar solicitudes en espera.");
    return;
}

int confirmacion = JOptionPane.showConfirmDialog(
        this,
        "¿Está seguro de aprobar esta solicitud?",
        "Confirmar aprobación",
        JOptionPane.YES_NO_OPTION
);

if (confirmacion == JOptionPane.YES_OPTION) {

    try {
        if (prestamoDAO.cambiarEstado(id, "Aprobado")) {

            cargarPrestamos();

            JOptionPane.showMessageDialog(this,
                    "Préstamo aprobado correctamente.");

        } else {
            cargarPrestamos();

            JOptionPane.showMessageDialog(this,
                    "No se pudo aprobar la solicitud.");
        }

    } catch (RuntimeException e) {
        JOptionPane.showMessageDialog(this,
                "Error al aprobar: " + e.getMessage());
    }
}
    }//GEN-LAST:event_btnAprobarActionPerformed

    private void btnRechazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechazarActionPerformed
       int fila = tblPrestamos.getSelectedRow();

if (fila == -1) {
    JOptionPane.showMessageDialog(this,
            "Seleccione una solicitud de préstamo.");
    return;
}

int filaModelo = tblPrestamos.convertRowIndexToModel(fila);

int id = (int) tblPrestamos.getModel().getValueAt(filaModelo, 0);

String estado = tblPrestamos.getModel()
        .getValueAt(filaModelo, 10).toString();

if (!estado.equals("En espera")) {
    JOptionPane.showMessageDialog(this,
            "Solo se pueden rechazar solicitudes en espera.");
    return;
}

int confirmacion = JOptionPane.showConfirmDialog(
        this,
        "¿Está seguro de rechazar esta solicitud?",
        "Confirmar rechazo",
        JOptionPane.YES_NO_OPTION
);

if (confirmacion == JOptionPane.YES_OPTION) {

    try {
        if (prestamoDAO.cambiarEstado(id, "Rechazado")) {

            cargarPrestamos();

            JOptionPane.showMessageDialog(this,
                    "Préstamo rechazado correctamente.");

        } else {
            cargarPrestamos();

            JOptionPane.showMessageDialog(this,
                    "No se pudo rechazar la solicitud.");
        }

    } catch (RuntimeException e) {
        JOptionPane.showMessageDialog(this,
                "Error al rechazar: " + e.getMessage());
    }
}
    }//GEN-LAST:event_btnRechazarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        int fila = tblPrestamos.getSelectedRow();

if (fila == -1) {
    JOptionPane.showMessageDialog(this,
            "Selecciona una solicitud para modificar.");
    return;
}

int filaModelo = tblPrestamos.convertRowIndexToModel(fila);

int id = (int) tblPrestamos.getModel()
        .getValueAt(filaModelo, 0);

try {

    Prestamo prestamo = prestamoDAO.buscarPorId(id);

    if (prestamo == null) {
        JOptionPane.showMessageDialog(this,
                "No se encontró el préstamo.");
        return;
    }

    if (!"En espera".equals(prestamo.getEstado())) {
        JOptionPane.showMessageDialog(this,
                "Solo se pueden modificar solicitudes en espera.");
        return;
    }

    if (!prestamo.getMiembro().isActivo()) {
        JOptionPane.showMessageDialog(this,
                "No se puede modificar la solicitud "
                + "porque el miembro está inactivo.");
        return;
    }

    java.awt.Frame padre = (java.awt.Frame)
            javax.swing.SwingUtilities.getWindowAncestor(this);

    DlgNuevoPrestamo dialogo =
            new DlgNuevoPrestamo(padre, true, prestamo);

    dialogo.setLocationRelativeTo(this);
    dialogo.setVisible(true);

    cargarPrestamos();

} catch (Exception e) {

    JOptionPane.showMessageDialog(this,
            "Error al modificar el préstamo: "
            + e.getMessage());
}
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnRegistrarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarPagoActionPerformed
       int fila = tblPrestamos.getSelectedRow();

if (fila == -1) {
    JOptionPane.showMessageDialog(this,
            "Debes seleccionar un préstamo.");
    return;
}

int filaModelo = tblPrestamos.convertRowIndexToModel(fila);

int id = (int) tblPrestamos.getModel()
        .getValueAt(filaModelo, 0);

try {
    Prestamo prestamo = prestamoDAO.buscarPorId(id);

    if (prestamo == null) {
        JOptionPane.showMessageDialog(this,
                "El préstamo no existe.");
        return;
    }

    if (!"Aprobado".equals(prestamo.getEstado())) {
        JOptionPane.showMessageDialog(this,
                "Solo se pueden pagar préstamos aprobados.");
        return;
    }

    java.awt.Frame padre = (java.awt.Frame)
            javax.swing.SwingUtilities.getWindowAncestor(this);

    DlgPagoPrestamo dialogo = new DlgPagoPrestamo(
            padre, true, prestamo);

    dialogo.setLocationRelativeTo(this);
    dialogo.setVisible(true);

    cargarPrestamos();

} catch (Exception e) {
    JOptionPane.showMessageDialog(this,
            "Error al abrir el diálogo de pago: "
            + e.getMessage());
}
    }//GEN-LAST:event_btnRegistrarPagoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnlBuscador;
    private javax.swing.JButton btnAprobar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevaSolicitud;
    private javax.swing.JButton btnRechazar;
    private javax.swing.JButton btnRegistrarPago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPrestamos;
    private javax.swing.JTextField txtBuscarPrestamo;
    // End of variables declaration//GEN-END:variables
}
