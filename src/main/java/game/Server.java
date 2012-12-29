package game;

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
import transport.*;

public class Server extends HttpServlet{
    private static final String PATH = "/board.html";
    private static final Charset ENCODING = Charsets.UTF_8;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        resp.setContentType("text/html");

	String username = req.getUserPrincipal().getName();

	String token = ChannelServiceFactory.getChannelService().createChannel(username);
	String tokenized = CharStreams.toString(new InputStreamReader(getServletContext().getResourceAsStream(PATH), ENCODING)).replace("TOKEN",token);

	Query q = new Query(Transport.PLAYER).setFilter(new Query.FilterPredicate("identity",
										  Query.FilterOperator.EQUAL,
										  username));
	DatastoreService store = DatastoreServiceFactory.getDatastoreService();
	if(store.prepare(q).asSingleEntity() == null){
	    Entity player = new Entity(Transport.PLAYER);
	    player.setProperty("identity",username);
	    player.setProperty("x",10);
	    player.setProperty("y",10);
	    store.put(player);
	}

	Logger.getLogger("Server").info(String.format("Established channel: %s -> %s",username,token));

	OutputStream out = resp.getOutputStream();
	out.write(ENCODING.encode(tokenized).array());
	out.flush();
    }
}
