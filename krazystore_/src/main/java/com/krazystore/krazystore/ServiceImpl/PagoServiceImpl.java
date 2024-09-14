/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;


import com.krazystore.krazystore.DTO.PedidoMontoPagadoDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;

import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;

import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Repository.PagoRepository;
import com.krazystore.krazystore.Service.AnticipoService;

import com.krazystore.krazystore.Service.PagoService;

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
public class PagoServiceImpl implements PagoService {
    private final PagoRepository pagoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    
   @Autowired
    private AnticipoService anticipoService;
 

    @Override
    public List<PagoEntity> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<PagoEntity> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public PagoEntity savePago(PagoEntity pagoEntity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    @Override
    public Iterable<PagoEntity> savePagos(MovimientoEntity movimiento, List<PagoEntity> pagos) {
         
        List<AnticipoEntity> anticiposUtilizados = new ArrayList<>();
        
        pagos.forEach(d -> {
            d.setMovimiento(movimiento);
            
            if("Anticipo".equals(d.getFormaPago().getDescripcion())){
              AnticipoEntity anticipo = d.getAnticipo();
              anticipo.setUtilizado(anticipo.getUtilizado() + (int)(long)d.getImporte());
              anticipo.setSaldo(anticipo.getSaldo() - (int)(long)d.getImporte());
              anticiposUtilizados.add(anticipo);
            }
        });
        Iterable<PagoEntity> newPagos =  pagoRepository.saveAll(pagos);
        
        if(!anticiposUtilizados.isEmpty()){
           anticipoService.updateAnticipos(anticiposUtilizados);
       }

        return newPagos; 
    }
    

    
     @Override
    public Iterable<PagoEntity> savePagos(VentaEntity venta, List<PagoEntity> pagos) {
        System.out.println("savepago");
        
        
        pagos.forEach(d -> {
          //  d.setVenta(venta);
        });
        
        
        List<AnticipoEntity> anticiposModificar = new ArrayList<>();
        PedidoEntity pedido = venta.getPedido();
        List<MovimientoEntity> movimientos = new ArrayList<>();
        pagos.forEach(d -> {
          // MovimientoEntity nuevoMovimiento = movimientoService.crearMovimiento(d, venta);
         //  movimientos.add(nuevoMovimiento);
           
           /*if(pedido != null){
                if("Anticipo".equals(d.getFormaPago().getDescripcion())){
                  AnticipoEntity anticipo = d.getAnticipo();
                  anticipo.setUtilizado(anticipo.getUtilizado() + (int)(long)d.getImporte());
                  anticipo.setSaldo(anticipo.getSaldo() - (int)(long)d.getImporte());
                  anticiposModificar.add(anticipo);
                  
                  
                }else{
                    
                    pedido.setPagado(pedido.getPagado() + (int)(long)d.getImporte());
                    
                }
            }*/

        });
       
       //movimientoService.saveMovimientos(movimientos);
       /*if(!anticiposModificar.isEmpty()){
           anticipoService.updateAnticipos(anticiposModificar);
       }
       
       if(pedido != null){
           pedidoService.updatePedido(pedido, pedido.getId());
       }*/
     

        return pagoRepository.saveAll(pagos);
    }
    
   

    @Override
    public PagoEntity updatePago(PagoEntity pagoEntity, Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deletePago(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deletePagosByAnticipo(Long id) {
        pagoRepository.deletePagosByAnticipo(id);
    }

    @Override
    public void deletePagosByReembolso(Long id) {
        pagoRepository.deletePagosByReembolso(id);
    }
    
    @Override
    public void deletePagosByReembolsos(List<Long> ids) {
        pagoRepository.deletePagosByReembolsos(ids);
    }

    @Override
    public Optional<PedidoMontoPagadoDTO> getPagosPedido(Long id) {
        return pagoRepository.getPagosPedido(id);
                
    }
}
