/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloInventarioJpaController;
import controladores.DepartamentoJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.inventario.ArticuloInventario;
import entidades.inventario.Inventario;
import entidades.localidad.Departamento;
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
public class ArticuloInventarioFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    ArticuloInventarioJpaController articuloInventarioJpaController = new ArticuloInventarioJpaController(emf);

    private static ArticuloInventarioFacade instance = null;

    protected ArticuloInventarioFacade() {
    }

    public static ArticuloInventarioFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new ArticuloInventarioFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(ArticuloInventario articuloInventario) {
        new ArticuloInventarioJpaController(emf).create(articuloInventario);
    }

    public ArticuloInventario buscar(Long id) {
        return new ArticuloInventarioJpaController(emf).findArticuloInventario(id);   
    }

    public void modificar(ArticuloInventario articuloInventario) {
        try {
            new ArticuloInventarioJpaController(emf).edit(articuloInventario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ArticuloInventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ArticuloInventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new ArticuloInventarioJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ArticuloInventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<ArticuloInventario> getTodos() {
        return new ArticuloInventarioJpaController(emf).findArticuloInventarioEntities();
    }
    /*public List<ArticuloInventario> buscarPorInventario(Long id) {
        Query qu = em.createQuery("SELECT i FROM Inventario i WHERE i.sucursal =:sucursal");
        qu.setParameter("sucursal", sucursal);
        return qu.getResultList();
    }*/
}
