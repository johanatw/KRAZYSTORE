/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.EmpresaTransporte;
import com.krazystore.krazystore.Entity.MedioPagoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface MedioPagoRepository extends JpaRepository<MedioPagoEntity, Long>  {
    @Query(
    "SELECT m FROM MedioPagoEntity m "
            + "WHERE m.activo = TRUE "
            + "ORDER BY m.id DESC "
           )
    @Override
    public List<MedioPagoEntity> findAll();
}
