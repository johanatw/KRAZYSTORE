/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.TipoAjusteExistencia;
import Utils.TipoOperacionDetalle;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.DetalleCompra;
import com.krazystore.krazystore.Repository.DetalleCompraRepository;
import com.krazystore.krazystore.Service.DetalleCompraService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
    public List<ProductoExistenciasDTO> saveDetCompra(List<DetalleCompra> detalle, Long idCompra) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
        List<ProductoExistenciasDTO> productosActualizarExistencias = new ArrayList<>();
        detalle.forEach(d -> {
            ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO(
                    d.getProducto().getId(),
                    d.getCantidad(),
                    TipoAjusteExistencia.INCREMENTAR
            );
            CompraEntity compra = new CompraEntity();
            compra.setId(idCompra);
            d.setCompra(compra);
            productosActualizarExistencias.add(productoActualizar);
        });
        
        detalleRepository.saveAll(detalle);
        return productosActualizarExistencias;
    }

    @Transactional
    @Override
    public List<ProductoExistenciasDTO> updateDetCompra(List<DetalleCompra> detalle, Long idCompra) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
        List<DetalleCompra> detallesAnteriores = detalleRepository.findAllByIdCompra(idCompra); 
        
        // Crear copias de los objetos para evitar que se modifiquen las referencias originales
        List<DetalleCompra> anterioresCopias = detallesAnteriores.stream()
            .map(item -> new DetalleCompra(item)) // Constructor de copia
            .collect(Collectors.toList());
        
        // Procesar cambios en los detalles
        procesarCambiosEnDetalles(detallesAnteriores, detalle);
        
        //List<DetallePedidoEntity> detallesAnteriores = detallepedidorepository.findByNroPedido(id);
        List<ProductoExistenciasDTO> productosActualizarExistencias = calcularProductosExistencias(anterioresCopias, detalle);   

        return productosActualizarExistencias;
        
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
    private void procesarCambiosEnDetalles(List<DetalleCompra> detallesAnteriores, List<DetalleCompra> nuevosDetalles) throws Exception {
        procesarEliminaciones(detallesAnteriores, nuevosDetalles);
        procesarModificaciones(detallesAnteriores, nuevosDetalles);
        procesarRegistros(detallesAnteriores, nuevosDetalles);
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
    
    private List<ProductoExistenciasDTO> calcularProductosExistencias(List<DetalleCompra> detallesAnteriores, List<DetalleCompra> nuevosDetalles) throws Exception {
        List<ProductoExistenciasDTO> productosExistencias = new ArrayList<>();

        // Agregar productos de eliminaciones
        productosExistencias.addAll(getProductosExistencias(detallesAnteriores, nuevosDetalles, TipoOperacionDetalle.ELIMINAR));

        // Agregar productos de modificaciones
        productosExistencias.addAll(getProductosExistencias(detallesAnteriores, nuevosDetalles, TipoOperacionDetalle.MODIFICAR));

        // Agregar productos de registros
        productosExistencias.addAll(getProductosExistencias(detallesAnteriores, nuevosDetalles, TipoOperacionDetalle.REGISTRAR));

        return productosExistencias;
    }
    
    private List<ProductoExistenciasDTO> getProductosExistencias(List<DetalleCompra> detallesAnteriores, List<DetalleCompra> nuevosDetalles, TipoOperacionDetalle tipoOperacion) throws Exception{
        List<ProductoExistenciasDTO> productosExistencias = new ArrayList<>();
        Map<Long, Integer> mapaCantidadAnterior = construirMapaCantidadAnterior(detallesAnteriores);

        List<DetalleCompra> detallesProcesar = switch (tipoOperacion) {
            case REGISTRAR -> getElementosRegistrar(detallesAnteriores, nuevosDetalles);
            case MODIFICAR -> getElementosModificar(detallesAnteriores, nuevosDetalles);
            case ELIMINAR -> getElementosEliminar(detallesAnteriores, nuevosDetalles);
        };
                
        for (DetalleCompra detalle : detallesProcesar) {
            Long idProducto = detalle.getProducto().getId();
            
            int cantidadAnterior = (tipoOperacion == TipoOperacionDetalle.MODIFICAR ) ? mapaCantidadAnterior.getOrDefault(idProducto, 0): 0;
            int cantidadNueva = detalle.getCantidad();
            
            ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO();
            productoActualizar.setIdProducto(idProducto);
            productoActualizar.setCantidad(cantidadNueva - cantidadAnterior);
            productoActualizar.setAccion(determinarAccion(tipoOperacion));
            productosExistencias.add(productoActualizar);
        }

        return productosExistencias;
    }
    

    private TipoAjusteExistencia determinarAccion(TipoOperacionDetalle tipoOperacion) {
        return (tipoOperacion == TipoOperacionDetalle.ELIMINAR) ? TipoAjusteExistencia.DISMINUIR : TipoAjusteExistencia.INCREMENTAR;
    }
    
    private Map<Long, Integer> construirMapaCantidadAnterior(List<DetalleCompra> detallesAnteriores) {
        return detallesAnteriores.stream()
                .collect(Collectors.toMap(
                    detalle -> detalle.getProducto().getId(),
                    DetalleCompra::getCantidad
                ));
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
                if(actual.get().getCantidad() != anterior.getCantidad() || actual.get().getCostoCompra()!= anterior.getCostoCompra()  ){
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
