package de.kesper.crypto.gui.actions;

import de.kesper.crypto.ApplicationContext;
import de.kesper.crypto.engine.CryptFactory;
import de.kesper.crypto.engine.model.KeyChainEntry;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 10:33
 */
public class EncryptMessage extends AbstractAction {
    public EncryptMessage(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
            String text = ApplicationContext.getInstance().getMainFrame().getTextContent();

            try {
                String b64Encrypted = cf.encrypt(selectedRecipient, text);
                ApplicationContext.getInstance().getMainFrame().setTextContent(b64Encrypted);
            } catch(Exception e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
