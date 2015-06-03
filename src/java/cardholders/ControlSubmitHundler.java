package cardholders;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ControlSubmitHundler", urlPatterns = {"/ControlSubmitHundler"})
public class ControlSubmitHundler extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("ok") != null) {
            ChsCfg cfg = (ChsCfg)request.getSession().getAttribute("cfg");
            GRAdapter adp = new GRAdapter(cfg);
            String[] vipch = request.getParameterValues("vipch");
            adp.setCHsVipValue(vipch, true);
            String[] notvipch = request.getParameterValues("remvipch");
            adp.setCHsVipValue(notvipch, false);
        }
    }
}
