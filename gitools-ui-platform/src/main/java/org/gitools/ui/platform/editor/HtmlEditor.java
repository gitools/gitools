package org.gitools.ui.platform.editor;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML;

@Deprecated
public class HtmlEditor extends AbstractEditor {

	private static final long serialVersionUID = 1693342849779799326L;

	private String title;
	private JTextPane htmlPane;

	@Override
	public String getName() {
		return title;
	}
	
	public HtmlEditor(String title) {
		this.title = title;
		
		createComponents();
	}
	
	private void createComponents() {
		htmlPane = new JTextPane();
		htmlPane.setEditable(false);
		//htmlPane.setBackground(Color.WHITE);
		htmlPane.setContentType("text/html");

		htmlPane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == EventType.ACTIVATED) {
					AttributeSet attrs = e.getSourceElement().getAttributes();
					AttributeSet aTagAttrs = (AttributeSet) attrs.getAttribute(HTML.Tag.A);
					String rel = (String) aTagAttrs.getAttribute(HTML.Attribute.REL);
					String href = (String) aTagAttrs.getAttribute(HTML.Attribute.HREF);

					if (rel != null && rel.equalsIgnoreCase("action"))
						performUrlAction(href);
					else {
						try {
							htmlPane.setPage(e.getURL());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		final JScrollPane scrollPane = new JScrollPane(htmlPane);
		scrollPane.setBorder(
				BorderFactory.createEmptyBorder());
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	protected void performUrlAction(String name) {
	}
	
	protected void navigate(URL url) throws Exception {
		htmlPane.setPage(url);
	}
	
	protected URL getUrl() {
		return htmlPane.getPage();
	}
	
	@Override
	public Object getModel() {
		return htmlPane.getDocument();
	}

}
