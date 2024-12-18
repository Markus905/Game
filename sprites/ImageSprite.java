import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageSprite implements DisplayableSprite {

	private Image image;
	private boolean visible = true;
	private double centerX = 0;
	private double centerY = 0;
	private double width = 1000;
	private double height = 200;
	private boolean dispose = false;	
	
	public ImageSprite(double centerX, double centerY, double width, double height, String image) {
		
		if (this.image == null && visible) {
			try {
				this.image = ImageIO.read(new File(image));
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}		
		}
		
		this.width = width;
		this.height = height;
		this.centerX = centerX;
		this.centerY = centerY;

		
	}
	
	public boolean getIsAtExit() {
		return false;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	//DISPLAYABLE
	
	public boolean getVisible() {
		return this.visible;
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
	
	public int getLives() {
		return 0;
	}

	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}


	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
			
	}

	@Override
	public void update(Universe universe, long actual_delta_time) {
		// TODO Auto-generated method stub
		
	}


}
