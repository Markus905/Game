import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class YellowUfo implements DisplayableSprite {

	private double centerX = 0;
	private double centerY = 0;
	private double width = 64;
	private double height = 64;
	private boolean dispose = false;
	private double reloadTime = 0;
	private double velocityX = 0;
	private double velocityY = 0;

	private int currentFrame = 0;
	private long elapsedTime = 0;
	private final static int FRAMES = 5;
	private double framesPerSecond = 3;
	private double milliSecondsPerFrame = 1000 / framesPerSecond;
	private static Image[] frames = new Image[FRAMES];
	private static boolean framesLoaded = false;

	private final int VELOCITY = 10000;
	private double ROTATION_SPEED = 150; // degrees per second
	private int currentAngle = 0;
	
	private boolean invincible = false;
	
	public YellowUfo(double centerX, double centerY, double height, double width) {
		this(centerX, centerY);

		this.height = height;
		this.width = width;
	}

	public YellowUfo(double centerX, double centerY, double framesPerSecond) {

		this.centerX = centerX;
		this.centerY = centerY;

		this.framesPerSecond = framesPerSecond;
		this.milliSecondsPerFrame = 1000 / framesPerSecond;

		if (framesLoaded == false) {
			for (int frame = 0; frame < FRAMES; frame++) {
				String filename = "res/ufo_yellow/yellowUfo_" + String.format("%01d", frame) + ".png";
				try {
					frames[frame] = ImageIO.read(new File(filename));
				} catch (IOException e) {
					System.err.println(e.toString());

				}
			}

			if (frames[0] != null) {
				framesLoaded = true;
			}

			System.out.println(frames.toString());

		}
	}

	public YellowUfo(double centerX, double centerY) {
		this(centerX, centerY, 3);	
	}

	public Image getImage() {
		return ImageRotator.rotate(frames[currentFrame], currentAngle);
	}

	// DISPLAYABLE

	public boolean getVisible() {
		return true;
	}

	public double getMinX() {
		return centerX - (width / 2);
	}

	public double getMaxX() {
		return centerX + (width / 2);
	}

	public double getMinY() {
		return centerY - (height / 2);
	}

	public double getMaxY() {
		return centerY + (height / 2);
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getCenterX() {
		return centerX;
	};

	public double getCenterY() {
		return centerY;
	};

	public boolean getDispose() {
		return dispose;
	}

	public void update(Universe universe, long actual_delta_time) {

		double velocityX = 0;
		double velocityY = 0;

		KeyboardInput keyboard = KeyboardInput.getKeyboard();

		// LEFT ( a)
		if (keyboard.keyDown(65)) {
			currentAngle -= (ROTATION_SPEED * (actual_delta_time * 0.001));
		}
		// UP (w)
		if (keyboard.keyDown(87)) {
			double angleInRadians = Math.toRadians(currentAngle);
			velocityX += Math.cos(angleInRadians) * VELOCITY * actual_delta_time * 0.001;
			velocityY += Math.sin(angleInRadians) * VELOCITY * actual_delta_time * 0.001;
			
		} // right (d)
		if (keyboard.keyDown(68)) {
			currentAngle += (ROTATION_SPEED * (actual_delta_time * 0.001));
		}
		// DOWN (s)
		if (keyboard.keyDown(83)) {
			double angleInRadians = Math.toRadians(currentAngle);
			velocityX -= Math.cos(angleInRadians) * VELOCITY * actual_delta_time * 0.001;
			velocityY -= Math.sin(angleInRadians) * VELOCITY * actual_delta_time * 0.001;
		} // (tab)
		if (keyboard.keyDown(20)) {
			shoot(universe);
		}
		if (currentAngle >= 360) {
			currentAngle -= 360;
		}
		if (currentAngle < 0) {
			currentAngle += 360;
		}

		currentAngle %= 360;
		reloadTime -= actual_delta_time;

		double deltaX = actual_delta_time * 0.001 * velocityX;
		double deltaY = actual_delta_time * 0.001 * velocityY;

		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0, universe);
		boolean collidingBarrierY = checkCollisionWithBarrier(universe.getSprites(), 0, deltaY, universe);
		
		if(!invincible) {
			this.checkCollisionWithBullet(universe.getSprites(), universe);
		}

		if (!collidingBarrierY) {
			this.centerY += deltaY;
		}
		if (!collidingBarrierX) {
			this.centerX += deltaX;
		}
		elapsedTime += actual_delta_time;
		long elapsedFrames = (long) (elapsedTime / milliSecondsPerFrame);
		currentFrame = (int) (elapsedFrames % FRAMES);

	}

	@Override
	public void setDispose(boolean dispose) {
		this.dispose = true;
	}

	private boolean checkCollisionWithBarrier(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY, Universe universe) {

		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof BarrierSprite) {
				if (CollisionDetection.overlaps(this.getMinX() + deltaX + 8, this.getMinY() + deltaY + 8,
						this.getMaxX() + deltaX - 8, this.getMaxY() + deltaY - 6, sprite.getMinX(), sprite.getMinY(),
						sprite.getMaxX(), sprite.getMaxY())) {
					colliding = true;
					break;
				}
			}
		}
		
	return colliding;	
	}
	
	private void checkCollisionWithBullet(ArrayList<DisplayableSprite> sprites, Universe universe) {
		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof bullet_sprite && (((bullet_sprite) sprite).getLifetime() < 7600)) {
				if (CollisionDetection.overlaps(this.getMinX(), this.getMinY(), this.getMaxX(), this.getMaxY(), sprite.getMinX(),sprite.getMinY(), sprite.getMaxX(), sprite.getMaxY())) {
					((MainUniverse)universe).setKillTracker(1, 1);//sets state on killTracker as dead
					this.dispose = true;		
				}
			}
		}
	}
	
	public void shoot(Universe universe) {

		if (reloadTime <= 0) {
			double bulletVelocity = 150; // + currentVelocity;

			double angleInRadians = Math.toRadians(currentAngle);
			double bulletVelocityX = Math.cos(angleInRadians) * bulletVelocity + velocityX;
			double bulletVelocityY = Math.sin(angleInRadians) * bulletVelocity + velocityY;

			double bulletCurrentX = this.getCenterX();
			double bulletCurrentY = this.getCenterY();

			bullet_sprite bullet = new bullet_sprite(bulletCurrentX, bulletCurrentY, bulletVelocityX, bulletVelocityY, "res/YellowLaser.png");
			universe.getSprites().add(bullet);
			reloadTime = 100;

		}
	}
	
	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}

}