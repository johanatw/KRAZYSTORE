/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.FormaCobroEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public interface FormaCobroService {
    List<FormaCobroEntity> saveFormasCobros(List<FormaCobroEntity> cobros);
    void deleteFormasCobrosByMovimientos(List<Long> ids);
    void deleteFormasCobrosByMovimiento(Long id);
}
