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
package org.gitools.clustering;

import org.gitools.clustering.method.annotations.AnnPatClusteringMethod;
import org.gitools.clustering.method.value.WekaCobWebMethod;
import org.gitools.clustering.method.value.WekaHCLMethod;
import org.gitools.clustering.method.value.WekaKmeansMethod;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClusteringMethodFactory
{

    private static final ClusteringMethodDescriptor[] DEFAULT_DESCRIPTORS = new ClusteringMethodDescriptor[]{
            new ClusteringMethodDescriptor(
                    "Clustering from annotations",
                    "Cluster data instances according to a set of selected annotations",
                    AnnPatClusteringMethod.class),
            new ClusteringMethodDescriptor(
                    "Agglomerative hierarchical clustering",
                    "Cluster data instances according to classic agglomerative hierarchical clustering method",
                    WekaHCLMethod.class),
            new ClusteringMethodDescriptor(
                    "K-means clustering",
                    "Cluster data instances according to k-means clustering method",
                    WekaKmeansMethod.class),
            new ClusteringMethodDescriptor(
                    "Cobweb clustering",
                    "Cluster data instances according to cobweb clustering method",
                    WekaCobWebMethod.class)
    };

    private static ClusteringMethodFactory instance;

    private List<ClusteringMethodDescriptor> descriptors;

    private ClusteringMethodFactory()
    {
        descriptors = new ArrayList<ClusteringMethodDescriptor>();
        registerMethods(DEFAULT_DESCRIPTORS);
    }

    public static ClusteringMethodFactory getDefault()
    {
        if (instance == null)
        {
            instance = new ClusteringMethodFactory();
        }
        return instance;
    }

    public List<ClusteringMethodDescriptor> getDescriptors()
    {
        return descriptors;
    }

    public final void registerMethods(ClusteringMethodDescriptor[] descriptors)
    {
        this.descriptors.addAll(Arrays.asList(descriptors));
    }

    public ClusteringMethod create(ClusteringMethodDescriptor descriptor)
    {
        Class<? extends ClusteringMethod> methodClass = descriptor.getMethodClass();

        try
        {
            Constructor<? extends ClusteringMethod> c = methodClass.getConstructor();
            return c.newInstance();
        } catch (Exception e)
        {
            return null;
        }
    }
}
