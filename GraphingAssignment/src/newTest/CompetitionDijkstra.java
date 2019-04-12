package newTest;
import java.io.FileInputStream;
import java.util.*;

/**
 * A Contest to Meet (ACM) is a reality TV contest that sets three contestants at three random
 * city intersections. In order to win, the three contestants need all to meet at any intersection
 * of the city as fast as possible.
 * It should be clear that the contestants may arrive at the intersections at different times, in
 * which case, the first to arrive can wait until the others arrive.
 * From an estimated walking speed for each one of the three contestants, ACM wants to determine the
 * minimum time that a live TV broadcast should last to cover their journey regardless of the contestantsâ€™
 * initial positions and the intersection they finally meet. You are hired to help ACM answer this question.
 * You may assume the following:
 *    ï‚· Each contestant walks at a given estimated speed.
 *    ï‚· The city is a collection of intersections in which some pairs are connected by one-way
 * streets that the contestants can use to traverse the city.
 *
 * This class implements the competition using Dijkstra's algorithm
 */

class CompetitionDijkstra {

    int sA, sB, sC;
    int slowestSpeed;

    String filename;

    private TreeMap<Integer, Node> myTreeMap;

    /**
     * @param filename: A filename containing the details of the city road network
     * @param sA,sB,sC: speeds for 3 contestants
     */
    CompetitionDijkstra(String filename, int sA, int sB, int sC) {
        this.filename = filename;
        this.sA = sA;
        this.sB = sB;
        this.sC = sC;
        this.setUp();
    }

    private void setUp() {

        slowestSpeed = Math.min(sA, sB);
        slowestSpeed = Math.min(slowestSpeed, sC);
        if (filename == null) slowestSpeed = -1;
        myTreeMap = new TreeMap<>();

        // initialise TreeMap and nodes
        try {
            Scanner myScanner = new Scanner(new FileInputStream(filename));
            int V = myScanner.nextInt();
            int S = myScanner.nextInt();
            for (int i = 0; i < S; i++) {
                if (myScanner.hasNext()) {
                    int firstIntersect = myScanner.nextInt();
                    int secondIntersect = myScanner.nextInt();
                    double distance = myScanner.nextDouble() * 1000;
                    Node myNode, newNode;

                    if (myTreeMap.get(firstIntersect) == null) {
                    	myNode = new Node(firstIntersect);
                        myTreeMap.put(firstIntersect, myNode);
                    } else myNode = myTreeMap.get(firstIntersect);

                    if (myTreeMap.get(secondIntersect) == null) {
                    	newNode = new Node(secondIntersect);
                        myTreeMap.put(secondIntersect, newNode);
                    } else newNode = myTreeMap.get(secondIntersect);

                    myNode.addAdj(newNode, distance);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            slowestSpeed = -1;
        }
    }

    private double findLowest(int startNode) {

        LinkedList<Node> nodeList = new LinkedList<>();
        for (Node myNode : myTreeMap.values()) {
            if (myNode.nodeID == startNode) myNode.nodeCost = 0;
            else myNode.nodeCost = Double.MAX_VALUE;
            nodeList.add(myNode);
        }

        for (int i = 0; i < myTreeMap.values().size(); i++) {
            for (Node myNode : nodeList) {
                for (Path myPath : myNode.pathList) {
                    double newCost = myNode.nodeCost + myPath.nodeCost;
                    if (newCost < myPath.nodeDest.nodeCost) {
                    	myPath.nodeDest.nodeCost = newCost;
                    }
                }
            }
        }

        double maxNode = Double.MIN_VALUE;
        for (Node myNode : myTreeMap.values()) {
            if (myNode.nodeCost == Double.MAX_VALUE) return myNode.nodeCost;
            else if (myNode.nodeCost > maxNode)
            	maxNode = myNode.nodeCost;
        }
        return maxNode;
    }

    /**
     * @return int: minimum minutes that will pass before the three contestants can meet
     */
    public int timeRequiredforCompetition() {
        if((sA > 100 || sA < 50) || (sB > 100 || sB < 50) || (sC > 100 || sC < 50)){
            return -1;
        }

        if (myTreeMap.size() == 0 || slowestSpeed <= 0) return -1;
        double maxDistance = -1;
        for (Node myNode : myTreeMap.values()) {
            double distance = findLowest(myNode.nodeID);
            if (distance == Double.MAX_VALUE) return -1;
            maxDistance = Math.max(maxDistance, distance);
        }
        return (int) Math.ceil(maxDistance / slowestSpeed);
    }

    private class Node {
        int nodeID;
        double nodeCost = Double.MAX_VALUE; 
        ArrayList<Path> pathList = new ArrayList<>();

        Node(int nodeID) {
            this.nodeID = nodeID;
        }

        void addAdj(Node node, double cost) {
        	pathList.add(new Path(node, cost));
        }
    }

    private class Path {
        Node nodeDest;
        double nodeCost;

        Path(Node nodeDest, double nodeCost) {
            this.nodeDest = nodeDest;
            this.nodeCost = nodeCost;
        }
    }
}