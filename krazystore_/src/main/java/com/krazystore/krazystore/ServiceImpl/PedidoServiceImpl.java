/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.Pedido;
import com.krazystore.krazystore.DTO.PedidoDTO;
import com.krazystore.krazystore.Entity.EstadoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Repository.PedidoRepository;
import com.krazystore.krazystore.Service.AnticipoService;
import com.krazystore.krazystore.Service.DetallePedidoService;
import com.krazystore.krazystore.Service.MovimientoService;
import com.krazystore.krazystore.Service.PedidoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author HP
 */
@Service
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidorepository;

    public PedidoServiceImpl(PedidoRepository pedidorepository) {
        this.pedidorepository = pedidorepository;
    }
    
    
    
    @Autowired
    private DetallePedidoService detallePedidoService;
    


    @Override
    public List<PedidoDTO> findAll() {
        return pedidorepository.findAllPedidos();
    }

    @Override
    public Optional<PedidoEntity> findById(Long id) {
        return pedidorepository.findById(id);
    }

    @Override
    public PedidoEntity savePedido(PedidoEntity pedidoEntity) {
        EstadoEntity estadoPago = new EstadoEntity((long)1,"Pago pendiente","P");
        EstadoEntity estadoPedido = new EstadoEntity((long)3,"Nuevo","E");
        
        pedidoEntity.setFecha(new Date());
        pedidoEntity.setEstadoPago(estadoPago);
        pedidoEntity.setEstadoPedido(estadoPedido);
        
        return pedidorepository.save(pedidoEntity);
    }

    @Override
    public PedidoEntity updatePedido(PedidoEntity pedidoEntity, Long id) {
        PedidoEntity updatedPedido = pedidorepository.findById(id).get();
        
        updatedPedido.setCliente(pedidoEntity.getCliente());
        updatedPedido.setFecha(pedidoEntity.getFecha());
        updatedPedido.setMontoIva(pedidoEntity.getMontoIva());
        updatedPedido.setCostoEnvio(pedidoEntity.getCostoEnvio());
        //updatedPedido.setEstadoPago(pedidoEntity.getEstadoPago());
        //updatedPedido.setEstadoPedido(pedidoEntity.getEstadoPedido());
        updatedPedido.setTotal(pedidoEntity.getTotal());
        updatedPedido.setModoEntrega(pedidoEntity.getModoEntrega());
        updatedPedido.setFormaPago(pedidoEntity.getFormaPago());
        System.out.println("updatePedido");
        System.out.println(pedidoEntity.getPagado());
        updatedPedido.setPagado(pedidoEntity.getPagado());
        EstadoEntity estadoPago = new EstadoEntity();
        
        if(updatedPedido.getPagado() == 0){
            estadoPago.setId((long)1);
        }else if(updatedPedido.getPagado() == updatedPedido.getTotal() || updatedPedido.getPagado() > updatedPedido.getTotal()){
            estadoPago.setId((long)2);
        }else if(updatedPedido.getPagado() < updatedPedido.getTotal() && updatedPedido.getPagado() > 0){
            estadoPago.setId((long)7);
        }
        
        updatedPedido.setEstadoPago(estadoPago);

        return pedidorepository.save(updatedPedido);
    }
   @Override  
    public void updateEstadoPagoPedido(PedidoEntity pedido, EstadoEntity estadoPago) {
        PedidoEntity updatedPedido = pedidorepository.findById(pedido.getId()).get();
        
        
        updatedPedido.setEstadoPago(estadoPago);
        
        pedidorepository.save(updatedPedido);
        
    }

    @Override
    public int deletePedido(Long id) {
        int estadoPedido = verificarPedidoEstado(id);
        if(estadoPedido > 0){
            return 1;
        }else{
            detallePedidoService.deleteByPedido(id);
        }
        pedidorepository.deleteById(id);
        return 0;
    }

    @Override
    public int verificarPedidoEstado(Long id) {
        PedidoEntity p = new PedidoEntity();
        p.setId(id);
        boolean tieneAnticipos = pedidorepository.existenAnticiposAsociados(id);

        if(tieneAnticipos){
            return 1;
        }
        
        return 0;
    }
    
}
