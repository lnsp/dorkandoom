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
import de.mooxmirror.dorkandoom.entities.Enemy;
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
 * 
 * @author Mooxmirror
 * @version 1.0
 */
public class SingleplayerGameState implements GameState {
	final private int SCORE_COUNT_FREQUENZY = 200;
	final private int ASTEROID_SPAWN_FREQUENZY = 1000;
	final private int ORBIT_SPAWN_FREQUENZY = 3000;
	final private int POWERUP_SPAWN_FREQUENZY = 5000;
	final private int PULSAR_SPAWN_FREQUENZY = 3000;
	final private int PROJECTILE_SPAWN_FREQUENZY = 100;
	final private int BOOST_DURATION_TIME = 2000;
	final private int BOOST_UPDATE_TIME = 60;

	private Random mRng;

	private Player mPlayer;
	private List<Projectile> mProjectiles;
	private List<Asteroid> mAsteroids;
	private List<Enemy> mEnemies;
	private List<PowerUp> mPowerups;
	private BufferedImage mOverlaySprite;

	private int mScore = 0;
	private long mScoreTimer = 0;

	private float mDelta = 1;

	private boolean mRightKeyState;
	private boolean mLeftKeyState;
	private boolean mSpaceKeyState;
	private boolean mPlayerProjectileSwitch;
	private byte mPlayerProjectileAmount = 1;
	private byte mProjectileSource = 100;
	private float mProjectileReload = 100;

	private long mAsteroidTimer;
	private long mOrbitTimer;
	private long mPowerupTimer;
	private long mProjectileTimer;
	private long mPulsarTimer;

	private int mSlowdownCounter = 500;
	private long mCleanupTimer;
	private boolean mBoost;
	private long mBoostStart;
	private float mBoostDeltaOffset;
	private float mBoostDeltaOriginal;
	private long mBoostTimer;

	private int mOverlayAlpha;
	private int mOverlayTimer;

	private boolean mPaused;

	public SingleplayerGameState() {
		init();
	}

