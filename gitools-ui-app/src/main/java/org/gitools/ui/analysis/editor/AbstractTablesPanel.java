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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.apache.velocity.VelocityContext;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.persistence.PersistenceUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTablesPanel<A> extends JPanel {

	private static Logger log = LoggerFactory.getLogger(AbstractTablesPanel.class);

	protected static final boolean DEFAULT_AUTOMATIC_UPDATE = false;

	protected static final int DATA_VIEW_MODE = 1;
	protected static final int RESULTS_VIEW_MODE = 2;

	private static abstract class DataAction extends BaseAction {
		public DataAction() {
			super("Data", true, true, "template");
		}
	}

	private static abstract class ResultsAction extends BaseAction {
		public ResultsAction() {
			super("Results", true, false, "template");
		}
	}

	private static abstract class ExportHtmlAction extends BaseAction {
		public ExportHtmlAction() {
			super("Export html...");

			setDesc("Export html");
			setLargeIconFromResource(IconNames.save24);
			setSmallIconFromResource(IconNames.save16);
		}
	}

	private static abstract class AutomaticUpdateAction extends BaseAction {
		public AutomaticUpdateAction() {
			super("Automatic update", true, DEFAULT_AUTOMATIC_UPDATE, null);

			setDesc("The tables are automatically updated when heatmap cursor changes");
			//setLargeIconFromResource(IconNames.save24);
			//setSmallIconFromResource(IconNames.save16);
		}
	}

	private static abstract class ForceUpdateAction extends BaseAction {
		public ForceUpdateAction() {
			super("Update");

			setDesc("Update tables");
			//setLargeIconFromResource(IconNames.save24);
			//setSmallIconFromResource(IconNames.save16);
		}
	}

	protected A analysis;

	protected Heatmap heatmap;

	protected int viewMode;

	protected boolean automaticUpdate;
	protected boolean forceUpdate;

	private TemplatePanel templatePanel;

	public AbstractTablesPanel(A analysis, Heatmap heatmap) {
		this.analysis = analysis;
		this.heatmap = heatmap;

		this.viewMode = DATA_VIEW_MODE;

		this.automaticUpdate = DEFAULT_AUTOMATIC_UPDATE;
		this.forceUpdate = false;

		createComponents();

		heatmap.getMatrixView().addPropertyChangeListener(new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				AbstractTablesPanel.this.propertyChange(evt); } });
	}

	private void createComponents() {
		JToolBar toolBar = createToolBar();

		templatePanel = new TemplatePanel();

		setLayout(new BorderLayout());
		add(toolBar, BorderLayout.NORTH);
		add(templatePanel, BorderLayout.CENTER);
	}

	private JToolBar createToolBar() {
		return ActionSetUtils.createToolBar(new ActionSet(new BaseAction[] {
			new DataAction() {
				@Override public void actionPerformed(ActionEvent e) {
					setViewMode(DATA_VIEW_MODE); } },
			new ResultsAction() {
				@Override public void actionPerformed(ActionEvent e) {
					setViewMode(RESULTS_VIEW_MODE); } },
			new ExportHtmlAction() {
				@Override public void actionPerformed(ActionEvent e) {
					saveHtml(); } },
			new AutomaticUpdateAction() {
				@Override public void actionPerformed(ActionEvent e) {
					setAutomaticUpdate(!isAutomaticUpdate());
					updateContents(); } },
			new ForceUpdateAction() {
				@Override public void actionPerformed(ActionEvent e) {
					forceUpdate(); } }
		}));
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if (evt.getSource() == heatmap.getMatrixView()) {
			if (MatrixView.SELECTED_LEAD_CHANGED.equals(name)) {
				updateContents();
			}
		}
	}

	private void updateContents() {
		if (!automaticUpdate && !forceUpdate)
			return;

		forceUpdate = false;
		
		VelocityContext context = createModel();
		context.put("exporting", false);
		String template = context.get("__template__").toString();

		try {
			URL url = getClass().getResource(template);
			templatePanel.setTemplateFromResource(template, url);
			templatePanel.render(context);
		}
		catch (Exception ex) {
			LogUtils.logException(ex, log);
		}
	}

	protected abstract VelocityContext createModel();

	public int getViewMode() {
		return viewMode;
	}

	public void setViewMode(int viewMode) {
		this.viewMode = viewMode;
		updateContents();
	}

	public boolean isAutomaticUpdate() {
		return automaticUpdate;
	}

	public void setAutomaticUpdate(boolean automaticUpdate) {
		this.automaticUpdate = automaticUpdate;
	}

	public void forceUpdate() {
		this.forceUpdate = true;
		updateContents();
	}
	
	protected void saveHtml() {
		AbstractEditor editor = AppFrame.instance().getEditorsPanel()
				.getSelectedEditor();

		if (editor == null)
			return;

		SaveFileWizard saveWiz = SaveFileWizard.createSimple(
				"Export html ...",
				PersistenceUtils.getFileName(editor.getName()),
				Settings.getDefault().getLastExportPath(),
				new FileFormat[] {
					FileFormats.HTML
				});

		WizardDialog dlg = new WizardDialog(AppFrame.instance(), saveWiz);
		dlg.setVisible(true);
		if (dlg.isCancelled())
			return;

		Settings.getDefault().setLastExportPath(saveWiz.getFolder());

		final File file = saveWiz.getPathAsFile();

		final String formatExtension = saveWiz.getFormat().getExtension();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Exporting html ...", 1);
					monitor.info("File: " + file.getName());

					VelocityContext context = createModel();
					context.put("exporting", true);
					String template = context.get("__template__").toString();

					try {
						URL url = getClass().getResource(template);
						templatePanel.setTemplateFromResource(template, url);
						templatePanel.render(context);
					}
					catch (Exception ex) {
						LogUtils.logException(ex, log);
					}

					Writer writer = new FileWriter(file);
					templatePanel.merge(context, writer);

					writer.close();

					monitor.end();
				}
				catch (Exception ex) {
					monitor.exception(ex);
				}
			}
		});

		AppFrame.instance().setStatusText("Ok.");
	}
}
