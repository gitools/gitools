package org.gitools.ui.app.analysis.htest.wizard;

import org.gitools.analysis.Analysis;
import org.gitools.ui.platform.wizard.AbstractWizard;

public abstract class AnalysisWizard<A extends Analysis> extends AbstractWizard {

    public abstract A createAnalysis();

}
