/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package promocion;

import entidades.articulo.Articulo;
import entidades.promocion.Promocion;
import entidades.promocion.PromocionArticulo;
import facade.ArticuloFacade;
import facade.PromocionFacade;
import facade.SincronizaFacade;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author root
 */
public class PromocionTest {

    public PromocionTest() {
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
//        for (Articulo articulo : ArticuloFacade.getInstance().getTodos()) {
//            List<Promocion> buscarPorPromocionArticulo = PromocionFacade.getInstance().buscarPorPromocionArticulo(articulo);
//            for (Promocion articulo1 :buscarPorPromocionArticulo){
//              System.out.println("Articulo: "+articulo1.getNombre());
//              System.out.println("Articulo: "+articulo1.getPorcentajeATodos());
//            }
//        }
 
//       String backPromocion = "pg_dump -i -h localhost -p 5432 -U postgres -f /home/nago/backfullpromocion.backup carniceria -t promocion_articulo -t promocion -t promocion_promocion_articulo";
//        try {
//            File file = new File("/home/nago/backPromocion_articulo.backup");
//            String ruta=file.getAbsolutePath();
//            //Runtime.getRuntime().exec("psql -h localhost -U postgres -d carniceria -f "+ruta);
//            Runtime.getRuntime().exec("psql -h localhost -U postgres -d carniceria -f /home/nago/backfullpromocion.backup");
//        } catch (IOException ex) {
//            Logger.getLogger(SincronizaFacade.class.getName()).log(Level.SEVERE, null, ex);
//        }
//       File file = new File("/home/nago/backPromocion_articulo.backup");
//       System.out.println(file.exists());
//        System.out.println(file.getAbsolutePath());
  //PromocionFacade.getInstance().borrarPromociones();
  ArticuloFacade.getInstance().borrarArticulos();
  
    }

}
