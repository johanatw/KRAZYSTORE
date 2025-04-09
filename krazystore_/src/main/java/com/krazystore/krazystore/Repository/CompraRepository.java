/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.CompraEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface CompraRepository extends JpaRepository<CompraEntity, Long> {
        @Query(
        "SELECT c FROM CompraEntity c "
               + "LEFT JOIN FETCH c.pedido p "
                + "WHERE p.id = ?1 "
               )
        public List<CompraEntity> findByIdPedido(Long id);
}
