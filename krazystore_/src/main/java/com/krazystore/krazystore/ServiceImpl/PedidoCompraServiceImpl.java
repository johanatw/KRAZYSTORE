/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import Utils.PedidoCompraEvent;
import Utils.ProductosPedidoRecepcionadosEvent;
import com.krazystore.krazystore.DTO.DetallePedidoCompraDTO;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    
    public PedidoCompraServiceImpl(PedidoCompraRepository pedidoCompraRepository) {
        this.pedidoCompraRepository = pedidoCompraRepository;
    }
    
    @Autowired
    private DetallePedidoCompraService detallePedidoService;
    
    @Autowired
    private RecepcionService recepcionService;
    
    @Autowired
    private DetallePedidoCompraMapper detallePedidoMapper;

    @Override
    public List<PedidoCompraEntity> findAll() {
        return pedidoCompraRepository.findAll();
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
        
        pedidoCompraRepository.save(updatedPedido);
        
        List<DetallePedidoCompra> detalle = pedidoDTO.getDetalle()
                .stream()
                .map(detallePedidoMapper)
                .collect(Collectors.toList());
        

        detalle.forEach(det -> det.setPedidoCompra(updatedPedido) );
        
        detallePedidoService.updateDetalle(detalle, id);
        
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
    
    public Character getEstadoRecepcionPedido(Long id) {
        long cantProductosRecepcionados;
        long cantProductosPedidos;
        cantProductosPedidos = detallePedidoService.getCantProductosPedidos(id);
        cantProductosRecepcionados = detallePedidoService.getCantProductosRecepcionados(id);
      
        if (cantProductosRecepcionados == 0){
            return null;
        }else if(cantProductosRecepcionados == cantProductosPedidos){
            return Estado.RECEPCINADO.getCodigo();
        }else{
            return Estado.PARCIALMENTERECEPCINADO.getCodigo();
        }
      
    }
    
    @EventListener
    public void updateEstadoPedido(PedidoCompraEvent evento) {
        PedidoCompraEntity pedido = pedidoCompraRepository.findById(evento.getPedidoId())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        boolean hasAnticipos = hasAnticipos(pedido.getId());
        Character estadoRecepcion = getEstadoRecepcionPedido(pedido.getId());

        if(!hasAnticipos && estadoRecepcion == null){
            System.out.println("IF");
            
            pedido.setEstadoPedido(Estado.NUEVO.getCodigo());
        }else if(hasAnticipos && estadoRecepcion == null){
            System.out.println("ELSE1");
            pedido.setEstadoPedido(Estado.CONANTICIPO.getCodigo());
        }else if(estadoRecepcion != null){
            System.out.println("ELSE2");
            pedido.setEstadoPedido(estadoRecepcion);
        }
        /*switch (evento.getTipoEvento()) {
            case ANTICIPO_CREADO:
                    pedido.setEstadoPedido(Estado.CONANTICIPO.getCodigo());

                break;

            case ANTICIPO_ELIMINADO:
                if (getEstadoRecepcionPedido(pedido.getId()) == null && !hasAnticipos(pedido.getId())) {
                    pedido.setEstadoPedido(Estado.NUEVO.getCodigo());
                }
                
                break;
            case PEDIDO_RECEPCIONADO:
                    pedido.setEstadoPedido(getEstadoRecepcionPedido(pedido.getId()));
                break;
            case RECEPCION_ELIMINADO:
                Character estadoRecepcion = getEstadoRecepcionPedido(pedido.getId());
                if ( estadoRecepcion  == null && !hasAnticipos(pedido.getId())) {
                    pedido.setEstadoPedido(Estado.NUEVO.getCodigo());
                }
                    pedido.setEstadoPedido(getEstadoRecepcionPedido(pedido.getId()));
                break;

            default:
                throw new IllegalArgumentException("Evento no reconocido: " + evento.getTipoEvento());
        }*/
        
        pedidoCompraRepository.save(pedido);
        
    }
    
    /*@EventListener
    public void handleRecepcion(ProductosPedidoRecepcionadosEvent event)throws Exception {
        Long pedidoId = event.getPedidocompraId();
        
        actualizarCantidadesRecepcionadas(pedidoId);
    }*/
    

    private boolean hasAnticipos(Long id) {
        return !pedidoCompraRepository.getAnticipos(id).isEmpty();
    }
}
