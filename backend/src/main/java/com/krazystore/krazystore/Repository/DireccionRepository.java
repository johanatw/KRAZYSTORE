/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.DireccionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface DireccionRepository extends JpaRepository<DireccionEntity, Long>{
    @Query(
        "SELECT d FROM DireccionEntity d "
        + "LEFT JOIN d.persona p "
        + "JOIN ClienteEntity c ON c.persona = p "
        + "WHERE c.id = ?1"
    )
    public List<DireccionEntity> findByIdCliente(long id);
}
