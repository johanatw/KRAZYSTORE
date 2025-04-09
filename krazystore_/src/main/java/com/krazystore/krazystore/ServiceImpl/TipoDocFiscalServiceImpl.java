/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.TipoDocumentoFiscal;
import com.krazystore.krazystore.Repository.TipoDocFiscalRepository;
import com.krazystore.krazystore.Service.TipoDocFiscalService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class TipoDocFiscalServiceImpl implements TipoDocFiscalService {
    private final TipoDocFiscalRepository tipoDocFiscalRepository;
    
    public TipoDocFiscalServiceImpl(TipoDocFiscalRepository tipoDocFiscalRepository) {
        this.tipoDocFiscalRepository = tipoDocFiscalRepository;
    }
    

    @Override
    public List<TipoDocumentoFiscal> findAll() {
        return tipoDocFiscalRepository.findAll();
    }
}
