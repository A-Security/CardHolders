package personcontrol;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TransAction", urlPatterns = {"/TransAction"})
public class TransAction extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("ok") != null) {
            PcWSOAdapters adp = new PcWSOAdapters();
            String[] vipch = request.getParameterValues("vipch");
            adp.setCHsVIP(vipch);
            String[] notvipch = request.getParameterValues("notvipch");
            adp.remCHsVIP(notvipch);
        }
        if (request.getParameter("cancel") != null) {
            request.getRequestDispatcher("/person_control.jsp").forward(request, response);
        }
        request.getRequestDispatcher("/person_control.jsp").forward(request, response);
    }
}
