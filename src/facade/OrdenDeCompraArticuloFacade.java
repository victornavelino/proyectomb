/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.OrdenDeCompraArticuloJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.venta.OrdenDeCompraArticulo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author hugo
 */
public class OrdenDeCompraArticuloFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    private static OrdenDeCompraArticuloFacade instance = null;

    protected OrdenDeCompraArticuloFacade() {
    }

    public static OrdenDeCompraArticuloFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new OrdenDeCompraArticuloFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(OrdenDeCompraArticulo ordenDeCompraArticulo) {
        new OrdenDeCompraArticuloJpaController(emf).create(ordenDeCompraArticulo);
    }

    public OrdenDeCompraArticulo buscar(Long id) {
        return new OrdenDeCompraArticuloJpaController(emf).findOrdenDeCompraArticulo(id);   
    }

    public void modificar(OrdenDeCompraArticulo ordenDeCompraArticulo) {
        try {
            new OrdenDeCompraArticuloJpaController(emf).edit(ordenDeCompraArticulo);
        } catch (Exception ex) {
            Logger.getLogger(OrdenDeCompraArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new OrdenDeCompraArticuloJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(OrdenDeCompraArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<OrdenDeCompraArticulo> getTodos() {
        return new OrdenDeCompraArticuloJpaController(emf).findOrdenDeCompraArticuloEntities();
    }
}
