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
package org.gitools.ui.scale;

import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.wizard.common.SaveFilePage;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.NumericColorScale;

public class ScaleExportWizard extends AbstractWizard
{

    protected SaveFilePage savePage;
    protected ScaleExportConfigPage configPage;

    public ScaleExportWizard()
    {
        savePage = new SaveFilePage();
        configPage = new ScaleExportConfigPage();
    }

    public SaveFilePage getSavePage()
    {
        return savePage;
    }

    public void setScale(IColorScale scale)
    {

        if (scale instanceof NumericColorScale)
        {
            NumericColorScale nScale = (NumericColorScale) scale;
            configPage.setRange(nScale.getMinValue(), nScale.getMaxValue());
        }

    }

    @Override
    public void addPages()
    {
        addPage(savePage);
        addPage(configPage);
    }

    public boolean isPartialRange()
    {
        return configPage.isPartialRange();
    }

    public double getRangeMin()
    {
        return configPage.getRangeMin();
    }

    public double getRangeMax()
    {
        return configPage.getRangeMax();
    }

    public int getScaleSize()
    {
        return configPage.getScaleSize();
    }
}
