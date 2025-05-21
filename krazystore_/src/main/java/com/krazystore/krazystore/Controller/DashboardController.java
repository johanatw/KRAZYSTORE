/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.GraficoIngresosEgresosDTO;
import com.krazystore.krazystore.DTO.GraficoVentasPorCategoriaDTO;
import com.krazystore.krazystore.DTO.MovimientoMensualDTO;
import com.krazystore.krazystore.DTO.ProductoVentasDTO;
import com.krazystore.krazystore.Service.DashboardService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/dashboard")
public class DashboardController {

    public DashboardController(com.krazystore.krazystore.Service.DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    private final DashboardService dashboardService;
    
    @GetMapping
    public GraficoIngresosEgresosDTO obtenerIngresosYEgresosUltimos6Meses() {
        return dashboardService.obtenerIngresosYEgresosUltimos6Meses();
    }
    
    @GetMapping("/ventas_por_categorias")
    public GraficoVentasPorCategoriaDTO obtenerVentasPorCategoriaMes(@RequestParam(value="mes") String mes) {
        return dashboardService.obtenerVentasPorCategoriaMes(mes);
    }
    
    @GetMapping("/top_productos")
    public List<ProductoVentasDTO> obtenerTopProductosVendidos(@RequestParam(value="mes") String mes) {
        return dashboardService.obtenerTopProductosVendidos(mes);
    }
}
