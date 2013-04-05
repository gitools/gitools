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

import org.gitools.analysis.AnalysisCommand;
import org.gitools.analysis.AnalysisException;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.File;


public class CombinationCommand extends AnalysisCommand
{

    private final CombinationAnalysis analysis;

    private final IResourceFormat dataFormat;
    private final String dataPath;

    private final IResourceFormat columnsFormat;
    private final String columnsPath;

    public CombinationCommand(CombinationAnalysis analysis, IResourceFormat dataFormat, String dataPath, IResourceFormat columnsFormat, String columnsPath, String workdir, String fileName)
    {

        super(workdir, fileName);

        this.analysis = analysis;
        this.dataFormat = dataFormat;
        this.dataPath = dataPath;
        this.columnsFormat = columnsFormat;
        this.columnsPath = columnsPath;

        this.storeAnalysis = true;
    }

    @Override
    public void run(IProgressMonitor progressMonitor) throws AnalysisException
    {
        try
        {
            if (analysis.getData() == null)
            {
                ResourceReference<IMatrix> data = new ConvertModuleMapToMatrixResourceReference(new UrlResourceLocator(new File(dataPath)), dataFormat);
                analysis.setData(data);
                data.load(progressMonitor);
            }

            if (columnsPath != null)
            {
                ResourceReference<ModuleMap> columnsMap = new ConvertMatrixToModuleMapResourceReference(new UrlResourceLocator(new File(columnsPath)), columnsFormat);
                analysis.setGroupsMap(columnsMap);
                columnsMap.load(progressMonitor);
            }

            CombinationProcessor proc = new CombinationProcessor(analysis);

            proc.run(progressMonitor);

            if (storeAnalysis)
            {
                File workdirFile = new File(workdir);
                if (!workdirFile.exists())
                {
                    workdirFile.mkdirs();
                }

                UrlResourceLocator resourceLocator = new UrlResourceLocator(new File(workdirFile, fileName));
                PersistenceManager.get().store(resourceLocator, analysis, progressMonitor);
            }
        } catch (Throwable cause)
        {
            throw new AnalysisException(cause);
        }
    }
}
