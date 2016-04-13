package de.mooxmirror.dorkandoom.projectiles;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public abstract class Projectile {
	private double mX, mY, mAngle, mSpeed;
	private int mWidth, mHeight;
	private BufferedImage mSprite;
	
	protected Projectile(int x, int y, double angle, double speed, int width, int height) {
		mX = x; mY = y;
		mAngle = angle;
		mSpeed = speed;
		mWidth = width;
		mHeight = height;
	}
	
	public void update(double timeScale) {
		double radiant = Math.toRadians(mAngle);
		mX += Math.cos(radiant) * timeScale * mSpeed;
		mY += Math.sin(radiant) * timeScale * mSpeed;
	}
	
	public Point getPosition() {
		return new Point((int) mX, (int) mY);
	}
	public void drawProjectile(Graphics2D g2d) {
		g2d.drawImage(mSprite, (int) mX - mWidth / 2, (int) mY - mHeight / 2, mWidth, mHeight, null);
	}
	protected void setProjectileSprite(BufferedImage sprite) {
		mSprite = sprite;
	}
	public void destroy() {}
}
