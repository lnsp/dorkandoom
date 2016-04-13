package de.mooxmirror.dorkandoom.powerups;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.mooxmirror.dorkandoom.entities.Entity;

public class PowerUp extends Entity {
	public static final int BOOST_FASTER = 0;
	public static final int BOOST_SLOWER = 1;
	public static final int DOUBLE_SHOT = 2;
	public static final int TRIPLE_SHOT = 3;
	public static final int QUADRO_SHOT = 4;
	public static final int HEALTH_RESTORE = 5;

	private final String[] POWERUP_SPRITE_SOURCES = { "Boost_Faster.png", "Boost_Slower.png", "Double_Shoot.png",
			"Triple_Shoot.png", "Quadro_Shoot.png", "Health_Restore.png" };
	private ArrayList<BufferedImage> _powerUpSpriteSheet;
	private int _localEffect;

	@Override
	public int getHitpoints() {
		return 0;
	}

	public void setPowerUpEffect(int effect) {
		_localEffect = effect;
	}

	public int getPowerUpEffect() {
		return _localEffect;
	}

	@Override
	public void setHitpoints(int hitpoints) {

	}

	@Override
	public boolean hasHitbox() {
		return false;
	}

	@Override
	public int getVerticalSpeed() {
		return 2;
	}

	@Override
	public int getHorizontalSpeed() {
		return 0;
	}

	@Override
	public void updateEntity(double timeScale) {
		translate(0, timeScale * getVerticalSpeed());
	}

	@Override
	public void drawEntity(Graphics2D g2d) {
		g2d.drawImage(_powerUpSpriteSheet.get(_localEffect), (int) getX() - 16, (int) getY() - 16, 32, 32, null);
	}

	@Override
	public boolean doesHit(Point p) {
		if (p.x >= getX() - 16 && p.x <= getX() + 16) {
			if (p.y >= getY() - 16 && p.y < getY() + 16) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {
		// TODO Automatisch generierter Methodenstub

	}

	public PowerUp() {
		super(0, false, 2, 0);
		_powerUpSpriteSheet = new ArrayList<BufferedImage>();
		for (int i = 0; i < POWERUP_SPRITE_SOURCES.length; i++) {
			try {
				_powerUpSpriteSheet.add(ImageIO.read(new File("res/images/powerups/" + POWERUP_SPRITE_SOURCES[i])));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("res/images/powerups/" + POWERUP_SPRITE_SOURCES[i]);
			}
		}
	}

	@Override
	public boolean destroyAnimationDone() {
		return false;
	}

	@Override
	public boolean destroyAnimationRunning() {
		return false;
	}

}
