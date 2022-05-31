/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.cliente;

import controladores.ClienteJpaController;
import controladores.TipoDocumentoJpaController;
import entidades.persona.DocumentoIdentidad;
import entidades.persona.Sexo;
import entidades.persona.TipoDocumento;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author franco
 */
public class PersonaTest {

    public PersonaTest() {
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

    /**
     * Test of getApellido method, of class Persona.
     */
    @Test
    public void testGetApellido() {
        System.out.println("getApellido");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU");
        Persona instance = new Persona();
        DocumentoIdentidad di = new DocumentoIdentidad();
        TipoDocumento td = new TipoDocumento();
        td.setDescripcion("DNI");
        new TipoDocumentoJpaController(emf).create(td);
        di.setNumero("30207103");
        di.setTipoDocumento(td);
        instance.setApellido("navelino");
        instance.setNombre("hugo");
        instance.setDocumentoIdentidad(di);
        instance.setCuil("1212121212");
        instance.setSexo(Sexo.MASCULINO);
        new ClienteJpaController(emf).create(instance);
        
    }

}
