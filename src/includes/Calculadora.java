/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package includes;

/**
* Simple Calculator
*
* Developed by Marioly Garza
*
*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculadora extends SuperFrame implements ActionListener, KeyListener   {

    boolean nuevo = true;
    float   resultado_total = 0.0f;
    String  ultimo = "=";
    Label pantalla = null;
    Button b;
    JPanel panel, panel2;

   /* Boton1.addActionListener(this); //botones
frame.addFocusListener(this); //añado el foco
frame.addKeyListener(this); //añado el keylistene */


    public Calculadora() {
        super("Calculadora");

        setSize(200, 200);

        Pantalla();

        Teclado();

        addKeyListener( this);
   }

    public void keyPressed(KeyEvent evt) {

int key = evt.getKeyCode(); // obtengo tecla pulsada

if (key == KeyEvent.VK_F1) {}
    }



   private void Pantalla() {

        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        pantalla = new Label();

        pantalla.setText("0");
        pantalla.setAlignment(Label.RIGHT);
        pantalla.setForeground(Color.black);
        pantalla.setBackground(Color.white);

        panel.add(pantalla);
        add("North", panel);
   }

    public void Teclado () {
        panel2 = new JPanel();
        panel2.setLayout(new GridLayout(4, 4));

        addBoton("7", Color.blue);
        addBoton("8", Color.blue);
        addBoton("9", Color.blue);
        addBoton("/", Color.red);
        addBoton("C", Color.red);

        addBoton("4", Color.blue);
        addBoton("5", Color.blue);
        addBoton("6", Color.blue);
        addBoton("*", Color.red);
        addBoton("sqrt", Color.red);

        addBoton("1", Color.blue);
        addBoton("2", Color.blue);
        addBoton("3", Color.blue);
        addBoton("-", Color.red);
        addBoton("AC", Color.red);

        addBoton("0", Color.blue);
        addBoton("+/-", Color.red);
        addBoton(".", Color.red);
        addBoton("+", Color.red);
        addBoton("=", Color.red);

        add("Center", panel2);
  }

  private void addBoton(String n, Color color) {
       b = new Button(n);

       b.setForeground(color);

       panel2.add(b);

       b.addActionListener( this );
  }


  public void actionPerformed(ActionEvent event) {

        String digit   = event.getActionCommand();
        String s     = pantalla.getText();

        // Logic based in a source of Santiago PavÃ³n

        float  valor = 0;
        try {
            valor = new Float(s).floatValue();
        } catch (Exception e) {
            if (!digit.equals("C")) return;
        }

        if ("0123456789".indexOf(digit) != -1) {

            if (nuevo) {
                nuevo = false;
                pantalla.setText(digit);
            } else {
                pantalla.setText(s + digit);
            }

        } else if (digit.equals(".")) {

            if (nuevo) {
                nuevo = false;
                pantalla.setText("0.");
            } else {
                pantalla.setText(s + digit);
            }

        } else if (digit.equals("sqrt")) {

            valor = (float)Math.sqrt(valor);
            pantalla.setText(String.valueOf(valor));
            nuevo = true;

        } else if (digit.equals("+/-")) {

            valor = -valor;
            pantalla.setText(String.valueOf(valor));
            nuevo = true;

        } else if (digit.equals("C")) {

            resultado_total  = 0;
            pantalla.setText("0");
            ultimo = "=";
            nuevo = true;

        } else {

            char c = ultimo.charAt(0);

            switch (c) {
                case '=': resultado_total  = valor; break;
                case '+': resultado_total += valor; break;
                case '-': resultado_total -= valor; break;
                case '*': resultado_total *= valor; break;
                case '/': resultado_total /= valor; break;
            }
      ultimo = digit;
      nuevo = true;
      pantalla.setText(String.valueOf(resultado_total));
  }
}

    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}