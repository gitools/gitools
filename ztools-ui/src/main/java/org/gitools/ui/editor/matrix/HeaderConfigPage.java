package org.gitools.ui.editor.matrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.ui.component.ColorChooserLabel;
import org.gitools.ui.component.ColorChooserLabel.ColorChangeListener;

public class HeaderConfigPage extends JPanel {

	private static final long serialVersionUID = -8596459827146598515L;

	public enum HeaderType {
		rows, columns
	}
	
	private MatrixFigure model;
	private HeaderType type;
	
	public HeaderConfigPage(MatrixFigure model, HeaderType type) {
		this.model = model;
		this.type = type;
		
		createComponents();
	}

	private void createComponents() {
		int size = type == HeaderType.rows ? model.getRowSize() 
				: type == HeaderType.columns ? model.getColumnSize() : 0;
		final JSpinner sizeSpinner = new JSpinner(
				new SpinnerNumberModel(size, 0, 600, 8));
		sizeSpinner.addChangeListener(new ChangeListener() {
			@Override public void stateChanged(ChangeEvent e) {
				SpinnerNumberModel m = (SpinnerNumberModel) sizeSpinner.getModel();
				switch (type) {
				case rows: model.setRowSize(m.getNumber().intValue()); break;
				case columns: model.setColumnSize(m.getNumber().intValue()); break;
				}
			}
		});
		
		final HeaderDecorator hd = getHeaderDecorator();
		
		final ColorChooserLabel fgColorCc = new ColorChooserLabel(hd.getForegroundColor());
		fgColorCc.setToolTipText("Foreground color");
		fgColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				hd.setForegroundColor(color); }
		});
		
		final ColorChooserLabel bgColorCc = new ColorChooserLabel(hd.getBackgroundColor());
		bgColorCc.setToolTipText("Background color");
		bgColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				hd.setBackgroundColor(color);
			}
		});
		
		String sizeString = type == HeaderType.rows ? "Width" 
				: type == HeaderType.columns ? "Height" : "Size";
		final JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel(sizeString));
		p.add(sizeSpinner);
		p.add(Box.createRigidArea(new Dimension(8, 0)));
		p.add(new JLabel("Fg"));
		p.add(fgColorCc);
		p.add(Box.createRigidArea(new Dimension(8, 0)));
		p.add(new JLabel("Bg"));
		p.add(bgColorCc);
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		add(p, BorderLayout.CENTER);
	}
	
	protected HeaderDecorator getHeaderDecorator() {
		switch (type) {
		case rows: return model.getRowDecorator();
		case columns: return model.getColumnDecorator();
		}
		return null;
	}
	
}
