/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.venta;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author franco
 */
@Entity
@Table(name = "orden_compra_entrega_parcial")
public class EntregaParcial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(nullable = false)
    @OneToOne
    private OrdenDeCompraArticulo ordenDeCompraArticulo;
    @JoinColumn(nullable = false)
    @OneToOne
    private VentaArticulo ventaArticulo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrdenDeCompraArticulo getOrdenDeCompraArticulo() {
        return ordenDeCompraArticulo;
    }

    public void setOrdenDeCompraArticulo(OrdenDeCompraArticulo ordenDeCompraArticulo) {
        this.ordenDeCompraArticulo = ordenDeCompraArticulo;
    }

    public VentaArticulo getVentaArticulo() {
        return ventaArticulo;
    }

    public void setVentaArticulo(VentaArticulo ventaArticulo) {
        this.ventaArticulo = ventaArticulo;
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
        if (!(object instanceof EntregaParcial)) {
            return false;
        }
        EntregaParcial other = (EntregaParcial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.venta.EntregaParcial[ id=" + id + " ]";
    }

}
