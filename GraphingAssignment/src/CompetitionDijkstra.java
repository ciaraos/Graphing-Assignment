import java.util.ArrayList;

/*
 * A Contest to Meet (ACM) is a reality TV contest that sets three contestants at three random
 * city intersections. In order to win, the three contestants need all to meet at any intersection
 * of the city as fast as possible.
 * It should be clear that the contestants may arrive at the intersections at different times, in
 * which case, the first to arrive can wait until the others arrive.
 * From an estimated walking speed for each one of the three contestants, ACM wants to determine the
 * minimum time that a live TV broadcast should last to cover their journey regardless of the contestants’
 * initial positions and the intersection they finally meet. You are hired to help ACM answer this question.
 * You may assume the following:
 *     Each contestant walks at a given estimated speed.
 *     The city is a collection of intersections in which some pairs are connected by one-way
 * streets that the contestants can use to traverse the city.
 *
 * This class implements the competition using Dijkstra's algorithm
 */

//weighted edge graph
public class CompetitionDijkstra {

	/**
	 * @param filename:
	 *            A filename containing the details of the city road network
	 * @param sA,
	 *            sB, sC: speeds for 3 contestants
	 */
	CompetitionDijkstra(String filename, int sA, int sB, int sC) {

		// TODO
	}

	/**
	 * @return int: minimum minutes that will pass before the three contestants can
	 *         meet
	 */
	public int timeRequiredforCompetition() {

		// TO DO
		return -1;
	}

	public class Graph {
		private final int V;
		private final ArrayList<Integer>[] adj;
		public Graph(int V)
		{
		this.V = V;
		adj = (ArrayList<Integer>[]) new ArrayList[V];
		for (int v = 0; v < V; v++)
		adj[v] = new ArrayList<Integer>();
		}
		public void addEdge(int v, int w)
		{
		adj[v].add(w);
		adj[w].add(v);
		}
		public Iterable<Integer> adj(int v)
		{ return adj[v]; }
	}
	
	public class EdgeWeightedGraph
	{
	private final int V;
	private final ArrayList<Edge>[] adj;
	public EdgeWeightedGraph(int V)
	{
	this.V = V;
	adj = (ArrayList<Edge>[]) new ArrayList[V];
	for (int v = 0; v < V; v++)
	adj[v] = new ArrayList<Edge>();
	}
	public void addEdge(Edge e)
	{
	int v = e.either(), w = e.other(v);
	adj[v].add(e);
	adj[w].add(e);
	}
	public Iterable<Edge> adj(int v)
	{ return adj[v]; }
	}
	
	public class Edge implements Comparable<Edge>
	{
	private final int v, w;
	private final double weight;
	public Edge(int v, int w, double weight)
	{
	this.v = v;
	this.w = w;
	this.weight = weight;
	}
	public int either()
	{ return v; }
	public int other(int vertex)
	{
	if (vertex == v) return w;
	else return v;
	}
	public int compareTo(Edge that)
	{
	if (this.weight < that.weight) return -1;
	else if (this.weight > that.weight) return +1;
	else return 0;
	}
	}
	
	
}