package de.mooxmirror.dorkandoom.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class Asteroid implements GameEntity, Destroyable {
	private final int 			MAXIMUM_HITPOINTS = 3;
	private final boolean 		HAS_HITBOX = true;
	private final int 			VERTICAL_MOVEMENT_SPEED = 1;
	private final int 			HORIZONTAL_MOVEMENT_SPEED = 5;
	private double 				_objectRotationAngle = 0;
	private double 				_objectRotationAmount = 0.05;
	private boolean 			_explosionActive = false;
	private int 				_frameAnimationCounter = 0;
	private int 				_lastAnimationFrameID = 8;
	private List<BufferedImage> _explosionSpriteSheet;
	private long 				_lastExplosionFrameUpdate;

	private BufferedImage 		_asteroidImage;
	private double 				_positionX = 0, _positionY = 0;
	private int					_currentHitpoints = MAXIMUM_HITPOINTS;
	
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
		_objectRotationAngle += _objectRotationAmount * timeScale;
	}

	@Override
	public void drawEntity(Graphics2D g2d) {
		AffineTransform at = new AffineTransform();
		at.translate(_positionX, _positionY);

		at.rotate(_objectRotationAngle);
		g2d.setTransform(at);
		if (!_explosionActive) g2d.drawImage(_asteroidImage, -32, -32, 64, 64,   null);
		else {
			if (System.currentTimeMillis() - _lastExplosionFrameUpdate > 100) {
				_frameAnimationCounter++;
				_lastExplosionFrameUpdate = System.currentTimeMillis();
			}
			if (_frameAnimationCounter < _lastAnimationFrameID) g2d.drawImage(_explosionSpriteSheet.get(_frameAnimationCounter), -32, -32, 64, 64,   null);

		}
		g2d.setTransform(new AffineTransform());
		
		double healthSize = (double) _currentHitpoints / MAXIMUM_HITPOINTS * 64;
		g2d.setColor(Color.RED);
		g2d.fillRect((int) _positionX - 32, (int) _positionY - 48, (int) healthSize, 4);
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect((int) _positionX - 12, (int) _positionY - 48, 3, 4);
		g2d.fillRect((int) _positionX + 11, (int) _positionY - 48, 3, 4);
		
	}

	@Override
	public void setPosition(Point p) {
		_positionX = p.x;
		_positionY = p.y;
	}
	public Asteroid() {
		_currentHitpoints = MAXIMUM_HITPOINTS;
		_objectRotationAmount = new Random().nextDouble() / 50 - 0.01;

		_explosionSpriteSheet = new ArrayList<BufferedImage>();
		try {
			_asteroidImage = ImageIO.read(new File("res/images/asteroid.png"));

			for (int i = 0; i < _lastAnimationFrameID; i++) {
				_explosionSpriteSheet.add(ImageIO.read(new File("res/images/asteroid/explode_" + i + ".png")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public Point getPosition() {
		return new Point((int) _positionX, (int) _positionY);
	}

	@Override
	public boolean doesHit(Point p) {
		if (p.x > _positionX - 32 && p.x < _positionX + 32) {
			if (p.y > _positionY - 32 && p.y < _positionY + 32) {
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
}
