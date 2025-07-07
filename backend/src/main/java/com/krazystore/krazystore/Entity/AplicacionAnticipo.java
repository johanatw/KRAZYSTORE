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
import java.util.Date;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "aplicacion_anticipos")
public class AplicacionAnticipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column
    private Date fecha;
    @Column
    private Long monto;
    @ManyToOne
    @JoinColumn(name = "id_movimiento")
    private MovimientoEntity movimiento;
    @ManyToOne
    @JoinColumn(name = "id_anticipo")
    private AnticipoEntity anticipo;

    public AplicacionAnticipo() {
    }

    public AplicacionAnticipo(Long id, Date fecha, Long monto, MovimientoEntity movimiento, AnticipoEntity anticipo) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
        this.movimiento = movimiento;
        this.anticipo = anticipo;
    }

    public AplicacionAnticipo(AnticipoEntity anticipo) {
        this.monto = (long)anticipo.getSaldo();
        this.anticipo = anticipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMonto() {
        return monto;
    }

    public void setMonto(Long monto) {
        this.monto = monto;
    }

    public MovimientoEntity getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoEntity movimiento) {
        this.movimiento = movimiento;
    }

    public AnticipoEntity getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(AnticipoEntity anticipo) {
        this.anticipo = anticipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
}
