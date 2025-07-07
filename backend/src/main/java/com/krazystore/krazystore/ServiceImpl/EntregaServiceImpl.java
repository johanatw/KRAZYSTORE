/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import com.krazystore.krazystore.Events.FacturaEvent;
import com.krazystore.krazystore.Events.PedidoEvent;
import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.DetalleEntregaDTO;
import com.krazystore.krazystore.DTO.EntregaCreationDTO;
import com.krazystore.krazystore.Entity.DetalleEntrega;
import com.krazystore.krazystore.Entity.EntregaEntity;
import com.krazystore.krazystore.Mapper.DetalleEntregaMapper;
import com.krazystore.krazystore.Repository.EntregaRepository;
import com.krazystore.krazystore.Service.DetalleEntregaService;
import com.krazystore.krazystore.Service.EntregaService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class EntregaServiceImpl implements EntregaService {

    public EntregaServiceImpl(EntregaRepository entregaRepository, org.springframework.context.ApplicationEventPublisher eventPublisher) {
        this.entregaRepository = entregaRepository;
        this.eventPublisher = eventPublisher;
    }
    private final EntregaRepository entregaRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private DetalleEntregaMapper detalleEntregaMapper;
    
    @Autowired
    private DetalleEntregaService detalleService;

    @Override
    public List<EntregaEntity> findAll() {
        return entregaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public EntregaCreationDTO findById(Long id) {
        EntregaEntity entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
        
        List<DetalleEntregaDTO> detalle = detalleService.findByIdEntrega(id);
        
        EntregaCreationDTO entregaDTO = new EntregaCreationDTO();
        entregaDTO.setEntrega(entrega);
        entregaDTO.setDetalle(detalle);
        return entregaDTO;
    }

    @Transactional
    @Override
    public EntregaEntity saveEntrega(EntregaCreationDTO entregaDTO) {
        
        entregaDTO.getEntrega().setEstado(Estado.PENDIENTE.getCodigo());
        
        entregaRepository.save(entregaDTO.getEntrega());
        
        
        List<DetalleEntrega> detalle = entregaDTO.getDetalle()
                .stream()
                .map(detalleEntregaMapper)
                .collect(Collectors.toList());
        
        detalle.forEach(det -> System.out.println(det.getDetalleVenta().getIdDetVent()));
        
        detalle.forEach(det -> det.setEntrega(entregaDTO.getEntrega()));
        
        detalleService.saveDetalle(detalle);
        
        
        actualizarEstadoPedido(entregaDTO.getEntrega().getVenta().getPedido().getId(),TipoEvento.ESTADO_PEDIDO);
        
        return entregaDTO.getEntrega();
    }

    @Transactional
    @Override
    public EntregaEntity updateEntrega(EntregaCreationDTO entrega, Long id) {
        EntregaEntity updatedEntrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
        
        updatedEntrega.setDireccionEnvio(entrega.getEntrega().getDireccionEnvio());
        updatedEntrega.setEmpresaTransporte(entrega.getEntrega().getEmpresaTransporte());
        updatedEntrega.setFecha(entrega.getEntrega().getFecha());
        updatedEntrega.setModoEntrega(entrega.getEntrega().getModoEntrega());
        updatedEntrega.setPuntoEntrega(entrega.getEntrega().getPuntoEntrega());
        updatedEntrega.setObservaciones(entrega.getEntrega().getObservaciones());
        
        return entregaRepository.save(updatedEntrega);
    }
    
    @Transactional
    @Override
    public EntregaEntity reprogramarEntrega(EntregaCreationDTO entrega, Long id) {
        EntregaEntity updatedEntrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
        
        updatedEntrega.setDireccionEnvio(entrega.getEntrega().getDireccionEnvio());
        updatedEntrega.setEmpresaTransporte(entrega.getEntrega().getEmpresaTransporte());
        updatedEntrega.setFecha(entrega.getEntrega().getFecha());
        updatedEntrega.setModoEntrega(entrega.getEntrega().getModoEntrega());
        updatedEntrega.setPuntoEntrega(entrega.getEntrega().getPuntoEntrega());
        updatedEntrega.setEstado(Estado.PENDIENTE.getCodigo());
        actualizarEstadoPedido(updatedEntrega.getVenta().getPedido().getId(),TipoEvento.ESTADO_PEDIDO);
        return entregaRepository.save(updatedEntrega);
    }

    @Transactional
    @Override
    public void deleteEntrega(Long id) {
        EntregaEntity deletedEntrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
        detalleService.deleteByIdEntrega(id);
         
        Long idPedido = deletedEntrega.getVenta()!=null?deletedEntrega.getVenta().getPedido().getId():null;
        entregaRepository.deleteById(id);
        
        if(idPedido!=null){
            actualizarEstadoPedido(idPedido,TipoEvento.ESTADO_PEDIDO);
        }
        
        
    }

    @Transactional
    @Override
    public EntregaEntity marcarComoEntregado(Long id) {
        EntregaEntity updatedEntrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
        updatedEntrega.setEstado(Estado.ENTREGADO.getCodigo());
        actualizarEstadoPedido(updatedEntrega.getVenta().getPedido().getId(),TipoEvento.ESTADO_PEDIDO);
        return entregaRepository.save(updatedEntrega);
    }

    @Transactional
    @Override
    public EntregaEntity marcarComoNoEntregado(Long id) {
        EntregaEntity updatedEntrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
        updatedEntrega.setEstado(Estado.NO_ENTREGADO.getCodigo());
        actualizarEstadoPedido(updatedEntrega.getVenta().getPedido().getId(),TipoEvento.ESTADO_PEDIDO);
        return entregaRepository.save(updatedEntrega);
    }
    
    public void actualizarEstadoPedido(Long idPedido, TipoEvento tipoEvento) {
        // Publicar el evento
        System.out.println("ACTUALIZARESTADO");
        System.out.println(idPedido);
        PedidoEvent evento = new PedidoEvent(idPedido, tipoEvento);
        eventPublisher.publishEvent(evento);
    }
    
    @EventListener
    public void handleFacturaEvent(FacturaEvent event) {
        if(event.getTipoEvento() == TipoEvento.FACTURA_ANULADA && event.getIdPedido() != null ) {
            entregaRepository.desasociarVentaEntrega(event.getNuevaVenta().getId(),Estado.CANCELADO.getCodigo());
            actualizarEstadoPedido(event.getIdPedido(),TipoEvento.ESTADO_PEDIDO);
            
        }
        
    }
}
