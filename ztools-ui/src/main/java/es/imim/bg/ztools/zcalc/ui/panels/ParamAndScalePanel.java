package es.imim.bg.ztools.zcalc.ui.panels;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import es.imim.bg.ztools.zcalc.ui.scale.ScaleManager;

public class ParamAndScalePanel extends ParamAndScalePanelUI {

	private static final long serialVersionUID = -8041894482792837388L;

	public interface ParamChangeListener {
		public void paramChange(String name);
	}
	
	private List<ParamChangeListener> paramChangeListeners = 
		new ArrayList<ParamChangeListener>();
	
	private String[] paramNames;
	private ScaleManager scaleManager;
	
	public ParamAndScalePanel(
			String[] paramNames,
			ScaleManager scaleManager) {
		
		super();
		
		this.paramNames = paramNames;
		this.scaleManager = scaleManager;
		
		this.paramCombo.setModel(new DefaultComboBoxModel(paramNames));
		this.paramCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					for (int i = 0; i < paramChangeListeners.size(); i++)
						paramChangeListeners.get(i).paramChange(e.getItem().toString());
				}
			}
		});
	}
	
	public void addParameterChangedListener(ParamChangeListener listener) {
		paramChangeListeners.add(listener);
	}
}
