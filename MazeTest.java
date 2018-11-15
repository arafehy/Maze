package sjsu.ArafehSmaluk.cs146.project2;

import org.junit.jupiter.api.Test;

/**
 * JUnit class to test the Maze class
 * 
 * @author Yazan Arafeh
 * @author Tim Smaluk
 */
class MazeTest {
	/**
	 * Test for a 4x4 maze
	 */
	@Test
	void maze4x4() {
		int dimension = 4;
		Maze perfectMaze = new Maze(dimension);
		perfectMaze.generatePerfectMaze();
		System.out.println(perfectMaze);
		
		Maze dfsSolvedMaze = perfectMaze.solveDFS();
		System.out.println("DFS Solution");
		System.out.println(dfsSolvedMaze);
		dfsSolvedMaze.labelSolutionPath();
		System.out.println(dfsSolvedMaze);
		
		Maze bfsSolvedMaze = perfectMaze.solveBFS();
		System.out.println("BFS Solution");
		System.out.println(bfsSolvedMaze);
		bfsSolvedMaze.labelSolutionPath();
		System.out.println(bfsSolvedMaze);
	}
	
	/**
	 * Test for a 5x5 maze
	 */
	@Test
	void maze5x5() {
		int dimension = 5;
		Maze perfectMaze = new Maze(dimension);
		perfectMaze.generatePerfectMaze();
		System.out.println(perfectMaze);
		
		Maze dfsSolvedMaze = perfectMaze.solveDFS();
		System.out.println("DFS Solution");
		System.out.println(dfsSolvedMaze);
		dfsSolvedMaze.labelSolutionPath();
		System.out.println(dfsSolvedMaze);
		
		Maze bfsSolvedMaze = perfectMaze.solveBFS();
		System.out.println("BFS Solution");
		System.out.println(bfsSolvedMaze);
		bfsSolvedMaze.labelSolutionPath();
		System.out.println(bfsSolvedMaze);
	}
	
	/**
	 * Test for a 6x6 maze
	 */
	@Test
	void maze6x6() {
		int dimension = 6 ;
		Maze perfectMaze = new Maze(dimension);
		perfectMaze.generatePerfectMaze();
		System.out.println(perfectMaze);
		
		Maze dfsSolvedMaze = perfectMaze.solveDFS();
		System.out.println("DFS Solution");
		System.out.println(dfsSolvedMaze);
		dfsSolvedMaze.labelSolutionPath();
		System.out.println(dfsSolvedMaze);
		
		Maze bfsSolvedMaze = perfectMaze.solveBFS();
		System.out.println("BFS Solution");
		System.out.println(bfsSolvedMaze);
		bfsSolvedMaze.labelSolutionPath();
		System.out.println(bfsSolvedMaze);
	}
}