package de.kesper.crypto.gui.actions;

import de.kesper.crypto.ApplicationContext;
import de.kesper.crypto.engine.CryptFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 10:33
 */
public class DecryptMessage extends AbstractAction {

    public DecryptMessage(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        ApplicationContext ac = ApplicationContext.getInstance();

        String text = ac.getMainFrame().getTextContent();

        CryptFactory cf = ac.getCryptFactory();

        try {
            String clear = cf.decrypt(text);
            ac.getMainFrame().setTextContent(clear);
        } catch(Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(ac.getMainFrame(),
                    "Error during decryption: "+e1.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
}
