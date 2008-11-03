package es.imim.bg.ztools.ui.views.analysis;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import es.imim.bg.ztools.ui.model.AnalysisModel;
import es.imim.bg.ztools.ui.model.ResultsModel;

public class AnalysisLateralPanel extends JPanel {

	private static final long serialVersionUID = -3844597759805854811L;

	private AnalysisModel analysisModel;

	//private JComboBox viewCb;
	//private JComboBox 
	private JTextPane infoPane;
	
	public AnalysisLateralPanel(AnalysisModel analysisModel) {
		
		this.analysisModel = analysisModel;
		
		createComponents();
	}
	
	private void createComponents() {
		
		ResultsModel rmodel = analysisModel.getResultsModel();
		
		String[] names = new String[rmodel.getParamCount()];
		for (int i = 0; i < names.length; i++)
			names[i] = rmodel.getParamName(i);
		
		infoPane = new JTextPane();
		infoPane.setBackground(Color.WHITE);
		infoPane.setContentType("text/html");
		final JScrollPane scrollPane = new JScrollPane(infoPane);
		
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		setLayout(new BorderLayout());
		add(scrollPane);
	}

	public void showInfo(String info) {
		infoPane.setText(info);
	}
}
