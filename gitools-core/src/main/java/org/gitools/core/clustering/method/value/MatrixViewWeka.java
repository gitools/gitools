/*
 * #%L
 * gitools-core
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
package org.gitools.core.clustering.method.value;

import org.gitools.core.clustering.ClusteringData;
import org.gitools.core.utils.MatrixUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.io.IOException;

/**
 * This class is an adapter for Weka Instances class
 * and ImatrixView class
 *
 */
class MatrixViewWeka extends Instances {

    private final ClusteringData matrixView;

    private Instances structure;

    @Nullable
    private int[] indexes; //selected attributes from preprocessing

    private final int initClassIndex;

    public MatrixViewWeka(Instances ds, ClusteringData matrix, int classIndex) {
        super(ds);

        initClassIndex = m_ClassIndex = classIndex;

        structure = new Instances("matrixToCluster", m_Attributes, 0);

        indexes = null;

        matrixView = matrix;
    }

    @NotNull
    FastVector addAttributes(int numAttributes) {

        FastVector attr = new FastVector();

        for (int rows = 0; rows < numAttributes; rows++)
            attr.addElement(new Attribute("a" + rows));

        return attr;
    }

    public Instances getStructure() throws IOException {

        return structure;
    }

    public void setDataSet(Instances mergeInstances) {
    }

    @NotNull
    public Instances getDataSet() throws IOException {

        Instance current = null;

        Instances dataSet = new Instances("matrixToCluster", m_Attributes, 0);

        try {
            for (int i = 0; i < matrixView.getSize(); i++) {
                current = get(i);
                dataSet.add(current);
            }
        } catch (Exception ex) {
            throw new IOException("Error retrieving Weka dataset");
        }
        return dataSet;
    }

    /**
     * Given an index (col,row) from the matrix we retrieve the instance
     */
    @Nullable
    public Instance get(int index) throws Exception {

        if (index > matrixView.getSize() - 1) {
            return null;
        }

        double[] values = null;

        int row;

        final MatrixUtils.DoubleCast valueCast = MatrixUtils.createDoubleCast(matrixView.getInstance(index).getValueClass(0));

        if (indexes == null) {

            values = new double[matrixView.getInstance(index).getNumAttributes()];

            for (row = 0; row < matrixView.getInstance(index).getNumAttributes(); row++) {
                try {
                    values[row] = valueCast.getDoubleValue(matrixView.getInstance(index).getValue(row));
                } catch (Exception e) {
                    values[row] = Double.NaN;
                }
            }
        } else {

            values = new double[indexes.length];

            for (int i = 0; i < indexes.length; i++) {
                try {
                    row = indexes[i];
                    values[i] = valueCast.getDoubleValue(matrixView.getInstance(index).getValue(row));
                } catch (Exception e) {
                    values[i] = Double.NaN;
                }
            }
        }

        //Instance is created once data in array values. This improves time performance
        Instance current = new Instance(1, values);

        //The dataset for the instance
        Instances dataset = new Instances("matrixToCluster", m_Attributes, 0);

        dataset.setClassIndex(m_ClassIndex);

        current.setDataset(dataset);

        dataset.add(current);

        return current;
    }

    void setFilteredAttributes(@NotNull int[] selectedAttributes) {

        indexes = selectedAttributes;

        m_Attributes = addAttributes(selectedAttributes.length);

        structure = new Instances("matrixToCluster", m_Attributes, 0);

        m_ClassIndex = initClassIndex;
    }

    void resetFilteredAttributes() {

        indexes = null;

        m_Attributes = addAttributes(matrixView.getSize());

        structure = new Instances("matrixToCluster", m_Attributes, 0);
    }

    @Override
    public int numInstances() {
        return matrixView.getSize();
    }

    @Nullable
    @Override
    public Instance instance(int i) {
        try {
            return get(i);

        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public int numAttributes() {

        if (matrixView.getInstance(0) == null) {
            return 0;
        }

        if (indexes == null) {
            return matrixView.getInstance(0).getNumAttributes();
        } else {
            return indexes.length;
        }

    }

    @NotNull
    public Attribute attribute(Integer index) {

        return (Attribute) m_Attributes.elements(index);
    }

    @Override
    public int classIndex() {
        return m_ClassIndex;
    }

    public ClusteringData getMatrixView() {
        return matrixView;
    }

    public void setClassIndex(int index) {
        m_ClassIndex = index;
    }
}
