package game;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.*;
import com.google.appengine.api.channel.*;
import java.util.logging.Logger;
import com.google.gson.Gson;
import transport.*;

public class TickServer extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	PreparedQuery pq = DatastoreServiceFactory.getDatastoreService().prepare(new Query(Transport.PLAYER));
	ChannelService channelService = ChannelServiceFactory.getChannelService();
	
	Gson gson = new Gson();
	for(Entity result : pq.asIterable()){
	    String identity = (String) result.getProperty("identity");
	    Long x = (Long) result.getProperty("x");
	    Long y = (Long) result.getProperty("y");
	    Transport.PlayerUpdate update = new Transport.PlayerUpdate(identity,x,y);
	    channelService.sendMessage(new ChannelMessage(identity,gson.toJson(update)));
	}
    }
}
