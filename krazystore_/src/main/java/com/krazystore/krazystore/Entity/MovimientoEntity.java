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
@Table(name = "movimientos")
public class MovimientoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column
    private int monto;
    @Column
    private Date fecha;
    @Column
    private String observacion;
    @Column(name = "nro_documento")
    private String nroDocumento;
    @ManyToOne
    @JoinColumn(name = "id_anticipo")
    private AnticipoEntity anticipo;
    @ManyToOne
    @JoinColumn(name = "id_reembolso_anticipo")
    private ReembolsoAnticipo reembolso;
    @ManyToOne
    @JoinColumn(name = "id_venta")
    private VentaEntity venta;
    @ManyToOne
    @JoinColumn(name = "id_compra")
    private CompraEntity compra;
    @ManyToOne
    @JoinColumn(name = "id_concepto")
    private ConceptoEntity concepto;
    @ManyToOne
    @JoinColumn(name = "id_caja")
    private CajaEntity caja;
    @ManyToOne
    @JoinColumn(name="id_cliente")
    private ClienteEntity cliente;
    @ManyToOne
    @JoinColumn(name="id_proveedor")
    private ProveedorEntity proveedor;
    

    @Column
    private long ingreso=0;
    @Column
    private long egreso=0;
    @Column
    private char estado='C';

    public MovimientoEntity() {
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public CompraEntity getCompra() {
        return compra;
    }

    public void setCompra(CompraEntity compra) {
        this.compra = compra;
    }

   
    

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

     

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public CajaEntity getCaja() {
        return caja;
    }

    public void setCaja(CajaEntity caja) {
        this.caja = caja;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public VentaEntity getVenta() {
        return venta;
    }

    public void setVenta(VentaEntity venta) {
        this.venta = venta;
    }
    
    

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }



    public AnticipoEntity getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(AnticipoEntity anticipo) {
        this.anticipo = anticipo;
    }

    public ReembolsoAnticipo getReembolso() {
        return reembolso;
    }

    public void setReembolso(ReembolsoAnticipo reembolso) {
        this.reembolso = reembolso;
    }

    public long getIngreso() {
        return ingreso;
    }

    public void setIngreso(long ingreso) {
        this.ingreso = ingreso;
    }

    public long getEgreso() {
        return egreso;
    }

    public void setEgreso(long egreso) {
        this.egreso = egreso;
    }

    public ConceptoEntity getConcepto() {
        return concepto;
    }

    public void setConcepto(ConceptoEntity concepto) {
        this.concepto = concepto;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public ProveedorEntity getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorEntity proveedor) {
        this.proveedor = proveedor;
    }
    
    

}
