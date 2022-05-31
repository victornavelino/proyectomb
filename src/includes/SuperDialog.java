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
import javax.swing.JDialog;
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

public class SuperDialog extends JDialog implements ActionListener {

    public SuperDialog() throws HeadlessException {
        super();


    }

    public SuperDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
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

        return rootPane1;
    }

    public void actionPerformed(ActionEvent e) {
        if (ACTION_CLOSE.equals(e.getActionCommand())) {
            setVisible(false);
            dispose();
        }

    }

    public SuperDialog(String title) throws HeadlessException {
        super();
        this.setTitle(title);
    }
}
