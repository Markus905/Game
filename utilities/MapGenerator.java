import java.util.Random;

public class MapGenerator {

    private int[][] maze;
    private int[][] visited;
    private int rows, cols;
    private Random rand;

    public MapGenerator(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.maze = new int[rows][cols];
        this.visited = new int[rows][cols];
        this.rand = new Random();

        // Initialize maze with walls (1's)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 1;
                visited[i][j] = 0;
            }
        }
    }

    // Main function to generate the maze
    public void generateMaze(int xStartPos,int yStartPos) {
        // Starts recursion
        carvePath(xStartPos, yStartPos);
    }

    // Recursive function to carve a path
    private void carvePath(int x, int y) {
        // Directions: right, down, left, up
        int[] direction = {0, 1, 0, -1, 1, 0, -1, 0}; // (dx, dy)

       shuffleArray(direction, rand);

        // Carve out the current cell
        maze[x][y] = 0;
        visited[x][y] = 0;

       
        for (int i = 0; i < 2; i++) {
            int nx = x + direction[i * 2];
            int ny = y + direction[i * 2 + 1];

            // Ensure the new position is within bounds and is a wall
            if (nx > 0 && ny > 0 && nx < rows - 1 && ny < cols - 1 && maze[nx][ny] == 1 && visited[nx][ny] == 0) {
                    carvePath(nx, ny);
      
            }
        }
    }
    
    public static void shuffleArray(int[] arr, Random rand) {
    	for (int i = 0; i < 4; i++) {
            // Randomly swap directions
            int j = rand.nextInt(4);
            int temp = arr[i * 2];
            arr[i * 2] = arr[j * 2];
            arr[j * 2] = temp;
            temp = arr[i * 2 + 1];
            arr[i * 2 + 1] = arr[j * 2 + 1];
            arr[j * 2 + 1] = temp;
        }
    }
}
