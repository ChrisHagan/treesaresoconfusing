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
          = 686 edges
        */
    }
    @Test
        public void graphSolves(){
        assertEquals(10,board.solve(0,0,9,9).size());
    }
    @Test
        public void bulk(){
        board = new PlayBoard(100,100);
        assertEquals(100,board.solve(0,0,99,99).size());
    }
}

