/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.TimbradoEntity;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface TimbradoRepository extends JpaRepository<TimbradoEntity, Long>{
    @Query("SELECT t FROM TimbradoEntity t WHERE t.vigenciaInicio <= CURRENT_DATE AND t.vigenciaFin >= CURRENT_DATE AND t.ultimoRemitido < t.numeroFin")
    public Optional<TimbradoEntity> findVigente();
    
}

