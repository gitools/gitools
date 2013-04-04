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
package org.gitools.analysis.overlapping;

import org.gitools.analysis.AnalysisCommand;
import org.gitools.analysis.AnalysisException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.File;


public class OverlappingCommand extends AnalysisCommand
{

    private OverlappingAnalysis analysis;

    public OverlappingCommand(OverlappingAnalysis analysis, String workdir, String fileName)
    {
        super(workdir, fileName);

        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor progressMonitor) throws AnalysisException
    {
        try
        {
            OverlappingProcessor proc = new OverlappingProcessor(analysis);

            proc.run(progressMonitor);

            if (storeAnalysis)
            {
                File workdirFile = new File(workdir);
                if (!workdirFile.exists())
                {
                    workdirFile.mkdirs();
                }

                PersistenceManager.get().store(
                        new UrlResourceLocator(new File(workdirFile, fileName)),
                        analysis,
                        progressMonitor
                );
            }
        } catch (Throwable cause)
        {
            throw new AnalysisException(cause);
        }
    }
}
