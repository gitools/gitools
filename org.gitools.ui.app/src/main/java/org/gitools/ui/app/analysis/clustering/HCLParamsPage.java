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

import org.gitools.analysis.clustering.distance.DistanceMeasure;
import org.gitools.analysis.clustering.distance.EuclideanDistance;
import org.gitools.analysis.clustering.distance.ManhattanDistance;
import org.gitools.analysis.clustering.hierarchical.HierarchicalMethod;
import org.gitools.analysis.clustering.hierarchical.strategy.AverageLinkageStrategy;
import org.gitools.analysis.clustering.hierarchical.strategy.CompleteLinkageStrategy;
import org.gitools.analysis.clustering.hierarchical.strategy.LinkageStrategy;
import org.gitools.analysis.clustering.hierarchical.strategy.SingleLinkageStrategy;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.utils.aggregation.SumAggregator;

import javax.swing.*;

public class HCLParamsPage extends AbstractWizardPage {


    private HierarchicalMethod method;
    private JComboBox distAlgCombo;
    private JTextPane explanationTextPane;
    private JComboBox linkTypeCombo;
    private JPanel root;

    public HCLParamsPage(HierarchicalMethod method) {
        super();
        this.method = method;
        setTitle("Select hierarchical clustering settings");
        setComplete(true);
        linkTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Average", "Complete (maximum)", "Single (minimum)"}));
        distAlgCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Euclidean", "Manhattan"}));
        explanationTextPane.setText("<html>\n" +
                "  <head>\n" +
                "    \n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <i>Linkage</i>\n" +
                "\n" +
                "    <ul>\n" +
                "      <li>\n" +
                "        Choose average linkage by default\n" +
                "      </li>\n" +
                "      <li>\n" +
                "        Minimum linkage makes distances seem bigger and similar samples may be \n" +
                "        separated in sublty different clusteres.\n" +
                "      </li>\n" +
                "      <li>\n" +
                "        Maximum linkage makes distances seem smaller and the differently \n" +
                "        detected clusters should be very distinct.\n" +
                "      </li>\n" +
                "    </ul>\n" +
                "  </body>\n" +
                "</html>\n");

    }


    @Override
    public void updateModel() {

        DistanceMeasure distanceMeasure;
        LinkageStrategy linkageStrategy;

        switch (linkTypeCombo.getSelectedItem().toString()) {
            case "Single (minimum)": linkageStrategy = new SingleLinkageStrategy(); break;
            case "Complete (maximum)": linkageStrategy = new CompleteLinkageStrategy(); break;
            case "Average": linkageStrategy = new AverageLinkageStrategy(); break;
            default: linkageStrategy = new AverageLinkageStrategy();
        }

        if (distAlgCombo.getSelectedItem().toString().equalsIgnoreCase("euclidean")) {
            distanceMeasure = EuclideanDistance.get();
        } else {
            distanceMeasure = ManhattanDistance.get();
        }

        method.setDistanceMeasure(distanceMeasure);
        method.setLinkageStrategy(linkageStrategy);

        //TODO Add aggregator selector
        method.setAggregator(SumAggregator.INSTANCE);

    }

    @Override
    public JComponent createControls() {
        return root;
    }

}
