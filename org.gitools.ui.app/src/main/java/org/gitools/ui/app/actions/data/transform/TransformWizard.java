package org.gitools.ui.app.actions.data.transform;

import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.TransformFunction;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.ui.platform.wizard.AbstractWizard;

import java.util.List;

public class TransformWizard extends AbstractWizard {
    private Heatmap heatmap;
    private SelectTransformationsPage selectTransformationsPage;


    public TransformWizard(Heatmap heatmap) {

        this.heatmap = heatmap;
    }

    @Override
    public void addPages() {
        selectTransformationsPage = new SelectTransformationsPage(heatmap);
        addPage("transformations", selectTransformationsPage);
    }


    @Override
    public void performFinish() {
        super.performFinish();
    }

    public List<ConfigurableTransformFunction> getFunctions() {
        return selectTransformationsPage.getTransformationFunctions();
    }


    public IMatrixLayer getLayer() {
        return selectTransformationsPage.getLayer();
    }

    public MatrixLayer<Double> getNewLayer() {

        return selectTransformationsPage.getNewLayer();
    }
}
