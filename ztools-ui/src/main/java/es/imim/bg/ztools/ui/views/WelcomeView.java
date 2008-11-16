package es.imim.bg.ztools.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class WelcomeView extends AbstractView {

	private static final long serialVersionUID = 1693342849779799326L;

	private JTextPane htmlPane;

	@Override
	public String getName() {
		return "Welcome";
	}
	
	public WelcomeView() {
		createComponents();

		try {
			URL url = getClass().getResource("/html/welcome.html");
			htmlPane.setPage(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createComponents() {
		htmlPane = new JTextPane();
		//htmlPane.setBackground(Color.WHITE);
		htmlPane.setContentType("text/html");
		final JScrollPane scrollPane = new JScrollPane(htmlPane);
		scrollPane.setBorder(
				BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public Object getModel() {
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
