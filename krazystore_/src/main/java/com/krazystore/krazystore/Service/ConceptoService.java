/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.ConceptoEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public interface ConceptoService {
    List<ConceptoEntity> findByTipo(char tipo);
}
