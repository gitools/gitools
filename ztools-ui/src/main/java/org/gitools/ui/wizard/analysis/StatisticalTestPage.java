package org.gitools.ui.wizard.analysis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;

import org.gitools.model.ToolConfig;
import org.gitools.stats.test.factory.BinomialTestFactory;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.stats.test.factory.ZscoreTestFactory;
import org.gitools.ui.wizard.AbstractWizardPage;

public class StatisticalTestPage extends AbstractWizardPage {

	private static class Test {
		public String name;
		public String description;
		public Test(String name, String description) {
			this.name = name;
			this.description = description;
		}
		@Override
		public String toString() {
			return name;
		}
	};
	
	private StatisticalTestPanel panel;

	public StatisticalTestPage() {
		setTitle("Select statistical test");
		setComplete(true);
	}
	
	@Override
	public JComponent createControls() {
		panel = new StatisticalTestPanel();
		panel.testCbox.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				Test test = (Test) panel.testCbox.getSelectedItem();
				panel.descLabel.setText(test.description);
				
				boolean isZ = panel.testCbox.getSelectedIndex() == 2;
				panel.samplingSizeLabel.setVisible(isZ);
				panel.samplingSizeCbox.setVisible(isZ);
				panel.estimatorLabel.setVisible(isZ);
				panel.estimatorCbox.setVisible(isZ);
			}
		});
		panel.testCbox.setModel(new DefaultComboBoxModel(new Test[] {
				new Test("Binomial (Bernoulli)", ""),
				new Test("Fisher Exact", ""),
				new Test("Z Score", "")
		}));
		
		panel.samplingSizeCbox.setSelectedItem(
				String.valueOf(ZscoreTestFactory.DEFAULT_NUM_SAMPLES));
		
		panel.estimatorCbox.setModel(new DefaultComboBoxModel(new String[] {
				ZscoreTestFactory.MEAN_ESTIMATOR,
				ZscoreTestFactory.MEDIAN_ESTIMATOR	
		}));
		
		return panel;
	}

	public ToolConfig getTestConfig() {
		ToolConfig config = new ToolConfig(ToolConfig.ZETCALC);
		
		switch (panel.testCbox.getSelectedIndex()) {
		case 0:
			config.put(
					TestFactory.TEST_NAME_PROPERTY,
					TestFactory.BINOMIAL_TEST);
			config.put(
					BinomialTestFactory.APROXIMATION_PROPERTY, 
					BinomialTestFactory.EXACT_APROX);
			break;
			
		case 1:
			config.put(
					TestFactory.TEST_NAME_PROPERTY,
					TestFactory.FISHER_EXACT_TEST);
			break;
			
		case 2:
			config.put(
					TestFactory.TEST_NAME_PROPERTY,
					TestFactory.ZSCORE_TEST);
			config.put(
					ZscoreTestFactory.NUM_SAMPLES_PROPERTY,
					panel.samplingSizeCbox.getSelectedItem().toString());
			config.put(
					ZscoreTestFactory.ESTIMATOR_PROPERTY,
					panel.estimatorCbox.getSelectedItem().toString());
			break;
		}
		
		return null;
	}

}
