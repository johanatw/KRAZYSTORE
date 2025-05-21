/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.GraficoIngresosEgresosDTO;
import com.krazystore.krazystore.DTO.GraficoVentasPorCategoriaDTO;
import com.krazystore.krazystore.DTO.MovimientoMensualDTO;
import com.krazystore.krazystore.DTO.ProductoVentasDTO;
import java.util.List;

/**
 *
 * @author HP
 */
public interface DashboardService {

    public GraficoIngresosEgresosDTO obtenerIngresosYEgresosUltimos6Meses();

    public GraficoVentasPorCategoriaDTO obtenerVentasPorCategoriaMes(String mes);

    public List<ProductoVentasDTO> obtenerTopProductosVendidos(String mes);
    
}
