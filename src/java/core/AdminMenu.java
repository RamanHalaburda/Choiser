package core;

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

@WebServlet(name = "AdminMenu", urlPatterns = {"/AdminMenu"})
public class AdminMenu extends HttpServlet 
{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");        
        PrintWriter out=response.getWriter();
        
        try
        {
            session=request.getSession();         
            
            // отображение формы
            out.println("<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <title>Продолжение регистрации - Choiser</title>");
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
            out.println("<div class=\"page-wrapper\"><center>");
            out.println("<br><h2><br>Администратор: Голосования</h2><br>");            
            
            out.println("<table class=\"container\">");
            out.println("<thead>" +
            "                <td>Тема</td>" +
            "                <td>Просмотр</td>" +
            "                <td>Изменение</td>" +
            "                <td>Удаление</td>" +                    
            "           </thead><tbody>");
            
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("select * from vote;");

                // вывод истории сообщений
                String id, vote;
                while(rs.next())
                {
                    id = rs.getString(1);
                    vote = rs.getString(2);
                    out.println("<tr>");
                    out.println("<td>" + vote + "</td>");                    
                    out.println("<td><form action=\"AdminViewVote\" method=\"post\">\n" +
                    "                <input type=\"submit\" class=\"btn\" name=\"view\" value=\"Просмотреть\" />\n" +
                    "                <input type=\"hidden\" name=\"key\" value=\"" + id + "\" />\n" +
                    "            </form></td>");
                    out.println("<td><form action=\"AdminEditVote\" method=\"post\">\n" +
                    "                <input type=\"submit\" class=\"btn\" name=\"edit\" value=\"Релактировать\" />\n" +
                    "                <input type=\"hidden\" name=\"key\" value=\"" + id + "\" />\n" +
                    "            </form></td>");
                    out.println("<td><form action=\"AdminDeleteVote\" method=\"post\">\n" +
                    "                <input type=\"submit\" class=\"btn\" name=\"delete\" value=\"Удалить\" />\n" +
                    "                <input type=\"hidden\" name=\"key\" value=\"" + id + "\" />\n" +
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
            out.println("</body>");
            out.println("</html>");
            System.out.println(e);
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
