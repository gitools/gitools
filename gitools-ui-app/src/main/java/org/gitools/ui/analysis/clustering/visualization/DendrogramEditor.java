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
package org.gitools.ui.analysis.clustering.visualization;

import org.gitools.analysis.clustering.hierarchical.Cluster;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.editor.AbstractEditor;

import java.awt.*;

public class DendrogramEditor extends AbstractEditor {

    private Cluster model;

    public DendrogramEditor(Cluster model) {
        super();

        this.model = model;
        setName("Hierarchical clustering '" + model.getName() + "'");
        setIcon(IconNames.CREATE_IMAGE_SMALL_ICON);
        setLayout(new BorderLayout());
        setSaveAllowed(false);
        setSaveAsAllowed(false);
        setBackground(Color.WHITE);

        DendrogramPanel panel = new DendrogramPanel();
        panel.setModel(model);
        panel.setBackground(Color.WHITE);

        add(panel);
    }

    @Override
    public Object getModel() {
        return model;
    }
}
