package es.imim.bg.ztools.zcalc.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

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
		paramLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		final JComboBox paramCb = new JComboBox(paramNames);
		paramCb.setAlignmentX(Component.LEFT_ALIGNMENT);
		paramCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					System.out.println(e.getItem());
			}
		});
		
		final JPanel paramPanel = new JPanel();
		paramPanel.setLayout(new BoxLayout(paramPanel, BoxLayout.PAGE_AXIS));
		paramPanel.add(paramLabel);
		paramPanel.add(paramCb);
		
		/*final Border blackline = BorderFactory.createLineBorder(Color.lightGray);
		final TitledBorder titled = BorderFactory.createTitledBorder(blackline, 
				LangManager.instance().getString(LangKey.CFGDLG_LANG_TITLE));
		titled.setTitleJustification(TitledBorder.LEFT);
		titled.setTitlePosition(TitledBorder.DEFAULT_POSITION);*/
		
		setLayout(new BorderLayout());
		add(paramPanel, BorderLayout.NORTH);
	}
}
