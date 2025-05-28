/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import Utils.EstadoPedido;
import Utils.PedidoCompraEvent;
import Utils.ProductosPedidoRecepcionadosEvent;
import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.DetallePedidoCompraDTO;
import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.DTO.PedidoCompraCreationDTO;
import com.krazystore.krazystore.Entity.DetallePedidoCompra;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.PedidoCompraEntity;
import com.krazystore.krazystore.Mapper.DetallePedidoCompraMapper;
import com.krazystore.krazystore.Repository.PedidoCompraRepository;
import com.krazystore.krazystore.Service.DetallePedidoCompraService;
import com.krazystore.krazystore.Service.PedidoCompraService;
import com.krazystore.krazystore.Service.RecepcionService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class PedidoCompraServiceImpl implements PedidoCompraService {
    private final PedidoCompraRepository pedidoCompraRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    
    public PedidoCompraServiceImpl(PedidoCompraRepository pedidoCompraRepository, ApplicationEventPublisher eventPublisher) {
        this.pedidoCompraRepository = pedidoCompraRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Autowired
    private DetallePedidoCompraService detallePedidoService;
    
    @Autowired
    private RecepcionService recepcionService;
    
    @Autowired
    private DetallePedidoCompraMapper detallePedidoMapper;

    @Override
    public List<PedidoCompraEntity> findAll() {
        return pedidoCompraRepository.findAllByOrderByIdDesc();
    }

    @Override
    public PedidoCompraCreationDTO findById(Long id) {
       
        PedidoCompraCreationDTO pedidoDTO = new PedidoCompraCreationDTO();
        Optional<PedidoCompraEntity> pedido = pedidoCompraRepository.findById(id);
        if (pedido.isPresent()) {
            List<DetallePedidoCompraDTO> detalles = detallePedidoService.findDTOByIdPedido(id);
            pedidoDTO.setDetalle(detalles);
            pedidoDTO.setPedido(pedido.get());
        }
        return pedidoDTO;
    }

    @Transactional
    @Override
    public PedidoCompraEntity savePedido(PedidoCompraCreationDTO pedido)throws Exception {
        if(pedido.getPedido().getProveedor()== null){
            throw new BadRequestException("Falta proveedor " );
        }
        
        pedido.getPedido().setEstadoFacturacion(Estado.PENDIENTE_DE_FACTURA.getCodigo());
        pedido.getPedido().setEstadoPedido(Estado.NUEVO.getCodigo());
        
        PedidoCompraEntity nuevoPedido;
        nuevoPedido = pedidoCompraRepository.save(pedido.getPedido());
        
        List<DetallePedidoCompra> detalle = pedido.getDetalle()
                .stream()
                .map(detallePedidoMapper)
                .collect(Collectors.toList());
        

        detalle.forEach(det -> det.setPedidoCompra(nuevoPedido) );
        
        detallePedidoService.saveDetalle(detalle, nuevoPedido.getId());
        
        return nuevoPedido;
    }

    @Transactional
    @Override
    public PedidoCompraEntity updatePedido(PedidoCompraCreationDTO pedidoDTO, Long id) throws Exception {
        PedidoCompraEntity pedido = pedidoDTO.getPedido();
        PedidoCompraEntity updatedPedido = pedidoCompraRepository.findById(id).get();
        
        updatedPedido.setFecha(pedido.getFecha());
        updatedPedido.setProveedor(pedido.getProveedor());
        updatedPedido.setTotal(pedido.getTotal());
        updatedPedido.setObservaciones(pedido.getObservaciones());
        updatedPedido.setTipoPedido(pedido.getTipoPedido());
        
        pedidoCompraRepository.save(updatedPedido);
        
        List<DetallePedidoCompra> detalle = pedidoDTO.getDetalle()
                .stream()
                .map(detallePedidoMapper)
                .collect(Collectors.toList());
        

        detalle.forEach(det -> det.setPedidoCompra(updatedPedido) );
        
        detallePedidoService.updateDetalle(detalle, id);
        
        this.updateEstadoFacturacionPedido(updatedPedido);
        this.updateEstadoPedido(updatedPedido);
        
        return updatedPedido;
        
    }

    @Transactional
    @Override
    public void deletePedido(Long id) {
        detallePedidoService.deleteDetalle(id);
        pedidoCompraRepository.deleteById(id);
    }
    
    /*@Transactional
    @Override
    public void actualizarCantidadesRecepcionadas(Long idPedido) throws Exception{
        PedidoCompraEntity pedido = pedidoCompraRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));
        
        List<DetallePedidoCompra> detalle = detallePedidoService.findByIdPedido(idPedido);
        
        detalle.forEach(d -> {
            Integer totalRecepcionado = recepcionService.getTotalRecepcionadoPorProducto(idPedido, d.getProducto().getId());
            d.setCantRecepcionada(totalRecepcionado);
            System.out.println("DETALLECANRECEPCIONADA");
            System.out.println(d.getCantRecepcionada());
        });
        
        
        List<DetallePedidoCompra> updatedDetalle = detallePedidoService.updateDetalle(detalle, idPedido);
        
        pedido.setEstadoPedido(getEstadoRecepcionPedido(idPedido));
        
        pedidoCompraRepository.save(pedido);
              
    }
    
    @Transactional
    @Override
    public void modificarCantidadesRecepcionadas(List<DetalleRecepcion> nuevoDetalleRecepcion,List<DetalleRecepcion> anteriorDetalleRecepcion) {
        Long idPedido = nuevoDetalleRecepcion.get(0).getDetallePedido().getPedidoCompra().getId();

        List<DetallePedidoCompra> updatedDetalle = detallePedidoService.modificarCantidadesRecepcionadas(nuevoDetalleRecepcion, anteriorDetalleRecepcion, idPedido);
        
        PedidoCompraEntity pedido = pedidoCompraRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));
        

        pedido.setEstadoPedido(getEstadoRecepcionPedido(idPedido));
        
        pedidoCompraRepository.save(pedido);
      
    }*/
    
    public void updateEstadoPedido(PedidoCompraEntity pedido) {
        Long pedidoId = pedido.getId();
        
        Character nuevoEstado = pedido.getEstadoPedido();
        
        Long totalProductosPedidos = pedidoCompraRepository.totalProductosPedido(pedidoId);
        Long totalRecepcionado = pedidoCompraRepository.totalProductosRecepcionados(pedidoId);
        
        boolean hasProductosRecepcionados = totalRecepcionado>0;
        boolean isTotalRecepcionado = totalRecepcionado == totalProductosPedidos;

        if(hasProductosRecepcionados){
            nuevoEstado = isTotalRecepcionado?Estado.RECEPCIONADO.getCodigo():Estado.PARCIALMENTE_RECEPCIONADO.getCodigo();
        }else{
            nuevoEstado = Estado.NUEVO.getCodigo();
        }

        if (!Objects.equals(nuevoEstado, pedido.getEstadoPedido())) { 
            pedido.setEstadoPedido(nuevoEstado);
            pedidoCompraRepository.save(pedido);
        }
        
    }
    
    public void updateEstadoPedidos(List<Long> idsPedidos) {
        List<PedidoCompraEntity> pedidosActualizarEstados = new ArrayList<>();
        
        for (Long id : idsPedidos) {
            PedidoCompraEntity pedido = obtenerPedido(id);
            
            Character nuevoEstado = pedido.getEstadoPedido();
        
            Long totalProductosPedidos = pedidoCompraRepository.totalProductosPedido(id);
            Long totalRecepcionado = pedidoCompraRepository.totalProductosRecepcionados(id);

            boolean hasProductosRecepcionados = totalRecepcionado>0;
            boolean isTotalRecepcionado = totalRecepcionado == totalProductosPedidos;

            if(hasProductosRecepcionados){
                nuevoEstado = isTotalRecepcionado?Estado.RECEPCIONADO.getCodigo():Estado.PARCIALMENTE_RECEPCIONADO.getCodigo();
            }else{
                nuevoEstado = Estado.NUEVO.getCodigo();
            }

            if (!Objects.equals(nuevoEstado, pedido.getEstadoPedido())) { 
                pedido.setEstadoPedido(nuevoEstado);
                pedidosActualizarEstados.add(pedido);
            }
            
        }
        
        if(!pedidosActualizarEstados.isEmpty()){
            pedidoCompraRepository.saveAll(pedidosActualizarEstados);
        }
        
        
        
        
    }

    public void updateEstadoFacturacionPedido(PedidoCompraEntity pedido) {
    
        Long pedidoId = pedido.getId();
        String tipoPedido = pedido.getProveedor().getTipo().getDescripcion();
        boolean hasFacturas = hasFacturas(pedido.getId());
        boolean esTotalmenteFacturado = pedidoCompraRepository.esPedidoTotalmenteFacturado(pedido.getId());
        
        Character nuevoEstado = pedido.getEstadoFacturacion();
        Character nuevoEstadoPedido = pedido.getEstadoPedido();
        
        if (hasFacturas) {
            nuevoEstado = esTotalmenteFacturado ? Estado.FACTURADO.getCodigo() : Estado.PARCIALMENTE_FACTURADO.getCodigo();
        }else{
            nuevoEstado = Estado.PENDIENTE_DE_FACTURA.getCodigo();
        }
        
        if(tipoPedido != null && "Nacional".equals(tipoPedido) ){
            Long totalProductosPedidos = pedidoCompraRepository.totalProductosPedido(pedidoId);
            Long totalProductosFacturados = pedidoCompraRepository.totalProductosFacturados(pedidoId);
        
            boolean isTotalProductosRecepcionado = Objects.equals(totalProductosFacturados, totalProductosPedidos);
            if(hasFacturas){
                nuevoEstadoPedido = isTotalProductosRecepcionado ? Estado.RECEPCIONADO.getCodigo() : Estado.PARCIALMENTE_RECEPCIONADO.getCodigo();
            }else{
                nuevoEstadoPedido = Estado.NUEVO.getCodigo();
            }
            
        }

        if (!Objects.equals(nuevoEstado, pedido.getEstadoPedido())) { 
            pedido.setEstadoFacturacion(nuevoEstado);
            pedido.setEstadoPedido(nuevoEstadoPedido);
            pedidoCompraRepository.save(pedido);
        }
        
    }
    
    
    @EventListener
    public void handlePedidoEvents(PedidoCompraEvent evento)  {
    if (evento == null || evento.getTipoEvento() == null) {
        throw new IllegalArgumentException("Evento o tipo de evento no puede ser nulo");
    }

    List<Long> idsPedidos = evento.getIdsPedidos();
    boolean actualizarVariosPedidos = idsPedidos != null && !idsPedidos.isEmpty();
    TipoEvento tipoEvento = evento.getTipoEvento();

    switch (tipoEvento) {
        case ESTADO_PEDIDO:
            if (actualizarVariosPedidos) {
                updateEstadoPedidos(idsPedidos);
            } else {
                PedidoCompraEntity pedido = obtenerPedido(evento.getPedidoId());
                updateEstadoPedido(pedido);
            }
            break;

        case ESTADO_FACTURACION:
            if (actualizarVariosPedidos) {
                
            } else {
                PedidoCompraEntity pedido = obtenerPedido(evento.getPedidoId());
                updateEstadoFacturacionPedido(pedido);
            }
            break;

        default:
            throw new IllegalArgumentException("Evento no reconocido: " + tipoEvento);
    }
}

        private PedidoCompraEntity obtenerPedido(Long idPedido) {
            if (idPedido == null) {
                throw new IllegalArgumentException("ID de pedido no puede ser nulo");
            }
            return pedidoCompraRepository.findById(idPedido)
                    .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + idPedido));
        }
    
    public Character getEstadoRecepcionPedido(Long id) {
        long cantProductosRecepcionados;
        long cantProductosPedidos;
        cantProductosPedidos = detallePedidoService.getCantProductosPedidos(id);
        cantProductosRecepcionados = detallePedidoService.getCantProductosRecepcionados(id);
      
        if (cantProductosRecepcionados == 0){
            return null;
        }else if(cantProductosRecepcionados == cantProductosPedidos){
            return Estado.RECEPCIONADO.getCodigo();
        }else{
            return Estado.PARCIALMENTE_RECEPCIONADO.getCodigo();
        }
      
    }
    
    public void actualizarEstadoPedido(Long idPedido, TipoEvento tipoEvento) {
        
        // Publicar el evento
        // Publicar el evento
        PedidoCompraEvent evento = new PedidoCompraEvent(idPedido, tipoEvento);
        eventPublisher.publishEvent(evento);
    }
    
    
    /*@EventListener
    public void handleRecepcion(ProductosPedidoRecepcionadosEvent event)throws Exception {
        Long pedidoId = event.getPedidocompraId();
        
        actualizarCantidadesRecepcionadas(pedidoId);
    }*/
    
    private boolean hasFacturas(Long id) {
        return !pedidoCompraRepository.getFacturas(id).isEmpty();
    }

    @Override
    public PedidoCompraCreationDTO findDetalleFacturarByIdsRecepciones(Long idPedido, List<Long> ids) {
        PedidoCompraEntity pedido = pedidoCompraRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
        
         List<DetallePedidoCompraDTO> detalle = detallePedidoService.findDetalleFacturarByIdsRecepciones(ids);
         
         PedidoCompraCreationDTO pedidoDTO = new PedidoCompraCreationDTO(pedido, detalle);
        return pedidoDTO;
    }


   
}
