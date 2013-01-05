package game;

import java.util.Arrays;
import java.util.Random;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import com.google.common.io.CharStreams;
import java.nio.charset.Charset;
import com.google.common.base.Charsets;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import com.google.appengine.api.channel.*;
import java.util.logging.Logger;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.*;
import com.google.gson.Gson;
import com.google.appengine.api.taskqueue.QueueFactory;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import transport.*;
import ai.*;

public class Server extends HttpServlet{
    private static final String PATH = "/board.html";
    private static final Charset ENCODING = Charsets.UTF_8;

    private static void generateScenery(String mapId, int x, int y, int nx, int ny, DatastoreService store){
	if(x != nx && y != ny && x != y){//Clear the diagonal line for escape
	    Entity obstacle = new Entity(Transport.SCENERY);
	    obstacle.setProperty("x",x);
	    obstacle.setProperty("y",y);
	    obstacle.setProperty("map",mapId);
	    store.put(obstacle);
	}
    }

    private static Entity ensureMapExists(Entity player,DatastoreService store){
	Query mq = new Query(Transport.MAP);
	Entity map = store.prepare(mq).asSingleEntity();
	Random rand = new Random();
	if(map == null){
	    map = new Entity(Transport.MAP);
	    String mapId = "sandbox";
	    int width = 20;
	    int height = 20;
	    map.setProperty("width",width);
	    map.setProperty("height",height);
	    map.setProperty("identity",mapId);
	    store.put(map);
	    for(int j = 0; j < 8; j++){
		int sx = width / 2;
		int sy = height / 2;
		for(int i = 0; i < 100; i++){
		    switch(rand.nextInt(4)){
		    case 0:
			sx++;
			break;
		    case 1:
			sy++;
			break;
		    case 2:
			sx--;
			break;
		    case 3:
			sy--;
			break;
		    }
		    generateScenery(mapId,sx,sy,10,10,store);
		}
	    }
	    player.setProperty("map",mapId);
	    store.put(player);
	}
	return map;
    }

    private static Entity ensurePlayerExists(String username, DatastoreService store){
	Query q = new Query(Transport.PLAYER).setFilter(new Query.FilterPredicate("identity",
										  Query.FilterOperator.EQUAL,
										  username));
	Entity player = store.prepare(q).asSingleEntity();
	if(player == null){
	    Long x = 10l;
	    Long y = 10l;
	    player = new Entity(Transport.PLAYER);
	    player.setProperty("identity",username);
	    player.setProperty("speed",4);
	    PointSet points = new PointSet(20,20);
	    points.add(new Transport.Point(x,y));
	    player.setProperty("path",new Gson().toJson(new Transport.Points(points)));
	    store.put(player);

	    QueueFactory.getDefaultQueue().add(withUrl("/tick").param("identity",username));
	}
	return player;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        resp.setContentType("text/html");

	String username = req.getUserPrincipal().getName();

	String token = ChannelServiceFactory.getChannelService().createChannel(username);
	String tokenized = CharStreams.toString(new InputStreamReader(getServletContext().getResourceAsStream(PATH), ENCODING)).replace("TOKEN",token);

	DatastoreService store = DatastoreServiceFactory.getDatastoreService();
	Entity player = ensurePlayerExists(username,store);
	Entity map = ensureMapExists(player,store);

	OutputStream out = resp.getOutputStream();
	out.write(ENCODING.encode(tokenized).array());
	out.flush();
    }
}
