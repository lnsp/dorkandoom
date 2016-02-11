package de.mooxmirror.dorkandoom.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import de.mooxmirror.dorkandoom.entities.Asteroid;
import de.mooxmirror.dorkandoom.entities.EnemyEntity;
import de.mooxmirror.dorkandoom.entities.Player;
import de.mooxmirror.dorkandoom.entities.Pulsar;
import de.mooxmirror.dorkandoom.entities.SmallOrbiter;
import de.mooxmirror.dorkandoom.main.Game;
import de.mooxmirror.dorkandoom.powerups.PowerUp;
import de.mooxmirror.dorkandoom.projectiles.EnemyProjectile;
import de.mooxmirror.dorkandoom.projectiles.LaserProjectile;
import de.mooxmirror.dorkandoom.projectiles.Projectile;

/**
 * Main game state with asteroids etc.
 * @author Mooxmirror
 * @version 1.0
 */
public class SingleplayerGameState implements GameState {
	final private int			SCORE_COUNT_FREQUENZY = 200;
	final private int			ASTEROID_SPAWN_FREQUENZY = 1000;
	final private int			ORBIT_SPAWN_FREQUENZY = 3000;
	final private int			POWERUP_SPAWN_FREQUENZY = 5000;
	final private int			PROJECTILE_SPAWN_FREQUENZY = 100;
	final private int 			BOOST_DURATION_TIME = 2000;
	final private int			BOOST_UPDATE_TIME = 60;

	private Random 				randomValueGenerator;

	private Player 				gamePlayer;
	private List<Projectile> 	gameProjectiles;
	private List<Asteroid> 		gameAsteroids;
	private List<EnemyEntity> 	gameEnemies;
	private List<PowerUp> 		gamePowerUps;
	private BufferedImage 		overlayHUD;

	private int 				gameScore = 0;
	private long				_scoreCountTimer = 0;

	private double 				gameDelta = 1;

	private boolean 			_rightButtonPressed;
	private boolean 			_leftButtonPressed;
	private boolean 			_spaceButtonPressed;
	private boolean 			_playerProjectileSourceSwitcher;
	private byte				_playerProjectileSpawnAmount = 1;
	private byte				_projectileSourceCounter = 100;
	private float				_projectileReloadCounter = 100;

	private long 				_asteroidSpawnTimer;
	private long 				_orbitSpawnTimer;
	private long 				_powerupSpawnTimer;
	private long				_projectileSpawnTimer;

	private int 				_slowdownCounter = 500;
	private long				_cleanUpEntitiesTimer;
	private boolean				_temporaryBoostActive;
	private long				_temporaryBoostStartTime;
	private double				_temporaryDeltaOffset;
	private double				_temporaryOriginalDelta;
	private long				_temporaryBoostTimer;

	private int					_darkOverlayAlpha;
	private int					_darkOverlayTimer;
	
	private boolean				_gamePaused;

	public SingleplayerGameState() {
		init();
	}

