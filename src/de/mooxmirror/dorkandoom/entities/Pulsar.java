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

public class Pulsar extends Enemy implements Destroyable {
	private boolean mExplosionActive = false;
	private int mFrameAnimationCounter = 0;
	private int mLastAnimationFrameId = 10;
	private List<BufferedImage> mExplosionSpriteSheet;
	private long mLastExplosionFrameUpdate;
	private long mLastProjectileSpawnTimer;
	private boolean mProjectileSourceSwitcher;

	private BufferedImage mSpriteImage;

	private int mProjectileSpawnAngle;
	private int mPulsarRotation;

	@Override
	public void updateEntity(double timeScale) {
		translate(0, getVerticalSpeed() * timeScale);

		if (getHitpoints() == 0) {
			destroy();
		}
		for (EnemyProjectile p : getProjectiles()) {
			p.update(timeScale);
		}
	}

	@Override
	public void drawEntity(Graphics2D g2d) {
		if (!mExplosionActive)
			g2d.drawImage(mSpriteImage, (int) getX() - 32, (int) getY() - 32, 64, 64, null);
		else {
			if (System.currentTimeMillis() - mLastExplosionFrameUpdate > 200) {
				mFrameAnimationCounter++;
				mLastExplosionFrameUpdate = System.currentTimeMillis();
			}
			if (mFrameAnimationCounter < mLastAnimationFrameId) {
				g2d.drawImage(mExplosionSpriteSheet.get(mFrameAnimationCounter), (int) getX() - 32, (int) getY() - 32,
						64, 64, null);
			}
		}

		for (EnemyProjectile p : getProjectiles()) {
			p.drawProjectile(g2d);
		}

		drawHealthBar(g2d);
	}

	public Pulsar() {
		super(6, true, 2, 5);

		mExplosionSpriteSheet = new ArrayList<BufferedImage>();

		try {
			mSpriteImage = ImageIO.read(new File("res/images/pulsar.png"));
			for (int i = 0; i < mLastAnimationFrameId; i++) {
				mExplosionSpriteSheet.add(ImageIO.read(new File("res/images/smoke/smoke_" + i + ".png")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shot(double timeScale) {
		if (!mExplosionActive) {
			if (System.currentTimeMillis() - mLastProjectileSpawnTimer > 100 / timeScale) {

				EnemyProjectile enemyProjectile = new EnemyProjectile((int) getX(), (int) getY(), mProjectileSpawnAngle);

				getProjectiles().add(enemyProjectile);
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