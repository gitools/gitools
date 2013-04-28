/*
 * #%L
 * gitools-ui-platform
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
package org.gitools.ui.platform.panel;

import org.jetbrains.annotations.NotNull;
import org.lobobrowser.html.FormInput;
import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.w3c.dom.html2.HTMLElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Html4Panel extends JPanel {

    public static class LinkVetoException extends Exception {
        public LinkVetoException() {
        }
    }

    private class LocalHtmlRendererContext extends SimpleHtmlRendererContext {

        public LocalHtmlRendererContext(HtmlPanel panel, UserAgentContext userAgentContext) {
            super(panel, userAgentContext);
        }

        @Override
        public void onMouseOver(@NotNull HTMLElement element, MouseEvent event) {
            super.onMouseOver(element, event);

            Cursor cursor = null;
            if ("a".equalsIgnoreCase(element.getTagName())) {
                cursor = new Cursor(Cursor.HAND_CURSOR);
            } else {
                cursor = new Cursor(Cursor.DEFAULT_CURSOR);
            }

            setCursor(cursor);
        }

        @Override
        public void onMouseOut(HTMLElement element, MouseEvent event) {
            super.onMouseOut(element, event);
            Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(cursor);
        }

        @Override
        public boolean onMouseClick(@NotNull HTMLElement element, MouseEvent event) {

            if ("a".equalsIgnoreCase(element.getTagName())) {
                try {
                    Html4Panel.this.linkClicked(element);
                    return false;
                } catch (LinkVetoException ex) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs) {
            try {
                Html4Panel.this.submitForm(method, action, target, enctype, formInputs);
                super.submitForm(method, action, target, enctype, formInputs);
            } catch (LinkVetoException ex) {
            }
        }

    }

    HtmlPanel panel;
    SimpleHtmlRendererContext rcontext;

    Html4Panel() {
        createComponents();
    }

    private void createComponents() {
        panel = new HtmlPanel();
        rcontext = new LocalHtmlRendererContext(panel, new SimpleUserAgentContext());

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    void linkClicked(@NotNull HTMLElement linkNode) throws LinkVetoException {
        String rel = linkNode.getAttribute("rel");
        String href = linkNode.getAttribute("href");
        String target = linkNode.getAttribute("target");
        if (rel != null && rel.equalsIgnoreCase("action")) {
            String name = href;
            Map<String, String> params = new HashMap<String, String>();

            int pos = href.indexOf('?');
            if (pos >= 0) {
                name = href.substring(0, pos);
                String p1 = pos < href.length() ? href.substring(pos + 1) : "";
                if (!p1.isEmpty()) {
                    String[] p2 = p1.split("\\&");
                    for (String p3 : p2) {
                        pos = p3.indexOf('=');
                        if (pos > 0) {
                            String id = p3.substring(0, pos);
                            String value = pos < p3.length() ? p3.substring(pos + 1) : "";
                            params.put(id, value);
                        }
                    }
                }
            }

            performUrlAction(name, params);
            throw new LinkVetoException();
        } else if (target != null && target.equalsIgnoreCase("_external")) {
            try {
                URI uri = new URI(href);
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(uri);
                } else {
                    JOptionPane.showInputDialog(getRootPane(), "Copy this URL into your web browser", uri.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new LinkVetoException();
        }
    }

    protected void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs) throws LinkVetoException {
        /*System.out.println("method=" + method + ", action=" + action + ", target=" + target + ", enctype="+ enctype);
          if (formInputs != null)
              for (FormInput fi : formInputs)
                  System.out.println("name=" + fi.getName() + ", value=" + fi.getTextValue() + ", file=" + fi.getFileValue());*/
    }

    protected void performUrlAction(String name, Map<String, String> params) {
        // do nothing
    }

    public void navigate(URL url) throws Exception {
        rcontext.navigate(url, "_this");
    }

    public HtmlRendererContext getHtmlRenderContext() {
        return rcontext;
    }
}
