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
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.SwingUtils;
import info.clearthought.layout.TableLayout;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

/**
 * Create a property table panel
 */
public class PropertiesBox extends WebPanel
{
    private static final int DEFAULT_MARGIN = 20;
    private static final int MINIMUM_VALUE_LENGTH = 6;

    private int nextProperty = 1;
    private final int totalProperties;
    private int maxValueLength;

    /**
     * @param maxWidth   Maximum width of the panel
     * @param properties Properties to draw
     */
    public PropertiesBox(int maxWidth, @NotNull Collection<PropertyItem> properties)
    {
        this(maxWidth, properties.toArray(new PropertyItem[properties.size()]));
    }

    /**
     * @param maxWidth   Maximum width of the panel
     * @param properties Properties to draw
     */
    private PropertiesBox(int maxWidth, PropertyItem... properties)
    {
        this(maxWidth, null, properties);
    }

    /**
     * @param maxWidth   Maximum width of the panel
     * @param title      Optional title of the properties table
     * @param properties Properties to draw
     */
    private PropertiesBox(int maxWidth, @Nullable String title, @NotNull PropertyItem... properties)
    {
        maxValueLength = convertToCharacters(maxWidth) - maxValueLength(properties);
        maxValueLength = (maxValueLength < 8 ? 8 : maxValueLength);
        this.totalProperties = properties.length;
        setBackground(Color.WHITE);

        double columns[] = {5, TableLayout.PREFERRED, 10, TableLayout.FILL, 5};
        double rows[] = new double[3 + totalProperties * 2];
        rows[0] = (title == null) ? 2 : DEFAULT_MARGIN;
        rows[1] = 2;
        for (int i = 2; i < rows.length - 1; i += 2)
        {
            rows[i] = TableLayout.PREFERRED;
            rows[i + 1] = 2;
        }
        rows[rows.length - 1] = DEFAULT_MARGIN;

        TableLayout boxLayout = new TableLayout(new double[][]{columns, rows});
        boxLayout.setHGap(4);
        boxLayout.setVGap(4);
        setLayout(boxLayout);

        if (title != null)
        {
            add(createNameLabel(new PropertyItem(title, null, null)), "0,0,3,0,C,B");
        }

        add(createVerticalSeparator(), "2,1,2," + (rows.length - 2));

        for (PropertyItem property : properties)
        {
            addProperty(property);
        }

    }

    /**
     * Add a property to the table
     *
     * @param property The property to add
     */
    final void addProperty(@NotNull PropertyItem property)
    {
        int nextRow = nextProperty * 2;
        add(createHorizontalSeparator(), "0," + (nextRow - 1) + ",4," + (nextRow - 1));
        add(createNameLabel(property), "1," + nextRow);
        add(createValueLabel(property, maxValueLength), "3," + nextRow);
        nextProperty++;

        if (nextProperty > totalProperties)
        {
            add(createHorizontalSeparator(), "0," + (nextRow + 1) + ",4," + (nextRow + 1));
        }
    }

    @NotNull
    private static WebSeparator createHorizontalSeparator()
    {
        WebSeparator separator = new WebSeparator(WebSeparator.HORIZONTAL);
        separator.setDrawSideLines(false);
        return separator;
    }

    @NotNull
    private static WebSeparator createVerticalSeparator()
    {
        WebSeparator separator = new WebSeparator(WebSeparator.VERTICAL);
        separator.setDrawSideLines(false);
        return separator;
    }

    @NotNull
    private WebLabel createNameLabel(@NotNull PropertyItem property)
    {
        WebLabel label = new WebLabel(StringUtils.capitalize(property.getName()), JLabel.TRAILING);
        label.setDrawShade(true);
        SwingUtils.changeFontSize(label, -1);


        if (StringUtils.isNotEmpty(property.getDescription()))
        {
            TooltipManager.setTooltip(label, property.getDescription(), TooltipWay.down, 0);
        }

        if (property.isSelected())
        {
            SwingUtils.setBoldFont(label);
        }

        if (property.isSelectable())
        {
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            label.addMouseListener(new PropertyMouseListener(property));
        }

        return label;
    }

    private WebLabel createValueLabel(@NotNull PropertyItem property, int maxLength)
    {

        String value = property.getValue();

        boolean abbreviate = (value.length() > maxLength);

        String abbreviatedValue;
        if (abbreviate)
        {
            abbreviatedValue = StringUtils.abbreviate(value, maxLength);
        }
        else
        {
            abbreviatedValue = value;
        }

        WebLabel label;
        if (StringUtils.isEmpty(property.getLink()))
        {
            label = new WebLabel(abbreviatedValue);
        }
        else
        {
            WebLinkLabel webLabel = new WebLinkLabel(abbreviatedValue);
            webLabel.setIcon(WebLinkLabel.LINK_ICON);
            webLabel.setLink(property.getLink(), false);
            label = webLabel;
        }

        SwingUtils.changeFontSize(label, -1);

        if (abbreviate)
        {
            TooltipManager.setTooltip(label, value, TooltipWay.down, 0);
        }

        if (property.getColor() != null)
        {
            label.setDrawShade(true);
            label.setShadeColor(property.getColor());
        }

        if (property.isSelectable())
        {
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            label.addMouseListener(new PropertyMouseListener(property));
        }

        return label;
    }

    private static int convertToCharacters(int pixels)
    {
        return Math.round(pixels / 7);
    }

    private static int maxValueLength(@NotNull PropertyItem... properties)
    {
        int max = MINIMUM_VALUE_LENGTH;

        for (PropertyItem property : properties)
        {
            int length = property.getName().length();

            if (length > max)
            {
                max = length;
            }
        }

        return max;
    }

    private class PropertyMouseListener extends MouseAdapter
    {
        private PropertyItem item;

        private PropertyMouseListener(PropertyItem item)
        {
            this.item = item;
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            onMouseClick(item);
        }
    }

    protected void onMouseClick(PropertyItem propertyItem)
    {
        // Override
    }

}


