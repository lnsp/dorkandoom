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

public class CreditsScreenState implements GameState {
	private Font menuFont;
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
		g2d.setColor(Color.WHITE);
		g2d.setFont(menuFont.deriveFont(Font.PLAIN, 24));
		FontMetrics fm = g2d.getFontMetrics();
		
		g2d.drawString("Game by Mooxmirror.de", 128 - fm.stringWidth("Game by Mooxmirror.de") / 2, 256);
	}

	@Override
	public void update() {
	}

	@Override
	public void keyDown(String msg) {
		if (msg.equals("space")) {
			Game.stateManager.loadState(0);
		}
	}

	@Override
	public void keyUp(String msg) {
	}

}
