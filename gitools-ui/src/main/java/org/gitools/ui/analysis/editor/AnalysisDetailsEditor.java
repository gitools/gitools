/*
 *  Copyright 2010 chris.
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
import java.awt.BorderLayout;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;
import org.apache.velocity.VelocityContext;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.ActionSetUtils;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.panel.TemplatePanel;
import org.lobobrowser.html.FormInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalysisDetailsEditor<A> extends AbstractEditor {

	private static final Logger log = LoggerFactory.getLogger(AnalysisDetailsEditor.class);
	
	protected A analysis;
	
	protected String template;

	protected ActionSet toolBar = null;

	protected TemplatePanel templatePanel;

	public AnalysisDetailsEditor(A analysis, String template, ActionSet toolBar) {
		this.analysis = analysis;
		this.template = template;
		this.toolBar = toolBar;

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
			templatePanel.setTemplate(template);

			VelocityContext context = new VelocityContext();
			context.put("fmt", new GenericFormatter());
			context.put("analysis", analysis);

			prepareContext(context);

			templatePanel.setContext(context);
			templatePanel.render();
		}
		catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.close();
			log.error(sw.toString());
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
}
