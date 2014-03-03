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
package org.gitools.ui.app.heatmap.header;

import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class AddHeaderPage extends AbstractWizardPage {

    private final static Color SELECTED_BACKGROUND_COLOR = new Color(155, 155, 205);

    private class IconListRenderer extends DefaultListCellRenderer {


        private Map<Object, ImageIcon> icons = null;

        public IconListRenderer(Map<Object, ImageIcon> icons) {
            this.icons = icons;
        }


        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            // Get the renderer component from parent class

            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // Get icon to use for the list item value

            Icon icon = icons.get(value.toString());

            // Set icon to display for value

            label.setIcon(icon);

            if (isSelected) {
                setBackground(SELECTED_BACKGROUND_COLOR);
            }

            return label;
        }
    }


    private static class HeaderType {
        private final String title;
        private final Class<? extends HeatmapHeader> cls;

        public HeaderType(String title, Class<? extends HeatmapHeader> cls) {
            this.title = title;
            this.cls = cls;
        }

        public String getTitle() {
            return title;
        }

        public Class<? extends HeatmapHeader> getHeaderClass() {
            return cls;
        }

        @Override
        public String toString() {
            return title;
        }
    }


    private static final String ANNOTATION_TEXT_LABEL_HEADER = "Load values from file and show as a text header";

    private static final String ANNOTATION_COLORED_LABEL = "Load text values from file and show using a categorical color scale";

    public static final String ANNOTATION_HEATMAP = "Load numeric values from file and show using a numeric color scale";

    private static final String AGGREGATED_DATA_HEATMAP = "Calculate aggregated values from the heatmap and show using a numeric color scale";


    /**
     * Creates new form AddHeaderDialog
     */
    public AddHeaderPage() {
        initComponents();

        Map<Object, ImageIcon> icons = new HashMap<>();
        icons.put(ANNOTATION_TEXT_LABEL_HEADER, IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_ANNOTATION_TEXT_LABEL_HEADER, 60));
        icons.put(ANNOTATION_COLORED_LABEL, IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_ANNOTATION_COLORED_LABEL, 60));
        icons.put(ANNOTATION_HEATMAP, IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_ANNOTATION_HEATMAP, 60));
        icons.put(AGGREGATED_DATA_HEATMAP, IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_AGGREGATED_DATA_HEATMAP, 60));


        DefaultListModel model = new DefaultListModel();
        model.addElement(new HeaderType(ANNOTATION_TEXT_LABEL_HEADER, HeatmapTextLabelsHeader.class));
        model.addElement(new HeaderType(ANNOTATION_COLORED_LABEL, HeatmapColoredLabelsHeader.class));
        model.addElement(new HeaderType(ANNOTATION_HEATMAP, HeatmapDecoratorHeader.class));
        model.addElement(new HeaderType(AGGREGATED_DATA_HEATMAP, HeatmapDecoratorHeader.class));

        headerTypeList.setModel(model);
        headerTypeList.setCellRenderer(new IconListRenderer(icons));
        headerTypeList.setSelectedIndex(0);

        setTitle("Which type of header do you want to add ?");

        setComplete(true);
    }

    public Class<? extends HeatmapHeader> getHeaderClass() {
        return ((HeaderType) headerTypeList.getSelectedValue()).getHeaderClass();
    }

    public String getHeaderTitle() {
        return ((HeaderType) headerTypeList.getSelectedValue()).getTitle();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        headerTypeList = new javax.swing.JList();

        jScrollPane1.setViewportView(headerTypeList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE).addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList headerTypeList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup optGroup;
    // End of variables declaration//GEN-END:variables

}
