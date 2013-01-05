package ai;

import java.util.List;
import java.util.LinkedList;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.rules.MethodRule;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import transport.*;

public class PointSetTest{
    private PointSet pointSet;

    @Before
    public void construct(){
	pointSet = new PointSet(20,20);
    }
    @Test
    public void addOutsideBoundsIgnoresPoint(){
	pointSet.add(new Transport.Point(21,21));
    }
    @Test
    public void contains(){
	assertEquals(0,pointSet.size());
	pointSet.add(new Transport.Point(0,0));
	assertEquals(1,pointSet.size());
	assertTrue(pointSet.contains(0,0));
	assertTrue(pointSet.get(0,0) != null);
    }
    @Test
    public void stringify(){
	assertEquals("",pointSet.toString());
	pointSet.add(new Transport.Point(0,0));
	assertEquals("0,0 ",pointSet.toString());
    }
    @Test
    public void orderly(){
	pointSet.add(new Transport.Point(0,0));
	pointSet.add(new Transport.Point(0,1));
	pointSet.add(new Transport.Point(0,2));
	pointSet.add(new Transport.Point(0,3));
	assertEquals("0,0 0,1 0,2 0,3 ",pointSet.toString());
	assertEquals(pointSet.toString(),new Transport.Points(pointSet).toString());
    }
    @Test
    public void prev(){
	pointSet.add(new Transport.Point(0,0));
	pointSet.add(new Transport.Point(0,1));
	pointSet.add(new Transport.Point(0,2));
	pointSet.add(new Transport.Point(0,3));
	Transport.Point prev = new Transport.Points(pointSet).tail();
	assertEquals(3,prev.y);
    }
}
