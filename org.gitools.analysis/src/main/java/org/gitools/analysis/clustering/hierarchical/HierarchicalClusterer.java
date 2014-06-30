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
package org.gitools.analysis.clustering.hierarchical;

import com.google.common.base.Joiner;
import org.apache.commons.math3.util.FastMath;
import org.gitools.analysis.clustering.distance.DistanceMeasure;
import org.gitools.analysis.clustering.hierarchical.strategy.LinkageStrategy;
import org.gitools.api.analysis.IAggregator;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.heatmap.header.HierarchicalCluster;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.google.common.collect.Sets.newHashSet;

public class HierarchicalClusterer {

    private LinkageStrategy linkageStrategy;
    private DistanceMeasure measure;
    private IAggregator aggregator;

    public HierarchicalClusterer(LinkageStrategy linkageStrategy, DistanceMeasure measure, IAggregator aggregator) {
        this.linkageStrategy = linkageStrategy;
        this.measure = measure;
        this.aggregator = aggregator;
    }

    public HierarchicalCluster cluster(IMatrix matrix, IMatrixLayer<Double> layer, IMatrixDimension clusterDimension, IMatrixDimension aggregationDimension, IProgressMonitor monitor) {

        Map<String, HierarchicalCluster> clusters = new HashMap<>(clusterDimension.size());
        SortedSet<ClusterPair> linkages = new ConcurrentSkipListSet<>();

        IMatrixPosition position1 = matrix.newPosition();
        IMatrixPosition position2 = matrix.newPosition();

        monitor.begin("Calculating distances...", clusterDimension.size());
        for (String id1 : position1.iterate(clusterDimension)) {

            // Check user cancel action
            monitor.worked(1);
            if (monitor.isCancelled()) {
                throw new CancellationException();
            }

            HierarchicalCluster cluster1 = newCluster(clusters, id1);
            cluster1.setWeight( aggregator.aggregate(position1.iterate(layer, aggregationDimension)) );

            for (String id2 : position2.iterate(clusterDimension.from(id1))) {

                // Skip equal ids
                if (id1.equals(id2)) continue;

                Double distance = measure.compute(
                        position1.iterate(layer, aggregationDimension),
                        position2.iterate(layer, aggregationDimension)
                );

                HierarchicalCluster cluster2 = newCluster(clusters, id2);

                linkages.add(new ClusterPair(distance, cluster1, cluster2));
            }
        }

        /* Process */
        HierarchyBuilder builder = new HierarchyBuilder(newHashSet(clusters.values()), linkages);
        builder.agglomerate(linkageStrategy, monitor, clusterDimension.size());

        /* Set cluster names */
        HierarchicalCluster root = builder.getRootCluster();
        root.setName("");
        Color color = nameClusters(root.getChildren(), 1);
        root.setColor(color.getRGB());

        root.setName("root");

        return root;
    }

    private static final int[] DEFAULT_PALETTE = {0x4bb2c5, 0xEAA228, 0xc5b47f, 0x546D61, 0x958c12, 0x953579, 0xc12e2e, 0x4b5de4, 0xd8b83f, 0xff5800, 0x0085cc, 0xc747a3, 0xcddf54, 0xFBD178, 0x26B4E3, 0xbd70c7, 0xabdbeb, 0x40D800, 0x8AFF00, 0xD9EB00, 0xFFFF71, 0x777B00, 0x498991, 0xC08840, 0x9F9274, 0x579575, 0x646C4A, 0x6F6621, 0x6E3F5F, 0x4F64B0, 0xA89050, 0xC45923, 0x187399, 0x945381, 0x959E5C, 0xAF5714, 0x478396, 0x907294, 0x426c7a, 0x878166, 0xAEA480, 0xFFFFD3, 0xE9D5A4, 0xA29877};
    private int index = -1;

    private Color next() {
        index++;
        return new Color(DEFAULT_PALETTE[index % DEFAULT_PALETTE.length]);
    }

    private Color nameClusters(List<HierarchicalCluster> children, int level) {

        if (children.isEmpty()) {
            return next();
        }

        int digits = calculateDigits(children.size());

        Collections.sort(children, Collections.reverseOrder());

        Color colorParent = null;
        Double weightParent = 0.0;
        for (int i = 0; i < children.size(); i++) {
            HierarchicalCluster child = children.get(i);
            if (child.getChildren().isEmpty()) {
                child.setName(JOINER.join(child.getIdentifiers()));
            } else {
                child.setName(child.getParent().getName() + createLabel(i, digits));
            }
            Color colorChild = nameClusters(child.getChildren(), level+1);
            child.setColor(colorChild.getRGB());

            if (colorParent == null) {
                colorParent = colorChild;
                weightParent = (child.getChildren().isEmpty() ? 0.0 : child.getWeight());
            } else {
                colorParent = mixColors(colorParent, weightParent, colorChild, child.getWeight());
                weightParent += (child.getChildren().isEmpty() ? 0.0 : child.getWeight());
            }

        }

        if (level > 10) {
            return next();
        }

        return colorParent;

    }

    private static Color mixColors(Color c1, double w1, Color c2, double w2) {

        if (w1 == 0.0) {
            return c2;
        }

        if (w2 == 0.0) {
            return c1;
        }

        double wa1 = FastMath.abs(w1);
        double wa2 = FastMath.abs(w2);

        double total = wa1 + wa2;

        double t1 = wa1 / total;
        double t2 = wa2 / total;

        if (t1 < 0.1) {
            t1 = 0.1; t2 = 0.8;
        }

        if (t2 < 0.1) {
            t1 = 0.8; t2 = 0.1;
        }

        int red = (int) (t1*(double)c1.getRed() + t2*(double)c2.getRed());
        int green = (int) (t1*(double)c1.getGreen() + t2*(double)c2.getGreen());
        int blue = (int) (t1*(double)c1.getBlue() + t2*(double)c2.getBlue());

        return new Color(red, green, blue);
    }

    private static Joiner JOINER = Joiner.on("&");
    private static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static int calculateDigits(int size) {
        return ((int) FastMath.log(ALPHABET.length, size)) + 1;
    }

    public static String createLabel(int number, int digits) {

        char[] label = new char[digits];
        label[digits - 1] = ALPHABET[number % ALPHABET.length];

        for (int d = 1; d < digits; d++) {
            int quocient = number / (ALPHABET.length * d);
            label[digits - d - 1] = ALPHABET[quocient % ALPHABET.length];
        }

        return String.valueOf(label);

    }

    private static HierarchicalCluster newCluster(Map<String, HierarchicalCluster> clusters, String id) {

        if (!clusters.containsKey(id)) {
            HierarchicalCluster newCluster = new HierarchicalCluster(id);
            clusters.put(id, newCluster);
            return newCluster;
        }

        return clusters.get(id);
    }

}
