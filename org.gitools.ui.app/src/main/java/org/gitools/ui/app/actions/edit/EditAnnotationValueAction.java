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

import org.gitools.api.analysis.Clusters;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.app.heatmap.panel.settings.headers.AddNewManualAnnotationSection;
import org.gitools.ui.app.heatmap.panel.settings.headers.ChangeAnnotationValueSection;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.dialog.DialogButtonsPanel;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.platform.settings.SettingsDialog;
import org.gitools.ui.platform.settings.SettingsPanel;
import org.gitools.utils.color.ColorGenerator;
import org.gitools.utils.textpattern.TextPattern;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import static org.gitools.ui.app.commands.AddHeaderColoredLabelsCommand.makeAnnotationClustering;

public class EditAnnotationValueAction extends HeatmapDimensionAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;
    private HeatmapPosition position;

    public EditAnnotationValueAction(MatrixDimensionKey dimensionKey) {
        super(dimensionKey, "<html><i>Annotate</i> selected ...</html>");
        setSmallIconFromResource(IconNames.anno16);
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

        final List<ISettingsSection> sections = new ArrayList<>();
        final Set<String> selected = header.getHeatmapDimension().getSelected();

        if (editable(header)) {
            final ChangeAnnotationValueSection annotationValueSection = new ChangeAnnotationValueSection(header, new ArrayList<>(selected));
            sections.add(annotationValueSection);
        }

        for (HeatmapHeader heatmapHeader : header.getHeatmapDimension().getHeaders()) {
            if (editable(heatmapHeader) && !header.getTitle().equals(heatmapHeader.getTitle())) {
                sections.add(new ChangeAnnotationValueSection(heatmapHeader, new ArrayList<String>(selected)));
            }
        }

        sections.add(new AddNewManualAnnotationSection(header.getHeatmapDimension().getAnnotations(), new ArrayList<String>(selected)));


        SettingsPanel settingsPanel = new SettingsPanel(
                "Edit & add annotation",
                "<html><body>Change the annotation of the selected " + header.getHeatmapDimension().getId().toString().toLowerCase() + " and/or add a new annotation.</body></html>",
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
                for (ISettingsSection section : sections) {
                    if (section.isDirty()) {
                        if (section instanceof ChangeAnnotationValueSection) {
                            applyChanges((ChangeAnnotationValueSection) section);
                        } else if (section instanceof AddNewManualAnnotationSection) {
                            addNewAnnotation((AddNewManualAnnotationSection) section);
                        }
                    }
                }
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

                JButton applyButton = new JButton("Apply");

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

    private static boolean editable(HeatmapHeader header) {
        return !header.getTitle().toLowerCase().equals("id")
                && !(header instanceof HierarchicalClusterHeatmapHeader);
    }

    private static void applyChanges(ChangeAnnotationValueSection section) {
        Map<String, String> inputs = section.getInputMap();
        List<TextPattern.VariableToken> annotationKeys = section.getAnnotationKeys();
        AnnotationMatrix annotations = section.getAnnotations();
        for (String s : section.getSelected()) {
            for (TextPattern.VariableToken annotationKey : annotationKeys) {
                annotations.setAnnotation(s, annotationKey.toString(), inputs.get(annotationKey.getVariableName()));
            }
        }

        if (section.getHeatmapHeader() instanceof HeatmapColoredLabelsHeader) {
            HeatmapColoredLabelsHeader h = (HeatmapColoredLabelsHeader) section.getHeatmapHeader();
            List<Color> usedColors = new ArrayList<>();
            for (ColoredLabel coloredLabel : h.getClusters()) {
                usedColors.add(coloredLabel.getColor());
            }
            ColorGenerator cg = new ColorGenerator();
            cg.initUsed(usedColors);


            Clusters results = makeAnnotationClustering(h);
            for (String s : results.getClusters()) {
                if (!h.getClusters().contains(s)) {
                    h.getClusters().add(new ColoredLabel(s, cg.next(s)));
                }
            }
        }
    }

    private static void addNewAnnotation(AddNewManualAnnotationSection section) {
        AnnotationMatrix annotations = section.getAnnotations();
        for (String s : section.getSelected()) {
                annotations.setAnnotation(s, section.getAnnotationLabel(), section.getAnnotationValue());
        }
    }

    @Override
    public void onConfigure(HeatmapHeader object, HeatmapPosition position) {
        setHeader(object);
        setPosition(position);
        setEnabled(getDimension().getSelected().size() > 0);

    }


    public void setPosition(HeatmapPosition position) {
        this.position = position;
    }
}
