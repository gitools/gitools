/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.actions.data;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.data.integration.DataIntegrationCriteria;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.ArrayElementAdapter;
import org.gitools.matrix.model.element.ArrayElementFactory;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorDescriptor;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.wizard.add.data.DataIntegrationDimensionsWizard;
import org.gitools.utils.operators.Operator;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class IntegrateDataDimensionsAction extends BaseAction
{

    public IntegrateDataDimensionsAction()
    {
        super("Integrate data dimensions ...");
        setDesc("Integrate data dimensions from heatmap");
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        IEditor editor = AppFrame.get()
                .getEditorsPanel()
                .getSelectedEditor();

        Object model = editor != null ? editor.getModel() : null;
        if (model == null || !(model instanceof Heatmap))
        {
            return;
        }

        final Heatmap hm = (Heatmap) model;

        DataIntegrationDimensionsWizard wiz
                = new DataIntegrationDimensionsWizard(hm);
        WizardDialog dlg = new WizardDialog(AppFrame.get(), wiz);
        dlg.setVisible(true);

        if (dlg.isCancelled())
        {
            return;
        }


        executeIntegrateDataDimensions(hm, wiz.getValues(), wiz.getCriteria(), wiz.getDimensionName());

        AppFrame.get().setStatusText("Data integrated and added.");
    }


    public void executeIntegrateDataDimensions(Heatmap hm,
                                               double[] setToValuesArray,
                                               List<ArrayList<DataIntegrationCriteria>> criteriaList,
                                               String dimensionNameString)
    {

        final Heatmap heatmap = hm;
        final double[] setToValues = setToValuesArray;
        final List<ArrayList<DataIntegrationCriteria>> criteriaLists = criteriaList;
        final String dimensionName = dimensionNameString;

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {

                IElementAdapter adapter = heatmap.getMatrixView().getCellAdapter();
                String[] attributes = new String[adapter.getPropertyCount() + 1];
                int c = 0;
                for (IElementAttribute iElementAttribute : adapter.getProperties())
                {
                    attributes[c] = iElementAttribute.getName();
                    c++;
                }
                ;

                attributes[attributes.length - 1] = dimensionName;

                ArrayElementAdapter newAdapter = new ArrayElementAdapter(attributes);
                ArrayElementFactory elementFactory = new ArrayElementFactory(attributes.length);

                int colNb = heatmap.getMatrixView().getContents().getColumnCount();
                int rowNb = heatmap.getMatrixView().getContents().getRowCount();

                List<Object[]> list = new ArrayList<Object[]>();

                ObjectMatrix objectMatrix = (ObjectMatrix) heatmap.getMatrixView().getContents();


                monitor.begin("Integrating Data", rowNb * 2);
                int newIndex = attributes.length - 1;
                MatrixUtils.DoubleCast[] doubleCasts = new MatrixUtils.DoubleCast[attributes.length];
                for (int i = 0; i < adapter.getPropertyCount(); i++)
                {
                    doubleCasts[i] = MatrixUtils.createDoubleCast(
                            adapter.getProperty(i).getValueClass());
                }

                for (int r = 0; r < rowNb; r++)
                {

                    for (c = 0; c < colNb; c++)
                    {
                        Object element = elementFactory.create();

                        for (int a = 0; a < attributes.length - 1; a++)
                        {
                            newAdapter.setValue(element, a, objectMatrix.getCellValue(r, c, a));
                            list.add(new Object[]{
                                    new int[]{r, c}, element});
                        }

                        Object value = Double.NaN;
                        for (int i = 0; i < setToValues.length; i++)
                        {
                            if (evaluateCriteria(c, r, objectMatrix, doubleCasts[i], criteriaLists.get(i)))
                            {
                                value = setToValues[i];
                                break;
                            }
                        }
                        newAdapter.setValue(element, newIndex, value);
                        list.add(new Object[]{
                                new int[]{r, c}, element});
                        monitor.worked(1);
                    }
                }

                objectMatrix.makeCells();

                for (Object[] result : list)
                {
                    int[] coord = (int[]) result[0];
                    final int columnIndex = coord[1];
                    final int rowIndex = coord[0];
                    Object element = result[1];
                    objectMatrix.setCell(rowIndex, columnIndex, element);
                    monitor.worked(1);
                }
                objectMatrix.setCellAdapter(newAdapter);

                ElementDecorator[] oldDecorators = heatmap.getCellDecorators();
                ElementDecorator[] newDecorators = new ElementDecorator[oldDecorators.length + 1];
                for (int i = 0; i < oldDecorators.length; i++)
                {
                    newDecorators[i] = oldDecorators[i];
                    newDecorators[i].setAdapter(newAdapter);
                    newDecorators[i].setValueIndex(i);
                }
                ElementDecoratorDescriptor descriptor = new ElementDecoratorDescriptor("", oldDecorators[0].getClass());
                ElementDecorator decorator = ElementDecoratorFactory.create(
                        descriptor,
                        oldDecorators[0].getAdapter());
                newDecorators[oldDecorators.length] = decorator;
                decorator.setValueIndex(oldDecorators.length);
                heatmap.setCellDecorators(newDecorators);

            }
        });
    }

    private boolean evaluateCriteria(int column, int row, @NotNull ObjectMatrix objectMatrix, @NotNull MatrixUtils.DoubleCast doubleCast, @NotNull List<DataIntegrationCriteria> criteria)
    {
        ArrayList<Boolean> ORs = new ArrayList<Boolean>();
        for (DataIntegrationCriteria dic : criteria)
        {

            double matrixValue = doubleCast.getDoubleValue(
                    objectMatrix.getCellValue(row, column, dic.getAttributeIndex()));
            boolean evaluatedCondition = dic.getComparator().compare(
                    matrixValue, dic.getCutoffValue());

            if (dic.getOperator().equals(Operator.EMPTY))
            {
                ORs.add(evaluatedCondition);
            }
            else if (dic.getOperator().equals(Operator.OR))
            {
                ORs.add(evaluatedCondition);
            }
            else
            {
                Boolean lastOr;
                lastOr = ORs.get(ORs.size() - 1);
                ORs.remove(lastOr);
                ORs.add(dic.getOperator().evaluate(lastOr, evaluatedCondition));
            }
        }
        for (Boolean or : ORs)
            if (or)
            {
                return true;
            }
        return false;
    }

    @Override
    public boolean isEnabledByModel(Object model)
    {
        boolean isHeatmap = model instanceof Heatmap;
        boolean isMatrixView = model instanceof IMatrixView;
        int dimensions = 0;
        if (isHeatmap)
        {
            Heatmap h = (Heatmap) model;
            dimensions = h.getCellDecorators().length;
        }
        else if (isMatrixView)
        {
            IMatrixView mv = (IMatrixView) model;
            dimensions = mv.getContents().getCellAdapter().getPropertyCount();
        }

        return (isHeatmap || isMatrixView) && dimensions > 0;
    }
}
