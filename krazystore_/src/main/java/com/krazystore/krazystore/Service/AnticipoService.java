/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import Utils.TipoPedido;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface AnticipoService {
    List<AnticipoEntity> findAll();
    List<Long> getIdReembolsosAnticipo(Long id);
    void deleteReembolsosByIdAnticipo(Long id);
    Optional<AnticipoEntity> findById(Long id);
    AnticipoEntity saveAnticipo(AnticipoEntity anticipoEntity);
    AnticipoEntity updateAnticipo(AnticipoEntity asnticipoEntity, Long id);
    AnticipoEntity updateAnticipo(AnticipoEntity asnticipoEntity);
    List<AnticipoEntity> updateAnticipos(List<AnticipoEntity> anticipos);
    void getAnticipoPdf(HttpServletResponse response, Long idAnticipo);
    int deleteAnticipo(Long id);
    List<AnticipoEntity> findByIdPedido(Long id, TipoPedido tipoPedido);
    boolean existsByPedido(PedidoEntity p);
    int verificarAnticipoEstado(Long id);
    void deleteAnticipoReembolsos(Long id);
    AnticipoEntity actualizarSaldoAnticipo(Long idAnticipo, int montoReembolsado);
}
