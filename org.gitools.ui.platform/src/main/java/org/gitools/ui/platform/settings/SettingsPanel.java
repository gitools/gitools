/*
 * #%L
 * gitools-ui-platform
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.platform.settings;

import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SettingsPanel implements ISettingsPanel {

    private String title = "";
    private Icon logo;
    private MessageStatus status = MessageStatus.INFO;
    private Map<String, ISettingsSection> sections = new LinkedHashMap<>();
    private String message = "";

    public SettingsPanel(String title, String message, String logo, List<ISettingsSection> sections) {
        this(title, message, logo, sections.toArray(new ISettingsSection[sections.size()]));
    }

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
