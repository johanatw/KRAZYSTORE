/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.ProveedorEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Long> {

    @Query(
    "SELECT p "
            + "FROM ProveedorEntity p "
            + "WHERE p.activo = TRUE AND p.esProveedorImportacion <> TRUE "
            + "ORDER BY p.id "
           )
    public List<ProveedorEntity> findProveedoresProductos();

    @Query(
    "SELECT p "
            + "FROM ProveedorEntity p "
            + "WHERE p.activo = TRUE AND p.esProveedorImportacion = TRUE "
            + "ORDER BY p.id "
           )
    public List<ProveedorEntity> findProveedoresImportacion();

    @Query(
    "SELECT p "
            + "FROM ProveedorEntity p "
            + "LEFT JOIN p.tipo t "
            + "WHERE p.activo = TRUE AND p.esProveedorImportacion <> TRUE "
            + "AND t.descripcion = 'Nacional' "
            + "ORDER BY p.id "
           )
    public List<ProveedorEntity> findProveedoresNacionalesProductos();
    
}
