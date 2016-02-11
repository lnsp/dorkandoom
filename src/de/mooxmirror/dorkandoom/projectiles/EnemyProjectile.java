package de.mooxmirror.dorkandoom.projectiles;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EnemyProjectile implements Projectile {
	private double 				positionX;
	private double 				positionY;
	private double 				movementAngle;
	private BufferedImage 		projectileSprite;
	
	@Override
	public void update(double timeScale) {
		double radiant = Math.toRadians(movementAngle);
		positionY += Math.sin(radiant) * (timeScale / 2) * 10;
		positionX += Math.cos(radiant) * (timeScale / 2) * 10;
	}

	@Override
	public void spawn(int x, int y, double angle) {
		this.positionX = x;
		this.positionY = y;
		this.movementAngle = angle;
	}

	@Override
	public Point getPosition() {
		return new Point((int) positionX, (int) positionY);
	}

	@Override
	public void drawProjectile(Graphics2D g2d) {
		g2d.drawImage(projectileSprite, (int) positionX - 3, (int) positionY - 9, 6, 18, null);
	}
	
	public EnemyProjectile() {
		try {
			projectileSprite = ImageIO.read(new File("res/images/particles/laser_enemy.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
	}

}