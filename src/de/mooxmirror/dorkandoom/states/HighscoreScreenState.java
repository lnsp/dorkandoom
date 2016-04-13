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

public class HighscoreScreenState implements GameState {

	private Font mFont;

	@Override
	public void init() {
		try {
			mFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/misc/Bebas.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setFont(mFont.deriveFont(Font.PLAIN, 36));
		g2d.setColor(Color.WHITE);
		FontMetrics fm = g2d.getFontMetrics();
		g2d.drawString("Highscore", 128 - fm.stringWidth("Highscore") / 2, 64);

		g2d.setFont(mFont.deriveFont(Font.PLAIN, 24));
		fm = g2d.getFontMetrics();

		g2d.drawString("Your Highscore", 128 - fm.stringWidth("Your Highscore") / 2, 230);
		g2d.setFont(mFont.deriveFont(Font.PLAIN, 60));
		fm = g2d.getFontMetrics();
		g2d.drawString(Game.dataStorage.get(0).toString(), 128 - fm.stringWidth(Game.dataStorage.get(0).toString()) / 2,
				300);
	}

	@Override
	public void update() {

	}

	@Override
	public void keyDown(String msg) {
		Game.getStates().load(0);
	}

	@Override
	public void keyUp(String msg) {

	}

}
