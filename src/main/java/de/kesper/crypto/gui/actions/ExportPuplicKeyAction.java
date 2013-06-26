package de.kesper.crypto.gui.actions;

import de.kesper.crypto.ApplicationContext;
import de.kesper.crypto.engine.model.KeyChain;
import de.kesper.crypto.engine.model.KeyChainEntry;

import javax.swing.*;
import javax.xml.bind.JAXB;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 14:19
 */
public class ExportPuplicKeyAction extends AbstractAction {
    public ExportPuplicKeyAction(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser(".");
        
        int result = fc.showSaveDialog(ApplicationContext.getInstance().getMainFrame());
        
        if (result == JFileChooser.APPROVE_OPTION) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            KeyChain chain = ApplicationContext.getInstance().getCryptFactory().getKeyChain();
            KeyChainEntry kce = new KeyChainEntry();
            kce.setEncodedPublicKey(chain.getEncodedPublicKey());
            kce.setLabel(chain.getName()+" <"+chain.getEmail()+">");
            kce.setId(chain.getUuid());
            JAXB.marshal(kce, baos);

            try {
                FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch(IOException e1) {
                JOptionPane.showConfirmDialog(null, "Error: "+e1.getMessage());
            }
        }
    }
}
