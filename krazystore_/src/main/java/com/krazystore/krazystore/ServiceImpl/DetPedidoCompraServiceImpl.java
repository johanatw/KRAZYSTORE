/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.DetallePedidoCompraDTO;
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
    
    
    
    @Override
    public List<DetallePedidoCompraDTO> findDTOByIdPedido(Long id) {
        return detalleRepository.findDetallesByIdPedido(id);
    }
    
    @Override
    public List<DetallePedidoCompra> findByIdPedido(Long id) {
        return detalleRepository.findByIdPedido(id);
    }
    
    @Override
    public Long getCantProductosPedidos(Long id) {
        return detalleRepository.getCantProductosPedidos(id);
    }
    
    @Override
    public Long getCantProductosRecepcionados(Long id) {
        return detalleRepository.getCantProductosRecepcionados(id);
    }

    @Transactional
    @Override
    public List<DetallePedidoCompra> saveDetalle(List<DetallePedidoCompra> detalle, Long idPedido) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
        
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
                /*if (anterior.getCantRecepcionada() > 0) {
                    // Lanza excepción si el producto ya fue facturado
                    throw new BadRequestException("No es posible eliminar el Producto: " + anterior.getProducto().getNombre());
                }*/
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
                    /*if(anterior.getCantRecepcionada() > actual.get().getCantidad()){
                        throw new BadRequestException("No es posible modificar la cantidad del Producto: "+ anterior.getProducto().getNombre());
                    }*/
                    
                    
                    actual.get().setId(anterior.getId());
                    
                    elementos.add(actual.get());
                }
            }

        });
        
        return elementos;
    }
    
    public List<DetallePedidoCompra> getElementosRegistrar (List<DetallePedidoCompra> anteriorDetalle, List<DetallePedidoCompra> actualDetalle) {
        List<DetallePedidoCompra> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        actualDetalle.forEach(actual -> {
            // Busca si el producto del detalle actual no existe en el detalle anterior
            boolean existe = anteriorDetalle.stream()
                    .anyMatch(anterior -> Objects.equals(actual.getProducto().getId(), anterior.getProducto().getId()));

            // Si no existe en el detalle anterior, se intenta registrar
            if (!existe) {            
                elementos.add(actual);
            }
        });
        
        return elementos;
    }

    @Override
    public List<DetallePedidoCompraDTO> findDetalleFacturarByIdsRecepciones(List<Long> ids) {
        return detalleRepository.findDetalleFacturarByIdsRecepciones(ids);
    }
    
}
