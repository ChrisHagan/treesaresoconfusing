package ai;

public class BinarySearchTree<T extends Comparable<T>> {
    private String logf(Object... os){
        StringBuilder b = new StringBuilder();
        for(Object o : os){
            if(o == null){
                b.append("null");
            }
            else{
                b.append(o.toString());
            }
            b.append(" ");
        }
        return b.toString();
    }
    private void log(Object... os){
        System.out.println(logf(os));
    }
    public Node root;

    public int size(){
        if(root == null){
            return 0;
        }
        else{
            return root.size(0);
        }
    }

    public int depth(){
        if(root == null){
            return 0;
        }
        else{
            return root.depth(0);
        }
    }

    public void apply(Visitor visitor){
        if(root == null){
            return;
        }
        else{
            root.accept(visitor);
        }
    }

    public void insert(T datum) {
        if(root == null){
            root = new Node(datum);
        }
        else{
            root.insert(datum);
        }
    }

    public void delete(T datum){
        root = delete(datum,root);
    }

    private Node delete(T query,Node node){
	if(node == null){
	    return null;
	}
        int comparison = query.compareTo(node.datum);
        if(comparison == 0){
            if(node.left == null && node.right == null){
                return null;
            }
            else if(node.left == null){
                return node.right;
            }
            else if(node.right == null){
                return node.left;
            }
	    else{
		Node replacement = new Node(node.left.rightmost().getDatum());
		replacement.right = node.right;
		replacement.left = delete(replacement.getDatum(),node.left);
		return replacement;
	    }
        }
        if(comparison > 0){
            node.right = delete(query,node.right);
        }
        else{
            node.left = delete(query,node.left);
        }
        return node;
    }

    public boolean contains(T datum){
        if(root == null){
            return false;
        }
        return root.contains(datum);
    }

    public String toString(){
        return logf(root);
    }

    public abstract class Visitor<T>{
        public abstract T getResult();
        public abstract void visit(Node node);
    }

    public class Node {
        private Node left;
        private Node right;
        private final T datum;

        public Node(T datum){
            this.datum = datum;
        }

        public T getDatum(){
            return datum;
        }

        public String toString(){
            return logf(datum,left,right);
        }

        public void accept(Visitor v){
            if(left != null){
                left.accept(v);
            }
            v.visit(this);
            if(right != null){
                right.accept(v);
            }
        }

        public int depth(int start){
            int inc = start + 1;
            if(left == null && right == null){
                return inc;
            }
            else if(left == null){
                return right.depth(inc);
            }
            else if(right == null){
                return left.depth(inc);
            }
            else{
                return Math.max(left.depth(inc),right.depth(inc));
            }
        }



        public int size(int start){
            int inc = start + 1;
            if(left == null && right == null){
                return inc;
            }
            else if(left == null){
                return right.size(inc);
            }
            else if(right == null){
                return left.size(inc);
            }
            else{
                return left.size(right.size(inc));
            }
        }

        public void insert(T newDatum){
            if(newDatum == datum){
                return;
            }
            int comparison = newDatum.compareTo(datum);
            if(comparison < 0){
                if(left == null){
                    left = new Node(newDatum);
                }
                else{
                    left.insert(newDatum);
                }
            }
            else{
                if(right == null){
                    right = new Node(newDatum);
                }
                else{
                    right.insert(newDatum);
                }
            }
        }

        public Node rightmost(){
            if(right == null){
                return this;
            }
            return right.rightmost();
        }

        public Node leftmost(){
            if(left == null){
                return this;
            }
            return left.leftmost();
        }

        public boolean contains(T query){
            if(datum == query){
                return true;
            }
            int comparison = query.compareTo(datum);
            if(comparison < 0){
                if(left == null){
                    return false;
                }
                return left.contains(query);
            }
            else{
                if(right == null){
                    return false;
                }
                return right.contains(query);
            }
        }
    }
}
