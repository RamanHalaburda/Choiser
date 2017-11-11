package core.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "UserMenu", urlPatterns = {"/UserMenu"})
public class UserMenu extends HttpServlet 
{
    public String userID;
    public String username;
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");        
        PrintWriter out=response.getWriter();
        
        userID = (String)request.getAttribute("userID");
        username = (String)request.getAttribute("userName");
        
        try
        {
            session=request.getSession();         
            
            // отображение таблицы
            out.println("<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <title>Голосования - Choiser</title>");
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
            out.println("<br><span align=\"right\"><form action=\"Back\" method=\"post\"><b>" + username + "  </b>"
                    + "<input type=\"submit\" class=\"btn\" name=\"back\" value=\"Выйти\"/>  "
                    + "</form></span><center>");
            out.println("<h2><br>Голосования</h2><br>"); 
            out.println("<table class=\"container\">");
            out.println("<thead>" +
            "                <td>Тема</td>" +
            "                <td>Варианты</td>" +
            "                <td>Статистика</td>" +                   
            "           </thead><tbody>");
            
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("select * from vote;");

                // вывод тем голосований
                String id, vote;
                while(rs.next())
                {
                    id = rs.getString(1);
                    vote = rs.getString(2);
                    out.println("<tr>");
                    out.println("<td>" + vote + "</td>");                    
                    out.println("<td>");
                    
                    Class.forName("com.mysql.jdbc.Driver");
                    java.sql.Connection c = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");
                    Statement s = c.createStatement();
                    ResultSet r = s.executeQuery("select * from choice where choice_vote_id = " + id + " and choice_user_id = " + userID + ";");

                    if(!r.last())
                    {
                        out.println("<form action=\"UserOpenVote\" method=\"post\">\n" +
                        "<input type=\"submit\" class=\"btn\" name=\"open\" value=\"Открыть\" />\n" +
                        "<input type=\"hidden\" name=\"key\" value=\"" + id + ";" + vote + ";" + userID + ";" + username + "\" />\n" +
                        "</form>");
                    }                     
                    
                    out.println("</td>");
                    out.println("<td><form action=\"GetStat\" method=\"post\">\n" +
                    "                <input type=\"submit\" class=\"btn\" name=\"stat\" value=\"Статистика\" />\n" +
                    "                <input type=\"hidden\" name=\"key\" value=\"" + id + ";" + vote + ";" + userID + ";" + username + "\" />\n" +
                    "            </form></td>");
                    out.println("</tr>");                    
                }
                connection.close();
            }
            catch(Exception exception) 
            {
               System.err.println(exception.getMessage());
            }
            
            out.println("<tbody></table");
            
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
