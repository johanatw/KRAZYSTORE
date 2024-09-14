/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;


import com.krazystore.krazystore.DTO.DetallePedidoDTO;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedidoEntity, Long>{
    
     @Query(
  value = "SELECT * FROM detalle_pedidos u WHERE u.id_pedido = ?1 ", 
  nativeQuery = true)
    public List<DetallePedidoEntity> findByNroPedido(Long idPedido);
    
   // @Query(value = "select com.krazystore.krazystore.DTO.DetallePedidoDTO(long id, String producto, int precio, int cantidad, int subtotal) from Person where age = 30")
   @Query(nativeQuery = true)   
     List<DetallePedidoDTO> findPedido(Long idPedido);
     
     @Transactional
     @Modifying
     @Query(
  value = "DELETE FROM detalle_pedidos u WHERE u.id_pedido = :id ", 
  nativeQuery = true)

    void deleteByPedido(@Param("id") Long id);

    @Query(
  value = "SELECT * FROM detalle_pedidos u WHERE u.id_pedido = :idPedido AND u.id_producto = :id ", 
  nativeQuery = true)
    public DetallePedidoEntity findByProductoId(@Param("id")Long id, @Param("idPedido") Long idPedido);
}
