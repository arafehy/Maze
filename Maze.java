package sjsu.ArafehSmaluk.cs146.project2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * Maze Generator and Solver
 * This program generates a random maze and solves it using DFS/BFS
 * 
 * @author Yazan Arafeh
 * @author Tim Smaluk
 */
public class Maze {
	private Vertex vertexList[]; // array of vertices
	private int mazeAdjacencyMatrix[][]; // adjacency matrix
	private int dimension; // dimension of maze
	private Random myRandGen; // random generator

	/**
	 * Generates a maze of a given size
	 * @param dimension size of the maze
	 */
	public Maze(int dimension) {
		this.dimension = dimension;

		// create array of vertices
		vertexList = new Vertex[dimension*dimension];
		for (int i = 0; i < dimension*dimension; i++)
			vertexList[i] = new Vertex(' ', i);

		// create adjacency matrix of maze
		mazeAdjacencyMatrix = new int[dimension*dimension][dimension*dimension];
		for (int i = 0; i < dimension*dimension; i++)
			for (int j = 0; j < dimension*dimension; j++)
				mazeAdjacencyMatrix[i][j] = 0;

		myRandGen = new Random(0);
	}

	// Returns a duplicate of the perfect maze
	private Maze duplicateMaze() {
		Maze maze = new Maze(dimension);

		// Create new vertex list array for new maze
		for (int i = 0; i < dimension*dimension; i++)
			maze.vertexList[i] = new Vertex(vertexList[i].label, vertexList[i].index);

		// Create new adjacency matrix
		for (int i = 0; i < dimension*dimension; i++)
			for (int j = 0; j < dimension*dimension; j++)
				maze.mazeAdjacencyMatrix[i][j] = this.mazeAdjacencyMatrix[i][j];

		return maze;
	}

	// Generates a perfect maze using DFS
	// Algorithm pseudocode given by Dr. Potika
	public void generatePerfectMaze() {
		Stack<Integer> cellStack = new Stack<Integer>(); // cell stack holds cell location list
		int totalCells = vertexList.length;
		for (int i = 0; i < totalCells; i++)
			cellStack.push(i);
		int currentCell = mazeAdjacencyMatrix[0][0];
		int visitedCells = 1;
		while (visitedCells < totalCells) {
			ArrayList<Integer> intactNeighbors = findFullyIntactNeighbors(currentCell);
			if (!intactNeighbors.isEmpty()) {
				// choose a random, intact neighbor
				int chosenCellIndex = myRandGen.nextInt(intactNeighbors.size());
				chosenCellIndex = intactNeighbors.get(chosenCellIndex);

				mazeAdjacencyMatrix[currentCell][chosenCellIndex] = 1;
				mazeAdjacencyMatrix[chosenCellIndex][currentCell] = 1;

				cellStack.push(currentCell);
				currentCell = chosenCellIndex;
				visitedCells++;
			}
			else {
				currentCell = cellStack.pop();
			}
		}
	}

	// Solves the perfect maze using DFS and returns the new solved maze
	// @return maze the DFS solved maze
	public Maze solveDFS() {
		Maze maze = duplicateMaze();
		Stack<Vertex> vertexStack = new Stack<Vertex>();
		char label = '0';
		vertexStack.push(maze.vertexList[0]); // push starting cell to stack
		while (!vertexStack.isEmpty()) {
			Vertex vertex = vertexStack.pop();			

			if (!vertex.visited) {
				vertex.visited = true;	// change visited status of vertex to true
				vertex.label = label++;	// increment label by 1 to show next step in search

				// break if reached last vertex/finishing cell/end of maze
				if (vertex.index == vertexList.length - 1)
					break;

				// label cannot be greater than 9, so reset label to 0
				if (vertex.label == '9')
					label = '0';

				// push all unvisited neighbors of vertex to stack
				ArrayList<Vertex> unvisitedNeighbors = maze.getUnvisitedNeighbors(vertex.index);
				for (Vertex unvisitedNeighbor : unvisitedNeighbors) {
					unvisitedNeighbor.parent = vertex;				
					vertexStack.push(unvisitedNeighbor);
				}
			}
		}
		return maze;
	}

