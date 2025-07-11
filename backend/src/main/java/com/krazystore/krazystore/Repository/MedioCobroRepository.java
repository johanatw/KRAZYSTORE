/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.MedioCobroEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface MedioCobroRepository extends JpaRepository<MedioCobroEntity, Long> {
    @Query(
    "SELECT m FROM MedioCobroEntity m "
            + "WHERE m.activo = TRUE "
            + "ORDER BY m.id DESC "
           )
    @Override
    public List<MedioCobroEntity> findAll();
}
