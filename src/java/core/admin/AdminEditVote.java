package core.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AdminEditVote", urlPatterns = {"/AdminEditVote"})
public class AdminEditVote extends HttpServlet 
{
    public String voteID;
    public String subject;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");        
        PrintWriter out=response.getWriter();
                
        String[] parts = request.getParameter("key").split(";");
        voteID = parts[0];// берем id голосования
        subject = parts[1]; // берем тему голосования 
        
        try
        {
            session = request.getSession();         
            
            // отображение таблицы
            out.println("<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <title>Изменение голосования - Choiser</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">\n");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/tablestyle.css\">\n");
            out.println("<script type=\"text/javascript\" src=\"js/script.js\"></script>");
            out.println("</head>");
            out.println("<div class=\"clock\"> \n" +
            "                <form name=\"form_clock\">\n" +
            "                    <p><input id=\"inputdate\" name=\"date\" type=\"text\" name=\"date\" value=\"\" size=\"12\" disabled=\"true\"></p>\n" +
            "                    <p><input id=\"inputtime\" name=\"time\" type=\"text\" name=\"time\" value=\"\" size=\"12\" disabled=\"true\"></p>\n" +
            "                </form> \n" +
            "           </div>");
            out.println("<body onload=\"datetime()\">");
            out.println("<div class=\"page-wrapper\">");
            out.println("<div align=\"right\"><form action=\"Back\" method=\"post\">"
                    + "<input type=\"submit\" class=\"btn\" name=\"back\" value=\"Выйти\"/>"
                    + "</form></div><center>");
            out.println("<br><br><h2>Администратор: Изменение голосования \"" + subject + "\"</h2><br>"); 
            out.println("<form name=\"AdminEditedVote\" action=\"AdminEditedVote\" method=\"POST\">\n" +
            "                    <input placeholder=\"Тема голосования\" type=\"text\" name=\"subject\" value=\"\" width=600px/>\n" +
            "                    <input type=\"hidden\" name=\"key\" value=\"" + voteID + "\" />\n" +
            "                    <br><br>\n" +
            "                    <input class=\"btn\" type=\"submit\" value=\"Изменить\" name=\"do\" />\n" +
            "                </form>"); 
            out.println("</div></body>");
            out.println("</html>"); 
        }
        catch(Exception e)
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Ошибка!</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1><br><br><center>Ошибка сервлета</center></h1>");
            System.out.println(e);
            out.println("</body>");
            out.println("</html>");
        }    
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    HttpSession session;
}
