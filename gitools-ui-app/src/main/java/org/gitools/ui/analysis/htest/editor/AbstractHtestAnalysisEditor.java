package org.gitools.ui.analysis.htest.editor;


import org.apache.commons.lang.WordUtils;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.analysis.stats.test.factory.TestFactory;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.model.ToolConfig;
import org.gitools.core.model.decorator.impl.BinaryDecorator;
import org.gitools.persistence.formats.analysis.EnrichmentAnalysisFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHtestAnalysisEditor<T extends HtestAnalysis> extends AnalysisDetailsEditor<T> {

    private String formatExtension;

    protected AbstractHtestAnalysisEditor(T analysis, String template, String formatExtension) {
        super(analysis, template, null);
        this.formatExtension = formatExtension;
    }


    @Override
    protected void prepareContext(VelocityContext context) {

        IResourceLocator fileRef = analysis.getData().getLocator();

        context.put("dataFile", fileRef != null ? fileRef.getName() : "Not defined");

        ToolConfig testConfig = analysis.getTestConfig();
        if (!testConfig.get(TestFactory.TEST_NAME_PROPERTY).equals("")) {
            context.put("test", WordUtils.capitalize(testConfig.get(TestFactory.TEST_NAME_PROPERTY)));
            HashMap<String, Object> testAttributes = new HashMap<>();
            for (String key : testConfig.getConfiguration().keySet()) {
                if (!key.equals(TestFactory.TEST_NAME_PROPERTY)) {
                    testAttributes.put(WordUtils.capitalize(key), WordUtils.capitalize(testConfig.get(key)));
                }
            }
            if (testAttributes.size() > 0) {
                context.put("testAttributes", testAttributes);
            }

        }

        CutoffCmp cmp = analysis.getBinaryCutoffCmp();
        String filterDesc = cmp == null ? "Not filtered" : "Binary cutoff filter for values " + cmp.getLongName() + " " + analysis.getBinaryCutoffValue();
        context.put("filterDesc", filterDesc);

        fileRef = analysis.getModuleMap().getLocator();

        context.put("modulesFile", fileRef != null ? fileRef.getName() : "Unknown");

        context.put("moduleMinSize", analysis.getMinModuleSize());
        int maxSize = analysis.getMaxModuleSize();
        context.put("moduleMaxSize", maxSize != Integer.MAX_VALUE ? maxSize : "No limit");

        if (analysis.getMtc().equals("bh")) {
            context.put("mtc", "Benjamini Hochberg FDR");
        } else if (analysis.getMtc().equals("bonferroni")) {
            context.put("mtc", "Bonferroni");
        }

        if (analysis.getResults() != null) {
            fileRef = analysis.getResults().getLocator();
            context.put("resultsFile", fileRef != null ? fileRef.getName() : "Not defined");
        }

        fileRef = analysis.getLocator();
        if (fileRef != null) {
            context.put("analysisLocation", fileRef.getURL());

            if (fileRef.isWritable()) {
                setSaveAllowed(true);
            }
        }

        super.prepareContext(context);
    }

    @Override
    protected void performUrlAction(String name, Map<String, String> params) {
        if ("NewDataHeatmap".equals(name)) {
            newDataHeatmap();
        } else if ("NewResultsHeatmap".equals(name)) {
            newResultsHeatmap();
        }
    }

    protected void newDataHeatmap() {
        if (analysis.getData() == null) {
            AppFrame.get().setStatusText("Analysis doesn't contain data.");
            return;
        }

        final EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Creating new heatmap from data ...", 1);

                Heatmap heatmap = new Heatmap(analysis.getData().get());
                String testName = analysis.getTestConfig().getConfiguration().get(TestFactory.TEST_NAME_PROPERTY);
                if (!testName.equals(TestFactory.ZSCORE_TEST)) {
                    heatmap.getLayers().get(0).setDecorator(new BinaryDecorator());
                }
                heatmap.setTitle(analysis.getTitle() + " (data)");

                final HeatmapEditor editor = new HeatmapEditor(heatmap);

                editor.setName(editorPanel.deriveName(getName(), formatExtension, "-data", ""));

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        editorPanel.addEditor(editor);
                        AppFrame.get().setStatusText("New heatmap created.");
                    }
                });
            }
        });
    }

    protected void newResultsHeatmap() {
        if (analysis.getResults() == null) {
            AppFrame.get().setStatusText("Analysis doesn't contain results.");
            return;
        }

        final EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Creating new heatmap from results ...", 1);

                final HeatmapEditor editor = new HeatmapEditor(createHeatmap(analysis));
                editor.setIcon(IconUtils.getIconResource(IconNames.analysisHeatmap16));

                editor.setName(editorPanel.deriveName(getName(), EnrichmentAnalysisFormat.EXTENSION, "-results", ""));

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        editorPanel.addEditor(editor);
                        AppFrame.get().setStatusText("Heatmap results created.");
                    }
                });
            }
        });
    }

    protected Heatmap createHeatmap(T analysis) {
        Heatmap heatmap = new Heatmap(analysis.getResults().get());
        heatmap.setTitle(analysis.getTitle() + " (results)");
        return heatmap;
    }

    /*TODO
    protected static List<BaseAction> createToolBar(EnrichmentAnalysis analysis) {
        ViewRelatedDataFromAction action = new ViewRelatedDataFromAction(analysis.getTitle(), analysis.getData().get(), analysis.getModuleMap().get(), MatrixDimensionKey.ROWS);
        List<BaseAction> tb = new ArrayList<>();
        tb.add(action);
        return tb;
    }*/
}
