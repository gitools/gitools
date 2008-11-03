package es.imim.bg.ztools.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import es.imim.bg.ztools.ui.model.ITableModel;

public class TableAndInfoView extends AbstractView {

	private static final long serialVersionUID = -204292290362958054L;

	private ITableModel tableModel;
	
	private JTextPane infoPane;
	private TableView tableView;
	
	public TableAndInfoView(final ITableModel tableModel) {
		
		this.tableModel = tableModel;
			
		createComponents();
		
		tableModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ITableModel.SELECTION_LEAD_PROPERTY)) {
					infoPane.setText(
							tableModel.getHtmlInfo());
				}
			}
		});
	}

	private void createComponents() {
		
		infoPane = new JTextPane();
		infoPane.setBackground(Color.WHITE);
		infoPane.setContentType("text/html");
		final JScrollPane scrollPane = new JScrollPane(infoPane);
		scrollPane.setBorder(
				BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		tableView = new TableView(tableModel);
		
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.add(infoPane);
		splitPane.add(tableView);
		splitPane.setDividerLocation(220);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}

	@Override
	public Object getModel() {
		return tableModel;
	}

	@Override
	public void refresh() {
		tableView.refresh();
	}

}
