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
package org.gitools.analysis.clustering.method.value;

import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringException;
import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.clustering.Clusters;
import org.gitools.api.analysis.IProgressMonitor;


public abstract class AbstractClusteringValueMethod implements ClusteringMethod {

    boolean preprocess;

    boolean transpose;

    int classIndex;

    public abstract Clusters cluster(ClusteringData data, IProgressMonitor monitor) throws ClusteringException;

    public void setPreprocess(boolean preprocess) {
        this.preprocess = preprocess;
    }

    public void setTranspose(boolean transposed) {
        this.transpose = transposed;
    }
}
