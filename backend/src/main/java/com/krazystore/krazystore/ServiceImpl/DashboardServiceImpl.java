/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.CategoriaVentasDTO;
import com.krazystore.krazystore.DTO.GraficoIngresosEgresosDTO;
import com.krazystore.krazystore.DTO.GraficoVentasPorCategoriaDTO;
import com.krazystore.krazystore.DTO.MovimientoMensualDTO;
import com.krazystore.krazystore.DTO.ProductoVentasDTO;
import com.krazystore.krazystore.Service.DashboardService;
import com.krazystore.krazystore.Service.MovimientoService;
import com.krazystore.krazystore.Service.VentaService;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private MovimientoService movimientoService;
    
    @Autowired
    private VentaService ventaService;
    
    @Override
    public GraficoIngresosEgresosDTO obtenerIngresosYEgresosPorAño(Integer año) {
        System.out.println("entraa dashboard");
        List<MovimientoMensualDTO> movimientosMensuales = movimientoService.obtenerIngresosYEgresosPorAño(año);
        
        List<String> labels = new ArrayList<>();
        List<BigDecimal> totalIngresos = new ArrayList<>();
        List<BigDecimal> totalEgresos = new ArrayList<>();

        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMMM", new Locale("es"));

        for (MovimientoMensualDTO m : movimientosMensuales) {
            labels.add(outputFormat.format(m.getMes())); // Ej: "enero", "febrero"
            totalIngresos.add(m.getTotalIngresos());
            totalEgresos.add(m.getTotalEgresos());
        }
        return new GraficoIngresosEgresosDTO(labels, totalIngresos, totalEgresos);
        }

    @Override
    public GraficoVentasPorCategoriaDTO obtenerVentasPorCategoriaMes(String mes) {
        List<CategoriaVentasDTO> topCategorias = ventaService.obtenerVentasPorCategoriaMes(mes);
        List<String> labels = new ArrayList<>();
        List<Long> totales = new ArrayList<>();
        
        long otrosTotal = 0;

        for (int i = 0; i < topCategorias.size(); i++) {

            if (i < 5) {
                labels.add(topCategorias.get(i).getCategoria());
                totales.add(topCategorias.get(i).getCantidad());
            } else {
                otrosTotal += topCategorias.get(i).getCantidad();
            }
        }

        if (otrosTotal > 0) {
            labels.add("Otros");
            totales.add(otrosTotal);
        }
        
        return new GraficoVentasPorCategoriaDTO(labels,totales);
        
    }

    @Override
    public List<ProductoVentasDTO> obtenerTopProductosVendidos(String mes) {
        return ventaService.obtenerTopProductosVendidos(mes);
    }

    @Override
    public GraficoIngresosEgresosDTO obtenerIngresosPorAño(Integer año) {
        System.out.println("entraa dashboard");
        List<MovimientoMensualDTO> movimientosMensuales = movimientoService.obtenerIngresosPorAño(año);
        
        List<String> labels = new ArrayList<>();
        List<BigDecimal> ingresosAnticipo = new ArrayList<>();
        List<BigDecimal> ingresosVenta = new ArrayList<>();
        List<BigDecimal> ingresosOtro = new ArrayList<>();
        List<BigDecimal> totalIngresos = new ArrayList<>();

        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMMM", new Locale("es"));

        for (MovimientoMensualDTO m : movimientosMensuales) {
            labels.add(outputFormat.format(m.getMes())); // Ej: "enero", "febrero"
            ingresosAnticipo.add(m.getIngresosAnticipo());
            ingresosVenta.add(m.getIngresosVenta());
            ingresosOtro.add(m.getIngresosOtros());
            totalIngresos.add(m.getTotalIngresos());
        }
        return new GraficoIngresosEgresosDTO(labels,ingresosAnticipo, ingresosVenta, ingresosOtro, totalIngresos);
        
    }

    @Override
    public List<Integer> obtenerAñosDisponibles() {
        return movimientoService.obtenerAñosDisponibles();
    }

    @Override
    public GraficoIngresosEgresosDTO obtenerEgresosPorAño(Integer año) {
        List<MovimientoMensualDTO> movimientosMensuales = movimientoService.obtenerEgresosPorAño(año);
        
        List<String> labels = new ArrayList<>();
        List<BigDecimal> egresosCompra = new ArrayList<>();
        List<BigDecimal> egresosOtro = new ArrayList<>();
        List<BigDecimal> totalEgresos = new ArrayList<>();

        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMMM", new Locale("es"));

        for (MovimientoMensualDTO m : movimientosMensuales) {
            labels.add(outputFormat.format(m.getMes())); // Ej: "enero", "febrero"
            egresosCompra.add(m.getEgresosCompra());
            egresosOtro.add(m.getEgresosOtros());
            totalEgresos.add(m.getTotalIngresos());
        }
        return new GraficoIngresosEgresosDTO(labels, egresosCompra, egresosOtro, totalEgresos);
    }
    
}
