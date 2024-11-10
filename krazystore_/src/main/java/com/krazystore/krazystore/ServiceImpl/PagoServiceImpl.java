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
import org.springframework.transaction.annotation.Transactional;

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
    public List<PagoEntity> findByIdMovimiento(Long id) {
        return pagoRepository.findByIdMovimiento(id);
    }

    @Override
    public PagoEntity savePago(PagoEntity pagoEntity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    @Override
    public Iterable<PagoEntity> savePagos(MovimientoEntity movimiento, List<PagoEntity> pagos) {
         
        List<AnticipoEntity> anticiposUtilizados = new ArrayList<>();
        List<PagoEntity> pagosGuardar = new ArrayList<>();
        
        pagos.forEach(d -> {
            if(d.getImporte()>0){
                d.setMovimiento(movimiento);
                if(d.getAnticipo() != null){
                    AnticipoEntity anticipo = d.getAnticipo();
                    anticipo.setUtilizado(anticipo.getUtilizado() + (int)(long)d.getImporte());
                    anticipo.setSaldo(anticipo.getSaldo() - (int)(long)d.getImporte());
                    anticiposUtilizados.add(anticipo);
                }
                pagosGuardar.add(d);
            }
            
        });
        Iterable<PagoEntity> newPagos =  pagoRepository.saveAll(pagosGuardar);
        
        if(!anticiposUtilizados.isEmpty()){
           anticipoService.updateAnticipos(anticiposUtilizados);
       }

        return newPagos; 
    }
    
    @Transactional
    @Override
    public Iterable<PagoEntity> revertirPagos(MovimientoEntity movimiento, List<PagoEntity> pagos) {
         
        List<AnticipoEntity> anticipos = new ArrayList<>();
        List<PagoEntity> pagosAnulados = new ArrayList<>();
        
        pagos.forEach(d -> {
            PagoEntity p = new PagoEntity();
            p.setMovimiento(movimiento);
            p.setFormaPago(d.getFormaPago());
            p.setImporte(d.getImporte());
            p.setAnticipo(d.getAnticipo());
            pagosAnulados.add(p);
            
            if(d.getAnticipo() != null){
              AnticipoEntity anticipo = d.getAnticipo();
              anticipo.setUtilizado(anticipo.getUtilizado() - (int)(long)d.getImporte());
              anticipo.setSaldo(anticipo.getSaldo() + (int)(long)d.getImporte());
              anticipos.add(anticipo);
            }
        });
        Iterable<PagoEntity> newPagos =  pagoRepository.saveAll(pagosAnulados);
        
        if(!anticipos.isEmpty()){
           anticipoService.updateAnticipos(anticipos);
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
    public void deletePagosByMovimiento(Long id) {
        List<PagoEntity> pagos = pagoRepository.findByIdMovimiento(id);
        List<AnticipoEntity> anticipos = new ArrayList<>();
        pagos.forEach(d -> {

            if(d.getAnticipo() != null){
              AnticipoEntity anticipo = d.getAnticipo();
              anticipo.setUtilizado(anticipo.getUtilizado() - (int)(long)d.getImporte());
              anticipo.setSaldo(anticipo.getSaldo() + (int)(long)d.getImporte());
              anticipos.add(anticipo);
            }
        });
        
        if(!anticipos.isEmpty()){
           anticipoService.updateAnticipos(anticipos);
       }
        
        pagoRepository.deletePagosByMovimiento(id);
    }
    
    @Override
    public void deletePagosByMovimientos(List<Long> ids) {
        pagoRepository.deletePagosByMovimientos(ids);
    }

    @Override
    public Optional<PedidoMontoPagadoDTO> getPagosPedido(Long id) {
        return pagoRepository.getPagosPedido(id);
                
    }
}
