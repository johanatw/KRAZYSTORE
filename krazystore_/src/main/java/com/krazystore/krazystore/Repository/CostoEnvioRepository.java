/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;


import com.krazystore.krazystore.Entity.CostoEnvioEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author HP
 */
public interface CostoEnvioRepository extends JpaRepository<CostoEnvioEntity, Long> {
    
    @Query(
  value = "SELECT * FROM costos_envio u WHERE u.id_ciudad = ?1", 
  nativeQuery = true)
    public List<CostoEnvioEntity> findByCiudad(Long id);
    
}
