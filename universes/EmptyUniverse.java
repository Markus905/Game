import java.util.ArrayList;

public class EmptyUniverse implements Universe {

	private boolean complete = false;
	private boolean gameOver = false;

	private Background background;
	private ArrayList<Background> backgrounds;

	
	ArrayList<DisplayableSprite> disposedSprites = new ArrayList<DisplayableSprite>();
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();

	private double xCenter;
	private double yCenter;

	public EmptyUniverse() {
		 ImageSprite title = new ImageSprite(740, 550, 1300, 1300, "res/gameover.jpg");
		backgrounds = new ArrayList<Background>();
		backgrounds.add(new GameBackground());
		backgrounds.add(background);
		sprites.add(title);




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
		return "marcus stinks, jack rules";
	}

	protected void disposeSprites() {

		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			if (sprite.getDispose() == true) {
				disposedSprites.add(sprite);
			}
		}
		for (int i = 0; i < disposedSprites.size(); i++) {
			DisplayableSprite sprite = disposedSprites.get(i);
			sprites.remove(sprite);
		}
		if (disposedSprites.size() > 0) {
			disposedSprites.clear();
		}
	}

	public void update(Animation animation, long actual_delta_time) {

		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, actual_delta_time);
		}
		disposeSprites();

	}

	@Override
	public void setKillCount() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getKillCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
