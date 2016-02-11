package de.mooxmirror.dorkandoom.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import de.mooxmirror.dorkandoom.main.Game;

public class MainMenuState implements GameState {
	private Font menuFont;
	
	private int selected = 0;
	private boolean setExitAsk = false;
	
	@Override
	public void init() {
		try {
			menuFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/misc/Bebas.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(menuFont.deriveFont(Font.PLAIN, 36));
		g2d.setColor(Color.WHITE);
		FontMetrics fm = g2d.getFontMetrics();
		g2d.drawString("Dorkandoom", 128 - fm.stringWidth("Dorkandoom") / 2, 64);
		
		g2d.setFont(menuFont.deriveFont(Font.PLAIN, 24));
		fm = g2d.getFontMetrics();
		
		if (!setExitAsk) {
			g2d.setColor(getMenuColor(0));
			g2d.drawString("Play", 128 - fm.stringWidth("Play") / 2, 200);

			g2d.setColor(getMenuColor(1));
			g2d.drawString("Credits", 128 - fm.stringWidth("Credits") / 2, 250);
			
			g2d.setColor(getMenuColor(2));
			g2d.drawString("Exit", 128 - fm.stringWidth("Exit") / 2, 300);
		}
		else {
			g2d.setColor(Color.WHITE);
			g2d.drawString("CU    soon! ", 128 - fm.stringWidth("CU      soon!") / 2, 256);
		}
	}
	private Color getMenuColor(int mID) {
		Color m  = new Color(0,0,0);
		
		if (mID == selected) m = new Color(34, 166, 231);
		else m = new Color(214, 57, 66);
		
		return m;
	}
	@Override
	public void update() {
		// TODO Automatisch generierter Methodenstub

	}

	@Override
	public void keyDown(String msg) {
		switch (msg) {
		case "down":
			selected += 1;
			if (selected > 2) selected = 0;
			break;
		case "up":
			selected -= 1;
			if (selected < 0) selected = 2;
			break;
		case "space":
			if (setExitAsk) {
				System.exit(0);
			}
			if (selected == 0) Game.stateManager.loadState(1);
			else if (selected == 1) Game.stateManager.loadState(3);
			else if (selected == 2) setExitAsk = true;
			break;
		case "escape":
			setExitAsk = true;
		}
		
		
	}

	@Override
	public void keyUp(String msg) {
		
	}

}
