/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;


import com.krazystore.krazystore.Entity.EmpresaTransporte;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface EmpresaTransporteRepository extends JpaRepository<EmpresaTransporte, Long>{
    @Query(
    "SELECT e FROM EmpresaTransporte e "
            + "WHERE e.activo = TRUE "
            + "ORDER BY e.id DESC "
           )
    @Override
    public List<EmpresaTransporte> findAll();
}
