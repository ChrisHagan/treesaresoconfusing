package ai;

import java.util.Collections;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Sorter{
    private static Random r = new Random();
    private static void swap(int[] backing, int i, int j){
        int tmp;
        tmp = backing[i];
        backing[i] = backing[j];
        backing[j] = tmp;
    }
    private static int[] _xs;
    public static int[] quicker(int[] xs){
	_xs = xs;
	_quicker(0,xs.length-1);
	return _xs;
    }
    private static void _quicker(int low, int high){
	int i = low, j = high;
	int pivot = _xs[low + (high-low)/2];
	while(i <= j){
	    while(_xs[i] < pivot){
		i++;
	    }
	    while(_xs[j] > pivot){
		j--;
	    }
	    if(i <= j){
		swap(_xs,i,j);
		i++;
		j--;
	    }
	}
	if(low < j){
	    _quicker(low,j);
	}
	if(i < high){
	    _quicker(i,high);
	}
    }
    public static int[] quick(int[] xs){
	List<Integer> l = new ArrayList<Integer>();
	for(int x : xs){
	    l.add(x);
	}
	List<Integer> sorted = _quick(l);
	int i = 0;
	for(Integer x : sorted){
	    xs[i++] = x;
	}
	return xs;
    }
    private static List<Integer> _quick(List<Integer> xs){
        int n = xs.size();
        if(n <= 1){
            return xs;
        }
        int pivot = xs.get(r.nextInt(n));
	List left = new ArrayList<Integer>();
	List center = new ArrayList<Integer>();
	List right = new ArrayList<Integer>();
	for(int x : xs){
	    if(x < pivot){
		left.add(x);
	    }
	    else if(x > pivot){
		right.add(x);
	    }
	    else{
		center.add(x);
	    }
	}
	left = _quick(left);
	right = _quick(right);
	left.addAll(center);
	left.addAll(right);
	return left;
    }
    public static int[] insertion(int[] ns){
        int j,mobile;
        for(int i = 0; i < ns.length; i++){
            mobile = ns[i];
            j = i;
            while(j > 0 && ns[j-1] > mobile){
                ns[j] = ns[j-1];
                j--;
            }
            ns[j] = mobile;
        }
        return ns;
    }
    public static int[] tree(int[] ns){
        final BinarySearchTree<Integer> t  = new BinarySearchTree<Integer>();
        for(int n : ns){
            t.insert(n);
        }
        BinarySearchTree.Visitor a = t.new Visitor<int[]>(){
            private int[] result = new int[t.size()];
            private int index = 0;
            public int[] getResult(){
                return result;
            }
            public void visit(BinarySearchTree.Node node){
                result[index] = (Integer) node.getDatum();
                index++;
            }
        };
        t.apply(a);
        return (int[]) a.getResult();
    }
    public static int[] heap(int[] nums){
        HeapBasedPriorityQueue q = new HeapBasedPriorityQueue<Integer>();
        q.setDominanceStrategy(q.new DominanceStrategy(){
                @Override
                    public boolean dominates(HeapBasedPriorityQueue.Node a, HeapBasedPriorityQueue.Node b){
                    return a.priority < b.priority;
                }
            });
        for(int num : nums){
            q.prioritize(num,num);
        }
        int i = 0;
        while(q.size() > 0){
            nums[i] = (Integer)q.popDominant();
            i++;
        }
        return nums;
    }
    private static void print(int[] nums){
        for(int n : nums){
            System.out.print(n);
            System.out.print(" ");
        }
        System.out.println();
    }
    public static int[] merge(int[] nums){
        int n = nums.length;
        if(n <= 1){
            return nums;
        }
        int mid = n / 2;
        int[] left = new int[mid];
        int[] right = new int[n - mid];
        int i = 0;
        for(; i < mid; i++){
            left[i] = nums[i];
        }
        for(int j = 0; j < right.length; j++){
            right[j] = nums[i];
            i++;
        }
        left = merge(left);
        right = merge(right);
        if(left[left.length-1] <= right[0]){
            return concat(left,right);
        }
        return merge(left,right);
    }
    private static int[] concat(int[] left, int[] right){
        int n = left.length+right.length;
        int[] result = new int[n];
        int i = 0;
        for(; i < left.length; i++){
            result[i] = left[i];
        }
        for(int j = 0; j < right.length;j++){
            result[i] = right[j];
            i++;
        }
        return result;
    }
    private static int[] merge(int[] left, int[] right){
        int[] result = new int[left.length+right.length];
        int j = 0;
        int k = 0;
        int m = 0;
        while(j < left.length && k < right.length){
            if(left[j] <= right[k]){
                result[m++] = left[j++];
            }
            else{
                result[m++] = right[k++];
            }
        }
        for(; j < left.length; j++){
            result[m++] = left[j];
        }
        for(; k < right.length; k++){
            result[m++] = right[k];
        }
        return result;
    }
    public static int[] specializedHeap(int[] nums){
        IntegerMinHeap heap = new IntegerMinHeap(nums.length);
        for(int num : nums){
            heap.push(num);
        }
        for(int i = 0; i < nums.length; i++){
            nums[i] = heap.pop();
        }
        return nums;
    }
    static class IntegerMinHeap{
        private int[] backing;
        private int size;
        public IntegerMinHeap(int capacity){
            backing = new int[capacity];
            size = 0;
        }
        private void siftUp(int index){
            if(index == 0) {
                return;
            }
            int parent = (index - 1) / 2;
            if(backing[index] < backing[parent]){
                swap(parent,index);
                siftUp(parent);
            }
        }
        private void siftDown(int index){
            final int leftChild = 2 * index + 1;
            final int rightChild = 2 * index + 2;

            if( rightChild >= size && leftChild >= size ) return;

            final int smallestChild =
                backing[ rightChild ] > backing[ leftChild ] ? leftChild : rightChild;

            if( backing[ index ] > backing[ smallestChild ] ) {
                swap(smallestChild,index );
                siftDown(smallestChild);
            }
        }
        public void push(int num){
            backing[size] = num;
            siftUp(size);
            size++;
        }
        public int pop(){
            int min = backing[0];
            backing[0] = backing[size-1];
            if( --size > 0){
                siftDown(0);
            }
            return min;
        }
        private void swap(int i, int j){
            int tmp;
            tmp = backing[i];
            backing[i] = backing[j];
            backing[j] = tmp;
        }
    }
}
