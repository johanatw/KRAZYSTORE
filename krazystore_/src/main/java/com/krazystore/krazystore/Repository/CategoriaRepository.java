/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.CategoriaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long>{
    @Query(
    "SELECT c FROM CategoriaEntity c "
            + "WHERE c.activo = TRUE "
            + "ORDER BY c.id DESC "
           )
    @Override
    public List<CategoriaEntity> findAll();
}
