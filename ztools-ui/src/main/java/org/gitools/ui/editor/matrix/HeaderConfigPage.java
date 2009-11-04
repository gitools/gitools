package org.gitools.ui.editor.matrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.decorator.impl.AnnotationHeaderDecorator;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.text.AnnotationMatrixTextPersistence;
import org.gitools.ui.component.ColorChooserLabel;
import org.gitools.ui.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.utils.Options;

import cern.colt.matrix.ObjectMatrix1D;
import edu.upf.bg.progressmonitor.NullProgressMonitor;

public class HeaderConfigPage extends JPanel {

	private static final long serialVersionUID = -8596459827146598515L;

	public enum HeaderType {
		rows, columns
	}
	
	private MatrixFigure model;
	private HeaderType type;
	private JComboBox annNameCombo;
	
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
		
		final JButton annButton = new JButton("...");
		annButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try {
					loadAnnotations();
				} catch (PersistenceException e1) {
					e1.printStackTrace();
					AppFrame.instance().setStatusText("Error loading annotations.");
				}
			}
		});
		
		final JLabel annNameLabel = new JLabel("Name");
		annNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		
		annNameCombo = new JComboBox();
		annNameCombo.setEditable(true);
		annNameCombo.setMinimumSize(new Dimension(120, 10));
		annNameCombo.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				getHeaderDecorator().setNamePattern(
						(String) annNameCombo.getSelectedItem());
			}
		});
		
		final JButton annFilterButton = new JButton("Filter");
		annFilterButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				filterByAnnotations(); }
		});
		
		final JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p3.add(new JLabel("Annotations"));
		p3.add(annButton);
		p3.add(annNameLabel);
		p3.add(annNameCombo);
		p3.add(Box.createRigidArea(new Dimension(8,0)));
		p3.add(annFilterButton);
		
		final JPanel p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		p2.add(p);
		p2.add(p3);
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		add(p2, BorderLayout.NORTH);
	}
	
	protected AnnotationHeaderDecorator getHeaderDecorator() {
		switch (type) {
		case rows: return (AnnotationHeaderDecorator) model.getRowDecorator();
		case columns: return (AnnotationHeaderDecorator) model.getColumnDecorator();
		}
		return null;
	}
	
	private void loadAnnotations() throws PersistenceException {
		File res = getSelectedPath();
		
		AnnotationMatrixTextPersistence amp = new AnnotationMatrixTextPersistence();
		
		AnnotationMatrix annMatrix = amp.read(res, new NullProgressMonitor());
		
		AnnotationHeaderDecorator decorator = getHeaderDecorator();
		decorator.setAnnotations(annMatrix);
		
		ObjectMatrix1D columns = annMatrix.getColumns();
		DefaultComboBoxModel cbmodel = new DefaultComboBoxModel();
		cbmodel.addElement("${id}");
		for (int i = 0; i < columns.size(); i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("${");
			sb.append(columns.getQuick(i).toString());
			sb.append("}");
			cbmodel.addElement(sb.toString());
		}
		annNameCombo.setModel(cbmodel);
		annNameCombo.setSelectedIndex(0);
	}
	
	private File getSelectedPath() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastPath());
		
		fileChooser.setDialogTitle("Select file");
		fileChooser.setMinimumSize(new Dimension(800,600));
		fileChooser.setPreferredSize(new Dimension(800,600));
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		
		return null;
	}
	
	private void filterByAnnotations() {
		IMatrixView matrixView = model.getMatrixView();
		
		int count = type == HeaderType.rows ? 
				matrixView.getRowCount() : matrixView.getColumnCount();
		
		AnnotationHeaderDecorator decorator = getHeaderDecorator();
		
		AnnotationMatrix annotations = decorator.getAnnotations();
		
		List<Integer> indices = new ArrayList<Integer>();
		
		for (int i = 0; i < count; i++) {
			String element = type == HeaderType.rows ?
					matrixView.getRowLabel(i) : matrixView.getColumnLabel(i);

			int j = annotations.getRowIndex(element);
			if (j >= 0)
				indices.add(i);
		}
		
		int[] view = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++)
			view[i] = indices.get(i);

		if (type == HeaderType.rows)
			matrixView.setVisibleRows(view);
		else
			matrixView.setVisibleColumns(view);
	}
}
