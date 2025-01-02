/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.CambioExistenciasEvent;
import Utils.PedidoFacturadoEvent;
import Utils.ProductosFacturadosEvent;
import Utils.ProductosReservadosEvent;
import static Utils.TipoAjusteExistencia.DISMINUIR;
import Utils.TipoEventoExistencias;
import com.krazystore.krazystore.DTO.ProductoDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Repository.ProductoRepository;
import com.krazystore.krazystore.Service.ProductoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class ProductoServiceImpl implements ProductoService{
    private final ProductoRepository productorepository;

    public ProductoServiceImpl(ProductoRepository productorepository) {
        this.productorepository = productorepository;
    }
    

    @Override
    public List<ProductoDTO> findAll() {
        //return productorepository.findAll();
        return productorepository.findProductos();
    }
    
    

    @Override
    public Optional<ProductoEntity> findById(Long id) {
        return productorepository.findById(id);
    }

    @Override
    public ProductoEntity saveProducto(ProductoEntity productoEntity) {
        return productorepository.save(productoEntity);
    }

    @Override
    public ProductoEntity updateProducto(ProductoEntity productoEntity, Long id) {
        ProductoEntity updatedProducto = productorepository.findById(id).get();
        
        updatedProducto.setNombre(productoEntity.getNombre());
        updatedProducto.setCategoria(productoEntity.getCategoria());
        updatedProducto.setPrecio(productoEntity.getPrecio());
        updatedProducto.setCosto(productoEntity.getCosto());
        updatedProducto.setEstado(productoEntity.getEstado());
        updatedProducto.setPreVenta(productoEntity.getPreVenta());
        updatedProducto.setCantPreVenta(productoEntity.getCantPreVenta());
        updatedProducto.setCantDisponible(productoEntity.getCantDisponible());
        updatedProducto.setCantStock(productoEntity.getCantStock());
        updatedProducto.setCantReservada(productoEntity.getCantReservada());

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
        updatedProducto.setPreVenta(true);
        updatedProducto.setCantPreVenta(cantPreVenta);
        
        
        
        return productorepository.save(updatedProducto);
    }

    @Override
    public void deleteProducto(Long id) {
        productorepository.deleteById(id);
    }
    
    
    
    @EventListener
    public void handleCambioExistencias(CambioExistenciasEvent event) {
        actualizarExistencias(event.getProductosActualizar(), event.getTipoEvento());
    }
    
    private void actualizarExistencias(List<ProductoExistenciasDTO> productos, TipoEventoExistencias tipoEvento) {
        List<ProductoEntity> productosActualizar = new ArrayList<>();

        productos.forEach(d -> {
            ProductoEntity producto = productorepository.findById(d.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            switch (d.getAccion()) {
                case INCREMENTAR:                  
                    if (tipoEvento == TipoEventoExistencias.FACTURACION_PEDIDOS) {
                        producto.setCantStock(producto.getCantStock() - d.getCantidad());
                        producto.setCantReservada(producto.getCantReservada() - d.getCantidad());
                    } else if (tipoEvento == TipoEventoExistencias.FACTURACION_PRODUCTOS) {
                        producto.setCantDisponible(producto.getCantDisponible() - d.getCantidad());
                        producto.setCantStock(producto.getCantStock() - d.getCantidad());
                    } else if (tipoEvento == TipoEventoExistencias.ACTUALIZAR_RESERVAS) {
                        producto.setCantReservada(producto.getCantReservada() + d.getCantidad());
                        producto.setCantDisponible(producto.getCantDisponible() - d.getCantidad());
                    } else if (tipoEvento == TipoEventoExistencias.RECEPCIONAR_PRODUCTOS) {
                        producto.setCantStock(producto.getCantStock() + d.getCantidad());
                        producto.setCantDisponible(producto.getCantDisponible() + d.getCantidad());
                    } else if (tipoEvento == TipoEventoExistencias.AJUSTAR_INVENTARIO) {
                        producto.setCantStock(producto.getCantStock() + d.getCantidad());
                        producto.setCantDisponible(producto.getCantDisponible() + d.getCantidad());
                    }
                    break;
                case DISMINUIR:
                    if (tipoEvento == TipoEventoExistencias.ACTUALIZAR_RESERVAS) {
                        producto.setCantReservada(producto.getCantReservada() - d.getCantidad());
                        producto.setCantDisponible(producto.getCantDisponible() + d.getCantidad());
                    } else if (tipoEvento == TipoEventoExistencias.FACTURACION_PRODUCTOS) {
                        producto.setCantDisponible(producto.getCantDisponible() + d.getCantidad());
                        producto.setCantStock(producto.getCantStock() + d.getCantidad());
                    } else if (tipoEvento == TipoEventoExistencias.FACTURACION_PEDIDOS) {
                        producto.setCantStock(producto.getCantStock() + d.getCantidad());
                        producto.setCantReservada(producto.getCantReservada() + d.getCantidad());
                    }
            }

            productosActualizar.add(producto);
        });

        productorepository.saveAll(productosActualizar);
    }
}
