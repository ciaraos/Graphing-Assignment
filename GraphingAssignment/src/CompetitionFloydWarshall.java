import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

//import CompetitionDijkstra.DirectedEdge;
//import CompetitionDijkstra.Path;

public class CompetitionFloydWarshall {

    private int Node;
    private int Edge;

    private int speedA;
    private int speedB;
    private int speedC;

    private EdgeWeightedDigraph myGraph;

    /**
     * @param filename: A filename containing the details of the city road network
     * @param speedA,       speedB, speedC: speeds for 3 contestants
     */
    CompetitionFloydWarshall(String filename, int speedA, int speedB, int speedC) {
        this.speedA = speedA;
        this.speedB = speedB;
        this.speedC = speedC;

        BufferedReader myBufferedReader = null;
        try {
            if (filename != null) {
            	myBufferedReader = new BufferedReader(new FileReader(filename));
                int lineCount = 0;
                try {
                    String readLine = myBufferedReader.readLine();
                    while (readLine != null) {
                        if (lineCount == 0) {
                        	Node = Integer.parseInt(readLine);
                        } else if (lineCount == 1) {
                        	Edge = Integer.parseInt(readLine);
                            myGraph = new EdgeWeightedDigraph(Node, Edge);
                        } else {
                            String[] splitString = readLine.trim().split("\\s+");

                            assert myGraph != null;
                            myGraph.addIntersection(Integer.parseInt(splitString[0]),
                                    Integer.parseInt(splitString[1]),
                                    Double.parseDouble(splitString[2]));
                        }

                        lineCount++;
                        readLine = myBufferedReader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                    	myBufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
            	myGraph = null;
            }
        } catch (FileNotFoundException e) {
        	myGraph = null;
        }
    }

    /**
     * @return int: minimum minutes that will pass before the three contestants can meet
     */
    public int timeRequiredforCompetition() {
        if (myGraph != null) {
            if (speedA < 50 || speedB < 50 || speedC < 50
                    || speedA > 100 || speedB > 100 || speedC > 100) return -1;

            double[][] nodeDistance = new double[myGraph.Vertex][myGraph.Vertex];
            DirectedEdge[][] nodeEdge = new DirectedEdge[myGraph.Vertex][myGraph.Vertex];

            for (int vector = 0; vector < myGraph.Vertex; vector++) {
                for (int weight = 0; weight < myGraph.Vertex; weight++) {
                	nodeDistance[vector][weight] = Double.POSITIVE_INFINITY;
                }
            }

            for (int vector = 0; vector < myGraph.Vertex; vector++) {
                for (DirectedEdge e : myGraph.adj[vector]) {
                	nodeDistance[e.vector][e.w] = e.weight;
                	nodeEdge[e.vector][e.w] = e;
                }
                // in case of self-loops
                if (nodeDistance[vector][vector] >= 0.0) {
                	nodeDistance[vector][vector] = 0.0;
                	nodeEdge[vector][vector] = null;
                }
            }

            for (int i = 0; i < myGraph.Vertex; i++) {
                // compute shortest paths using only 0, 1, ..., i as intermediate vertices
                for (int vector = 0; vector < myGraph.Vertex; vector++) {
                    if (nodeEdge[vector][i] == null) continue;  // optimization
                    for (int weight = 0; weight < myGraph.Vertex; weight++) {
                        if (nodeDistance[vector][weight] > nodeDistance[vector][i] + nodeDistance[i][weight]) {
                        	nodeDistance[vector][weight] = nodeDistance[vector][i] + nodeDistance[i][weight];
                        	nodeEdge[vector][weight] = nodeEdge[i][weight];
                        }
                    }
                }
            }

            double distance = getGreatestDistance(nodeDistance);
            if (Double.POSITIVE_INFINITY == distance) {
                return -1;
            } else {
                int slowest = getSlowest();
                slowest = (int) Math.ceil((distance * 1000) / slowest);
                return slowest;
            }
        }

        return -1;
    }

    private int getSlowest() {
        int[] speedList = new int[]{speedA, speedB, speedC};
        Arrays.sort(speedList);
        return speedList[0];
    }

    private double getGreatestDistance(double[][] nodeDistance) {
        double greatestDistance = 0;
        for (double[] distArray : nodeDistance) {
            for (double distance : distArray) {
                if (greatestDistance < distance)
                	greatestDistance = distance;
            }
        }

        return greatestDistance;
    }
    class EdgeWeightedDigraph {
        public int Vertex;
        public int EdgeGraph;
        public HashSet<DirectedEdge>[] adj;
        public int[] degrees;

        EdgeWeightedDigraph(int EWVector, int EWEdge) {
            this.Vertex = EWVector;
            this.EdgeGraph = EWEdge;
            this.degrees = new int[EWVector];

            adj = (HashSet<DirectedEdge>[]) new HashSet[EWVector];
            for (int i = 0; i < EWVector; i++)
                adj[i] = new HashSet<DirectedEdge>();
        }

        void addIntersection(int vertexA, int vertexB, double weight){
            try {
                validateVertex(vertexA);
                validateVertex(vertexB);

                DirectedEdge e = new DirectedEdge(vertexA, vertexB, weight);
                adj[vertexA].add(e);
                degrees[vertexB]++;
            }
            catch(IllegalArgumentException ignore) {}
        }

        private void validateVertex(int valVertex) {
            if (valVertex < 0 || valVertex >= Vertex)
                throw new IllegalArgumentException("vertex " + valVertex + " is not between 0 and " + (Vertex-1));
        }
    }
    
    public class Path {
        int pathV;
        double pathW;

        Path(int vector, double weight){
            this.pathV = vector;
            this.pathW = weight;
        }

        public boolean equals(Path myPath){
            return (myPath.pathV==this.pathV);
        }

        @Override
        public int hashCode(){
            return pathV;
        }
    }

    class MyComparator implements Comparator<Path>{
        @Override
        public int compare(Path myPath, Path pathCompare) {
            return Double.compare(myPath.pathW, pathCompare.pathW);
        }
    }
    
    public class DirectedEdge {
        public int vector;
        public int w;
        public double weight;

        public DirectedEdge(int v, int w, double weight){
            this.vector = v;
            this.w = w;
            this.weight = weight;
        }
    }
}


