package de.kesper.crypto;

import de.kesper.crypto.engine.CryptFactory;
import de.kesper.crypto.gui.CryptoMainFrame;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 10:14
 */
public class ApplicationContext {
    private static final ApplicationContext me = new ApplicationContext();

    private CryptoMainFrame mainFrame;
    private CryptFactory cryptFactory;
    private SecretKey passwordKey;

    private ApplicationContext() {
        mainFrame = new CryptoMainFrame();
    }

    public static ApplicationContext getInstance() {
        return me;
    }

    public CryptoMainFrame getMainFrame() {
        return mainFrame;
    }

    public CryptFactory getCryptFactory() {
        return cryptFactory;
    }

    public SecretKey getPasswordKey() {
        return passwordKey;
    }

    public void init() throws Exception {
        cryptFactory = CryptFactory.newInstance();

        JPasswordField passwordField = new JPasswordField(20);
        JOptionPane.showMessageDialog(
                null,
                passwordField,
                "Enter password",
                JOptionPane.QUESTION_MESSAGE);
        String pwd = new String(passwordField.getPassword());

        try {
            passwordKey = cryptFactory.createAesKeyFromPassword(pwd);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "The password key could not be generated! Can't proceed from here so I'll going down now.",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
