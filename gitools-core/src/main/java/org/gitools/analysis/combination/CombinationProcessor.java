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
package org.gitools.analysis.combination;

import cern.jet.stat.Probability;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.matrix.ObjectMatrix;
import org.gitools.matrix.model.matrix.element.BeanElementAdapter;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.ResourceReference;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;

public class CombinationProcessor implements AnalysisProcessor
{

    private final CombinationAnalysis analysis;

    public CombinationProcessor(CombinationAnalysis analysis)
    {
        this.analysis = analysis;
    }

    @Override
    public void run(@NotNull IProgressMonitor monitor) throws AnalysisException
    {

        Date startTime = new Date();

        // Prepare data
        IMatrix data = analysis.getData().get();
        if (analysis.isTransposeData())
        {
            data = new TransposedMatrixView(data);
        }

        final int numCols = data.getColumns().size();
        final int numRows = data.getRows().size();

        String[] labels = new String[numCols];
        for (int i = 0; i < numCols; i++)
            labels[i] = data.getColumns().getLabel(i);

        String combOf = analysis.isTransposeData() ? "rows" : "columns";

        // Prepare columns map
        ModuleMap cmap = analysis.getGroupsMap().get();
        if (cmap != null)
        {
            cmap = cmap.remap(labels);
        }
        else
        {
            cmap = new ModuleMap("All data " + combOf, labels);
        }
        analysis.setGroupsMap(new ResourceReference<ModuleMap>("modules", cmap));

        // Prepare results matrix
        final ObjectMatrix results = new ObjectMatrix();

        String[] cclabels = cmap.getModuleNames();
        cclabels = Arrays.copyOf(cclabels, cclabels.length);
        String[] rlabels = new String[numRows];
        for (int i = 0; i < numRows; i++)
            rlabels[i] = data.getRows().getLabel(i);

        results.setColumns(cclabels);
        results.setRows(rlabels);
        results.makeCells();

        results.setObjectCellAdapter(new BeanElementAdapter(CombinationResult.class));

        analysis.setResults(new ResourceReference<IMatrix>("results", results));

        // Run combination
        int sizeIndex = -1;
        String sizeAttrName = analysis.getSizeAttrName();
        /*if (sizeAttrName == null || sizeAttrName.isEmpty())
            sizeIndex = analysis.getSizeAttrIndex();*/
        if (sizeAttrName != null && !sizeAttrName.isEmpty())
        {
            sizeIndex = data.getLayers().findId(sizeAttrName);
        }

        int pvalueIndex = 0;
        String pvalueAttrName = analysis.getPvalueAttrName();
        /*if (pvalueAttrName == null || pvalueAttrName.isEmpty())
            pvalueIndex = analysis.getPvalueAttrIndex();*/
        if (pvalueAttrName != null && !pvalueAttrName.isEmpty())
        {
            pvalueIndex = data.getLayers().findId(pvalueAttrName);
        }

        MatrixUtils.DoubleCast sizeCast = null;

        if (sizeIndex >= 0)
        {
            sizeCast = MatrixUtils.createDoubleCast(data.getLayers().get(sizeIndex).getValueClass());
        }

        MatrixUtils.DoubleCast pvalueCast = MatrixUtils.createDoubleCast(data.getLayers().get(pvalueIndex).getValueClass());

        int numCC = cmap.getModuleCount();

        monitor.begin("Running combination analysis ...", numCC * numRows);

        for (int cmi = 0; cmi < numCC; cmi++)
        {
            int[] cindices = cmap.getItemIndices(cmi);
            for (int ri = 0; ri < numRows; ri++)
            {
                int n = 0;
                double sumSizeZ = 0;
                double sumSizeSqr = 0;

                for (int ci = 0; ci < cindices.length; ci++)
                {
                    int mci = cindices[ci];

                    if (!data.isEmpty(ri, mci))
                    {
                        double size = sizeIndex < 0 ? 1 : sizeCast.getDoubleValue(data.getCellValue(ri, mci, sizeIndex));

                        double pvalue = pvalueCast.getDoubleValue(data.getCellValue(ri, mci, pvalueIndex));

                        double zscore = pvalueToZscore(pvalue);

                        if (!Double.isNaN(size + pvalue + zscore))
                        {
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

                results.setCell(ri, cmi, r);

                monitor.worked(1);
            }
        }

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

        monitor.end();
    }

    private double pvalueToZscore(double pvalue)
    {
        if (Double.isNaN(pvalue))
        {
            return pvalue;
        }

        pvalue = 1.0 - pvalue;
        pvalue = pvalue < 0.000001 ? 0.000001 : pvalue;
        pvalue = pvalue > 0.999999 ? 0.999999 : pvalue;

        double zscore = Probability.normalInverse(pvalue);
        return zscore;
    }

    private double zscoreToPvalue(double zscore)
    {
        if (Double.isNaN(zscore))
        {
            return zscore;
        }

        double pvalue = 1.0 - Probability.normal(zscore);
        return pvalue;
    }

}
