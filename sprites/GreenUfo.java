import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GreenUfo implements DisplayableSprite {

	private static Image[] rotatedImages = new Image[360];
	private static Image image;
	private double centerX = 0;
	private double centerY = 0;
	private double width = 64;
	private double height = 64;
	private boolean dispose = false;
	private Image rotatedImage;
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

	private double ACCELERATION = 10000;
	private final double VELOCITY = 40000;
	private double ROTATION_SPEED = 150; // degrees per second
	private int currentAngle = 0;
	private int currentImageAngle = 0;

	public GreenUfo(double centerX, double centerY, double height, double width) {
		this(centerX, centerY);

		this.height = height;
		this.width = width;
	}

	public GreenUfo(double centerX, double centerY, double framesPerSecond) {

		this.centerX = centerX;
		this.centerY = centerY;

		this.framesPerSecond = framesPerSecond;
		this.milliSecondsPerFrame = 1000 / framesPerSecond;
		long startTime = System.currentTimeMillis();

		/*
		 * Loading of the images into an array. The image files are named in sequence by
		 * the online tool that split the animated GIF, and String formatting is used to
		 * re-create the correct file name. Note the use of a static array to store the
		 * images... this ensures that only a single copy of each rotated image is
		 * stored, even if there are multiple instances of this sprite.
		 */
		if (framesLoaded == false) {
			for (int frame = 0; frame < FRAMES; frame++) {
				String filename = "res/ufo_green/sprite_" + String.format("%01d", frame) + ".png";
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
//			System.exit(0);

		}
	}

	public GreenUfo(double centerX, double centerY) {
		this(centerX, centerY, 3);

//		Image image = null;
//		try {
//			image = ImageIO.read(new File("res/ufo_green/sprite_0.png"));
//		}
//		catch (IOException e) {
//			System.out.print(e.toString());
//		}
//
//		if (image != null) {
//			for (int i = 0; i < 360; i++) {
//				rotatedImages[i] = ImageRotator.rotate(image, i);			
//			}
//		}		
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

		// LEFT
		if (keyboard.keyDown(37)) {
			currentAngle -= (ROTATION_SPEED * (actual_delta_time * 0.001));
		}
		// UP
		if (keyboard.keyDown(38)) {
			double angleInRadians = Math.toRadians(currentAngle);
			velocityX += Math.cos(angleInRadians) * ACCELERATION * actual_delta_time * 0.001;
			velocityY += Math.sin(angleInRadians) * ACCELERATION * actual_delta_time * 0.001;
			// RIGHT
		}
		if (keyboard.keyDown(39)) {
			currentAngle += (ROTATION_SPEED * (actual_delta_time * 0.001));
		}
		// DOWN
		if (keyboard.keyDown(40)) {
			double angleInRadians = Math.toRadians(currentAngle);
			velocityX -= Math.cos(angleInRadians) * ACCELERATION * actual_delta_time * 0.001;
			velocityY -= Math.sin(angleInRadians) * ACCELERATION * actual_delta_time * 0.001;
		}
		if (keyboard.keyDown(32)) {
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

		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
		boolean collidingBarrierY = checkCollisionWithBarrier(universe.getSprites(), 0, deltaY);

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

	private boolean checkCollisionWithBarrier(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {

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

	public void shoot(Universe universe) {

		if (reloadTime <= 0) {
			double currentVelocity = Math.sqrt((velocityX * velocityX) + (velocityY * velocityY));
			double bulletVelocity = 750; // + currentVelocity;
			double ratio = (bulletVelocity / currentVelocity);
//			 = ratio * velocityX + velocityX;
//			double bulletVelocityY = ratio * velocityY + velocityY;
			double angleInRadians = Math.toRadians(currentAngle);
			double bulletVelocityX = Math.cos(angleInRadians) * bulletVelocity + velocityX;
			double bulletVelocityY = Math.sin(angleInRadians) * bulletVelocity + velocityY;

			double bulletCurrentX = this.getCenterX();
			double bulletCurrentY = this.getCenterY();

			bullet_sprite bullet = new bullet_sprite(bulletCurrentX, bulletCurrentY, bulletVelocityX, bulletVelocityY);
			universe.getSprites().add(bullet);
			reloadTime = 100;

		}
	}

}
