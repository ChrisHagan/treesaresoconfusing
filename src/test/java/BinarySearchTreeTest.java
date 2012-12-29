import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import ai.*;

public class BinarySearchTreeTest{
    private BinarySearchTree s;
    private BinarySearchTree.Visitor counter;
    @Before
        public void buildEmptyTree(){
        s = new BinarySearchTree<Integer>();
        counter = s.new Visitor(){
                private int visited = 0;
                public Integer getResult(){
                    return visited;
                }
                public void visit(BinarySearchTree.Node n){
                    visited += 1;
                }
            };
    }
    @Test
        public void noRootContainsNothing(){
        assertFalse(s.contains(0));
        assertFalse(s.contains(1));
    }
    @Test
        public void rootCountsForContains(){
        s.insert(2);
        assertTrue(s.contains(2));
    }
    @Test
        public void theRootIsStillFindableAfterAnotherInsertion(){
        s.insert(3);
        s.insert(2);
        assertTrue(s.contains(2));
        assertTrue(s.contains(3));
    }
    @Test
        public void treeDoesNotContainsAbsentMember(){
        s.insert(2);
        assertFalse(s.contains(3));
    }
    @Test
        public void emptyTreeHasSizeZero(){
        assertEquals(s.size(),0);
    }
    @Test
        public void treeMeasuresSize(){
        s.insert(2);
        assertEquals(1,s.size());
        s.insert(3);
        s.insert(4);
        s.insert(5);
        assertEquals(4,s.size());
    }
    @Test
        public void treeDiscardsDuplicates(){
        s.insert(2);
        s.insert(2);
        s.insert(3);
        s.insert(3);
        assertEquals(2,s.size());
    }

    @Test
        public void emptyTreeHasDepthZero(){
        assertEquals(0,s.depth());
    }
    @Test
        public void treeWithOnlyRootHasDepthOne(){
        s.insert(2);
        assertEquals(1,s.depth());
    }
    @Test
        public void treeWithEvenInsertsHasBalancedDepth(){
        s.insert(3);
        s.insert(2);
        s.insert(4);
        s.insert(1);
        s.insert(5);
        assertEquals(3,s.depth());
    }

    @Test
        public void treeWithPresortedInsertsHasUnbalancedDepth(){
        s.insert(1);
        s.insert(2);
        s.insert(3);
        s.insert(4);
        s.insert(5);
        assertEquals(5,s.depth());
    }

    @Test
        public void extremetiesFindAppropriateExtreme(){
        s.insert(4);
        s.insert(2);
        s.insert(3);
        s.insert(7);
        s.insert(1);
        assertEquals(7,s.root.rightmost().getDatum());
        assertEquals(1,s.root.leftmost().getDatum());
    }

    @Test
        public void deletionRemovesRoot(){
        s.insert(2);
        s.insert(1);
        s.insert(3);
        s.insert(4);
        s.delete(2);
        assertFalse("2 was deleted, s should not contain it",s.contains(2));
        assertTrue("Contains 1",s.contains(1));
        assertTrue("Contains 3",s.contains(3));
        assertTrue("Contains 4",s.contains(4));
    }
    @Test
        public void deletionRotatesSubtree(){
        s.insert(2);
        s.insert(1);
        s.insert(3);
        s.insert(4);
        s.insert(5);
        s.insert(0);
        s.insert(9);
        s.delete(3);
        assertTrue("Contains 1",s.contains(1));
        assertFalse("Contains 3",s.contains(3));
        assertTrue("Contains 4",s.contains(4));
        assertTrue("Contains 9",s.contains(9));
    }
    @Test
	public void deletionRemovesLeaf(){
	s.insert(1);
	s.insert(2);
	s.insert(3);
	s.insert(4);
	s.delete(4);
	assertTrue(s.contains(1));
	assertTrue(s.contains(2));
	assertTrue(s.contains(3));
	assertFalse(s.contains(4));
    }

    @Test
        public void treeAcceptsVisitor(){
        s.insert(1);
        s.insert(2);
        s.insert(3);
        s.insert(4);
        s.insert(5);
        assertEquals(5,s.size());
        s.apply(counter);
        assertEquals(5,counter.getResult());
    }
    @Test
        public void visitorSeesSortedElements(){
        s.insert(3);
        s.insert(5);
        s.insert(1);
        s.insert(4);
        s.insert(2);
        BinarySearchTree.Visitor sortChecker = s.new Visitor(){
                private Comparable highWaterMark;
                private boolean sorted = true;
                public Object getResult(){
                    return sorted;
                }
                public void visit(BinarySearchTree.Node n){
                    Comparable d = n.getDatum();
                    if(highWaterMark != null){
                        sorted = sorted && highWaterMark.compareTo(n.getDatum()) < 0;
                    }
                    highWaterMark = d;
                }
            };
        s.apply(sortChecker);
        assertEquals(true,sortChecker.getResult());
    }
}
