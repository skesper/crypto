/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.kesper.crypto.gui.fx;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import de.kesper.crypto.ApplicationContext;
import de.kesper.crypto.engine.CryptFactory;
import de.kesper.crypto.engine.model.KeyChainEntry;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;

/**
 *
 * @author kesper
 */
public class CryptoMainFrameController implements Initializable {

    @FXML
    private TextField statusField;

    @FXML
    private TextArea mainText;
    
    @FXML
    private MenuBar menuBar;


    @Override
    public void initialize(java.net.URL arg0, ResourceBundle arg1) {
        menuBar.setFocusTraversable(true);

        statusField.setText("initialized.");

        try {
            ApplicationContext.getInstance().init();
        } catch(Exception e) {
            e.printStackTrace();
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ApplicationContext.getInstance().getCryptFactory().initialize(Paths.get("."));
                } catch(Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
        t.start();
    }
    
    public void encryptMessage() {
        System.out.println("clicked encrypt message");
        statusField.setText("encrypting message ...");
        CryptFactory cf = ApplicationContext.getInstance().getCryptFactory();

        ArrayList<KeyChainEntry> entries = new ArrayList<>();
        entries.add(cf.getMeAsEntry());
        entries.addAll(cf.getKeyChain().getEntries());

        Object result = JOptionPane.showInputDialog(ApplicationContext.getInstance().getMainFrame(),
                "Please choose the recipient",
                "Recipient",
                JOptionPane.QUESTION_MESSAGE,
                null,
                entries.toArray(),
                null
        );

        if (result!=null && (result instanceof KeyChainEntry)) {
            KeyChainEntry selectedRecipient = (KeyChainEntry)result;
            String text = mainText.getText();

            try {
                String b64Encrypted = cf.encrypt(selectedRecipient, text);
                mainText.setText(b64Encrypted);
            } catch(Exception e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        statusField.setText("");
    }

    public void decryptMessage() {
        ApplicationContext ac = ApplicationContext.getInstance();

        String text = mainText.getText();

        CryptFactory cf = ac.getCryptFactory();

        try {
            String clear = cf.decrypt(text);
            mainText.setText(clear);
        } catch(Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(ac.getMainFrame(),
                    "Error during decryption: "+e1.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }


    }
    
    public void aboutMessage() throws IOException {
        System.out.println("clicked about");

        URL url = ClassLoader.getSystemClassLoader().getResource("about.fxml");
        Pane aboutPane = FXMLLoader.load(url);
        Scene aboutScene = new Scene(aboutPane);        

        Stage s = new Stage();
        s.setTitle("About");
        s.setScene(aboutScene);
        s.show();
        
    }
    
    public void exitAction() {
        System.exit(0);
    }
}
