package de.mooxmirror.dorkandoom.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Represents the player controller in the game.
 * 
 * @author Mooxmirror
 * @version 1.0
 */
public class Player extends Entity {
	private final String[] PARTICLE_SPRITE_SHEET_PATH = { "res/images/particles/flair_0.png",
			"res/images/particles/flair_1.png", "res/images/particles/flair_2.png", "res/images/particles/flair_3.png",
			"res/images/particles/flair_4.png" };
	private final int PARTICLE_ANIMATION_UPDATE_FREQUENZY = 100;

	private int mParticleAnimationFrameCounter;
	private long mParticleAnimationFrameTimer;

	private boolean mShieldStatus;
	private BufferedImage mOrbiterSprite;
	private List<BufferedImage> mParticleSpriteList;

	public Player(double x, double y) {
		super(100, true, 0, 5);
		mParticleSpriteList = new ArrayList<BufferedImage>();
		setX(x);
		setY(y);

		try {
			mOrbiterSprite = ImageIO.read(new File("res/images/orbiter.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < PARTICLE_SPRITE_SHEET_PATH.length; i++) {
				mParticleSpriteList.add(ImageIO.read(new File(PARTICLE_SPRITE_SHEET_PATH[i])));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setHitpoints(int hitpoints) {
		super.setHitpoints(hitpoints);
		if (mShieldStatus) {
			super.setHitpoints(getMaxHitpoints());
		}
	}

	@Override
	public void drawEntity(Graphics2D g2d) {
		if (System.currentTimeMillis() - mParticleAnimationFrameTimer > PARTICLE_ANIMATION_UPDATE_FREQUENZY) {
			mParticleAnimationFrameCounter++;
			mParticleAnimationFrameTimer = System.currentTimeMillis();

			if (mParticleAnimationFrameCounter >= mParticleSpriteList.size()) {
				mParticleAnimationFrameCounter = 0;
			}
		}
		g2d.drawImage(mOrbiterSprite, (int) getX() - 32, (int) getY() - 32, 64, 64, null);
		g2d.drawImage(mParticleSpriteList.get(mParticleAnimationFrameCounter), (int) getX() - 22, (int) getY(), 8, 24,
				null);
		g2d.drawImage(mParticleSpriteList.get(mParticleAnimationFrameCounter), (int) getX() + 14, (int) getY(), 8, 24,
				null);

		if (getInvincible()) {
			g2d.setColor(Color.WHITE);
			g2d.drawRoundRect((int) getX() - 32, (int) getY() - 40, 64, 64, 32, 32);
		}
	}

	public void setInvincible(boolean shieldActive) {
		mShieldStatus = shieldActive;
	}

	public boolean getInvincible() {
		return mShieldStatus;
	}

	@Override
	public boolean doesHit(Point p) {
		if (p.x >= getX() - 32 && p.x <= getX() + 32) {
			if (p.y >= getY() - 32 && p.y <= getY() + 32) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {
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
