package de.mooxmirror.dorkandoom.projectiles;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EnemyProjectile extends Projectile {
	public EnemyProjectile(int x, int y, double angle) {
		super(x, y, angle, 5, 6, 18);
		try {
			setProjectileSprite(ImageIO.read(new File("res/images/particles/laser_enemy.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}