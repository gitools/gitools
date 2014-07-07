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
package org.gitools.ui.core.components.editor;

import org.gitools.ui.platform.panel.Html4Panel;

import java.awt.*;
import java.net.URL;
import java.util.Map;

public class HtmlEditor extends AbstractEditor {

    private String title;
    private Html4Panel panel;

    protected HtmlEditor(String title, URL url) {
        this.title = title;
        createComponents();
        navigate(url);
    }

    protected void exception(Exception e) {
    }

    @Override
    public String getName() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Object getModel() {
        return null;
    }

    private void createComponents() {
        panel = new Html4Panel() {

            @Override
            protected void performAction(String name, Map<String, String> params) {
                HtmlEditor.this.performAction(name, params);
            }

            @Override
            protected void performLoad(String href) {
                HtmlEditor.this.performLoad(href);
            }

            @Override
            protected void performSave(String href) {
                HtmlEditor.this.performSave(href);
            }
        };

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    protected void performAction(String name, Map<String, String> params) {
    }

    protected void performLoad(String href) {
    }

    protected void performSave(String href) {
    }

    protected void navigate(URL url) {

        try {
            panel.navigate(url);
        } catch (Exception e) {
            exception(e);
        }

    }
}
