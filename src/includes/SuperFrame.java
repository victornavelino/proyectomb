/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package includes;

/**
 *
 * @author Administrador
 */
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXDatePicker;

public class SuperFrame extends JFrame implements ActionListener {

    public SuperFrame() throws HeadlessException {
        super();

    }
    private static final String ACTION_CLOSE = "ACTION_CLOSE";

    @Override
    protected JRootPane createRootPane() {
        JRootPane rootPane1 = new JRootPane();
        int menuShortcutKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        KeyStroke w = KeyStroke.getKeyStroke(KeyEvent.VK_W, menuShortcutKey);
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);

        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                if (getFocusOwner() instanceof JButton) {
                    Component boton = getFocusOwner();
                    ((JButton) boton).doClick();
                } else if ((getFocusOwner() instanceof JTextField)
                        || (getFocusOwner() instanceof JTextArea)
                        || (getFocusOwner() instanceof JXDatePicker)
                        || (getFocusOwner() instanceof JFormattedTextField)
                        || (getFocusOwner() instanceof JScrollPane)
                        || (getFocusOwner() instanceof JComboBox)
                        || (getFocusOwner() instanceof JList)
                        || (getFocusOwner() instanceof JTextArea)) {
                    getFocusOwner().transferFocus();
                }

            }
        };
        rootPane1.registerKeyboardAction(this, ACTION_CLOSE, w, JComponent.WHEN_IN_FOCUSED_WINDOW);
        rootPane1.registerKeyboardAction(this, ACTION_CLOSE, esc, JComponent.WHEN_IN_FOCUSED_WINDOW);
        rootPane1.registerKeyboardAction(actionListener, enter, JComponent.WHEN_IN_FOCUSED_WINDOW);
        /*//BLOQUE QUE CONVIERTE TODO EN MAYUSCULAS
         KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

         //y enseguida registramos nuestro dispatcher
         manager.addKeyEventDispatcher(new KeyEventDispatcher(){
         public boolean dispatchKeyEvent(KeyEvent e) {

         //como dije, solo las notificaciones del tipo "typed" son las que actualizan los componentes
         if(e.getID() == KeyEvent.KEY_TYPED){

         //como vamos a convertir todo a mayúsculas, entonces solo checamos si los caracteres son
         //minusculas
         if(e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z'){
         //si lo son, entonces lo reemplazamos por su respectivo en mayúscula en el mismo evento
         // (esto se logra por que en java todas las variables son pasadas por referencia)
         e.setKeyChar((char)(((int)e.getKeyChar()) - 32));
         }
         }

         //y listo, regresamos siempre falso para que las demas notificaciones continuen, si regresamos true
         // significa que el dispatcher consumio el evento
         return false;
         }
         });
         //FIN DEL BLOQUE*/
        return rootPane1;
    }

    public void actionPerformed(ActionEvent e) {
        if (ACTION_CLOSE.equals(e.getActionCommand())) {
            setVisible(false);
            dispose();
        }

    }

    public SuperFrame(String title) throws HeadlessException {
        super(title);
    }
}
