package de.kesper.crypto;

import de.kesper.crypto.engine.model.KeyChain;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.nio.file.Paths;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 10:14
 */
public class Application {

    public static void main(String[] args) throws Exception {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());


        //KeyChain kc = ApplicationContext.getInstance().getCryptFactory().getKeyChain();
        ApplicationContext.getInstance().init();

        ApplicationContext.getInstance().getMainFrame().setTitle("Crypto - ");


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

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ApplicationContext.getInstance().getMainFrame().setVisible(true);
            }
        });
    }
}