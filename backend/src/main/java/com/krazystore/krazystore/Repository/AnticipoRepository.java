/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.AplicacionAnticipo;
import com.krazystore.krazystore.Entity.PedidoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Repository
public interface AnticipoRepository extends JpaRepository<AnticipoEntity,Long>{

    public List<AnticipoEntity> findAllByOrderByIdDesc();
    
    @Query(
   "SELECT new com.krazystore.krazystore.Entity.AplicacionAnticipo(a) "
           + "FROM AnticipoEntity a WHERE a.pedido.id = ?1 AND a.saldo > 0 ORDER BY a.fecha DESC ")
    public List<AplicacionAnticipo> findAnticiposAplicarByIdPedidoVenta(Long id);

    
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM reembolsos_anticipo r WHERE r.id_anticipo = ?1 ", 
  nativeQuery = true)
    void deleteReembolsosByIdAnticipo(Long id);
    
    
    @Query(
  value = "SELECT r.id FROM reembolsos_anticipo r WHERE r.id_anticipo = ?1 ", 
  nativeQuery = true)
    public List<Long> getReembolsosByIdAnticipo(Long id);

    @Query(
   "SELECT a.id "
           + "FROM AnticipoEntity a "
           + "LEFT JOIN a.pedido p "
           + "WHERE p.id = ?1 ")
    public List<Long> findAnticiposByIdPedido(Long idPedido);

    @Transactional
    @Modifying
    @Query("UPDATE AnticipoEntity a SET a.pedido = NULL WHERE a.id IN ?1 ")
    public void desvincularPedido(List<Long> idAnticipos);
    
    
    
}
