/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.GrupoJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.persona.TipoTelefono;
import entidades.usuario.Grupo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 *
 * @author carlos
 */



public class GrupoFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU",ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    private static GrupoFacade instance = null;

    protected GrupoFacade() {
    }

    public static GrupoFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new GrupoFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Grupo grupo) {
        new GrupoJpaController(emf).create(grupo);
    }

    public Grupo buscar(long id) {
        return new GrupoJpaController(emf).findGrupo(id);

    }

    public void editar(Grupo grupo) {
        try {
            new GrupoJpaController(emf).edit(grupo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(GrupoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GrupoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Grupo> listarGrupos(String text) {
        Query quBuscar = em.createQuery("SELECT g FROM Grupo g WHERE g.nombre LIKE '%"
                + text + "%'");
        return quBuscar.getResultList();
    }

    public List<Grupo> buscar(String descripcion) {
        Query quBuscar = em.createQuery("SELECT g FROM Grupo g WHERE g.descripcion = :descripcion");
        quBuscar.setParameter("descripcion", descripcion);
        return quBuscar.getResultList();
    }

    public List<Grupo> listarGrupos() {
        return new GrupoJpaController(emf).findGrupoEntities();
    }

    public List<Grupo> listarGruposExceptoAdministradores() {
        List<Grupo> grupos = listarGrupos();
        List<Grupo> administradores = buscar("Administradores");
        grupos.removeAll(administradores);
        return grupos;
}
}
