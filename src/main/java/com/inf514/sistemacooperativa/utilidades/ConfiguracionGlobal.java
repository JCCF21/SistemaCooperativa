package com.inf514.sistemacooperativa.utilidades;

import java.math.BigDecimal;

public class ConfiguracionGlobal {
    public static final String VERSION_APP = "1.0";

    // ---- Aporte de cada miembro: porcentaje de su ingreso mensual ----
    // ingreso < 30,000            -> 7%
    // 30,000 <= ingreso <= 70,000 -> 9%
    // ingreso > 70,000            -> 12%
    public static final BigDecimal SALARIO_LIMITE_BAJO = new BigDecimal("30000");
    public static final BigDecimal SALARIO_LIMITE_ALTO = new BigDecimal("70000");
    public static final BigDecimal APORTE_SALARIO_BAJO = new BigDecimal("0.07");
    public static final BigDecimal APORTE_SALARIO_MEDIO = new BigDecimal("0.09");
    public static final BigDecimal APORTE_SALARIO_ALTO = new BigDecimal("0.12");

    // Porcentaje del presupuesto de un miembro (sin interés) que pasa a "disponible"
    public static final BigDecimal PORCENTAJE_DISPONIBLE = new BigDecimal("0.40");

    // Interés anual sobre el presupuesto de cada miembro
    public static final BigDecimal TASA_INTERES = new BigDecimal("0.08");
}
