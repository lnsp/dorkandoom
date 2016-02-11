package de.mooxmirror.dorkandoom.powerups;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.mooxmirror.dorkandoom.entities.GameEntity;

public class PowerUp implements GameEntity {
	public static final int	BOOST_FASTER = 0,
							BOOST_SLOWER = 1,
							DOUBLE_SHOT = 2,
							TRIPLE_SHOT = 3,
							QUADRO_SHOT = 4,
							HEALTH_RESTORE = 5;
	private final String[] 				POWERUP_SPRITE_SOURCES = {
			"Boost_Faster.png",
			"Boost_Slower.png",
			"Double_Shoot.png",
			"Triple_Shoot.png",
			"Quadro_Shoot.png",
			"Health_Restore.png"
	};
	private ArrayList<BufferedImage> 	_powerUpSpriteSheet;
	private double 						_positionX = 0, _positionY = 0;
	private int 						_localEffect;

	@Override
	public int getHitpoints() {
		return 0;
	}
	public void setPowerUpEffect(int effect) {
		_localEffect = effect;
	}
	public int getPowerUpEffect() {
		return _localEffect;
	}
	@Override
	public void setHitpoints(int hitpoints) {

	}

	@Override
	public boolean hasHitbox() {
		return false;
	}

	@Override
	public int getVerticalSpeed() {
		// TODO Automatisch generierter Methodenstub
		return 2;
	}

	@Override
	public int getHorizontalSpeed() {
		// TODO Automatisch generierter Methodenstub
		return 0;
	}

	@Override
	public void updateEntity(double timeScale) {
		_positionY += timeScale * getVerticalSpeed();
	}

	@Override
	public void drawEntity(Graphics2D g2d) {
		g2d.drawImage(_powerUpSpriteSheet.get(_localEffect), (int) _positionX - 16, (int) _positionY - 16, 32, 32, null);
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
		if (p.x >= _positionX - 16 && p.x <= _positionX + 16) {
			if (p.y >= _positionY - 16 && p.y < _positionY + 16) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {
		// TODO Automatisch generierter Methodenstub

	}
	public PowerUp() {
		_powerUpSpriteSheet = new ArrayList<BufferedImage>();
		for (int i = 0; i < POWERUP_SPRITE_SOURCES.length; i++) {
			try {
				_powerUpSpriteSheet.add(ImageIO.read(new File("res/images/powerups/" + POWERUP_SPRITE_SOURCES[i])));
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("res/images/powerups/" + POWERUP_SPRITE_SOURCES[i]);
			}
		}
	}
	@Override
	public boolean destroyAnimationDone() {
		// TODO Automatisch generierter Methodenstub
		return false;
	}

	@Override
	public boolean destroyAnimationRunning() {
		// TODO Automatisch generierter Methodenstub
		return false;
	}

}
