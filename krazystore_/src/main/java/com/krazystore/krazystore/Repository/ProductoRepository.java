/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.ProductoDTO;
import com.krazystore.krazystore.Entity.ProductoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long>{
    
    @Query(nativeQuery = true)
        List<ProductoDTO> findProductosDTO();
        
        @Query(
    "SELECT new com.krazystore.krazystore.DTO.ProductoDTO(p.id, p.nombre, c, p.precio, "
            + "COALESCE(t.costo, 0), p.preVenta, COALESCE(p.cantPreVenta, 0), p.cantStock, p.cantDisponible, p.cantReservada ) "
            + "FROM ProductoEntity p "
            + "LEFT JOIN CategoriaEntity c ON p.categoria = c "
            + "LEFT JOIN CostoEntity t ON p.id = t.producto.id AND t.fechaInicio = " 
            + " (SELECT MAX(tc.fechaInicio) FROM CostoEntity tc WHERE tc.producto.id = p.id)"
           )
    public List<ProductoDTO> findProductos();
    
}
