import static org.junit.Assert.*;
import org.junit.*;
import org.junit.rules.MethodRule;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import java.util.Random;
import java.util.Arrays;

public class SortingTest{
    int capacity = 100000;
    private int[] nums;
    private int[] sortedNums;
    /*
    @Rule
        public MethodRule benchmarkRun = new BenchmarkRule();
    */
    @Before
        public void BuildSortableSet(){
        Random r = new Random();
        nums = new int[capacity];
        sortedNums = new int[capacity];
        for(int i = 0; i < capacity; i++){
            nums[i] = r.nextInt();
            sortedNums[i] = nums[i];
        }
        Arrays.sort(sortedNums);
        BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
        for(int n : nums){
            tree.insert(n);
        }
        int distinct = tree.size();
        assertTrue(String.format("nums should contain distinct values (%s found)",distinct),distinct > 1);
    }
    private void assertSorted(int[] test){
        for(int i = 0; i < test.length; i++){
            assertEquals(sortedNums[i],test[i]);
        }
    }
    //@Test
        public void InsertSort(){
        assertSorted(Sorter.insertion(nums));
    }
    @Test
        public void TreeSort(){
        assertSorted(Sorter.tree(nums));
    }
    //@Test
	public void HeapSort(){
	assertSorted(Sorter.heap(nums));
    }
    @Test
	public void SpecializedHeapSort(){
	assertSorted(Sorter.specializedHeap(nums));
    }
    @Test
	public void MergeSort(){
	assertSorted(Sorter.merge(nums));
    }
    @Test
	public void QuickSort(){
	assertSorted(Sorter.quick(nums));
    }
    @Test
	public void InPlaceQuickSort(){
	assertSorted(Sorter.quicker(nums));
    }
}
