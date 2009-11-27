package org.gitools.ui.panels.heatmap;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gitools.model.figure.MatrixFigure;

public class HeatmapPanel extends JPanel {

	private static final long serialVersionUID = 5817479437770943868L;

	private MatrixFigure heatmap;

	private HeatmapBodyPanel bodyPanel;

	private HeatmapHeaderPanel columnHeaderPanel;

	private HeatmapHeaderPanel rowHeaderPanel;
	
	public HeatmapPanel(MatrixFigure heatmap) {
		this.heatmap = heatmap;
		
		createComponents();
	}
	
	public MatrixFigure getHeatmap() {
		return heatmap;
	}
	
	public void setHeatmap(MatrixFigure heatmap) {
		this.heatmap = heatmap;
	}
	
	private void createComponents() {
		bodyPanel = new HeatmapBodyPanel(heatmap);
		columnHeaderPanel = new HeatmapHeaderPanel(heatmap, true);
		rowHeaderPanel = new HeatmapHeaderPanel(heatmap, false);
		
		JScrollPane scrollPane = new JScrollPane(bodyPanel);
		scrollPane.setViewportBorder(null);
		scrollPane.setColumnHeaderView(columnHeaderPanel);
		scrollPane.setRowHeaderView(rowHeaderPanel);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
}
