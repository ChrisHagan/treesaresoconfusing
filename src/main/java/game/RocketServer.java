package game;

import java.util.logging.Logger;
import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class RocketServer extends HttpServlet{
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
	String username = req.getUserPrincipal().getName();
	String action = String.format("Req %s -> x:%s, y:%s",username,req.getParameter("x"),req.getParameter("y"));
	Logger.getLogger("RocketServer").info(action);
	ChannelServiceFactory.getChannelService().sendMessage(new ChannelMessage(username,action));
    }
}
