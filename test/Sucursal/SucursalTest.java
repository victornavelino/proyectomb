/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sucursal;

import entidades.Configuracion;
import entidades.Sucursal;
import facade.ConfiguracionFacade;
import facade.SucursalFacade;
import includes.SHA1;
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
 * @author Silvia bue...
 */
public class SucursalTest {

    public SucursalTest() {
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
        if (SucursalFacade.getInstance().getTodos().isEmpty()) {
            Sucursal sucursal = new Sucursal();
            sucursal.setNombre("Central");
            sucursal.setCodigo("1");
            SucursalFacade.getInstance().alta(sucursal);
        }
        Configuracion configSucursal = ConfiguracionFacade.getInstance().buscar("sucursal");
        try {
            boolean empty = configSucursal.getValor().isEmpty();
        } catch (NullPointerException e) {
            ConfiguracionFacade.getInstance().alta("sucursal", "1");
        }
    }
}
