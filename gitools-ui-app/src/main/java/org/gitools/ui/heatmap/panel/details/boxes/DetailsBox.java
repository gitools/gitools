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
package org.gitools.ui.heatmap.panel.details.boxes;

import com.alee.extended.label.WebLinkLabel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.SwingUtils;
import info.clearthought.layout.TableLayout;
import org.apache.commons.lang.StringUtils;
import org.gitools.core.model.decorator.DetailsDecoration;
import org.gitools.ui.IconNames;
import org.jdesktop.swingx.JXTaskPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Create a property table panel
 */
public class DetailsBox extends JXTaskPane {
    private static final int DEFAULT_MARGIN = 20;
    private static final int MINIMUM_VALUE_LENGTH = 12;

    private WebPanel container;

    /**
     * @param title     Optional title of the details table
     */
    public DetailsBox(String title) {
        super();
        setTitle(title);
        setSpecial(true);

        this.getContentPane().setBackground(Color.WHITE);

        this.container = new WebPanel();
        getContentPane().setBackground(Color.white);
        getContentPane().add(new WebScrollPane(container, false, false));
        container.setBackground(Color.WHITE);
        container.setBorder(null);
    }

    public void draw(List<DetailsDecoration> details) {

        container.removeAll();

        int maxValueLength = convertToCharacters(getWidth()) - maxValueLength(details);
        maxValueLength = (maxValueLength < 8 ? 8 : maxValueLength);

        double columns[] = {5, TableLayout.PREFERRED, 10, TableLayout.FILL, 5};
        double rows[] = new double[3 + details.size() * 2];
        rows[0] = 2;
        rows[1] = 2;
        for (int i = 2; i < rows.length - 1; i += 2) {
            rows[i] = TableLayout.PREFERRED;
            rows[i + 1] = 2;
        }
        rows[rows.length - 1] = DEFAULT_MARGIN;

        TableLayout boxLayout = new TableLayout(new double[][]{columns, rows});

        boxLayout.setHGap(4);
        boxLayout.setVGap(4);

        container.setLayout(boxLayout);
        container.validate();

        container.add(createVerticalSeparator(), "2,1,2," + (rows.length - 2));

        int nextDetail = 1;
        for (DetailsDecoration detail : details) {
            int nextRow = nextDetail * 2;
            container.add(createHorizontalSeparator(), "0," + (nextRow - 1) + ",4," + (nextRow - 1));
            container.add(createNameLabel(detail), "1," + nextRow);
            container.add(createValueLabel(detail, maxValueLength), "3," + nextRow);
            nextDetail++;

            if (nextDetail > details.size()) {
                container.add(createHorizontalSeparator(), "0," + (nextRow + 1) + ",4," + (nextRow + 1));
            }
        }

        container.repaint();

    }

    @NotNull
    private static WebSeparator createHorizontalSeparator() {
        WebSeparator separator = new WebSeparator(WebSeparator.HORIZONTAL);
        separator.setDrawSideLines(false);
        return separator;
    }

    @NotNull
    private static WebSeparator createVerticalSeparator() {
        WebSeparator separator = new WebSeparator(WebSeparator.VERTICAL);
        separator.setDrawSideLines(false);
        return separator;
    }

    @NotNull
    private Component createNameLabel(@NotNull DetailsDecoration detail) {

        WebLabel label = new WebLabel(StringUtils.capitalize(detail.getName()), JLabel.TRAILING);;
        label.setDrawShade(true);
        SwingUtils.changeFontSize(label, -1);


        if (StringUtils.isNotEmpty(detail.getDescription())) {
            TooltipManager.setTooltip(label, detail.getDescription(), TooltipWay.down, 0);
        }

        if (detail.isSelected()) {
            SwingUtils.setBoldFont(label);
        }

        if (detail.isSelectable()) {
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            label.addMouseListener(new PropertyMouseListener(detail));
        }

        if (!StringUtils.isEmpty(detail.getDescriptionLink())) {
            WebLinkLabel webLabel = new WebLinkLabel("", JLabel.TRAILING);
            webLabel.setIcon(IconNames.INFO_ICON);
            webLabel.setLink(detail.getDescriptionLink(), false);
            return new GroupPanel(GroupingType.fillFirst, 5, webLabel, label);
        }

        return label;
    }

    private WebLabel createValueLabel(@NotNull DetailsDecoration property, int maxLength) {

        String value = property.getFormatedValue();

        boolean abbreviate = (value.length() > maxLength);

        String abbreviatedValue;
        if (abbreviate) {
            abbreviatedValue = StringUtils.abbreviate(value, maxLength);
        } else {
            abbreviatedValue = value;
        }

        WebLabel label;
        if (StringUtils.isEmpty(property.getValueLink())) {
            label = new WebLabel(abbreviatedValue);
        } else {
            WebLinkLabel webLabel = new WebLinkLabel(abbreviatedValue);
            webLabel.setIcon(WebLinkLabel.LINK_ICON);
            webLabel.setLink(property.getValueLink(), false);
            label = webLabel;
        }

        SwingUtils.changeFontSize(label, -1);

        if (abbreviate) {
            TooltipManager.setTooltip(label, value, TooltipWay.down, 0);
        }

        if (property.getBgColor() != null) {
            label.setDrawShade(true);
            label.setShadeColor(property.getBgColor());
        }

        if (property.isSelectable()) {
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            label.addMouseListener(new PropertyMouseListener(property));
        }

        return label;
    }

    private static int convertToCharacters(int pixels) {
        return Math.round(pixels / 7);
    }

    private static int maxValueLength(List<DetailsDecoration> details) {
        int max = MINIMUM_VALUE_LENGTH;

        for (DetailsDecoration property : details) {
            int length = property.getFormatedValue().length();

            if (length > max) {
                max = length;
            }
        }

        return max;
    }

    private class PropertyMouseListener extends MouseAdapter {
        private DetailsDecoration item;

        private PropertyMouseListener(DetailsDecoration item) {
            this.item = item;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            onMouseClick(item);
        }
    }

    protected void onMouseClick(DetailsDecoration propertyItem) {
        // Override
    }

}


