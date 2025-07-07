/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import jakarta.persistence.*;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "formas_pago")
public class FormaPagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column
    private Long importe;
    @ManyToOne
    @JoinColumn(name = "id_medio_pago")
    private MedioPagoEntity medioPago;
    @ManyToOne
    @JoinColumn(name = "id_movimiento")
    private MovimientoEntity movimiento;

    public FormaPagoEntity() {
    }

    public FormaPagoEntity(Long id, Long importe, MedioPagoEntity medio, MovimientoEntity movimiento) {
        this.id = id;
        this.importe = importe;
        this.medioPago = medio;
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

    public MedioPagoEntity getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MedioPagoEntity medioPago) {
        this.medioPago = medioPago;
    }

    public MovimientoEntity getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoEntity movimiento) {
        this.movimiento = movimiento;
    }

    
    
}
