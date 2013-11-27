package org.gitools.ui.platform.settings;

import org.gitools.ui.platform.dialog.MessageStatus;

import javax.swing.*;

public abstract class AbstractSettingsPanel implements ISettingsPanel {

    private String title = "";

    private Icon logo;

    private MessageStatus status = MessageStatus.INFO;

    private String message = "";

    public AbstractSettingsPanel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setLogo(Icon logo) {
        this.logo = logo;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Icon getLogo() {
        return logo;
    }

    @Override
    public MessageStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public abstract JComponent createComponents();

}
