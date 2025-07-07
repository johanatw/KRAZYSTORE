/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.CajaEntity;
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
public interface CajaRepository extends JpaRepository<CajaEntity, Long>{
    @Query(
   "SELECT c FROM CajaEntity c "
           + "WHERE c.estado = 'A'")
    public Optional<CajaEntity> getCajaAbierta();
    
    @Query(
   "SELECT c FROM CajaEntity c "
           + "WHERE c.estado <> 'P' order by c.id desc")
    public List<CajaEntity> findAllByOrderByIdDesc();
    
}
