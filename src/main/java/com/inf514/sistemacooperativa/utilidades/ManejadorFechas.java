/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inf514.sistemacooperativa.utilidades;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author jfria
 */
public class ManejadorFechas {
    public static String obtenerFecha(){
        Date fecha = new Date();
    
    SimpleDateFormat formato = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", new Locale("es", "DO"));
    
    String fechaStr = formato.format(fecha);
    return fechaStr.substring(0,1).toUpperCase()+fechaStr.substring(1);
    }
}
