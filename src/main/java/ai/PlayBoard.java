package ai;

import java.util.List;
import java.util.LinkedList;

public class PlayBoard{
    public int width;
    public int height;
    public class Vertex{
        public int x;
        public int y;
        public transient int distance;
        public transient List<Edge> edges = new LinkedList<Edge>();
        public transient Vertex prev;
        public Vertex(int x, int y){
            this.x = x;
            this.y = y;
        }
        public void link(int xDelta, int yDelta){
            boolean diagonal = xDelta != 0 && yDelta != 0;
	    int terminusIndex = ((y + yDelta) * width) + x + xDelta;
	    Vertex terminus =  vertices[terminusIndex];
	    if(this == terminus){
		throw new RuntimeException(String.format("Illegal edge attempted to create a cycle at (%s,%s) : %s @ %s",xDelta,yDelta,this,terminusIndex));
	    }
            edges.add(new Edge(terminus,diagonal? 14 : 10));
        }
        @Override
            public String toString(){
            return String.format("%s,%s",x,y);
        }
    }
    public class Edge{
        public Vertex terminus;
        public int distance;
        public Edge(Vertex terminus, int distance){
            this.terminus = terminus;
            this.distance = distance;
        }
    }
    public Vertex[] vertices;
    public PlayBoard(int width, int height){
        this.width = width;
        this.height = height;
        vertices = new Vertex[width * height];
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                vertices[row * width + col] = new Vertex(col,row);
            }
        }
        for(Vertex v : vertices){
            boolean left = v.x == 0;
            boolean right = v.x == width - 1;
            boolean top = v.y == 0;
            boolean bottom = v.y == height -1;
            if(!left){
                v.link(-1,0);
                if(!top){
                    v.link(-1,-1);
                }
                if(!bottom){
                    v.link(-1,1);
                }
            }
            if(!top){
                v.link(0,-1);
            }
            if(!bottom){
                v.link(0,1);
            }
            if(!right){
                v.link(1,0);
                if(!top){
                    v.link(1,-1);
                }
                if(!bottom){
                    v.link(1,1);
                }
            }
        }
    }
    public List<Vertex> solve(int sx, int sy, int dx, int dy){
        HeapBasedPriorityQueue<Vertex> unsolved = new HeapBasedPriorityQueue<Vertex>();
        Vertex start = vertices[sy * width + sx];
        Vertex end = vertices[dy * width + dx];
	Vertex current = start;
        for(Vertex v : vertices){
            v.distance = Integer.MAX_VALUE;
        }
        current.distance = 0;
	unsolved.prioritize(current,current.distance);
        while(unsolved.size() > 0){
            current = unsolved.popDominant();
            for(Edge e : current.edges){
                Vertex neighbour = e.terminus;
                int tendril = current.distance + e.distance;
                if(tendril < neighbour.distance){
                    neighbour.distance = tendril;
                    neighbour.prev = current;
                    unsolved.prioritize(neighbour,tendril);
                }
            }
        }
        List<Vertex> result = new LinkedList<Vertex>();
	int cap = 0;
        for(Vertex step = end; step != null; step = step.prev){
            result.add(step);
	    if(step == start){
		return result;
	    }
	    if(cap++ > 1000){
		break;
	    }
        }
        return result;
    }
}
