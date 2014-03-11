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

import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.analysis.clustering.distance.EuclideanDistance;
import org.gitools.analysis.clustering.distance.ManhattanDistance;
import org.gitools.analysis.clustering.kmeans.KMeansPlusPlusMethod;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import java.text.NumberFormat;

import static com.jgoodies.binding.adapter.Bindings.bind;
import static com.jgoodies.binding.value.ConverterFactory.createStringConverter;
import static java.util.Arrays.asList;

public class KMeansPlusPlusPage extends AbstractWizardPage {

    private JComboBox distance;
    private JTextField clusters;
    private JTextField iterations;
    private JPanel root;
    private JTextPane kMeansClusteringAimsTextPane;

    public KMeansPlusPlusPage(final KMeansPlusPlusMethod method) {
        super("K-means++");

        setTitle("Clustering using K-means++ algorithm");

        bind(
                distance,
                new SelectionInList<>(
                        asList(EuclideanDistance.get(), ManhattanDistance.get()),
                        new PropertyAdapter<>(method, KMeansPlusPlusMethod.PROPERTY_DISTANCE)
                )
        );

        bind(
                clusters,
                createStringConverter(
                        new PropertyAdapter<>(method, KMeansPlusPlusMethod.PROPERTY_NUMCLUSTERS),
                        NumberFormat.getIntegerInstance()
                )
        );

        bind(
                iterations,
                createStringConverter(
                        new PropertyAdapter<>(method, KMeansPlusPlusMethod.PROPERTY_ITERATIONS),
                        NumberFormat.getIntegerInstance()
                )
        );

    }

    @Override
    public JComponent createControls() {
        return root;
    }
}
