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
package org.gitools.ui.wizard.add.data;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.data.integration.DataIntegrationCriteria;
import org.gitools.ui.platform.wizard.AbstractWizard;

import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class DataIntegrationDimensionsWizard extends AbstractWizard
{
    private static final long serialVersionUID = -6058042494975580570L;

    private DataIntegrationPage dataIntegrationPage;
    private DataDetailsPage dataDetailsPage;
    private final Heatmap heatmap;

    private double[] values;
    private List<ArrayList<DataIntegrationCriteria>> criteria;
    private String dimensionName;

    public DataIntegrationDimensionsWizard(Heatmap heatmap)
    {
        super();
        this.heatmap = heatmap;
        setTitle("Add data to heatmap");
        setHelpContext("Integrate data dimensnsions.");
    }

    @Override
    public void addPages()
    {
        dataIntegrationPage = new DataIntegrationPage(heatmap);
        addPage(dataIntegrationPage);

        dataDetailsPage = new DataDetailsPage(heatmap);
        addPage(dataDetailsPage);
    }

    @Override
    public boolean canFinish()
    {
        // TODO Auto-generated method stub
        if (getCurrentPage() == dataDetailsPage)
        {
            if (dataDetailsPage.isComplete())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void performFinish()
    {
        values = dataIntegrationPage.getValues();
        criteria = dataIntegrationPage.getCriteria();
        dimensionName = dataDetailsPage.getDimensionName();
    }

    public List<ArrayList<DataIntegrationCriteria>> getCriteria()
    {
        return criteria;
    }

    public double[] getValues()
    {
        return values;
    }

    public String getDimensionName()
    {
        return dimensionName;
    }
    

        /*@Override
    public IWizardPage getNextPage(IWizardPage page) {
        IWizardPage currentPage = getCurrentPage();
        if (currentPage == dataIntegrationPage)
            return super.getPage(dataDetailsPage.getId());
        return super.getPage(currentPage.getId());
    }

    @Override
    public IWizardPage getPreviousPage(IWizardPage page) {
        IWizardPage currentPage = getCurrentPage();
        if (currentPage == dataDetailsPage) {
            return super.getPage(dataIntegrationPage.getId());
        }
        return super.getPage(currentPage.getId());
    }*/

}
