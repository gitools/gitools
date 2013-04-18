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
package org.gitools.clustering.method.value;

import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringException;
import org.gitools.clustering.ClusteringMethod;
import org.gitools.clustering.ClusteringResults;
import org.gitools.utils.progressmonitor.IProgressMonitor;


/**
 * @noinspection ALL
 */
public abstract class AbstractClusteringValueMethod implements ClusteringMethod {

    boolean preprocess;

    boolean transpose;

    int classIndex;

    public abstract ClusteringResults cluster(ClusteringData data, IProgressMonitor monitor) throws ClusteringException;

    public boolean isPreprocess() {
        return preprocess;
    }

    public void setPreprocess(boolean preprocess) {
        this.preprocess = preprocess;
    }

    public boolean isTranspose() {
        return transpose;
    }

    public void setTranspose(boolean transposed) {
        this.transpose = transposed;
    }
}
