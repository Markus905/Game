
import java.util.ArrayList;


public class TitleScreen implements Universe {
	
	
	
	private boolean complete = false;
	private double xCenter = 500;
	private double yCenter = 500;
	
	private Background background = null;
	private ArrayList<Background> backgrounds = null;
	
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private DisplayableSprite title = null;
	

	

	public TitleScreen() {
		
		title = new ImageSprite(0, 500, 2000, 2000, "res/Title.jpg");
	
		backgrounds = new ArrayList<Background>();
		backgrounds.add(background);
	
		sprites.add(title);
		

		

	}

	public double getScale() {
		return 0.5;
	}

	public double getXCenter() {
		return title.getCenterX();
	}
	
	public double getYCenter() {
		return yCenter;
	}

	public void setXCenter(double xCenter) {
		this.xCenter = xCenter;
	}

	public void setYCenter(double yCenter) {
		this.yCenter = yCenter;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = true;
	}
	
	
	public boolean getGameOver() {
		return false;
	}


	public ArrayList<DisplayableSprite> getSprites() {
		return sprites;
	}

	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}

	public void update(KeyboardInput keyboard, long actual_delta_time) {

		if (keyboard.keyDownOnce(27)) {
			complete = true;
		}
		
		
		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, actual_delta_time);
		}
		
	}
	
	public String toString() {
		return "";
	}

	@Override
	public void update(Animation animation, long actual_delta_time) {
		// TODO Auto-generated method stub
		
	}



}
