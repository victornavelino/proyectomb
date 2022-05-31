/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package includes;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Administrador
 */
public class limitarMaximoDocument extends PlainDocument {

    private int maxLength;

    public limitarMaximoDocument(int maxLength) {
        this.maxLength = maxLength;
    }

    public void insertString(int offset, String str,
            AttributeSet a)
            throws BadLocationException {
        if (getLength() + str.length() > maxLength) {

            Toolkit.getDefaultToolkit().beep();
        } else {
            super.insertString(offset, str, a);

        }
    }
}
