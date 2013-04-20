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
package org.gitools.analysis;

import org.gitools.datafilters.BinaryCutoff;
import org.gitools.datafilters.BinaryCutoffTranslator;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

public abstract class AnalysisCommand {

    protected String workdir;

    protected String fileName;

    protected boolean storeAnalysis;

    protected AnalysisCommand(String workdir, String fileName) {
        this.workdir = workdir;
        this.fileName = fileName;
        this.storeAnalysis = true;
    }

    public String getWorkdir() {
        return workdir;
    }

    public void setWorkdir(String workdir) {
        this.workdir = workdir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isStoreAnalysis() {
        return storeAnalysis;
    }

    public void setStoreAnalysis(boolean storeAnalysis) {
        this.storeAnalysis = storeAnalysis;
    }

    public abstract void run(IProgressMonitor monitor) throws AnalysisException;

    @NotNull
    protected ValueTranslator createValueTranslator(boolean binaryCutoffEnabled, CutoffCmp cutoffCmp, Double cutoffValue) {

        return binaryCutoffEnabled ? new BinaryCutoffTranslator(new BinaryCutoff(cutoffCmp, cutoffValue)) : new DoubleTranslator();
    }
}
