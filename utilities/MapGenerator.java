import java.util.*;

public class MapGenerator {
    // Define the size of the maze (including borders)
    private static final int WIDTH = 16;   // Width with border (original WIDTH + 2 for the border)
    private static final int HEIGHT = 16;  // Height with border (original HEIGHT + 2 for the border)

    private int[][] map; // The maze grid
    private boolean[][] visited; // Tracks the visited cells

    private static final int WALL = 1;
    private static final int PATH = 0;

    private static final int[] DX = {-1, 1, 0, 0}; // Directions for row movements (Up, Down, Left, Right)
    private static final int[] DY = {0, 0, -1, 1}; // Directions for column movements (Up, Down, Left, Right)

    // Constructor to initialize the maze
    public MapGenerator() {
        map = new int[HEIGHT][WIDTH];
        visited = new boolean[HEIGHT][WIDTH];

        // Initialize the maze with walls (including border)
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                map[i][j] = WALL;
            }
        }
    }

    // Generate the maze using recursive backtracking
    public void generateMap(int startX, int startY) {
        visited[startX][startY] = true;  // Mark the starting point as visited
        map[startX][startY] = PATH;     // Mark the starting point as a path

        // Randomly shuffle directions
        List<Integer> directions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            directions.add(i);
        }
        Collections.shuffle(directions);

        // Explore each of the 4 possible directions (Up, Down, Left, Right)
        for (int i = 0; i < directions.size(); i++) {
            int dir = directions.get(i);
            int newX = startX + DX[dir] * 2;
            int newY = startY + DY[dir] * 2;

            // Check if the new position is within bounds and not visited
            if (isValid(newX, newY)) {
                // Carve a path between the current cell and the new cell
                map[startX + DX[dir]][startY + DY[dir]] = PATH;

                // Recursively generate the maze from the new position
                generateMap(newX, newY);
            }
        }
    }

    // Check if the cell is within bounds and not visited
    private boolean isValid(int x, int y) {
        return x > 0 && x < HEIGHT - 1 && y > 0 && y < WIDTH - 1 && !visited[x][y];
    }

	public int[][] getMap() {
		return this.map;
	}

    
    
}
