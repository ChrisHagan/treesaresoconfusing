import ai.*;
import java.util.List;
import java.util.Arrays;
import transport.Transport;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.rules.MethodRule;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class DijkstraTest{
    private PlayBoard board;
    /*
      @Rule
      public MethodRule benchmarkRun = new BenchmarkRule();
    */
    @Before
    public void buildGraph(){
        board = new PlayBoard(10,10);
    }
    @Test
    public void graphIsLegible(){
        assertEquals(100,board.vertices.length);
        assertEquals(4,board.vertices[4].x);
        assertEquals(0,board.vertices[4].y);
        assertEquals(4,board.vertices[14].x);
        assertEquals(1,board.vertices[14].y);
        /*
          8 * 10 * 10 vertices
          - 5 * 4 on the corners
          - 3 * 8 * 4 on the sides
          = 800 - 20 - 96
          = 684 edges
        */
	assertEquals(684,board.edgeSize());
    }
    @Test
    public void graphSolves(){
	PointSet solution = board.solve(9,9,0,0);
        assertEquals(10,solution.size());
	List<Transport.Point> path = solution.values();
	assertEquals(9,path.get(0).x);
	assertEquals(9,path.get(0).y);
	assertEquals(0,path.get(9).x);
	assertEquals(0,path.get(9).y);
    }
    @Test
    public void reverseGraphSolves(){
	PointSet solution = board.solve(0,0,9,9);
        assertEquals(10,solution.size());
	List<Transport.Point> path = solution.values();
	assertEquals(0,path.get(0).x);
	assertEquals(0,path.get(0).y);
	assertEquals(9,path.get(9).x);
	assertEquals(9,path.get(9).y);
    }
    @Test
    public void obstacles(){
	int width = 10;
	int height = 10;
	PointSet obstacles = new PointSet(width,height);
	for(Transport.Point p : Arrays.asList(
					      new Transport.Point(1l,1l),
					      new Transport.Point(1l,2l),
					      new Transport.Point(1l,3l),
					      new Transport.Point(1l,4l)
					      )){
	    obstacles.add(p);
	}
	board = new PlayBoard(width,height, obstacles);
	assertEquals(100,board.vertices.length);
	assertEquals(626,board.edgeSize());
    }
    @Test
    public void evolution(){
	long seed = 1000l;
	assertEquals(0,board.obstacles.size());
	board = board.evolve(seed);
	assertEquals(10,board.obstacles.size());
    }
}

