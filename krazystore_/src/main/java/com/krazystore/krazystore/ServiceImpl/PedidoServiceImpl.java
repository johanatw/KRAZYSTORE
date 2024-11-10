/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.Pedido;
import com.krazystore.krazystore.DTO.PedidoDTO;
import com.krazystore.krazystore.DTO.PersonaCreationDTO;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.EstadoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Entity.PersonaEntity;
import com.krazystore.krazystore.Repository.PedidoRepository;
import com.krazystore.krazystore.Service.AnticipoService;
import com.krazystore.krazystore.Service.DetallePedidoService;
import com.krazystore.krazystore.Service.MovimientoService;
import com.krazystore.krazystore.Service.PagoService;
import com.krazystore.krazystore.Service.PedidoService;
import com.krazystore.krazystore.Service.PersonaService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
    private PersonaService personaService;
    
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

    @Transactional
    @Override
    public PedidoEntity savePedido(PedidoEntity pedidoEntity, List<DetallePedidoEntity> detalle) {
        EstadoEntity estadoPago = new EstadoEntity((long)1,"Pago pendiente","P");
        EstadoEntity estadoPedido = new EstadoEntity((long)3,"Nuevo","E");
        PersonaCreationDTO personaDTO = new PersonaCreationDTO();
        personaDTO.setPersonaEntity(pedidoEntity.getCliente());
        personaDTO.setDireccion(pedidoEntity.getDireccionEnvio());
        
        if(pedidoEntity.getDireccionEnvio()!= null && pedidoEntity.getDireccionEnvio().getId() == null ){
            personaService.updatePersona(personaDTO, pedidoEntity.getCliente().getId());
        }
        
        pedidoEntity.setFecha(new Date());
        pedidoEntity.setEstadoPago(estadoPago);
        pedidoEntity.setEstadoPedido(estadoPedido);
        
        PedidoEntity nuevoPedido = pedidorepository.save(pedidoEntity);
        
        detallePedidoService.saveDetallePedido(nuevoPedido.getId(), detalle);
        
        return nuevoPedido;
    }

    @Transactional
    @Override
    public PedidoEntity updatePedido(PedidoEntity pedidoEntity, List<DetallePedidoEntity> detalle, Long id) throws Exception {
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
        updatedPedido.setDireccionEnvio(pedidoEntity.getDireccionEnvio());
        updatedPedido.setEstadoPago(pedidoEntity.getEstadoPago());
        
        PersonaCreationDTO personaDTO = new PersonaCreationDTO();
        personaDTO.setPersonaEntity(pedidoEntity.getCliente());
        personaDTO.setDireccion(pedidoEntity.getDireccionEnvio());
        
        if(pedidoEntity.getDireccionEnvio()!= null && pedidoEntity.getDireccionEnvio().getId() == null ){
            personaService.updatePersona(personaDTO, pedidoEntity.getCliente().getId());
        }

        PedidoEntity pedido = pedidorepository.save(updatedPedido);
        detallePedidoService.updateDetallesPedido(detalle, id);
        
        return pedido;
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
