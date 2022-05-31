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
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.localidad.Departamento;
import entidades.localidad.Localidad;
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
public class LocalidadFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    LocalidadJpaController localidadJpaController = new LocalidadJpaController(emf);

    private static LocalidadFacade instance = null;

    protected LocalidadFacade() {
    }

    public static LocalidadFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new LocalidadFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Localidad localidad) {
        new LocalidadJpaController(emf).create(localidad);
    }

    public Localidad buscar(Long id) {
        return new LocalidadJpaController(emf).findLocalidad(id);   
    }

    public void modificar(Localidad localidad) {
        try {
            new LocalidadJpaController(emf).edit(localidad);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(LocalidadFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LocalidadFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new LocalidadJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(LocalidadFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Localidad> getTodos() {
        return new LocalidadJpaController(emf).findLocalidadEntities();
    }

    public List<Localidad> buscarPorDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT l FROM Localidad l WHERE l.descripcion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion + "%");
        return qu.getResultList();
    }
    
    public List<Localidad> listarTodosLocalidadOrdenados(Departamento departamento) {
        Query quBuscar = em.createQuery("SELECT l FROM Localidad l WHERE l.departamento "
                + "= :departamento ORDER BY l.descripcion");
        quBuscar.setParameter("departamento", departamento);
        return quBuscar.getResultList();
    }

}
