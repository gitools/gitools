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

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.Serializable;

/**
 * The type Property item.
 */
public class PropertyItem implements Serializable {
    private final String name;
    private final String description;
    private final String value;
    private final String link;
    private Color color;

    private int index;
    private boolean selectable = false;
    private boolean selected = false;

    /**
     * Instantiates a new Property item.
     *
     * @param name  the name
     * @param value the value
     */
    public PropertyItem(@NotNull String name, @NotNull String value) {
        this(name, null, value);
    }

    /**
     * Instantiates a new Property item.
     *
     * @param name        the name
     * @param description the description
     * @param value       the value
     */
    public PropertyItem(@NotNull String name, String description, @NotNull String value) {
        this(name, description, value, null);
    }

    /**
     * Instantiates a new Property item.
     *
     * @param name        the name
     * @param description the description
     * @param value       the value
     * @param valueLink   the value link
     */
    public PropertyItem(@NotNull String name, String description, @NotNull String value, String valueLink) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.link = valueLink;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets value link.
     *
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Gets value color.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    @NotNull
    public PropertyItem setColor(Color color) {
        this.color = color;

        return this;
    }

    /**
     * Is the current top layer selection.
     *
     * @return the boolean
     */
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
