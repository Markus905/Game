import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class bullet_sprite implements DisplayableSprite {

	private final int WIDTH = 20;
	private final int HEIGHT = 20;
	
	
	private double accelerationX = 0;          			//PIXELS PER SECOND PER SECOND 
	private double accelerationY = 0;          			//PIXELS PER SECOND PER SECOND 
	private long lifeTime = 1000;
	
	private Image image;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;	
	private double velocityX = 0;
	private double velocityY = 0;
	
	public bullet_sprite(double centerX, double centerY, double velocityX, double velocityY, String filepath) {

		this.centerX = centerX;
		this.centerY = centerY;
		this.width = WIDTH;
		this.height = HEIGHT;

		this.velocityX = velocityX;
		this.velocityY = velocityY;
		
		if (image == null) {
			try {
				image = ImageIO.read(new File(filepath));
			}
			catch (IOException e) {
				System.err.println(e.toString());
			}
		}
		
	}
	public Image getImage() {
		return image;
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

	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}


	public void update(Universe universe, long actual_delta_time) {
	    double movement_x = (this.velocityX * actual_delta_time * 0.001);
	    double movement_y = (this.velocityY * actual_delta_time * 0.001);
	    
	    this.centerX += movement_x;
	    this.centerY += movement_y;

	    lifeTime -= actual_delta_time;
	    if (lifeTime < 0) {
	    	this.dispose = true;
	    }	    			
	
	}	
}
