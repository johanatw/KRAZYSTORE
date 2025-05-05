/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "formas_cobro")
public class FormaCobroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column
    private Long importe;
    @ManyToOne
    @JoinColumn(name = "id_medio_pago")
    private MedioPagoEntity medio;
    @ManyToOne
    @JoinColumn(name = "id_movimiento")
    private MovimientoEntity movimiento;

    public FormaCobroEntity() {
    }

    public FormaCobroEntity(Long id, Long importe, MedioPagoEntity medio, MovimientoEntity movimiento) {
        this.id = id;
        this.importe = importe;
        this.medio = medio;
        this.movimiento = movimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImporte() {
        return importe;
    }

    public void setImporte(Long importe) {
        this.importe = importe;
    }

    public MedioPagoEntity getMedio() {
        return medio;
    }

    public void setMedio(MedioPagoEntity medio) {
        this.medio = medio;
    }

    public MovimientoEntity getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoEntity movimiento) {
        this.movimiento = movimiento;
    }
    
    
}
