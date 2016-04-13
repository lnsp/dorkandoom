package de.mooxmirror.dorkandoom.entities;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.mooxmirror.dorkandoom.projectiles.EnemyProjectile;

/**
 * Represents the player controller in the game.
 * 
 * @author Mooxmirror
 * @version 1.0
 */
public class SmallOrbiter extends Enemy {
	private boolean mExplosionActive = false;
	private int mFrameAnimationCounter = 0;
	private int mLastAnimationFrameId = 10;
	private List<BufferedImage> mExplosionSpriteList;
	private long mLastExplosionFrameUpdate;
	private long mLastProjectileSpawnTimer;
	private boolean mProjectileSourceSwitcher;

	private BufferedImage mSprite;

	@Override
	public void drawEntity(Graphics2D g2d) {
		if (!mExplosionActive)
			g2d.drawImage(mSprite, (int) getX() - 32, (int) getY() - 32, 64, 64, null);
		else {
			if (System.currentTimeMillis() - mLastExplosionFrameUpdate > 200) {
				mFrameAnimationCounter++;
				mLastExplosionFrameUpdate = System.currentTimeMillis();
			}
			if (mFrameAnimationCounter < mLastAnimationFrameId) {
				g2d.drawImage(mExplosionSpriteList.get(mFrameAnimationCounter), (int) getX() - 32, (int) getY() - 32,
						64, 64, null);
			}
		}

		for (EnemyProjectile p : getProjectiles()) {
			p.drawProjectile(g2d);
		}

		drawHealthBar(g2d);
	}

	public SmallOrbiter() {
		super(4, true, 2, 5);
		mExplosionSpriteList = new ArrayList<BufferedImage>();

		try {
			mSprite = ImageIO.read(new File("res/images/orbiter_enemy.png"));
			for (int i = 0; i < mLastAnimationFrameId; i++) {
				mExplosionSpriteList.add(ImageIO.read(new File("res/images/smoke/smoke_" + i + ".png")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shot(double timeScale) {
		if (!mExplosionActive) {
			if (System.currentTimeMillis() - mLastProjectileSpawnTimer > 100 / timeScale) {

				EnemyProjectile p = null;
				if (mProjectileSourceSwitcher) {
					p = new EnemyProjectile((int) getX() - 16, (int) getY() + 20, 90);
					mProjectileSourceSwitcher = false;
				} else {
					p = new EnemyProjectile((int) getX() + 16, (int) getY() + 20, 90);
					mProjectileSourceSwitcher = true;
				}
				getProjectiles().add(p);
				mLastProjectileSpawnTimer = System.currentTimeMillis();
			}
		}
	}

	@Override
	public boolean doesHit(Point p) {
		if (p.x >= getX() - 32 && p.x <= getX() + 32) {
			if (p.y >= getY() - 32 && p.y < getY() + 32) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {
		mExplosionActive = true;
	}

	@Override
	public boolean destroyAnimationDone() {
		return (mFrameAnimationCounter >= mLastAnimationFrameId);
	}

	@Override
	public boolean destroyAnimationRunning() {
		return (mFrameAnimationCounter != 0);
	}
}