	// Solves the perfect maze using BFS and returns the new solved maze
	// @return maze the BFS solved maze
	public Maze solveBFS() {
		Maze maze = duplicateMaze();
		Queue<Vertex> vertexQueue = new LinkedList<Vertex>();
		char label = '0';
		vertexQueue.add(maze.vertexList[0]); 	// enqueue starting cell
		while (!vertexQueue.isEmpty()) {
			Vertex vertex = vertexQueue.poll();	// gets head of vertex queue

			// if vertex has not been visited
			if (!vertex.visited) {
				vertex.visited = true;		// change visited status of vertex to true
				vertex.label = label++;		// increment label by 1 to show next step in search

				// break if reached last vertex/finishing cell/end of maze
				if (vertex.index == vertexList.length - 1)
					break;

				// label cannot be greater than 9, so reset label to 0
				if (vertex.label == '9')
					label = '0'; 

				// enqueue all unvisited neighbors of vertex
				ArrayList<Vertex> unvisitedNeighbors = maze.getUnvisitedNeighbors(vertex.index);
				for (Vertex unvisitedNeighbor : unvisitedNeighbors) {
					unvisitedNeighbor.parent = vertex;
					vertexQueue.add(unvisitedNeighbor);
				}
			}
		}
		return maze;
	}

	// Labels the path of the solution
	public void labelSolutionPath() {		
		// Reset label for each vertex
		for (int i = 0; i < vertexList.length; i++)
			vertexList[i].label = ' ';

		// traverse backwards starting from finishing/last cell to starting/first cell 
		// mark solution path with '#'
		Vertex currentVertex = vertexList[vertexList.length - 1];
		while (currentVertex.parent != null) {
			currentVertex.label = '#';
			currentVertex = currentVertex.parent;
		}
		currentVertex.label = '#';	// set first cell label to '#' since parent of first cell is null
	}

	// Returns a display of the maze in string
	// @return readableMaze string of the maze
	@Override
	public String toString() {
		String readableMaze = "";
		for (int row = 0; row < dimension; row++) {

			// draw walls & corners north of all cells in row
			for (int column = 0; column < dimension; column++) {
				readableMaze += "+"; // corner
				if (cellIndex(row, column) == 0)
					readableMaze += " "; // start
				else 
					// prints space if there is no wall between the two vertices, '-' if there is a wall
					readableMaze += (isNorthWallBroken(cellIndex(row, column)) ? " " : "-");
			}
			readableMaze += "+\n";	// top right corner of last cell in row, jump to next row

			// draw walls on side of all cells in row
			for (int column = 0; column < dimension; column++) {
				readableMaze += (isWestWallBroken(cellIndex(row, column)) ? " " : "|");
				readableMaze += vertexList[cellIndex(row, column)].label;
			}
			readableMaze += "|\n";	// right wall of last cell in row, jump to next row
		}

		// draws walls & corners south of all cells in last row
		for (int column = 0; column < dimension; column++) {
			readableMaze += "+"; // corner
			if (cellIndex(dimension - 1, column) == vertexList.length - 1)
				readableMaze += " "; // end
			else 
				// prints space if there is no wall between the two vertices, '-' if there is a wall
				readableMaze += (isSouthWallBroken(cellIndex(dimension - 1, column)) ? " " : "-");
		}
		readableMaze += "+\n";		// add '+' for bottom right corner
		return readableMaze;
	}

	// Returns cell index of a given row, column coordinate
	// @return cell index
	private int cellIndex(int row, int column) {
		return row * dimension + column;
	}

	// Returns true if there is no wall north of the cell
	// @return true if there is no wall north of the cell
	private boolean isNorthWallBroken(int vertexIndex) {
		int row = vertexIndex / dimension;		// calculates row of vertex
		int column = vertexIndex % dimension;	// calculates column of vertex

		// Must have north wall in first row (top edge of maze)
		if (row == 0)
			return false;

		int northNeighbor = cellIndex(row - 1, column);
		return (mazeAdjacencyMatrix[vertexIndex][northNeighbor] == 1);	// true if wall between vertices is broken
	}

	// Returns true if there is no wall south of the cell
	// @return true if there is no wall south of the cell
	private boolean isSouthWallBroken(int vertexIndex) {
		int row = vertexIndex / dimension;		// calculates row of vertex
		int column = vertexIndex % dimension;	// calculates column of vertex

		// Must have south wall in last row (bottom edge of maze)
		if (row == dimension - 1)
			return false;

		int southNeighbor = cellIndex(row + 1, column);
		return (mazeAdjacencyMatrix[vertexIndex][southNeighbor] == 1);	// true if wall between vertices is broken
	}

	// Returns true if there is no wall east of the cell
	// @return true if there is no wall east of the cell
	private boolean isEastWallBroken(int vertexIndex) {
		int row = vertexIndex / dimension;			// calculates row of vertex
		int column = vertexIndex % dimension;		// calculates column of vertex

		// Must have east wall in last column (right edge of maze)
		if (column == dimension - 1)
			return false;

		int eastNeighbor = cellIndex(row, column + 1);
		return (mazeAdjacencyMatrix[vertexIndex][eastNeighbor] == 1);	// true if wall between vertices is broken
	}

