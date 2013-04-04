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
package org.gitools.analysis.htest.oncozet;

import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.htest.HtestCommand;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.GeneSet;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.*;
import org.gitools.persistence.formats.analysis.OncodriveAnalysisXmlFormat;
import org.gitools.persistence.formats.matrix.AbstractMatrixFormat;
import org.gitools.persistence.formats.matrix.AbstractTextMatrixFormat;
import org.gitools.persistence.formats.matrix.MultiValueMatrixFormat;
import org.gitools.persistence.formats.modulemap.AbstractModuleMapFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OncodriveCommand extends HtestCommand
{

    protected IResourceFormat modulesFormat;
    protected String modulesPath;

    public OncodriveCommand(
            OncodriveAnalysis analysis,
            IResourceFormat dataMime,
            String dataPath,
            int valueIndex,
            String populationPath,
            Double populationDefaultValue,
            IResourceFormat modulesFormat,
            String modulesFile,
            String workdir,
            String fileName)
    {

        super(analysis, dataMime, dataPath, valueIndex,
                populationPath, populationDefaultValue,
                workdir, fileName);

        this.modulesFormat = modulesFormat;
        this.modulesPath = modulesFile;
    }

    @Override
    public void run(@NotNull IProgressMonitor monitor) throws AnalysisException
    {

        try
        {
            final OncodriveAnalysis oncozAnalysis = (OncodriveAnalysis) analysis;

            // Load data and modules

            monitor.begin("Loading ...", 1);
            monitor.info("Data: " + dataPath);
            monitor.info("Columns: " + modulesPath);

            loadDataAndModules(
                    dataFormat, dataPath,
                    valueIndex,
                    populationPath,
                    modulesFormat, modulesPath,
                    oncozAnalysis,
                    monitor.subtask());

            monitor.end();

            OncodriveProcessor processor = new OncodriveProcessor(oncozAnalysis);

            processor.run(monitor);

            // Save analysis

            save(oncozAnalysis, monitor);
        } catch (Exception ex)
        {
            throw new AnalysisException(ex);
        }
    }

    private void loadDataAndModules(
            IResourceFormat dataFormat,
            String dataFileName,
            int valueIndex,
            @Nullable String populationFileName,
            IResourceFormat modulesFormat,
            @Nullable String modulesFileName,
            @NotNull OncodriveAnalysis analysis,
            IProgressMonitor progressMonitor) throws PersistenceException
    {

        // Load background population

        String[] populationLabels = null;

        if (populationFileName != null)
        {
            IResourceLocator bgLocator = new UrlResourceLocator(new File(populationFileName));

            IResourceFormat<GeneSet> bgFormat = PersistenceManager.get().getFormat(populationFileName, GeneSet.class);
            GeneSet popLabels = PersistenceManager.get().load(bgLocator, bgFormat, progressMonitor);
            populationLabels = popLabels.toArray(new String[popLabels.size()]);
        }

        // Load data

        IResourceLocator dataLocator = new UrlResourceLocator(new File(dataFileName));
        Map<Integer, ValueTranslator> valueTranslators = new HashMap<Integer, ValueTranslator>();
        valueTranslators.put(0,
                createValueTranslator(
                        analysis.isBinaryCutoffEnabled(),
                        analysis.getBinaryCutoffCmp(),
                        analysis.getBinaryCutoffValue())
        );

        Properties dataProps = new Properties();
        dataProps.put(AbstractMatrixFormat.BINARY_VALUES, analysis.isBinaryCutoffEnabled());
        dataProps.put(AbstractMatrixFormat.VALUE_TRANSLATORS, valueTranslators);
        dataProps.put(MultiValueMatrixFormat.VALUE_INDICES, new int[]{valueIndex});
        if (populationLabels != null)
        {
            dataProps.put(AbstractTextMatrixFormat.POPULATION_LABELS, populationLabels);
            dataProps.put(AbstractTextMatrixFormat.BACKGROUND_VALUE, populationDefaultValue);
        }

        ResourceReference<BaseMatrix> dataMatrix = new ResourceReference<BaseMatrix>(dataLocator, dataFormat);
        dataMatrix.setProperties(dataProps);
        dataMatrix.load(progressMonitor);
        analysis.setData(new ResourceReference<IMatrix>("data", dataMatrix.get()));

        // Load modules

        if (modulesFileName != null)
        {
            IResourceLocator modulesLocator = new UrlResourceLocator(new File(modulesFileName));

            Properties modProps = new Properties();
            modProps.put(AbstractModuleMapFormat.ITEM_NAMES_FILTER_ENABLED, true);
            modProps.put(AbstractModuleMapFormat.ITEM_NAMES, dataMatrix.get().getColumnStrings());
            modProps.put(AbstractModuleMapFormat.MIN_SIZE, analysis.getMinModuleSize());
            modProps.put(AbstractModuleMapFormat.MAX_SIZE, analysis.getMaxModuleSize());

            ResourceReference<ModuleMap> moduleMap = new ResourceReference<ModuleMap>(modulesLocator, modulesFormat);
            moduleMap.setProperties(modProps);
            moduleMap.load(progressMonitor);

            analysis.setModuleMap(new ResourceReference<ModuleMap>("modules", moduleMap.get()));
        }
    }

    private void save(final OncodriveAnalysis analysis, IProgressMonitor monitor) throws PersistenceException
    {

        File workdirFile = new File(workdir);
        if (!workdirFile.exists())
        {
            workdirFile.mkdirs();
        }

        File file = new File(workdirFile, fileName);
        OncodriveAnalysisXmlFormat p = new OncodriveAnalysisXmlFormat();
        p.write(new UrlResourceLocator(file), analysis, monitor);
    }
}
