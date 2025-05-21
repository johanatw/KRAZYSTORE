/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import java.util.List;

/**
 *
 * @author HP
 */
public class GraficoVentasPorCategoriaDTO {
    private List<String> labels;
    private List<Long> cantidades;

    public GraficoVentasPorCategoriaDTO() {
    }

    public GraficoVentasPorCategoriaDTO(List<String> labels, List<Long> cantidades) {
        this.labels = labels;
        this.cantidades = cantidades;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Long> getCantidades() {
        return cantidades;
    }

    public void setCantidades(List<Long> cantidades) {
        this.cantidades = cantidades;
    }
    
    
}
