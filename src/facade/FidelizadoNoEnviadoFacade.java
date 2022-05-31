/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ClienteJpaController;
import controladores.FidelizadoNoEnviadoJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.cliente.Cliente;
import entidades.fidelizacion.FidelizadoNoEnviado;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author nago
 */
public class FidelizadoNoEnviadoFacade {

    private static FidelizadoNoEnviadoFacade instance = null;

    protected FidelizadoNoEnviadoFacade() {
    }

    public static FidelizadoNoEnviadoFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new FidelizadoNoEnviadoFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(FidelizadoNoEnviado fidelizadoNoEnviado) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        new FidelizadoNoEnviadoJpaController(emf).create(fidelizadoNoEnviado);
    }

    public FidelizadoNoEnviado buscar(Long id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        return new FidelizadoNoEnviadoJpaController(emf).findFidelizadoNoEnviado(id);
    }

    public void modificar(FidelizadoNoEnviado fidelizadoNoEnviado) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            new FidelizadoNoEnviadoJpaController(emf).edit(fidelizadoNoEnviado);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(FidelizadoNoEnviadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FidelizadoNoEnviadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public void eliminar(long id) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            new FidelizadoNoEnviadoJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(FidelizadoNoEnviadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<FidelizadoNoEnviado> buscarNoEnviados() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT f FROM FidelizadoNoEnviado f where NOT f.enviado");
        return quBuscar.getResultList();
    }

}
