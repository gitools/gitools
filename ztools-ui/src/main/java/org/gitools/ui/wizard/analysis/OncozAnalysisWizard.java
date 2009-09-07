package org.gitools.ui.wizard.analysis;

import java.io.File;

import javax.swing.JFileChooser;

import org.gitools.ui.utils.Options;
import org.gitools.ui.wizard.AbstractWizard;
import org.gitools.ui.wizard.common.FileChooserPage;

public class OncozAnalysisWizard extends AbstractWizard {

	private FileChooserPage selectDestDirPage;
	private FileChooserPage dataFilePage;
	private DataFilteringPage dataFilteringPage;
	private StatisticalTestPage statisticalTestPage;
	private AnalysisDetailsPage analysisDetailsPage;

	public OncozAnalysisWizard() {
		super();
		
		setTitle("New Oncoz analysis");
	}
	
	@Override
	public void addPages() {
		// Destination directory
		
		selectDestDirPage = new FileChooserPage();
		selectDestDirPage.setTitle("Select destination folder");
		selectDestDirPage.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		selectDestDirPage.setSelectedFile(
				new File(Options.instance().getLastWorkPath()));
		addPage(selectDestDirPage);

		// Data file
		
		dataFilePage = new FileChooserPage();
		dataFilePage.setTitle("Select data file");
		dataFilePage.setCurrentPath(
				new File(Options.instance().getLastDataPath()));
		addPage(dataFilePage);
		
		// Data filtering
		
		dataFilteringPage = new DataFilteringPage();
		addPage(dataFilteringPage);
		
		// Statistical test
		
		statisticalTestPage = new StatisticalTestPage();
		addPage(statisticalTestPage);
		
		// Analysis details
		
		analysisDetailsPage = new AnalysisDetailsPage();
		addPage(analysisDetailsPage);
	}

}
