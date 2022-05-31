/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.CuponTarjetaJpaController;
import controladores.GrupoJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.caja.CuponTarjeta;
import entidades.cliente.Cliente;
import entidades.persona.TipoTelefono;
import entidades.usuario.Grupo;
import entidades.venta.Venta;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 *
 * @author carlos
 */
public class CuponTarjetaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    private static CuponTarjetaFacade instance = null;

    protected CuponTarjetaFacade() {
    }

    public static CuponTarjetaFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new CuponTarjetaFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(CuponTarjeta cuponTarjeta) {
        new CuponTarjetaJpaController(emf).create(cuponTarjeta);
    }

    public CuponTarjeta buscar(long id) {
        return new CuponTarjetaJpaController(emf).findCuponTarjeta(id);

    }

    public void eliminar(long id) {
        try {
            new CuponTarjetaJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CuponTarjetaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void editar(CuponTarjeta cuponTarjeta) {
        try {
            new CuponTarjetaJpaController(emf).edit(cuponTarjeta);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CuponTarjetaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CuponTarjetaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CuponTarjeta> listarCuponesTarjeta(String text) {
        Query quBuscar = em.createQuery("SELECT c FROM CuponTarjeta c");
        return quBuscar.getResultList();
    }

    public List<CuponTarjeta> listarCuponesTarjetaCliente(Cliente cliente) {
        Query quBuscar = em.createQuery("SELECT c FROM CuponTarjeta c where c.cliente=:cliente");
        quBuscar.setParameter("cliente", cliente);
        return quBuscar.getResultList();
    }

    public List<CuponTarjeta> listarCuponesTarjetaVenta(Venta venta) {
        Query quBuscar = em.createQuery("SELECT c FROM CuponTarjeta c WHERE c.venta=:venta");
        quBuscar.setParameter("venta", venta);
        return quBuscar.getResultList();
    }

    public List<CuponTarjeta> listarCuponesTarjeta(String apellido, Date fechaInicio) {
        Query quBuscar = em.createQuery("SELECT c FROM CuponTarjeta c WHERE c.cliente.id IN (SELECT p.id from Persona p where p.apellido LIKE '%" + apellido + "%') AND c.fecha >=:fechaInicio");
        //quBuscar.setParameter("apellido","%" + apellido + "%");
        quBuscar.setParameter("fechaInicio", fechaInicio);
        return quBuscar.getResultList();
    }

    public List<CuponTarjeta> listarCuponesTarjetaCaja(Date fechaInicio) {
        Query quBuscar = em.createQuery("SELECT c FROM CuponTarjeta c WHERE c.fecha >=:fechaInicio");
        //quBuscar.setParameter("apellido","%" + apellido + "%");
        quBuscar.setParameter("fechaInicio", fechaInicio);
        return quBuscar.getResultList();
    }

    public List<CuponTarjeta> getCuponesTarjetaCtaCteCaja(Date fechaInicio) {
        Query quBuscar = em.createQuery("SELECT c FROM CuponTarjeta c WHERE  c.fecha >=:fechaInicio AND c.esCuentaCorriente=TRUE ");
        quBuscar.setParameter("fechaInicio", fechaInicio);
        return quBuscar.getResultList();
    }

}
