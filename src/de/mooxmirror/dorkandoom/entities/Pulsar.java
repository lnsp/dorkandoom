package de.mooxmirror.dorkandoom.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.mooxmirror.dorkandoom.projectiles.EnemyProjectile;

public class Pulsar implements EnemyEntity, Destroyable {
		private final int 				MAXIMUM_HITPOINTS = 6;
		private final boolean 			HAS_HITBOX = true;
		private final int 				VERTICAL_MOVEMENT_SPEED = 2;
		private final int 				HORIZONTAL_MOVEMENT_SPEED = 5;


		private boolean 				_explosionActive = false;
		private int 					_frameAnimationCounter  = 0;
		private int 					_lastAnimationFrameID  = 10;
		private List<BufferedImage> 	_explosionSpriteSheet;
		private long 					_lastExplosionFrameUpdate;
		private long					_lastProjectileSpawnTimer;
		private boolean					_projectileSourceSwitcher;

		private int 					_currentHitpoints = 4;
		private BufferedImage 			_spriteImage;

		private double					_positionX = 0, _positionY = 0;
		private List<EnemyProjectile>	_entityProjectiles;
		private int						_projectileSpawnAngle;
		private int						_pulsarRotation;
		@Override
		public int getHitpoints() {
			return _currentHitpoints;
		}

		@Override
		public void setHitpoints(int hitpoints) {
			_currentHitpoints = hitpoints;
		}

		@Override
		public boolean hasHitbox() {
			return HAS_HITBOX;
		}

		@Override
		public int getVerticalSpeed() {
			return VERTICAL_MOVEMENT_SPEED;
		}

		@Override
		public int getHorizontalSpeed() {
			return HORIZONTAL_MOVEMENT_SPEED;
		}

		@Override
		public void updateEntity(double timeScale) {
			_positionY += VERTICAL_MOVEMENT_SPEED * timeScale;
			
			if (getHitpoints() == 0) {
				destroy();
			}
			for (EnemyProjectile p : _entityProjectiles) {
				p.update(timeScale);
			}
		}

		@Override
		public void drawEntity(Graphics2D g2d) {
			if (!_explosionActive) g2d.drawImage(_spriteImage, (int) _positionX - 32, (int) _positionY - 32, 64, 64,   null);
			else {
				if (System.currentTimeMillis() - _lastExplosionFrameUpdate > 200) {
					_frameAnimationCounter++;
					_lastExplosionFrameUpdate = System.currentTimeMillis();
				}
				if (_frameAnimationCounter < _lastAnimationFrameID) {
					g2d.drawImage(_explosionSpriteSheet.get(_frameAnimationCounter), (int) _positionX - 32, (int) _positionY - 32, 64, 64,   null);
				}
			}

			for(EnemyProjectile p : _entityProjectiles) {
				p.drawProjectile(g2d);
			}
			double healthSize = (double) _currentHitpoints / MAXIMUM_HITPOINTS * 64;
			g2d.setColor(Color.RED);
			g2d.fillRect((int) _positionX - 32, (int) _positionY - 48, (int) healthSize, 4);
			g2d.setColor(Color.BLACK);
			g2d.fillRect((int) _positionX - 16, (int) _positionY - 48, 3, 4);
			g2d.fillRect((int) _positionX, (int) _positionY - 48, 3, 4);
			g2d.fillRect((int) _positionX + 16, (int) _positionY - 48, 3, 4);

		}
		public Pulsar() {
			_currentHitpoints = MAXIMUM_HITPOINTS;
			_entityProjectiles = new ArrayList<EnemyProjectile>();
			_explosionSpriteSheet = new ArrayList<BufferedImage>();
			
			try {
				_spriteImage = ImageIO.read(new File("res/images/pulsar.png"));
				for (int i = 0; i < _lastAnimationFrameID; i++) {
					_explosionSpriteSheet.add(ImageIO.read(new File("res/images/smoke/smoke_" + i + ".png")));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setPosition(Point p) {
			_positionX = p.x;
			_positionY = p.y;
		}

		@Override
		public Point getPosition() {
			return new Point((int) _positionX, (int) _positionY);
		}
		@Override
		public void shot(double timeScale) {
			if(!_explosionActive){
				if (System.currentTimeMillis() - _lastProjectileSpawnTimer > 100 / timeScale) {

					EnemyProjectile enemyProjectile = new EnemyProjectile();
					enemyProjectile.spawn((int) _positionX, (int) _positionY, _projectileSpawnAngle);
					
					_entityProjectiles.add(enemyProjectile);
					_lastProjectileSpawnTimer = System.currentTimeMillis();
				}
			}
		}
		@Override
		public boolean doesHit(Point p) {
			if (p.x >= _positionX - 32 && p.x <= _positionX + 32) {
				if (p.y >= _positionY - 32 && p.y < _positionY + 32) {
					return true;
				}
			}
			return false;
		}
		@Override
		public void destroy() {
			_explosionActive = true;
		}
		@Override
		public boolean destroyAnimationDone() {
			return (_frameAnimationCounter >= _lastAnimationFrameID);
		}

		@Override
		public boolean destroyAnimationRunning() {
			return (_frameAnimationCounter != 0);
		}


		@Override
		public List<EnemyProjectile> getProjectiles() {
			return _entityProjectiles;
		}
	}