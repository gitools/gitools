/*
 * #%L
 * org.gitools.ui.app
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
/* ===================================================
 * JFreeSVG : an SVG library for the Java(tm) platform
 * ===================================================
 * 
 * (C)opyright 2013, 2014, by Object Refinery Limited.  All rights reserved.
 *
 * Project Info:  http://www.jfree.org/jfreesvg/index.html
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 * 
 * If you do not wish to be bound by the terms of the AGPL, an alternative
 * commercial license can be purchased.  For details, please see visit the
 * JFreeSVG home page:
 * 
 * http://www.jfree.org/jfreesvg
 */

package org.gitools.ui.app.svg.jfreesvg;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A <code>Graphics2D</code> implementation that creates SVG output.  After 
 * rendering the graphics via the <code>SVGGraphics2D</code>, you can retrieve
 * an SVG element (see {@link #getSVGElement()}) or an SVG document (see 
 * {@link #getSVGDocument()}) containing your content.
 * <p>
 * <b>Usage</b><br>
 * Using the <code>SVGGraphics2D</code> class is straightforward.  First, 
 * create an instance specifying the height and width of the SVG element that 
 * will be created.  Then, use standard Java2D API calls to draw content 
 * into the element.  Finally, retrieve the SVG element that has been 
 * accumulated.  For example:
 * <p>
 * <code>&nbsp;&nbsp;&nbsp;&nbsp;SVGGraphics2D g2 = new SVGGraphics2D(300, 200);<br></code>
 * <code>&nbsp;&nbsp;&nbsp;&nbsp;g2.setPaint(Color.RED);<br></code>
 * <code>&nbsp;&nbsp;&nbsp;&nbsp;g2.draw(new Rectangle(10, 10, 280, 180);<br></code>
 * <code>&nbsp;&nbsp;&nbsp;&nbsp;String svgElement = g2.getSVGElement();<br></code>
 * <p>
 * For the content generation step, you can make use of third party libraries,
 * such as <a href="http://www.jfree.org/jfreechart/">JFreeChart</a>, that 
 * render output using standard Java2D API calls.
 * <p>
 * <b>Rendering Hints</b><br>
 * The <code>SVGGraphics2D</code> supports a couple of custom rendering hints -  
 * for details, refer to the {@link SVGHints} class documentation.
 * <p>
 * <b>Other Notes</b><br>
 * Some additional notes:
 * <ul>
 *
 * <li>Images are supported, but for methods with an <code>ImageObserver</code>
 * parameter note that the observer is ignored completely.  In any case, using 
 * images that are not fully loaded already would not be a good idea in the 
 * context of generating SVG data/files;</li>
 * 
 * <li>the {@link #getFontMetrics(java.awt.Font)} and
 * {@link #getFontRenderContext()} methods return values that come from an
 * internal <code>BufferedImage</code>, this is a short-cut and we don't know
 * if there are any negative consequences (if you know of any, please let us
 * know and we'll add the info here or find a way to fix it);</li>
 *
 * <li>there are settings to control the number of decimal places used to
 * write the coordinates for geometrical elements (default 2dp) and transform
 * matrices (default 6dp).  These defaults may change in a future release.</li>
 * </ul>
 *
 * For some demos showing how to use this class, look in the
 * <code>org.jfree.graphics2d.demo</code> package in the <code>src</code>
 * directory.
 */
public final class SVGGraphics2D extends Graphics2D {

    /** The prefix for keys used to identify clip paths. */
    private static final String CLIP_KEY_PREFIX = "clip-";

    private final int width;

    private final int height;

    /** Rendering hints (see SVGHints). */
    private final RenderingHints hints;

    /**
     * The number of decimal places to use when writing the matrix values
     * for transformations.
     */
    private int transformDP;

    /**
     * The decimal formatter for transform matrices.
     */
    private DecimalFormat transformFormat;

    /**
     * The number of decimal places to use when writing coordinates for
     * geometrical shapes.
     */
    private int geometryDP;

    /**
     * The decimal formatter for coordinates of geometrical shapes.
     */
    private DecimalFormat geometryFormat;

    /** The buffer that accumulates the SVG output. */
    private final StringBuilder sb;

    /**
     * A prefix for the keys used in the DEFS element.  This can be used to
     * ensure that the keys are unique when creating more than one SVG element
     * for a single HTML page.
     */
    private String defsKeyPrefix = "";

    /**
     * A map of all the gradients used, and the corresponding id.  When
     * generating the SVG file, all the gradient paints used must be defined
     * in the defs element.
     */
    private final Map<GradientPaintKey, String> gradientPaints
            = new HashMap<GradientPaintKey, String>();

    /**
     * A map of all the linear gradients used, and the corresponding id.  When
     * generating the SVG file, all the linear gradient paints used must be
     * defined in the defs element.
     */
    private final Map<LinearGradientPaintKey, String> linearGradientPaints
            = new HashMap<LinearGradientPaintKey, String>();

    /**
     * A map of all the radial gradients used, and the corresponding id.  When
     * generating the SVG file, all the radial gradient paints used must be
     * defined in the defs element.
     */
    private final Map<RadialGradientPaintKey, String> radialGradientPaints
            = new HashMap<RadialGradientPaintKey, String>();

    /**
     * A list of the registered clip regions.  These will be written to the
     * DEFS element.
     */
    private final List<String> clipPaths = new ArrayList<String>();

    /**
     * The filename prefix for images that are referenced rather than
     * embedded but don't have an <code>href</code> supplied via the
     * {@link #KEY_IMAGE_HREF} hint.
     */
    private String filePrefix;

    /**
     * The filename suffix for images that are referenced rather than
     * embedded but don't have an <code>href</code> supplied via the
     * {@link #KEY_IMAGE_HREF} hint.
     */
    private String fileSuffix;

    /**
     * A list of images that are referenced but not embedded in the SVG.
     * After the SVG is generated, the caller can make use of this list to
     * write PNG files if they don't already exist.
     */
    private final List<ImageElement> imageElements;

    /** The user clip (can be null). */
    private Shape clip;

    /** The reference for the current clip. */
    private String clipRef;

    /** The current transform. */
    private AffineTransform transform = new AffineTransform();

    private Paint paint = Color.BLACK;

    private Color color = Color.BLACK;

    private Composite composite = AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, 1.0f);

    /** The current stroke. */
    private Stroke stroke = new BasicStroke(1.0f);

    /**
     * The width of the SVG stroke to use when the user supplies a
     * BasicStroke with a width of 0.0 (in this case the Java specificatin
     * says "If width is set to 0.0f, the stroke is rendered as the thinnest
     * possible line for the target device and the antialias hint setting."
     */
    private double zeroStrokeWidth;

    /** The last font that was set. */
    private Font font;

    /** Maps font family names to alternates (or leaves them unchanged). */
    private FontMapper fontMapper;

    /** The background color, used by clearRect(). */
    private Color background = Color.BLACK;

    /** A hidden image used for font metrics. */
    private final BufferedImage fmImage = new BufferedImage(10, 10,
            BufferedImage.TYPE_INT_RGB);

    /**
     * An instance that is lazily instantiated in drawLine and then
     * subsequently reused to avoid creating a lot of garbage.
     */
    private Line2D line;

    /**
     * An instance that is lazily instantiated in fillRect and then
     * subsequently reused to avoid creating a lot of garbage.
     */
    Rectangle2D rect;

    /**
     * An instance that is lazily instantiated in draw/fillRoundRect and then
     * subsequently reused to avoid creating a lot of garbage.
     */
    private RoundRectangle2D roundRect;

    /**
     * An instance that is lazily instantiated in draw/fillOval and then
     * subsequently reused to avoid creating a lot of garbage.
     */
    private Ellipse2D oval;

    /**
     * An instance that is lazily instantiated in draw/fillArc and then
     * subsequently reused to avoid creating a lot of garbage.
     */
    private Arc2D arc;

    /**
     * If the current paint is an instance of {@link GradientPaint}, this
     * field will contain the reference id that is used in the DEFS element
     * for that linear gradient.
     */
    private String gradientPaintRef = null;

    /**
     * The device configuration (this is lazily instantiated in the
     * getDeviceConfiguration() method.
     */
    private GraphicsConfiguration deviceConfiguration;

    /** A set of element IDs. */
    private final Set<String> elementIDs;

    /**
     * Creates a new instance with the specified width and height.
     *
     * @param width  the width of the SVG element.
     * @param height  the height of the SVG element.
     */
    public SVGGraphics2D(int width, int height) {
        this.width = width;
        this.height = height;
        this.defsKeyPrefix = "";
        this.clip = null;
        this.imageElements = new ArrayList<ImageElement>();
        this.filePrefix = "image-";
        this.fileSuffix = ".png";
        this.font = new Font("SansSerif", Font.PLAIN, 12);
        this.fontMapper = new StandardFontMapper();
        this.zeroStrokeWidth = 0.1;
        this.sb = new StringBuilder();
        this.hints = new RenderingHints(SVGHints.KEY_IMAGE_HANDLING,
                SVGHints.VALUE_IMAGE_HANDLING_EMBED);
        // force the formatters to use a '.' for the decimal point
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        this.transformFormat = new DecimalFormat("0.######", dfs);
        this.geometryFormat = new DecimalFormat("0.##", dfs);
        this.elementIDs = new HashSet<String>();
    }

    /**
     * Returns the width for the SVG element, specified in the constructor.
     * This value will be written to the SVG element returned by the
     * {@link #getSVGElement()} method.
     *
     * @return The width for the SVG element.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Returns the height for the SVG element, specified in the constructor.
     * This value will be written to the SVG element returned by the
     * {@link #getSVGElement()} method.
     *
     * @return The height for the SVG element.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns the prefix used for all keys in the DEFS element.  The default
     * value is the empty string.
     *
     * @return The prefix string (never <code>null</code>).
     *
     * @since 1.9
     */
    public String getDefsKeyPrefix() {
        return this.defsKeyPrefix;
    }

    /**
     * Sets the prefix that will be used for all keys in the DEFS element.
     * If required, this must be set immediately after construction (before any
     * content generation methods have been called).
     *
     * @param prefix  the prefix (<code>null</code> not permitted).
     *
     * @since 1.9
     */
    public void setDefsKeyPrefix(String prefix) {
        Args.nullNotPermitted(prefix, "prefix");
        this.defsKeyPrefix = prefix;
    }

    /**
     * Returns the number of decimal places used to write the transformation
     * matrices in the SVG output.  The default value is 6.
     * <p>
     * Note that there is a separate attribute to control the number of decimal
     * places for geometrical elements in the output (see
     * {@link #getGeometryDP()}).
     *
     * @return The number of decimal places.
     *
     * @see #setTransformDP(int)
     */
    public int getTransformDP() {
        return this.transformDP;
    }

    /**
     * Sets the number of decimal places used to write the transformation
     * matrices in the SVG output.  Values in the range 1 to 10 will be used
     * to configure a formatter to that number of decimal places, for all other
     * values we revert to the normal <code>String</code> conversion of
     * <code>double</code> primitives (approximately 16 decimals places).
     * <p>
     * Note that there is a separate attribute to control the number of decimal
     * places for geometrical elements in the output (see
     * {@link #setGeometryDP(int)}).
     *
     * @param dp  the number of decimal places (normally 1 to 10).
     *
     * @see #getTransformDP()
     */
    public void setTransformDP(int dp) {
        this.transformDP = dp;
        if (dp < 1 || dp > 10) {
            this.transformFormat = null;
            return;
        }
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        this.transformFormat = new DecimalFormat("0."
                + "##########".substring(0, dp), dfs);
    }

    /**
     * Returns the number of decimal places used to write the coordinates
     * of geometrical shapes.  The default value is 2.
     * <p>
     * Note that there is a separate attribute to control the number of decimal
     * places for transform matrices in the output (see
     * {@link #getTransformDP()}).
     *
     * @return The number of decimal places.
     */
    public int getGeometryDP() {
        return this.geometryDP;
    }

    /**
     * Sets the number of decimal places used to write the coordinates of
     * geometrical shapes in the SVG output.  Values in the range 1 to 10 will
     * be used to configure a formatter to that number of decimal places, for
     * all other values we revert to the normal String conversion of double
     * primitives (approximately 16 decimals places).
     * <p>
     * Note that there is a separate attribute to control the number of decimal
     * places for transform matrices in the output (see
     * {@link #setTransformDP(int)}).
     *
     * @param dp  the number of decimal places (normally 1 to 10).
     */
    public void setGeometryDP(int dp) {
        this.geometryDP = dp;
        if (dp < 1 || dp > 10) {
            this.geometryFormat = null;
            return;
        }
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        this.geometryFormat = new DecimalFormat("0."
                + "##########".substring(0, dp), dfs);
    }

    /**
     * Returns the prefix used to generate a filename for an image that is
     * referenced from, rather than embedded in, the SVG element.
     *
     * @return The file prefix (never <code>null</code>).
     *
     * @since 1.5
     */
    public String getFilePrefix() {
        return this.filePrefix;
    }

    /**
     * Sets the prefix used to generate a filename for any image that is
     * referenced from the SVG element.
     *
     * @param prefix  the new prefix (<code>null</code> not permitted).
     *
     * @since 1.5
     */
    public void setFilePrefix(String prefix) {
        Args.nullNotPermitted(prefix, "prefix");
        this.filePrefix = prefix;
    }

    /**
     * Returns the suffix used to generate a filename for an image that is
     * referenced from, rather than embedded in, the SVG element.
     *
     * @return The file suffix (never <code>null</code>).
     *
     * @since 1.5
     */
    public String getFileSuffix() {
        return this.fileSuffix;
    }

    /**
     * Sets the suffix used to generate a filename for any image that is
     * referenced from the SVG element.
     *
     * @param suffix  the new prefix (<code>null</code> not permitted).
     *
     * @since 1.5
     */
    public void setFileSuffix(String suffix) {
        Args.nullNotPermitted(suffix, "suffix");
        this.fileSuffix = suffix;
    }

    /**
     * Returns the width to use for the SVG stroke when the AWT stroke
     * specified has a zero width (the default value is <code>0.1</code>.  In
     * the Java specification for <code>BasicStroke</code> it states "If width
     * is set to 0.0f, the stroke is rendered as the thinnest possible
     * line for the target device and the antialias hint setting."  We don't
     * have a means to implement that accurately since we must specify a fixed
     * width.
     *
     * @return The width.
     *
     * @since 1.9
     */
    public double getZeroStrokeWidth() {
        return this.zeroStrokeWidth;
    }

    /**
     * Sets the width to use for the SVG stroke when the current AWT stroke
     * has a width of 0.0.
     *
     * @param width  the new width (must be 0 or greater).
     *
     * @since 1.9
     */
    public void setZeroStrokeWidth(double width) {
        if (width < 0.0) {
            throw new IllegalArgumentException("Width cannot be negative.");
        }
        this.zeroStrokeWidth = width;
    }

    /**
     * Returns the device configuration associated with this
     * <code>Graphics2D</code>.
     *
     * @return The graphics configuration.
     */
    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        if (this.deviceConfiguration == null) {
            this.deviceConfiguration = new SVGGraphicsConfiguration(this.width,
                    this.height);
        }
        return this.deviceConfiguration;
    }

    /**
     * Creates a new graphics object that is a copy of this graphics object
     * (except that it has not accumulated the drawing operations).  Not sure
     * yet when or why this would be useful when creating SVG output.
     *
     * @return A new graphics object.
     */
    @Override
    public Graphics create() {
        SVGGraphics2D copy = new SVGGraphics2D(this.width, this.height);
        copy.setRenderingHints(getRenderingHints());
        copy.setClip(getClip());
        copy.setPaint(getPaint());
        copy.setColor(getColor());
        copy.setComposite(getComposite());
        copy.setStroke(getStroke());
        copy.setFont(getFont());
        copy.setTransform(getTransform());
        copy.setBackground(getBackground());
        copy.setFilePrefix(getFilePrefix());
        copy.setFileSuffix(getFileSuffix());
        return copy;
    }

    /**
     * Returns the paint used to draw or fill shapes (or text).  The default
     * value is {@link Color#BLACK}.
     *
     * @return The paint (never <code>null</code>).
     *
     * @see #setPaint(java.awt.Paint)
     */
    @Override
    public Paint getPaint() {
        return this.paint;
    }

    /**
     * Sets the paint used to draw or fill shapes (or text).  If
     * <code>paint</code> is an instance of <code>Color</code>, this method will
     * also update the current color attribute (see {@link #getColor()}). If
     * you pass <code>null</code> to this method, it does nothing (in
     * accordance with the JDK specification).
     *
     * @param paint  the paint (<code>null</code> is permitted but ignored).
     *
     * @see #getPaint()
     */
    @Override
    public void setPaint(Paint paint) {
        if (paint == null) {
            return;
        }
        this.paint = paint;
        this.gradientPaintRef = null;
        if (paint instanceof Color) {
            setColor((Color) paint);
        } else if (paint instanceof GradientPaint) {
            GradientPaint gp = (GradientPaint) paint;
            GradientPaintKey key = new GradientPaintKey(gp);
            String ref = this.gradientPaints.get(key);
            if (ref == null) {
                int count = this.gradientPaints.keySet().size();
                String id = this.defsKeyPrefix + "gp" + count;
                this.elementIDs.add(id);
                this.gradientPaints.put(key, id);
                this.gradientPaintRef = id;
            } else {
                this.gradientPaintRef = ref;
            }
        } else if (paint instanceof LinearGradientPaint) {
            LinearGradientPaint lgp = (LinearGradientPaint) paint;
            LinearGradientPaintKey key = new LinearGradientPaintKey(lgp);
            String ref = this.linearGradientPaints.get(key);
            if (ref == null) {
                int count = this.linearGradientPaints.keySet().size();
                String id = this.defsKeyPrefix + "lgp" + count;
                this.elementIDs.add(id);
                this.linearGradientPaints.put(key, id);
                this.gradientPaintRef = id;
            }
        } else if (paint instanceof RadialGradientPaint) {
            RadialGradientPaint rgp = (RadialGradientPaint) paint;
            RadialGradientPaintKey key = new RadialGradientPaintKey(rgp);
            String ref = this.radialGradientPaints.get(key);
            if (ref == null) {
                int count = this.radialGradientPaints.keySet().size();
                String id = this.defsKeyPrefix + "rgp" + count;
                this.elementIDs.add(id);
                this.radialGradientPaints.put(key, id);
                this.gradientPaintRef = id;
            }
        }
    }

    /**
     * Returns the foreground color.  This method exists for backwards
     * compatibility in AWT, you should use the {@link #getPaint()} method.
     *
     * @return The foreground color (never <code>null</code>).
     *
     * @see #getPaint()
     */
    @Override
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the foreground color.  This method exists for backwards
     * compatibility in AWT, you should use the
     * {@link #setPaint(java.awt.Paint)} method.
     *
     * @param c  the color (<code>null</code> permitted but ignored).
     *
     * @see #setPaint(java.awt.Paint)
     */
    @Override
    public void setColor(Color c) {
        if (c == null) {
            return;
        }
        this.color = c;
        this.paint = c;
    }

    /**
     * Returns the background color.  The default value is {@link Color#BLACK}.
     * This is used by the {@link #clearRect(int, int, int, int)} method.
     *
     * @return The background color (possibly <code>null</code>).
     *
     * @see #setBackground(java.awt.Color)
     */
    @Override
    public Color getBackground() {
        return this.background;
    }

    /**
     * Sets the background color.  This is used by the
     * {@link #clearRect(int, int, int, int)} method.  The reference
     * implementation allows <code>null</code> for the background color so
     * we allow that too (but for that case, the clearRect method will do
     * nothing).
     *
     * @param color  the color (<code>null</code> permitted).
     *
     * @see #getBackground()
     */
    @Override
    public void setBackground(Color color) {
        this.background = color;
    }

    /**
     * Returns the current composite.
     *
     * @return The current composite (never <code>null</code>).
     *
     * @see #setComposite(java.awt.Composite)
     */
    @Override
    public Composite getComposite() {
        return this.composite;
    }

    /**
     * Sets the composite (only <code>AlphaComposite</code> is handled).
     *
     * @param comp  the composite (<code>null</code> not permitted).
     *
     * @see #getComposite()
     */
    @Override
    public void setComposite(Composite comp) {
        if (comp == null) {
            throw new IllegalArgumentException("Null 'comp' argument.");
        }
        this.composite = comp;
    }

    /**
     * Returns the current stroke (used when drawing shapes).
     *
     * @return The current stroke (never <code>null</code>).
     *
     * @see #setStroke(java.awt.Stroke)
     */
    @Override
    public Stroke getStroke() {
        return this.stroke;
    }

    /**
     * Sets the stroke that will be used to draw shapes.
     *
     * @param s  the stroke (<code>null</code> not permitted).
     *
     * @see #getStroke()
     */
    @Override
    public void setStroke(Stroke s) {
        if (s == null) {
            throw new IllegalArgumentException("Null 's' argument.");
        }
        this.stroke = s;
    }

    /**
     * Returns the current value for the specified hint.  See the
     * {@link SVGHints} class for information about the hints that can be
     * used with <code>SVGGraphics2D</code>.
     *
     * @param hintKey  the hint key (<code>null</code> permitted, but the
     *     result will be <code>null</code> also).
     *
     * @return The current value for the specified hint
     *     (possibly <code>null</code>).
     *
     * @see #setRenderingHint(java.awt.RenderingHints.Key, java.lang.Object)
     */
    @Override
    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return this.hints.get(hintKey);
    }

    /**
     * Sets the value for a hint.  See the {@link SVGHints} class for
     * information about the hints that can be used with this implementation.
     *
     * @param hintKey  the hint key (<code>null</code> not permitted).
     * @param hintValue  the hint value.
     *
     * @see #getRenderingHint(java.awt.RenderingHints.Key)
     */
    @Override
    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        if (hintKey == null) {
            throw new NullPointerException("Null 'hintKey' not permitted.");
        }
        // KEY_BEGIN_GROUP and KEY_END_GROUP are handled as special cases that
        // never get stored in the hints map...
        if (SVGHints.isBeginGroupKey(hintKey)) {
            String groupId = null;
            String ref = null;
            List<Entry> otherKeysAndValues = null;
            if (hintValue instanceof String) {
                groupId = (String) hintValue;
             } else if (hintValue instanceof Map) {
                Map hintValueMap = (Map) hintValue;
                groupId = (String) hintValueMap.get("id");
                ref = (String) hintValueMap.get("ref");
                for (final Object obj: hintValueMap.entrySet()) {
                   final Entry e = (Entry) obj;
                   final Object key = e.getKey();
                   if ("id".equals(key) || "ref".equals(key)) {
                      continue;
                   }
                   if (otherKeysAndValues == null) {
                      otherKeysAndValues = new ArrayList<Entry>();
                   }
                   otherKeysAndValues.add(e);
                }
            }
            this.sb.append("<g");
            if (groupId != null) {
                if (this.elementIDs.contains(groupId)) {
                    throw new IllegalArgumentException("The group id ("
                            + groupId + ") is not unique.");
                } else {
                    this.sb.append(" id=\"").append(groupId).append("\"");
                    this.elementIDs.add(groupId);
                }
            }
            if (ref != null) {
                this.sb.append(" jfreesvg:ref=\"");
                this.sb.append(SVGUtils.escapeForXML(ref)).append("\"");
            }
            if (otherKeysAndValues != null) {
               for (final Entry e: otherKeysAndValues) {
                    this.sb.append(" ").append(e.getKey()).append("=\"");
                    this.sb.append(SVGUtils.escapeForXML(String.valueOf(
                            e.getValue()))).append("\"");
               }
            }
            this.sb.append(">");
        } else if (SVGHints.isEndGroupKey(hintKey)) {
            this.sb.append("</g>\n");
        } else if (SVGHints.isElementTitleKey(hintKey) && (hintValue != null)) {
            this.sb.append("<title>");
            this.sb.append(SVGUtils.escapeForXML(String.valueOf(hintValue)));
            this.sb.append("</title>");
        } else {
            this.hints.put(hintKey, hintValue);
        }
    }

    /**
     * Returns a copy of the rendering hints.  Modifying the returned copy
     * will have no impact on the state of this <code>Graphics2D</code>
     * instance.
     *
     * @return The rendering hints (never <code>null</code>).
     *
     * @see #setRenderingHints(java.util.Map)
     */
    @Override
    public RenderingHints getRenderingHints() {
        return (RenderingHints) this.hints.clone();
    }

    /**
     * Sets the rendering hints to the specified collection.
     *
     * @param hints  the new set of hints (<code>null</code> not permitted).
     *
     * @see #getRenderingHints()
     */
    @Override
    public void setRenderingHints(Map<?, ?> hints) {
        this.hints.clear();
        addRenderingHints(hints);
    }

    /**
     * Adds all the supplied rendering hints.
     *
     * @param hints  the hints (<code>null</code> not permitted).
     */
    @Override
    public void addRenderingHints(Map<?, ?> hints) {
        this.hints.putAll(hints);
    }

    /**
     * A utility method that appends an optional element id if one is
     * specified via the rendering hints.
     *
     * @param sb  the string builder (<code>null</code> not permitted).
     */
    private void appendOptionalElementIDFromHint(StringBuilder sb) {
        String elementID = (String) this.hints.get(SVGHints.KEY_ELEMENT_ID);
        if (elementID != null) {
            this.hints.put(SVGHints.KEY_ELEMENT_ID, null); // clear it
            if (this.elementIDs.contains(elementID)) {
                throw new IllegalStateException("The element id "
                        + elementID + " is already used.");
            } else {
                this.elementIDs.add(elementID);
            }
            this.sb.append("id=\"").append(elementID).append("\" ");
        }
    }

    /**
     * Draws the specified shape with the current <code>paint</code> and
     * <code>stroke</code>.  There is direct handling for <code>Line2D</code>,
     * <code>Rectangle2D</code> and <code>Path2D</code>. All other shapes are
     * mapped to a <code>GeneralPath</code> and then drawn (effectively as
     * <code>Path2D</code> objects).
     *
     * @param s  the shape (<code>null</code> not permitted).
     *
     * @see #fill(java.awt.Shape)
     */
    @Override
    public void draw(Shape s) {
        // if the current stroke is not a BasicStroke then it is handled as
        // a special case
        if (!(this.stroke instanceof BasicStroke)) {
            fill(this.stroke.createStrokedShape(s));
            return;
        }
        if (s instanceof Line2D) {
            Line2D l = (Line2D) s;
            this.sb.append("<line ");
            appendOptionalElementIDFromHint(this.sb);
            this.sb.append("x1=\"").append(geomDP(l.getX1()))
                    .append("\" y1=\"").append(geomDP(l.getY1()))
                    .append("\" x2=\"").append(geomDP(l.getX2()))
                    .append("\" y2=\"").append(geomDP(l.getY2()))
                    .append("\" ");
            this.sb.append("style=\"").append(strokeStyle()).append("\" ");
            this.sb.append("transform=\"").append(getSVGTransform(
                    this.transform)).append("\" ");
            this.sb.append(getClipPathRef());
            this.sb.append("/>");
        } else if (s instanceof Rectangle2D) {
            Rectangle2D r = (Rectangle2D) s;
            this.sb.append("<rect ");
            appendOptionalElementIDFromHint(this.sb);
            this.sb.append("x=\"").append(geomDP(r.getX()))
                    .append("\" y=\"").append(geomDP(r.getY()))
                    .append("\" width=\"").append(geomDP(r.getWidth()))
                    .append("\" height=\"").append(geomDP(r.getHeight()))
                    .append("\" ");
            this.sb.append("style=\"").append(strokeStyle())
                    .append("; fill: none").append("\" ");
            this.sb.append("transform=\"").append(getSVGTransform(
                    this.transform)).append("\" ");
            this.sb.append(getClipPathRef());
            this.sb.append("/>");
        } else if (s instanceof Path2D) {
            Path2D path = (Path2D) s;
            this.sb.append("<g ");
            appendOptionalElementIDFromHint(this.sb);
            this.sb.append("style=\"").append(strokeStyle())
                    .append("; fill: none").append("\" ");
            this.sb.append("transform=\"").append(getSVGTransform(
                    this.transform)).append("\" ");
            this.sb.append(getClipPathRef());
            this.sb.append(">");
            this.sb.append("<path ").append(getSVGPathData(path)).append("/>");
            this.sb.append("</g>");
        } else {
            draw(new GeneralPath(s)); // handled as a Path2D next time through
        }
    }

    /**
     * Fills the specified shape with the current <code>paint</code>.  There is
     * direct handling for <code>Rectangle2D</code> and <code>Path2D</code>.
     * All other shapes are mapped to a <code>GeneralPath</code> and then
     * filled.
     *
     * @param s  the shape (<code>null</code> not permitted).
     *
     * @see #draw(java.awt.Shape)
     */
    @Override
    public void fill(Shape s) {
        if (s instanceof Rectangle2D) {
            Rectangle2D r = (Rectangle2D) s;
            if (r.isEmpty()) {
                return;
            }
            this.sb.append("<rect ");
            appendOptionalElementIDFromHint(this.sb);
            this.sb.append("x=\"").append(geomDP(r.getX()))
                    .append("\" y=\"").append(geomDP(r.getY()))
                    .append("\" width=\"").append(geomDP(r.getWidth()))
                    .append("\" height=\"").append(geomDP(r.getHeight()))
                    .append("\" ");
            this.sb.append("style=\"").append(getSVGFillStyle()).append("\" ");
            this.sb.append("transform=\"").append(getSVGTransform(
                    this.transform)).append("\" ");
            this.sb.append(getClipPathRef());
            this.sb.append("/>");
        } else if (s instanceof Path2D) {
            Path2D path = (Path2D) s;
            this.sb.append("<g ");
            appendOptionalElementIDFromHint(this.sb);
            this.sb.append("style=\"").append(getSVGFillStyle());
            this.sb.append("; stroke: none").append("\" ");
            this.sb.append("transform=\"").append(getSVGTransform(
                    this.transform)).append("\" ");
            this.sb.append(getClipPathRef());
            this.sb.append(">");
            this.sb.append("<path ").append(getSVGPathData(path)).append("/>");
            this.sb.append("</g>");
        }  else {
            fill(new GeneralPath(s));  // handled as a Path2D next time through
        }
    }

    /**
     * Creates an SVG path string for the supplied Java2D path.
     *
     * @param path  the path (<code>null</code> not permitted).
     *
     * @return An SVG path string.
     */
    private String getSVGPathData(Path2D path) {
        StringBuilder b = new StringBuilder("d=\"");
        float[] coords = new float[6];
        double[] closePt = null;
        boolean first = true;
        PathIterator iterator = path.getPathIterator(null);
        while (!iterator.isDone()) {
            int type = iterator.currentSegment(coords);
            if (!first) {
                b.append(" ");
            }
            first = false;
            switch (type) {
            case (PathIterator.SEG_MOVETO):
                closePt = new double[2];
                closePt[0] = coords[0];
                closePt[1] = coords[1];
                b.append("M ").append(geomDP(coords[0])).append(" ")
                        .append(geomDP(coords[1]));
                break;
            case (PathIterator.SEG_LINETO):
                b.append("L ").append(geomDP(coords[0])).append(" ")
                        .append(geomDP(coords[1]));
                break;
            case (PathIterator.SEG_QUADTO):
                b.append("Q ").append(geomDP(coords[0]))
                        .append(" ").append(geomDP(coords[1]))
                        .append(" ").append(geomDP(coords[2]))
                        .append(" ").append(geomDP(coords[3]));
                break;
            case (PathIterator.SEG_CUBICTO):
                b.append("C ").append(geomDP(coords[0])).append(" ")
                        .append(geomDP(coords[1])).append(" ")
                        .append(geomDP(coords[2])).append(" ")
                        .append(geomDP(coords[3])).append(" ")
                        .append(geomDP(coords[4])).append(" ")
                        .append(geomDP(coords[5]));
                break;
            case (PathIterator.SEG_CLOSE):
                if (closePt != null) {
                    b.append("M ").append(geomDP(closePt[0])).append(" ")
                            .append(geomDP(closePt[1]));
                }
                break;
            default:
                break;
            }
            iterator.next();
        }
        return b.append("\"").toString();
    }

    /**
     * Returns the current alpha (transparency) in the range 0.0 to 1.0.
     * If the current composite is an {@link AlphaComposite} we read the alpha
     * value from there, otherwise this method returns 1.0.
     *
     * @return The current alpha (transparency) in the range 0.0 to 1.0.
     */
    private float getAlpha() {
       float alpha = 1.0f;
       if (this.composite instanceof AlphaComposite) {
           AlphaComposite ac = (AlphaComposite) this.composite;
           alpha = ac.getAlpha();
       }
       return alpha;
    }

    /**
     * Returns an SVG color string based on the current paint.  To handle
     * <code>GradientPaint</code> we rely on the <code>setPaint()</code> method
     * having set the <code>gradientPaintRef</code> attribute.
     *
     * @return An SVG color string.
     */
    private String svgColorStr() {
        String result = "black;";
        if (this.paint instanceof Color) {
            return rgbaColorStr((Color) this.paint);
        } else if (this.paint instanceof GradientPaint
                || this.paint instanceof LinearGradientPaint
                || this.paint instanceof RadialGradientPaint) {
            return "url(#" + this.gradientPaintRef + ")";
        }
        return result;
    }

    /**
     * Returns the SVG RGB color string for the specified color.
     *
     * @param c  the color (<code>null</code> not permitted).
     *
     * @return The SVG RGB color string.
     */
    private String rgbColorStr(Color c) {
        StringBuilder b = new StringBuilder("rgb(");
        b.append(c.getRed()).append(",").append(c.getGreen()).append(",")
                .append(c.getBlue()).append(")");
        return b.toString();
    }

    /**
     * Returns a string representing the specified color in RGBA format.
     *
     * @param c  the color (<code>null</code> not permitted).
     *
     * @return The SVG RGBA color string.
     */
    private String rgbaColorStr(Color c) {
        return "#" + Integer.toHexString((c.getRGB() & 0xffffff) | 0x1000000).substring(1);
    }

    /**
     * Returns a stroke style string based on the current stroke and
     * alpha settings.
     *
     * @return A stroke style string.
     */
    private String strokeStyle() {
        double strokeWidth = 1.0f;
        float[] dashArray = new float[0];
        if (this.stroke instanceof BasicStroke) {
            // in fact this method doesn't get called for other stroke types
            BasicStroke bs = (BasicStroke) this.stroke;
            strokeWidth = bs.getLineWidth() > 0.0 ? bs.getLineWidth()
                    : this.zeroStrokeWidth;
            dashArray = bs.getDashArray();
        }
        StringBuilder b = new StringBuilder();
        b.append("stroke-width: ").append(strokeWidth).append(";");
        b.append("stroke: ").append(svgColorStr()).append(";");
        b.append("stroke-opacity: ").append(getAlpha()).append(";");
        if (dashArray != null && dashArray.length != 0) {
            b.append("stroke-dasharray: ");
            for (int i = 0; i < dashArray.length; i++) {
                if (i != 0) b.append(", ");
                b.append(dashArray[i]);
            }
            b.append(";");
        }
        return b.toString();
    }

    /**
     * Returns a fill style string based on the current paint and
     * alpha settings.
     *
     * @return A fill style string.
     */
    private String getSVGFillStyle() {
        StringBuilder b = new StringBuilder();
        b.append("fill: ").append(svgColorStr()).append(";");
        b.append("fill-opacity: ").append(getAlpha());
        return b.toString();
    }

    /**
     * Returns the current font used for drawing text.
     *
     * @return The current font (never <code>null</code>).
     *
     * @see #setFont(java.awt.Font)
     */
    @Override
    public Font getFont() {
        return this.font;
    }

    /**
     * Sets the font to be used for drawing text.
     *
     * @param font  the font (<code>null</code> is permitted but ignored).
     *
     * @see #getFont()
     */
    @Override
    public void setFont(Font font) {
        if (font == null) {
            return;
        }
        this.font = font;
    }

    /**
     * Returns the font mapper (an object that optionally maps font family
     * names to alternates).  The default mapper will convert Java logical
     * font names to the equivalent SVG generic font name, and leave all other
     * font names unchanged.
     *
     * @return The font mapper (never <code>null</code>).
     *
     * @see #setFontMapper(FontMapper)
     * @since 1.5
     */
    public FontMapper getFontMapper() {
        return this.fontMapper;
    }

    /**
     * Sets the font mapper.
     *
     * @param mapper  the font mapper (<code>null</code> not permitted).
     *
     * @since 1.5
     */
    public void setFontMapper(FontMapper mapper) {
        Args.nullNotPermitted(mapper, "mapper");
        this.fontMapper = mapper;
    }

    /**
     * Returns a string containing font style info.
     *
     * @return A string containing font style info.
     */
    private String getSVGFontStyle() {
        StringBuilder b = new StringBuilder();
        b.append("fill: ").append(svgColorStr()).append("; ");
        String fontFamily = this.fontMapper.mapFont(this.font.getFamily());
        b.append("font-family: ").append(fontFamily).append("; ");
        b.append("font-size: ").append(this.font.getSize()).append("px; ");
        if (this.font.isBold()) {
            b.append("font-weight: bold; ");
        }
        if (this.font.isItalic()) {
            b.append("font-style: italic; ");
        }
        return b.toString();
    }

    /**
     * Returns the font metrics for the specified font.
     *
     * @param f  the font.
     *
     * @return The font metrics.
     */
    @Override
    public FontMetrics getFontMetrics(Font f) {
        return this.fmImage.createGraphics().getFontMetrics(f);
    }

    /**
     * Returns the font render context.  The implementation here returns the
     * <code>FontRenderContext</code> for an image that is maintained
     * internally (as for {@link #getFontMetrics}).
     *
     * @return The font render context.
     */
    @Override
    public FontRenderContext getFontRenderContext() {
        return this.fmImage.createGraphics().getFontRenderContext();
    }

    /**
     * Draws a string at <code>(x, y)</code>.  The start of the text at the
     * baseline level will be aligned with the <code>(x, y)</code> point.
     * <br><br>
     * Note that you can make use of the {@link SVGHints#KEY_TEXT_RENDERING}
     * hint when drawing strings (this is completely optional though).
     *
     * @param str  the string (<code>null</code> not permitted).
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     *
     * @see #drawString(java.lang.String, float, float)
     */
    @Override
    public void drawString(String str, int x, int y) {
        drawString(str, (float) x, (float) y);
    }

    /**
     * Draws a string at <code>(x, y)</code>. The start of the text at the
     * baseline level will be aligned with the <code>(x, y)</code> point.
     * <br><br>
     * Note that you can make use of the {@link SVGHints#KEY_TEXT_RENDERING}
     * hint when drawing strings (this is completely optional though).
     *
     * @param str  the string (<code>null</code> not permitted).
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     */
    @Override
    public void drawString(String str, float x, float y) {
        if (str == null) {
            throw new NullPointerException("Null 'str' argument.");
        }
        this.sb.append("<g ");
        appendOptionalElementIDFromHint(this.sb);
        this.sb.append("transform=\"").append(getSVGTransform(
                    this.transform)).append("\">");
        this.sb.append("<text x=\"").append(geomDP(x))
                .append("\" y=\"").append(geomDP(y))
                .append("\"");
        this.sb.append(" style=\"").append(getSVGFontStyle()).append("\"");
        String textRenderValue = "auto";
        Object hintValue = getRenderingHint(SVGHints.KEY_TEXT_RENDERING);
        if (hintValue != null) {
            textRenderValue = hintValue.toString();
        }
        this.sb.append(" text-rendering=\"").append(textRenderValue)
                .append("\" ");
        this.sb.append(getClipPathRef());
        this.sb.append(">");
        this.sb.append(SVGUtils.escapeForXML(str)).append("</text>");
        this.sb.append("</g>");
    }

    /**
     * Draws a string of attributed characters at <code>(x, y)</code>.  The
     * call is delegated to
     * {@link #drawString(AttributedCharacterIterator, float, float)}.
     *
     * @param iterator  an iterator for the characters.
     * @param x  the x-coordinate.
     * @param y  the x-coordinate.
     */
    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        drawString(iterator, (float) x, (float) y);
    }

    /**
     * Draws a string of attributed characters at <code>(x, y)</code>.
     *
     * @param iterator  an iterator over the characters (<code>null</code> not
     *     permitted).
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     */
    @Override
    public void drawString(AttributedCharacterIterator iterator, float x,
            float y) {
        Set<Attribute> s = iterator.getAllAttributeKeys();
        if (!s.isEmpty()) {
            TextLayout layout = new TextLayout(iterator,
                    getFontRenderContext());
            layout.draw(this, x, y);
        } else {
            StringBuilder strb = new StringBuilder();
            iterator.first();
            for (int i = iterator.getBeginIndex(); i < iterator.getEndIndex();
                    i++) {
                strb.append(iterator.current());
                iterator.next();
            }
            drawString(strb.toString(), x, y);
        }
    }

    /**
     * Draws the specified glyph vector at the location <code>(x, y)</code>.
     *
     * @param g  the glyph vector (<code>null</code> not permitted).
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     */
    @Override
    public void drawGlyphVector(GlyphVector g, float x, float y) {
        fill(g.getOutline(x, y));
    }

    /**
     * Applies the translation <code>(tx, ty)</code>.  This call is delegated
     * to {@link #translate(double, double)}.
     *
     * @param tx  the x-translation.
     * @param ty  the y-translation.
     *
     * @see #translate(double, double)
     */
    @Override
    public void translate(int tx, int ty) {
        translate((double) tx, (double) ty);
    }

    /**
     * Applies the translation <code>(tx, ty)</code>.
     *
     * @param tx  the x-translation.
     * @param ty  the y-translation.
     */
    @Override
    public void translate(double tx, double ty) {
        AffineTransform t = getTransform();
        t.translate(tx, ty);
        setTransform(t);
    }

    /**
     * Applies a rotation (anti-clockwise) about <code>(0, 0)</code>.
     *
     * @param theta  the rotation angle (in radians).
     */
    @Override
    public void rotate(double theta) {
        AffineTransform t = getTransform();
        t.rotate(theta);
        setTransform(t);
    }

    /**
     * Applies a rotation (anti-clockwise) about <code>(x, y)</code>.
     *
     * @param theta  the rotation angle (in radians).
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     */
    @Override
    public void rotate(double theta, double x, double y) {
        translate(x, y);
        rotate(theta);
        translate(-x, -y);
    }

    /**
     * Applies a scale transformation.
     *
     * @param sx  the x-scaling factor.
     * @param sy  the y-scaling factor.
     */
    @Override
    public void scale(double sx, double sy) {
        AffineTransform t = getTransform();
        t.scale(sx, sy);
        setTransform(t);
    }

    /**
     * Applies a shear transformation. This is equivalent to the following
     * call to the <code>transform</code> method:
     * <br><br>
     * <ul><li>
     * <code>transform(AffineTransform.getShearInstance(shx, shy));</code>
     * </ul>
     *
     * @param shx  the x-shear factor.
     * @param shy  the y-shear factor.
     */
    @Override
    public void shear(double shx, double shy) {
        transform(AffineTransform.getShearInstance(shx, shy));
    }

    /**
     * Applies this transform to the existing transform by concatenating it.
     *
     * @param t  the transform (<code>null</code> not permitted).
     */
    @Override
    public void transform(AffineTransform t) {
        AffineTransform tx = getTransform();
        tx.concatenate(t);
        setTransform(tx);
    }

    /**
     * Returns a copy of the current transform.
     *
     * @return A copy of the current transform (never <code>null</code>).
     *
     * @see #setTransform(java.awt.geom.AffineTransform)
     */
    @Override
    public AffineTransform getTransform() {
        return (AffineTransform) this.transform.clone();
    }

    /**
     * Sets the transform.
     *
     * @param t  the new transform (<code>null</code> permitted, resets to the
     *     identity transform).
     *
     * @see #getTransform()
     */
    @Override
    public void setTransform(AffineTransform t) {
        if (t == null) {
            this.transform = new AffineTransform();
        } else {
            this.transform = new AffineTransform(t);
        }
        this.clipRef = null;
    }

    /**
     * Returns <code>true</code> if the rectangle (in device space) intersects
     * with the shape (the interior, if <code>onStroke</code> is false,
     * otherwise the stroked outline of the shape).
     *
     * @param rect  a rectangle (in device space).
     * @param s the shape.
     * @param onStroke  test the stroked outline only?
     *
     * @return A boolean.
     */
    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        Shape ts;
        if (onStroke) {
            ts = this.transform.createTransformedShape(
                    this.stroke.createStrokedShape(s));
        } else {
            ts = this.transform.createTransformedShape(s);
        }
        if (!rect.getBounds2D().intersects(ts.getBounds2D())) {
            return false;
        }
        Area a1 = new Area(rect);
        Area a2 = new Area(ts);
        a1.intersect(a2);
        return !a1.isEmpty();
    }

    /**
     * Does nothing in this <code>SVGGraphics2D</code> implementation.
     */
    @Override
    public void setPaintMode() {
        // do nothing
    }

    /**
     * Does nothing in this <code>SVGGraphics2D</code> implementation.
     *
     * @param c  ignored
     */
    @Override
    public void setXORMode(Color c) {
        // do nothing
    }

    /**
     * Returns the bounds of the user clipping region.
     *
     * @return The clip bounds (possibly <code>null</code>).
     *
     * @see #getClip()
     */
    @Override
    public Rectangle getClipBounds() {
        if (this.clip == null) {
            return null;
        }
        return getClip().getBounds();
    }

    /**
     * Returns the user clipping region.  The initial default value is
     * <code>null</code>.
     *
     * @return The user clipping region (possibly <code>null</code>).
     *
     * @see #setClip(java.awt.Shape)
     */
    @Override
    public Shape getClip() {
        if (this.clip == null) {
            return null;
        }
        AffineTransform inv;
        try {
            inv = this.transform.createInverse();
            return inv.createTransformedShape(this.clip);
        } catch (NoninvertibleTransformException ex) {
            return null;
        }
    }

    /**
     * Sets the user clipping region.
     * 
     * @param shape  the new user clipping region (<code>null</code> permitted).
     * 
     * @see #getClip()
     */
    @Override
    public void setClip(Shape shape) {
        // null is handled fine here...
        this.clip = this.transform.createTransformedShape(shape);
        this.clipRef = null;
    }
    
    /**
     * Registers the clip so that we can later write out all the clip 
     * definitions in the DEFS element.
     * 
     * @param clip  the clip (ignored if <code>null</code>) 
     */
    private String registerClip(Shape clip) {
        if (clip == null) {
            this.clipRef = null;
            return null;
        }
        // generate the path
        String pathStr = getSVGPathData(new Path2D.Double(clip));
        int index = this.clipPaths.indexOf(pathStr);
        if (index < 0) {
            this.clipPaths.add(pathStr);
            index = this.clipPaths.size() - 1;
        }
        return this.defsKeyPrefix + CLIP_KEY_PREFIX + index;
    }
    
    private String transformDP(double d) {
        if (this.transformFormat != null) {
            return transformFormat.format(d);            
        } else {
            return String.valueOf(d);
        }
    }
    
    private String geomDP(double d) {
        if (this.geometryFormat != null) {
            return geometryFormat.format(d);            
        } else {
            return String.valueOf(d);
        }
    }
    
    private String getSVGTransform(AffineTransform t) {
        StringBuilder b = new StringBuilder("matrix(");
        b.append(transformDP(t.getScaleX())).append(",");
        b.append(transformDP(t.getShearY())).append(",");
        b.append(transformDP(t.getShearX())).append(",");
        b.append(transformDP(t.getScaleY())).append(",");
        b.append(transformDP(t.getTranslateX())).append(",");
        b.append(transformDP(t.getTranslateY())).append(")");
        return b.toString();
    }

    /**
     * Clips to the intersection of the current clipping region and the
     * specified shape. 
     * 
     * According to the Oracle API specification, this method will accept a 
     * <code>null</code> argument, but there is an open bug report (since 2004) 
     * that suggests this is wrong:
     * <p>
     * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6206189">
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6206189</a>
     * 
     * @param s  the clip shape (<code>null</code> not permitted). 
     */
    @Override
    public void clip(Shape s) {
        if (s instanceof Line2D) {
            s = s.getBounds2D();
        }
        if (this.clip == null) {
            setClip(s);
            return;
        }
        Shape ts = this.transform.createTransformedShape(s);
        if (!ts.intersects(this.clip.getBounds2D())) {
            setClip(new Rectangle2D.Double());
        } else {
          Area a1 = new Area(ts);
          Area a2 = new Area(this.clip);
          a1.intersect(a2);
          this.clip = new Path2D.Double(a1);
        }
        this.clipRef = null;
    }

    /**
     * Clips to the intersection of the current clipping region and the 
     * specified rectangle.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     */
    @Override
    public void clipRect(int x, int y, int width, int height) {
        setRect(x, y, width, height);
        clip(this.rect);
    }

    /**
     * Sets the user clipping region to the specified rectangle.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     * 
     * @see #getClip() 
     */
    @Override
    public void setClip(int x, int y, int width, int height) {
        setRect(x, y, width, height);
        setClip(this.rect);
    }

    /**
     * Draws a line from <code>(x1, y1)</code> to <code>(x2, y2)</code> using 
     * the current <code>paint</code> and <code>stroke</code>.
     * 
     * @param x1  the x-coordinate of the start point.
     * @param y1  the y-coordinate of the start point.
     * @param x2  the x-coordinate of the end point.
     * @param y2  the x-coordinate of the end point.
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        if (this.line == null) {
            this.line = new Line2D.Double(x1, y1, x2, y2);
        } else {
            this.line.setLine(x1, y1, x2, y2);
        }
        draw(this.line);
    }

    /**
     * Fills the specified rectangle with the current <code>paint</code>.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the rectangle width.
     * @param height  the rectangle height.
     */
    @Override
    public void fillRect(int x, int y, int width, int height) {
        setRect(x, y, width, height);
        fill(this.rect);
    }

    /**
     * Clears the specified rectangle by filling it with the current 
     * background color.  If the background color is <code>null</code>, this
     * method will do nothing.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     * 
     * @see #getBackground() 
     */
    @Override
    public void clearRect(int x, int y, int width, int height) {
        if (getBackground() == null) {
            return;  // we can't do anything
        }
        Paint saved = getPaint();
        setPaint(getBackground());
        fillRect(x, y, width, height);
        setPaint(saved);
    }
    
    /**
     * Draws a rectangle with rounded corners using the current 
     * <code>paint</code> and <code>stroke</code>.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     * @param arcWidth  the arc-width.
     * @param arcHeight  the arc-height.
     * 
     * @see #fillRoundRect(int, int, int, int, int, int) 
     */
    @Override
    public void drawRoundRect(int x, int y, int width, int height, 
            int arcWidth, int arcHeight) {
        setRoundRect(x, y, width, height, arcWidth, arcHeight);
        draw(this.roundRect);
    }

    /**
     * Fills a rectangle with rounded corners using the current 
     * <code>paint</code>.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     * @param arcWidth  the arc-width.
     * @param arcHeight  the arc-height.
     * 
     * @see #drawRoundRect(int, int, int, int, int, int) 
     */
    @Override
    public void fillRoundRect(int x, int y, int width, int height, 
            int arcWidth, int arcHeight) {
        setRoundRect(x, y, width, height, arcWidth, arcHeight);
        fill(this.roundRect);
    }

    /**
     * Draws an oval framed by the rectangle <code>(x, y, width, height)</code>
     * using the current <code>paint</code> and <code>stroke</code>.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     * 
     * @see #fillOval(int, int, int, int) 
     */
    @Override
    public void drawOval(int x, int y, int width, int height) {
        setOval(x, y, width, height);
        draw(this.oval);
    }

    /**
     * Fills an oval framed by the rectangle <code>(x, y, width, height)</code>.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     * 
     * @see #drawOval(int, int, int, int) 
     */
    @Override
    public void fillOval(int x, int y, int width, int height) {
        setOval(x, y, width, height);
        fill(this.oval);
    }

    /**
     * Draws an arc contained within the rectangle 
     * <code>(x, y, width, height)</code>, starting at <code>startAngle</code>
     * and continuing through <code>arcAngle</code> degrees using 
     * the current <code>paint</code> and <code>stroke</code>.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     * @param startAngle  the start angle in degrees, 0 = 3 o'clock.
     * @param arcAngle  the angle (anticlockwise) in degrees.
     * 
     * @see #fillArc(int, int, int, int, int, int) 
     */
    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, 
            int arcAngle) {
        setArc(x, y, width, height, startAngle, arcAngle);
        draw(this.arc);
    }

    /**
     * Fills an arc contained within the rectangle 
     * <code>(x, y, width, height)</code>, starting at <code>startAngle</code>
     * and continuing through <code>arcAngle</code> degrees, using 
     * the current <code>paint</code>
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     * @param startAngle  the start angle in degrees, 0 = 3 o'clock.
     * @param arcAngle  the angle (anticlockwise) in degrees.
     * 
     * @see #drawArc(int, int, int, int, int, int) 
     */
    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, 
            int arcAngle) {
        setArc(x, y, width, height, startAngle, arcAngle);
        fill(this.arc);
    }

    /**
     * Draws the specified multi-segment line using the current 
     * <code>paint</code> and <code>stroke</code>.
     * 
     * @param xPoints  the x-points.
     * @param yPoints  the y-points.
     * @param nPoints  the number of points to use for the polyline.
     */
    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        GeneralPath p = GraphicsUtils.createPolygon(xPoints, yPoints, nPoints,
                false);
        draw(p);
    }

    /**
     * Draws the specified polygon using the current <code>paint</code> and 
     * <code>stroke</code>.
     * 
     * @param xPoints  the x-points.
     * @param yPoints  the y-points.
     * @param nPoints  the number of points to use for the polygon.
     * 
     * @see #fillPolygon(int[], int[], int)      */
    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        GeneralPath p = GraphicsUtils.createPolygon(xPoints, yPoints, nPoints,
                true);
        draw(p);
    }

    /**
     * Fills the specified polygon using the current <code>paint</code>.
     * 
     * @param xPoints  the x-points.
     * @param yPoints  the y-points.
     * @param nPoints  the number of points to use for the polygon.
     * 
     * @see #drawPolygon(int[], int[], int) 
     */
    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        GeneralPath p = GraphicsUtils.createPolygon(xPoints, yPoints, nPoints,
                true);
        fill(p);
    }

    /**
     * Returns the bytes representing a PNG format image.
     * 
     * @param img  the image to encode.
     * 
     * @return The bytes representing a PNG format image. 
     */
    private byte[] getPNGBytes(Image img) {
        RenderedImage ri;
        if (img instanceof RenderedImage) {
            ri = (RenderedImage) img;
        } else {
            BufferedImage bi = new BufferedImage(img.getWidth(null),
                    img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bi.createGraphics();
            g2.drawImage(img, 0, 0, null);
            ri = bi;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(ri, "png", baos);
        } catch (IOException ex) {
            Logger.getLogger(SVGGraphics2D.class.getName()).log(Level.SEVERE,
                    "IOException while writing PNG data.", ex);
        }
        return baos.toByteArray();
    }  
    
    /**
     * Draws an image at the location <code>(x, y)</code>.  Note that the 
     * <code>observer</code> is ignored.
     * 
     * @param img  the image (<code>null</code> not permitted).
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param observer  ignored.
     * 
     * @return {@code true} if the image is drawn. 
     */
    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        int w = img.getWidth(observer);
        if (w < 0) {
            return false;
        }
        int h = img.getHeight(observer);
        if (h < 0) {
            return false;
        }
        return drawImage(img, x, y, w, h, observer);
    }

    /**
     * Draws the image into the rectangle defined by <code>(x, y, w, h)</code>.  
     * Note that the <code>observer</code> is ignored (it is not useful in this
     * context).
     * 
     * @param img  the image.
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param w  the width.
     * @param h  the height.
     * @param observer  ignored.
     * 
     * @return {@code true} if the image is drawn. 
     */
    @Override
    public boolean drawImage(Image img, int x, int y, int w, int h,
            ImageObserver observer) {

        // the rendering hints control whether the image is embedded or
        // referenced...
        Object hint = getRenderingHint(SVGHints.KEY_IMAGE_HANDLING);
        if (SVGHints.VALUE_IMAGE_HANDLING_EMBED.equals(hint)) {
            this.sb.append("<image ");
            appendOptionalElementIDFromHint(this.sb);
            this.sb.append("preserveAspectRatio=\"none\" ");
            this.sb.append("xlink:href=\"data:image/png;base64,");
            this.sb.append(DatatypeConverter.printBase64Binary(getPNGBytes(
                    img)));
            this.sb.append("\" ");
            this.sb.append(getClipPathRef()).append(" ");
            this.sb.append("transform=\"").append(getSVGTransform(
                    this.transform)).append("\" ");            
            this.sb.append("x=\"").append(geomDP(x))
                    .append("\" y=\"").append(geomDP(y))
                    .append("\" ");
            this.sb.append("width=\"").append(geomDP(w)).append("\" height=\"")
                    .append(geomDP(h)).append("\"/>\n");
            return true;
        } else { // here for SVGHints.VALUE_IMAGE_HANDLING_REFERENCE
            int count = this.imageElements.size();
            String href = (String) this.hints.get(SVGHints.KEY_IMAGE_HREF);
            if (href == null) {
                href = this.filePrefix + count + this.fileSuffix;
            } else {
                // KEY_IMAGE_HREF value is for a single use...
                this.hints.put(SVGHints.KEY_IMAGE_HREF, null);
            }
            ImageElement imageElement = new ImageElement(href, img);
            this.imageElements.add(imageElement);
            // write an SVG element for the img
            this.sb.append("<image ");
            appendOptionalElementIDFromHint(this.sb);
            this.sb.append("xlink:href=\"");
            this.sb.append(href).append("\" ");
            this.sb.append(getClipPathRef()).append(" ");
            this.sb.append("transform=\"").append(getSVGTransform(
                    this.transform)).append("\" ");
            this.sb.append("x=\"").append(geomDP(x))
                    .append("\" y=\"").append(geomDP(y))
                    .append("\" ");
            this.sb.append("width=\"").append(geomDP(w)).append("\" height=\"")
                    .append(geomDP(h)).append("\"/>\n");
            return true;
        }
    }

    /**
     * Draws an image at the location <code>(x, y)</code>.  Note that the 
     * <code>observer</code> is ignored.
     * 
     * @param img  the image (<code>null</code> not permitted).
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param bgcolor  the background color (<code>null</code> permitted).
     * @param observer  ignored.
     * 
     * @return {@code true} if the image is drawn. 
     */
    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor,
            ImageObserver observer) {
        int w = img.getWidth(null);
        if (w < 0) {
            return false;
        }
        int h = img.getHeight(null);
        if (h < 0) {
            return false;
        }
        return drawImage(img, x, y, w, h, bgcolor, observer);
    }

    /**
     * Draws an image to the rectangle <code>(x, y, w, h)</code> (scaling it if
     * required), first filling the background with the specified color.  Note 
     * that the <code>observer</code> is ignored.
     * 
     * @param img  the image.
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param w  the width.
     * @param h  the height.
     * @param bgcolor  the background color (<code>null</code> permitted).
     * @param observer  ignored.
     * 
     * @return {@code true} if the image is drawn.      
     */
    @Override
    public boolean drawImage(Image img, int x, int y, int w, int h,
            Color bgcolor, ImageObserver observer) {
        Paint saved = getPaint();
        setPaint(bgcolor);
        fillRect(x, y, w, h);
        setPaint(saved);
        return drawImage(img, x, y, w, h, observer);
    }

    /**
     * Draws part of an image (defined by the source rectangle 
     * <code>(sx1, sy1, sx2, sy2)</code>) into the destination rectangle
     * <code>(dx1, dy1, dx2, dy2)</code>.  Note that the <code>observer</code> 
     * is ignored.
     * 
     * @param img  the image.
     * @param dx1  the x-coordinate for the top left of the destination.
     * @param dy1  the y-coordinate for the top left of the destination.
     * @param dx2  the x-coordinate for the bottom right of the destination.
     * @param dy2  the y-coordinate for the bottom right of the destination.
     * @param sx1 the x-coordinate for the top left of the source.
     * @param sy1 the y-coordinate for the top left of the source.
     * @param sx2 the x-coordinate for the bottom right of the source.
     * @param sy2 the y-coordinate for the bottom right of the source.
     * 
     * @return {@code true} if the image is drawn. 
     */
    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
            int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        int w = dx2 - dx1;
        int h = dy2 - dy1;
        BufferedImage img2 = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img2.createGraphics();
        g2.drawImage(img, 0, 0, w, h, sx1, sy1, sx2, sy2, null);
        return drawImage(img2, dx1, dx2, null);
    }

    /**
     * Draws part of an image (defined by the source rectangle 
     * <code>(sx1, sy1, sx2, sy2)</code>) into the destination rectangle
     * <code>(dx1, dy1, dx2, dy2)</code>.  The destination rectangle is first
     * cleared by filling it with the specified <code>bgcolor</code>. Note that
     * the <code>observer</code> is ignored. 
     * 
     * @param img  the image.
     * @param dx1  the x-coordinate for the top left of the destination.
     * @param dy1  the y-coordinate for the top left of the destination.
     * @param dx2  the x-coordinate for the bottom right of the destination.
     * @param dy2  the y-coordinate for the bottom right of the destination.
     * @param sx1 the x-coordinate for the top left of the source.
     * @param sy1 the y-coordinate for the top left of the source.
     * @param sx2 the x-coordinate for the bottom right of the source.
     * @param sy2 the y-coordinate for the bottom right of the source.
     * @param bgcolor  the background color (<code>null</code> permitted).
     * @param observer  ignored.
     * 
     * @return {@code true} if the image is drawn. 
     */
    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
            int sx1, int sy1, int sx2, int sy2, Color bgcolor,
            ImageObserver observer) {
        Paint saved = getPaint();
        setPaint(bgcolor);
        fillRect(dx1, dy1, dx2 - dx1, dy2 - dy1);
        setPaint(saved);
        return drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    /**
     * Draws the rendered image.
     * 
     * @param img  the image.
     * @param xform  the transform.
     */
    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        BufferedImage bi = GraphicsUtils.convertRenderedImage(img);
        drawImage(bi, xform, null);
    }

    /**
     * Draws the renderable image.
     * 
     * @param img  the renderable image.
     * @param xform  the transform.
     */
    @Override
    public void drawRenderableImage(RenderableImage img,
            AffineTransform xform) {
        RenderedImage ri = img.createDefaultRendering();
        drawRenderedImage(ri, xform);
    }

    /**
     * Draws an image with the specified transform. Note that the 
     * <code>observer</code> is ignored.     
     * 
     * @param img  the image.
     * @param xform  the transform.
     * @param obs  the image observer (ignored).
     * 
     * @return {@code true} if the image is drawn. 
     */
    @Override
    public boolean drawImage(Image img, AffineTransform xform,
            ImageObserver obs) {
        AffineTransform savedTransform = getTransform();
        transform(xform);
        boolean result = drawImage(img, 0, 0, obs);
        setTransform(savedTransform);
        return result;
    }

    /**
     * Draws the image resulting from applying the <code>BufferedImageOp</code>
     * to the specified image at the location <code>(x, y)</code>.
     * 
     * @param img  the image.
     * @param op  the operation.
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     */
    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        BufferedImage imageToDraw = op.filter(img, null);
        drawImage(imageToDraw, new AffineTransform(1f, 0f, 0f, 1f, x, y), null);
    }

    /**
     * This method does nothing.  The operation assumes that the output is in 
     * bitmap form, which is not the case for SVG, so we silently ignore
     * this method call.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width of the area.
     * @param height  the height of the area.
     * @param dx  the delta x.
     * @param dy  the delta y.
     */
    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        // do nothing, this operation is silently ignored.
    }

    /**
     * This method does nothing, there are no resources to dispose.
     */
    @Override
    public void dispose() {
        // nothing to do
    }

    /**
     * Returns the SVG element that has been generated by calls to this 
     * <code>Graphics2D</code> implementation.
     * 
     * @return The SVG element.
     */
    public String getSVGElement() {
        return getSVGElement(null);
    }
    
    /**
     * Returns the SVG element that has been generated by calls to this
     * <code>Graphics2D</code> implementation, giving it the specified 
     * <code>id</code>.  If <code>id</code> is <code>null</code>, the
     * element will have no <code>id</code> attribute.
     * 
     * @param id  the element id (<code>null</code> permitted).
     * 
     * @return A string containing the SVG element. 
     * 
     * @since 1.8
     */
    public String getSVGElement(String id) {
        StringBuilder svg = new StringBuilder("<svg ");
        if (id != null) {
            svg.append("id=\"").append(id).append("\" ");
        }
        svg.append("xmlns=\"http://www.w3.org/2000/svg\" ")
           .append("xmlns:xlink=\"http://www.w3.org/1999/xlink\" ")
           .append("xmlns:jfreesvg=\"http://www.jfree.org/jfreesvg/svg\" ")
           .append("width=\"").append(width)
           .append("\" height=\"").append(height).append("\">\n");
        StringBuilder defs = new StringBuilder("<defs>");
        for (GradientPaintKey key : this.gradientPaints.keySet()) {
            defs.append(getLinearGradientElement(this.gradientPaints.get(key), 
                    key.getPaint()));
            defs.append("\n");
        }
        for (LinearGradientPaintKey key : this.linearGradientPaints.keySet()) {
            defs.append(getLinearGradientElement(
                    this.linearGradientPaints.get(key), key.getPaint()));
            defs.append("\n");            
        }
        for (RadialGradientPaintKey key : this.radialGradientPaints.keySet()) {
            defs.append(getRadialGradientElement(
                    this.radialGradientPaints.get(key), key.getPaint()));
            defs.append("\n");
        }
        for (int i = 0; i < this.clipPaths.size(); i++) {
            StringBuilder b = new StringBuilder("<clipPath id=\"")
                    .append(this.defsKeyPrefix).append(CLIP_KEY_PREFIX).append(i)
                    .append("\">");
            b.append("<path ").append(this.clipPaths.get(i)).append("/>");
            b.append("</clipPath>").append("\n");
            defs.append(b.toString());
        }
        defs.append("</defs>\n");
        svg.append(defs);
        svg.append(this.sb);
        svg.append("</svg>");        
        return svg.toString();
    }
    
    /**
     * Returns an SVG document.
     * 
     * @return An SVG document.
     */
    public String getSVGDocument() {
        StringBuilder b = new StringBuilder();
        b.append("<?xml version=\"1.0\"?>\n");
        b.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" ");
        b.append("\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n");
        b.append(getSVGElement());
        return b.append("\n").toString();
    }
    
    /**
     * Returns the list of image elements that have been referenced in the 
     * SVG output but not embedded.  If the image files don't already exist,
     * you can use this list as the basis for creating the image files.
     * 
     * @return The list of image elements.
     * 
     * @see SVGHints#KEY_IMAGE_HANDLING
     */
    public List<ImageElement> getSVGImages() {
        return this.imageElements;
    }
    
    /**
     * Returns a new set containing the element IDs that have been used in
     * output so far.
     * 
     * @return The element IDs.
     * 
     * @since 1.5
     */
    public Set<String> getElementIDs() {
        return new HashSet<String>(this.elementIDs);
    }
    
    /**
     * Returns an element to represent a linear gradient.  All the linear
     * gradients that are used get written to the DEFS element in the SVG.
     * 
     * @param id  the reference id.
     * @param paint  the gradient.
     * 
     * @return The SVG element.
     */
    private String getLinearGradientElement(String id, GradientPaint paint) {
        StringBuilder b = new StringBuilder("<linearGradient id=\"").append(id)
                .append("\" ");
        Point2D p1 = paint.getPoint1();
        Point2D p2 = paint.getPoint2();
        b.append("x1=\"").append(geomDP(p1.getX())).append("\" ");
        b.append("y1=\"").append(geomDP(p1.getY())).append("\" ");
        b.append("x2=\"").append(geomDP(p2.getX())).append("\" ");
        b.append("y2=\"").append(geomDP(p2.getY())).append("\" ");
        b.append("gradientUnits=\"userSpaceOnUse\">");
        b.append("<stop offset=\"0%\" style=\"stop-color: ").append(
                rgbColorStr(paint.getColor1())).append(";\"/>");
        b.append("<stop offset=\"100%\" style=\"stop-color: ").append(
                rgbColorStr(paint.getColor2())).append(";\"/>");
        return b.append("</linearGradient>").toString();
    }
    
    /**
     * Returns an element to represent a linear gradient.  All the linear
     * gradients that are used get written to the DEFS element in the SVG.
     * 
     * @param id  the reference id.
     * @param paint  the gradient.
     * 
     * @return The SVG element.
     */
    private String getLinearGradientElement(String id, LinearGradientPaint paint) {
        StringBuilder b = new StringBuilder("<linearGradient id=\"").append(id)
                .append("\" ");
        Point2D p1 = paint.getStartPoint();
        Point2D p2 = paint.getEndPoint();
        b.append("x1=\"").append(geomDP(p1.getX())).append("\" ");
        b.append("y1=\"").append(geomDP(p1.getY())).append("\" ");
        b.append("x2=\"").append(geomDP(p2.getX())).append("\" ");
        b.append("y2=\"").append(geomDP(p2.getY())).append("\" ");
        if (!paint.getCycleMethod().equals(CycleMethod.NO_CYCLE)) {
            String sm = paint.getCycleMethod().equals(CycleMethod.REFLECT)
                    ? "reflect" : "repeat";
            b.append("spreadMethod=\"").append(sm).append("\" ");
        }
        b.append("gradientUnits=\"userSpaceOnUse\">");
        for (int i = 0; i < paint.getFractions().length; i++) {
            Color c = paint.getColors()[i];
            float fraction = paint.getFractions()[i];
            b.append("<stop offset=\"").append(geomDP(fraction * 100))
                    .append("%\" style=\"stop-color: ")
                    .append(rgbColorStr(c)).append(";\"/>");
        }
        return b.append("</linearGradient>").toString();
    }
    
    /**
     * Returns an element to represent a radial gradient.  All the radial
     * gradients that are used get written to the DEFS element in the SVG.
     * 
     * @param id  the reference id.
     * @param rgp  the radial gradient.
     * 
     * @return The SVG element. 
     */
    private String getRadialGradientElement(String id, RadialGradientPaint rgp) {
        StringBuilder b = new StringBuilder("<radialGradient id=\"").append(id)
                .append("\" gradientUnits=\"userSpaceOnUse\" ");
        Point2D center = rgp.getCenterPoint();
        Point2D focus = rgp.getFocusPoint();
        float radius = rgp.getRadius();
        b.append("cx=\"").append(geomDP(center.getX())).append("\" ");
        b.append("cy=\"").append(geomDP(center.getY())).append("\" ");
        b.append("r=\"").append(geomDP(radius)).append("\" ");
        b.append("fx=\"").append(geomDP(focus.getX())).append("\" ");
        b.append("fy=\"").append(geomDP(focus.getY())).append("\">");
        
        Color[] colors = rgp.getColors();
        float[] fractions = rgp.getFractions();
        for (int i = 0; i < colors.length; i++) {
            Color c = colors[i];
            float f = fractions[i];
            b.append("<stop offset=\"").append(geomDP(f * 100)).append("%\" ");
            b.append("stop-color=\"").append(rgbColorStr(c)).append("\"/>");
        }
        return b.append("</radialGradient>").toString();
    }

    /**
     * Returns a clip path reference for the current user clip.  This is 
     * written out on all SVG elements that draw or fill shapes or text.
     * 
     * @return A clip path reference. 
     */
    private String getClipPathRef() {
        if (this.clip == null) {
            return "";
        }
        if (this.clipRef == null) {
            this.clipRef = registerClip(getClip());
        }
        StringBuilder b = new StringBuilder();
        b.append("clip-path=\"url(#").append(this.clipRef).append(")\"");
        return b.toString();
    }
    
    /**
     * Sets the attributes of the reusable {@link Rectangle2D} object that is
     * used by the {@link SVGGraphics2D#drawRect(int, int, int, int)} and 
     * {@link SVGGraphics2D#fillRect(int, int, int, int)} methods.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     */
    private void setRect(int x, int y, int width, int height) {
        if (this.rect == null) {
            this.rect = new Rectangle2D.Double(x, y, width, height);
        } else {
            this.rect.setRect(x, y, width, height);
        }
    }
    
    /**
     * Sets the attributes of the reusable {@link RoundRectangle2D} object that
     * is used by the {@link #drawRoundRect(int, int, int, int, int, int)} and
     * {@link #fillRoundRect(int, int, int, int, int, int)} methods.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     * @param arcWidth  the arc width.
     * @param arcHeight  the arc height.
     */
    private void setRoundRect(int x, int y, int width, int height, int arcWidth, 
            int arcHeight) {
        if (this.roundRect == null) {
            this.roundRect = new RoundRectangle2D.Double(x, y, width, height,
                    arcWidth, arcHeight);
        } else {
            this.roundRect.setRoundRect(x, y, width, height, 
                    arcWidth, arcHeight);
        }        
    }

    /**
     * Sets the attributes of the reusable {@link Arc2D} object that is used by
     * {@link #drawArc(int, int, int, int, int, int)} and 
     * {@link #fillArc(int, int, int, int, int, int)} methods.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     * @param startAngle  the start angle in degrees, 0 = 3 o'clock.
     * @param arcAngle  the angle (anticlockwise) in degrees.
     */
    private void setArc(int x, int y, int width, int height, int startAngle, 
            int arcAngle) {
        if (this.arc == null) {
            this.arc = new Arc2D.Double(x, y, width, height, startAngle,
                    arcAngle, Arc2D.OPEN);
        } else {
            this.arc.setArc(x, y, width, height, startAngle, arcAngle, 
                    Arc2D.OPEN);
        }        
    }
    
    /**
     * Sets the attributes of the reusable {@link Ellipse2D} object that is
     * used by the {@link #drawOval(int, int, int, int)} and
     * {@link #fillOval(int, int, int, int)} methods.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param width  the width.
     * @param height  the height.
     */
    private void setOval(int x, int y, int width, int height) {
        if (this.oval == null) {
            this.oval = new Ellipse2D.Double(x, y, width, height);
        } else {
            this.oval.setFrame(x, y, width, height);
        }
    }

}
