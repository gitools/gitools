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
package org.gitools.ui.analysis.overlapping.wizard;

import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.persistence._DEPRECATED.FileFormats;
import org.gitools.persistence.formats.analysis.OverlappingAnalysisFormat;
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
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * @noinspection ALL
 */
public class OverlappingAnalysisWizard extends AbstractWizard
{

    private static final String EXAMPLE_ANALYSIS_FILE = "analysis." + OverlappingAnalysisFormat.EXTENSION;
    private static final String EXAMPLE_DATA_FILE = "8_kidney_6_brain_downreg_annot.cdm.gz";

    private static final FileFormat[] dataFormats = new FileFormat[]{FileFormats.MULTIVALUE_DATA_MATRIX, FileFormats.GENE_MATRIX, FileFormats.GENE_MATRIX_TRANSPOSED, FileFormats.DOUBLE_MATRIX, FileFormats.DOUBLE_BINARY_MATRIX, FileFormats.MODULES_2C_MAP, FileFormats.MODULES_INDEXED_MAP};

    private ExamplePage examplePage;
    private DataFilePage dataPage;
    private DataFilterPage dataFilterPage;
    private OverlappingAnalysisWizardPage overlappingPage;
    private SaveFilePage saveFilePage;
    private AnalysisDetailsPage analysisDetailsPage;

    private boolean examplePageEnabled;
    private boolean dataFromMemory;
    private List<IElementAttribute> attributes;
    private boolean saveFilePageEnabled;


    public OverlappingAnalysisWizard()
    {
        super();

        examplePageEnabled = true;
        dataFromMemory = false;
        saveFilePageEnabled = true;

        setTitle("Overlapping analysis");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_OVERLAPPING, 96));
        setHelpContext("analysis_overlapping");
    }

    @Override
    public void addPages()
    {
        // Example
        if (examplePageEnabled && Settings.getDefault().isShowCombinationExamplePage())
        {
            examplePage = new ExamplePage("an overlapping analysis");
            examplePage.setTitle("Overlapping analysis");
            addPage(examplePage);
        }

        // Data
        if (!dataFromMemory)
        {
            dataPage = new DataFilePage(dataFormats);
            addPage(dataPage);
        }

        // Data filters
        dataFilterPage = new DataFilterPage();
        dataFilterPage.setRowsFilterFileVisible(false);
        dataFilterPage.setBinaryCutoffCmp(CutoffCmp.LT);
        dataFilterPage.setBinaryCutoffValue(0.05);
        dataFilterPage.setBinaryCutoffEnabled(true);
        addPage(dataFilterPage);

        // Overlapping parameters
        overlappingPage = new OverlappingAnalysisWizardPage();
        overlappingPage.setAttributes(attributes);
        addPage(overlappingPage);

        // Destination
        if (saveFilePageEnabled)
        {
            saveFilePage = new org.gitools.ui.wizard.common.SaveFilePage();
            saveFilePage.setTitle("Select destination file");
            saveFilePage.setFolder(Settings.getDefault().getLastWorkPath());
            saveFilePage.setFormats(new FileFormat[]{OverlappingAnalysisFormat.FILE_FORMAT});
            saveFilePage.setFormatsVisible(false);
            addPage(saveFilePage);
        }

        // Analysis details
        analysisDetailsPage = new AnalysisDetailsPage();
        addPage(analysisDetailsPage);
    }


    @Override
    public void pageLeft(IWizardPage currentPage)
    {
        if (currentPage == examplePage)
        {
            Settings.getDefault().setShowCombinationExamplePage(examplePage.isShowAgain());

            if (examplePage.isExampleEnabled())
            {
                JobThread.execute(AppFrame.get(), new JobRunnable()
                {
                    @Override
                    public void run(@NotNull IProgressMonitor monitor)
                    {

                        final File basePath = ExamplesManager.getDefault().resolvePath("overlap", monitor);

                        if (basePath == null)
                        {
                            throw new RuntimeException("Unexpected error: There are no examples available");
                        }

                        File analysisFile = new File(basePath, EXAMPLE_ANALYSIS_FILE);
                        Properties props = new Properties();
                        try
                        {
                            monitor.begin("Loading example parameters ...", 1);

                            final OverlappingAnalysis a = PersistenceManager.get().load(analysisFile, OverlappingAnalysis.class, props, monitor);

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
        IWizardPage page = getCurrentPage();

        boolean canFinish = super.canFinish();
        canFinish |= page == saveFilePage && page.isComplete();

        return canFinish;
    }

    public boolean isExamplePageEnabled()
    {
        return examplePageEnabled;
    }

    public void setExamplePageEnabled(boolean examplePageEnabled)
    {
        this.examplePageEnabled = examplePageEnabled;
    }

    public boolean isDataFromMemory()
    {
        return dataFromMemory;
    }

    public void setDataFromMemory(boolean dataFromMemory)
    {
        this.dataFromMemory = dataFromMemory;
    }

    public List<IElementAttribute> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(List<IElementAttribute> attributes)
    {
        this.attributes = attributes;
    }

    public boolean isSaveFilePageEnabled()
    {
        return saveFilePageEnabled;
    }

    public void setSaveFilePageEnabled(boolean saveFilePageEnabled)
    {
        this.saveFilePageEnabled = saveFilePageEnabled;
    }

    @NotNull
    public OverlappingAnalysis getAnalysis()
    {
        OverlappingAnalysis a = new OverlappingAnalysis();

        a.setTitle(analysisDetailsPage.getAnalysisTitle());
        a.setDescription(analysisDetailsPage.getAnalysisNotes());
        a.setProperties(analysisDetailsPage.getAnalysisAttributes());

        a.setBinaryCutoffEnabled(dataFilterPage.isBinaryCutoffEnabled());
        a.setBinaryCutoffCmp(dataFilterPage.getBinaryCutoffCmp());
        a.setBinaryCutoffValue(dataFilterPage.getBinaryCutoffValue());

        //Verify: a.setAttributeIndex(overlappingPage.getAttributeIndex());
        a.setReplaceNanValue(overlappingPage.isReplaceNanValuesEnabled() ? overlappingPage.getReplaceNanValue() : null);
        a.setTransposeData(overlappingPage.isTransposeEnabled());

        return a;
    }

    private void setAnalysis(@NotNull OverlappingAnalysis a)
    {
        analysisDetailsPage.setAnalysisTitle(a.getTitle());
        analysisDetailsPage.setAnalysisNotes(a.getDescription());
        analysisDetailsPage.setAnalysisAttributes(a.getProperties());

        dataFilterPage.setBinaryCutoffEnabled(a.isBinaryCutoffEnabled());
        dataFilterPage.setBinaryCutoffCmp(a.getBinaryCutoffCmp());
        dataFilterPage.setBinaryCutoffValue(a.getBinaryCutoffValue());

        overlappingPage.setReplaceNanValuesEnabled(a.getReplaceNanValue() != null);
        if (a.getReplaceNanValue() != null)
        {
            overlappingPage.setReplaceNanValue(a.getReplaceNanValue());
        }

        overlappingPage.setTransposeEnabled(a.isTransposeData());
    }

    public DataFilePage getDataFilePage()
    {
        return dataPage;
    }

    public String getWorkdir()
    {
        return saveFilePage.getFolder();
    }

    public String getFileName()
    {
        return saveFilePage.getFileName();
    }

}
