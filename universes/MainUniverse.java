import java.util.ArrayList;
import java.util.Random;

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
		return 800;
	}

	public double getYCenter() {
		return 800;
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
