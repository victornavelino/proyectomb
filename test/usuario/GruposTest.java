/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuario;

import entidades.usuario.Grupo;
import facade.GrupoFacade;
import javax.swing.JOptionPane;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author hugo
 */
public class GruposTest {

    public GruposTest() {
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
    public void crearGrupos() {
     //Este metodo crea los grupos segun 
     //los que se asignan los permisos de menu
        Grupo grupo = new Grupo();
        grupo.setDescripcion("Sucursales");
        grupo.setNombre("sucursal");
        GrupoFacade.getInstance().alta(grupo);
        grupo.setDescripcion("Central");
        grupo.setNombre("central");
        GrupoFacade.getInstance().alta(grupo);
        JOptionPane.showMessageDialog(null, "se crearon los grupos: 'central' y 'sucursal'");
    }
}
