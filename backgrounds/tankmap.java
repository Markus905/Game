import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class tankmap implements Background {
	protected static int TILE_WIDTH = 100;
	protected static int TILE_HEIGHT = 100;
	private int maxCols = 0;
	private int maxRows = 0;

	private Image wall;
	
	private int map[][] = new int[][] { 
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		{1,1,1,1,0,0,0,0,0,0,0,1,1,1,1},
        {1,0,0,0,0,1,0,1,1,1,0,0,0,1,1},
        {1,0,1,1,0,0,0,0,0,0,0,1,0,0,1},
        {1,0,0,0,0,1,1,1,0,1,0,1,1,0,1},
        {1,1,0,1,0,1,1,0,0,0,0,1,1,0,1},
        {1,1,0,1,0,0,0,0,1,0,1,0,0,0,1},
        {1,1,0,1,0,1,1,1,1,0,1,1,1,0,1},
        {1,1,0,1,0,1,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };


	public tankmap() {
		try {
			this.wall = ImageIO.read(new File("res/Barrier.png"));
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		maxRows = map.length - 1;
		maxCols = map[0].length - 1;
	}

	public Tile getTile(int col, int row) {
		Image image = null;
		if (row < 0 || row > maxRows || col < 0 || col > maxCols) {
			image = null;
		} else if (map[row][col] == 1) {
			image = wall;
		} else {
			image = null;
		}
		int x = (col * TILE_WIDTH);
		int y = (row * TILE_HEIGHT);
		Tile newTile = new Tile(image, x, y, TILE_WIDTH, TILE_HEIGHT, false);
		return newTile;
	}

	public int getCol(double x) {
		int col = 0;
		if (TILE_WIDTH != 0) {
			col = (int) (x / TILE_WIDTH);
			if (x < 0) {
				return col - 1;
			} else {
				return col;
			}
		} else {
			return 0;
		}
	}

	@Override
	public int getRow(double y) {
		int row = 0;

		if (TILE_HEIGHT != 0) {
			row = (int) (y / TILE_HEIGHT);
			if (y < 0) {
				return row - 1;
			} else {
				return row;
			}
		} else {
			return 0;
		}
	}

	public double getShiftX() {
		return 0;
	}

	public double getShiftY() {
		return 0;
	}

	public void setShiftX(double shiftX) {
	}

	public void setShiftY(double shiftY) {
	}

	public ArrayList<DisplayableSprite> getBarriers() {
		ArrayList<DisplayableSprite> barriers = new ArrayList<DisplayableSprite>();
		for (int col = 0; col < map[0].length; col++) {
			for (int row = 0; row < map.length; row++) {
				if (map[row][col] == 1 ) {
					barriers.add(new BarrierSprite(col * TILE_WIDTH, row * TILE_HEIGHT, (col + 1) * TILE_WIDTH,
							(row + 1) * TILE_HEIGHT, false));
				}
			}
		}
		return barriers;
	}

	@Override
	public void update(Universe universe, long actual_delta_time) {
		
		
	}
	
}	

