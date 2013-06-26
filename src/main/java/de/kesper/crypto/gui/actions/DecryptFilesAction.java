package de.kesper.crypto.gui.actions;

import de.kesper.crypto.ApplicationContext;
import de.kesper.crypto.engine.CryptFactory;
import de.kesper.crypto.engine.model.KeyChainEntry;
import de.kesper.crypto.gui.CryptoMainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * User: kesper
 * Date: 20.06.13
 * Time: 16:16
 */
public class DecryptFilesAction extends AbstractAction {

    public DecryptFilesAction(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                CryptFactory cf = ApplicationContext.getInstance().getCryptFactory();

                JFileChooser fc = new JFileChooser(".");
                fc.setDialogTitle("Select files to decrypt");
                fc.setMultiSelectionEnabled(true);
                int ok = fc.showOpenDialog(ApplicationContext.getInstance().getMainFrame());
                if (ok==JFileChooser.APPROVE_OPTION) {

                    File[] selection = fc.getSelectedFiles();

                    if (selection.length<1) {
                        selection = new File[]{fc.getSelectedFile()};
                    }

                    int encryptedFiles = 0;

                    CryptoMainFrame cmf = ApplicationContext.getInstance().getMainFrame();

                    for(File f : selection) {
                        String absolutePath = f.getAbsolutePath();
                        String targetAbsolutePath = absolutePath.substring(0,absolutePath.length()-10);
                        try {
                            Path sourcePath = Paths.get(absolutePath);
                            Path targetPath = Paths.get(targetAbsolutePath);
                            cf.decrypt(sourcePath, targetPath, cf.decodePrivateKey(cf.getKeyChain().getEncodedPrivateKey()));

                            cmf.setTextContent(
                                    cmf.getTextContent().concat("\n").concat(sourcePath.toString()).concat(" decrypted."));
                            encryptedFiles++;
                        } catch(Exception e1) {
                            int proceed = JOptionPane.showConfirmDialog(ApplicationContext.getInstance().getMainFrame(),
                                    "Error during decryption: "+e1.getMessage()+", Continue?",
                                    "ERROR",
                                    JOptionPane.ERROR_MESSAGE);
                            if (proceed==JOptionPane.CANCEL_OPTION || proceed==JOptionPane.CLOSED_OPTION) {
                                return;
                            }
                        }
                    }

                    JOptionPane.showMessageDialog(ApplicationContext.getInstance().getMainFrame(),
                            encryptedFiles+" files have been decrypted.",
                            "INFO",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });

        t.start();

    }
}
