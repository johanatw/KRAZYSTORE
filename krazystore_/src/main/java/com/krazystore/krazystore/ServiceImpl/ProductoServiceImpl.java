/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.ProductoDTO;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Repository.ProductoRepository;
import com.krazystore.krazystore.Service.ProductoService;
import java.util.List;
import java.util.Optional;
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
    public List<ProductoEntity> findAll() {
        return productorepository.findAll();
        //return productorepository.findProductosDTO();
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
    
}
