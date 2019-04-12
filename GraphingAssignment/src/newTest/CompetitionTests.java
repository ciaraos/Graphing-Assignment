package newTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 1. Justify the choice of the data structures used in CompetitionDijkstra and
	CompetitionFloydWarshall
	- CompetitionDijkstra uses a treeMap as it was the most efficient option I could think of. 
	  Although I considered using hash sets, this improved my times greatly
	- CompetitionFloydWarshall uses a 2D array which was most efficient for accessing values and comparing them 
	
   2. Explain theoretical differences in the performance of Dijkstra and Floyd-Warshall algorithms
	in the given problem. Also explain how would their relative performance be affected by the
	density of the graph. Which would you choose in which set of circumstances and why?
	- Dijsktra: get O(VE + VlogV) when you run it for all nodes
	- FloydWarshall: get O(V^3) when you run it for all nodes
	- I would use FloydWarshall for a smaller graph as it is not affected
	- I would use Dijsktra for a dense graph, otherwise FloydWarshall 
	
 */

public class CompetitionTests {

    @Test
    public void testDijkstraConstructor() {
        CompetitionDijkstra expected = new CompetitionDijkstra("tinyEWD.txt", 50, 80, 60);
        assertEquals("constructor with valid input failed", expected.slowestSpeed, 50);
    }

    @Test
    public void testDijkstra() {
        CompetitionDijkstra expected = new CompetitionDijkstra("tinyEWD.txt", 50,80,60);
        assertEquals("Test with TINYEWD", 38, expected.timeRequiredforCompetition());

        CompetitionDijkstra expected1 = new CompetitionDijkstra("TINYsdfgdfgEWD.txt", 50, 80, 60);
        assertEquals("Test with invalid filename", -1, expected1.timeRequiredforCompetition());

        CompetitionDijkstra expected2 = new CompetitionDijkstra("tinyEWD.txt", -1, 80, 60);
        assertEquals("Test with negative speed", -1, expected2.timeRequiredforCompetition());

        CompetitionDijkstra expected3 = new CompetitionDijkstra(null, 50, 80, 60);
        assertEquals("Test with null filename", -1, expected3.timeRequiredforCompetition());

        CompetitionDijkstra expected4 = new CompetitionDijkstra("tinyEWD-2.txt", 50, 80, 60);
        assertEquals("Test with node with no path", -1, expected4.timeRequiredforCompetition());

        CompetitionDijkstra expected5 = new CompetitionDijkstra("input-J.txt", 98, 70, 84);
        assertEquals("Test with speed in range", -1, expected5.timeRequiredforCompetition());

        CompetitionDijkstra expected6 = new CompetitionDijkstra("tinyEWD.txt", 5, 80, 60);
        assertEquals("Test with speed outside range", -1, expected6.timeRequiredforCompetition());
    }



    @Test
    public void testFWConstructor() {
        CompetitionFloydWarshall expected = new CompetitionFloydWarshall("input-I.txt", 60,70,84);
        assertEquals("constructor with valid input failed", expected.slowestSpeed, 60);
    }

    @Test
    public void testFloyWar() {
        CompetitionFloydWarshall expected = new CompetitionFloydWarshall("tinyEWD.txt", 50,80,60);
        assertEquals("Test with TINYEWD", 38, expected.timeRequiredforCompetition());

        CompetitionFloydWarshall expected1 = new CompetitionFloydWarshall("ahjhsjfhsj.txt", 50, 80, 60);
        assertEquals("Test with invalid filename", -1, expected1.timeRequiredforCompetition());

        CompetitionFloydWarshall expected2 = new CompetitionFloydWarshall("tinyEWD.txt", -1, 80, 60);
        assertEquals("Test with negative speed", -1, expected2.timeRequiredforCompetition());

        CompetitionFloydWarshall expected3 = new CompetitionFloydWarshall(null, 50, 80, 60);
        assertEquals("Test with null filename", -1, expected3.timeRequiredforCompetition());

        CompetitionFloydWarshall expected4 = new CompetitionFloydWarshall("tinyEWD-2.txt", 50, 80, 60);
        assertEquals("Test with node with no path", -1, expected4.timeRequiredforCompetition());

        CompetitionFloydWarshall expected5 = new CompetitionFloydWarshall("input-J.txt", 98, 70, 84);
        assertEquals("Test with speed in range", -1, expected5.timeRequiredforCompetition());

        CompetitionFloydWarshall expected6 = new CompetitionFloydWarshall("tinyEWD.txt", 5, 80, 60);
        assertEquals("Test with speed outside range", -1, expected6.timeRequiredforCompetition());
    }

   

}