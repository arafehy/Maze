package sjsu.ArafehSmaluk.cs146.project2;

import java.util.Scanner;

/*
 * Class that includes a main method that
 * prints out results of DFS and BFS on a perfect maze
 * 
 * @author Yazan Arafeh
 * @author Tim Smaluk
 */
public class MazeRunner {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter integer maze size: ");
		int size = input.nextInt();
		input.close();

		// Create a perfect maze
		Maze perfectMaze = new Maze(size);
		perfectMaze.generatePerfectMaze();
		System.out.println("Perfect Maze:");
		System.out.println(perfectMaze);

		// Solve the maze using DFS
		System.out.println("\nDFS:");
		Maze dfsSolvedMaze = perfectMaze.solveDFS();
		System.out.println(dfsSolvedMaze);
		dfsSolvedMaze.labelSolutionPath();
		System.out.println(dfsSolvedMaze);

		// Solve the maze using BFS
		System.out.println("\nBFS:");
		Maze bfsSolvedMaze = perfectMaze.solveBFS();
		System.out.println(bfsSolvedMaze);
		bfsSolvedMaze.labelSolutionPath();
		System.out.println(bfsSolvedMaze);
	}
}