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
import static org.eclipse.persistence.config.ResultType.Value;

/**
 *
 * @author root
 */
@Entity
@Table(name="caja_cupontarjeta")
public class CuponTarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Cliente cliente;
    @OneToOne
    private PlanTarjeta planTarjeta;
    @Column(scale = 2, precision = 12)
    private BigDecimal importeCupon;
    private int nroCupon;
    private String lote;
    @Column(scale = 2, precision = 12)
    private BigDecimal recargo;
    @Column(scale = 2, precision = 12)
    private BigDecimal importeCuponConRecargo;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    @OneToOne
    private Venta venta;
    private String observaciones;
    @Column(columnDefinition = "boolean default false")
    private Boolean esCuentaCorriente = false;

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

    public PlanTarjeta getPlanTarjeta() {
        return planTarjeta;
    }

    public void setPlanTarjeta(PlanTarjeta planTarjeta) {
        this.planTarjeta = planTarjeta;
    }

    public BigDecimal getImporteCupon() {
        return importeCupon;
    }

    public void setImporteCupon(BigDecimal importeCupon) {
        this.importeCupon = importeCupon;
    }

    public int getNroCupon() {
        return nroCupon;
    }

    public void setNroCupon(int nroCupon) {
        this.nroCupon = nroCupon;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getImporteCuponConRecargo() {
        return importeCuponConRecargo;
    }

    public void setImporteCuponConRecargo(BigDecimal importeCuponConRecargo) {
        this.importeCuponConRecargo = importeCuponConRecargo;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getEsCuentaCorriente() {
        return esCuentaCorriente;
    }

    public void setEsCuentaCorriente(Boolean esCuentaCorriente) {
        this.esCuentaCorriente = esCuentaCorriente;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuponTarjeta)) {
            return false;
        }
        CuponTarjeta other = (CuponTarjeta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.caja.CuponTarjeta[ id=" + id + " ]";
    }

}
