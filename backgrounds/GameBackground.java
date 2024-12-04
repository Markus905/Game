import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameBackground implements Background {

    private Image background;
    private int backgroundWidth = 0;
    private int backgroundHeight = 0;

    public GameBackground() {
    	try {
    		this.background = ImageIO.read(new File("res/SpaceBG.png"));
    		backgroundWidth = background.getWidth(null);
    		backgroundHeight = background.getHeight(null);
    	}
    	catch (IOException e) {
    		//System.out.println(e.toString());
    	}		
    }
	
	public Tile getTile(int col, int row) {
		int x = (col * backgroundWidth);
		int y = (row * backgroundHeight);
		Tile newTile = null;
		
		newTile = new Tile(background, x, y, backgroundWidth, backgroundHeight, false);
			
		return newTile;
	}
	
	public int getCol(double x) {
		int col = 0;
		if (backgroundWidth != 0) {
			col = (int) (x / backgroundWidth);
			if (x < 0) {
				return col - 1;
			}
			else {
				return col;
			}
		}
		else {
			return 0;
		}
	}
	
	public int getRow(double y) {
		int row = 0;
		
		if (backgroundHeight != 0) {
			row = (int) (y / backgroundHeight);
			if (y < 0) {
				return row - 1;
			}
			else {
				return row;
			}
		}
		else {
			return 0;
		}
	}

	@Override
	public double getShiftX() {
		return 0;
	}

	@Override
	public double getShiftY() {
		return 0;
	}

	@Override
	public void setShiftX(double shiftX) {
	}

	@Override
	public void setShiftY(double shiftY) {
	}

	@Override
	public void update(Universe universe, long actual_delta_time) {
		// TODO Auto-generated method stub
		
	}
	
}


