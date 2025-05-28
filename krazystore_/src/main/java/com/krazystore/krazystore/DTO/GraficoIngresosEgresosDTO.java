/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author HP
 */
public class GraficoIngresosEgresosDTO {
    private List<String> labels;
    private List<BigDecimal> ingresosAnticipo;
    private List<BigDecimal> ingresosVenta;
    private List<BigDecimal> ingresosOtros;
    private List<BigDecimal> egresosAnticipo;
    private List<BigDecimal> egresosCompra;
    private List<BigDecimal> egresosOtros;
    private List<BigDecimal> totalIngresos;
    private List<BigDecimal> totalEgresos;

    public GraficoIngresosEgresosDTO() {
    }

    public GraficoIngresosEgresosDTO(List<String> labels, List<BigDecimal> ingresosAnticipo, List<BigDecimal> ingresosVenta, List<BigDecimal> ingresosOtros, List<BigDecimal> egresosVenta, List<BigDecimal> egresosOtros, List<BigDecimal> totalIngresos, List<BigDecimal> totalEgresos) {
        this.labels = labels;
        this.ingresosAnticipo = ingresosAnticipo;
        this.ingresosVenta = ingresosVenta;
        this.ingresosOtros = ingresosOtros;
        this.egresosCompra = egresosVenta;
        this.egresosOtros = egresosOtros;
        this.totalIngresos = totalIngresos;
        this.totalEgresos = totalEgresos;
    }

    public GraficoIngresosEgresosDTO(List<String> labels, List<BigDecimal> ingresosAnticipo, 
            List<BigDecimal> ingresosVenta, List<BigDecimal> ingresosOtros, List<BigDecimal> totalIngresos) {
        this.labels = labels;
        this.ingresosAnticipo = ingresosAnticipo;
        this.ingresosVenta = ingresosVenta;
        this.ingresosOtros = ingresosOtros;
        this.totalIngresos = totalIngresos;
    }

    public GraficoIngresosEgresosDTO(List<String> labels, List<BigDecimal> egresosCompra, List<BigDecimal> egresosOtros, List<BigDecimal> totalEgresos) {
        this.labels = labels;
        this.egresosCompra = egresosCompra;
        this.egresosOtros = egresosOtros;
        this.totalEgresos = totalEgresos;
    }

    public GraficoIngresosEgresosDTO(List<String> labels, List<BigDecimal> totalIngresos, List<BigDecimal> totalEgresos) {
        this.labels = labels;
        this.totalIngresos = totalIngresos;
        this.totalEgresos = totalEgresos;
    }
    
    

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<BigDecimal> getIngresosAnticipo() {
        return ingresosAnticipo;
    }

    public void setIngresosAnticipo(List<BigDecimal> ingresosAnticipo) {
        this.ingresosAnticipo = ingresosAnticipo;
    }

    public List<BigDecimal> getIngresosVenta() {
        return ingresosVenta;
    }

    public void setIngresosVenta(List<BigDecimal> ingresosVenta) {
        this.ingresosVenta = ingresosVenta;
    }

    public List<BigDecimal> getIngresosOtros() {
        return ingresosOtros;
    }

    public void setIngresosOtros(List<BigDecimal> ingresosOtros) {
        this.ingresosOtros = ingresosOtros;
    }

    public List<BigDecimal> getEgresosAnticipo() {
        return egresosAnticipo;
    }

    public void setEgresosAnticipo(List<BigDecimal> egresosAnticipo) {
        this.egresosAnticipo = egresosAnticipo;
    }

    public List<BigDecimal> getEgresosCompra() {
        return egresosCompra;
    }

    public void setEgresosCompra(List<BigDecimal> egresosCompra) {
        this.egresosCompra = egresosCompra;
    }

    public List<BigDecimal> getEgresosOtros() {
        return egresosOtros;
    }

    public void setEgresosOtros(List<BigDecimal> egresosOtros) {
        this.egresosOtros = egresosOtros;
    }

    public List<BigDecimal> getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(List<BigDecimal> totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public List<BigDecimal> getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(List<BigDecimal> totalEgresos) {
        this.totalEgresos = totalEgresos;
    }

    
    
}
