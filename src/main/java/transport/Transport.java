package transport;

import game.*;
import ai.PlayBoard;
import java.util.List;

public class Transport{
    public static final String PLAYER = "player";
    public static final String PATH = "path";
    public static final String SCENERY = "scenery";

    public static class PlayerUpdate{
	private String identity;
	private Long x;
	private Long y;
	private String type = PLAYER;

	public PlayerUpdate(String identity, Long x, Long y){
	    this.identity = identity;
	    this.x = x;
	    this.y = y;
	}
    }
    public static class PathUpdate{
	private String identity;
	private List<PlayBoard.Vertex> vertices;
	private String type = PATH;
	public PathUpdate(String identity, List<PlayBoard.Vertex> vertices){
	    this.identity = identity;
	    this.vertices = vertices;
	}
    }
    public static class SceneryUpdate{
	private Long x;
	private Long y;
	private String type = SCENERY;
    }
}
