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
	private Font mFont;

	private int mSelection = 0;
	private boolean mExitStatus = false;

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
		g2d.drawString("Dorkandoom", 128 - fm.stringWidth("Dorkandoom") / 2, 64);

		g2d.setFont(mFont.deriveFont(Font.PLAIN, 24));
		fm = g2d.getFontMetrics();

		if (!mExitStatus) {
			g2d.setColor(getMenuColor(0));
			g2d.drawString("Play", 128 - fm.stringWidth("Play") / 2, 200);

			g2d.setColor(getMenuColor(1));
			g2d.drawString("Credits", 128 - fm.stringWidth("Credits") / 2, 250);

			g2d.setColor(getMenuColor(2));
			g2d.drawString("Exit", 128 - fm.stringWidth("Exit") / 2, 300);
		} else {
			g2d.setColor(Color.WHITE);
			g2d.drawString("CU    soon! ", 128 - fm.stringWidth("CU      soon!") / 2, 256);
		}
	}

	private Color getMenuColor(int mID) {
		Color m = new Color(0, 0, 0);

		if (mID == mSelection)
			m = new Color(34, 166, 231);
		else
			m = new Color(214, 57, 66);

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
			mSelection += 1;
			if (mSelection > 2)
				mSelection = 0;
			break;
		case "up":
			mSelection -= 1;
			if (mSelection < 0)
				mSelection = 2;
			break;
		case "space":
			if (mExitStatus) {
				System.exit(0);
			}
			if (mSelection == 0)
				Game.getStates().load(1);
			else if (mSelection == 1)
				Game.getStates().load(3);
			else if (mSelection == 2)
				mExitStatus = true;
			break;
		case "escape":
			mExitStatus = true;
		}

	}

	@Override
	public void keyUp(String msg) {

	}

}
