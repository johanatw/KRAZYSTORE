/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.PuntoEntregaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface PuntoEntregaRepository extends JpaRepository<PuntoEntregaEntity, Long>{
    @Query(
    "SELECT p FROM PuntoEntregaEntity p "
            + "WHERE p.activo = TRUE "
            + "ORDER BY p.id DESC "
           )
    @Override
    public List<PuntoEntregaEntity> findAll();
}
