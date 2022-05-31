/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.CorreoElectronicoJpaController;
import controladores.ListaPrecioJpaController;
import controladores.LocalidadJpaController;
import controladores.ProvinciaJpaController;
import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.cliente.Organismo;
import entidades.localidad.Localidad;
import entidades.localidad.Provincia;
import entidades.persona.CorreoElectronico;
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
public class CorreoElectronicoFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    CorreoElectronicoJpaController correoElectronicoJpaController = new CorreoElectronicoJpaController(emf);

    private static CorreoElectronicoFacade instance = null;

    protected CorreoElectronicoFacade() {
    }

    public static CorreoElectronicoFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new CorreoElectronicoFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(CorreoElectronico correoElectronico) {
        new CorreoElectronicoJpaController(emf).create(correoElectronico);
    }

    public CorreoElectronico buscar(Long id) {
        return new CorreoElectronicoJpaController(emf).findCorreoElectronico(id);
    }

    public void modificar(CorreoElectronico correoElectronico) {
        try {
            new CorreoElectronicoJpaController(emf).edit(correoElectronico);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CorreoElectronicoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CorreoElectronicoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new CorreoElectronicoJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CorreoElectronicoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CorreoElectronico> getTodos() {
        return new CorreoElectronicoJpaController(emf).findCorreoElectronicoEntities();
    }

    public List<CorreoElectronico> buscarPorDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT c FROM CorreoElectronico c WHERE c.direccion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion + "%");
        return qu.getResultList();
    }



}
