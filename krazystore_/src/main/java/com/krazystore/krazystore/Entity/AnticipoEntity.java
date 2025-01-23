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
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *
 * @author HP
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="anticipos")
public class AnticipoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "tipo_pedido")
    private char tipoPedido;
    @Column
    private int total;
    @Column
    private Date fecha;
    @Column(name = "id_pedido")
    private Long idPedido;
    @Column
    private int saldo;
    @Column
    private int reembolsado=0;
    @Column
    private int utilizado=0;

    public int getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(int utilizado) {
        this.utilizado = utilizado;
    }
    
    

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getReembolsado() {
        return reembolsado;
    }

    public void setReembolsado(int reembolsado) {
        this.reembolsado = reembolsado;
    }

    public Long getId() {
        return id;
    }

    public char getTipoPedido() {
        return tipoPedido;
    }

    public void setTipoPedido(char tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    
    
    
}
