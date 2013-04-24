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
package org.gitools.core.analysis.htest.enrichment;

import org.gitools.core.analysis.AnalysisException;
import org.gitools.core.analysis.htest.HtestCommand;
import org.gitools.core.datafilters.ValueTranslator;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.matrix.BaseMatrix;
import org.gitools.core.model.GeneSet;
import org.gitools.core.model.ModuleMap;
import org.gitools.core.persistence.*;
import org.gitools.core.persistence.formats.analysis.EnrichmentAnalysisFormat;
import org.gitools.core.persistence.formats.matrix.AbstractMatrixFormat;
import org.gitools.core.persistence.formats.matrix.AbstractTextMatrixFormat;
import org.gitools.core.persistence.formats.matrix.MultiValueMatrixFormat;
import org.gitools.core.persistence.formats.modulemap.AbstractModuleMapFormat;
import org.gitools.core.persistence.locators.UrlResourceLocator;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class EnrichmentCommand extends HtestCommand {

    private final IResourceFormat modulesFormat;
    private final String modulesPath;

    public EnrichmentCommand(EnrichmentAnalysis analysis, IResourceFormat dataFormat, String dataFile, int valueIndex, String populationPath, Double populationDefaultValue, IResourceFormat modulesFormat, String modulesFile, String workdir, String fileName) {

        super(analysis, dataFormat, dataFile, valueIndex, populationPath, populationDefaultValue, workdir, fileName);

        this.modulesFormat = modulesFormat;
        this.modulesPath = modulesFile;
    }

    @Override
    public void run(@NotNull IProgressMonitor monitor) throws AnalysisException {

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
    private void loadDataAndModules(IResourceFormat dataFormat, String dataFileName, int valueIndex, @Nullable String populationFileName, IResourceFormat modulesFormat, String modulesFileName, @NotNull EnrichmentAnalysis analysis, IProgressMonitor monitor) throws PersistenceException {

        // Load background population

        String[] populationLabels = null;

        if (populationFileName != null) {
            IResourceLocator resourceLocator = new UrlResourceLocator(new File(populationFileName));

            IResourceFormat<GeneSet> resourceFormat = PersistenceManager.get().getFormat(populationFileName, GeneSet.class);
            GeneSet popLabels = PersistenceManager.get().load(resourceLocator, resourceFormat, monitor);

            populationLabels = popLabels.toArray(new String[popLabels.size()]);
        }

        // Load data

        IResourceLocator dataLocator = new UrlResourceLocator(new File(dataFileName));

        Map<Integer, ValueTranslator> valueTranslators = new HashMap<Integer, ValueTranslator>();
        valueTranslators.put(0, createValueTranslator(analysis.isBinaryCutoffEnabled(), analysis.getBinaryCutoffCmp(), analysis.getBinaryCutoffValue()));

        Properties dataProps = new Properties();
        dataProps.put(AbstractMatrixFormat.BINARY_VALUES, analysis.isBinaryCutoffEnabled());
        dataProps.put(AbstractMatrixFormat.VALUE_TRANSLATORS, valueTranslators);
        dataProps.put(MultiValueMatrixFormat.VALUE_INDICES, new int[]{valueIndex});
        if (populationLabels != null) {
            dataProps.put(AbstractTextMatrixFormat.POPULATION_LABELS, populationLabels);
            dataProps.put(AbstractTextMatrixFormat.BACKGROUND_VALUE, populationDefaultValue);
        }

        ResourceReference<BaseMatrix> dataMatrix = new ResourceReference<BaseMatrix>(dataLocator, this.dataFormat);
        dataMatrix.setProperties(dataProps);
        dataMatrix.load(monitor);

        // Load modules
        IResourceLocator modLocator = new UrlResourceLocator(new File(modulesFileName));

        Properties modProps = new Properties();
        modProps.put(AbstractModuleMapFormat.ITEM_NAMES_FILTER_ENABLED, true);
        modProps.put(AbstractModuleMapFormat.ITEM_NAMES, dataMatrix.get().getRowStrings());
        modProps.put(AbstractModuleMapFormat.MIN_SIZE, analysis.getMinModuleSize());
        modProps.put(AbstractModuleMapFormat.MAX_SIZE, analysis.getMaxModuleSize());

        ResourceReference<ModuleMap> moduleMap = new ResourceReference<ModuleMap>(modLocator, this.modulesFormat);
        moduleMap.setProperties(modProps);
        moduleMap.load(monitor);

        // Filter rows if DiscardNonMappedRows is enabled
        if (analysis.isDiscardNonMappedRows()) {

            BaseMatrix fmatrix = null;
            try {
                fmatrix = dataMatrix.get().getClass().newInstance();
                fmatrix.setObjectCellAdapter(dataMatrix.get().getObjectCellAdapter());
            } catch (Exception ex) {
                throw new PersistenceException("Error filtering data matrix.", ex);
            }

            List<Integer> rows = new ArrayList<Integer>();

            String[] names = moduleMap.get().getItemNames();
            Set<String> backgroundNames = new HashSet<String>();
            backgroundNames.addAll(Arrays.asList(names));

            for (int i = 0; i < dataMatrix.get().getRows().size(); i++)
                if (backgroundNames.contains(dataMatrix.get().internalRowLabel(i))) {
                    rows.add(i);
                }

            int numRows = rows.size();
            int numColumns = dataMatrix.get().getColumns().size();

            fmatrix.make(numRows, numColumns);
            fmatrix.setColumns(dataMatrix.get().getInternalColumns());

            for (int ri = 0; ri < numRows; ri++) {
                int srcRow = rows.get(ri);
                fmatrix.setRow(ri, dataMatrix.get().internalRowLabel(srcRow));
                for (int ci = 0; ci < numColumns; ci++) {
                    Object value = dataMatrix.get().getValue(srcRow, ci, 0);
                    fmatrix.setValue(ri, ci, 0, value);
                }
            }

            analysis.setData(new ResourceReference<IMatrix>("data", fmatrix));
        } else {
            analysis.setData(new ResourceReference<IMatrix>("data", dataMatrix.get()));
        }

        analysis.setModuleMap(new ResourceReference<ModuleMap>("modules", moduleMap.get()));
    }
}
