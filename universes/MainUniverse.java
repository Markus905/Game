import java.util.ArrayList;
import java.util.Random;

public class MainUniverse implements Universe {
	private boolean complete = false;
	private Background background;
	private ArrayList<Background> backgrounds;

	private DisplayableSprite greenUfo;
	private DisplayableSprite purpleUfo;
	private DisplayableSprite redUfo;
	private DisplayableSprite yellowUfo;

	private SpawnPointGenerator spawnPointGenerator;

	ArrayList<DisplayableSprite> disposedSprites = new ArrayList<DisplayableSprite>();
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();

	private double xCenter = 800;
	private double yCenter = 800;
	
	private int[] killTracker = new int[4]; //tracks which one of the ufos is dead [red, yellow, green, purple]
	//0 == alive, 1 == dead

	public MainUniverse() {
		Random random = new Random();
	
		background = new RandomlyGeneratedMap();
		backgrounds = new ArrayList<Background>();
		backgrounds.add(new GameBackground());
		backgrounds.add(background);
		ArrayList<DisplayableSprite> barriers = ((RandomlyGeneratedMap) background).getBarriers();

		spawnPointGenerator = new SpawnPointGenerator(((RandomlyGeneratedMap) this.background).getMap(), 100);

		int[] greenUfoSpawn = spawnPointGenerator.generateSpawnPoint(random.nextInt(13), random.nextInt(13));
		greenUfo = new GreenUfo(greenUfoSpawn[0], greenUfoSpawn[1]);

		int[] purpleUfoSpawn = spawnPointGenerator.generateSpawnPoint(random.nextInt(13), random.nextInt(13));
		purpleUfo = new PurpleUfo(purpleUfoSpawn[0], purpleUfoSpawn[1]);

		int[] redUfoSpawn = spawnPointGenerator.generateSpawnPoint(random.nextInt(13), random.nextInt(13));
		redUfo = new RedUfo(redUfoSpawn[0], redUfoSpawn[1]);

		int[] yellowUfoSpawn = spawnPointGenerator.generateSpawnPoint(random.nextInt(13), random.nextInt(13));
		yellowUfo = new YellowUfo(yellowUfoSpawn[0], yellowUfoSpawn[1]);

		sprites.add(greenUfo);
		sprites.add(purpleUfo);
		sprites.add(redUfo);
		sprites.add(yellowUfo);
		sprites.addAll(barriers);

	}

	public double getScale() {
		return .65;
	}

	public double getXCenter() {
		return this.xCenter;
	}

	public double getYCenter() {
		return this.yCenter;
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

	@Override
	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}


	public ArrayList<DisplayableSprite> getSprites() {
		return sprites;
	}

	public String toString() {
		return "";

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

	
	public void setKillTracker(int index, int value) {
		this.killTracker[index] = value;
	}
	
	public int[] getKillTracker() {
		return this.killTracker;
	}
	
	public void gameOver() {
		if(this.killTracker[0] == 0) {
			//red is winner
			this.xCenter = redUfo.getCenterX();
			this.yCenter = redUfo.getCenterY();
		} else if(this.killTracker[1] == 0) {
			//yellow is winner
			this.xCenter = yellowUfo.getCenterX();
			this.yCenter = yellowUfo.getCenterY();
		} else if(this.killTracker[2] == 0) {
			//green is winner
			this.xCenter = greenUfo.getCenterX();
			this.yCenter = greenUfo.getCenterY();
		}else if(this.killTracker[3] == 0) {
			//purple is winner
			this.xCenter = purpleUfo.getCenterX();
			this.yCenter = purpleUfo.getCenterY();
		}
	}
}
