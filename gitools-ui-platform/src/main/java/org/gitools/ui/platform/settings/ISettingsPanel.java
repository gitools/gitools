package org.gitools.ui.platform.settings;

import org.gitools.ui.platform.dialog.MessageStatus;

import javax.swing.*;

public interface ISettingsPanel {

    String getTitle();

    Icon getLogo();

    String[] getSectionNames();

    JComponent createComponents(String section);

    MessageStatus getStatus();

    String getMessage();
}
