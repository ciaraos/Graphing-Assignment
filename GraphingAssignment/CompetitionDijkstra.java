import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
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
	double speedA;
	double speedB;
	double speedC;
	int nodeCount;
	int edgeCount;
	EdgeWeightedGraph myGraph;

	/**
	 * Constructor
	 * @param filename: A filename containing the details of the city road network
	 * @param sA, sB, sC: speeds for 3 contestants
	 */
	CompetitionDijkstra(String filename, int sA, int sB, int sC) throws IOException {
		speedA = sA;
		speedB = sB;
		speedC = sC;
		readFile(filename);
	}

	/**
	 * Reads the file, creates graph, adds edges
	 * @param filename: A filename containing the details of the city road network
	 */

	public void readFile(String filename) throws IOException {
		File myFile = new File(filename);
		FileReader fReader = new FileReader(myFile);
		BufferedReader br = new BufferedReader(fReader);
		String readLine = br.readLine();

		nodeCount = Integer.parseInt(readLine);
		readLine = br.readLine();
		edgeCount = Integer.parseInt(readLine);

		myGraph = new EdgeWeightedGraph(nodeCount);

		readLine = br.readLine();
		while (readLine != null) {
			if (nodeCount >= 100) {
				//check if first and second numbers are double or single digits
				if (readLine.charAt(5) == ' ') 
				{
					String tmp = readLine.substring(6);
					readLine = readLine.substring(0, 4);
					readLine += tmp;
				} else if (readLine.charAt(4) == ' ') 
				{
					String tempString = readLine.substring(5);
					readLine = readLine.substring(0, 4);
					readLine += tempString;
				}
				if (readLine.charAt(1) == ' ') 
				{
					readLine = readLine.substring(2);
				} else if (readLine.charAt(0) == ' ') 
				{
					readLine = readLine.substring(1);
				}
			} else if (nodeCount >= 10) {
				if (readLine.charAt(3) == ' ') 
				{
					String tempString = readLine.substring(4);
					readLine = readLine.substring(0, 2);
					readLine += tempString;
				}
				if (readLine.charAt(0) == ' ') 
				{
					readLine = readLine.substring(1);
				}
			}

			String[] stringArray = readLine.split(" ");
			String node1 = stringArray[0];
			String nodeFinal = stringArray[1];
			String stringWeight = stringArray[2];

			int firstNode = Integer.parseInt(node1);
			int finalNode = Integer.parseInt(nodeFinal);
			double weight = Double.parseDouble(stringWeight);

			directedEdge edge = new directedEdge(firstNode, finalNode, weight);
			myGraph.addEdge(edge);
			readLine = br.readLine();
		}
		br.close();
	}

	/**
	 * Finds the shortest amount of time to pass before all three contestants meet
	 * @return int: minimum minutes that will pass before the three contestants can meet
	 */
	public int timeRequiredforCompetition() {
		int time = -1;
		if (speedA < 50 || speedA > 100 || speedB < 50 || speedB > 100
				|| speedC > 100 || speedC < 50) {
			return time;
		}

		double[][] nodeDistance = new double[nodeCount][nodeCount];
		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < nodeCount; j++) { 
				// sets values to -1
				nodeDistance[i][j] = -1;
			}
		}

		for (int i = 0; i < nodeCount; i++) { 
			// find shortest path 
			double[] startDistance = Dijkstra(myGraph, i);
			for (int index = 0; index < nodeCount; index++) {
				double distanceFromStart = startDistance[index];
				nodeDistance[i][index] = distanceFromStart;
			}
		}

		if (!isPossible(nodeDistance)) {
			return time;
		}

		double slowest = speedA;
		if (speedB < slowest) {
			slowest = speedB;
		}
		if (speedC < slowest) {
			slowest = speedC;
		}

		double[][] slowestTime = createChart(nodeDistance, slowest);
		time = getSlowest(slowestTime);
		return time;
	}

	/**
	 * Calculate distance between nodes in the graph
	 * @param myGraph: represents city.
	 * @param startNode: index of starting node
	 * @return array: distance from start node to other node
	 */
	public double[] Dijkstra(EdgeWeightedGraph myGraph, int startNode) {
		ArrayList<String> testSeq = new ArrayList<String>();
		boolean[] isChecked = new boolean[nodeCount];
		double[] distFromStart = new double[nodeCount];
		directedEdge[] edgeArray = new directedEdge[nodeCount];
		int currNode = startNode;
		testSeq.add(Integer.toString(currNode));
		for (int i = 0; i < nodeCount; i++) {
			distFromStart[i] = -1;
			edgeArray[i] = null;
			isChecked[i] = false;
		}
		distFromStart[startNode] = 0;

		while (testSeq.size() > 0) {
			currNode = Integer.parseInt(testSeq.get(0));
			if (isChecked[currNode] == false) {
				Bag startEdge = myGraph.adj[currNode];

				ArrayList<String> edgeList = getValues(startEdge);

				for (int j = 0; j < edgeList.size(); j++) {
					testSeq.add(edgeList.get(j));
				}
				relaxEdge(startEdge, currNode, distFromStart, edgeArray);
				isChecked[currNode] = true;
			}
			testSeq.remove(0);
		}
		for (int k = 0; k < nodeCount; k++)
		{
			// change to metres
			distFromStart[k] = distFromStart[k] * 1000;
		}
		return distFromStart;
	}

	/**
	 * Finds a list of nodes connected to the startNode, sorted by weight to find priority
	 * @param startEdge: bag containing the edges where sort starts at a certain node
	 * @return ArrayList: nodes where start nodes has edges coming out of (sorted by weight)
	 */

	public ArrayList<String> getValues(Bag startEdge) {
		ArrayList<String> nodeList = new ArrayList<String>();
		for (int i = 0; i < startEdge.size(); i++) {
			directedEdge currNode = startEdge.get(i);
			String child = Integer.toString(currNode.nodeTo());
			nodeList.add(child);
		}
		return nodeList;
	}

	/**
	 * Relax edge between start and edge at index
	 * @param startEdge: bag containing edges where sort starts at given node
	 * @param edgeIndex: index value of start node
	 * @param distFromStart: 2D array of shortest distances from start node to other nodes
	 * @param edgeArray: DirectedEdge array recording previous node traveled to get to node at each index  
	 */
	public void relaxEdge(Bag startEdge, int edgeIndex, double[] distFromStart, directedEdge[] edgeArray) {
		for (int i = 0; i < startEdge.size(); i++) {
			directedEdge tmpEdge = startEdge.get(i);
			int edgeWeight = tmpEdge.nodeTo();
			double currWeight = distFromStart[edgeWeight];
			double finalWeight = distFromStart[edgeIndex] + tmpEdge.weight;

			if (currWeight == -1) {
				distFromStart[edgeWeight] = finalWeight;
				edgeArray[edgeWeight] = tmpEdge;
			} else if (currWeight > finalWeight) {
				distFromStart[edgeWeight] = finalWeight;
				edgeArray[edgeWeight] = tmpEdge;
			}
		}
	}

	/**
	 * checks if possible if node has a path to every other node (returns true if yes, otherwise false)
	 * @param dist: 2D array of distances (meters)
	 * @return boolean: if there is a possible path to each node exists
	 */
	public boolean isPossible(double[][] distance) {
		boolean possible = true;
		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < nodeCount; j++) {
				if (distance[i][j] == -1) {
					possible = false;
					return possible;
				}
			}
		}
		return possible;
	}

	/**
	 * Records time it takes to get to each node (minutes)
	 * @param nodeDistance: 2D array of distances (meters)
	 * @param speed: speed of contests (meters per minute)
	 * @return 2D array: time between nodes (minutes)
	 */
	public double[][] createChart(double[][] nodeDistance, double speed) {
		double[][] tmp = nodeDistance;
		double[][] time = new double[nodeCount][nodeCount];
		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < nodeCount; j++) {
				time[i][j] = tmp[i][j] / speed;
			}
		}
		return time;
	}

	/**
	 * Find longest time for slowest person to walk to anywhere in the city
	 * @param slowestTimeChart: 2D array of time it takes slowest persons to get to any node
	 * @return int: max time taken
	 */
	public int getSlowest(double[][] slowestTimeChart) {
		double slowest = slowestTimeChart[0][0];
		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < nodeCount; j++) {
				if (slowestTimeChart[i][j] > slowest) {
					slowest = slowestTimeChart[i][j];
				}
			}
		}
		slowest++;
		int slowestTime = (int) slowest;
		return slowestTime;
	}

	/**
	 * convert array of distances between nodes to a string (for tests)
	 * @param nodeDistance: 2D array of distances from start node to other nodes (metres)
	 * @return String: distance from start node to other nodes
	 */

	public String DistancetoString(double[] nodeDistance) {
		DecimalFormat intFormat = new DecimalFormat("#.########");
		String printString = "";
		for (int i = 0; i < nodeCount; i++) {
			String string = "";
			string += "	Vertex: " + i + " Value: " + intFormat.format(nodeDistance[i]) + "\n";
			printString += string;
		}
		return printString;
	}

	/**
	 * Convert 2D array of times to get to each node to a string (for test)
	 * @param time: 2D array of time to get from one node to another (minutes)
	 * @return String: time to get to node node from another
	 */

	public String toStringTimeChart(double[][] time) {
		DecimalFormat intFormat = new DecimalFormat("#.##");
		String printString = "";
		for (int i = 0; i < nodeCount; i++) {
			printString += "Time (minutes) to get to " + i + "\n";
			for (int l = 0; l < nodeCount; l++) {
				String string = " From startNode " + l + ":	" + intFormat.format(time[i][l]) + "\n";
				printString += string;
			}
		}
		return printString;
	}

	/**
	 * Print graph (for test)
	 * @return String: graph showing nodeTo, nodeFrom and weight of edges 
	 */

	public String toStringGraph() {
		String printString = "";
		for (int i = 0; i < nodeCount; i++) {
			Bag currBag = myGraph.adj[i];
			int index = 0;
			directedEdge currEdge = currBag.get(index);
			while (currEdge != null) {
				String string = "";
				string = currEdge.nodeFrom() + " ->" + currEdge.nodeTo() + " " + currEdge.weight + "\n";
				printString += string;
				index++;
				currEdge = currBag.get(index);

			}
		}
		return printString;
	}

	private static class EdgeWeightedGraph {

		Bag[] adj;

		EdgeWeightedGraph(int nodeCount) {
			adj = new Bag[nodeCount];
			for (int i = 0; i < nodeCount; i++) {
				adj[i] = new Bag();
			}
		}

		void addEdge(directedEdge currEdge) {
			int prevNode = currEdge.nodeFrom();
			adj[prevNode].add(currEdge);
		}
	}

	public static class Bag {
		private edgeNode firstNode; 
		private int bagSize; 

		public Bag() {
			firstNode = null;
			bagSize = 0;
		}

		public boolean isEmpty() {
			return firstNode == null;
		}

		public int size() {
			return bagSize;
		}

		public void add(directedEdge myEdge) { // sort by ascending weights
			if (bagSize == 0) {
				firstNode = new edgeNode(myEdge, null);
				bagSize++;
				return;
			}
			directedEdge startEdge = firstNode.currNode;
			boolean isAdded = false;
			if (startEdge.getWeight() > myEdge.getWeight()) { 
				//if newest node has lowest weight
				edgeNode prevFirst = firstNode;
				firstNode = new edgeNode(myEdge, prevFirst);
			} else {
				edgeNode nodeBefore = firstNode;
				edgeNode nextNode = firstNode.getNextNode();
				while (nextNode != null && isAdded == false) {
					directedEdge nextEdge = nextNode.getCurrNode();
					if (nextEdge.getWeight() > myEdge.getWeight()) { 
						edgeNode newNode = new edgeNode(myEdge, nextNode);
						nodeBefore.nextNode = newNode;
						isAdded = true;
					} else {
						nodeBefore = nextNode;
						nextNode = nextNode.getNextNode();
					}
				}
				if (nextNode == null) {
					edgeNode newNode = new edgeNode(myEdge, nextNode);
					nodeBefore.nextNode = newNode;
				}
			}
			bagSize++;
			return;
		}

		public directedEdge get(int edgeIndex) {
			int count = 0;
			directedEdge item = null;
			if (edgeIndex >= bagSize) {
				return item;
			}

			edgeNode currItem = firstNode;
			while (count != edgeIndex) {
				currItem = currItem.nextNode;
				count++;
			}
			item = currItem.currNode;
			return item;
		}
	}

	public static class edgeNode {

		public directedEdge currNode;
		public edgeNode nextNode;

		edgeNode(directedEdge currNode, edgeNode nextNode) {
			this.currNode = currNode;
			this.nextNode = nextNode;
		}

		public directedEdge getCurrNode() {
			return currNode;
		}

		public edgeNode getNextNode() {
			return nextNode;
		}

	}

	public class directedEdge {
		int firstNode;
		int lastNode;
		double weight;

		directedEdge(int firstNode, int lastNode, double weight) {
			this.firstNode = firstNode;
			this.lastNode = lastNode;
			this.weight = weight;
		}

		public int nodeTo() {
			return lastNode;
		}

		public int nodeFrom() {
			return firstNode;
		}

		public double getWeight() {
			return weight;
		}
	}
}