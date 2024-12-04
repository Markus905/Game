import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SimpleSprite implements DisplayableSprite {
	
	
	private static Image[] rotatedImages = new Image[360];
	private static Image image;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;	
	private Image rotatedImage;
	
	private double ACCELERATION = 400;    
	private final double VELOCITY = 200;
	private double ROTATION_SPEED = 150;	//degrees per second	
	private double currentAngle = 0;
	private int currentImageAngle = 0;

	public SimpleSprite(double centerX, double centerY, double height, double width) {
		this(centerX, centerY);
		
		this.height = height;
		this.width = width;
	}

	
	public SimpleSprite(double centerX, double centerY) {

		this.centerX = centerX;
		this.centerY = centerY;
		
		Image image = null;
		try {
			image = ImageIO.read(new File("res/simple-sprite.png"));
		}
		catch (IOException e) {
			System.out.print(e.toString());
		}

		if (image != null) {
			for (int i = 0; i < 360; i++) {
				rotatedImages[i] = ImageRotator.rotate(image, i);			
			}
		}		
	}

	public Image getImage() {
		return rotatedImages[(int)currentAngle];
	}
	
	
	//DISPLAYABLE
	
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
		
		

		//LEFT	
		if (keyboard.keyDown(37)) {
			currentAngle -= (ROTATION_SPEED * (actual_delta_time * 0.001));
		}
		//UP
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
			velocityY += VELOCITY;			
		}
		if (currentAngle >= 360) {
	    	currentAngle -= 360;
	    }
	    if (currentAngle < 0) {
	    	currentAngle += 360;
	    }	
	    
	    currentAngle %= 360;

		double deltaX = actual_delta_time * 0.001 * velocityX;
		double deltaY = actual_delta_time * 0.001 * velocityY;
		
		
		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
		boolean collidingBarrierY = checkCollisionWithBarrier(universe.getSprites(), 0, deltaY);
    	
		if(!collidingBarrierY) {
			this.centerY += deltaY;
		}
		if(!collidingBarrierX) {
			this.centerX += deltaX;
		}

	}


	@Override
	public void setDispose(boolean dispose) {
		this.dispose = true;
	}
	
	private boolean checkCollisionWithBarrier(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {

		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof BarrierSprite) {
				if (CollisionDetection.overlaps(this.getMinX() + deltaX, this.getMinY() + deltaY, 
						this.getMaxX()  + deltaX, this.getMaxY() + deltaY, 
						sprite.getMinX(),sprite.getMinY(), 
						sprite.getMaxX(), sprite.getMaxY())) {
					colliding = true;
					break;					
				}
			}
		}
		return colliding;
	}

}
