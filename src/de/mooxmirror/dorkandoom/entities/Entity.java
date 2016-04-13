package de.mooxmirror.dorkandoom.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Helps to indicate an entity.
 * 
 * @author Mooxmirror
 * @version 1.0
 */
public abstract class Entity {
	private final int MAX_HITPOINTS;
	private final float VERTICAL_SPEED;
	private final float HORIZONTAL_SPEED;
	private final boolean HAS_HITBOX;
	private int mWidth, mHeight;
	private int mHitpoints;
	private float mX, mY;

	protected Entity(int maxHitpoints, boolean hasHitbox, float hSpeed, float vSpeed, int width, int height) {
		MAX_HITPOINTS = maxHitpoints;
		VERTICAL_SPEED = vSpeed;
		HORIZONTAL_SPEED = hSpeed;
		HAS_HITBOX = hasHitbox;
		mHitpoints = maxHitpoints;
		mWidth = width;
		mHeight = height;
	}

	/**
	 * Returns the hitpoints value.
	 * 
	 * @return Current hitpoints of the entity.
	 */
	public int getHitpoints() {
		return mHitpoints;
	}

	public int getMaxHitpoints() {
		return MAX_HITPOINTS;
	}

	/**
	 * Sets the hitpoints value.
	 * 
	 * @param hitpoints
	 *            New hitpoints of the entity.
	 */
	public void setHitpoints(int hp) {
		mHitpoints = hp;
	}

	/**
	 * Returns if the entity is targetable.
	 * 
	 * @return Is true, if the entity is targetable.
	 */
	public boolean hasHitbox() {
		return HAS_HITBOX;
	}

	/**
	 * Returns the vertical movement speed.
	 * 
	 * @return Vertical movement speed.
	 */
	public float getVerticalSpeed() {
		return VERTICAL_SPEED;
	}

	/**
	 * Returns the horizontal movement speed.
	 * 
	 * @return Horizontal movement speed.
	 */
	public float getHorizontalSpeed() {
		return HORIZONTAL_SPEED;
	}

	/**
	 * Updates the entity values.
	 * 
	 * @param timeScale
	 *            Player-based timescaling.
	 */
	public void updateEntity(float timeScale) {
		translate(getHorizontalSpeed() * timeScale, getVerticalSpeed() * timeScale);
	}

	/**
	 * Draws the entity with the given graphic interface.
	 * 
	 * @param g2d
	 *            Graphic interface to use for rendering.
	 */
	public abstract void drawEntity(Graphics2D g2d);

	protected void drawHealthBar(Graphics2D g2d) {
		double healthSize = (double) getHitpoints() / getMaxHitpoints() * 64;
		g2d.setColor(Color.RED);
		g2d.fillRect((int) mX - 32, (int) mY - 48, (int) healthSize, 4);
		g2d.setColor(Color.BLACK);
		g2d.fillRect((int) mX - 16, (int) mY - 48, 3, 4);
		g2d.fillRect((int) mX, (int) mY - 48, 3, 4);
		g2d.fillRect((int) mX + 16, (int) mY - 48, 3, 4);
	}

	/**
	 * Sets the current entity position.
	 * 
	 * @param p
	 *            Position on the screen.
	 */
	public void setPosition(Point p) {
		mX = p.x;
		mY = p.y;
	}

	/**
	 * Returns the current entity position.
	 * 
	 * @return p Position on the screen.
	 */
	public Point getPosition() {
		return new Point((int) mX, (int) mY);
	}

	protected float getX() {
		return mX;
	}

	protected float getY() {
		return mY;
	}

	protected void translate(float x, float y) {
		mX += x;
		mY += y;
	}

	/**
	 * Returns if the point does collide with the hitbox.
	 * 
	 * @param The
	 *            control point.
	 * @return true, when the point does collide.
	 */
	public boolean doesHit(Point p) {
		int w = mWidth / 2;
		int h = mHeight / 2;
		if (p.x >= getX() - w / 2 && p.x <= getX() + w / 2) {
			if (p.y >= getY() - h / 2 && p.y <= getY() + h / 2) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Destroys the entity.
	 */
	public void destroy() {}

	public abstract boolean destroyAnimationDone();

	public abstract boolean destroyAnimationRunning();
}
