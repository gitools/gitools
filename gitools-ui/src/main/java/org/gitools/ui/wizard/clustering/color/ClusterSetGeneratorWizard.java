package org.gitools.ui.wizard.clustering.color;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapClusterSet;
import org.gitools.ui.platform.wizard.AbstractWizard;

public class ClusterSetGeneratorWizard extends AbstractWizard {
	private HeatmapClusterSet clusterSet;
	private boolean rowMode;
	private Heatmap heatmap;

	public ClusterSetGeneratorWizard(Heatmap heatmap, boolean rowMode) {
		super();
		this.clusterSet = new HeatmapClusterSet();
		this.rowMode = rowMode;
		this.heatmap = heatmap;
	}

	@Override
	public void addPages() {
		ClusterSetGeneratePage clusterSetGenerator = new ClusterSetGeneratePage(heatmap, clusterSet, rowMode);
		clusterSetGenerator.setTitle("Generate Cluster Set");
		addPage(clusterSetGenerator);

		ClusterSetEditorPage clusterSetEditorPage = new ClusterSetEditorPage(clusterSet);
		clusterSetEditorPage.setTitle("Generate Cluster Set");
		addPage(clusterSetEditorPage);

	}
	

	@Override
	public void performFinish() {
		// TODO Auto-generated method stub
	}

	public HeatmapClusterSet getClusterSet() {
		return this.clusterSet;
	}

}
