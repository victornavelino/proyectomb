    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.caja;

import entidades.cliente.Cliente;
import entidades.venta.Venta;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author root
 */
@Entity
@Table(name = "movimientocaja_cuentacorriente")
public class CuentaCorriente implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Cliente cliente;
    @Column(scale = 3, precision = 12)
    private BigDecimal importeOriginal;
    @Column(scale = 3, precision = 12)
    private BigDecimal importeCtaCte;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    @OneToOne
    private Venta venta;
    @Column(scale = 3, precision = 12)
    private BigDecimal saldo;
    private String observaciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getImporteOriginal() {
        return importeOriginal;
    }

    public void setImporteOriginal(BigDecimal importeOriginal) {
        this.importeOriginal = importeOriginal;
    }

    public BigDecimal getImporteCtaCte() {
        return importeCtaCte;
    }

    public void setImporteCtaCte(BigDecimal importeCtaCte) {
        this.importeCtaCte = importeCtaCte;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    


    @Override
    public String toString() {
        return "entidades.caja.CuentaCorriente[ id=" + id + " ]";
    }
    
}
