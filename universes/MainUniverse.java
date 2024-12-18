import java.util.ArrayList;
public class MainUniverse implements Universe {
	private boolean complete = false;
	private boolean gameOver = false;
	private Background background;
	private ArrayList<Background> backgrounds;
	
	
	private DisplayableSprite greenUfo;
	private DisplayableSprite purpleUfo;
	private DisplayableSprite redUfo;
	private DisplayableSprite yellowUfo;
	
	
	private SpawnPointGenerator spawnPointGenerator;
	
	ArrayList<DisplayableSprite> disposedSprites = new ArrayList<DisplayableSprite>();
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	
	private double xCenter;
	private double yCenter;
	
	private int killCount = 0;
	
	public MainUniverse() {
		background = new RandomlyGeneratedMap();
		backgrounds = new ArrayList<Background>();
		backgrounds.add(new GameBackground());
		backgrounds.add(background);
		ArrayList<DisplayableSprite> barriers = ((RandomlyGeneratedMap) background).getBarriers();
		
		spawnPointGenerator = new SpawnPointGenerator(((RandomlyGeneratedMap)this.background).getMap(), 100);
		
		int[] greenUfoSpawn = spawnPointGenerator.generateSpawnPoint(0 , 0);
		greenUfo = new GreenUfo(greenUfoSpawn[0], greenUfoSpawn[1]);
		
		int[] purpleUfoSpawn = spawnPointGenerator.generateSpawnPoint(4,0);
		purpleUfo = new PurpleUfo(purpleUfoSpawn[0], purpleUfoSpawn[0]);
		
		int[] redUfoSpawn = spawnPointGenerator.generateSpawnPoint(8,0);
		redUfo = new RedUfo(redUfoSpawn[0], redUfoSpawn[0]);
		
		int[] yellowUfoSpawn = spawnPointGenerator.generateSpawnPoint(12,0);
		yellowUfo = new YellowUfo(yellowUfoSpawn[0], yellowUfoSpawn[0]);
		
		sprites.add(greenUfo);
		sprites.add(purpleUfo);
		sprites.add(redUfo);
		sprites.add(yellowUfo);
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
		this.killCount++;
		
	}
	public int getKillCount() {
		return this.killCount;
	}
}
