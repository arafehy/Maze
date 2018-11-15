package sjsu.ArafehSmaluk.cs146.project2;

/**
 * Class Vertex that models one cell in a maze
 * 
 * @author Yazan Arafeh
 * @author Tim Smaluk
 */
public class Vertex {
	public final int index; // index of the vertex in the list of vertices
	public boolean visited; // has the vertex been visited
	public char label; // label of the vertex
	public Vertex parent;

	// Vertex constructor
	public Vertex(char label, int index)
	{
		this.label = label;
		this.index = index;
		visited = false;
		parent = null;
	}
}