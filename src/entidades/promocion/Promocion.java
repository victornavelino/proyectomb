/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.promocion;

import entidades.Sucursal;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author diego
 */
@Entity
@Table(name = "promocion")
public class Promocion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic(optional = false)
    private String nombre;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaInicio;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaFin;
    private boolean esPorPrecio;
    private List<DiaSemana> diasSemanas;
    @Column(scale = 2, precision = 12)
    private BigDecimal porcentajeATodos;
    private boolean habilitada;
    private int prioridad;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)   
    private List<PromocionArticulo> promocionesArticulos;
    @OneToOne
    @JoinColumn(nullable = false)
    private Sucursal sucursal;

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public boolean isHabilitada() {
        return habilitada;
    }

    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isEsPorPrecio() {
        return esPorPrecio;
    }

    public void setEsPorPrecio(boolean esPorPrecio) {
        this.esPorPrecio = esPorPrecio;
    }

    public List<DiaSemana> getDiasSemanas() {
        return diasSemanas;
    }

    public void setDiasSemanas(List<DiaSemana> diasSemanas) {
        this.diasSemanas = diasSemanas;
    }

    public List<PromocionArticulo> getPromocionesArticulos() {
        return promocionesArticulos;
    }

    public void setPromocionesArticulos(List<PromocionArticulo> promocionesArticulos) {
        this.promocionesArticulos = promocionesArticulos;
    }

    public BigDecimal getPorcentajeATodos() {
        return porcentajeATodos;
    }

    public void setPorcentajeATodos(BigDecimal porcentajeATodos) {
        this.porcentajeATodos = porcentajeATodos;
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
        if (!(object instanceof Promocion)) {
            return false;
        }
        Promocion other = (Promocion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
