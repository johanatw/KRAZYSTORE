/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.SubCategoriaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoriaEntity, Long>{
    @Query("SELECT sc FROM SubCategoriaEntity sc WHERE sc.categoria.id = ?1 ")
    public List<SubCategoriaEntity> findByIdCategoria(Long id);
    
    @Query("SELECT sc FROM SubCategoriaEntity sc WHERE sc.categoria.id IN ?1 ")
    public List<SubCategoriaEntity> findByIdsCategoria(List<Long> ids);
}
