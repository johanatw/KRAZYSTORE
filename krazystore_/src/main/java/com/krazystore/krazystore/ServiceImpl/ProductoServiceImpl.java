/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.CambioExistenciasEvent;
import Utils.PedidoFacturadoEvent;
import Utils.PreciosCompraActualizadosEvent;
import Utils.PreciosVentaEvent;
import Utils.ProductosFacturadosEvent;
import Utils.ProductosReservadosEvent;
import static Utils.TipoAjusteExistencia.DISMINUIR;
import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.ProductoDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.CostoEntity;
import com.krazystore.krazystore.Entity.PrecioVentaEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Mapper.ProductoEntityMapper;
import com.krazystore.krazystore.Repository.ProductoRepository;
import com.krazystore.krazystore.Service.ProductoService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
public class ProductoServiceImpl implements ProductoService{
    private final ProductoRepository productorepository;
    private final ApplicationEventPublisher eventPublisher;

    public ProductoServiceImpl(ProductoRepository productorepository, ApplicationEventPublisher eventPublisher) {
        this.productorepository = productorepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Autowired
    private ProductoEntityMapper productoMapper;

    @Override
    public List<ProductoDTO> findAll() {
        //return productorepository.findAll();
        return productorepository.findProductos();
    }
    

    @Override
    public Optional<ProductoEntity> findById(Long id) {
        return productorepository.findById(id);
    }

    @Transactional
    @Override
    public ProductoEntity saveProducto(ProductoDTO productoDTO) {
        
        ProductoEntity producto = productoMapper.apply(productoDTO);
        productorepository.save(producto);
        if(productoDTO.getPrecio()>0){
            PrecioVentaEntity precioVenta = new PrecioVentaEntity();
            precioVenta.setFecha(new Date());
            precioVenta.setProducto(producto);
            precioVenta.setPrecio((long)productoDTO.getPrecio());
            
            guardarNuevoPrecioVenta(precioVenta);
        }
        
        if(productoDTO.getCosto()>0){
            CostoEntity precioCompra = new CostoEntity();
            precioCompra.setFecha(new Date());
            precioCompra.setProducto(producto);
            precioCompra.setCosto((long)productoDTO.getCosto());
            
            guardarNuevoPrecioCompra(precioCompra);
        }
        return producto;
    }

    
    @Override
    public ProductoEntity updateProducto(ProductoEntity productoEntity, Long id) {
        ProductoEntity updatedProducto = productorepository.findById(id).get();
        
        updatedProducto.setNombre(productoEntity.getNombre());
        updatedProducto.setDescripcion(productoEntity.getDescripcion());
        updatedProducto.setSubCategoria(productoEntity.getSubCategoria());
        updatedProducto.setEstado(productoEntity.getEstado());
        updatedProducto.setBajoDemanda(productoEntity.getBajoDemanda());
        updatedProducto.setCantLimBajoDemanda(productoEntity.getCantLimBajoDemanda());
        updatedProducto.setCantDisponible(productoEntity.getCantDisponible());
        updatedProducto.setCantStock(productoEntity.getCantStock());
        updatedProducto.setCantReservada(productoEntity.getCantReservada());
        updatedProducto.setTipoIva(productoEntity.getTipoIva());

        return productorepository.save(updatedProducto);
    }
    
    @Override
    public void updateExistencias(List<ProductoEntity> productos) {
        
        productos.forEach(d -> {
            ProductoEntity updatedProducto = productorepository.findById(d.getId()).get();
            updatedProducto.setCantDisponible(d.getCantDisponible());
            //updatedProducto.setCantStock(d.getCantStock());
            updatedProducto.setCantReservada(d.getCantReservada());
            productorepository.save(updatedProducto);
        });
        

    }
    
    @Override
    public ProductoEntity updatePreVenta (Long id,Integer cantPreVenta){
        ProductoEntity updatedProducto = productorepository.findById(id).get();
        System.out.println(cantPreVenta);
        updatedProducto.setBajoDemanda(true);
        updatedProducto.setCantLimBajoDemanda(cantPreVenta);
        
        
        
        return productorepository.save(updatedProducto);
    }

    @Override
    public void deleteProducto(Long id) {
        ProductoEntity deletedProducto = productorepository.findById(id).get();
        deletedProducto.setActivo(Boolean.FALSE);
        productorepository.save(deletedProducto);
    }
    
    
    
    @EventListener
    public void handleCambioExistencias(CambioExistenciasEvent event) {
        actualizarExistencias(event.getProductosActualizar(), event.getTipoEvento());
    }
    
    private void actualizarExistencias(List<ProductoExistenciasDTO> productos, TipoEvento tipoEvento) {
        List<ProductoEntity> productosActualizar = new ArrayList<>();
        System.out.println("ACTUALIZAR EXISTENCIA");
        productos.forEach(d -> {
            ProductoEntity producto = productorepository.findById(d.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            switch (d.getAccion()) {
                case INCREMENTAR:                  
                    if (tipoEvento == TipoEvento.FACTURACION_PEDIDOS) {
                        producto.setCantStock(producto.getCantStock() - d.getCantidad());
                        producto.setCantReservada(producto.getCantReservada() - d.getCantidad());
                    } else if (tipoEvento == TipoEvento.FACTURACION_PRODUCTOS) {
                        producto.setCantDisponible(producto.getCantDisponible() - d.getCantidad());
                        producto.setCantStock(producto.getCantStock() - d.getCantidad());
                    } else if (tipoEvento == TipoEvento.ACTUALIZAR_RESERVAS) {
                        producto.setCantReservada(producto.getCantReservada() + d.getCantidad());
                        producto.setCantDisponible(producto.getCantDisponible() - d.getCantidad());
                    } else if (tipoEvento == TipoEvento.RECEPCIONAR_PRODUCTOS) {
                        producto.setCantStock(producto.getCantStock() + d.getCantidad());
                        producto.setCantDisponible(producto.getCantDisponible() + d.getCantidad());
                    } else if (tipoEvento == TipoEvento.AJUSTAR_INVENTARIO) {
                        System.out.println("ajustar inventario");
                        System.out.println(d.getCantidad());
                        producto.setCantStock(producto.getCantStock() + d.getCantidad());
                        producto.setCantDisponible(producto.getCantDisponible() + d.getCantidad());
                    }
                    break;
                case DISMINUIR:
                    if (tipoEvento == TipoEvento.ACTUALIZAR_RESERVAS) {
                        producto.setCantReservada(producto.getCantReservada() - d.getCantidad());
                        producto.setCantDisponible(producto.getCantDisponible() + d.getCantidad());
                    } else if (tipoEvento == TipoEvento.FACTURACION_PRODUCTOS) {
                        producto.setCantDisponible(producto.getCantDisponible() + d.getCantidad());
                        producto.setCantStock(producto.getCantStock() + d.getCantidad());
                    } else if (tipoEvento == TipoEvento.FACTURACION_PEDIDOS) {
                        producto.setCantStock(producto.getCantStock() + d.getCantidad());
                        producto.setCantReservada(producto.getCantReservada() + d.getCantidad());
                    } else if (tipoEvento == TipoEvento.AJUSTAR_INVENTARIO) {
                        System.out.println("ajustar inventario disminuir");
                        System.out.println(d.getCantidad());
                        producto.setCantStock(producto.getCantStock() - d.getCantidad());
                        producto.setCantDisponible(producto.getCantDisponible() - d.getCantidad());
                    } else if (tipoEvento == TipoEvento.RECEPCIONAR_PRODUCTOS) {
                        producto.setCantStock(producto.getCantStock() - d.getCantidad());
                        producto.setCantDisponible(producto.getCantDisponible() - d.getCantidad());
                    }
            }

            productosActualizar.add(producto);
        });

        productorepository.saveAll(productosActualizar);
    }

    @Override
    public List<ProductoDTO> buscarPorNombre(String nombre) {
        return productorepository.buscarPorNombre(nombre);
    }
    
    public void guardarNuevoPrecioVenta(PrecioVentaEntity precio) {
        List<PrecioVentaEntity> preciosActualizar = new ArrayList<>();
        preciosActualizar.add(precio);
        
        PreciosVentaEvent evento = new PreciosVentaEvent(preciosActualizar);
        eventPublisher.publishEvent(evento);
    }
    
    public void guardarNuevoPrecioCompra(CostoEntity precioCompra) {
        List<CostoEntity> preciosActualizar = new ArrayList<>();
        preciosActualizar.add(precioCompra);
        
        PreciosCompraActualizadosEvent evento = new PreciosCompraActualizadosEvent(preciosActualizar);
        eventPublisher.publishEvent(evento);
    }
}
