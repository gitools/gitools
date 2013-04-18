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
package org.gitools.ui.wizard.export;

import org.gitools.ui.platform.wizard.AbstractWizard;

/**
 * @noinspection ALL
 */
public class ExportWizard extends AbstractWizard {

    private static final long serialVersionUID = -6058042494975580570L;

    private static final String PAGE_EXPORT_CHOOSER = "ExportChooser";

    public ExportWizard() {
        super();

        setTitle("Export");
    }

    @Override
    public void addPages() {
        addPage(PAGE_EXPORT_CHOOSER, new ExportChooserPage());
    }

    @Override
    public boolean canFinish() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void performFinish() {
        // TODO Auto-generated method stub

    }
}
