/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.ProductoDTO;
import com.krazystore.krazystore.Entity.ProductoEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long>{
    
   
        
        @Query(
    "SELECT new com.krazystore.krazystore.DTO.ProductoDTO(p.id, p.nombre, c, COALESCE(pr.precio, 0), "
            + "COALESCE(t.costo, 0), p.bajoDemanda, COALESCE(p.cantLimBajoDemanda, 0), p.cantStock, p.cantDisponible, p.cantReservada, p.tipoIva ) "
            + "FROM ProductoEntity p "
            + "LEFT JOIN p.tipoIva "
            + "LEFT JOIN SubCategoriaEntity c ON p.subCategoria = c "
            + "LEFT JOIN CostoEntity t ON p.id = t.producto.id AND t.fecha = " 
            + " (SELECT tc.fecha FROM CostoEntity tc WHERE tc.producto.id = p.id "
            + "AND tc.fecha <= NOW() ORDER BY tc.fecha DESC LIMIT 1 ) "
            + "LEFT JOIN PrecioVentaEntity pr ON pr.producto.id = p.id AND pr.fecha = "
            + " (SELECT pv.fecha FROM PrecioVentaEntity pv WHERE pv.producto.id = p.id "
            + "AND pv.fecha <= NOW() ORDER BY pv.fecha DESC LIMIT 1 )"
            + "WHERE p.activo = TRUE AND p.esServicio <> TRUE  ORDER BY p.id DESC"
           )
        
    public List<ProductoDTO> findProductos();
    
    @Query("SELECT new com.krazystore.krazystore.DTO.ProductoDTO(p.id, p.nombre ) "
            + "FROM ProductoEntity p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<ProductoDTO> buscarPorNombre(@Param("nombre") String nombre);

    @Query(
    "SELECT new com.krazystore.krazystore.DTO.ProductoDTO(p.id, p.nombre, "
            + "COALESCE(t.costo, 0), p.tipoIva ) "
            + "FROM ProductoEntity p "
            + "LEFT JOIN p.tipoIva "
            + "LEFT JOIN CostoEntity t ON p.id = t.producto.id AND t.fecha = " 
            + " (SELECT tc.fecha FROM CostoEntity tc WHERE tc.producto.id = p.id "
            + "AND tc.fecha <= NOW() ORDER BY tc.fecha DESC LIMIT 1 ) "
            + "WHERE p.nombre = 'Servicio de entrega'"
           )
    public Optional<ProductoDTO> getServicioTransporte();

    @Query(
    "SELECT new com.krazystore.krazystore.DTO.ProductoDTO(p.id, p.nombre, "
            + "COALESCE(t.costo, 0), p.tipoIva ) "
            + "FROM ProductoEntity p "
            + "LEFT JOIN p.tipoIva "
            + "LEFT JOIN CostoEntity t ON p.id = t.producto.id AND t.fecha = " 
            + " (SELECT tc.fecha FROM CostoEntity tc WHERE tc.producto.id = p.id "
            + "AND tc.fecha <= NOW() ORDER BY tc.fecha DESC LIMIT 1 ) "
            + "WHERE p.esServicio = TRUE "
           )
    public List<ProductoDTO> getServicios();

    @Query(
    "SELECT new com.krazystore.krazystore.DTO.ProductoDTO(p.id, p.nombre, "
            + "COALESCE(t.costo, 0), p.tipoIva ) "
            + "FROM ProductoEntity p "
            + "LEFT JOIN p.tipoIva "
            + "LEFT JOIN CostoEntity t ON p.id = t.producto.id AND t.fecha = " 
            + " (SELECT tc.fecha FROM CostoEntity tc WHERE tc.producto.id = p.id "
            + "AND tc.fecha <= NOW() ORDER BY tc.fecha DESC LIMIT 1 ) "
            + "WHERE p.nombre = 'Costo de envío'"
           )
    public Optional<ProductoDTO> getCostoEnvio();
    
    
}
