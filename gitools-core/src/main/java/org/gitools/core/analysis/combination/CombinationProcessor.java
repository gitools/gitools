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
package org.gitools.core.analysis.combination;

import cern.jet.stat.Probability;
import org.gitools.core.analysis.AnalysisException;
import org.gitools.core.analysis.AnalysisProcessor;
import org.gitools.core.matrix.TransposedMatrixView;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.matrix.model.matrix.element.BeanElementAdapter;
import org.gitools.core.matrix.model.matrix.element.ElementAdapter;
import org.gitools.core.model.HashModuleMap;
import org.gitools.core.model.IModuleMap;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.core.utils.ModuleMapUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.Date;

public class CombinationProcessor implements AnalysisProcessor {

    private final CombinationAnalysis analysis;

    public CombinationProcessor(CombinationAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException {

        Date startTime = new Date();

        // Prepare data
        IMatrix data = analysis.getData().get();
        if (analysis.isTransposeData()) {
            data = new TransposedMatrixView(data);
        }

        final int numRows = data.getRows().size();

        String combOf = analysis.isTransposeData() ? "rows" : "columns";

        // Prepare columns map
        IModuleMap cmap;
        if (analysis.getGroupsMap() == null) {
            cmap = new HashModuleMap().addMapping("All data " + combOf, data.getColumns());
        } else {
            cmap = ModuleMapUtils.filterByItems( analysis.getGroupsMap().get(), data.getColumns() );
        }
        analysis.setGroupsMap(new ResourceReference<>("modules", cmap));

        // Prepare results matrix
        final ElementAdapter adapter = new BeanElementAdapter(CombinationResult.class);
        final IMatrix results = new HashMatrix(data.getRows(), cmap.getModules(), adapter.getMatrixLayers());

        analysis.setResults(new ResourceReference<>("results", results));

        // Run combination
        int sizeIndex = -1;
        String sizeAttrName = analysis.getSizeAttrName();
        if (sizeAttrName != null && !sizeAttrName.isEmpty()) {
            sizeIndex = data.getLayers().getIndex(sizeAttrName);
        }

        int pvalueIndex = 0;
        String pvalueAttrName = analysis.getPvalueAttrName();
        if (pvalueAttrName != null && !pvalueAttrName.isEmpty()) {
            pvalueIndex = data.getLayers().getIndex(pvalueAttrName);
        }

        MatrixUtils.DoubleCast sizeCast = null;

        if (sizeIndex >= 0) {
            sizeCast = MatrixUtils.createDoubleCast(data.getLayers().get(sizeIndex).getValueClass());
        }

        MatrixUtils.DoubleCast pvalueCast = MatrixUtils.createDoubleCast(data.getLayers().get(pvalueIndex).getValueClass());

        int numCC = cmap.getModules().size();

        monitor.begin("Running combination analysis ...", numCC * numRows);

        int cmi = 0;
        for (String module : cmap.getModules()) {
            int[] cindices = cmap.getItemIndices(module);
            for (int ri = 0; ri < numRows; ri++) {
                int n = 0;
                double sumSizeZ = 0;
                double sumSizeSqr = 0;

                for (int ci = 0; ci < cindices.length; ci++) {
                    int mci = cindices[ci];

                    if (data.getValue(ri, mci, pvalueIndex) != null) {
                        double size = sizeIndex < 0 ? 1 : sizeCast.getDoubleValue(data.getValue(ri, mci, sizeIndex));

                        double pvalue = pvalueCast.getDoubleValue(data.getValue(ri, mci, pvalueIndex));

                        double zscore = pvalueToZscore(pvalue);

                        if (!Double.isNaN(size + pvalue + zscore)) {
                            n++;
                            sumSizeZ += size * zscore;
                            sumSizeSqr += size * size;
                        }
                    }
                }

                double zcomb = sumSizeZ / Math.sqrt(sumSizeSqr);
                double pvalue = zscoreToPvalue(zcomb);

                CombinationResult r = new CombinationResult();
                r.setN(n);
                r.setZscore(zcomb);
                r.setPvalue(pvalue);

                adapter.setCell(results, ri, cmi, r);

                monitor.worked(1);
                cmi++;
            }
        }

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

        monitor.end();
    }

    private double pvalueToZscore(double pvalue) {
        if (Double.isNaN(pvalue)) {
            return pvalue;
        }

        pvalue = 1.0 - pvalue;
        pvalue = pvalue < 0.000001 ? 0.000001 : pvalue;
        pvalue = pvalue > 0.999999 ? 0.999999 : pvalue;

        double zscore = Probability.normalInverse(pvalue);
        return zscore;
    }

    private double zscoreToPvalue(double zscore) {
        if (Double.isNaN(zscore)) {
            return zscore;
        }

        double pvalue = 1.0 - Probability.normal(zscore);
        return pvalue;
    }

}
