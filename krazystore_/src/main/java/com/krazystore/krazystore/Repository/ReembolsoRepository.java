/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.ReembolsoEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface ReembolsoRepository extends JpaRepository<ReembolsoEntity, Long> {
    @Query(
  value = "SELECT * FROM reembolsos r WHERE r.id_anticipo = ?1 ORDER BY r.fecha DESC ", 
  nativeQuery = true)
    public List<ReembolsoEntity> findByIdAnticipo(Long id);
    
    public List<ReembolsoEntity> findAllByOrderByIdDesc();
}
