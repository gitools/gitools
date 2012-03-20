/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.platform.editor;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.lobobrowser.html.FormInput;
import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.w3c.dom.html2.HTMLElement;

//FIXME Use Html4Panel !!!
public class Html4Editor extends AbstractEditor {

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
			if ("a".equalsIgnoreCase(element.getTagName()))
				cursor = new Cursor(Cursor.HAND_CURSOR);
			else
				cursor = new Cursor(Cursor.DEFAULT_CURSOR);

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
				Html4Editor.this.linkClicked(linkNode, url, target);
				super.linkClicked(linkNode, url, target);
			}
			catch (LinkVetoException ex) {
			}
		}

		@Override
		public void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs) {
			try {
				//System.out.println("method=" + method + ", action=" + action + ", target=" + target + ", enctype="+ enctype);
				Html4Editor.this.submitForm(method, action, target, enctype, formInputs);
				super.submitForm(method, action, target, enctype, formInputs);
			}
			catch (LinkVetoException ex) {
			}
		}

	}

	private String title;
	protected HtmlPanel panel;
	protected SimpleHtmlRendererContext rcontext;

	public Html4Editor(String title) {
		this.title = title;

		createComponents();
	}

	@Override
	public String getName() {
		return title;
	}

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
		rcontext = new LocalHtmlRendererContext(panel, new SimpleUserAgentContext());

		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
	}

	protected void linkClicked(HTMLElement linkNode, URL url, String target) throws LinkVetoException {
		String rel = linkNode.getAttribute("rel");
		String href = linkNode.getAttribute("href");
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
		}
		else if (target != null && target.equalsIgnoreCase("_external")) {
			try {
				Desktop.getDesktop().browse(new URI(href));
			}
			catch (Exception ex) {
				ex.printStackTrace();
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
