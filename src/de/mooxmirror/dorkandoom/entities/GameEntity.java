package de.mooxmirror.dorkandoom.entities;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Helps to indicate an entity.
 * @author Mooxmirror
 * @version 1.0
 */
public interface GameEntity {
	/**
	 * Returns the hitpoints value.
	 * @return Current hitpoints of the entity.
	 */
	public int getHitpoints();
	/**
	 * Sets the hitpoints value.
	 * @param hitpoints New hitpoints of the entity.
	 */
	public void setHitpoints(int hitpoints);
	/**
	 * Returns if the entity is targetable.
	 * @return Is true, if the entity is targetable.
	 */
	public boolean hasHitbox();
	/**
	 * Returns the vertical movement speed.
	 * @return Vertical movement speed.
	 */
	public int getVerticalSpeed();
	/**
	 * Returns the horizontal movement speed.
	 * @return Horizontal movement speed.
	 */
	public int getHorizontalSpeed();
	/**
	 * Updates the entity values.
	 * @param timeScale Player-based timescaling.
	 */
	public void updateEntity(double timeScale);
	/**
	 * Draws the entity with the given graphic interface.
	 * @param g2d Graphic interface to use for rendering.
	 */
	public void drawEntity(Graphics2D g2d);
	/**
	 * Sets the current entity position.
	 * @param p Position on the screen.
	 */
	public void setPosition(Point p);
	/** 
	 * Returns the current entity position.
	 * @return p Position on the screen.
	 */
	public Point getPosition();
	/**
	 * Returns if the point does collide with the hitbox.
	 * @param The control point.
	 * @return true, when the point does collide.
	 */
	public boolean doesHit(Point p);
	public void destroy();
	public boolean destroyAnimationDone();
	public boolean destroyAnimationRunning();
}
