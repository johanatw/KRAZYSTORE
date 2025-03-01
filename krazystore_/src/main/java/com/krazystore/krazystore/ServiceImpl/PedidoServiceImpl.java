/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import Utils.PedidoEvent;
import Utils.ProductosReservadosEvent;
import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.Pedido;
import com.krazystore.krazystore.DTO.PedidoCreationDTO;
import com.krazystore.krazystore.DTO.PedidoDTO;
import com.krazystore.krazystore.DTO.PersonaCreationDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Entity.PersonaEntity;
import com.krazystore.krazystore.Repository.PedidoRepository;
import com.krazystore.krazystore.Service.AnticipoService;
import com.krazystore.krazystore.Service.DetallePedidoService;
import com.krazystore.krazystore.Service.PedidoService;
import com.krazystore.krazystore.Service.PersonaService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidorepository;
    private final ApplicationEventPublisher eventPublisher;

    public PedidoServiceImpl(PedidoRepository pedidorepository, ApplicationEventPublisher eventPublisher) {
        this.pedidorepository = pedidorepository;
        this.eventPublisher = eventPublisher;
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
    public PedidoCreationDTO findById(Long id) {
        PedidoCreationDTO pedidoDTO = new PedidoCreationDTO();
        PedidoEntity pedido = pedidorepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no existe"));
        List<DetallePedidoEntity> detalle = detallePedidoService.findByPedido(id);
        pedidoDTO.setPedido(pedido);
        pedidoDTO.setDetalle(detalle);
        return pedidoDTO;
    }

    @Transactional
    @Override
    public PedidoEntity savePedido(PedidoEntity pedidoEntity, List<DetallePedidoEntity> detalle) {
        char estadoPedido = Estado.NUEVO.getCodigo();
        PersonaCreationDTO personaDTO = new PersonaCreationDTO();
        personaDTO.setPersonaEntity(pedidoEntity.getCliente());
        personaDTO.setDireccion(pedidoEntity.getDireccionEnvio());
        
        if(pedidoEntity.getDireccionEnvio()!= null && pedidoEntity.getDireccionEnvio().getId() == null ){
            personaService.updatePersona(personaDTO, pedidoEntity.getCliente().getId());
        }
        
        pedidoEntity.setEstadoPedido(estadoPedido);
        
        PedidoEntity nuevoPedido = pedidorepository.save(pedidoEntity);
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = detallePedidoService.saveDetallePedido(nuevoPedido.getId(), detalle);
        actualizarExistencias(productosActualizarExistencias);
        
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
        
        PersonaCreationDTO personaDTO = new PersonaCreationDTO();
        personaDTO.setPersonaEntity(pedidoEntity.getCliente());
        personaDTO.setDireccion(pedidoEntity.getDireccionEnvio());
        
        if(pedidoEntity.getDireccionEnvio()!= null && pedidoEntity.getDireccionEnvio().getId() == null ){
            personaService.updatePersona(personaDTO, pedidoEntity.getCliente().getId());
        }

        PedidoEntity pedido = pedidorepository.save(updatedPedido);
        List<ProductoExistenciasDTO> productosActualizarExistencias = detallePedidoService.updateDetallesPedido(detalle, id);
        
        actualizarExistencias(productosActualizarExistencias);
        return pedido;
    }
    
    
   @EventListener
    public void updateEstadoPedido(PedidoEvent evento) {
        PedidoEntity pedido = pedidorepository.findById(evento.getPedidoId())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        System.out.println("UPDATEESTADOPEDIDO");
        switch (evento.getTipoEvento()) {
            case ANTICIPO_CREADO:
                
                    pedido.setEstadoPedido(Estado.CONANTICIPO.getCodigo());
                
                break;

            case ANTICIPO_ELIMINADO:
                if (!hasFacturacion(pedido.getId()) && !hasAnticipos(pedido.getId())) {
                    pedido.setEstadoPedido(Estado.NUEVO.getCodigo());
                }
                
                break;

            default:
                throw new IllegalArgumentException("Evento no reconocido: " + evento.getTipoEvento());
        }
        
        pedidorepository.save(pedido);
        
    }

    @Override
    public void deletePedido(Long id) {       
        if(puedeEliminarsePedido(id)){
            detallePedidoService.deleteByPedido(id);
            pedidorepository.deleteById(id);
        }
    }
    
    public void actualizarExistencias(List<ProductoExistenciasDTO> productosActualizarExistencias) {
        // Publicar el evento
        ProductosReservadosEvent evento = new ProductosReservadosEvent(productosActualizarExistencias, TipoEvento.ACTUALIZAR_RESERVAS);
        eventPublisher.publishEvent(evento);
    }

    public boolean puedeEliminarsePedido(Long id){
        PedidoEntity pedido = pedidorepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no existe"));
                
        return pedido.getEstadoPedido() == Estado.NUEVO.getCodigo();
    }
    
    private boolean hasFacturacion(Long id) {
        return !pedidorepository.getFacturas(id).isEmpty();
    }

    private boolean hasAnticipos(Long id) {
        return !pedidorepository.getAnticipos(id).isEmpty();
    }
    
}
