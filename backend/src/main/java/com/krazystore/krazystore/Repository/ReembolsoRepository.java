/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.ReembolsoAnticipo;
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
public interface ReembolsoRepository extends JpaRepository<ReembolsoAnticipo, Long> {
    @Query(
  value = "SELECT * FROM reembolsos_anticipo r WHERE r.id_anticipo = ?1 ORDER BY r.fecha DESC ", 
  nativeQuery = true)
    public List<ReembolsoAnticipo> findByIdAnticipo(Long id);
    
    public List<ReembolsoAnticipo> findAllByOrderByIdDesc();
    
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM reembolsos_anticipo r WHERE r.id IN ?1 ", 
  nativeQuery = true)
    void deleteByIds(List<Long> ids);
}
