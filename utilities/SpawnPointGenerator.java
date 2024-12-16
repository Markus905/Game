public class SpawnPointGenerator {
	private int[][] map;
	private int tileSize;
	
	public SpawnPointGenerator(int[][] map, int tileSize ) {
		this.map = map;
		this.tileSize = tileSize;
	}
	
	public int[] generateSpawnPoint(int xStartingPoint, int yStartingPoint) {
		int[] coordinates = new int[2];
		
		for(int i = yStartingPoint; i < map.length; i++) {
			for(int j = xStartingPoint; j < map[i].length; j++) {
				//finds first empty square in map from starting point
				if (map[i][j] == 0) {
					//converts coordinates to units of tiles
					coordinates[0] = j * tileSize + 50;
					coordinates[1] = i * tileSize + 50;
					return coordinates;
				}
			}
		}
		return null;
	}
}