	@Override
	public void init() {
		mRng = new Random();
		mPlayer = new Player(128, 418);
		mProjectiles = new ArrayList<Projectile>();
		mAsteroids = new ArrayList<Asteroid>();
		mEnemies = new ArrayList<Enemy>();
		mPowerups = new ArrayList<PowerUp>();
		try {
			mOverlaySprite = ImageIO.read(new File("res/images/hud/overlay.png"));
			System.out.println("Overlay succesfully loaded.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		mPlayer.drawEntity(g2d);

		for (Projectile p : mProjectiles) {
			p.drawProjectile(g2d);
		}
		for (Asteroid p : mAsteroids) {
			p.drawEntity(g2d);
		}
		for (Enemy e : mEnemies) {
			e.drawEntity(g2d);
		}
		for (PowerUp p : mPowerups) {
			p.drawEntity(g2d);
		}

		int life = mPlayer.getHitpoints();
		int calcBarLife = life * 2;
		int calcBarShots = (int) mProjectileReload * 2;

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, 256, 32);
		if (!mPlayer.getInvincible()) {
			g2d.setColor(Color.GREEN);
		} else {
			g2d.setColor(Color.WHITE);
		}
		g2d.fillRect(5, 5, (int) calcBarLife, 10);
		if (!mPlayer.getInvincible()) {
			g2d.setColor(Color.RED);
		} else {
			g2d.setColor(Color.WHITE);
		}
		g2d.fillRect(5, 19, (int) calcBarShots, 10);

		g2d.drawImage(mOverlaySprite, 0, 0, 256, 512, null);
		g2d.setColor(Color.WHITE);

		String highscoreString = Integer.toString(mScore) + " Meter";
		String lifeString = Integer.toString(mPlayer.getHitpoints()) + " / 100";
		String shotsString = Float.toString(mProjectileReload) + " / 100";

		if (mPlayer.getInvincible()) {
			lifeString = "Infinite!";
			shotsString = "Infinite!";
		}
		g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
		FontMetrics fm = g2d.getFontMetrics();

		g2d.drawString(highscoreString, 128 - (fm.stringWidth(highscoreString) / 2), 500);
		g2d.drawString(shotsString, 250 - (fm.stringWidth(shotsString)), 26);
		g2d.drawString(lifeString, 250 - (fm.stringWidth(lifeString)), 14);

		g2d.setColor(new Color(0, 0, 0, mOverlayAlpha));
		g2d.fillRect(0, 0, 256, 512);
	}

	private void updateHighscore() {
		if (System.currentTimeMillis() - mScoreTimer > SCORE_COUNT_FREQUENZY / mDelta) {
			mScore += 1;
			mScoreTimer = System.currentTimeMillis();
		}
	}

	private void spawnAsteroids() {
		if (System.currentTimeMillis()
				- mAsteroidTimer > (mRng.nextInt(ASTEROID_SPAWN_FREQUENZY) + ASTEROID_SPAWN_FREQUENZY) / mDelta) {
			Asteroid a = new Asteroid();
			a.setPosition(new Point(mRng.nextInt(224) + 16, 0));
			mAsteroids.add(a);
			mAsteroidTimer = System.currentTimeMillis();
		}
	}

	private void spawnOrbits() {
		if (System.currentTimeMillis() - mOrbitTimer > (mRng.nextInt(ORBIT_SPAWN_FREQUENZY) + ORBIT_SPAWN_FREQUENZY)
				/ mDelta) {
			SmallOrbiter smallOrbiter = new SmallOrbiter();
			smallOrbiter.setPosition(new Point(mRng.nextInt(224) + 16, 0));
			mEnemies.add(smallOrbiter);
			mOrbitTimer = System.currentTimeMillis();
		}
	}

	private void spawnPulsar() {
		if (System.currentTimeMillis() - mPulsarTimer > (mRng.nextInt(PULSAR_SPAWN_FREQUENZY) + PULSAR_SPAWN_FREQUENZY)
				/ mDelta) {
			Pulsar pulsar = new Pulsar();
			pulsar.setPosition(new Point(mRng.nextInt(224) + 16, 0));
			mEnemies.add(pulsar);
			mPulsarTimer = System.currentTimeMillis();
		}
	}

	private void spawnPowerUps() {
		if (System.currentTimeMillis()
				- mPowerupTimer > (mRng.nextInt(POWERUP_SPAWN_FREQUENZY) + POWERUP_SPAWN_FREQUENZY) / mDelta) {
			PowerUp p = new PowerUp();
			p.setPowerUpEffect(mRng.nextInt(6));
			p.setPosition(new Point(mRng.nextInt(224) + 16, 0));
			mPowerups.add(p);
			mPowerupTimer = System.currentTimeMillis();
		}
	}

	private void handleInput() {
		Point playerPosition = mPlayer.getPosition();
		if (mLeftKeyState) {
			playerPosition.x -= mPlayer.getHorizontalSpeed();
			if (playerPosition.x < 32)
				playerPosition.x = 32;
		}
		if (mRightKeyState) {
			playerPosition.x += mPlayer.getHorizontalSpeed();
			if (playerPosition.x > 224)
				playerPosition.x = 224;
		}
		if (mSpaceKeyState) {
			if (System.currentTimeMillis() - mProjectileTimer > PROJECTILE_SPAWN_FREQUENZY) {
				if (mProjectileReload > 1) {
					if (mPlayerProjectileAmount == 1) {
						LaserProjectile p = null;
						if (mPlayerProjectileSwitch) {
							p = new LaserProjectile(playerPosition.x - 16, playerPosition.y - 20, -90);
						} else {
							p = new LaserProjectile(playerPosition.x + 16, playerPosition.y - 20, -90);
						}
						mProjectiles.add(p);
					} else {
						List<Integer> angles = new ArrayList<Integer>();
						switch (mPlayerProjectileAmount) {
						case 2:
							angles.add(-100);
							angles.add(-80);
							break;
						case 3:
							angles.add(-105);
							angles.add(-90);
							angles.add(-75);
							break;
						case 4:
							angles.add(-105);
							angles.add(-95);
							angles.add(-85);
							angles.add(-75);
							break;
						}
						for (Integer m : angles) {
							LaserProjectile p = null;
							if (mPlayerProjectileSwitch) {
								p = new LaserProjectile(playerPosition.x - 13 - (angles.size() * 2),
										playerPosition.y - 20, m);
							} else {
								p = new LaserProjectile(playerPosition.x + 13 + (angles.size() * 2),
										playerPosition.y - 20, m);
							}
							mProjectiles.add(p);
						}
						mProjectileSource--;

						if (mProjectileSource <= 0) {
							mPlayerProjectileAmount = 1;
							mProjectileSource = 0;
						}
					}

					mPlayerProjectileSwitch = !mPlayerProjectileSwitch;
				} else {
					mSpaceKeyState = false;
				}
				mProjectileTimer = System.currentTimeMillis();
				if (!mPlayer.getInvincible())
					mProjectileReload -= 1;
			}
		} else {
			mProjectileReload += 0.5;
			if (mProjectileReload > 100)
				mProjectileReload = 100;
		}
		mPlayer.setPosition(playerPosition);
	}

	private void handlePlayerProjectiles() {
		for (int i = 0; i < mProjectiles.size(); i++) {
			mProjectiles.get(i).update(mDelta);
			if (mProjectiles.get(i).getPosition().y < 0) {
				mProjectiles.remove(i);
			} else {
				try {
					for (Asteroid a : mAsteroids) {
						if (a.doesHit(mProjectiles.get(i).getPosition()) && !a.destroyAnimationRunning()) {
							mProjectiles.remove(i);

							if (a.getHitpoints() > 0) {
								a.setHitpoints(a.getHitpoints() - 1);

							} else {
								a.destroy();
							}
						}

					}
					for (Enemy e : mEnemies) {
						if (e.doesHit(mProjectiles.get(i).getPosition()) && !e.destroyAnimationRunning()) {
							mProjectiles.remove(i);

							if (e.getHitpoints() > 0) {
								e.setHitpoints(e.getHitpoints() - 1);

							} else {
								e.destroy();

							}
						}
					}
				} catch (Exception e) {
				}

			}

		}
	}

	private void testAsteroidCollision() {
		Point playerPosition = mPlayer.getPosition();
		for (Asteroid a : mAsteroids) {
			a.updateEntity(mDelta);
			if (Math.sqrt(Math.abs(playerPosition.x - a.getPosition().x)
					+ Math.abs(playerPosition.y - a.getPosition().y)) < 8) {
				if (!a.destroyAnimationRunning() && !a.destroyAnimationDone()) {
					a.destroy();
					mPlayer.setHitpoints(mPlayer.getHitpoints() - 10);
				}
			}

		}
	}

	private void testPowerUpCollision() {
		Point playerPosition = mPlayer.getPosition();
		for (int i = 0; i < mPowerups.size(); i++) {
			PowerUp p = mPowerups.get(i);
			p.updateEntity(mDelta);
			if (p.doesHit(playerPosition)) {
				int powerupEffect = p.getPowerUpEffect();
				if (powerupEffect == PowerUp.BOOST_FASTER) {
					if (mDelta < 5) {
						mDelta += 0.5;
					}
				} else if (powerupEffect == PowerUp.BOOST_SLOWER) {
					if (mDelta > 0.5) {
						mDelta -= 0.5;
					}
				} else if (powerupEffect == PowerUp.DOUBLE_SHOT) {
					if (!(mPlayerProjectileAmount > 2))
						mPlayerProjectileAmount = 2;
					mProjectileSource += 50;
				} else if (powerupEffect == PowerUp.TRIPLE_SHOT) {
					if (!(mPlayerProjectileAmount > 3))
						mPlayerProjectileAmount = 3;
					mProjectileSource += 50;
				} else if (powerupEffect == PowerUp.QUADRO_SHOT) {
					mPlayerProjectileAmount = 4;
					mProjectileSource += 50;
				} else if (powerupEffect == PowerUp.HEALTH_RESTORE) {
					mPlayer.setHitpoints(mPlayer.getMaxHitpoints());
				}
				mPowerups.remove(i);
			}
		}
	}

	private void cleanUp() {
		if (System.currentTimeMillis() - mCleanupTimer > 30) {
			for (int a = 0; a < mAsteroids.size(); a++) {
				if (mAsteroids.get(a).getPosition().y < 0 || mAsteroids.get(a).getPosition().y > 512
						|| mAsteroids.get(a).destroyAnimationDone()) {
					mAsteroids.remove(a);
				}
			}
			for (int e = 0; e < mEnemies.size(); e++) {
				if (mEnemies.get(e).getPosition().y < 0 || mEnemies.get(e).getPosition().y > 512
						|| mEnemies.get(e).destroyAnimationDone()) {
					mEnemies.remove(e);
				}
			}
			for (int q = 0; q < mPowerups.size(); q++) {
				if (mPowerups.get(q).getPosition().y < 0 || mPowerups.get(q).getPosition().y > 512
						|| mPowerups.get(q).destroyAnimationDone()) {
					mPowerups.remove(q);
				}
			}
			mCleanupTimer = System.currentTimeMillis();
		}
	}

	private void testEnemyCollision() {
		Point playerPosition = mPlayer.getPosition();
		for (int ec = 0; ec < mEnemies.size(); ec++) {
			mEnemies.get(ec).updateEntity(mDelta);

			if (Math.sqrt(Math.abs(playerPosition.x - mEnemies.get(ec).getPosition().x)
					+ Math.abs(playerPosition.y - mEnemies.get(ec).getPosition().y)) < 16) {
				mEnemies.get(ec).shoot(mDelta);
			}
			if (Math.sqrt(Math.abs(playerPosition.x - mEnemies.get(ec).getPosition().x)
					+ Math.abs(playerPosition.y - mEnemies.get(ec).getPosition().y)) < 8) {

				if (!mEnemies.get(ec).destroyAnimationRunning()) {
					mPlayer.setHitpoints(mPlayer.getHitpoints() - 5);

				}
				mEnemies.get(ec).destroy();

			}
			;

			List<EnemyProjectile> enemyProjectiles = mEnemies.get(ec).getProjectiles();
			for (int i = 0; i < enemyProjectiles.size(); i++) {
				EnemyProjectile p = enemyProjectiles.get(i);
				if (p.getPosition().y < 0 || p.getPosition().y > 512) {
					enemyProjectiles.remove(i);
				} else {
					if (mPlayer.doesHit(p.getPosition())) {
						mPlayer.setHitpoints(mPlayer.getHitpoints() - 2);
						enemyProjectiles.remove(i);
					}
					for (Asteroid a : mAsteroids) {
						if (a.doesHit(p.getPosition())) {
							if (a.getHitpoints() > 0) {
								a.setHitpoints(a.getHitpoints() - 1);
							} else {
								a.destroy();
							}
						}
					}
				}
			}
		}
	}

	private void updateTemporaryBoost() {
		if (mBoost && System.currentTimeMillis() - mBoostTimer > BOOST_UPDATE_TIME) {
			if (System.currentTimeMillis() - mBoostStart < BOOST_DURATION_TIME) {
				mDelta = mBoostDeltaOriginal
						+ mBoostDeltaOffset * ((mBoostStart + BOOST_DURATION_TIME - System.currentTimeMillis())
								/ (float) BOOST_DURATION_TIME);
				System.out.println(mDelta);
			} else {
				mDelta = mBoostDeltaOriginal;
				mBoost = false;
			}
			mBoostTimer = System.currentTimeMillis();
		}
	}

	private void cancelGame() {
		mPlayer.setInvincible(false);
		mPlayer.setHitpoints(0);
		try {
			Game.dataStorage.set(0, new Integer(mScore));
		} catch (Exception e) {
			Game.dataStorage.add(new Integer(mScore));
		}
		mSlowdownCounter--;
		mDelta = mSlowdownCounter / 500f;

		mOverlayAlpha = (int) ((500f - mSlowdownCounter) / 500f * 255f);
		if (mSlowdownCounter <= 0) {
			Game.getStates().load(2);
		}
		System.out.println(mSlowdownCounter);
	}

	private void temporaryBoost(int v) {
		if (!mBoost) {
			mBoost = true;
			mBoostDeltaOriginal = mDelta;
			mBoostStart = System.currentTimeMillis();
			mBoostDeltaOffset = v;
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
		if (mPlayer.getHitpoints() <= 0) {
			cancelGame();
		}
	}

	@Override
	public void keyDown(String msg) {
		switch (msg) {
		case "left":
			mLeftKeyState = true;
			break;
		case "right":
			mRightKeyState = true;
			break;
		case "space":
			mSpaceKeyState = true;
			break;
		case "up":
			temporaryBoost(3);
			break;
		case "down":
			temporaryBoost(-2);
			break;
		case "q":
			mPlayer.setInvincible(!mPlayer.getInvincible());
			break;
		case "w":
			PowerUp p = new PowerUp();
			p.setPosition(new Point(mRng.nextInt(256), 0));
			mPowerups.add(p);
			break;
		case "e":
			SmallOrbiter o = new SmallOrbiter();
			o.setPosition(new Point(mRng.nextInt(256), 0));
			mEnemies.add(o);
			break;
		case "p":
			Pulsar pulsar = new Pulsar();
			pulsar.setPosition(new Point(mRng.nextInt(224) + 16, 0));
			mEnemies.add(pulsar);
			System.out.println("pulsar added");
			break;
		case "1":
			mPlayerProjectileAmount = 1;
			break;
		case "2":
			mPlayerProjectileAmount = 2;
			break;
		case "3":
			mPlayerProjectileAmount = 3;
			break;
		case "4":
			mPlayerProjectileAmount = 4;
			break;
		}
	}

	@Override
	public void keyUp(String msg) {
		switch (msg) {
		case "left":
			mLeftKeyState = false;
			break;
		case "right":
			mRightKeyState = false;
			break;
		case "space":
			mSpaceKeyState = false;
			break;
		}
	}

}
