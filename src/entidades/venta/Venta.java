/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.venta;

import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.caja.CobroVenta;
import entidades.cliente.Cliente;
import entidades.usuario.Usuario;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author diego
 */
@Entity
@Table(name = "venta")
public class Venta implements Serializable, Comparator {
    @OneToOne
    private CobroVenta cobroVenta;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<VentaArticulo> ventasArticulos;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    //Monto total de la venta realizada (monto real)
    @Basic(optional = false)
    @Column(scale = 3, precision = 12)
    private BigDecimal monto;
    private int numeroTicket;
    //** Importe del descuento que se aplica al momento de la venta
    @Column(scale = 3, precision = 12)
    private BigDecimal descuento;
    private boolean anulado;
    @OneToOne
    @JoinColumn(nullable = false)
    private Sucursal sucursal;
    @JoinColumn(nullable = false)
    private String cliente;
    private String dniCliente;
    private boolean esPersona;
    @OneToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;
    @ManyToOne
    private CierreVentas cierreVentas;

    public CobroVenta getCobroVenta() {
        return cobroVenta;
    }

    public void setCobroVenta(CobroVenta cobroVenta) {
        this.cobroVenta = cobroVenta;
    }

    public CierreVentas getCierreVentas() {
        return cierreVentas;
    }

    public void setCierreVentas(CierreVentas cierreVentas) {
        this.cierreVentas = cierreVentas;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public List<VentaArticulo> getVentasArticulos() {
        return ventasArticulos;
    }

    public void setVentasArticulos(List<VentaArticulo> ventasArticulos) {
        this.ventasArticulos = ventasArticulos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public boolean isAnulado() {
        return anulado;
    }

    public void setAnulado(boolean anulado) {
        this.anulado = anulado;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public boolean isEsPersona() {
        return esPersona;
    }

    public void setEsPersona(boolean esPersona) {
        this.esPersona = esPersona;
    }
    
    public int getNumeroTicket() {
        return numeroTicket;
    }

    public void setNumeroTicket(int numeroTicket) {
        this.numeroTicket = numeroTicket;
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
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.venta.Venta[ id=" + id + " ]";
    }
        @Override
    public int compare(Object o1, Object o2) {
       VentaArticulo vc1 = (VentaArticulo) o1;
        VentaArticulo vc2 = (VentaArticulo) o2;
        //los dos objetos de abajo estan creados solo para evitar el error HUGO
        Articulo ar1 = new Articulo();
        Articulo ar2 = new Articulo();
        return ar1.getSubCategoria().getCategoria().getTipoIva().getDescripcion().compareTo(ar2.getSubCategoria().getCategoria().getTipoIva().getDescripcion());
    }
}
