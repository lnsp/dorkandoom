package de.mooxmirror.dorkandoom.entities;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class Asteroid extends Entity implements Destroyable {
	private double mRotationAngle = 0;
	private double mRotationAmount = 0.05;
	private boolean mExplosionActive = false;
	private int mFrameCounter = 0;
	private int mLastFrameId = 8;
	private List<BufferedImage> mExplosionSpriteList;
	private long mLastFrameUpdate;

	private BufferedImage mSprite;

	@Override
	public void updateEntity(float timeScale) {
		super.updateEntity(timeScale);
		mRotationAngle += mRotationAmount * timeScale;
	}

	@Override
	public void drawEntity(Graphics2D g2d) {
		AffineTransform at = new AffineTransform();
		at.translate(getX(), getY());

		at.rotate(mRotationAngle);
		g2d.setTransform(at);
		if (!mExplosionActive)
			g2d.drawImage(mSprite, -32, -32, 64, 64, null);
		else {
			if (System.currentTimeMillis() - mLastFrameUpdate > 100) {
				mFrameCounter++;
				mLastFrameUpdate = System.currentTimeMillis();
			}
			if (mFrameCounter < mLastFrameId)
				g2d.drawImage(mExplosionSpriteList.get(mFrameCounter), -32, -32, 64, 64, null);

		}
		g2d.setTransform(new AffineTransform());

		drawHealthBar(g2d);
	}

	public Asteroid() {
		super(3, true, 0, 5, 64, 64);
		mRotationAmount = new Random().nextDouble() / 50 - 0.01;

		mExplosionSpriteList = new ArrayList<BufferedImage>();
		try {
			mSprite = ImageIO.read(new File("res/images/asteroid.png"));

			for (int i = 0; i < mLastFrameId; i++) {
				mExplosionSpriteList.add(ImageIO.read(new File("res/images/asteroid/explode_" + i + ".png")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		mExplosionActive = true;
	}

	@Override
	public boolean destroyAnimationDone() {
		return (mFrameCounter >= mLastFrameId);
	}

	@Override
	public boolean destroyAnimationRunning() {
		return (mFrameCounter != 0);
	}
}
