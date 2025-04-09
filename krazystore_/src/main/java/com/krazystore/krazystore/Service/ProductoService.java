/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Service;


import com.krazystore.krazystore.DTO.ProductoDTO;
import com.krazystore.krazystore.Entity.ProductoEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface ProductoService {
    List<ProductoDTO> findAll();
    
    Optional<ProductoEntity> findById(Long id);
    List<ProductoDTO> buscarPorNombre(String nombre);
    ProductoEntity saveProducto(ProductoDTO productoDTO);
    ProductoEntity updateProducto(ProductoEntity productoEntity, Long id);
    ProductoEntity updatePreVenta (Long id,Integer cantPreVenta);
    void updateExistencias(List<ProductoEntity> productos);
    void deleteProducto(Long id);
}
