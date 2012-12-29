package game;

import java.util.logging.Logger;
import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.channel.*;
import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import transport.*;
import ai.*;

public class RocketServer extends HttpServlet{
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
	String username = req.getUserPrincipal().getName();
	int x = Integer.parseInt(req.getParameter("x"));
	int y = Integer.parseInt(req.getParameter("y"));
	Query q = new Query(Transport.PLAYER).setFilter(new Query.FilterPredicate("identity",
										  Query.FilterOperator.EQUAL,
										  username));
	DatastoreService store = DatastoreServiceFactory.getDatastoreService();
	Entity entity = store.prepare(q).asSingleEntity();
	if(entity != null){
	    int sx = ((Long) entity.getProperty("x")).intValue();
	    int sy = ((Long) entity.getProperty("y")).intValue();
	    Transport.PathUpdate update = new Transport.PathUpdate(username,new PlayBoard(20,20).solve(sx,sy,x,y));
	    entity.setProperty("x",x);
	    entity.setProperty("y",y);
	    store.put(entity);
	    Gson gson = new Gson();
	    ChannelServiceFactory.getChannelService().sendMessage(new ChannelMessage(username, gson.toJson(update)));
	}
    }
}
