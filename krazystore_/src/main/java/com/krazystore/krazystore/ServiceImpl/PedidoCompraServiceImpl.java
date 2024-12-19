/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.ProductosPedidoRecepcionadosEvent;
import com.krazystore.krazystore.DTO.PedidoCompraCreationDTO;
import com.krazystore.krazystore.Entity.DetallePedidoCompra;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.PedidoCompraEntity;
import com.krazystore.krazystore.Repository.PedidoCompraRepository;
import com.krazystore.krazystore.Service.DetallePedidoCompraService;
import com.krazystore.krazystore.Service.PedidoCompraService;
import com.krazystore.krazystore.Service.RecepcionService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.List;
import java.util.Optional;
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

    @Override
    public List<PedidoCompraEntity> findAll() {
        return pedidoCompraRepository.findAll();
    }

    @Override
    public Optional<PedidoCompraEntity> findById(Long id) {
        return pedidoCompraRepository.findById(id);
    }

    @Transactional
    @Override
    public PedidoCompraEntity savePedido(PedidoCompraCreationDTO pedido)throws Exception {
        if(pedido.getPedido().getProveedor()== null){
            throw new BadRequestException("Falta proveedor " );
        }
        
        PedidoCompraEntity nuevoPedido;
        nuevoPedido = pedidoCompraRepository.save(pedido.getPedido());
        
        detallePedidoService.saveDetalle(pedido.getDetalle(), nuevoPedido.getId());
        
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
        detallePedidoService.updateDetalle(pedidoDTO.getDetalle(), id);
        
        return updatedPedido;
        
    }

    @Transactional
    @Override
    public void deletePedido(Long id) {
        detallePedidoService.deleteDetalle(id);
        pedidoCompraRepository.deleteById(id);
    }
    
    @Transactional
    @Override
    public void actualizarCantidadesRecepcionadas(Long idPedido) throws Exception{
        PedidoCompraEntity pedido = pedidoCompraRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));
        
        List<DetallePedidoCompra> detalle = detallePedidoService.findByIdPedido(idPedido);
        
        detalle.forEach(d -> {
            Integer totalRecepcionado = recepcionService.getTotalRecepcionadoPorProducto(idPedido, d.getProducto().getId());
            d.setCantRecepcionada(totalRecepcionado);
        });
        
        
        List<DetallePedidoCompra> updatedDetalle = detallePedidoService.updateDetalle(detalle, idPedido);
        
        pedido.setEstado(getEstadoRecepcionPedido(updatedDetalle));
        
        pedidoCompraRepository.save(pedido);
              
    }
    
    @Transactional
    @Override
    public void modificarCantidadesRecepcionadas(List<DetalleRecepcion> nuevoDetalleRecepcion,List<DetalleRecepcion> anteriorDetalleRecepcion) {
        Long idPedido = nuevoDetalleRecepcion.get(0).getDetallePedido().getPedidoCompra().getId();
        System.out.println("nuevoDetalleRecepcion");
        nuevoDetalleRecepcion.forEach(d -> {
            System.out.println("cantRecepcionada");
            System.out.println(d.getCantRecepcionada());
        });
        
        System.out.println("anteriorDetalleRecepcion");
        anteriorDetalleRecepcion.forEach(d -> {
            System.out.println("cantRecepcionada");
            System.out.println(d.getCantRecepcionada());
        });
        List<DetallePedidoCompra> updatedDetalle = detallePedidoService.modificarCantidadesRecepcionadas(nuevoDetalleRecepcion, anteriorDetalleRecepcion, idPedido);
        
        PedidoCompraEntity pedido = pedidoCompraRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));
        

        pedido.setEstado(getEstadoRecepcionPedido(updatedDetalle));
        
        pedidoCompraRepository.save(pedido);
      
    }
    
    public char getEstadoRecepcionPedido(List<DetallePedidoCompra> detalle) {
        int cantDetalleTotalRecepcionado = 0;
        
        for (int i = 0; i < detalle.size(); i++) {
            if(detalle.get(i).getCantidad() == detalle.get(i).getCantRecepcionada()){
                cantDetalleTotalRecepcionado++;
            }
          }
      
        if(cantDetalleTotalRecepcionado == detalle.size()){
            return 'R';
        }else{
            return 'P';
        }
      
    }
    
    @EventListener
    public void handleRecepcion(ProductosPedidoRecepcionadosEvent event)throws Exception {
        Long pedidoId = event.getPedidocompraId();
        
        actualizarCantidadesRecepcionadas(pedidoId);
    }
}
