import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class HeapBasedPriorityQueueTest{
    private HeapBasedPriorityQueue s;
    @Before
        public void buildEmptyTree(){
        s = new HeapBasedPriorityQueue<Integer>();
    }
    @Test
	public void popReturnsNullOnEmptyList(){
	assertEquals(null,s.popDominant());
    }
    @Test
	public void popReturnsDominantItem(){
	s.add(20,5);
	s.add(2,10);
	s.add(9,20);
	s.add(15,40);
	s.add(1,70);
	s.add(3,60);
	s.add(0,90);
	s.add(18,50);
	
	assertEquals(90,s.popDominant());
	assertEquals(70,s.popDominant());
	assertEquals(10,s.popDominant());
	assertEquals(60,s.popDominant());
	assertEquals(20,s.popDominant());
	assertEquals(40,s.popDominant());
	assertEquals(50,s.popDominant());
	assertEquals(5,s.popDominant());
    }
}
