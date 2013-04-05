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
package org.gitools.ui.analysis.editor;

import org.apache.velocity.VelocityContext;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.persistence._DEPRECATED.FileFormats;
import org.gitools.persistence._DEPRECATED.PersistenceUtils;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.ActionSetUtils;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.panel.TemplatePanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.LogUtils;
import org.gitools.ui.wizard.common.SaveFileWizard;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;

/**
 * @noinspection ALL
 */
public abstract class AbstractTablesPanel<A> extends JPanel
{

    private static final Logger log = LoggerFactory.getLogger(AbstractTablesPanel.class);

    private static final boolean DEFAULT_AUTOMATIC_UPDATE = false;

    protected static final int DATA_VIEW_MODE = 1;
    protected static final int RESULTS_VIEW_MODE = 2;

    private static abstract class DataAction extends BaseAction
    {
        public DataAction()
        {
            super("Data", true, true, "template");
        }
    }

    private static abstract class ResultsAction extends BaseAction
    {
        public ResultsAction()
        {
            super("Results", true, false, "template");
        }
    }

    private static abstract class ExportHtmlAction extends BaseAction
    {
        public ExportHtmlAction()
        {
            super("Export html...");

            setDesc("Export html");
            setLargeIconFromResource(IconNames.save24);
            setSmallIconFromResource(IconNames.save16);
        }
    }

    private static abstract class AutomaticUpdateAction extends BaseAction
    {
        public AutomaticUpdateAction()
        {
            super("Automatic update", true, DEFAULT_AUTOMATIC_UPDATE, null);

            setDesc("The tables are automatically updated when heatmap cursor changes");
            //setLargeIconFromResource(IconNames.save24);
            //setSmallIconFromResource(IconNames.save16);
        }
    }

    private static abstract class ForceUpdateAction extends BaseAction
    {
        public ForceUpdateAction()
        {
            super("Update");

            setDesc("Update tables");
            //setLargeIconFromResource(IconNames.save24);
            //setSmallIconFromResource(IconNames.save16);
        }
    }

    protected final A analysis;

    protected final Heatmap heatmap;

    protected int viewMode;

    private boolean automaticUpdate;
    private boolean forceUpdate;

    private TemplatePanel templatePanel;

    protected AbstractTablesPanel(A analysis, @NotNull Heatmap heatmap)
    {
        this.analysis = analysis;
        this.heatmap = heatmap;

        this.viewMode = DATA_VIEW_MODE;

        this.automaticUpdate = DEFAULT_AUTOMATIC_UPDATE;
        this.forceUpdate = false;

        createComponents();

        heatmap.getMatrixView().addPropertyChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                AbstractTablesPanel.this.propertyChange(evt);
            }
        });
    }

    private void createComponents()
    {
        JToolBar toolBar = createToolBar();

        templatePanel = new TemplatePanel();

        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(templatePanel, BorderLayout.CENTER);
    }

    private JToolBar createToolBar()
    {
        return ActionSetUtils.createToolBar(new ActionSet(new BaseAction[]{new DataAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setViewMode(DATA_VIEW_MODE);
            }
        }, new ResultsAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setViewMode(RESULTS_VIEW_MODE);
            }
        }, new ExportHtmlAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                saveHtml();
            }
        }, new AutomaticUpdateAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setAutomaticUpdate(!isAutomaticUpdate());
                updateContents();
            }
        }, new ForceUpdateAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                forceUpdate();
            }
        }}));
    }

    void propertyChange(@NotNull PropertyChangeEvent evt)
    {
        String name = evt.getPropertyName();
        if (evt.getSource() == heatmap.getMatrixView())
        {
            if (MatrixView.SELECTED_LEAD_CHANGED.equals(name))
            {
                updateContents();
            }
        }
    }

    private void updateContents()
    {
        if (!automaticUpdate && !forceUpdate)
        {
            return;
        }

        forceUpdate = false;

        VelocityContext context = createModel();
        context.put("exporting", false);
        String template = context.get("__template__").toString();

        try
        {
            URL url = getClass().getResource(template);
            templatePanel.setTemplateFromResource(template, url);
            templatePanel.render(context);
        } catch (Exception ex)
        {
            LogUtils.logException(ex, log);
        }
    }

    protected abstract VelocityContext createModel();

    public int getViewMode()
    {
        return viewMode;
    }

    void setViewMode(int viewMode)
    {
        this.viewMode = viewMode;
        updateContents();
    }

    boolean isAutomaticUpdate()
    {
        return automaticUpdate;
    }

    void setAutomaticUpdate(boolean automaticUpdate)
    {
        this.automaticUpdate = automaticUpdate;
    }

    void forceUpdate()
    {
        this.forceUpdate = true;
        updateContents();
    }

    void saveHtml()
    {
        AbstractEditor editor = AppFrame.get().getEditorsPanel().getSelectedEditor();

        if (editor == null)
        {
            return;
        }

        SaveFileWizard saveWiz = SaveFileWizard.createSimple("Export html ...", PersistenceUtils.getFileName(editor.getName()), Settings.getDefault().getLastExportPath(), new FileFormat[]{FileFormats.HTML});

        WizardDialog dlg = new WizardDialog(AppFrame.get(), saveWiz);
        dlg.setVisible(true);
        if (dlg.isCancelled())
        {
            return;
        }

        Settings.getDefault().setLastExportPath(saveWiz.getFolder());

        final File file = saveWiz.getPathAsFile();

        final String formatExtension = saveWiz.getFormat().getExtension();

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                try
                {
                    monitor.begin("Exporting html ...", 1);
                    monitor.info("File: " + file.getName());

                    VelocityContext context = createModel();
                    context.put("exporting", true);
                    String template = context.get("__template__").toString();

                    try
                    {
                        URL url = getClass().getResource(template);
                        templatePanel.setTemplateFromResource(template, url);
                        templatePanel.render(context);
                    } catch (Exception ex)
                    {
                        LogUtils.logException(ex, log);
                    }

                    Writer writer = new FileWriter(file);
                    templatePanel.merge(context, writer);

                    writer.close();

                    monitor.end();
                } catch (Exception ex)
                {
                    monitor.exception(ex);
                }
            }
        });

        AppFrame.get().setStatusText("Ok.");
    }
}
