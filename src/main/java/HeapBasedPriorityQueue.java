import java.util.Vector;
import java.util.Collections;

class HeapBasedPriorityQueue<T> {
    private Vector<Node> backing = new Vector<Node>();
    public class Node {
        public int priority;
        public T value;
        public Node(int priority, T value){
            this.value = value;
            this.priority = priority;
        }
        @Override
            public String toString(){
            return String.format("%s@%s",value,priority);
        }
    }

    private DominanceStrategy dominanceStrategy;
    public abstract class DominanceStrategy{
        public abstract boolean dominates(Node a, Node b);
    }
    public class MinDominanceStrategy extends DominanceStrategy{
        @Override
            public boolean dominates(Node a, Node b){
            return a.priority < b.priority;
        }
    }

    public HeapBasedPriorityQueue(){
        this.dominanceStrategy = new MinDominanceStrategy();
    }

    public void setDominanceStrategy(DominanceStrategy strategy){
	this.dominanceStrategy = strategy;
    }

    public void add(int priority, T value) {
        backing.add(new Node(priority, value));
        upsift(size()-1);
    }

    public void prioritize(T value, int priority){
        for(int i = 0; i < backing.size(); i++){
	    Node n = backing.elementAt(i);
            if(n.value == value){
		Node match = backing.remove(i);
		break;
            }
        }
	add(priority, value);
    }

    public T popDominant() {
        if(backing.size() == 0){
            return null;
        }
        Node head = backing.firstElement();
        backing.removeElementAt(0);
        int n = backing.size();
        if(n > 1){
            Node mobile = backing.elementAt(n-1);
            backing.removeElementAt(n-1);
            int pos = 0;
            backing.add(pos,mobile);
            downsift(pos);
        }
        return head.value;
    }

    private static final int ROOT = 0;
    private void upsift(int index){
        if(index == ROOT) {
            return;
        }
        int parent = (index -1) / 2;
        if(dominanceStrategy.dominates(backing.elementAt(index),backing.elementAt(parent))){
            Collections.swap(backing,parent,index);
            upsift(parent);
        }
    }

    private void downsift(int index){
        if(index >= size() - 1){
            return;
        }
        int left = 2 * index + 1;
        int right = left + 1;
        int comparateeIndex;
        Node comparatee;
        Node current = backing.elementAt(index);
        if(left >= size()){
            return;
        }
        else if(left == size() - 1){
            comparateeIndex = left;
            comparatee = backing.elementAt(left);
        }
        else{
            Node leftChild = backing.elementAt(left);
            Node rightChild = backing.elementAt(right);
            comparateeIndex = right;
            comparatee = rightChild;
            if(dominanceStrategy.dominates(leftChild,rightChild)){
                comparatee = leftChild;
                comparateeIndex = left;
            }
        }
        if(dominanceStrategy.dominates(comparatee,current)){
            Collections.swap(backing,comparateeIndex,index);
            downsift(comparateeIndex);
        }
    }

    public int size(){
        return backing.size();
    }
    @Override
        public String toString(){
        StringBuilder buff = new StringBuilder();
        for(Node node : backing){
            buff.append(node.toString());
            buff.append(",");
        }
        return buff.toString();
    }
}