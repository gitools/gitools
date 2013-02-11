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

package org.gitools.ui.actions.analysis;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.groupcomparison.GroupComparisonProcessor;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.analysis.groupcomparison.editor.GroupComparisonAnalysisEditor;
import org.gitools.ui.analysis.groupcomparison.wizard.GroupComparisonAnalysisFromEditorWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.settings.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GroupComparisonAction extends BaseAction {

	public GroupComparisonAction() {
		super("Group Comparison");
		setDesc("Group Comparison analysis");
	}

	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		final IEditor currentEditor = editorPanel.getSelectedEditor();

		Heatmap heatmap = ActionUtils.getHeatmap();
		IMatrixView matrixView = ActionUtils.getMatrixView();

		if (heatmap == null)
			return;

		List<IElementAttribute> attributes = matrixView.getCellAttributes();
		String[] attributeNames = new String[attributes.size()];
		for (int i = 0; i < attributes.size(); i++)
			attributeNames[i] = attributes.get(i).getName();

		GroupComparisonAnalysisFromEditorWizard wiz = new GroupComparisonAnalysisFromEditorWizard(heatmap);
		WizardDialog dlg = new WizardDialog(AppFrame.instance(), wiz);
		dlg.setVisible(true);

		if (dlg.isCancelled())
			return;

		final GroupComparisonAnalysis analysis = wiz.getAnalysis();

		analysis.setData(matrixView);

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					new GroupComparisonProcessor(analysis).run(monitor);

					if (monitor.isCancelled())
						return;

					final AnalysisDetailsEditor editor =
							new GroupComparisonAnalysisEditor(analysis);
								//TODO: adapt to group comparison analysis

					String ext = PersistenceUtils.getExtension(currentEditor.getName());
                    String analysisTitle = analysis.getTitle();

                    if (!analysisTitle.equals(""))
                        editor.setName(analysis.getTitle() + "." + FileSuffixes.GROUP_COMPARISON);
                    else
                        editor.setName(editorPanel.deriveName(currentEditor.getName(), ext, "", FileSuffixes.GROUP_COMPARISON));


                    SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							AppFrame.instance().getEditorsPanel().addEditor(editor);
							AppFrame.instance().refresh();
						}
					});

					monitor.end();

					AppFrame.instance().setStatusText("Done.");
				}
				catch (Throwable ex) {
					monitor.exception(ex);
				}
			}
		});

	}
}
