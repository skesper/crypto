package de.kesper.crypto.gui;

import de.kesper.crypto.gui.actions.*;

import javax.swing.*;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 10:28
 */
public class MenuCreator {

    public static JMenuBar create() {
        JMenuBar bar = new JMenuBar();

        JMenu encryption = new JMenu("Encryption/Decryption");

        encryption.add(new EncryptMessage("Encrypt Message"));
        encryption.add(new DecryptMessage("Decrypt Message"));

        encryption.add(new JSeparator());

        encryption.add(new EncryptFilesAction("Encrypt Files ..."));
        encryption.add(new DecryptFilesAction("Decrypt Files ..."));

        encryption.add(new JSeparator());

        encryption.add(new ExitAction("Exit"));

        bar.add(encryption);

        JMenu keyManager = new JMenu("Key Management");

        keyManager.add(new ExportPuplicKeyAction("Export my public key"));
        keyManager.add(new ImportPublicKeyAction("Import other public key"));

        keyManager.add(new JSeparator());

        keyManager.add(new ContactListAction("List contacts"));

        bar.add(keyManager);

        return bar;
    }
}
