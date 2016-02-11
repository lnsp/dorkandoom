package de.mooxmirror.dorkandoom.entities;

import java.util.List;

import de.mooxmirror.dorkandoom.projectiles.EnemyProjectile;

/**
 * Helps to handle enemy entities.
 * @author Mooxmirror
 *
 */
public interface EnemyEntity extends GameEntity, Destroyable {
	/**
	 * Returns a list of projectiles shot by the enemy.
	 * @return Projectiles
	 */
	public List<EnemyProjectile> getProjectiles();
	/**
	 * Let the entity release a new projectile.
	 * @param timeScale
	 */
	public void shot(double timeScale);
}