	@Override
	public void init() {
		randomValueGenerator = new Random();
		gamePlayer = new Player(128, 418);
		gameProjectiles = new ArrayList<Projectile>();
		gameAsteroids = new ArrayList<Asteroid>();
		gameEnemies = new ArrayList<EnemyEntity>();
		gamePowerUps = new ArrayList<PowerUp>();
		try {
			overlayHUD = ImageIO.read(new File("res/images/hud/overlay.png"));
			System.out.println("Overlay succesfully loaded.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		gamePlayer.drawEntity(g2d);

		for (Projectile p : gameProjectiles) {
			p.drawProjectile(g2d);
		}
		for (Asteroid p : gameAsteroids) {
			p.drawEntity(g2d);
		}
		for (EnemyEntity e : gameEnemies) {
			e.drawEntity(g2d);
		}
		for (PowerUp p: gamePowerUps) {
			p.drawEntity(g2d);
		}

		int life = gamePlayer.getHitpoints();
		int calcBarLife = life * 2;
		int calcBarShots = (int) _projectileReloadCounter * 2;

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, 256, 32);
		if (!gamePlayer.getInvincible()) {
			g2d.setColor(Color.GREEN);
		}
		else {
			g2d.setColor(Color.WHITE);
		}
		g2d.fillRect(5, 5, (int) calcBarLife, 10);
		if (!gamePlayer.getInvincible()) {
			g2d.setColor(Color.RED);
		}
		else {
			g2d.setColor(Color.WHITE);
		}
		g2d.fillRect(5, 19, (int) calcBarShots, 10);

		g2d.drawImage(overlayHUD, 0, 0, 256, 512, null);
		g2d.setColor(Color.WHITE);

		String highscoreString = Integer.toString(gameScore) + " Meter";
		String lifeString = Integer.toString(gamePlayer.getHitpoints()) + " / 100";
		String shotsString = Float.toString(_projectileReloadCounter) + " / 100";

		if (gamePlayer.getInvincible()) {
			lifeString = "Infinite!"; shotsString = "Infinite!";
		}
		g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
		FontMetrics fm = g2d.getFontMetrics();

		g2d.drawString(highscoreString, 128 - (fm.stringWidth(highscoreString) / 2), 500);
		g2d.drawString(shotsString, 250 - (fm.stringWidth(shotsString)), 26);
		g2d.drawString(lifeString, 250 - (fm.stringWidth(lifeString)), 14);

		g2d.setColor(new Color(0,0,0, _darkOverlayAlpha));
		g2d.fillRect(0, 0, 256, 512);
	}

	private void updateHighscore() {
		if (System.currentTimeMillis() - _scoreCountTimer > SCORE_COUNT_FREQUENZY /  gameDelta) {
			gameScore += 1;
			_scoreCountTimer = System.currentTimeMillis();
		}
	}
	private void spawnAsteroids() {
		if (System.currentTimeMillis() - _asteroidSpawnTimer > (randomValueGenerator.nextInt(ASTEROID_SPAWN_FREQUENZY) + ASTEROID_SPAWN_FREQUENZY) / gameDelta) {
			Asteroid a = new Asteroid();
			a.setPosition(new Point(randomValueGenerator.nextInt(224) + 16, 0));
			gameAsteroids.add(a);
			_asteroidSpawnTimer = System.currentTimeMillis();
		}
	}
	private void spawnOrbits() {
		if (System.currentTimeMillis() - _orbitSpawnTimer > (randomValueGenerator.nextInt(ORBIT_SPAWN_FREQUENZY) + ORBIT_SPAWN_FREQUENZY) / gameDelta) {
			SmallOrbiter smallOrbiter = new SmallOrbiter();
			smallOrbiter.setPosition(new Point(randomValueGenerator.nextInt(224) + 16, 0));
			gameEnemies.add(smallOrbiter);
			_orbitSpawnTimer = System.currentTimeMillis();
		}
	}
	private void spawnPulsar() {
		
	}
	private void spawnPowerUps() {
		if (System.currentTimeMillis() - _powerupSpawnTimer > (randomValueGenerator.nextInt(POWERUP_SPAWN_FREQUENZY) + POWERUP_SPAWN_FREQUENZY) / gameDelta) {
			PowerUp p = new PowerUp();
			p.setPowerUpEffect(randomValueGenerator.nextInt(6));
			p.setPosition(new Point(randomValueGenerator.nextInt(224) + 16,   0));
			gamePowerUps.add(p);
			_powerupSpawnTimer = System.currentTimeMillis();
		}
	}
	private void handleInput() {
		Point playerPosition = gamePlayer.getPosition();
		if (_leftButtonPressed) {
			playerPosition.x -= gamePlayer.getHorizontalSpeed();
			if(playerPosition.x < 32) playerPosition.x = 32;
		}
		if (_rightButtonPressed) {
			playerPosition.x += gamePlayer.getHorizontalSpeed();
			if(playerPosition.x > 224) playerPosition.x = 224;
		}
		if (_spaceButtonPressed) {
			if (System.currentTimeMillis() - _projectileSpawnTimer > PROJECTILE_SPAWN_FREQUENZY) {
				if (_projectileReloadCounter > 1) {
					if(_playerProjectileSpawnAmount == 1) {
						LaserProjectile laserProjectile = new LaserProjectile();
						if(_playerProjectileSourceSwitcher) {
							laserProjectile.spawn(playerPosition.x - 16, playerPosition.y - 20, -90);
						}
						else {
							laserProjectile.spawn(playerPosition.x + 16, playerPosition.y - 20, -90);
						}
						gameProjectiles.add(laserProjectile);
					}
					else {
						List<Integer> angles = new ArrayList<Integer>();
						switch (_playerProjectileSpawnAmount) {
						case 2:
							angles.add(-100); angles.add(-80); break;
						case 3:
							angles.add(-105); angles.add(-90); angles.add(-75); break;
						case 4:
							angles.add(-105); angles.add(-95); angles.add(-85); angles.add(-75); break;
						}
						for (Integer m : angles) {
							LaserProjectile laserProjectile = new LaserProjectile();
							if (_playerProjectileSourceSwitcher) {
								laserProjectile.spawn(playerPosition.x - 13 - (angles.size()  * 2), playerPosition.y - 20, m);
							}
							else {
								laserProjectile.spawn(playerPosition.x + 13 + (angles.size()  * 2), playerPosition.y - 20, m);
							}
							gameProjectiles.add(laserProjectile);
						}
						_projectileSourceCounter--;

						if (_projectileSourceCounter <= 0) {
							_playerProjectileSpawnAmount = 1;
							_projectileSourceCounter = 0;
						}
					}

					_playerProjectileSourceSwitcher = !_playerProjectileSourceSwitcher;
				}
				else {
					_spaceButtonPressed = false;
				}
				_projectileSpawnTimer = System.currentTimeMillis();
				if(!gamePlayer.getInvincible()) _projectileReloadCounter -= 1;
			}
		}
		else {
			_projectileReloadCounter += 0.5;
			if (_projectileReloadCounter > 100) _projectileReloadCounter = 100;
		}
		gamePlayer.setPosition(playerPosition);
	}
	private void handlePlayerProjectiles() {
		for (int i = 0; i < gameProjectiles.size(); i++) {
			gameProjectiles.get(i).update(gameDelta);
			if (gameProjectiles.get(i).getPosition().y < 0) {
				gameProjectiles.remove(i);
			}
			else {
				try {
					for (Asteroid a : gameAsteroids) {
						if(a.doesHit(gameProjectiles.get(i).getPosition()) && !a.destroyAnimationRunning()) {
							gameProjectiles.remove(i);

							if (a.getHitpoints() > 0) {
								a.setHitpoints(a.getHitpoints() - 1);

							}
							else {
								a.destroy();
							}
						}

					}
					for (EnemyEntity e : gameEnemies) {
						if(e.doesHit(gameProjectiles.get(i).getPosition()) && !e.destroyAnimationRunning()) {
							gameProjectiles.remove(i);

							if (e.getHitpoints() > 0) {
								e.setHitpoints(e.getHitpoints() - 1);

							}
							else {
								e.destroy();

							}
						}
					}
				}
				catch (Exception e) {}

			}

		}
	}
	private void testAsteroidCollision() {
		Point playerPosition = gamePlayer.getPosition();
		for (Asteroid a : gameAsteroids) {
			a.updateEntity(gameDelta);
			if (Math.sqrt(Math.abs(playerPosition.x - a.getPosition().x) + Math.abs(playerPosition.y - a.getPosition().y)) < 8) {
				if (!a.destroyAnimationRunning() && !a.destroyAnimationDone()) {
					a.destroy();
					gamePlayer.setHitpoints(gamePlayer.getHitpoints() - 10);
				}
			}

		}
	}
	private void testPowerUpCollision() {
		Point playerPosition = gamePlayer.getPosition();
		for (int i = 0; i < gamePowerUps.size(); i++) {
			PowerUp p = gamePowerUps.get(i);
			p.updateEntity(gameDelta);
			if (p.doesHit(playerPosition)) {
				int powerupEffect  = p.getPowerUpEffect();
				if (powerupEffect == PowerUp.BOOST_FASTER) {
					if(gameDelta < 5) {
						gameDelta += 0.5;
					}
				}
				else if (powerupEffect == PowerUp.BOOST_SLOWER) {
					if(gameDelta > 0.5) {
						gameDelta -= 0.5;
					}
				}
				else if (powerupEffect == PowerUp.DOUBLE_SHOT) {
					if (! (_playerProjectileSpawnAmount > 2)) _playerProjectileSpawnAmount = 2;
					_projectileSourceCounter += 50;
				}
				else if (powerupEffect == PowerUp.TRIPLE_SHOT) {
					if (! (_playerProjectileSpawnAmount > 3)) _playerProjectileSpawnAmount = 3;
					_projectileSourceCounter += 50;
				}
				else if (powerupEffect == PowerUp.QUADRO_SHOT) {
					_playerProjectileSpawnAmount = 4;
					_projectileSourceCounter += 50;
				}
				else if (powerupEffect == PowerUp.HEALTH_RESTORE) {
					gamePlayer.setHitpoints(gamePlayer.getMaximumHitpoints());
				}
				gamePowerUps.remove(i);
			}
		}
	}
	private void cleanUp() {
		if (System.currentTimeMillis() - _cleanUpEntitiesTimer > 30) {
			for (int a = 0; a < gameAsteroids.size(); a++) {
				if (gameAsteroids.get(a).getPosition().y < 0 || gameAsteroids.get(a).getPosition().y > 512 || gameAsteroids.get(a).destroyAnimationDone()) {
					gameAsteroids.remove(a);
				}
			}
			for (int e = 0; e < gameEnemies.size(); e++) {
				if (gameEnemies.get(e).getPosition().y < 0 || gameEnemies.get(e).getPosition().y > 512 || gameEnemies.get(e).destroyAnimationDone()) {
					gameEnemies.remove(e);
				}
			}
			for (int q = 0; q < gamePowerUps.size(); q++) {
				if (gamePowerUps.get(q).getPosition().y < 0 || gamePowerUps.get(q).getPosition().y > 512 || gamePowerUps.get(q).destroyAnimationDone()) {
					gamePowerUps.remove(q);
				}
			}
			_cleanUpEntitiesTimer = System.currentTimeMillis();
		}
	}
	private void testEnemyCollision() {
		Point playerPosition = gamePlayer.getPosition();
		for (int ec = 0; ec < gameEnemies.size(); ec++) {
			gameEnemies.get(ec).updateEntity(gameDelta);

			if (Math.sqrt(Math.abs(playerPosition.x - gameEnemies.get(ec).getPosition().x) + Math.abs(playerPosition.y - gameEnemies.get(ec).getPosition().y)) < 16) {
				gameEnemies.get(ec).shot(gameDelta);
			}
			if (Math.sqrt(Math.abs(playerPosition.x - gameEnemies.get(ec).getPosition().x) + Math.abs(playerPosition.y - gameEnemies.get(ec).getPosition().y)) < 8) {

				if (!gameEnemies.get(ec).destroyAnimationRunning()){
					gamePlayer.setHitpoints(gamePlayer.getHitpoints() - 5);

				}
				gameEnemies.get(ec).destroy();

			};

			List<EnemyProjectile> enemyProjectiles = gameEnemies.get(ec).getProjectiles();
			for (int i = 0; i < enemyProjectiles.size(); i++) {
				EnemyProjectile p  = enemyProjectiles.get(i);
				if (p.getPosition().y < 0 || p.getPosition().y > 512) {
					enemyProjectiles.remove(i);
				}
				else {
					if (gamePlayer.doesHit(p.getPosition())) {
						gamePlayer.setHitpoints(gamePlayer.getHitpoints() - 2);
						enemyProjectiles.remove(i);
					}
					for (Asteroid a : gameAsteroids) {
						if (a.doesHit(p.getPosition())) {
							if (a.getHitpoints() > 0) {
								a.setHitpoints(a.getHitpoints() - 1);
							}
							else {
								a.destroy();
							}
						}
					}
				}
			}
		}
	}
	private void updateTemporaryBoost() {
		if (_temporaryBoostActive && System.currentTimeMillis() - _temporaryBoostTimer > BOOST_UPDATE_TIME) {
			if (System.currentTimeMillis() - _temporaryBoostStartTime < BOOST_DURATION_TIME) {
				gameDelta = _temporaryOriginalDelta + _temporaryDeltaOffset * ((_temporaryBoostStartTime + BOOST_DURATION_TIME - System.currentTimeMillis()) / (double) BOOST_DURATION_TIME);
				System.out.println(gameDelta);
			}
			else {
				gameDelta = _temporaryOriginalDelta;
				_temporaryBoostActive = false;
			}
			_temporaryBoostTimer = System.currentTimeMillis();
		}
	}
	private void cancelGame() {
		gamePlayer.setInvincible(false);
		gamePlayer.setHitpoints(0);
		try {
			Game.dataStorage.set(0, new Integer(gameScore));
		}
		catch (Exception e) {
			Game.dataStorage.add(new Integer(gameScore));
		}
		_slowdownCounter--;
		gameDelta = ((double) _slowdownCounter) / 500;

		_darkOverlayAlpha = (int) ((500 - (double) _slowdownCounter) / 500 * 255d);
		if (_slowdownCounter <= 0) {
			Game.stateManager.loadState(2);
		}
		System.out.println(_slowdownCounter);
	}
	private void temporaryBoost(int v) {
		if (!_temporaryBoostActive) {
			_temporaryBoostActive = true;
			_temporaryOriginalDelta = gameDelta;
			_temporaryBoostStartTime = System.currentTimeMillis();
			_temporaryDeltaOffset = v;
		}
	}
	@Override
	public void update() {
		// HIGHSCORE
		updateHighscore();
		// ASTEROIDEN SPAWN
		spawnAsteroids();
		// ORBIT SPAWN
		spawnOrbits();
		// PULSAR SPAWN
		spawnPulsar();
		// POWERUP SPAWN
		spawnPowerUps();
		// UPDATEN VON SPIELER-PROJEKTILEN
		handlePlayerProjectiles();
		// TESTEN VON KOLLISION MIT GEGNER UND GEGNER-PROJEKTILEN
		testEnemyCollision();
		// TESTEN VON KOLLISION MIT ASTEROIDEN
		testAsteroidCollision();
		// TESTEN VON KOLLISION MIT POWERUPS
		testPowerUpCollision();
		// KEYBOARD HANDLING
		handleInput();
		// AUFRÄUMEN
		cleanUp();
		// TEMPORÄREN BOOST UPDATEN
		updateTemporaryBoost();
		// BEI GAMEOVER HERUNTERFAHREN
		if (gamePlayer.getHitpoints() <= 0) {
			cancelGame();
		}
	}

	@Override
	public void keyDown(String msg) {
		switch (msg) {
		case "left":
			_leftButtonPressed = true;
			break;
		case "right":
			_rightButtonPressed = true;
			break;
		case "space":
			_spaceButtonPressed = true;
			break;
		case "up":
			temporaryBoost(3);
			break;
		case "down":
			temporaryBoost(-2);
			break;
		case "q":
			gamePlayer.setInvincible(!gamePlayer.getInvincible());
			break;
		case "w":
			PowerUp p = new PowerUp();
			p.setPosition(new Point(randomValueGenerator.nextInt(256), 0));
			gamePowerUps.add(p);
			break;
		case "e":
			SmallOrbiter o = new SmallOrbiter();
			o.setPosition(new Point(randomValueGenerator.nextInt(256), 0));
			gameEnemies.add(o);
			break;
		case "p":
			Pulsar pulsar = new Pulsar();
			pulsar.setPosition(new Point(randomValueGenerator.nextInt(224) + 16, 0));
			gameEnemies.add(pulsar);
			System.out.println("pulsar added");
			break;
		case "1":
			_playerProjectileSpawnAmount = 1;
			break;
		case "2":
			_playerProjectileSpawnAmount = 2;
			break;
		case "3":
			_playerProjectileSpawnAmount = 3;
			break;
		case "4":
			_playerProjectileSpawnAmount = 4;
			break;
		}
	}
	@Override
	public void keyUp(String msg) {
		switch (msg) {
		case "left":
			_leftButtonPressed = false;
			break;
		case "right":
			_rightButtonPressed = false;
			break;
		case "space":
			_spaceButtonPressed = false;
			break;
		}
	}

}
