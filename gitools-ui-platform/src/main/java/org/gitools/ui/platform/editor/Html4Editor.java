/*
 *  Copyright 2010 chris.
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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.w3c.dom.html2.HTMLElement;

public class Html4Editor extends AbstractEditor {

	public static class LinkVetoException extends Exception {
		public LinkVetoException() {
		}
	}

	private String title;
	private HtmlPanel panel;
	private SimpleHtmlRendererContext rcontext;

	public Html4Editor(String title) {
		setTitle(title);
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
		rcontext = new SimpleHtmlRendererContext(panel, new SimpleUserAgentContext()) {
			@Override
			public void linkClicked(HTMLElement linkNode, URL url, String target) {
				try {
					Html4Editor.this.linkClicked(linkNode, url, target);
					super.linkClicked(linkNode, url, target);
				}
				catch (LinkVetoException ex) {
				}
			}
		};

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
				// TODO implement params parsing...
			}
			performUrlAction(name, params);
			throw new LinkVetoException();
		}
	}

	protected void performUrlAction(String name, Map<String, String> params) {
		// do nothing
	}

	public void navigate(URL url) throws Exception {
		rcontext.navigate(url, "_self");
	}
}
