/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.TimbradoEntity;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface TimbradoService {
    Optional<TimbradoEntity> getTimbradoVigente();
    String getNroFactura(TimbradoEntity timbrado);
    TimbradoEntity updateTimbrado(TimbradoEntity timbrado);
}
