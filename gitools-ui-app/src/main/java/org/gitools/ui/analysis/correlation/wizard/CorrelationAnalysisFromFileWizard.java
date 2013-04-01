/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.analysis.correlation.wizard;

import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.persistence._DEPRECATED.FileFormats;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence.formats.analysis.AbstractXmlFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.analysis.wizard.DataFilePage;
import org.gitools.ui.analysis.wizard.DataFilterPage;
import org.gitools.ui.analysis.wizard.ExamplePage;
import org.gitools.ui.examples.ExamplesManager;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFilePage;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import javax.swing.*;
import java.io.File;
import java.util.Properties;

public class CorrelationAnalysisFromFileWizard extends AbstractWizard
{

    private static final String EXAMPLE_ANALYSIS_FILE = "analysis." + FileSuffixes.CORRELATIONS;
    private static final String EXAMPLE_DATA_FILE = "8_kidney_6_brain_downreg_annot.cdm.gz";

    private ExamplePage examplePage;
    private DataFilePage dataPage;
    private DataFilterPage dataFilterPage;
    protected CorrelationFromFilePage corrPage;
    private SaveFilePage saveFilePage;
    protected AnalysisDetailsPage analysisDetailsPage;

    public CorrelationAnalysisFromFileWizard()
    {
        super();

        setTitle("Correlation analysis");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_CORRELATION, 96));
        setHelpContext("analysis_correlation");
    }

    @Override
    public void addPages()
    {
        // Example
        if (Settings.getDefault().isShowCombinationExamplePage())
        {
            examplePage = new ExamplePage("a correlation analysis");
            examplePage.setTitle("Correlation analysis");
            addPage(examplePage);
        }

        // Data
        dataPage = new DataFilePage();
        addPage(dataPage);

        // Data filters
        dataFilterPage = new DataFilterPage();
        dataFilterPage.setRowsFilterFileVisible(false);
        addPage(dataFilterPage);

        // Correlation method
        corrPage = new CorrelationFromFilePage();
        addPage(corrPage);

        // Destination
        saveFilePage = new SaveFilePage();
        saveFilePage.setTitle("Select destination file");
        saveFilePage.setFolder(Settings.getDefault().getLastWorkPath());
        saveFilePage.setFormats(new FileFormat[]{
                FileFormats.CORRELATIONS});
        saveFilePage.setFormatsVisible(false);
        addPage(saveFilePage);

        // Analysis details
        analysisDetailsPage = new AnalysisDetailsPage();
        addPage(analysisDetailsPage);
    }

    @Override
    public void pageLeft(IWizardPage currentPage)
    {
        if (currentPage == examplePage)
        {
            Settings.getDefault().setShowCombinationExamplePage(
                    examplePage.isShowAgain());

            if (examplePage.isExampleEnabled())
            {
                JobThread.execute(AppFrame.get(), new JobRunnable()
                {
                    @Override
                    public void run(IProgressMonitor monitor)
                    {

                        final File basePath = ExamplesManager.getDefault().resolvePath("correlations", monitor);

                        if (basePath == null)
                        {
                            throw new RuntimeException("Unexpected error: There are no examples available");
                        }

                        File analysisFile = new File(basePath, EXAMPLE_ANALYSIS_FILE);
                        Properties props = new Properties();
                        props.setProperty(AbstractXmlFormat.LOAD_REFERENCES_PROP, "false");
                        try
                        {
                            monitor.begin("Loading example parameters ...", 1);

                            final CorrelationAnalysis a = PersistenceManager.get()
                                    .load(analysisFile, CorrelationAnalysis.class, props, monitor);

                            SwingUtilities.invokeLater(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    setAnalysis(a);

                                    dataPage.setFile(new File(basePath, EXAMPLE_DATA_FILE));
                                    saveFilePage.setFileNameWithoutExtension("example");
                                }
                            });

                            monitor.end();
                        } catch (Exception ex)
                        {
                            monitor.exception(ex);
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean canFinish()
    {
        boolean canFinish = super.canFinish();

        IWizardPage page = getCurrentPage();

        canFinish |= page.isComplete() && (page == saveFilePage);

        return canFinish;
    }

    @Override
    public void performFinish()
    {
        Settings.getDefault().setLastWorkPath(saveFilePage.getFolder());
    }

    public String getWorkdir()
    {
        return saveFilePage.getFolder();
    }

    public String getFileName()
    {
        return saveFilePage.getFileName();
    }

    public String getDataFileMime()
    {
        return dataPage.getFileFormat().getMime();
    }

    public File getDataFile()
    {
        return dataPage.getFile();
    }

    public File getPopulationFile()
    {
        return dataFilterPage.getRowsFilterFile();
    }

    public CorrelationAnalysis getAnalysis()
    {
        CorrelationAnalysis a = new CorrelationAnalysis();

        a.setTitle(analysisDetailsPage.getAnalysisTitle());
        a.setDescription(analysisDetailsPage.getAnalysisNotes());
        a.setAttributes(analysisDetailsPage.getAnalysisAttributes());

        //a.setAttributeIndex(corrPage.getAttributeIndex());
        a.setReplaceNanValue(corrPage.isReplaceNanValuesEnabled() ?
                corrPage.getReplaceNanValue() : null);
        a.setTransposeData(corrPage.isTransposeEnabled());

        return a;
    }

    private void setAnalysis(CorrelationAnalysis a)
    {
        analysisDetailsPage.setAnalysisTitle(a.getTitle());
        analysisDetailsPage.setAnalysisNotes(a.getDescription());
        analysisDetailsPage.setAnalysisAttributes(a.getAttributes());
        corrPage.setReplaceNanValuesEnabled(a.getReplaceNanValue() != null);
        if (a.getReplaceNanValue() != null)
        {
            corrPage.setReplaceNanValue(a.getReplaceNanValue());
        }
        corrPage.setTransposeEnabled(a.isTransposeData());
    }
}
