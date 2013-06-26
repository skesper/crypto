package de.kesper.crypto.gui.actions;

import de.kesper.crypto.ApplicationContext;
import de.kesper.crypto.engine.CryptFactory;
import de.kesper.crypto.engine.model.KeyChainEntry;

import javax.swing.*;
import javax.xml.bind.JAXB;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 14:20
 */
public class ImportPublicKeyAction extends AbstractAction {

    public ImportPublicKeyAction(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JFileChooser fc = new JFileChooser(".");
        int result = fc.showOpenDialog(ApplicationContext.getInstance().getMainFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            KeyChainEntry kce = JAXB.unmarshal(fc.getSelectedFile(), KeyChainEntry.class);
            if (kce.getId()==null) {
                JOptionPane.showConfirmDialog(null, "ERROR: Could not parse Key file.");
            } else {
                CryptFactory cf = ApplicationContext.getInstance().getCryptFactory();

                String msg = cf.addKeyChainEntry(kce);
                if (msg==null) {
                    JOptionPane.showMessageDialog(ApplicationContext.getInstance().getMainFrame(),
                            "You've successfully added "+kce.getLabel()+" to your key ring.",
                            "OK", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ApplicationContext.getInstance().getMainFrame(),
                            "You did not add ".concat(kce.getLabel()).concat(" to your key ring.\n").concat(msg),
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    cf.store();
                } catch(Exception e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }
}
