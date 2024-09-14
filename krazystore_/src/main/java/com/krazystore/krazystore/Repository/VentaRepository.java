/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Repository;


import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, Long>{
    public List<VentaEntity> findAllByOrderByIdDesc();
}
