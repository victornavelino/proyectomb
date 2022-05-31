/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.ListaPrecioJpaController;
import controladores.LocalidadJpaController;
import controladores.SucursalJpaController;
import controladores.VentaArticuloJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.localidad.Localidad;
import entidades.venta.VentaArticulo;
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
public class VentaArticuloFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    VentaArticuloJpaController ventaArticuloJpaController = new VentaArticuloJpaController(emf);

    private static VentaArticuloFacade instance = null;

    protected VentaArticuloFacade() {
    }

    public static VentaArticuloFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new VentaArticuloFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(VentaArticulo ventaArticulo) {
        new VentaArticuloJpaController(emf).create(ventaArticulo);
    }

    public VentaArticulo buscar(Long id) {
        return new VentaArticuloJpaController(emf).findVentaArticulo(id);   
    }

    public void modificar(VentaArticulo ventaArticulo) {
        try {
            new VentaArticuloJpaController(emf).edit(ventaArticulo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(VentaArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(VentaArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new VentaArticuloJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(VentaArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<VentaArticulo> getTodos() {
        return new VentaArticuloJpaController(emf).findVentaArticuloEntities();
    }
    

}
