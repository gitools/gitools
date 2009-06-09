package org.gitools.ui.editor.matrix;

import javax.swing.JPanel;

import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.figure.MatrixFigure;

public class HeaderConfigPage extends JPanel {

	private static final long serialVersionUID = -8596459827146598515L;

	private MatrixFigure model;
	private HeaderDecorator decorator;
	
	public HeaderConfigPage(MatrixFigure model, HeaderDecorator decorator) {
		this.model = model;
		this.decorator = decorator;
		
		createComponents();
	}

	private void createComponents() {
		
	}
	
}
