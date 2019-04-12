package newTest;

import java.io.BufferedReader;
import java.io.FileReader;
/**
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
 * This class implements the competition using Floyd-Warshall algorithm
 */


public class CompetitionFloydWarshall {

    /**
     * @param filename: A filename containing the details of the city road network
     * @param sA, sB, sC: speeds for 3 contestants
     */

    private static final double MAXINT = Integer.MAX_VALUE / 2;   // prevent overflow

    double table[][];  

    int sA, sB, sC;
    int verticesCount, edgeCount;     // intersections = vertices, streets = edges
    int slowestSpeed;

    String filename;

    boolean isValid = true;

    CompetitionFloydWarshall (String filename, int sA, int sB, int sC){
        this.filename = filename;
        this.sA = sA;
        this.sB = sB;
        this.sC = sC;
        this.setUp();
    }

    // initialise array
    private void setUp(){
        slowestSpeed = Math.min(sA, sB);
        slowestSpeed = Math.min(slowestSpeed, sC);

        try {
            BufferedReader myBR = new BufferedReader(new FileReader(filename));
            verticesCount = Integer.parseInt(myBR.readLine());
            edgeCount = Integer.parseInt(myBR.readLine());
            if(verticesCount == 0 || edgeCount == 0 ){
                isValid = false;
            }
            if(filename == null){
                isValid = false;
                slowestSpeed = -1;
            }
            else{
            	table = new double[verticesCount][edgeCount];
                for (int i = 0; i < verticesCount; i++){
                    for (int j = 0; j < verticesCount; j++){
                    	table[i][j] = MAXINT;
                    }
                }
                String readLine = myBR.readLine();
                while((readLine != null)){
                    String[] splitLine = readLine.trim().split(" ");
                    table[Integer.parseInt(splitLine[0])][Integer.parseInt(splitLine[1])] = Double.parseDouble(splitLine[2]);
                    readLine = myBR.readLine();
                }
                myBR.close();
            }
        }catch (Exception e){
            isValid = false;
            slowestSpeed = -1;
        }
    }

    /**
     * @return int: minimum minutes that will pass before the three contestants can meet
     */
    public int timeRequiredforCompetition(){
        if((sA > 100 || sA < 50) || (sB > 100 || sB < 50) || (sC > 100 || sC < 50)){
            return -1;
        }

        if(isValid == false){
            return -1;
        }
       
        for (int k = 0; k < verticesCount; k++){
            for (int i = 0; i < verticesCount; i++){
                for (int j = 0; j < verticesCount; j++){
                    if(table[i][k] + table[k][j] < table[i][j]){
                    	table[i][j] = table[i][k] + table[k][j];
                    }
                }
            }
        }
        double maxDistance = getMaxDistance();
        if(maxDistance == MAXINT){
            return -1;
        }
        maxDistance = maxDistance * 1000;   //meters

        return (int) Math.ceil(maxDistance / slowestSpeed);
    }

    private double getMaxDistance(){
        double maxDistance = -1;
        for (int i = 0; i < verticesCount; i++){
            for (int j = 0; j < verticesCount; j++){
                if(table[i][j] > maxDistance && i != j){
                	maxDistance = table[i][j];
                }
            }
        }
        return maxDistance;
    }

}