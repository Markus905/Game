import java.util.ArrayList;

public class tankmaplayout implements Universe {

	private boolean complete = false;
	private boolean gameOver = false;

	private Background background;
	private ArrayList<Background> backgrounds; 
	
	private DisplayableSprite player;
	
	
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	
	private double xCenter;
	private double yCenter;
	

	public tankmaplayout() {
		background = new tankmap();
		backgrounds = new ArrayList<Background>();
		backgrounds.add(new GameBackground());
		backgrounds.add(background);

		ArrayList<DisplayableSprite> barriers = ((tankmap) background).getBarriers();
		

		player = new green_tank_sprite(300,250);
		
		sprites.add(player);

		sprites.addAll(barriers);
	
	}

	public double getScale() {
		return 0.81;
	}

	public double getXCenter() {
		return 750;
	}

	public double getYCenter() {
		return 550;
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
		complete = true;
	}
	
	public boolean getGameOver() {
		return gameOver;
	}
	
	
	@Override
	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}

	public DisplayableSprite getPlayer1() {
		return null;
	}
	

	public ArrayList<DisplayableSprite> getSprites() {
		return sprites;
	}

	public boolean centerOnPlayer() {
		return true;
	}
  

	public String toString() {
		return "game game yay !";
	}


	public void update(Animation animation, long actual_delta_time) {
		
		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, actual_delta_time);
		}
		
	}

}
