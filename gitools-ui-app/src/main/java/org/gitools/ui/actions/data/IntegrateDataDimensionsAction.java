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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.data.integration.DataIntegrationCriteria;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.*;
import org.gitools.model.Attribute;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.wizard.add.data.DataIntegrationDimensionsWizard;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import org.gitools.ui.platform.progress.JobThread;

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

        DataIntegrationDimensionsWizard wiz
                = new DataIntegrationDimensionsWizard(hm);
        WizardDialog dlg = new WizardDialog(AppFrame.instance(), wiz);
        dlg.setVisible(true);

        if (dlg.isCancelled())
            return;


        executeIntegrateDataDimensions(hm, wiz.getValues(), wiz.getCriteria(), wiz.getDimensionName());

        AppFrame.instance().setStatusText("Data integrated and added.");
    }



    public void executeIntegrateDataDimensions(Heatmap hm,
                                               double[] valuesArray,
                                               List<ArrayList<DataIntegrationCriteria>> criteriaList,
                                               String dimensionNameString) {
        
        final Heatmap heatmap = hm;
        final double[] values = valuesArray;
        final List<ArrayList<DataIntegrationCriteria>> criteria = criteriaList;
        final String dimensionName = dimensionNameString;
        
        JobThread.execute(AppFrame.instance(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                
                IElementAdapter adapter = heatmap.getMatrixView().getCellAdapter();
                String[] attributes = new String[adapter.getPropertyCount()+1];
                int c = 0;
                for (IElementAttribute iElementAttribute : adapter.getProperties()) {
                    attributes[c] = iElementAttribute.getName();
                    c++;
                };

                attributes[attributes.length-1] = dimensionName;

                ArrayElementAdapter newAdapter = new ArrayElementAdapter(attributes);
                ArrayElementFactory elementFactory = new ArrayElementFactory(attributes.length);

                int colNb = heatmap.getMatrixView().getContents().getColumnCount();
                int rowNb = heatmap.getMatrixView().getContents().getRowCount();

                List<Object[]> list = new ArrayList<Object[]>();

                ObjectMatrix objectMatrix = (ObjectMatrix) heatmap.getMatrixView().getContents();


                monitor.begin("Integrating Data", rowNb *2);
                int newIndex = attributes.length-1;

                for (int r = 0; r<rowNb; r++) {

                    for (c = 0; c<colNb; c++) {
                        Object element = elementFactory.create();

                        for (int a = 0; a<attributes.length-1; a++) {
                            newAdapter.setValue(element, a, objectMatrix.getCellValue(r, c, a));
                            list.add(new Object[]{
                                    new int[]{r, c}, element });
                        }

                        newAdapter.setValue(element, newIndex, 99.0);
                        list.add(new Object[] {
                                new int[] {r ,c } ,element });
                        monitor.worked(1);
                    }
                }

                objectMatrix.makeCells();

                for (Object[] result : list) {
                    int[] coord = (int[]) result[0];
                    final int columnIndex = coord[1];
                    final int rowIndex = coord[0];
                    Object element = result[1];
                    objectMatrix.setCell(rowIndex, columnIndex, element);
                }
                objectMatrix.setCellAdapter(newAdapter);
            }
        });
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
