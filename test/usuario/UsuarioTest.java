/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuario;

import entidades.usuario.Grupo;
import entidades.usuario.Usuario;
import facade.GrupoFacade;
import facade.UsuarioFacade;
import includes.SHA1;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author franco
 */
public class UsuarioTest {

    public UsuarioTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() {
        Grupo grupo = new Grupo();
        grupo.setDescripcion("Administradores");
        grupo.setNombre("administrador");
        GrupoFacade.getInstance().alta(grupo);
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("admin");
        usuario.setNombreUsuario("admin");
        usuario.setGrupo(grupo);
        usuario.setContrasena("adminadmin");
        SHA1 sha1 = new SHA1();
        try {
            usuario.setContrasena(sha1.getHash(usuario.getContrasena()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        UsuarioFacade.getInstance().alta(usuario);

    }
}
