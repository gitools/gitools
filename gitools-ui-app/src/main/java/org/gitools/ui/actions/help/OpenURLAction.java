package org.gitools.ui.actions.help;

import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;

public class OpenURLAction extends BaseAction {

    private String url;

    public OpenURLAction(String name, String url, int mnemonic) {
        this(name, url, mnemonic, null);
    }

    public OpenURLAction(String name, String url, int mnemonic, KeyStroke accelerator) {
        super(name, null, null, mnemonic);

        if (accelerator!=null) {
            setAccelerator(accelerator);
        }

        setDefaultEnabled(true);
        this.url = url;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            URI uri = new URI(url);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(uri);
            } else {
                JOptionPane.showInputDialog(Application.get(), "Copy this URL into your web browser", uri.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
