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
package org.gitools.analysis.htest.enrichment;

import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.htest.HtestCommand;
import org.gitools.analysis.htest.enrichment.format.EnrichmentAnalysisFormat;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.analysis._DEPRECATED.model.IModuleMap;
import org.gitools.analysis._DEPRECATED.utils.ModuleMapUtils;
import org.gitools.api.resource.ResourceReference;
import org.gitools.persistence.locators.UrlResourceLocator;

import java.io.File;

public class EnrichmentCommand extends HtestCommand {

    private final IResourceFormat modulesFormat;
    private final String modulesPath;

    public EnrichmentCommand(EnrichmentAnalysis analysis, IResourceFormat dataFormat, String dataFile, int valueIndex, String populationPath, Double populationDefaultValue, IResourceFormat modulesFormat, String modulesFile, String workdir, String fileName) {

        super(analysis, dataFormat, dataFile, valueIndex, populationPath, populationDefaultValue, workdir, fileName);

        this.modulesFormat = modulesFormat;
        this.modulesPath = modulesFile;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException {

        try {
            final EnrichmentAnalysis enrichAnalysis = (EnrichmentAnalysis) analysis;

            // Load data and modules
            monitor.begin("Loading ...", 1);
            monitor.info("Data: " + dataPath);
            monitor.info("Modules: " + modulesPath);

            loadDataAndModules(dataFormat, dataPath, valueIndex, populationPath, modulesFormat, modulesPath, enrichAnalysis, monitor.subtask());

            monitor.end();

            // Create and process analysis
            EnrichmentProcessor processor = new EnrichmentProcessor(enrichAnalysis);

            processor.run(monitor);

            save(enrichAnalysis, monitor);
        } catch (Throwable ex) {
            throw new AnalysisException(ex);
        }
    }

    private void save(final EnrichmentAnalysis analysis, IProgressMonitor monitor) throws PersistenceException {

        File workdirFile = new File(workdir);
        if (!workdirFile.exists()) {
            workdirFile.mkdirs();
        }

        File file = new File(workdirFile, fileName);
        EnrichmentAnalysisFormat p = new EnrichmentAnalysisFormat();
        p.write(new UrlResourceLocator(file), analysis, monitor);
    }

    /**
     * Loads data and modules taking into account filtering
     *
     * @param dataFormat
     * @param dataFileName
     * @param valueIndex
     * @param modulesFormat
     * @param modulesFileName
     * @param analysis
     * @param monitor
     * @throws PersistenceException
     */
    private void loadDataAndModules(IResourceFormat dataFormat, String dataFileName, int valueIndex, String populationFileName, IResourceFormat modulesFormat, String modulesFileName, EnrichmentAnalysis analysis, IProgressMonitor monitor) throws PersistenceException {

        // Load data matrix
        ResourceReference<IMatrix> dataMatrixReference = new ResourceReference<IMatrix>(new UrlResourceLocator(new File(dataFileName)), this.dataFormat);
        IMatrix dataMatrix = dataMatrixReference.load(monitor);

        //TODO
        /*
        // Load background population
        String[] populationLabels = null;
        if (populationFileName != null) {
            IResourceLocator resourceLocator = new UrlResourceLocator(new File(populationFileName));

            IResourceFormat<GeneSet> resourceFormat = ApplicationContext.getPersistenceManager().getFormat(populationFileName, GeneSet.class);
            GeneSet popLabels = ApplicationContext.getPersistenceManager().load(resourceLocator, resourceFormat, monitor);

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
        }*/
        analysis.setData(new ResourceReference<>("data", dataMatrix));

        // Load and filter module map
        ResourceReference<IModuleMap> moduleMapRef = new ResourceReference<IModuleMap>(new UrlResourceLocator(new File(modulesFileName)), this.modulesFormat);
        IModuleMap moduleMap = moduleMapRef.load(monitor);
        moduleMap = ModuleMapUtils.filterByItems(moduleMap, dataMatrix.getRows());
        moduleMap = ModuleMapUtils.filterByModuleSize(moduleMap, analysis.getMinModuleSize(), analysis.getMaxModuleSize());
        analysis.setModuleMap(new ResourceReference<>("modules", moduleMap));
    }

}
