package game;

import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.*;
import com.google.appengine.api.channel.*;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import transport.*;
import com.google.appengine.api.taskqueue.QueueFactory;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import ai.*;

public class TickServer extends HttpServlet{
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	tick(req,resp);
    }
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	tick(req,resp);
    }
    private void tick(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	DatastoreService store = DatastoreServiceFactory.getDatastoreService();
	String username = req.getParameter("identity");
	PreparedQuery pq = store.prepare(new Query(Transport.PLAYER).setFilter(new Query.FilterPredicate("identity",
													 Query.FilterOperator.EQUAL,
													 username)));
	ChannelService channelService = ChannelServiceFactory.getChannelService();
	
	Random rand = new Random();
	Gson gson = new Gson();
	PreparedQuery sq = store.prepare(new Query(Transport.SCENERY));
	Entity player = pq.asSingleEntity();
	String mapId = (String)player.getProperty("map");
	Query mq = new Query(Transport.MAP).setFilter(new Query.FilterPredicate("identity",
										Query.FilterOperator.EQUAL,
										mapId));
	Entity map = store.prepare(mq).asSingleEntity();
	int width = ((Long) map.getProperty("width")).intValue();
	int height = ((Long) map.getProperty("height")).intValue();
	PointSet scene = new PointSet(width,height);
	String identity = (String) player.getProperty("identity");
	for(Entity scenery : sq.asIterable()){
	    scene.add(new Transport.Point((Long) scenery.getProperty("x"), (Long) scenery.getProperty("y")));
	}
	Transport.FoodUpdate food = new Transport.FoodUpdate(rand.nextInt(width),rand.nextInt(height));
	channelService.sendMessage(new ChannelMessage(identity,gson.toJson(new Transport.SceneryUpdate(scene))));
	channelService.sendMessage(new ChannelMessage(identity, gson.toJson(food)));

	int x = food.x;
	int y = food.y;
	int speed = ((Long) player.getProperty("speed")).intValue();
	String pathProp = (String) player.getProperty("path");
	Transport.Points points = gson.fromJson(pathProp, Transport.Points.class);
	Transport.Point prev = points.tail();
	int sx = prev.x;
	int sy = prev.y;
	PointSet solution = new PlayBoard(width,height,scene).solve(sx,sy,x,y);
	if(solution.size() > 0){
	    Transport.PlayerUpdate update = new Transport.PlayerUpdate(identity,speed,solution);
	    String json = gson.toJson(update);
	    player.setProperty("path", gson.toJson(new Transport.Points(solution)));
	    store.put(player);
	    channelService.sendMessage(new ChannelMessage(identity,json)); 
	    int delay = solution.size() * (1000 / speed);
	    QueueFactory.getDefaultQueue().add(withUrl("/tick").param("identity",username).countdownMillis(delay));
	    Logger.getLogger("TickServer").info(String.format("Ticking %s at speed %s again in %s",username,speed,delay));
	}
	else{
	    QueueFactory.getDefaultQueue().add(withUrl("/tick").param("identity",username));
	    Logger.getLogger("TickServer").info(String.format("No path.  Ticking immediately %s immediately.",username));
	}
    }
}
