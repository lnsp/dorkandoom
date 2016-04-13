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
	private ArrayList<BufferedImage> mPowerUpSprites;
	private int mLocalEffect;

	public void setPowerUpEffect(int effect) {
		mLocalEffect = effect;
	}

	public int getPowerUpEffect() {
		return mLocalEffect;
	}

	@Override
	public void setHitpoints(int hitpoints) {
	}

	@Override
	public void drawEntity(Graphics2D g2d) {
		g2d.drawImage(mPowerUpSprites.get(mLocalEffect), (int) getX() - 16, (int) getY() - 16, 32, 32, null);
	}

	public PowerUp() {
		super(0, false, 2, 0, 32, 32);
		mPowerUpSprites = new ArrayList<BufferedImage>();
		for (int i = 0; i < POWERUP_SPRITE_SOURCES.length; i++) {
			try {
				mPowerUpSprites.add(ImageIO.read(new File("res/images/powerups/" + POWERUP_SPRITE_SOURCES[i])));
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
