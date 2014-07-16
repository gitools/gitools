/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app.analysis.clustering.visualization;

import org.gitools.heatmap.header.HierarchicalCluster;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.components.editor.AbstractEditor;
import org.gitools.ui.platform.icons.IconNames;

import javax.enterprise.context.ApplicationScoped;
import javax.swing.*;
import java.awt.*;

@ApplicationScoped
public class DendrogramEditor extends AbstractEditor {

    private HierarchicalCluster model;

    public DendrogramEditor(HierarchicalCluster model) {
        super();

        this.model = model;
        setName(model.toString());
        setIcon(IconNames.DENDROGRAM_ICON);
        setLayout(new BorderLayout());
        setSaveAllowed(false);
        setSaveAsAllowed(false);
        setBackground(Color.WHITE);

        DendrogramPanel panel = new DendrogramPanel();
        panel.setModel(model);
        panel.setBackground(Color.WHITE);

        panel.setPreferredSize(new Dimension(getWidth(), model.getIdentifiers().size()*12));
        add(new JScrollPane(panel));

        Application.get().showNotification("Dendrogram " + model.getName()
                + " with " + model.getLeaves() + " leaves opened");
    }

    @Override
    public Object getModel() {
        return model;
    }
}
