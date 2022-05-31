/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ConfiguracionJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Configuracion;
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
public class ConfiguracionFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    private static ConfiguracionFacade instance = null;

    protected ConfiguracionFacade() {
    }

    public static ConfiguracionFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new ConfiguracionFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Configuracion config) {
        new ConfiguracionJpaController(emf).create(config);
    }

    public void alta(String parametro, String valor) {
        Configuracion config = new Configuracion(parametro, valor);
        new ConfiguracionJpaController(emf).create(config);
    }

    public Configuracion buscar(Long id) {
        return new ConfiguracionJpaController(emf).findConfiguracion(id);
    }

    public void modificar(Configuracion config) {
        try {
            new ConfiguracionJpaController(emf).edit(config);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ConfiguracionFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ConfiguracionFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new ConfiguracionJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ConfiguracionFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Configuracion> getTodos() {
        return new ConfiguracionJpaController(emf).findConfiguracionEntities();
    }

    public Configuracion buscar(String parametro) {
        Configuracion configuracion = new Configuracion();
        EntityManager em = emf.createEntityManager();
        Query quConfiguracion = em.createQuery("SELECT c FROM "
                + "Configuracion c WHERE c.parametro='" + parametro + "'");
        List listConfiguracion = quConfiguracion.getResultList();
        if (listConfiguracion.size() > 0) {
            configuracion = (Configuracion) listConfiguracion.get(0);
        }
        return configuracion;
    }
}
