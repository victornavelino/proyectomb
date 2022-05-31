/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.venta;

import entidades.Sucursal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author franco
 */
@Entity
@Table(name = "ventas_cierre")
public class CierreVentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(nullable = false)
    private Sucursal sucursal;
    @Basic(optional = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    private int numeroCierre;
    @Basic(optional = false)
    private int ticketDesde;
    @Basic(optional = false)
    private int ticketHasta;
    @Basic(optional = false)
    private int cantidad;
    @OneToMany(mappedBy = "cierreVentas")
    private List<Venta> ventas;
    @Column(scale = 3, precision = 12)
    private BigDecimal importe;

    public int getNumeroCierre() {
        return numeroCierre;
    }

    public void setNumeroCierre(int numeroCierre) {
        this.numeroCierre = numeroCierre;
    }
    public int getTicketDesde() {
        return ticketDesde;
    }

    public void setTicketDesde(int ticketDesde) {
        this.ticketDesde = ticketDesde;
    }

    public int getTicketHasta() {
        return ticketHasta;
    }

    public void setTicketHasta(int ticketHasta) {
        this.ticketHasta = ticketHasta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
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
        if (!(object instanceof CierreVentas)) {
            return false;
        }
        CierreVentas other = (CierreVentas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.venta.CierrreVentas[ id=" + id + " ]";
    }

}