	// Returns true if there is no wall west of the cell
	// @return true if there is no wall west of the cell
	private boolean isWestWallBroken(int vertexIndex) {
		int row = vertexIndex / dimension;
		int column = vertexIndex % dimension;

		// Must have west wall in first column (left edge of maze)
		if (column == 0)
			return false;

		int westNeighbor = cellIndex(row, column - 1);
		return (mazeAdjacencyMatrix[vertexIndex][westNeighbor] == 1);	// true if wall between vertices is broken
	}

	// Returns true if all walls of the cell are intact
	// @return true if all walls of the cell are intact
	private boolean areAllWallsIntact(int vertexIndex) {
		return !isWestWallBroken(vertexIndex) && !isEastWallBroken(vertexIndex) && 
				!isNorthWallBroken(vertexIndex) && !isSouthWallBroken(vertexIndex);
	}

	// Finds fully intact neighbors of a cell
	// @return intactNeighbors an ArrayList of the neighbors of a vertex that are intact
	private ArrayList<Integer> findFullyIntactNeighbors(int vertexIndex) {
		ArrayList<Integer> intactNeighbors = new ArrayList<Integer>();
		int row = vertexIndex / dimension;		// calculates row of vertex
		int column = vertexIndex % dimension;	// calculates column of vertex

		// Check west neighbor
		if (column - 1 >= 0) {
			int westNeighbor = cellIndex(row, column - 1);
			if (areAllWallsIntact(westNeighbor))
				intactNeighbors.add(westNeighbor);
		}

		// Check east neighbor
		if (column + 1 < dimension) {
			int eastNeighbor = cellIndex(row, column + 1);
			if (areAllWallsIntact(eastNeighbor))
				intactNeighbors.add(eastNeighbor);
		}

		// Check north neighbor
		if (row - 1 >= 0) {
			int northNeighbor = cellIndex(row - 1, column);
			if (areAllWallsIntact(northNeighbor))
				intactNeighbors.add(northNeighbor);
		}

		// Check south neighbor
		if (row + 1 < dimension) {
			int southNeighbor = cellIndex(row + 1, column);
			if (areAllWallsIntact(southNeighbor))
				intactNeighbors.add(southNeighbor);
		}
		return intactNeighbors;
	}

	// Returns neighbors of a vertex that have not been visited
	// @return unvisitedNeighbors an ArrayList of the neighbors of a vertex that have not been visited
	private ArrayList<Vertex> getUnvisitedNeighbors(int vertexIndex) {
		ArrayList<Vertex> unvisitedNeighbors = new ArrayList<Vertex>();
		int row = vertexIndex / dimension;		// calculates row of vertex
		int column = vertexIndex % dimension;	// calculates column of vertex

		// Checks west neighbor
		if (column - 1 >= 0) {
			int westNeighbor = cellIndex(row, column - 1);

			// if there is an edge between the vertexes and if the neighbor has not been visited
			// add to list of unvisited neighbors
			if (mazeAdjacencyMatrix[vertexIndex][westNeighbor] == 1 && !vertexList[westNeighbor].visited)
				unvisitedNeighbors.add(vertexList[westNeighbor]);
		}

		// Checks east neighbor
		if (column + 1 < dimension) {
			int eastNeighbor = cellIndex(row, column + 1);

			// if there is an edge between the vertexes and if the neighbor has not been visited
			// add to list of unvisited neighbors
			if (mazeAdjacencyMatrix[vertexIndex][eastNeighbor] == 1 && !vertexList[eastNeighbor].visited)
				unvisitedNeighbors.add(vertexList[eastNeighbor]);
		}

		// Checks north neighbor
		if (row - 1 >= 0) {
			int northNeighbor = cellIndex(row - 1, column);

			// if there is an edge between the vertexes and if the neighbor has not been visited
			// add to list of unvisited neighbors
			if (mazeAdjacencyMatrix[vertexIndex][northNeighbor] == 1 && !vertexList[northNeighbor].visited)
				unvisitedNeighbors.add(vertexList[northNeighbor]);
		}

		// Checks south neighbor
		if (row + 1 < dimension) {
			int southNeighbor = cellIndex(row + 1, column);

			// if there is an edge between the vertexes and if the neighbor has not been visited
			// add to list of unvisited neighbors
			if (mazeAdjacencyMatrix[vertexIndex][southNeighbor] == 1 && !vertexList[southNeighbor].visited)
				unvisitedNeighbors.add(vertexList[southNeighbor]);
		}
		return unvisitedNeighbors;
	}
}