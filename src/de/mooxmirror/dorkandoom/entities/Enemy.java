package de.mooxmirror.dorkandoom.entities;

import java.util.ArrayList;
import java.util.List;

import de.mooxmirror.dorkandoom.projectiles.EnemyProjectile;

/**
 * Helps to handle enemy entities.
 * 
 * @author Mooxmirror
 *
 */
public abstract class Enemy extends Entity {
	private final List<EnemyProjectile> mProjectiles;

	protected Enemy(int maxHitpoints, boolean hasHitbox, int verticalSpeed, int horizontalSpeed) {
		super(maxHitpoints, hasHitbox, verticalSpeed, horizontalSpeed);
		mProjectiles = new ArrayList<EnemyProjectile>();
	}

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

	/**
	 * Returns a list of projectiles shot by the enemy.
	 * 
	 * @return Projectiles
	 */
	public List<EnemyProjectile> getProjectiles() {
		return mProjectiles;
	}

	/**
	 * Let the entity release a new projectile.
	 * 
	 * @param timeScale
	 */
	public abstract void shot(double timeScale);
}
