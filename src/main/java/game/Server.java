package game;

import java.io.IOException;
import javax.servlet.http.*;

public class Server extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, world!");
    }
}
