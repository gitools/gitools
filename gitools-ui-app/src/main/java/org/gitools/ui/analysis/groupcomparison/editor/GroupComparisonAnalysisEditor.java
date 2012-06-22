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

package org.gitools.ui.analysis.groupcomparison.editor;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.apache.velocity.VelocityContext;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

public class GroupComparisonAnalysisEditor extends AnalysisDetailsEditor<GroupComparisonAnalysis> {

	public GroupComparisonAnalysisEditor(GroupComparisonAnalysis analysis) {
		super(analysis, "/vm/analysis/groupcomparison/analysis_details.vm", null);
	}


	@Override
	protected void prepareContext(VelocityContext context) {

		PersistenceManager.FileRef fileRef = PersistenceManager.getDefault()
				.getEntityFileRef(analysis.getData());

		context.put("dataFile",
				fileRef != null ? fileRef.getFile().getName() : "Not defined");

		context.put("mtc", analysis.getMtc().getName());
	}

	@Override
	protected void performUrlAction(String name, Map<String, String> params) {
		if ("NewDataHeatmap".equals(name))
			newDataHeatmap();
/*		else if ("ViewModuleMap".equals(name))
			viewModuleMap();*/
		else if ("NewResultsHeatmap".equals(name))
			newResultsHeatmap();
	}

	private void newDataHeatmap() {
		if (analysis.getData() == null) {
			AppFrame.instance().setStatusText("Analysis doesn't contain data.");
			return;
		}

		final EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				monitor.begin("Creating new heatmap from data ...", 1);

				IMatrixView dataTable = new MatrixView(analysis.getData());

				Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
				heatmap.setTitle(analysis.getTitle() + " (data)");

				heatmap.getRowDim().setAnnotations(analysis.getRowAnnotations());
				copyHeaders(heatmap.getRowDim(), analysis.getRowHeaders());
				heatmap.getColumnDim().setAnnotations(analysis.getColumnAnnotations());
				copyHeaders(heatmap.getColumnDim(), analysis.getColumnHeaders());

				final HeatmapEditor editor = new HeatmapEditor(heatmap);


				editor.setName(editorPanel.deriveName(
						getName(), FileSuffixes.GROUP_COMPARISON,
						"-data", FileSuffixes.HEATMAP));

				SwingUtilities.invokeLater(new Runnable() {
					@Override public void run() {
						editorPanel.addEditor(editor);
						AppFrame.instance().setStatusText("New heatmap created.");
					}
				});
			}
		});
	}

	/*private void viewModuleMap() {
		UnimplementedDialog.show(AppFrame.instance());
	}*/

	private void copyHeaders(HeatmapDim dim, List<HeatmapHeader> headers) {

		dim.removeHeader(0);

		for (HeatmapHeader hh : headers ) {
			HeatmapHeader headerCopy = null;
			if (hh instanceof HeatmapTextLabelsHeader) {
				HeatmapTextLabelsHeader oldHeader = (HeatmapTextLabelsHeader) hh;
				HeatmapTextLabelsHeader textHeaderCopy = new HeatmapTextLabelsHeader(dim);
				textHeaderCopy.setFont(oldHeader.getFont());
				textHeaderCopy.setForegroundColor(oldHeader.getForegroundColor());
				textHeaderCopy.setLabelAnnotation(oldHeader.getLabelAnnotation());
				textHeaderCopy.setLabelPattern(oldHeader.getLabelPattern());
				textHeaderCopy.setLabelSource(oldHeader.getLabelSource());
				headerCopy = textHeaderCopy;
			} else if (hh instanceof HeatmapColoredLabelsHeader) {
				//headerCopy = hh;
				// Not working yet!
				
				HeatmapColoredLabelsHeader oldHeader = (HeatmapColoredLabelsHeader) hh;
				HeatmapColoredLabelsHeader colorHeaderCopy = new HeatmapColoredLabelsHeader(dim);
				colorHeaderCopy.setLabelColor(oldHeader.getLabelColor());
				colorHeaderCopy.setLabelColorDefined(oldHeader.isLabelColorDefined());
				colorHeaderCopy.setLabelFont(oldHeader.getLabelFont());
				colorHeaderCopy.setLabelRotated(oldHeader.isLabelRotated());
				colorHeaderCopy.setLabelVisible(oldHeader.isLabelVisible());
				colorHeaderCopy.setMargin(oldHeader.getMargin());
				colorHeaderCopy.setSeparationGrid(oldHeader.isSeparationGrid());
				colorHeaderCopy.setThickness(oldHeader.getThickness());

				ColoredLabel[] clusters = oldHeader.getClusters();
				ColoredLabel[] newClusters = new ColoredLabel[clusters.length];
				int index = 0;

				for (ColoredLabel cl : clusters) {
					//newClusters[index] ;
					ColoredLabel newcl =
							new ColoredLabel(cl.getDescription(), cl.getColor());
					newClusters[index] = newcl;
					index++;
				}
				colorHeaderCopy.setClusters(newClusters);
				colorHeaderCopy.setAssignedColoredLabels(
						oldHeader.getAssignedColoredLabels());

				dim.getAnnotations().getRows();

				headerCopy = colorHeaderCopy;
				
			}
			headerCopy.setBackgroundColor(hh.getBackgroundColor());
			//newHh.setHeatmapDim(dim);
			headerCopy.setSize(hh.getSize());
			headerCopy.setTitle(hh.getTitle());
			headerCopy.setVisible(hh.isVisible());
			dim.addHeader(headerCopy);
		}
	}

	private void newResultsHeatmap() {
		if (analysis.getResults() == null) {
			AppFrame.instance().setStatusText("Analysis doesn't contain results.");
			return;
		}

		final EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				monitor.begin("Creating new heatmap from results ...", 1);

				IMatrixView dataTable = new MatrixView(analysis.getResults());

				Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
				heatmap.setTitle(analysis.getTitle() + " (results)");
		
				heatmap.getRowDim().setAnnotations(analysis.getRowAnnotations());
				copyHeaders(heatmap.getRowDim(), analysis.getRowHeaders());

				final HeatmapEditor editor = new HeatmapEditor(heatmap);

				editor.setName(editorPanel.deriveName(
						getName(), FileSuffixes.GROUP_COMPARISON,
						"-results", FileSuffixes.HEATMAP));

				SwingUtilities.invokeLater(new Runnable() {
					@Override public void run() {
						editorPanel.addEditor(editor);
						AppFrame.instance().setStatusText("Heatmap for group comparison results created.");
					}
				});
			}
		});
	}
}
