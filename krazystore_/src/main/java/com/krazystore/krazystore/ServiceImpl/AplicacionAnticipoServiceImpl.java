/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.AplicacionAnticiposEvent;
import Utils.TipoEvento;
import com.krazystore.krazystore.Entity.AplicacionAnticipo;
import com.krazystore.krazystore.Repository.AplicacionAnticiposRepository;
import com.krazystore.krazystore.Service.AplicacionAnticipoService;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class AplicacionAnticipoServiceImpl implements  AplicacionAnticipoService{

    private final ApplicationEventPublisher eventPublisher;
    public AplicacionAnticipoServiceImpl(com.krazystore.krazystore.Repository.AplicacionAnticiposRepository aplicacionAnticipoRepository, org.springframework.context.ApplicationEventPublisher eventPublisher) {
        this.aplicacionAnticipoRepository = aplicacionAnticipoRepository;
        this.eventPublisher = eventPublisher;
    }
    private final AplicacionAnticiposRepository aplicacionAnticipoRepository;

    @Override
    public List<AplicacionAnticipo> getAnticiposAplicarByIdPedido(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<AplicacionAnticipo> saveAnticiposAplicados(List<AplicacionAnticipo> anticiposAplicados) {
        aplicacionAnticipoRepository.saveAll(anticiposAplicados);
        actualizarSaldoAnticipo(anticiposAplicados, TipoEvento.ANTICIPO_APLICADO);
        return anticiposAplicados;
    }
    
    @Transactional
    @Override
    public void deleteAnticiposAplicadosByIdMovimiento(Long id) {
        List<AplicacionAnticipo> anticiposAplicados = aplicacionAnticipoRepository.findAnticiposAplicadosByIdMovimiento(id);
        actualizarSaldoAnticipo(anticiposAplicados, TipoEvento.ANTICIPO_APLICADO_ELIMINADO);
        aplicacionAnticipoRepository.deleteAnticiposAplicadosByIdMovimiento(id);
        
    }
    
    public void actualizarSaldoAnticipo(List<AplicacionAnticipo> anticiposAplicados, TipoEvento tipoEvento) {
        // Publicar el evento
        AplicacionAnticiposEvent evento = new AplicacionAnticiposEvent(anticiposAplicados, tipoEvento);
        eventPublisher.publishEvent(evento);
    }
}
