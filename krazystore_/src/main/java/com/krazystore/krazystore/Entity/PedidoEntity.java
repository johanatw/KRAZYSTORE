/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import com.krazystore.krazystore.DTO.PedidoDTO;
import jakarta.persistence.*;

import java.util.Date;

/**
 *
 * @author HP
 */
@NamedNativeQuery(name= "PedidoEntity.findAllPedidos",
        query="SELECT p.id_pedido as id, p.fecha as fecha, p.total as total, COALESCE(e.costo, 0) as costoEnvio, c.nombre || ' ' || COALESCE(c.apellido,'') as cliente, c.telefono as telefono, s.descripcion as estadoPedido, r.descripcion as estadoPago, sum(CASE WHEN o.pre_venta THEN t.cantidad ELSE 0 END) as cantPreVenta, sum(t.cantidad) as totalItems FROM pedidos p LEFT JOIN personas c ON c.id = p.id_cliente LEFT JOIN costos_envio e ON e.id = p.costo_envio JOIN estados s ON s.id = p.estado_pedido JOIN estados r ON r.id = p.estado_pago JOIN detalle_pedidos t ON t.id_pedido = p.id_pedido JOIN productos o ON o.id = t.id_producto GROUP BY p.id_pedido, e.costo, c.nombre,c.apellido,c.telefono,s.descripcion,r.descripcion order by id DESC",
        resultSetMapping = "Mapping.PedidoDTO")
@SqlResultSetMapping(name="Mapping.PedidoDTO",
        classes = @ConstructorResult(targetClass = PedidoDTO.class,
                columns = {@ColumnResult(name = "id"),
                @ColumnResult(name = "fecha"),
                @ColumnResult(name = "total"),
                @ColumnResult(name = "costoEnvio"),
                @ColumnResult(name = "cliente"),
                @ColumnResult(name = "telefono"),
                @ColumnResult(name = "estadoPedido"),
                @ColumnResult(name = "estadoPago"),
                @ColumnResult(name = "cantPreVenta"),
                @ColumnResult(name = "totalItems")}))



@Entity
@Table(name = "pedidos")
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;
    @Column
    private Date fecha;
    @Column
    private int montoIva;
    @Column
    private int total;
    @Column
    private int pagado=0;
    @ManyToOne
    @JoinColumn(name = "costo_envio")
    private CostoEnvioEntity costoEnvio;
  
    @ManyToOne
    @JoinColumn(name = "estado_pedido")
    private EstadoEntity estadoPedido;
    @ManyToOne
    @JoinColumn(name = "estado_pago")
    private EstadoEntity estadoPago;
    @ManyToOne
    @JoinColumn(name = "forma_pago")
    private FormaPagoEntity formaPago;
    @ManyToOne
    @JoinColumn(name = "modo_entrega")
    private ModoEntregaEntity modoEntrega;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private PersonaEntity cliente;
    @ManyToOne
    @JoinColumn(name = "id_direccion_envio")
    private DireccionEntity direccionEnvio;

    public PedidoEntity() {
    }

    public PedidoEntity(Long id, Date fecha, int montoIva, int total, CostoEnvioEntity costoEnvio, EstadoEntity estadoPedido, EstadoEntity estadoPago, FormaPagoEntity formaPago, ModoEntregaEntity modoEntrega, PersonaEntity cliente) {
        this.id = id;
        this.fecha = fecha;
        this.montoIva = montoIva;
        this.total = total;
        this.costoEnvio = costoEnvio;
        this.estadoPedido = estadoPedido;
        this.estadoPago = estadoPago;
        this.formaPago = formaPago;
        this.modoEntrega = modoEntrega;
        this.cliente = cliente;

    }

    public DireccionEntity getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(DireccionEntity direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    
    public int getPagado() {
        return pagado;
    }

    public void setPagado(int pagado) {
        this.pagado = pagado;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
     

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(int montoIva) {
        this.montoIva = montoIva;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public CostoEnvioEntity getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(CostoEnvioEntity costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public EstadoEntity getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoEntity estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public EstadoEntity getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoEntity estadoPago) {
        this.estadoPago = estadoPago;
    }

    public FormaPagoEntity getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPagoEntity formaPago) {
        this.formaPago = formaPago;
    }

    public ModoEntregaEntity getModoEntrega() {
        return modoEntrega;
    }

    public void setModoEntrega(ModoEntregaEntity modoEntrega) {
        this.modoEntrega = modoEntrega;
    }

    public PersonaEntity getCliente() {
        return cliente;
    }

    public void setCliente(PersonaEntity cliente) {
        this.cliente = cliente;
    }


    
    
}
