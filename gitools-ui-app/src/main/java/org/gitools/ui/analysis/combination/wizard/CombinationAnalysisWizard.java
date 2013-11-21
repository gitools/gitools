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
package org.gitools.ui.analysis.combination.wizard;


import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.formats.FileFormat;
import org.gitools.persistence.formats.FileFormats;
import org.gitools.persistence.formats.analysis.CombinationAnalysisFormat;
import org.gitools.persistence.formats.matrix.CmatrixMatrixFormat;
import org.gitools.persistence.formats.matrix.TdmMatrixFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.analysis.wizard.DataFilePage;
import org.gitools.ui.analysis.wizard.ExamplePage;
import org.gitools.ui.analysis.wizard.SelectFilePage;
import org.gitools.ui.examples.ExamplesManager;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFilePage;

import javax.swing.*;
import java.io.File;

public class CombinationAnalysisWizard extends AbstractWizard {

    private static final String EXAMPLE_ANALYSIS_FILE = "analysis." + CombinationAnalysisFormat.EXTENSION;
    private static final String EXAMPLE_DATA_FILE = "19_lung_10_breast_upreg_annot.cdm.gz";
    private static final String EXAMPLE_COLUM_SETS_FILE = "lung_breast_experiments_annotated.tcm";

    private static final FileFormat[] dataFormats = new FileFormat[]{FileFormats.MULTIVALUE_DATA_MATRIX, FileFormats.GENE_MATRIX, FileFormats.GENE_MATRIX_TRANSPOSED, FileFormats.DOUBLE_MATRIX, FileFormats.DOUBLE_BINARY_MATRIX, FileFormats.COMPRESSED_MATRIX, FileFormats.MODULES_2C_MAP, FileFormats.MODULES_INDEXED_MAP};

    private static final FileFormat[] columnSetsFormats = new FileFormat[]{FileFormats.GENE_MATRIX, FileFormats.GENE_MATRIX_TRANSPOSED, FileFormats.DOUBLE_MATRIX, FileFormats.DOUBLE_BINARY_MATRIX, FileFormats.MODULES_2C_MAP, FileFormats.MODULES_INDEXED_MAP};

    private ExamplePage examplePage;
    private DataFilePage dataPage;
    private CombinationAnalysisParamsPage combinationParamsPage;
    private SelectFilePage columnSetsPage;
    private SaveFilePage saveFilePage;
    private AnalysisDetailsPage analysisDetailsPage;

    private boolean examplePageEnabled;
    private boolean dataFromMemory;

    private String[] attributes;
    private boolean saveFilePageEnabled;

    private File dataFile;

