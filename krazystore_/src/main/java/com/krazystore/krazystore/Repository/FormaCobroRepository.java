/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.FormaCobroEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Repository
public interface FormaCobroRepository extends JpaRepository<FormaCobroEntity, Long> {
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM formas_cobro p USING movimientos m WHERE p.id_movimiento IN ?1 ", 
  nativeQuery = true)
    void deleteCobrosByMovimientos(List<Long> ids);
    
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM formas_cobro p USING movimientos m WHERE p.id_movimiento = m.id AND m.id = ?1 ", 
  nativeQuery = true)
    void deleteCobrosByMovimiento(Long id);
}
