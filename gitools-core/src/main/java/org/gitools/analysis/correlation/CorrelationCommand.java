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
package org.gitools.analysis.correlation;

import org.gitools.analysis.AnalysisCommand;
import org.gitools.analysis.AnalysisException;
import org.gitools.matrix.model.IMatrix;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CorrelationCommand extends AnalysisCommand
{

    protected CorrelationAnalysis analysis;
    protected IResourceFormat dataFormat;
    protected String dataPath;

    public CorrelationCommand(
            CorrelationAnalysis analysis,
            IResourceFormat dataFormat, String dataPath,
            String workdir, String fileName)
    {

        super(workdir, fileName);

        this.analysis = analysis;
        this.dataFormat = dataFormat;
        this.dataPath = dataPath;
    }

    @Override
    public void run(@NotNull IProgressMonitor progressMonitor) throws AnalysisException
    {

        try
        {

            ResourceReference<IMatrix> dataReference = new ResourceReference<IMatrix>(new UrlResourceLocator(new File(dataPath)), dataFormat);

            analysis.setData(new ResourceReference<IMatrix>("data", dataReference.get()));
            dataReference.load(progressMonitor);

            CorrelationProcessor proc = new CorrelationProcessor(analysis);
            proc.run(progressMonitor);

            File workdirFile = new File(workdir);
            if (!workdirFile.exists())
            {
                workdirFile.mkdirs();
            }

            IResourceLocator resourceLocator = new UrlResourceLocator(
                    new File(workdirFile, fileName)
            );
            PersistenceManager.get().store(resourceLocator, analysis, progressMonitor);
        } catch (Throwable cause)
        {
            throw new AnalysisException(cause);
        }
    }

}
