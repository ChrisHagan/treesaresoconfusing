package ai;

import java.util.logging.Logger;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import transport.Transport;

public class PointSet{
    private final int width;
    private final int height;
    private final int capacity;
    private final Transport.Point[] points;
    private final List<Transport.Point> inserts = new LinkedList<Transport.Point>();

    public PointSet(int width, int height){
	this.width = width;
	this.height = height;
	this.capacity = width * height;
	this.points = new Transport.Point[capacity];
    }
    public int inline(int x, int y){
	return (y * width) + x;
    }
    public Transport.Point get(int x, int y){
	return points[inline(x,y)];
    }
    public void add(Transport.Point value){
	int i = inline(value.x,value.y);
	if(i < capacity && i >= 0){
	    points[i] = value;
	    inserts.add(value);
	}
    }
    public boolean contains(int x, int y){
	return get(x,y) != null;
    }
    public List<Transport.Point> values(){
	return inserts;
    }
    @Override
    public String toString(){
	StringBuilder sb = new StringBuilder();
	for(Transport.Point p : values()){
	    sb.append(String.format("%s,%s ",p.x,p.y));
	}
	return sb.toString();
    }
    public int size(){
	int n = 0;
	for(Transport.Point p : points){
	    if(p != null){
		n++;
	    }
	}
	return n;
    }
}
