/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.analysis.clustering;

import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;

public class OptionsPage extends AbstractWizardPage {


    private ClusteringMethod method;
    private JPanel root;
    private JTextField clusteringName;
    private JCheckBox createBookmarkCheckBox;

    public OptionsPage() {
        super();
        setComplete(false);
    }

    public void updatePage(ClusteringMethod method, MatrixDimensionKey clusteringDimension, String layerId) {
        this.method = method;
        setTitle("Assign name to clustering and create a bookmark");

        if (method != null) {
            if (method.getUserGivenName() != null && !method.getUserGivenName().equals("")) {
                clusteringName.setText(method.getUserGivenName());
            } else {
                clusteringName.setText(method.getName() + "-" + clusteringDimension.getLabel().substring(0, 3).toLowerCase() + "s-" + layerId);
            }
        }
        setComplete(true);
    }


    @Override
    public void updateModel() {

        if (clusteringName.getText().length() > 0) {
            method.setUserGivenName(clusteringName.getText());
        }

    }

    public boolean doCreateBookmark() {
        return this.createBookmarkCheckBox.isSelected();
    }

    @Override
    public JComponent createControls() {
        return root;
    }

}
