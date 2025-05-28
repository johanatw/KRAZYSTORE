/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Date;

/**
 *
 * @author HP
 */
public class MovimientoMensualDTO {
    private YearMonth mes;
    private BigDecimal ingresosAnticipo;
    private BigDecimal ingresosVenta;
    private BigDecimal ingresosOtros;
    private BigDecimal totalIngresos;
    private BigDecimal egresosAnticipo;
    private BigDecimal egresosCompra;
    private BigDecimal egresosOtros;
    private BigDecimal totalEgresos;

    public MovimientoMensualDTO() {
    }

    public MovimientoMensualDTO(YearMonth mes, BigDecimal totalIngresos, BigDecimal totalEgresos) {
        this.mes = mes;
        this.totalIngresos = totalIngresos;
        this.totalEgresos = totalEgresos;
    }

    public MovimientoMensualDTO(YearMonth mes, BigDecimal ingresosAnticipo, BigDecimal ingresosVenta, BigDecimal ingresosOtros, BigDecimal totalIngresos, BigDecimal egresosAnticipo, BigDecimal egresosVenta, BigDecimal egresosOtros, BigDecimal totalEgresos) {
        this.mes = mes;
        this.ingresosAnticipo = ingresosAnticipo;
        this.ingresosVenta = ingresosVenta;
        this.ingresosOtros = ingresosOtros;
        this.totalIngresos = totalIngresos;
        this.egresosAnticipo = egresosAnticipo;
        this.egresosCompra = egresosVenta;
        this.egresosOtros = egresosOtros;
        this.totalEgresos = totalEgresos;
    }

    public MovimientoMensualDTO(YearMonth mes, BigDecimal ingresosAnticipo, BigDecimal ingresosVenta, BigDecimal ingresosOtros, BigDecimal totalIngresos) {
        this.mes = mes;
        this.ingresosAnticipo = ingresosAnticipo;
        this.ingresosVenta = ingresosVenta;
        this.ingresosOtros = ingresosOtros;
        this.totalIngresos = totalIngresos;
    }

    public MovimientoMensualDTO(YearMonth mes, BigDecimal egresosCompra, BigDecimal egresosOtros, BigDecimal totalEgresos) {
        this.mes = mes;
        this.egresosCompra = egresosCompra;
        this.egresosOtros = egresosOtros;
        this.totalEgresos = totalEgresos;
    }

    
    public YearMonth getMes() {
        return mes;
    }

    public void setMes(YearMonth mes) {
        this.mes = mes;
    }

    public BigDecimal getIngresosAnticipo() {
        return ingresosAnticipo;
    }

    public void setIngresosAnticipo(BigDecimal ingresosAnticipo) {
        this.ingresosAnticipo = ingresosAnticipo;
    }

    public BigDecimal getIngresosVenta() {
        return ingresosVenta;
    }

    public void setIngresosVenta(BigDecimal ingresosVenta) {
        this.ingresosVenta = ingresosVenta;
    }

    public BigDecimal getIngresosOtros() {
        return ingresosOtros;
    }

    public void setIngresosOtros(BigDecimal ingresosOtros) {
        this.ingresosOtros = ingresosOtros;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigDecimal getEgresosAnticipo() {
        return egresosAnticipo;
    }

    public void setEgresosAnticipo(BigDecimal egresosAnticipo) {
        this.egresosAnticipo = egresosAnticipo;
    }

    public BigDecimal getEgresosCompra() {
        return egresosCompra;
    }

    public void setEgresosCompra(BigDecimal egresosCompra) {
        this.egresosCompra = egresosCompra;
    }

    public BigDecimal getEgresosOtros() {
        return egresosOtros;
    }

    public void setEgresosOtros(BigDecimal egresosOtros) {
        this.egresosOtros = egresosOtros;
    }

    public BigDecimal getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(BigDecimal totalEgresos) {
        this.totalEgresos = totalEgresos;
    }

    
   

    
}
