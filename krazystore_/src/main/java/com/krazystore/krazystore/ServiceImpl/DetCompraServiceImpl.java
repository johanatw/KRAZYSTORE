/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.DetalleCompra;
import com.krazystore.krazystore.Repository.DetalleCompraRepository;
import com.krazystore.krazystore.Service.DetalleCompraService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.Date;
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
public class DetCompraServiceImpl implements DetalleCompraService {
    private final DetalleCompraRepository detalleRepository;

    public DetCompraServiceImpl(DetalleCompraRepository detalleRepository) {
        this.detalleRepository = detalleRepository;
    }
    

    @Override
    public List<DetalleCompra> findByIdCompra(Long id) {
        return detalleRepository.findAllByIdCompra(id);
    }

    @Transactional
    @Override
    public List<DetalleCompra> saveDetCompra(List<DetalleCompra> detalle, Long idCompra) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
        detalle.forEach(d -> {
            CompraEntity compra = new CompraEntity();
            compra.setId(idCompra);
            d.setCompra(compra);
        });
        
        return detalleRepository.saveAll(detalle);
    }

    @Transactional
    @Override
    public List<DetalleCompra> updateDetCompra(List<DetalleCompra> detalle, Long idCompra) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
        List<DetalleCompra> detallesAnteriores = detalleRepository.findAllByIdCompra(idCompra); 

        // Procesar eliminaciones
        procesarEliminaciones(detallesAnteriores, detalle);

        // Procesar modificaciones
        procesarModificaciones(detallesAnteriores, detalle);

        // Procesar registros
        procesarRegistros(detallesAnteriores, detalle);

     

        // Retornar los detalles del pedido actualizados
        return detalleRepository.findAllByIdCompra(idCompra);
    }

    @Transactional
    @Override
    public void deleteDetCompra(Long idCompra) {
        detalleRepository.deleteAllByIdCompra(idCompra);
    }

    @Override
    public boolean esCostoActualizado(Long idProducto, int costo, Date fecha) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private void procesarEliminaciones(List<DetalleCompra> detallesAnteriores, List<DetalleCompra> nuevosDetalles)  {
        List<DetalleCompra> itemsEliminar = getElementosEliminar(detallesAnteriores, nuevosDetalles);

        if (!itemsEliminar.isEmpty()) {
            detalleRepository.deleteAll(itemsEliminar);
        }

    }
    
    
    private void procesarModificaciones(List<DetalleCompra> detallesAnteriores, List<DetalleCompra> nuevosDetalles) {
        List<DetalleCompra> itemsModificar = getElementosModificar(detallesAnteriores, nuevosDetalles);

        if (!itemsModificar.isEmpty()) {
            detalleRepository.saveAll(itemsModificar);
        }

    }
    
    private void procesarRegistros(List<DetalleCompra> detallesAnteriores, List<DetalleCompra> nuevosDetalles) {
        List<DetalleCompra> itemsRegistrar = getElementosRegistrar(detallesAnteriores, nuevosDetalles);

        if (!itemsRegistrar.isEmpty()) {
            detalleRepository.saveAll(itemsRegistrar);
        }

    }
    
    public List<DetalleCompra> getElementosEliminar (List<DetalleCompra> anteriorDetalle, List<DetalleCompra> actualDetalle) {
        List<DetalleCompra> elementos = new ArrayList<>();
                           
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
    
    public List<DetalleCompra> getElementosModificar (List<DetalleCompra> anteriorDetalle, List<DetalleCompra> actualDetalle) {
        List<DetalleCompra> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        anteriorDetalle.forEach(anterior -> {
            // Busca si el producto del detalle anterior existe en el detalle actual
            Optional<DetalleCompra> actual = actualDetalle.stream().filter(act -> Objects.equals(act.getProducto().getId(), anterior.getProducto().getId())).findAny();
            // Si encuentra, y hay diferencia se intenta modificar
            if(actual.isPresent() ){
                if(actual.get().getCantidad() != anterior.getCantidad()){
                    // Lanza excepción si el producto ya fue facturado
                    /*if(anterior.getCantRecepcionada() > actual.get().getCantidad()){
                        throw new BadRequestException("No es posible modificar la cantidad del Producto: "+ anterior.getProducto().getNombre());
                    }*/
                    
                    actual.get().setId(anterior.getId());
                    actual.get().setCompra(anterior.getCompra());
                    
                    elementos.add(actual.get());
                }
            }

        });
        
        return elementos;
    }
    
    public List<DetalleCompra> getElementosRegistrar (List<DetalleCompra> anteriorDetalle, List<DetalleCompra> actualDetalle) {
        CompraEntity pedido = anteriorDetalle.get(0).getCompra();
        List<DetalleCompra> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        actualDetalle.forEach(actual -> {
            // Busca si el producto del detalle actual no existe en el detalle anterior
            boolean existe = anteriorDetalle.stream()
                    .anyMatch(anterior -> Objects.equals(actual.getProducto().getId(), anterior.getProducto().getId()));

            // Si no existe en el detalle anterior, se intenta registrar
            if (!existe) {
                actual.setCompra(pedido);
              
                elementos.add(actual);
            }
        });
        
        return elementos;
    }
    
}
