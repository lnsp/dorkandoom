package de.mooxmirror.dorkandoom.entities;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.mooxmirror.dorkandoom.projectiles.EnemyProjectile;

public class Pulsar extends Enemy implements Destroyable {
	private boolean mExplosion = false;
	private int mFrameCounter = 0;
	private int mFrameId = 10;
	private List<BufferedImage> mSpriteList;
	private long mFrameUpdate;
	private long mProjectileTimer;

	private BufferedImage mSprite;

	private float mRotation;

	@Override
	public void drawEntity(Graphics2D g2d) {
		AffineTransform at = new AffineTransform();
		at.translate(getX(), getY());

		at.rotate(Math.toRadians(mRotation));
		g2d.setTransform(at);
		if (!mExplosion) {
			g2d.drawImage(mSprite, -32, -32, 64, 64, null);
		} else {
			if (System.currentTimeMillis() - mFrameUpdate > 200) {
				mFrameCounter++;
				mFrameUpdate = System.currentTimeMillis();
			}
			if (mFrameCounter < mFrameId) {
				g2d.drawImage(mSpriteList.get(mFrameCounter), -32, -32, 64, 64, null);
			}
		}
		g2d.setTransform(new AffineTransform());

		for (EnemyProjectile p : getProjectiles()) {
			p.drawProjectile(g2d);
		}

		drawHealthBar(g2d);
	}

	public Pulsar() {
		super(6, true, 0, 1, 64, 64);

		mSpriteList = new ArrayList<BufferedImage>();

		try {
			mSprite = ImageIO.read(new File("res/images/pulsar.png"));
			for (int i = 0; i < mFrameId; i++) {
				mSpriteList.add(ImageIO.read(new File("res/images/smoke/smoke_" + i + ".png")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateEntity(float timeScale) {
		super.updateEntity(timeScale);
		mRotation += timeScale;
	}

	@Override
	public void shoot(double timeScale) {
		if (!mExplosion) {
			if (System.currentTimeMillis() - mProjectileTimer > 100 / timeScale) {

				EnemyProjectile enemyProjectile = new EnemyProjectile((int) getX(), (int) getY(), mRotation);

				getProjectiles().add(enemyProjectile);
				mProjectileTimer = System.currentTimeMillis();
			}
		}
	}

	@Override
	public void destroy() {
		mExplosion = true;
	}

	@Override
	public boolean destroyAnimationDone() {
		return (mFrameCounter >= mFrameId);
	}

	@Override
	public boolean destroyAnimationRunning() {
		return (mFrameCounter != 0);
	}

}