/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.analysis.editor;

import edu.upf.bg.formatter.GenericFormatter;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.apache.velocity.VelocityContext;
import org.gitools.model.Analysis;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.IResource;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.persistence.formats.xml.AbstractXmlFormat;
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
import org.lobobrowser.html.FormInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Map;

public class AnalysisDetailsEditor<A extends IResource> extends AbstractEditor {

	private static final Logger log = LoggerFactory.getLogger(AnalysisDetailsEditor.class);
	
	protected A analysis;

    protected String template;

    protected ActionSet toolBar = null;

    protected TemplatePanel templatePanel;

    protected AbstractXmlFormat xmlPersistance = null;

    protected FileFormat fileformat;

	public AnalysisDetailsEditor(A analysis, String template, ActionSet toolBar) {
		this.analysis = analysis;
		this.template = template;
		this.toolBar = toolBar;
        this.setIcon(IconUtils.getIconResource(IconNames.LOGO_ANALYSIS_DETAILS16));

        createComponents();
	}

	private void createComponents() {
		templatePanel = new TemplatePanel() {
			@Override protected void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs) throws LinkVetoException {
				AnalysisDetailsEditor.this.submitForm(method, action, target, enctype, formInputs);
			}

			@Override protected void performUrlAction(String name, Map<String, String> params) {
				AnalysisDetailsEditor.this.performUrlAction(name, params);
			}
		};
		try {
			URL url = getClass().getResource(template);
			templatePanel.setTemplateFromResource(template, url);

			VelocityContext context = new VelocityContext();
			context.put("fmt", new GenericFormatter());
			context.put("analysis", analysis);

			prepareContext(context);

			templatePanel.render(context);
		}
		catch (Exception e) {
			LogUtils.logException(e, log);
		}

		setLayout(new BorderLayout());
		
		if (toolBar != null)
			add(ActionSetUtils.createToolBar(toolBar), BorderLayout.NORTH);

		add(templatePanel, BorderLayout.CENTER);
	}

	protected void prepareContext(VelocityContext context) {
	}
	
	@Override
	public Object getModel() {
		return analysis;
	}

	@Override
	public void doVisible() {
		AppFrame.instance().setLeftPanelVisible(true);
		AppFrame.instance().getPropertiesView().updateContext(null);
		AppFrame.instance().getDetailsView().updateContext(null);
		templatePanel.requestFocusInWindow();
	}

	protected void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs) {
	}

	protected void performUrlAction(String name, Map<String, String> params) {
	}

    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        if (xmlPersistance == null || fileformat == null)
            return;

        String title = ((Analysis) analysis).getTitle();

        String lastWorkPath = Settings.getDefault().getLastWorkPath();

        SaveFileWizard wizard = SaveFileWizard.createSimple(title,title,lastWorkPath, fileformat);
        WizardDialog dlg = new WizardDialog(AppFrame.instance(), wizard);
        dlg.setVisible(true);

        if (dlg.isCancelled())
            return;

        File workdir = new File(wizard.getFolder());
        File file = new File(workdir,wizard.getFileName());
        Settings.getDefault().setLastWorkPath(wizard.getFolder());


        try {
            xmlPersistance.write(new UrlResourceLocator(file), analysis, progressMonitor);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

}
