/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import Utils.TipoEvento;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.AplicacionAnticipo;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface AnticipoService {
    List<AnticipoEntity> findAll();
    List<Long> getIdReembolsosAnticipo(Long id);
    Optional<AnticipoEntity> findById(Long id);
    AnticipoEntity saveAnticipo(AnticipoEntity anticipoEntity);
    int deleteAnticipo(Long id);
    List<AplicacionAnticipo> findAnticiposAplicarByIdPedidoVenta(Long id);
    void deleteAnticipoReembolsos(Long id);
    AnticipoEntity actualizarSaldoAnticipo(Long idAnticipo, int montoReembolsado,TipoEvento evento);
}
