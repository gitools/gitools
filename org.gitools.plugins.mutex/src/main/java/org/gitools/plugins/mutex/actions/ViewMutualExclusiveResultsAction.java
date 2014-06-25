/*
 * #%L
 * org.gitools.mutex
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.plugins.mutex.actions;

import org.gitools.plugins.mutex.MutualExclusiveBookmark;
import org.gitools.plugins.mutex.ui.IMutualExclusiveAction;
import org.gitools.plugins.mutex.ui.MutualExclusiveResultPage;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.platform.wizard.PageDialog;

import java.awt.event.ActionEvent;


public class ViewMutualExclusiveResultsAction extends HeatmapAction implements IMutualExclusiveAction {

    MutualExclusiveBookmark bookmark;

    public ViewMutualExclusiveResultsAction() {
        super("<html><i>View</i> test results</html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MutualExclusiveResultPage page =
                new MutualExclusiveResultPage(getHeatmap(),
                        bookmark);
        PageDialog dlg = new PageDialog(Application.get(), page);
        dlg.setVisible(true);

        if (dlg.isCancelled()) {
            return;
        }
    }

    @Override
    public void onConfigure(MutualExclusiveBookmark object, HeatmapPosition position) {
        bookmark = object;
    }
}
