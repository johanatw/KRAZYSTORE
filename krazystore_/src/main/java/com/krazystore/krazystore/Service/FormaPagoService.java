/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.FormaPagoEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface FormaPagoService {
    List<FormaPagoEntity> findAll();
    Optional<FormaPagoEntity> findById(Long id);
    FormaPagoEntity saveFormaPago(FormaPagoEntity formaPagoEntity);
    FormaPagoEntity updateFormaPago(FormaPagoEntity formaPagoEntity, Long id);
    void deleteFormaPago(Long id);
    
    List<FormaPagoEntity> saveFormasPagos(List<FormaPagoEntity> pagos);
    void deleteFormasPagosByMovimientos(List<Long> ids);
    void deleteFormasPagosByMovimiento(Long id);
}
