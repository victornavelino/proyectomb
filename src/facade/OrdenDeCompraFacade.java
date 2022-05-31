/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.OrdenDeCompraJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.cliente.Cliente;
import entidades.venta.OrdenDeCompra;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;

/**
 *
 * @author hugo
 */
public class OrdenDeCompraFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    private static OrdenDeCompraFacade instance = null;

    protected OrdenDeCompraFacade() {
    }

    public static OrdenDeCompraFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new OrdenDeCompraFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(OrdenDeCompra ordenDeCompra) {
        new OrdenDeCompraJpaController(emf).create(ordenDeCompra);
    }

    public OrdenDeCompra buscar(Long id) {
        return new OrdenDeCompraJpaController(emf).findOrdenDeCompra(id);
    }

    public void modificar(OrdenDeCompra ordenDeCompra) {
        try {
            new OrdenDeCompraJpaController(emf).edit(ordenDeCompra);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(OrdenDeCompraFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OrdenDeCompraFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new OrdenDeCompraJpaController(emf).destroy(id);
        } catch (Exception ex) {
           // Logger.getLogger(OrdenDeCompraFacade.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se puede borrar la orden de compra seleccionada\n "
                    + "Es posible que se hayan realizado entregas");
        }
    }

    public List<OrdenDeCompra> getTodos() {
        return new OrdenDeCompraJpaController(emf).findOrdenDeCompraEntities();
    }

    public List<OrdenDeCompra> listaOrdenDeComprasDescendente() {

        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT v FROM OrdenDeCompra v WHERE v.anulado=false ORDER BY v.numeroOrden DESC");
        return q.getResultList();

    }

    public List<OrdenDeCompra> listaOrdenDeComprasDescendente(Cliente cliente) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT v FROM OrdenDeCompra v WHERE v.anulado=false AND v.cliente=:cliente ORDER BY v.numeroOrden DESC");
        q.setParameter("cliente", cliente);
        return q.getResultList();
    }

    public OrdenDeCompra getOrdenDeCompraNumeroTicket(String numero) {
        EntityManager ema = emf.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT v FROM OrdenDeCompra v WHERE v.numeroOrden='" + numero + "'");
        try {
            return (OrdenDeCompra) quBuscar.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public boolean existeOrdenDeCompraNumeroTicket(String numero) {
        EntityManager ema = emf.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT v FROM OrdenDeCompra v WHERE v.numeroOrden='" + numero + "'");
        try {
            return !quBuscar.getResultList().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public List<OrdenDeCompra> buscarOrdenDeCompraNumeroTicket(String numero) {
        EntityManager ema = emf.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT v FROM OrdenDeCompra v WHERE v.numeroOrden='" + numero + "'");
        try {
            return quBuscar.getResultList();
        } catch (Exception e) {
            return null;
        }

    }

    public List<OrdenDeCompra> buscarOrdenDeCompraNumeroTicketCliente(String numero, Cliente cliente) {
        EntityManager ema = emf.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT v FROM OrdenDeCompra v WHERE v.cliente=:cliente AND v.numeroOrden=:numero");
        quBuscar.setParameter("cliente", cliente);
        quBuscar.setParameter("numero", numero);
        try {
            return quBuscar.getResultList();
        } catch (Exception e) {
            return null;
        }

    }
}
