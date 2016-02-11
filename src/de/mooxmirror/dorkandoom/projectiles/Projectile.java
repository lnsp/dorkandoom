package de.mooxmirror.dorkandoom.projectiles;

import java.awt.Graphics2D;
import java.awt.Point;

public interface Projectile {
	public void update(double d);
	public void spawn(int x, int y, double angle);
	public Point getPosition();
	public void drawProjectile(Graphics2D g2d);
	
	public void destroy();
}
