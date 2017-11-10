package core;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "GetStat", urlPatterns = {"/GetStat"})
public class GetStat extends HttpServlet 
{
    public String voteID;
    public String subject;
    public String userID;
    public String username;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");        
        PrintWriter out=response.getWriter();
        ResultSet rs;
        
        java.sql.Connection connection;
        Statement st;        
        
        userID = (String)request.getAttribute("userID");
        username = (String)request.getAttribute("userName");
        
        String[] parts = request.getParameter("key").split(";");
        voteID = parts[0];
        subject = parts[1]; 
        
        try
        {
            session=request.getSession();         
            
            // отображение таблицы
            out.println("<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <title>Просмотр вариантов - Choiser</title>");
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
            out.println("<br><h2><br>Администратор: Варианты голосования \"" + subject + "\"</h2><br>");
            out.println("<form action=\"AdminAddVariant\" method=\"post\">\n" +
                    "                <input type=\"submit\" class=\"btn\" name=\"view\" value=\"Добавить новый вариант\" />\n" +
                    "                <input type=\"hidden\" name=\"vote\" value=\"" + voteID + "\" />\n" +
                    "            </form><br>");
            
            
            
            try
            {                
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");
                st = connection.createStatement();
                rs = st.executeQuery("select variant_id, variant_title from variant where vote_id = " + voteID + ";");
                
                // вывод вариантов голосования
                String id, variant;
                while(rs.next())
                {
                    id = rs.getString(1);
                    variant = rs.getString(2);
                                        
                }
                connection.close();
            }
            catch(Exception exception) 
            {
               System.err.println(exception.getMessage());
            }                        
            out.println("</div></body>");
            out.println("</html>"); 
            
            request.setAttribute("userID", userID);
            request.setAttribute("userName", username);
            RequestDispatcher rd = request.getRequestDispatcher("UserMenu");
            rd.forward(request,response);
            response.sendRedirect("UserMenu");
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
