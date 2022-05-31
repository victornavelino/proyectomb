/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testBigdecimal;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nago
 */
public class TestBigDecimal {

    public TestBigDecimal() {
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
        BigDecimal montoventa = new BigDecimal("600");
        BigDecimal montoCobranza = new BigDecimal("500");
        int comparacion=montoCobranza.compareTo(montoventa);
        System.out.println("comparacion: "+comparacion);

    }
}
