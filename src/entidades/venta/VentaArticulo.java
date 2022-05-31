/*
 * To change this template, choose Tools | Templates
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author diego
 */
@Entity
@Table(name = "venta_articulo")
public class VentaArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //** Precio total (precio x cantidad)
    @Column(scale = 3, precision = 12)
    private BigDecimal precio;
    //** Cantidad o Peso dependiendo del articulo.
    @Column(scale = 3, precision = 12)
    private BigDecimal cantidadPeso;
    //** Precio que se le cobro a la persona, ya con las promociones y descuentos aplicados
    @Column(scale = 3, precision = 12)
    private BigDecimal precioPromocion;
    //** Precio original que tiene el articulo en la lista común.
    @Column(scale = 3, precision = 12)
    private BigDecimal precioUnitario;

    //** Articulo vendido(modificado astring)
    private String articuloDescripcion;
    private String articuloCodigo;

    public String getArticuloCodigo() {
        return articuloCodigo;
    }

    public void setArticuloCodigo(String articuloCodigo) {
        this.articuloCodigo = articuloCodigo;
    }
    

    public String getArticuloDescripcion() {
        return articuloDescripcion;
    }

    public void setArticuloDescripcion(String articuloDescripcion) {
        this.articuloDescripcion = articuloDescripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getCantidadPeso() {
        return cantidadPeso;
    }

    public void setCantidadPeso(BigDecimal cantidadPeso) {
        this.cantidadPeso = cantidadPeso;
    }

    public BigDecimal getPrecioPromocion() {
        return precioPromocion;
    }

    public void setPrecioPromocion(BigDecimal precioPromocion) {
        this.precioPromocion = precioPromocion;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
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
        if (!(object instanceof VentaArticulo)) {
            return false;
        }
        VentaArticulo other = (VentaArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.venta.VentaArticulo[ id=" + id + " ]";
    }

}
