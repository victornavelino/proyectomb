/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.DomicilioJpaController;
import controladores.ListaPrecioJpaController;
import controladores.LocalidadJpaController;
import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.localidad.Localidad;
import entidades.persona.Domicilio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author hugo
 */
public class DomicilioFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    DomicilioJpaController domicilioJpaController = new DomicilioJpaController(emf);

    private static DomicilioFacade instance = null;

    protected DomicilioFacade() {
    }

    public static DomicilioFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new DomicilioFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Domicilio domicilio) {
        new DomicilioJpaController(emf).create(domicilio);
    }

    public Domicilio buscar(Long id) {
        return new DomicilioJpaController(emf).findDomicilio(id);   
    }

    public void modificar(Domicilio domicilio) {
        try {
            new DomicilioJpaController(emf).edit(domicilio);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(DomicilioFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DomicilioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new DomicilioJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(DomicilioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Domicilio> getTodos() {
        return new DomicilioJpaController(emf).findDomicilioEntities();
    }

}
