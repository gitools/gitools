/*
 * #%L
 * org.gitools.ui.platform
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
package org.gitools.ui.platform.progress;

import com.alee.managers.glasspane.WebGlassPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public abstract class GitoolsGlassPane extends JComponent implements MouseListener {
    protected Thread animation = null;
    protected boolean started = false;
    protected boolean stopped = false;
    protected int alphaLevel = 0;
    protected int rampDelay = 300;
    protected int barsCount = 14;
    protected float fps = 15.0f;
    protected RenderingHints hints = null;
    protected String text = "";
    protected float shield = 0.85f;
    private RootPaneContainer parent;
    private static GitoolsGlassPane lastSetVisible;

    Component oldGlass;

    public GitoolsGlassPane(Window parent) {
        this.parent = (RootPaneContainer) parent;
        assignOldGlass();
        this.parent.setGlassPane(this);
        this.hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        this.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                escapePressed();
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    protected abstract void escapePressed();


    protected void assignOldGlass() {
        // Make sure that old glass is not a Gitools glass pane
        Component oldGlass = this.parent.getGlassPane();
        while (oldGlass instanceof GitoolsGlassPane) {
            oldGlass = ((GitoolsGlassPane) oldGlass).getOldGlass();
        }
        this.oldGlass = oldGlass;
    }

    private void revertToOldGlass(RootPaneContainer parent, Component component) {
        if (!oldGlass.equals(parent.getGlassPane())) {
            parent.setGlassPane(component);
            if (component instanceof WebGlassPane) {
                //Enable tooltips glasspane
                parent.getGlassPane().setVisible(true);
            }
        }
    }

    public Container getContentPane() {
        return this;
    }

    @Override
    public void setVisible(boolean aFlag) {


        if (this.isVisible() == aFlag) {
            if (aFlag == false && stopped) {
                return;
            } else if (aFlag && started) {
                return;
            }
        }

        if (aFlag) {
            if (lastSetVisible == null || !lastSetVisible.equals(this)) {
                lastSetVisible = this;
            }
            start();
        } else {
            if (stopped) {
                //Fade out animation performed
                doHideAfterAnimation();
            } else {
                //Perform fade out before setting unvisible
                stop();
            }
        }
    }

    public void doHideAfterAnimation() {
        if (!this.equals(lastSetVisible)) {
            return;
        }
        super.setVisible(false);
        revertToOldGlass(parent, oldGlass);
    }

    public void start() {
        addMouseListener(this);
        animation = new Thread(new Animator(true));
        animation.start();
    }

    public void stop() {
        if (animation != null) {
            animation.interrupt();
            animation = null;
            animation = new Thread(new Animator(false));
            animation.start();
        }
    }

    public void paintComponent(Graphics g) {
        if (started) {
            int width = getWidth();
            int height = getHeight();

            double maxY = 0.0;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(hints);

            g2.setColor(new Color(255, 255, 255, (int) (alphaLevel * shield)));
            g2.fillRect(0, 0, getWidth(), getHeight());

            if (text != null && text.length() > 0) {
                FontRenderContext context = g2.getFontRenderContext();
                TextLayout layout = new TextLayout(text, getFont(), context);
                Rectangle2D bounds = layout.getBounds();
                g2.setColor(getForeground());
                layout.draw(g2, (float) (width - bounds.getWidth()) / 2,
                        (float) (maxY + layout.getLeading() + 2 * layout.getAscent()));
            }
        }
    }

    public Component getOldGlass() {
        return oldGlass;
    }

    protected class Animator implements Runnable {
        private boolean rampUp = true;

        protected Animator(boolean rampUp) {
            this.rampUp = rampUp;
        }

        public void run() {
            Point2D.Double center = new Point2D.Double((double) getWidth() / 2, (double) getHeight() / 2);
            double fixedIncrement = 2.0 * Math.PI / ((double) barsCount);
            AffineTransform toCircle = AffineTransform.getRotateInstance(fixedIncrement, center.getX(), center.getY());

            long start = System.currentTimeMillis();
            if (rampDelay == 0)
                alphaLevel = rampUp ? 255 : 0;

            started = true;
            boolean inRamp = rampUp;

            while (!Thread.interrupted()) {

                repaint();

                if (rampUp) {
                    if (alphaLevel < 255) {
                        alphaLevel = (int) (255 * (System.currentTimeMillis() - start) / rampDelay);
                        if (alphaLevel >= 255) {
                            alphaLevel = 255;
                            inRamp = false;
                        }
                    }
                } else if (alphaLevel > 0) {
                    alphaLevel = (int) (255 - (255 * (System.currentTimeMillis() - start) / rampDelay));
                    if (alphaLevel <= 0) {
                        alphaLevel = 0;
                        break;
                    }
                }

                try {
                    Thread.sleep(inRamp ? 10 : (int) (1000 / fps));
                } catch (InterruptedException ie) {
                    break;
                }
                Thread.yield();
            }

            if (!rampUp) {
                started = false;
                stopped = true;
                repaint();

                removeMouseListener(GitoolsGlassPane.this);
                setVisible(false);
            }
        }
    }

    public float getShield() {
        return shield;
    }

    public void setShield(float shield) {
        this.shield = shield;
    }
}
