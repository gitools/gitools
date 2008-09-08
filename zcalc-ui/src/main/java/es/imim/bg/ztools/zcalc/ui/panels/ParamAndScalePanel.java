package es.imim.bg.ztools.zcalc.ui.panels;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.imim.bg.ztools.zcalc.ui.scale.ScaleManager;

public class ParamAndScalePanel extends JPanel {

	private static final long serialVersionUID = -8041894482792837388L;

	private String[] paramNames;
	private ScaleManager scaleManager;
	
	public ParamAndScalePanel(
			String[] paramNames,
			ScaleManager scaleManager) {
		
		this.paramNames = paramNames;
		this.scaleManager = scaleManager;
		
		createComponents();
	}
	
	private void createComponents() {
		
		final JLabel paramLabel = new JLabel("Parameter");
		final JComboBox paramCb = new JComboBox(paramNames);
		paramCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				System.out.println(e.getItem());
			}
			
		});
		
		final JPanel paramPanel = new JPanel();
		paramPanel.setLayout(new BorderLayout());
		paramPanel.add(paramLabel, BorderLayout.NORTH);
		paramPanel.add(paramCb, BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		add(paramPanel);
	}
}
