package de.kesper.crypto.gui.actions;

import de.kesper.crypto.ApplicationContext;
import de.kesper.crypto.engine.model.KeyChain;
import de.kesper.crypto.engine.model.KeyChainEntry;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * User: kesper
 * Date: 21.06.13
 * Time: 11:25
 */
public class ContactListAction extends AbstractAction {
    public ContactListAction(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        KeyChain chain = ApplicationContext.getInstance().getCryptFactory().getKeyChain();
        StringBuilder sb = new StringBuilder();
        sb.append("Overview of all registered public keys\n\n");
        for(KeyChainEntry kce : chain.getEntries()) {
            sb.append(kce.getId());
            sb.append(" : ");
            sb.append(kce.getLabel());
            sb.append("\n");
        }

        ApplicationContext.getInstance().getMainFrame().setTextContent(sb.toString());
    }
}
