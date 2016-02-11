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

/**
 * Represents the player controller in the game.
 * @author Mooxmirror
 * @version 1.0
 */
public class Player implements GameEntity {
	private final int				MAXIMUM_HITPOINTS = 100;
	private final boolean 			HAS_HITBOX = true;
	private final int 				VERTICAL_MOVEMENT_SPEED = 0;
	private final int 				HORIZONTAL_MOVEMENT_SPEED = 5;
	private final String[] 			PARTICLE_SPRITE_SHEET_PATH = {
			"res/images/particles/flair_0.png",
			"res/images/particles/flair_1.png",
			"res/images/particles/flair_2.png",
			"res/images/particles/flair_3.png",
			"res/images/particles/flair_4.png"
	};
	private final int 				PARTICLE_ANIMATION_UPDATE_FREQUENZY = 100;
	
	private int						_particleAnimationFrameCounter;
	private long					_particleAnimationUpdateTimer;
	
	private boolean					_shieldActive;
	private int						_currentHitpoints = MAXIMUM_HITPOINTS;
	private BufferedImage			_orbiterImage;
	private List<BufferedImage> 	_particleSpriteSheet;
	private double					_positionX, _positionY;
	
	@Override
 	public int getHitpoints() {
		return _currentHitpoints;
	}

	@Override
	public void setHitpoints(int hitpoints) {
		_currentHitpoints = hitpoints;
		if (_shieldActive) {
			_currentHitpoints = MAXIMUM_HITPOINTS;
		}
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
	}

	@Override
	public void drawEntity(Graphics2D g2d) {
		if (System.currentTimeMillis() - _particleAnimationUpdateTimer > PARTICLE_ANIMATION_UPDATE_FREQUENZY) {
			_particleAnimationFrameCounter++;
			_particleAnimationUpdateTimer = System.currentTimeMillis();
			
			if (_particleAnimationFrameCounter >= _particleSpriteSheet.size()) {
				_particleAnimationFrameCounter = 0;
			}
		}
		g2d.drawImage(_orbiterImage, (int) _positionX - 32, (int) _positionY - 32, 64, 64, null);
		g2d.drawImage(_particleSpriteSheet.get(_particleAnimationFrameCounter), (int) _positionX - 22, (int) _positionY, 8, 24, null);
		g2d.drawImage(_particleSpriteSheet.get(_particleAnimationFrameCounter), (int) _positionX + 14, (int) _positionY, 8, 24, null);
		
		if(getInvincible()) {
			g2d.setColor(Color.WHITE);
			g2d.drawRoundRect((int) _positionX - 32, (int) _positionY - 40, 64, 64, 32, 32);
		}
	}

	public void setInvincible(boolean shieldActive) {
		_shieldActive = shieldActive;
	}
	
	public boolean getInvincible() {
		return _shieldActive;
	}
	
	public Player(double px, double py) {
		_particleSpriteSheet = new ArrayList<BufferedImage>();
		_positionX = px;
		_positionY = py;
		
		try {
			_orbiterImage = ImageIO.read(new File("res/images/orbiter.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < PARTICLE_SPRITE_SHEET_PATH.length; i++) {
				_particleSpriteSheet.add(ImageIO.read(new File(PARTICLE_SPRITE_SHEET_PATH[i])));
			}
		}
		catch (Exception e) {
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
	public boolean doesHit(Point p) {
		if (p.x >= _positionX - 32 && p.x <= _positionX + 32) {
			if (p.y >= _positionY - 32 && p.y <= _positionY + 32) {
				return true;
			}
		}
		return false;
	}
	@Override
	public void destroy() {
	}
	
	@Override
	public boolean destroyAnimationDone() {
		return false;}

	@Override
	public boolean destroyAnimationRunning() {
		return false;
	}

	public int getMaximumHitpoints() {
		return MAXIMUM_HITPOINTS;
	}
}
