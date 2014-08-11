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

import org.gitools.api.ApplicationContext;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.modulemap.HashModuleMap;
import org.gitools.plugins.mutex.analysis.MutualExclusiveAnalysis;
import org.gitools.plugins.mutex.analysis.MutualExclusiveProcessor;
import org.gitools.ui.core.commands.HeatmapCommand;

import java.util.ArrayList;


public class MutualExclusiveAnalysisCommand extends HeatmapCommand {


    private Heatmap results;
    private MutualExclusiveAnalysis analysis;

    public MutualExclusiveAnalysisCommand(MatrixDimensionKey testDimensionKey,
                                          String colGroupsPattern,
                                          String rowGroupsPattern,
                                          boolean isAllColumnsGroup,
                                          int permutations,
                                          boolean discardEmpty) {
        super("");



        analysis = new MutualExclusiveAnalysis(heatmap);
        prepareAnalysisDetails(analysis, heatmap, testDimensionKey, colGroupsPattern, rowGroupsPattern, isAllColumnsGroup,
                permutations, discardEmpty);

    }

    public MutualExclusiveAnalysisCommand(MutualExclusiveAnalysis analysis) {
        super("");
        this.analysis = analysis;
    }  

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {
        super.execute(monitor);

        MutualExclusiveProcessor processor = new MutualExclusiveProcessor(analysis);
        processor.run(monitor);
        ApplicationContext.getEditorManger().addEditor(analysis);
    }

    public static void prepareSingularAnalysis(MutualExclusiveAnalysis analysis, IMatrixDimension testDimension, ArrayList<String> singleModuleIds, Heatmap hm) {
        HashModuleMap colMap = new HashModuleMap();
        HashModuleMap rowMap = new HashModuleMap();
        IMatrixDimension weightDimension = testDimension.getId().equals(MatrixDimensionKey.ROWS) ?
                hm.getDimension(MatrixDimensionKey.COLUMNS) :
                hm.getDimension(MatrixDimensionKey.ROWS);

        colMap.addMapping(weightDimension.getId().getLabel(), hm.newPosition().iterate(weightDimension));

        rowMap.addMapping(testDimension.getId().getLabel(), singleModuleIds);

        analysis.setTestDimension(testDimension);
        analysis.setRowModuleMap(rowMap);
        analysis.setColumnsModuleMap(colMap);
        analysis.setLayer(hm.getLayers().getTopLayer().getId());
        analysis.setEventFunction(hm.getLayers().get(analysis.getLayer()).getEventFunction());
    }

    private void prepareAnalysisDetails(MutualExclusiveAnalysis analysis, Heatmap heatmap, MatrixDimensionKey testDimension, String colGroupsPattern, String rowGroupsPattern, boolean isAllColumnsGroup, int permutations, boolean discardEmpty) {

        analysis.setTestDimension(heatmap.getDimension(testDimension));
        analysis.setLayer(heatmap.getLayers().getTopLayer().getId());
        analysis.setEventFunction(heatmap.getLayers().get(analysis.getLayer()).getEventFunction());
        analysis.setIterations(permutations < 100 ? 100 : permutations);
        analysis.setDiscardEmpty(discardEmpty);
        analysis.setRowModuleMap(MutualExclusiveAnalysis.createModules(rowGroupsPattern, false, heatmap.getRows()));
        analysis.setColumnsModuleMap(MutualExclusiveAnalysis.createModules(colGroupsPattern, isAllColumnsGroup, heatmap.getColumns()));

    }

}
