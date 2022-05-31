/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balanza;

//import jssc.SerialPort;
//import jssc.SerialPortException;
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
public class SerialPortTest {
    private String defaultPort;

    public SerialPortTest() {
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
//    @Test
//    public void hello() {
//         if (System.getProperty("os.name").contentEquals("Linux")) {
//            //nombre del puerto en linux
//            defaultPort = "/dev/ttyS0";
//        } else {
//            //nombre del puerto en windows
//            defaultPort = "COM1";
//        }
//        SerialPort serialPort = new SerialPort(defaultPort) {
//        };
//        try {
//            serialPort.openPort();//Open serial port
//            serialPort.setParams(9600, 8, 2, 0);//Set params.
//            
//            byte[] buffer = serialPort.readBytes(10);//Read 10 bytes from serial port
//            String readed = new String(buffer).trim().substring(0, 6);
//            System.out.println(serialPort.getPortName() + ": " + readed);
//            serialPort.closePort();//Close serial port
//        } catch (SerialPortException ex) {
//            System.out.println(ex);
//        }
//
//    }
}
