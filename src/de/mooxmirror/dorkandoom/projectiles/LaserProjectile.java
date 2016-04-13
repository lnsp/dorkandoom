package de.mooxmirror.dorkandoom.projectiles;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LaserProjectile extends Projectile {
	public LaserProjectile(int x, int y, double angle) {
		super(x, y, angle, 4, 6, 18);
		try {
			setProjectileSprite(ImageIO.read(new File("res/images/particles/laser.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
