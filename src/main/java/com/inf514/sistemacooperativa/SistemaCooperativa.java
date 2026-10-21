package com.inf514.sistemacooperativa;
import com.inf514.sistemacooperativa.vista.*;

public class SistemaCooperativa {

    /*public static void main(String[] args) {
        // Hace visible la pantalla de login al arrancar el programa
        java.awt.EventQueue.invokeLater(() -> {
           FrmMainLogin pantallaLogin = new FrmMainLogin();
           pantallaLogin.setLocationRelativeTo(null);
           pantallaLogin.setVisible(true);   
        });
//        java.awt.EventQueue.invokeLater(()-> {
//            FrmPrincipal pantallaMiembro = new FrmPrincipal();
//            pantallaMiembro.setVisible(true);
//            pantallaMiembro.setLocationRelativeTo(null);
//        });
    }*/
    public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(() -> {
        javax.swing.JFrame ventana = new javax.swing.JFrame("Préstamos");
        ventana.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        ventana.setContentPane(new PnlPrestamos());
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    });
}
}
