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

import org.gitools.newick.NewickNode;
import org.gitools.newick.NewickTree;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @noinspection ALL
 */
public class HierarchicalClusteringResults extends GenericClusteringResults
{

    private NewickTree tree;
    private int level;

    public HierarchicalClusteringResults(String[] labels, NewickTree tree, int level)
    {
        super(labels, new HashMap<String, List<Integer>>());

        this.tree = tree;
        this.level = level;

        updateClusters();
    }

    public NewickTree getTree()
    {
        return tree;
    }

    public void setNewickTree(NewickTree tree)
    {
        this.tree = tree;
        this.level = 0;
        updateClusters();
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
        updateClusters();
    }

    private void updateClusters()
    {
        NewickNode root = tree.getRoot();

        List<NewickNode> clusterLeaves = root.getLeaves(level);

        StringBuilder sb = new StringBuilder();
        int len = (int) (Math.floor(Math.log10(clusterLeaves.size())) + 1);
        for (int i = 0; i < len; i++)
            sb.append('0');

        String fmtPat = sb.toString();
        DecimalFormat fmt = new DecimalFormat(fmtPat);

        int index = 0;
        Map<String, List<Integer>> clusters = new HashMap<String, List<Integer>>();
        for (NewickNode cluster : clusterLeaves)
        {
            List<NewickNode> nodes = cluster.getLeaves();
            List<Integer> indices = new ArrayList<Integer>(nodes.size());
            for (NewickNode node : nodes)
                indices.add(Integer.parseInt(node.getName()));

            String name = fmt.format(index++);
            clusters.put(name, indices);
        }

        init(getDataLabels(), clusters);
    }
}
