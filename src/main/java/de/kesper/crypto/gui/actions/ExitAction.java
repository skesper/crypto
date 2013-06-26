package de.kesper.crypto.gui.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 10:38
 */
public class ExitAction extends AbstractAction {
    public ExitAction(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
