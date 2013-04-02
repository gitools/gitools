package org.gitools.ui.heatmap.panel.details.boxes;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.Serializable;

/**
 * The type Property item.
 */
public class PropertyItem implements Serializable
{
    private String name;
    private String description;
    private String value;
    private String link;
    private Color color;

    /**
     * Instantiates a new Property item.
     *
     * @param name the name
     * @param value the value
     */
    public PropertyItem(@NotNull String name, @NotNull String value) {
        this(name, null, value);
    }

    /**
     * Instantiates a new Property item.
     *
     * @param name the name
     * @param description the description
     * @param value the value
     */
    public PropertyItem(@NotNull String name, String description, @NotNull String value) {
        this(name, description, value, null);
    }

    /**
     * Instantiates a new Property item.
     *
     * @param name the name
     * @param description the description
     * @param value the value
     * @param valueLink the value link
     */
    public PropertyItem(@NotNull String name, String description, @NotNull String value, String valueLink)
    {
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
    public String getDescription()
    {
        return description;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Gets value link.
     *
     * @return the link
     */
    public String getLink()
    {
        return link;
    }

    /**
     * Gets value color.
     *
     * @return the color
     */
    public Color getColor()
    {
        return color;
    }

    public PropertyItem setColor(Color color)
    {
        this.color = color;

        return this;
    }

}
