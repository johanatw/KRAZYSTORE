/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.ConceptoEntity;
import com.krazystore.krazystore.Repository.ConceptoRepository;
import com.krazystore.krazystore.Service.ConceptoService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class ConceptoServiceImpl implements ConceptoService {
    private final ConceptoRepository conceptoRepository;
    
    public ConceptoServiceImpl(ConceptoRepository conceptoRepository) {
        this.conceptoRepository = conceptoRepository;
    }

    @Override
    public List<ConceptoEntity> findByTipo(char tipo) {
        return conceptoRepository.getConceptosByTipo(tipo);
    }
    
}
