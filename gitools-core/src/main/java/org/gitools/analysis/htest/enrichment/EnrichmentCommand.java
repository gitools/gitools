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
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.model.GeneSet;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.formats.analysis.EnrichmentAnalysisXmlFormat;
import org.gitools.persistence.formats.matrix.AbstractMatrixFormat;
import org.gitools.persistence.formats.matrix.AbstractTextMatrixFormat;
import org.gitools.persistence.formats.matrix.MultiValueMatrixFormat;
import org.gitools.persistence.formats.modulemap.AbstractModuleMapFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.File;
import java.util.*;

public class EnrichmentCommand extends HtestCommand
{

    protected String modulesMime;
    protected String modulesPath;

    public EnrichmentCommand(
            EnrichmentAnalysis analysis,
            String dataMime,
            String dataFile,
            int valueIndex,
            String populationPath,
            Double populationDefaultValue,
            String modulesMime,
            String modulesFile,
            String workdir,
            String fileName)
    {

        super(analysis, dataMime, dataFile, valueIndex,
                populationPath, populationDefaultValue,
                workdir, fileName);

        this.modulesMime = modulesMime;
        this.modulesPath = modulesFile;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException
    {

        try
        {
            final EnrichmentAnalysis enrichAnalysis = (EnrichmentAnalysis) analysis;

            // Load data and modules

            monitor.begin("Loading ...", 1);
            monitor.info("Data: " + dataPath);
            monitor.info("Modules: " + modulesPath);

            loadDataAndModules(
                    dataMime, dataPath,
                    valueIndex,
                    populationPath,
                    modulesMime, modulesPath,
                    enrichAnalysis,
                    monitor.subtask());

            monitor.end();

            // Create and process analysis

            EnrichmentProcessor processor = new EnrichmentProcessor(enrichAnalysis);

            processor.run(monitor);

            save(enrichAnalysis, monitor);
        } catch (Throwable ex)
        {
            throw new AnalysisException(ex);
        }
    }

    private void save(final EnrichmentAnalysis analysis, IProgressMonitor monitor) throws PersistenceException
    {

        File workdirFile = new File(workdir);
        if (!workdirFile.exists())
        {
            workdirFile.mkdirs();
        }

        File file = new File(workdirFile, fileName);
        EnrichmentAnalysisXmlFormat p = new EnrichmentAnalysisXmlFormat();
        p.write(new UrlResourceLocator(file), analysis, monitor);
    }

    /**
     * Loads data and modules taking into account filtering
     *
     * @param dataFileMime
     * @param dataFileName
     * @param valueIndex
     * @param modulesFileMime
     * @param modulesFileName
     * @param analysis
     * @param monitor
     * @throws PersistenceException
     */
    private void loadDataAndModules(
            String dataFileMime,
            String dataFileName,
            int valueIndex,
            String populationFileName,
            String modulesFileMime,
            String modulesFileName,
            EnrichmentAnalysis analysis,
            IProgressMonitor monitor)
            throws PersistenceException
    {

        // Load background population

        String[] populationLabels = null;

        if (populationFileName != null)
        {
            IResourceLocator resourceLocator = new UrlResourceLocator(new File(populationFileName));

            GeneSet popLabels = PersistenceManager.get().load(resourceLocator, GeneSet.class, monitor);

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

        BaseMatrix dataMatrix = loadDataMatrix(dataLocator, dataProps, monitor);

        // Load modules

        IResourceLocator modLocator = new UrlResourceLocator(new File(modulesFileName));

        Properties modProps = new Properties();
        modProps.put(AbstractModuleMapFormat.ITEM_NAMES_FILTER_ENABLED, true);
        modProps.put(AbstractModuleMapFormat.ITEM_NAMES, dataMatrix.getRowStrings());
        modProps.put(AbstractModuleMapFormat.MIN_SIZE, analysis.getMinModuleSize());
        modProps.put(AbstractModuleMapFormat.MAX_SIZE, analysis.getMaxModuleSize());

        ModuleMap moduleMap = loadModuleMap(modLocator, modProps, monitor);

        // Filter rows if DiscardNonMappedRows is enabled
        if (analysis.isDiscardNonMappedRows())
        {

            BaseMatrix fmatrix = null;
            try
            {
                fmatrix = dataMatrix.getClass().newInstance();
                fmatrix.setCellAdapter(dataMatrix.getCellAdapter());
            } catch (Exception ex)
            {
                throw new PersistenceException("Error filtering data matrix.", ex);
            }

            List<Integer> rows = new ArrayList<Integer>();

            String[] names = moduleMap.getItemNames();
            Set<String> backgroundNames = new HashSet<String>();
            backgroundNames.addAll(Arrays.asList(names));

            for (int i = 0; i < dataMatrix.getRowCount(); i++)
                if (backgroundNames.contains(dataMatrix.getRowLabel(i)))
                {
                    rows.add(i);
                }

            int numRows = rows.size();
            int numColumns = dataMatrix.getColumnCount();

            fmatrix.make(numRows, numColumns);
            fmatrix.setColumns(dataMatrix.getColumns());

            for (int ri = 0; ri < numRows; ri++)
            {
                int srcRow = rows.get(ri);
                fmatrix.setRow(ri, dataMatrix.getRowLabel(srcRow));
                for (int ci = 0; ci < numColumns; ci++)
                {
                    Object value = dataMatrix.getCellValue(srcRow, ci, 0);
                    fmatrix.setCellValue(ri, ci, 0, value);
                }
            }

            dataMatrix = fmatrix;
        }

        analysis.setData(dataMatrix);
        analysis.setModuleMap(moduleMap);
    }
}
