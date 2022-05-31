/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import controladores.ClienteJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.cliente.Cliente;
import entidades.cliente.Organismo;
import entidades.cliente.Persona;
import entidades.persona.CorreoElectronico;
import entidades.venta.Venta;
import includes.Comunes;
import includes.SesionUsuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import vista.frPrincipal;

/**
 *
 * @author hugo
 */
public class ClienteFacade {

    private static ClienteFacade instance = null;

    protected ClienteFacade() {
    }

    public static ClienteFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new ClienteFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Cliente cliente) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        new ClienteJpaController(emf).create(cliente);
    }

    public void altaCentral(Cliente cliente) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        new ClienteJpaController(emf).create(cliente);
    }

    public Cliente buscar(Long id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        return new ClienteJpaController(emf).findCliente(id);
    }

    public void modificar(Cliente cliente) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            new ClienteJpaController(emf).edit(cliente);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClienteFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificarCentral(Cliente cliente) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
            //EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            new ClienteJpaController(emf).edit(cliente);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClienteFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            new ClienteJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarCentral(long id) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
            //EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            new ClienteJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Cliente> getTodos() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscarCliente = ema.createQuery("SELECT c FROM Cliente c ");
        return quBuscarCliente.getResultList();
    }

    public List<Cliente> buscar(String descripcion) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscarCliente = ema.createQuery("SELECT c FROM Cliente c "
                + "WHERE TYPE(c) = Organismo and c.razonSocial like :descripcion ");
        quBuscarCliente.setParameter("descripcion", "%" + descripcion + "%");
        return quBuscarCliente.getResultList();
    }

    public List<Organismo> getOrganismos() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM Cliente c WHERE TYPE(c) = Organismo");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public List<Organismo> getOrganismosCentral() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM Cliente c WHERE TYPE(c) = Organismo");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public List<Persona> getPersonas() {
        /*EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
         EntityManager ema = emfa.createEntityManager();
         Query quBuscar = ema.createQuery("SELECT c FROM Cliente c WHERE TYPE(c) = Persona");
         return quBuscar.getResultList();*/
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Persona p ORDER BY p.apellido");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public List<Persona> getPersonasCentral() {
        /*EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
         EntityManager ema = emfa.createEntityManager();
         Query quBuscar = ema.createQuery("SELECT c FROM Cliente c WHERE TYPE(c) = Persona");
         return quBuscar.getResultList();*/
        EntityManagerFactory emfa = null;
        emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Persona p ORDER BY p.apellido");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public List<Persona> getPersonasXDni(String dni) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Persona p WHERE p.documentoIdentidad.numero=:dni");
        quBuscar.setParameter("dni", dni);
        return quBuscar.getResultList();
    }

    public List<Persona> buscarPersonas(String descripcion) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Persona p WHERE p.documentoIdentidad.numero LIKE :descripcion "
                + "OR p.apellido LIKE :descripcion");
        quBuscar.setParameter("descripcion", "%" + descripcion.toUpperCase() + "%");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public List<Organismo> buscarOrganismos(String descripcion) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT o FROM Organismo o WHERE o.cuit LIKE :descripcion "
                + "OR o.razonSocial LIKE :descripcion");
        quBuscar.setParameter("descripcion", "%" + descripcion.toUpperCase() + "%");
        return quBuscar.getResultList();
    }

    public Persona getPersonaXDni(String dni) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Persona p WHERE p.documentoIdentidad.numero=:dni");
        quBuscar.setParameter("dni", dni);
        quBuscar.setMaxResults(1);
        ema.getEntityManagerFactory().getCache().evictAll();
        try {
            return (Persona) quBuscar.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public boolean getPersonaDni(String dni) {
        boolean flag = false;
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Persona p WHERE p.documentoIdentidad.numero=:dni");
        quBuscar.setParameter("dni", dni);
        try {
            if (quBuscar.getResultList().isEmpty()) {
                flag = false;
            } else {
                flag = true;
            }
            return flag;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Salio por el catch");
            return false;
        }

    }

    public Persona getPersonasXId(Long id) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Persona p WHERE p.id=:id");
        quBuscar.setParameter("id", id);
        quBuscar.setMaxResults(1);

        return (Persona) quBuscar.getSingleResult();
    }

    public Persona getPersonasXIdCentral(Long id) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Persona p WHERE p.id=:id");
        quBuscar.setParameter("id", id);
        quBuscar.setMaxResults(1);

        return (Persona) quBuscar.getSingleResult();
    }

    public Organismo getOrganismoXId(Long id) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT o FROM Organismo o WHERE o.id=:id");
        quBuscar.setParameter("id", id);
        quBuscar.setMaxResults(1);

        return (Organismo) quBuscar.getSingleResult();
    }

    public Organismo getOrganismoXIdCentral(Long id) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT o FROM Organismo o WHERE o.id=:id");
        quBuscar.setParameter("id", id);
        quBuscar.setMaxResults(1);

        return (Organismo) quBuscar.getSingleResult();
    }

    public List<Persona> getPersonasPorApellido(String apellido) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Persona p WHERE p.apellido LIKE :apellido");
        quBuscar.setParameter("apellido", "%" + apellido + "%");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public List<Persona> getPersonasPorApellidoCentral(String apellido) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Persona p WHERE p.apellido LIKE :apellido");
        quBuscar.setParameter("apellido", "%" + apellido + "%");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public boolean buscarDniPersona(String dni) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            EntityManager em = emf.createEntityManager();
            Query qu = em.createQuery("SELECT p FROM Persona p WHERE p.documentoIdentidad.numero=:dni");
            qu.setParameter("dni", dni);
            qu.setMaxResults(1);

            qu.getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean buscarDniPersonaCentral(String dni) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
            //EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            EntityManager em = emf.createEntityManager();
            Query qu = em.createQuery("SELECT p FROM Persona p WHERE p.documentoIdentidad.numero=:dni");
            qu.setParameter("dni", dni);
            qu.setMaxResults(1);

            qu.getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean buscarCuitEmpresa(String cuit) {
        boolean flag = false;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT o FROM Organismo o WHERE o.cuit=:cuit");
        qu.setParameter("cuit", cuit);
        qu.setMaxResults(1);

        try {
            if (qu.getResultList().isEmpty()) {
                flag = false;
            } else {
                flag = true;
            }
            return flag;

        } catch (Exception e) {
            System.out.println("salio x el catch boolean cuit");
            return false;
        }
    }

    public boolean buscarCuitEmpresaCentral(String cuit) {
        boolean flag = false;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT o FROM Organismo o WHERE o.cuit=:cuit");
        qu.setParameter("cuit", cuit);
        qu.setMaxResults(1);

        try {
            if (qu.getResultList().isEmpty()) {
                flag = false;
            } else {
                flag = true;
            }
            return flag;

        } catch (Exception e) {
            System.out.println("salio x el catch boolean cuit");
            return false;
        }
    }

    public Organismo buscarCuitEmpresaObjeto(String cuit) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT o FROM Organismo o WHERE o.cuit=:cuit");
        qu.setParameter("cuit", cuit);
        qu.setMaxResults(1);
        try {
            return (Organismo) qu.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public Organismo buscarCuitEmpresaObjeto(String cuit, EntityManager em) {

        Query qu = em.createQuery("SELECT o FROM Organismo o WHERE o.cuit=:cuit");
        qu.setParameter("cuit", cuit);
        qu.setMaxResults(1);
        try {
            return (Organismo) qu.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public List<Organismo> getOrganismosPorRazonSocial(String razonSocial) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT o FROM Organismo o WHERE o.razonSocial LIKE :razonSocial");
        quBuscar.setParameter("razonSocial", "%" + razonSocial + "%");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public List<Organismo> getOrganismosPorRazonSocialCentral(String razonSocial) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT o FROM Organismo o WHERE o.razonSocial LIKE :razonSocial");
        quBuscar.setParameter("razonSocial", "%" + razonSocial + "%");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public Organismo getOrganismo(String razonSocial) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT o FROM Organismo o WHERE o.razonSocial=:razonSocial");
        qu.setParameter("razonSocial", razonSocial.trim());
        qu.setMaxResults(1);

        try {
            return (Organismo) qu.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public Organismo getOrgMail(String direccion, Organismo organismo) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT o FROM Organismo o, IN (o.correosElectronicos) ce where ce.direccion =:direccion AND o=:organismo");
        qu.setParameter("direccion", direccion);
        qu.setParameter("organismo", organismo);
        qu.setMaxResults(1);

        try {
            return (Organismo) qu.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Cliente buscarTipoCliente(Long id) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscarCliente = ema.createQuery("SELECT c FROM Cliente c WHERE c.id =:id");
        quBuscarCliente.setParameter("id", id);
        return (Cliente) quBuscarCliente.getSingleResult();
    }

    public List<Persona> getPersonasDuplicadas() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("Select MIN(pa.fechaAlta) as fecha From Persona pa where (select COUNT(p.documentoIdentidad.numero) FROM Persona p WHERE p.documentoIdentidad.numero = pa.documentoIdentidad.numero ) > 1 GROUP BY pa.documentoIdentidad.numero");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public void verificarConexion() throws Exception {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosAddClientesPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosAddClientesPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();

    }

    public void probarConexionALaCentral() throws Exception {

        Comunes.ventanaCargando(this, "probarConexion", "Conectando con el servidor", null);
    }

    public void probarConexion() throws JSchException   {
        String usuario = "root";
        String servidor = "latradicioncentral.ddns.net";
        Integer puerto = 22;
        String clave = "latradicion.7319";

        JSch jSSH = new JSch();
        Session session = jSSH.getSession(usuario, servidor, puerto);
        UserInfo ui = new SesionUsuario(clave, null);

        session.setUserInfo(ui);

        session.setPassword(clave);

        session.connect();
        session.disconnect();
        frPrincipal.conectadoALaCentral=true;
        
    }


}
