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
    public String voteSubject;
    public String userID;
    public String userName;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");        
        PrintWriter out=response.getWriter();
        
        //userID = (String)request.getAttribute("userID");
        //userName = (String)request.getAttribute("userName");
        
        String[] parts = request.getParameter("key").split(";");
        voteID = parts[0];
        voteSubject = parts[1];
        userID = parts[2];
        userName = parts[3];
        
        try
        {
            session=request.getSession();         
            
            // отображение таблицы
            out.println("<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <title>Статистика - Choiser</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">\n");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/tablestyle.css\">\n");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/chartstyle.css\">\n");
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
            out.println("<br><span align=\"right\"><form action=\"Back\" method=\"post\"><b>" + userName + "  </b>"
                    + "<input type=\"submit\" class=\"btn\" name=\"back\" value=\"Выйти\"/>  "
                    + "</form></span>");                            
            out.println("<center><br><h2><br>Статистика по голосованию \"" + voteSubject + "\"</h2><br></center>");

            // получаем количество всех голосов за данное голосование
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select count(ChoiserDB.choice.choice_variant_id) from ChoiserDB.choice where ChoiserDB.choice.choice_vote_id = "+voteID+";");
            
            String allChoices = "";
            if(rs.next())
            {
                allChoices = rs.getString(1);
            }
            else { response.sendRedirect("UserMenu"); }
                        
            // ищем вариант с максимальным количеством голосов
            int max = 0;
            rs = st.executeQuery("select variant_id, variant_title from ChoiserDB.variant where ChoiserDB.variant.vote_id = " + voteID + ";");
            while(rs.next())
            {
                Class.forName("com.mysql.jdbc.Driver");
                java.sql.Connection c = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");
                Statement s = c.createStatement();
                ResultSet r = s.executeQuery("select count(ChoiserDB.choice.choice_variant_id) from ChoiserDB.choice where ChoiserDB.choice.choice_vote_id = "+voteID+" and ChoiserDB.choice.choice_variant_id = " + rs.getString(1) + ";");
                if(r.next())                
                {
                    int t = Integer.parseInt(r.getString(1));
                    if(t > max)
                    {
                        max = t;
                    }
                }   
            }
            
            // ищем информацию по каждому варианту голосования
            out.println("<div class=\"chart\">\n");
            
            rs = st.executeQuery("select variant_id, variant_title from ChoiserDB.variant where ChoiserDB.variant.vote_id = " + voteID + ";");
            while(rs.next())
            {
                Class.forName("com.mysql.jdbc.Driver");
                java.sql.Connection c = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");
                Statement s = c.createStatement();
                ResultSet r = s.executeQuery("select count(ChoiserDB.choice.choice_variant_id) from ChoiserDB.choice where ChoiserDB.choice.choice_vote_id = "+voteID+" and ChoiserDB.choice.choice_variant_id = " + rs.getString(1) + ";");
                float divWidth = 0;
                if(r.next())                
                {
                    if(Integer.parseInt(r.getString(1)) == 0)
                    {                     
                        out.println(rs.getString(2) + "<div style=\"width: 10px;\">" + r.getString(1) + "</div>");    
                    }
                    else
                    {
                        divWidth = (int)(( Float.parseFloat(r.getString(1)) / max) * 980);
                        out.println(rs.getString(2) + "<div style=\"width: " + divWidth + "px;\">" + r.getString(1) + "</div>"); 
                    }
                }   
            }
            
            out.println("</div>");
            
            /*
            // печать графика
            out.println("<table id=\"q-graph\">\n" +
                "<thead>\n" +
                "<tr>\n" +
                "<th></th>\n" +
                "</tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr class=\"qtr\" id=\"q1\">\n" +
                "<th scope=\"row\">Q1</th>\n" +
                "<td class=\"paid bar\" style=\"height: 99px;\"><p>16</p></td>\n" +
                "</tr>\n" +
                "<tr class=\"qtr\" id=\"q2\">\n" +
                "<th scope=\"row\">Q2</th>\n" +
                "<td class=\"paid bar\" style=\"height: 194px;\"><p>32</p></td>\n" +
                "</tr>\n" +
                "<tr class=\"qtr\" id=\"q3\">\n" +
                "<th scope=\"row\">Q3</th>\n" +
                "<td class=\"paid bar\" style=\"height: 193px;\"><p>33</p></td>\n" +
                "</tr>\n" +
                "<tr class=\"qtr\" id=\"q4\">\n" +
                "<th scope=\"row\">Q4</th>\n" +
                "<td class=\"paid bar\" style=\"height: 195px;\"><p>33</p></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n");    
            */            
            out.println("</div></body>");
            out.println("</html>"); 
            
            request.setAttribute("userID", userID);
            request.setAttribute("userName", userName);
            RequestDispatcher rd = request.getRequestDispatcher("UserMenu");
            //rd.forward(request,response);
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
