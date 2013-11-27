package org.gitools.ui.platform.settings;

import org.gitools.ui.platform.dialog.MessageStatus;

import javax.swing.*;

public interface ISettingsPanel {

    String getTitle();

    Icon getLogo();

    JComponent createComponents();

    MessageStatus getStatus();

    String getMessage();
}
