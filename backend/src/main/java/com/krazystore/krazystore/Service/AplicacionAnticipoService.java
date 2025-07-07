/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.AplicacionAnticipo;
import java.util.List;

/**
 *
 * @author HP
 */
public interface AplicacionAnticipoService {
    List<AplicacionAnticipo> getAnticiposAplicarByIdPedido(Long id);
    List<AplicacionAnticipo> saveAnticiposAplicados(List<AplicacionAnticipo> anticiposAplicados);
    void deleteAnticiposAplicadosByIdMovimiento(Long id);
}
