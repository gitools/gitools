/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app.heatmap.panel.settings.headers;

import com.jgoodies.binding.PresentationModel;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.utils.FontUtils;
import org.gitools.ui.app.utils.landf.MyWebColorChooserField;
import org.gitools.ui.platform.dialog.FontChooserDialog;
import org.gitools.ui.platform.settings.ISettingsSection;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.jgoodies.binding.adapter.Bindings.bind;

public class FormatSection implements ISettingsSection {
    private JPanel root;
    private JTextField textColorTextField;
    private JTextField backgroundColorTextField;
    private JTextField fontTextField;
    private JButton changeButton;
    private JCheckBox showValueCheckBox;

    public FormatSection(boolean showColors, boolean showValue, final HeatmapHeader heatmapHeader) {

        PresentationModel<HeatmapHeader> header = new PresentationModel<>(heatmapHeader);

        bind(textColorTextField, "color", header.getModel(HeatmapHeader.PROPERTY_LABEL_COLOR));
        bind(backgroundColorTextField, "color", header.getModel(HeatmapHeader.PROPERTY_BACKGROUND_COLOR));
        bind(showValueCheckBox, header.getModel(HeatmapHeader.PROPERTY_LABEL_VISIBLE));

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FontChooserDialog dlg = new FontChooserDialog(null, heatmapHeader.getFont(), false);
                dlg.setVisible(true);

                if (dlg.isCancelled()) {
                    return;
                }

                heatmapHeader.setFont(dlg.getFont());

                fontTextField.setFont(heatmapHeader.getFont());
                fontTextField.setText(FontUtils.fontText(heatmapHeader.getFont()));
            }
        });

        fontTextField.setFont(heatmapHeader.getFont());
        fontTextField.setText(FontUtils.fontText(heatmapHeader.getFont()));

        textColorTextField.setEnabled(showColors);
        backgroundColorTextField.setEnabled(showColors);
        showValueCheckBox.setVisible(showValue);

    }

    @Override
    public String getName() {
        return "Format";
    }

    @Override
    public JPanel getPanel() {
        return root;
    }

    private void createUIComponents() {
        textColorTextField = new MyWebColorChooserField();
        backgroundColorTextField = new MyWebColorChooserField();
    }
}
