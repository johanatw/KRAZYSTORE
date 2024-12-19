/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.Entity.DetallePedidoCompra;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.PedidoCompraEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Repository.DetallePedidoCompraRepository;
import com.krazystore.krazystore.Service.DetallePedidoCompraService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class DetPedidoCompraServiceImpl implements DetallePedidoCompraService {
    private final DetallePedidoCompraRepository detalleRepository;

    public DetPedidoCompraServiceImpl(DetallePedidoCompraRepository detalleRepository) {
        this.detalleRepository = detalleRepository;
    }
    
    
    /*
    @Override
    public List<DetallePedidoCompra> findByIdPedido(Long id) {
        return detalleRepository.findByIdPedido(id);
    }*/
    
    @Override
    public List<DetallePedidoCompra> findByIdPedido(Long id) {
        return detalleRepository.findByIdPedido(id);
    }

    @Transactional
    @Override
    public List<DetallePedidoCompra> saveDetalle(List<DetallePedidoCompra> detalle, Long idPedido) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
        detalle.forEach(d -> {
            PedidoCompraEntity pedido = new PedidoCompraEntity();
            pedido.setId(idPedido);
            d.setPedidoCompra(pedido);
        });
        
        return detalleRepository.saveAll(detalle);
    }

    @Transactional
    @Override
    public List<DetallePedidoCompra> updateDetalle(List<DetallePedidoCompra> detalle, Long idPedido) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
        List<DetallePedidoCompra> detallesAnteriores = detalleRepository.findByIdPedido(idPedido); 

        // Procesar eliminaciones
        procesarEliminaciones(detallesAnteriores, detalle);

        // Procesar modificaciones
        procesarModificaciones(detallesAnteriores, detalle);

        // Procesar registros
        procesarRegistros(detallesAnteriores, detalle);

     

        // Retornar los detalles del pedido actualizados
        return detalleRepository.findByIdPedido(idPedido);
    }

    @Transactional
    @Override
    public void deleteDetalle(Long idPedido) {
        detalleRepository.deleteAllByIdPedido(idPedido);
    }
    
    @Transactional
    @Override
    public List<DetallePedidoCompra> actualizarCantidadesRecepcionadas(List<DetalleRecepcion> detalleRecepcion,Long idPedido) {
        List<DetallePedidoCompra> detallesPedido = detalleRepository.findByIdPedido(idPedido);
        
        
        detalleRecepcion.forEach(rec -> {
            
            Optional<DetallePedidoCompra> detallePedido = detallesPedido.stream().filter(ped -> Objects.equals(ped.getId(), rec.getDetallePedido().getId())).findAny();
            
            if(detallePedido.isPresent() ){
                detallePedido.get().setCantRecepcionada(detallePedido.get().getCantRecepcionada() + rec.getCantRecepcionada());
            }

        });
        
        return detalleRepository.saveAll(detallesPedido);
      
        
    }
    
    @Transactional
    @Override
    public List<DetallePedidoCompra> modificarCantidadesRecepcionadas(List<DetalleRecepcion> nuevoDetalleRecepcion,List<DetalleRecepcion> anteriorDetalleRecepcion,Long idPedido) {
        List<DetallePedidoCompra> detallesPedido = detalleRepository.findByIdPedido(idPedido);
        
        System.out.println("anterior Detalle");
        anteriorDetalleRecepcion.forEach(rec -> {
            
            Optional<DetallePedidoCompra> detallePedido = detallesPedido.stream().filter(ped -> Objects.equals(ped.getId(), rec.getDetallePedido().getId())).findAny();
            System.out.println("antes cantRecepcionada");
            System.out.println(detallePedido.get().getCantRecepcionada());
            if(detallePedido.isPresent() ){
                detallePedido.get().setCantRecepcionada(detallePedido.get().getCantRecepcionada() - rec.getCantRecepcionada());
            }
            System.out.println("despues cantRecepcionada");
            System.out.println(detallePedido.get().getCantRecepcionada());
            // Asegurar que no sea negativa
            if (detallePedido.get().getCantRecepcionada() < 0) {
                throw new IllegalArgumentException("La cantidad recepcionada no puede ser negativa para el producto: " + detallePedido.get().getProducto().getNombre());
            }

        });
        
        System.out.println("nuevo Detalle");
        nuevoDetalleRecepcion.forEach(rec -> {
            
            Optional<DetallePedidoCompra> detallePedido = detallesPedido.stream().filter(ped -> Objects.equals(ped.getId(), rec.getDetallePedido().getId())).findAny();
            System.out.println("antes cantRecepcionada");
            System.out.println(detallePedido.get().getCantRecepcionada());
            if(detallePedido.isPresent() ){
                detallePedido.get().setCantRecepcionada(detallePedido.get().getCantRecepcionada() + rec.getCantRecepcionada());
            }
            System.out.println("despues cantRecepcionada");
            System.out.println(detallePedido.get().getCantRecepcionada());
            // Validar que no exceda la cantidad pedida
            if (detallePedido.get().getCantRecepcionada() > detallePedido.get().getCantidad()) {
                throw new IllegalArgumentException("La cantidad recepcionada excede la cantidad pedida para el producto: " + detallePedido.get().getProducto().getNombre());
            }

        });
        
        return detalleRepository.saveAll(detallesPedido);
      
        
    }
    
    private void procesarEliminaciones(List<DetallePedidoCompra> detallesAnteriores, List<DetallePedidoCompra> nuevosDetalles) throws Exception {
        List<DetallePedidoCompra> itemsEliminar = getElementosEliminar(detallesAnteriores, nuevosDetalles);

        if (!itemsEliminar.isEmpty()) {
            detalleRepository.deleteAll(itemsEliminar);
        }

    }
    
    
    private void procesarModificaciones(List<DetallePedidoCompra> detallesAnteriores, List<DetallePedidoCompra> nuevosDetalles)throws Exception {
        List<DetallePedidoCompra> itemsModificar = getElementosModificar(detallesAnteriores, nuevosDetalles);

        if (!itemsModificar.isEmpty()) {
            detalleRepository.saveAll(itemsModificar);
        }

    }
    
    private void procesarRegistros(List<DetallePedidoCompra> detallesAnteriores, List<DetallePedidoCompra> nuevosDetalles) {
        List<DetallePedidoCompra> itemsRegistrar = getElementosRegistrar(detallesAnteriores, nuevosDetalles);

        if (!itemsRegistrar.isEmpty()) {
            detalleRepository.saveAll(itemsRegistrar);
        }

    }
    
    public List<DetallePedidoCompra> getElementosEliminar (List<DetallePedidoCompra> anteriorDetalle, List<DetallePedidoCompra> actualDetalle)throws Exception {
        List<DetallePedidoCompra> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        anteriorDetalle.forEach(anterior -> {
            // Busca si el producto del detalle anterior existe en el detalle actual
            boolean existe = actualDetalle.stream()
                    .anyMatch(actual -> Objects.equals(actual.getProducto().getId(), anterior.getProducto().getId()));

            // Si no existe en el detalle actual, se intenta eliminar
            if (!existe) {
                if (anterior.getCantRecepcionada() > 0) {
                    // Lanza excepción si el producto ya fue facturado
                    throw new BadRequestException("No es posible eliminar el Producto: " + anterior.getProducto().getNombre());
                }
                // Si no está facturado, se agrega a la lista de elementos a eliminar
                elementos.add(anterior);
            }
        });
        
        return elementos;
    }
    
    public List<DetallePedidoCompra> getElementosModificar (List<DetallePedidoCompra> anteriorDetalle, List<DetallePedidoCompra> actualDetalle)throws Exception {
        List<DetallePedidoCompra> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        anteriorDetalle.forEach(anterior -> {
            // Busca si el producto del detalle anterior existe en el detalle actual
            Optional<DetallePedidoCompra> actual = actualDetalle.stream().filter(act -> Objects.equals(act.getProducto().getId(), anterior.getProducto().getId())).findAny();
            // Si encuentra, y hay diferencia se intenta modificar
            if(actual.isPresent() ){
                if(actual.get().getCantidad() != anterior.getCantidad() || actual.get().getCostoCompra() != anterior.getCostoCompra()){
                    // Lanza excepción si el producto ya fue facturado
                    if(anterior.getCantRecepcionada() > actual.get().getCantidad()){
                        throw new BadRequestException("No es posible modificar la cantidad del Producto: "+ anterior.getProducto().getNombre());
                    }
                    
                    
                    actual.get().setId(anterior.getId());
                    actual.get().setPedidoCompra(anterior.getPedidoCompra());
                    
                    elementos.add(actual.get());
                }
            }

        });
        
        return elementos;
    }
    
    public List<DetallePedidoCompra> getElementosRegistrar (List<DetallePedidoCompra> anteriorDetalle, List<DetallePedidoCompra> actualDetalle) {
        PedidoCompraEntity pedido = anteriorDetalle.get(0).getPedidoCompra();
        List<DetallePedidoCompra> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        actualDetalle.forEach(actual -> {
            // Busca si el producto del detalle actual no existe en el detalle anterior
            boolean existe = anteriorDetalle.stream()
                    .anyMatch(anterior -> Objects.equals(actual.getProducto().getId(), anterior.getProducto().getId()));

            // Si no existe en el detalle anterior, se intenta registrar
            if (!existe) {
                actual.setPedidoCompra(pedido);
              
                elementos.add(actual);
            }
        });
        
        return elementos;
    }
    
}
