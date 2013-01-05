package ai;

import java.util.Iterator;
import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import transport.Transport;

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
	    if(obstacles.contains(x,y) || obstacles.contains(terminus.x,terminus.y)){
		return;
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
    public PointSet obstacles;
    public PlayBoard(int width, int height){
	this(width,height,new PointSet(width,height));
    }
    public PlayBoard(int width, int height, PointSet obstacles){
        this.width = width;
        this.height = height;
	this.obstacles = obstacles;
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
    public int edgeSize(){
	int n = 0;
	for(Vertex v : vertices){
	    n += v.edges.size();
	}
	return n;
    }
    public PlayBoard evolve(long seed){
	PointSet newObstacles = new PointSet(width,height);
	Random rand = new Random(seed);
	for(int i = 0; i < 10; i++){
	    newObstacles.add(new Transport.Point(rand.nextInt(width), rand.nextInt(height)));
	}
	return new PlayBoard(width,height,newObstacles);
    }
    public PointSet solve(int sx, int sy, int dx, int dy){
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
	PointSet result = new PointSet(width,height);
	LinkedList<Vertex> forward = new LinkedList<Vertex>();
        for(Vertex step = end; step != null; step = step.prev){
	    forward.add(step);
	}
	Iterator<Vertex> vs = forward.descendingIterator();
	while(vs.hasNext()){
	    Vertex v = vs.next();
            result.add(new Transport.Point(v.x,v.y));
        }
	if(result.contains(sx,sy)){
	    return result;
	}
	else{
	    return new PointSet(width,height);
	}
    }
}
