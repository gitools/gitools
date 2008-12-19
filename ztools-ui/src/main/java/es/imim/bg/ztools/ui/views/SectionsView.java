package es.imim.bg.ztools.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import es.imim.bg.ztools.ui.model.deprecated.ISectionModel;
import es.imim.bg.ztools.ui.model.deprecated.ITableModel;
import es.imim.bg.ztools.ui.model.deprecated.ISectionModel.SectionLayout;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.views.table.TableView;

@Deprecated
public abstract class SectionsView extends AbstractView {

	private static final long serialVersionUID = -6795423700290037713L;
	
	private int currentSection;
	private ISectionModel[] sectionModels;
	
	private TableView tableView;
	private JComboBox sectionCb;
	private JComboBox paramCb;
	private JTextPane infoPane;
	private JScrollPane infoScrollPane;

	private PropertyChangeListener sectionPropertyChangeListener;
	private PropertyChangeListener tablePropertyChangeListener;
	
	public SectionsView() {
		sectionPropertyChangeListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ISectionModel sectionModel = getCurrentSectionModel();
				if (ISectionModel.PARAM_COUNT_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
					paramCb.setModel(new DefaultComboBoxModel(
							sectionModel.getTableNames()));
				}
				else if (ISectionModel.SELECTION_PARAM_PROPERTY.equals(evt.getPropertyName())) {
					paramCb.setSelectedIndex(sectionModel.getCurrentTable());
				}
			}
		};
		
		tablePropertyChangeListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ISectionModel sectionModel = getCurrentSectionModel();
				if (ITableModel.HTML_INFO_PROPERTY.equals(evt.getPropertyName())) {
					//FIXME: infoPane.setText(sectionModel.getTable().getHtmlInfo());
				}
			}
		};
	}

	protected void setSectionModels(
			ISectionModel[] sectionModels, int initialSection) {
		
		ISectionModel oldSectionModel = this.sectionModels != null ? 
				getCurrentSectionModel() : null;
		
		this.sectionModels = sectionModels;
		this.currentSection = initialSection;
		
		changeSection(
				oldSectionModel, 
				sectionModels[currentSection]);
	}
	
	protected void createComponents() {
		ISectionModel sectionModel = getCurrentSectionModel();
		ITableModel currentModel = getCurrentModel();
	
		sectionCb = new JComboBox(sectionModels);
		sectionCb.setSelectedIndex(currentSection);
		sectionCb.setBorder(
				BorderFactory.createEmptyBorder(8, 8, 0, 8));
		sectionCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					changeSection(
							(ISectionModel) e.getSource(),
							(ISectionModel) e.getItem());
				}
			}
		});
		
		paramCb = new JComboBox(sectionModel.getTableNames());
		paramCb.setBorder(
				BorderFactory.createEmptyBorder(8, 8, 0, 8));
		paramCb.setSelectedIndex(sectionModel.getCurrentTable());
		paramCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					changeParam(e.getItem().toString());
				}
			}
		});
		
		infoPane = new JTextPane();
		infoPane.setBackground(Color.WHITE);
		infoPane.setContentType("text/html");
		infoScrollPane = new JScrollPane(infoPane);
		infoScrollPane.setBorder(
				BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		//tableView = new TableView(currentModel);
		
		setLayout(new BorderLayout());
		
		configureLayout();
		configureCbVisibility();
	}

	protected void changeSection(
			ISectionModel oldSectionModel,
			ISectionModel sectionModel) {
		
		if (oldSectionModel != null) {
			oldSectionModel
				.removePropertyChangeListener(sectionPropertyChangeListener);
			oldSectionModel.getTable()
				.removePropertyChangeListener(tablePropertyChangeListener);
		}
		
		sectionModel
			.addPropertyChangeListener(sectionPropertyChangeListener);
		sectionModel.getTable()
			.addPropertyChangeListener(tablePropertyChangeListener);
	}
	
	protected void changeParam(String paramName) {
		getCurrentSectionModel().setCurrentTable(paramName);
		tableView.refresh();
	}

	private void configureLayout() {
		SectionLayout layout = getCurrentLayout();
		
		int splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
		int cbAxis = BoxLayout.PAGE_AXIS;
		boolean leftOrTop = true;
		switch(layout) {
		case left:
		case right:
			splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
			cbAxis = BoxLayout.PAGE_AXIS;
			break;
		case top:
		case bottom:
			splitOrientation = JSplitPane.VERTICAL_SPLIT;
			cbAxis = BoxLayout.LINE_AXIS;
			break;
		}
		switch(layout) {
		case left:
		case top: leftOrTop =true; break;
		case right:
		case bottom: leftOrTop = false; break;
		}
		final JPanel cbPanel = new JPanel();
		cbPanel.setLayout(new BoxLayout(cbPanel, cbAxis));
		cbPanel.add(sectionCb);
		cbPanel.add(paramCb);
		
		final JPanel iPanel = new JPanel();
		iPanel.setLayout(new BorderLayout());
		iPanel.add(cbPanel, BorderLayout.NORTH);
		iPanel.add(infoScrollPane, BorderLayout.CENTER);
		
		final JSplitPane splitPane = new JSplitPane(splitOrientation);
		if (leftOrTop) {
			splitPane.add(iPanel);
			splitPane.add(tableView);
		}
		else {
			splitPane.add(tableView);
			splitPane.add(iPanel);
		}
		splitPane.setDividerLocation(220);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		
		add(splitPane, BorderLayout.CENTER);
	}

	private void configureCbVisibility() {
		ISectionModel sectionModel = getCurrentSectionModel();
		sectionCb.setVisible(sectionModels.length > 1);
		paramCb.setVisible(sectionModel.getTableCount() > 1);
	}
	
	private ISectionModel getCurrentSectionModel() {
		return sectionModels[currentSection];
	}
	
	private ITableModel getCurrentModel() {
		return sectionModels[currentSection].getTable();
	}
	
	private SectionLayout getCurrentLayout() {
		return sectionModels[currentSection].getLayout();
	}
	
	@Override
	public Object getModel() {
		return getCurrentModel();
	}
	
	@Override
	public void refresh() {
		tableView.refresh();
	}
	
	@Override
	public void refreshActions() {
		tableView.refreshActions();
	}
}
