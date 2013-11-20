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
package org.gitools.ui.platform.editor;

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

public class HtmlEditor extends AbstractEditor {

    public static class LinkVetoException extends Exception {
        public LinkVetoException() {
        }
    }

    private class LocalHtmlRendererContext extends SimpleHtmlRendererContext {

        public LocalHtmlRendererContext(HtmlPanel panel, UserAgentContext userAgentContext) {
            super(panel, userAgentContext);
        }

        @Override
        public void onMouseOver(HTMLElement element, MouseEvent event) {
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
        public void linkClicked(HTMLElement linkNode, URL url, String target) {
            try {
                HtmlEditor.this.linkClicked(linkNode, url, target);
                super.linkClicked(linkNode, url, target);
            } catch (LinkVetoException ex) {
            }
        }

        @Override
        public void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs) {
            try {
                HtmlEditor.this.submitForm(method, action, target, enctype, formInputs);
                super.submitForm(method, action, target, enctype, formInputs);
            } catch (LinkVetoException ex) {
            }
        }

    }

    private String title;
    private HtmlPanel panel;
    private SimpleHtmlRendererContext rcontext;

    protected HtmlEditor(String title) {
        this.title = title;

        createComponents();
    }

    @Override
    public String getName() {
        return title;
    }

    /**
     * @noinspection UnusedDeclaration
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public Object getModel() {
        return null;
    }

    private void createComponents() {
        panel = new HtmlPanel();
        panel.setBackground(Color.WHITE);
        rcontext = new LocalHtmlRendererContext(panel, new SimpleUserAgentContext());

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    void linkClicked(HTMLElement linkNode, URL url, String target) throws LinkVetoException {
        String rel = linkNode.getAttribute("rel");
        String href = linkNode.getAttribute("href");
        if ("action".equalsIgnoreCase(rel)) {
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
        } else if (target.equalsIgnoreCase("_external")) {
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

    void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs) throws LinkVetoException {
        /*System.out.println("method=" + method + ", action=" + action + ", target=" + target + ", enctype="+ enctype);
        if (formInputs != null)
			for (FormInput fi : formInputs)
				System.out.println("name=" + fi.getName() + ", value=" + fi.getTextValue() + ", file=" + fi.getFileValue());*/
    }

    protected void performUrlAction(String name, Map<String, String> params) {
        // do nothing
    }

    protected void navigate(URL url) throws Exception {
        rcontext.navigate(url, "_this");
    }

    public HtmlRendererContext getHtmlRenderContext() {
        return rcontext;
    }
}
