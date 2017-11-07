/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

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

/**
 *
 * @author Roman
 */
@WebServlet(name = "RegistrationResult", urlPatterns = {"/RegistrationResult"})
public class RegistrationResult extends HttpServlet 
{
    public String email;
    public String login;
    public String password;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");        
        PrintWriter out=response.getWriter();        
        
        email = request.getParameter("email"); // берем
        login = request.getParameter("login"); // берем
        password = request.getParameter("password"); // берем
        
        try
        {
            session=request.getSession();
            
            // отображение результата
            out.println("<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <title>Продолжение - Choiser</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">\n");
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
            
            try
            {
                if(email.equals("") || login.equals("") || password.equals(""))
                {
                    out.println("<br><h2><br>Ошибка регистрации!</h2><br>"); 
                    out.println(
                    "<form name=\"Registration\" action=\"Registration\" method=\"POST\">" +
                        "<input type=\"submit\" value=\"Попробовать снова\" name=\"name\" />" +
                        "<br><br>" +
                    "</form>");
                }
                else
                {
                    Class.forName("com.mysql.jdbc.Driver");
                    java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");              
                    Statement st = connection.createStatement();                 
                    String sql = "insert into choiserdb.user set user_email = '" + email + "', user_login = '" + login + "', user_password = '" + password + "';";
                    int flag = st.executeUpdate(sql);
                    st.execute("commit");

                    if(flag != 0)
                    {
                        out.println("<br><h2><br>Вы зерегестрированы!</h2><br>");
                        out.println("Ваш Email: " + email + "<br>Ваш логин: " + login + "<br><br>");
                        out.println("<span><a href=\"Welcome.jsp\">Авторизоваться</a></span>");
                    }
                    else
                    {                    
                        out.println("<br><h2><br>Ошибка регистрации!</h2><br>"); 
                        out.println(
                        "<form name=\"Registration\" action=\"Registration\" method=\"POST\">" +
                            "<input type=\"submit\" value=\"Попробовать снова\" name=\"name\" />" +
                            "<br><br>" +
                        "</form>");
                    }

                    connection.close();
                }
            }
            catch(Exception exception) 
            {
                System.err.println(exception.getMessage());
            }
            
            
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
