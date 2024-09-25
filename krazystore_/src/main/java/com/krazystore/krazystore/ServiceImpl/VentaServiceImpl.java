/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.ConceptoEntity;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Entity.TimbradoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Repository.VentaRepository;
import com.krazystore.krazystore.Service.AnticipoService;
import com.krazystore.krazystore.Service.DetallePedidoService;
import com.krazystore.krazystore.Service.DetalleVentaService;
import com.krazystore.krazystore.Service.MovimientoService;
import com.krazystore.krazystore.Service.PagoService;
import com.krazystore.krazystore.Service.PedidoService;
import com.krazystore.krazystore.Service.TimbradoService;
import com.krazystore.krazystore.Service.VentaService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class VentaServiceImpl implements VentaService{
    private final VentaRepository ventarepository;

    public VentaServiceImpl(VentaRepository ventarepository) {
        this.ventarepository = ventarepository;
    }
    @Autowired
    private DetalleVentaService detalleVentaService;
    
    
    @Autowired
    private MovimientoService movimientoService;
    
    
    @Autowired
    private TimbradoService timbradoService;
    
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private DetallePedidoService detallePedidoService;
    
    @Override
    public List<VentaEntity> findAll() {
        return ventarepository.findAllByOrderByIdDesc();
    }

    @Override
    public Optional<VentaEntity> findById(Long id) {
        return ventarepository.findById(id);
    }

    @Override
    public VentaEntity saveVenta(VentaEntity ventaEntity, List<DetalleVentaEntity> detalle, List<PagoEntity> pagos) {
        TimbradoEntity timbrado = timbradoService.getTimbradoVigente().get();
        ventaEntity.setTimbrado(timbrado);
        ventaEntity.setNroFactura(timbradoService.getNroFactura(timbrado));
        VentaEntity venta = ventarepository.save(ventaEntity);
        timbrado.setCantUtilizada(timbrado.getCantUtilizada() + 1);
        timbrado.setUltimoRemitido(timbrado.getUltimoRemitido()+1);
        timbradoService.updateTimbrado(timbrado);
        
        detalleVentaService.saveDetalleVenta(venta, detalle);
        
        movimientoService.saveMovimiento(venta, pagos);
        
        if(venta.getPedido() != null){
            PedidoEntity pedido = venta.getPedido();
            detallePedidoService.updateDetallesFacturadas(detalle, pedido, "REGISTRAR");
        }
        
        return venta;
    }

    @Override
    public VentaEntity updateVenta(VentaEntity ventaEntity, Long id) {
        
        VentaEntity updatedVenta = ventarepository.findById(id).get();
        
        updatedVenta.setCliente(ventaEntity.getCliente());
        updatedVenta.setFecha(ventaEntity.getFecha());
        updatedVenta.setMontoIva(ventaEntity.getMontoIva());
        updatedVenta.setMontoTotal(ventaEntity.getMontoTotal());

        return ventarepository.save(updatedVenta);
    }
    
    @Override
    public void anularFactura(Long id) {
        
        VentaEntity facturaVenta = ventarepository.findById(id).get();
        facturaVenta.setActivo(false);
        List<DetalleVentaEntity> detalleVenta = detalleVentaService.findByIdVenta(id);
        
        movimientoService.anularVenta(facturaVenta);
                
        if(facturaVenta.getPedido() != null){
            PedidoEntity pedido = facturaVenta.getPedido();
            detallePedidoService.updateDetallesFacturadas(detalleVenta, pedido, "ELIMINAR");
 
        }
        
       
    }

    @Override
    public void deleteVenta(Long id) {
        ventarepository.deleteById(id);
    }
    
}
