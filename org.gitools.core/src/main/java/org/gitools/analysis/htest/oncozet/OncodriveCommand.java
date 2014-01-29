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
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.core.model.IModuleMap;
import org.gitools.core.utils.ModuleMapUtils;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.analysis.OncodriveAnalysisFormat;
import org.gitools.persistence.locators.UrlResourceLocator;

import java.io.File;

public class OncodriveCommand extends HtestCommand {

    private final IResourceFormat modulesFormat;
    private final String modulesPath;

    public OncodriveCommand(OncodriveAnalysis analysis, IResourceFormat dataMime, String dataPath, int valueIndex, String populationPath, Double populationDefaultValue, IResourceFormat modulesFormat, String modulesFile, String workdir, String fileName) {

        super(analysis, dataMime, dataPath, valueIndex, populationPath, populationDefaultValue, workdir, fileName);

        this.modulesFormat = modulesFormat;
        this.modulesPath = modulesFile;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException {

        try {
            final OncodriveAnalysis oncozAnalysis = (OncodriveAnalysis) analysis;

            // Load data and modules

            monitor.begin("Loading ...", 1);
            monitor.info("Data: " + dataPath);
            monitor.info("Columns: " + modulesPath);

            loadDataAndModules(dataFormat, dataPath, valueIndex, populationPath, modulesFormat, modulesPath, oncozAnalysis, monitor.subtask());

            monitor.end();

            OncodriveProcessor processor = new OncodriveProcessor(oncozAnalysis);

            processor.run(monitor);

            // Save analysis

            save(oncozAnalysis, monitor);
        } catch (Exception ex) {
            throw new AnalysisException(ex);
        }
    }

    private void loadDataAndModules(IResourceFormat dataFormat, String dataFileName, int valueIndex, String populationFileName, IResourceFormat modulesFormat, String modulesFileName, OncodriveAnalysis analysis, IProgressMonitor progressMonitor) throws PersistenceException {

        ResourceReference<IMatrix> dataMatrixRef = new ResourceReference<IMatrix>(new UrlResourceLocator(new File(dataFileName)), dataFormat);
        IMatrix dataMatrix = dataMatrixRef.load(progressMonitor);

        //TODO
        /*
         // Load background population
        String[] populationLabels = null;

        if (populationFileName != null) {
            IResourceLocator bgLocator = new UrlResourceLocator(new File(populationFileName));

            IResourceFormat<GeneSet> bgFormat = ApplicationContext.getPersistenceManager().getFormat(populationFileName, GeneSet.class);
            GeneSet popLabels = ApplicationContext.getPersistenceManager().load(bgLocator, bgFormat, progressMonitor);
            populationLabels = popLabels.toArray(new String[popLabels.size()]);
        }

        Map<Integer, ValueTranslator> valueTranslators = new HashMap<Integer, ValueTranslator>();
        valueTranslators.put(0, createValueTranslator(analysis.isBinaryCutoffEnabled(), analysis.getBinaryCutoffCmp(), analysis.getBinaryCutoffValue()));

        Properties dataProps = new Properties();
        dataProps.put(AbstractMatrixFormat.BINARY_VALUES, analysis.isBinaryCutoffEnabled());
        dataProps.put(AbstractMatrixFormat.VALUE_TRANSLATORS, valueTranslators);
        dataProps.put(TdmMatrixFormat.VALUE_INDICES, new int[]{valueIndex});
        if (populationLabels != null) {
            dataProps.put(AbstractCdmMatrixFormat.POPULATION_LABELS, populationLabels);
            dataProps.put(AbstractCdmMatrixFormat.BACKGROUND_VALUE, populationDefaultValue);
        }
        */
        analysis.setData(new ResourceReference<>("data", dataMatrix));

        // Load modules
        if (modulesFileName != null) {

            ResourceReference<IModuleMap> moduleMapRef = new ResourceReference<IModuleMap>(new UrlResourceLocator(new File(modulesFileName)), modulesFormat);
            IModuleMap moduleMap = moduleMapRef.load(progressMonitor);
            moduleMap = ModuleMapUtils.filterByItems(moduleMap, dataMatrix.getColumns());
            moduleMap = ModuleMapUtils.filterByModuleSize(moduleMap, analysis.getMinModuleSize(), analysis.getMaxModuleSize());

            analysis.setModuleMap(new ResourceReference<>("modules", moduleMap));
        }
    }

    private void save(final OncodriveAnalysis analysis, IProgressMonitor monitor) throws PersistenceException {

        File workdirFile = new File(workdir);
        if (!workdirFile.exists()) {
            workdirFile.mkdirs();
        }

        File file = new File(workdirFile, fileName);
        OncodriveAnalysisFormat p = new OncodriveAnalysisFormat();
        p.write(new UrlResourceLocator(file), analysis, monitor);
    }
}