    public CombinationAnalysisWizard() {
        super();

        examplePageEnabled = true;
        dataFromMemory = false;
        saveFilePageEnabled = true;

        setTitle("Combination analysis");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_COMBINATION, 96));
        setHelpContext("analysis_combination");
    }

    @Override
    public void addPages() {
        // Example
        if (examplePageEnabled && Settings.getDefault().isShowCombinationExamplePage()) {
            examplePage = new ExamplePage("a combination analysis");
            examplePage.setTitle("Combination analysis");
            addPage(examplePage);
        }

        // Data
        if (!dataFromMemory) {
            dataPage = new DataFilePage(dataFormats);
            addPage(dataPage);
        }

        // Combination parameters
        combinationParamsPage = new CombinationAnalysisParamsPage();
        combinationParamsPage.setAttributes(attributes);
        addPage(combinationParamsPage);

        // Set of columns
        columnSetsPage = new SelectFilePage(columnSetsFormats) {
            @Override
            protected String getLastPath() {
                return Settings.getDefault().getLastMapPath();
            }

            @Override
            protected void setLastPath(String path) {
                Settings.getDefault().setLastMapPath(path);
            }
        };
        columnSetsPage.setTitle("Select sets of columns/rows to combine");
        columnSetsPage.setMessage(MessageStatus.INFO, "Leave blank to combine all the columns");
        columnSetsPage.setBlankFileAllowed(true);
        addPage(columnSetsPage);

        // Destination
        if (saveFilePageEnabled) {
            saveFilePage = new org.gitools.ui.wizard.common.SaveFilePage();
            saveFilePage.setTitle("Select destination file");
            saveFilePage.setFolder(Settings.getDefault().getLastWorkPath());
            saveFilePage.setFormats(new FileFormat[]{CombinationAnalysisFormat.FILE_FORMAT});
            saveFilePage.setFormatsVisible(false);
            addPage(saveFilePage);
        }

        // Analysis details
        analysisDetailsPage = new AnalysisDetailsPage();
        addPage(analysisDetailsPage);
    }

    @Override
    public void pageEntered(IWizardPage page) {
        if (combinationParamsPage.equals(page)) {
            if (!dataFromMemory && (dataFile == null || !dataPage.getFile().equals(dataFile))) {
                JobThread.execute(AppFrame.get(), new JobRunnable() {
                    @Override
                    public void run(IProgressMonitor monitor) {
                        monitor.begin("Reading data header ...", 1);

                        try {
                            dataFile = dataPage.getFile();

                            IResourceFormat dataFormat = PersistenceManager.get().getFormat(dataFile.getName(), IMatrix.class);
                            if (dataFormat instanceof TdmMatrixFormat) {
                                attributes = TdmMatrixFormat.readHeader(dataFile);
                            } else if (dataFormat instanceof CmatrixMatrixFormat) {
                                attributes = CmatrixMatrixFormat.readHeader(dataFile);
                            } else {
                                attributes = null;
                            }

                            combinationParamsPage.setAttributes(attributes);
                        } catch (Exception ex) {
                            monitor.exception(ex);
                        }
                        monitor.end();
                    }
                });
            }
        }
    }

    @Override
    public void pageLeft(IWizardPage currentPage) {
        if (currentPage == examplePage) {
            Settings.getDefault().setShowCombinationExamplePage(examplePage.isShowAgain());

            if (examplePage.isExampleEnabled()) {
                JobThread.execute(AppFrame.get(), new JobRunnable() {
                    @Override
                    public void run(IProgressMonitor monitor) {

                        final File basePath = ExamplesManager.getDefault().resolvePath("combination", monitor);

                        if (basePath == null) {
                            throw new RuntimeException("Unexpected error: There are no examples available");
                        }

                        File analysisFile = new File(basePath, EXAMPLE_ANALYSIS_FILE);

                        try {
                            final CombinationAnalysis a = PersistenceManager.get().load(analysisFile, CombinationAnalysis.class, monitor);

                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    setAnalysis(a);

                                    dataPage.setFile(new File(basePath, EXAMPLE_DATA_FILE));
                                    columnSetsPage.setFile(new File(basePath, EXAMPLE_COLUM_SETS_FILE));
                                    saveFilePage.setFileNameWithoutExtension("example");
                                }
                            });
                        } catch (Exception ex) {
                            monitor.exception(ex);
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean canFinish() {
        IWizardPage page = getCurrentPage();

        boolean canFinish = super.canFinish();
        canFinish |= page == saveFilePage && page.isComplete();

        return canFinish;
    }

    public boolean isExamplePageEnabled() {
        return examplePageEnabled;
    }

    public void setExamplePageEnabled(boolean examplePageEnabled) {
        this.examplePageEnabled = examplePageEnabled;
    }

    public boolean isDataFromMemory() {
        return dataFromMemory;
    }

    public void setDataFromMemory(boolean dataFromMemory) {
        this.dataFromMemory = dataFromMemory;
    }


    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    public boolean isSaveFilePageEnabled() {
        return saveFilePageEnabled;
    }

    public void setSaveFilePageEnabled(boolean saveFilePageEnabled) {
        this.saveFilePageEnabled = saveFilePageEnabled;
    }


    public CombinationAnalysis getAnalysis() {
        CombinationAnalysis a = new CombinationAnalysis();

        a.setTitle(analysisDetailsPage.getAnalysisTitle());
        a.setDescription(analysisDetailsPage.getAnalysisNotes());
        a.setProperties(analysisDetailsPage.getAnalysisAttributes());

        IMatrixLayer attr = combinationParamsPage.getSizeAttribute();
        String sizeAttrName = attr != null ? attr.getId() : null;
        a.setSizeLayer(sizeAttrName);

        attr = combinationParamsPage.getPvalueAttribute();
        String pvalueAttrName = attr != null ? attr.getId() : null;
        a.setValueLayer(pvalueAttrName);

        a.setTransposeData(combinationParamsPage.isTransposeEnabled());

        return a;
    }

    private void setAnalysis(CombinationAnalysis a) {
        analysisDetailsPage.setAnalysisTitle(a.getTitle());
        analysisDetailsPage.setAnalysisNotes(a.getDescription());
        analysisDetailsPage.setAnalysisAttributes(a.getProperties());
        combinationParamsPage.setPreferredSizeAttr(a.getSizeLayer());
        combinationParamsPage.setPreferredPvalueAttr(a.getValueLayer());
        combinationParamsPage.setTransposeEnabled(a.isTransposeData());
    }

    public DataFilePage getDataFilePage() {
        return dataPage;
    }

    public CombinationAnalysisParamsPage getCombinationParamsPage() {
        return combinationParamsPage;
    }

    public SelectFilePage getColumnSetsPage() {
        return columnSetsPage;
    }

    public SaveFilePage getSaveFilePage() {
        return saveFilePage;
    }
}
