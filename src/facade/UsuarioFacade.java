/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.UsuarioJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.usuario.Usuario;
import includes.Comunes;
import includes.SHA1;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;

/**
 *
 * @author carlos
 */
public class UsuarioFacade {

    //EntityManagerFactory emf;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    UsuarioJpaController usuarioJpa;

    private static UsuarioFacade instance = null;

    protected UsuarioFacade() {
    }

    public static UsuarioFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new UsuarioFacade();
        }
    }
//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

//    public UsuarioFacade() {
//        try {
//            emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
//            usuarioJpa = new UsuarioJpaController(emf);
//            em = emf.createEntityManager();
//            sha1 = new SHA1();
//        } catch (javax.persistence.PersistenceException ex) {
//            Comunes.mensajeError(ex, "Error Conectado a la Base de Datos");
//        } catch (Exception ex) {
//            Comunes.mensajeError(ex, "Error");
//        }
//    }
    public void alta(Usuario usuario) {
        new UsuarioJpaController(emf).create(usuario);
    }

    public void modificar(Usuario usuario) {
        try {
            new UsuarioJpaController(emf).edit(usuario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void eliminar(Long id) {
        try {
            new UsuarioJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Usuario buscar(Long id) {
        return new UsuarioJpaController(emf).findUsuario(id);
    }

    public Usuario validar(Usuario usuariop) {
        Usuario usuario = new Usuario();
        EntityManager em = emf.createEntityManager();
        List listUsuarios;
        SHA1 sha1= new SHA1();
        try {
            usuariop.setContrasena(sha1.getHash(usuariop.getContrasena()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        Query q = em.createQuery("SELECT u FROM "
                + "Usuario u WHERE u.nombreUsuario = '"
                + usuariop.getNombreUsuario() + "' AND u.contrasena = '"
                + usuariop.getContrasena() + "'");
        listUsuarios = q.getResultList();
        if (listUsuarios.size() > 0) {
            usuario = (Usuario) listUsuarios.get(0);
        }
        return usuario;
    }

    public List<Usuario> listarUsuarios(String filtro) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.nombreCompleto LIKE '%" + filtro + "%'");
        em.getEntityManagerFactory().getCache().evictAll();
        List<Usuario> listUsuario = q.getResultList();
        return listUsuario;
    }

    public List<Usuario> listarTodosUsuarios() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT u FROM Usuario u ORDER BY u.nombreUsuario");
        List<Usuario> listUsuario = q.getResultList();
        return listUsuario;
    }

    public Usuario buscarPorNombre(String nombre) {
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT u FROM Usuario u WHERE u.nombreUsuario=:nombre");
        qu.setParameter("nombre", nombre);
        try {
            return (Usuario) qu.getSingleResult();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error recuperando usuario nombre" + e);
            return null;
        }

    }

    public boolean buscarExisteUsuario(String nombre) {
        try {
            EntityManager em = emf.createEntityManager();
            Query qu = em.createQuery("SELECT u FROM Usuario u WHERE u.nombreUsuario=:nombre");
            qu.setParameter("nombre", nombre);
            qu.getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
