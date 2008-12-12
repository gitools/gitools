package es.imim.bg.ztools.ui.views;

import java.io.IOException;
import java.net.URL;

public class WelcomeView extends HtmlView {

	private static final long serialVersionUID = 6851947500231401412L;

	public WelcomeView() {
		super("Welcome");
		
		try {
			URL url = getClass().getResource("/html/welcome.html");
			setPage(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
