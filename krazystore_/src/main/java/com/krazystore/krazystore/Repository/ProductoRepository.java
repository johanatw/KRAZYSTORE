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
    
}
