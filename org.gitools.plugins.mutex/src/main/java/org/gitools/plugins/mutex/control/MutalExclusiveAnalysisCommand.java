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
package org.gitools.plugins.mutex.control;

import com.google.common.base.Strings;
import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringException;
import org.gitools.analysis.clustering.annotations.AnnPatClusteringData;
import org.gitools.analysis.clustering.annotations.AnnPatClusteringMethod;
import org.gitools.api.analysis.Clusters;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.modulemap.HashModuleMap;
import org.gitools.plugins.mutex.analysis.MutualExclusiveAnalysis;
import org.gitools.plugins.mutex.analysis.MutualExclusiveProcessor;
import org.gitools.ui.core.commands.HeaderCommand;
import org.gitools.utils.progressmonitor.DefaultProgressMonitor;

import java.util.*;


public class MutalExclusiveAnalysisCommand extends HeaderCommand {


    private final String colGroupsPattern;
    private final String rowGroupsPattern;
    private final boolean isAllColumnsGroup;
    private static int permutations;
    private Heatmap results;

    public MutalExclusiveAnalysisCommand(String heatmap,
                                         MatrixDimensionKey sortDimension,
                                         String sort,
                                         String colGroupsPattern,
                                         String rowGroupsPattern,
                                         boolean isAllColumnsGroup,
                                         int permutations) {
        super(heatmap, sortDimension, sort, colGroupsPattern);

        this.colGroupsPattern = colGroupsPattern;
        this.rowGroupsPattern = rowGroupsPattern;
        this.isAllColumnsGroup = isAllColumnsGroup;
        this.permutations = permutations;
    }


    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {
        super.execute(monitor);

        Map<String, Set<String>> columnGroups = getColumnGroups();
        Map<String, Set<String>> rowGroups = getRowGroups();

        MutualExclusiveAnalysis analysis = new MutualExclusiveAnalysis();

        MutalExclusiveAnalysisCommand.prepareAnalysis(analysis, heatmap.getContents().getDimension(heatmapDimension.getId()), columnGroups, rowGroups, heatmap);

        MutualExclusiveProcessor processor = new MutualExclusiveProcessor(analysis);
        processor.run(monitor);

        results = analysis.getResults().get();


        //todo add via editor manager (Weld not working welll
        /*IEditorManager man = ApplicationContext.getEditorManger();
        man.getEditors();
        man.createEditor(results);*/
    }


    public Heatmap getResults() {
        return results;
    }

    public Map<String, Set<String>> getColumnGroups() {

        Map<String, Set<String>> colGroups = new HashMap<>();
        if (!Strings.isNullOrEmpty(colGroupsPattern)) {
            ClusteringData data = new AnnPatClusteringData(heatmap.getColumns(), colGroupsPattern);
            Clusters results = null;
            try {
                results = new AnnPatClusteringMethod().cluster(data, new DefaultProgressMonitor());
            } catch (ClusteringException e) {
                e.printStackTrace();
            }
            colGroups = results.getClustersMap();
        }

        if (isAllColumnsGroup) {
            colGroups.put("All columns", new HashSet<>(heatmap.getColumns().toList()));
        }


        return colGroups;
    }


    public Map<String, Set<String>> getRowGroups()  {

        ClusteringData data = new AnnPatClusteringData(heatmap.getRows(), rowGroupsPattern);
        Clusters results = null;
        try {
            results = new AnnPatClusteringMethod().cluster(data, new DefaultProgressMonitor());
        } catch (ClusteringException e) {
            e.printStackTrace();
        }
        return results.getClustersMap();

    }

    public static void prepareSingularAnalysis(MutualExclusiveAnalysis analysis, IMatrixDimension testDimension, ArrayList<String> singleModuleIds, Heatmap hm) {
        HashModuleMap weightMap = new HashModuleMap();
        HashModuleMap testMap = new HashModuleMap();
        IMatrixDimension weightDimension = testDimension.getId().equals(MatrixDimensionKey.ROWS) ?
                hm.getDimension(MatrixDimensionKey.COLUMNS) :
                hm.getDimension(MatrixDimensionKey.ROWS);

        weightMap.addMapping(weightDimension.getId().getLabel(), hm.newPosition().iterate(weightDimension));

        testMap.addMapping(testDimension.getId().getLabel(), singleModuleIds);

        analysis.setTestDimension(testDimension);
        analysis.setTestGroupsModuleMap(testMap);
        analysis.setWeightDimension(weightDimension);
        analysis.setWeightGroupsModuleMap(weightMap);
        analysis.setData(hm);
        analysis.setLayer(hm.getLayers().getTopLayer().getId());
        analysis.setEventFunction(hm.getLayers().get(analysis.getLayer()).getEventFunction());
    }

    public static void prepareAnalysis(MutualExclusiveAnalysis analysis,
                                       IMatrixDimension testDimension,
                                       Map<String, Set<String>> columnGroups,
                                       Map<String, Set<String>>  rowGroups,
                                       Heatmap hm) {

        HashModuleMap weightMap = new HashModuleMap();
        HashModuleMap testMap = new HashModuleMap();
        IMatrixDimension weightDimension = testDimension.getId().equals(MatrixDimensionKey.ROWS) ?
                hm.getDimension(MatrixDimensionKey.COLUMNS) :
                hm.getDimension(MatrixDimensionKey.ROWS);

        if (weightDimension.getId().equals(MatrixDimensionKey.ROWS)) {
            //interchagne column and rows group
            Map<String, Set<String>> tempGroup = rowGroups;
            rowGroups = columnGroups;
            columnGroups = tempGroup;
        }

        for (String key : columnGroups.keySet()) {
            weightMap.addMapping(key, columnGroups.get(key));
        }

        for (String key : rowGroups.keySet()) {
            testMap.addMapping(key, rowGroups.get(key));
        }


        analysis.setTestDimension(testDimension);
        analysis.setTestGroupsModuleMap(testMap);
        analysis.setWeightDimension(weightDimension);
        analysis.setWeightGroupsModuleMap(weightMap);
        analysis.setData(hm);
        analysis.setLayer(hm.getLayers().getTopLayer().getId());
        analysis.setEventFunction(hm.getLayers().get(analysis.getLayer()).getEventFunction());
        analysis.setIterations(permutations < 100 ? 100 : permutations);
    }

}
