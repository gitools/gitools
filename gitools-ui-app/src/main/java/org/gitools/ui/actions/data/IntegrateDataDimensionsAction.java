/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */


package org.gitools.ui.actions.data;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.wizard.add.data.DataIntegrationDimensionsWizard;

import java.awt.event.ActionEvent;

public class IntegrateDataDimensionsAction extends BaseAction {

    public IntegrateDataDimensionsAction() {
        super("Integrate data dimensions ...");
        setDesc("Integrate data dimensions from heatmap");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        IEditor editor = AppFrame.instance()
                .getEditorsPanel()
                .getSelectedEditor();

        Object model = editor != null ? editor.getModel() : null;
        if (model == null || !(model instanceof Heatmap))
            return;

        final Heatmap hm = (Heatmap) model;

        WizardDialog wizDlg = new WizardDialog(
                AppFrame.instance(),
                new DataIntegrationDimensionsWizard(hm));

        wizDlg.open();



        AppFrame.instance().setStatusText("Data integrated and added.");
    }
    @Override
    public boolean isEnabledByModel(Object model) {
        boolean isHeatmap = model instanceof Heatmap;
        boolean isMatrixView =  model instanceof IMatrixView;
        int dimensions = 0;
        if (isHeatmap) {
            Heatmap h = (Heatmap) model;
            dimensions = h.getCellDecorators().length;
        } else if (isMatrixView) {
            IMatrixView mv = (IMatrixView) model;
            dimensions = mv.getContents().getCellAdapter().getPropertyCount();
        }

        return (isHeatmap||isMatrixView) && dimensions > 1;
    }
}
