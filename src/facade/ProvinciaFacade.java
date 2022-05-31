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
import controladores.ProvinciaJpaController;
import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.localidad.Localidad;
import entidades.localidad.Provincia;
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
public class ProvinciaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    ProvinciaJpaController provinciaJpaController = new ProvinciaJpaController(emf);

    private static ProvinciaFacade instance = null;

    protected ProvinciaFacade() {
    }

    public static ProvinciaFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new ProvinciaFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Provincia provincia) {
        new ProvinciaJpaController(emf).create(provincia);
    }

    public Provincia buscar(Long id) {
        return new ProvinciaJpaController(emf).findProvincia(id);   
    }

    public void modificar(Provincia provincia) {
        try {
            new ProvinciaJpaController(emf).edit(provincia);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ProvinciaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ProvinciaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new ProvinciaJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ProvinciaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Provincia> getTodos() {
        return new ProvinciaJpaController(emf).findProvinciaEntities();
    }

    public List<Provincia> buscarPorDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT p FROM Provincia p WHERE p.descripcion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion + "%");
        return qu.getResultList();
    }
    
    public List<Provincia> listarTodosProvinciaOrdenados() {
        Query quBuscar = em.createQuery("SELECT p FROM Provincia p ORDER BY p.descripcion");
        return quBuscar.getResultList();
    }

}
