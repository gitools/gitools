package org.gitools.ui.platform.settings;

import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class SettingsPanel implements ISettingsPanel {

    private String title = "";
    private Icon logo;
    private MessageStatus status = MessageStatus.INFO;
    private Map<String, ISettingsSection> sections = new LinkedHashMap<>();
    private String message = "";

    public SettingsPanel(String title, String message, String logo, ISettingsSection... sections) {

        if (logo != null) {
            setLogo(IconUtils.getImageIconResource(logo));
        }

        this.title = title;
        this.message = message;

        for (ISettingsSection section : sections) {
            addSection(section);
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Icon getLogo() {
        return logo;
    }

    public void setLogo(Icon logo) {
        this.logo = logo;
    }

    @Override
    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SettingsPanel addSection(ISettingsSection section) {
        this.sections.put(section.getName(), section);
        return this;
    }

    @Override
    public String[] getSectionNames() {
        return sections.keySet().toArray(new String[sections.size()]);
    }

    @Override
    public JComponent createComponents(String sectionName) {

        if (!sections.containsKey(sectionName)) {
            return null;
        }

        return sections.get(sectionName).getPanel();
    }

}
