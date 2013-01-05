package transport;

import game.*;
import ai.*;
import java.util.List;
import java.util.LinkedList;

public class Transport{
    public static final String PLAYER = "player";
    public static final String PATH = "path";
    public static final String SCENERY = "scenery";
    public static final String FOOD = "food";
    public static final String MAP = "map";

    public static class PlayerUpdate{
	private String identity;
	private int speed;
	private List<Point> path;
	private String type = PLAYER;

	public PlayerUpdate(String identity, int speed, PointSet path){
	    this.identity = identity;
	    this.speed = speed;
	    this.path = path.values();
	}
    }
    public static class SceneryUpdate{
	private List<Point> points;
	private String type = SCENERY;
	public SceneryUpdate(PointSet points){
	    this.points = points.values();
	}
    }
    public static class Point{
	public int x;
	public int y;
	public Point(){}
	public Point(Long x, Long y){
	    this.x = x.intValue();
	    this.y = y.intValue();
	} 
	public Point(int x, int y){
	    this.x = x;
	    this.y = y;
	}
	public Point(PlayBoard.Vertex v){
	    this.x = v.x;
	    this.y = v.y;
	}
    }
    public static class Points{
	public List<Point> path;
	public Points(){}
	
	public Points(PointSet points){
	    this.path = points.values();
	}
	public Point tail(){
	    return path.get(path.size()-1);
	}
	public String toString(){
	    StringBuilder sb = new StringBuilder();
	    for(Point p : path){
		sb.append(String.format("%s,%s ",p.x,p.y));
	    }
	    return sb.toString();
	}
    }
    public static class FoodUpdate{
	public int x;
	public int y;
	private String type = FOOD;
	public FoodUpdate(int x, int y){
	    this.x = x;
	    this.y = y;
	}
    }
}
