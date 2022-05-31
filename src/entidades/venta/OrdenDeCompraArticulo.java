/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades.venta;

import entidades.articulo.Articulo;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author franco
 */
@Entity
@Table(name = "orden_compra_articulos")
public class OrdenDeCompraArticulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //** Cantidad o Peso dependiendo del articulo.
    @Column(scale = 3, precision = 12)
    private BigDecimal cantidadPeso;
    //** Precio que se le cobro a la persona, ya con las promociones y descuentos aplicados
    @Column(scale = 3, precision = 12)
    private BigDecimal precio;
    @OneToOne(optional = false)
    private Articulo articulo;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCantidadPeso() {
        return cantidadPeso;
    }

    public void setCantidadPeso(BigDecimal cantidadPeso) {
        this.cantidadPeso = cantidadPeso;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precioPactado) {
        this.precio = precioPactado;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
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
        if (!(object instanceof OrdenDeCompraArticulo)) {
            return false;
        }
        OrdenDeCompraArticulo other = (OrdenDeCompraArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.venta.OrdenDeCompraArticulo[ id=" + id + " ]";
    }
    
}
