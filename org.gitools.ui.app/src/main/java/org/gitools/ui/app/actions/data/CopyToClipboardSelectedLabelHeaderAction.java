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
package org.gitools.ui.app.actions.data;

import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.heatmap.drawer.HeatmapPosition;
import org.gitools.ui.app.heatmap.popupmenus.dynamicactions.IHeatmapHeaderAction;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import static com.google.common.collect.Iterables.transform;
import static org.apache.commons.lang.StringUtils.isEmpty;


public class CopyToClipboardSelectedLabelHeaderAction extends HeatmapAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;

    public CopyToClipboardSelectedLabelHeaderAction() {
        super("<html><i>Copy</i> selected labels to clipboard<html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (header == null) {
            return;
        }

        StringBuilder content = new StringBuilder();
        for (String label : transform(header.getHeatmapDimension().getSelected(), header.getIdentifierTransform())) {
            if (!isEmpty(label)) {
                content.append(label).append('\n');
            }
        }

        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipBoard.setContents(new StringSelection(content.toString()), null);


    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {
        setEnabled(header instanceof HeatmapTextLabelsHeader);
        this.header = header;
    }
}
