package org.gitools.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogHeaderPanel extends JComponent {

	private static final long serialVersionUID = 1150407654841675810L;

	public enum Status {
		normal, warning, error
	}
	
	protected String header;
	protected String message;
	protected Status status;
	protected Icon logo;
	
	protected JLabel headerLabel;
	protected JLabel messageLabel;
	protected JLabel logoLabel;
	
	public DialogHeaderPanel(
			String header, 
			String message,
			Status status,
			Icon logo) {
		
		this.header = header;
		this.message = message;
		this.status = status;
		this.logo = logo;
		
		logoLabel = (logo != null) ? new JLabel(logo) : new JLabel();
		logoLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		headerLabel = new JLabel(header);
		headerLabel.setFont(headerLabel
				.getFont().deriveFont(Font.BOLD));
		headerLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		//TODO status icon
		messageLabel = new JLabel(message);
		messageLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 4));
		
		final JPanel p2 = new JPanel(new BorderLayout());
		p2.add(headerLabel, BorderLayout.NORTH);
		p2.add(messageLabel, BorderLayout.CENTER);
		
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		setLayout(new BorderLayout());
		//setOpaque(true);
		setBackground(Color.WHITE);
		add(p2, BorderLayout.CENTER);
		add(logoLabel, BorderLayout.EAST);
	}
	
	//TODO
	/*@Override
	protected void paintComponent(Graphics g) {

		g.setColor(Color.WHITE);
		Rectangle r = g.getClipBounds();
		g.fillRect(0, 0, r.width, r.height);
		//super.paintComponent(g);
		System.out.print("...");
	}*/
	
	public String getHeader() {
		return header;
	}
	
	public void setHeader(String header) {
		this.header = header;
		headerLabel.setText(header);
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
		messageLabel.setText(message);
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
		//TODO status icon
	}
	
	public Icon getLogo() {
		return logo;
	}
	
	public void setLogo(Icon logo) {
		this.logo = logo;
		logoLabel.setIcon(logo);
	}
}
