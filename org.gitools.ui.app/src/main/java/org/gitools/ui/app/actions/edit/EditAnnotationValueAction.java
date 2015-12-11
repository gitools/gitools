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
package org.gitools.ui.app.actions.edit;

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.app.heatmap.panel.settings.headers.ChangeAnnotationValueSection;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.dialog.DialogButtonsPanel;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.platform.settings.SettingsDialog;
import org.gitools.ui.platform.settings.SettingsPanel;
import org.gitools.utils.textpattern.TextPattern;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class EditAnnotationValueAction extends HeatmapDimensionAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;
    private HeatmapPosition position;

    public EditAnnotationValueAction(MatrixDimensionKey dimensionKey, String name) {
        super(dimensionKey, name);
        setSmallIconFromResource(IconNames.edit16);
    }

    public EditAnnotationValueAction(HeatmapHeader header) {
        super(header.getHeatmapDimension().getId(), header.getTitle());
        setSmallIconFromResource(IconNames.edit16);
        this.header = header;
    }

    public HeatmapHeader getHeader() {
        return header;
    }

    public void setHeader(HeatmapHeader header) {
        this.header = header;
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        System.out.println(header.getTitle().toLowerCase());

        if (model instanceof Heatmap) {
            return !header.getTitle().toLowerCase().equals("id");
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        execute(header, position);
    }

    public static void execute(final HeatmapHeader header, final HeatmapPosition position) {

        List<ISettingsSection> sections = new ArrayList<>();
        final Set<String> selected = header.getHeatmapDimension().getSelected();

        final ChangeAnnotationValueSection annotationValueSection = new ChangeAnnotationValueSection(header, new ArrayList<>(selected));

        sections.add(annotationValueSection);


        SettingsPanel settingsPanel = new SettingsPanel(
                header.getTitle() + " annotation values",
                header.getDescription(),
                IconNames.logoNoText,
                sections
        );

        SettingsDialog dialog = new SettingsDialog(Application.get(), settingsPanel, sections.get(0).getName()) {

            @Override
            protected void close() {
                setVisible(false);
            }

            @Override
            protected void apply() {
                applyChanges(selected, annotationValueSection);
                header.getHeatmapDimension().updateHeaders();
            }

            @Override
            public DialogButtonsPanel getButtonsPanel() {
                JButton closeButton = new JButton("Cancel");
                closeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        close();
                    }
                });

                JButton applyButton = new JButton("OK");
                applyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        apply();
                        close();
                    }
                });

                closeButton.setDefaultCapable(true);
                return new DialogButtonsPanel(Arrays.asList(applyButton, closeButton));
            }
        };

        dialog.open();

    }

    private static void applyChanges(Set<String> selected, ChangeAnnotationValueSection annotationValueSection) {
        Map<String, String> inputs = annotationValueSection.getInputMap();
        List<TextPattern.VariableToken> annotationKeys = annotationValueSection.getAnnotationKeys();
        AnnotationMatrix annotations = annotationValueSection.getAnnotations();
        for (String s : selected) {
            for (TextPattern.VariableToken annotationKey : annotationKeys) {
                annotations.setAnnotation(s, annotationKey.toString(), inputs.get(annotationKey.getVariableName()));

            }

        }
    }

    @Override
    public void onConfigure(HeatmapHeader object, HeatmapPosition position) {
        setHeader(object);
        setPosition(position);

        setEnabled(!object.getTitle().toLowerCase().equals("id"));

    }


    public void setPosition(HeatmapPosition position) {
        this.position = position;
    }
}
