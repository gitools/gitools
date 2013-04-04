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
import org.gitools.model.Analysis;
import org.gitools.persistence.IResource;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.persistence.formats.analysis.AbstractXmlFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.ActionSetUtils;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.panel.TemplatePanel;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.LogUtils;
import org.gitools.ui.wizard.common.SaveFileWizard;
import org.gitools.utils.formatter.GenericFormatter;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.Nullable;
import org.lobobrowser.html.FormInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Map;

public class AnalysisDetailsEditor<A extends IResource> extends AbstractEditor
{

    private static final Logger log = LoggerFactory.getLogger(AnalysisDetailsEditor.class);

    protected A analysis;

    protected String template;

    @Nullable
    protected ActionSet toolBar = null;

    protected TemplatePanel templatePanel;

    @Nullable
    protected AbstractXmlFormat xmlPersistance = null;

    protected FileFormat fileformat;

    public AnalysisDetailsEditor(A analysis, String template, ActionSet toolBar)
    {
        this.analysis = analysis;
        this.template = template;
        this.toolBar = toolBar;
        this.setIcon(IconUtils.getIconResource(IconNames.LOGO_ANALYSIS_DETAILS16));

        createComponents();
    }

    private void createComponents()
    {
        templatePanel = new TemplatePanel()
        {
            @Override
            protected void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs) throws LinkVetoException
            {
                AnalysisDetailsEditor.this.submitForm(method, action, target, enctype, formInputs);
            }

            @Override
            protected void performUrlAction(String name, Map<String, String> params)
            {
                AnalysisDetailsEditor.this.performUrlAction(name, params);
            }
        };
        try
        {
            URL url = getClass().getResource(template);
            templatePanel.setTemplateFromResource(template, url);

            VelocityContext context = new VelocityContext();
            context.put("fmt", new GenericFormatter());
            context.put("analysis", analysis);

            prepareContext(context);

            templatePanel.render(context);
        } catch (Exception e)
        {
            LogUtils.logException(e, log);
        }

        setLayout(new BorderLayout());

        if (toolBar != null)
        {
            add(ActionSetUtils.createToolBar(toolBar), BorderLayout.NORTH);
        }

        add(templatePanel, BorderLayout.CENTER);
    }

    protected void prepareContext(VelocityContext context)
    {
    }

    @Override
    public Object getModel()
    {
        return analysis;
    }

    @Override
    public void doVisible()
    {
        templatePanel.requestFocusInWindow();
    }

    protected void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs)
    {
    }

    protected void performUrlAction(String name, Map<String, String> params)
    {
    }

    @Override
    public void doSave(IProgressMonitor progressMonitor)
    {
        if (xmlPersistance == null || fileformat == null)
        {
            return;
        }

        String title = ((Analysis) analysis).getTitle();

        String lastWorkPath = Settings.getDefault().getLastWorkPath();

        SaveFileWizard wizard = SaveFileWizard.createSimple(title, title, lastWorkPath, fileformat);
        WizardDialog dlg = new WizardDialog(AppFrame.get(), wizard);
        dlg.setVisible(true);

        if (dlg.isCancelled())
        {
            return;
        }

        File workdir = new File(wizard.getFolder());
        File file = new File(workdir, wizard.getFileName());
        Settings.getDefault().setLastWorkPath(wizard.getFolder());


        try
        {
            xmlPersistance.write(new UrlResourceLocator(file), analysis, progressMonitor);
        } catch (PersistenceException e)
        {
            e.printStackTrace();
        }
    }

}
