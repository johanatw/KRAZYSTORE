/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.CiudadEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface CiudadRepository extends JpaRepository<CiudadEntity, Long> {
    @Query(
    "SELECT c FROM CiudadEntity c "
           + "LEFT JOIN FETCH c.departamento p "
            + "where p.id = ?1"
           )
    public List<CiudadEntity> findByIdDepartamento(long id);
}
