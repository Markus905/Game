import java.util.ArrayList;

public class tankmaplayout implements Universe {

	private boolean complete = false;
	private boolean gameOver = false;

	private Background background = null;
	private ArrayList<Background> backgrounds = null; 
	
	
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	
	private double xCenter;
	private double yCenter;
	

	public tankmaplayout() {
		background = new tankmap();
		backgrounds = new ArrayList<Background>();
//		backgrounds.add(new GameBackground());
//		backgrounds.add(new MainBackground());
		backgrounds.add(background);

		ArrayList<DisplayableSprite> barriers = ((tankmap) background).getBarriers();
		

//		frog = new FrogSprite(LevelOneLayout.TILE_HEIGHT * 2, (LevelOneLayout.TILE_WIDTH * 2) + 400);
//		fish = new FishSprite(LevelOneLayout.TILE_HEIGHT * 2, (LevelOneLayout.TILE_WIDTH * 2) + 600);
		
		
		
//		sprites.add(frog);
//		sprites.add(fish);
		sprites.addAll(barriers);
	
	}

	public double getScale() {
		return 0.47;
	}

	public double getXCenter() {
		return xCenter;
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

	public void update(KeyboardInput keyboard, long actual_delta_time) {
		

		
		//can skip level by pressing ESC and 0 at the same time
//		if(keyboard.keyDown(27) && keyboard.keyDown(48)) {
//			complete = true;
//		}
		
		
//		if(frog.getLives() <= 0 || fish.getLives() <= 0 ) {
//			gameOver = true;
//			complete = true;
//		}
		
		
		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, actual_delta_time);
		}
	}

	public String toString() {
		return "game game yay !";
	}

	@Override
	public void update(Animation animation, long actual_delta_time) {
		// TODO Auto-generated method stub
		
	}

}
