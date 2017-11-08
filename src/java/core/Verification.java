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

@WebServlet(name = "Verification", urlPatterns = {"/Verification"})
public class Verification extends HttpServlet 
{
    public String login;
    public String password; 
    public Boolean isadmin; 
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");        
        
        login = request.getParameter("login");
        password = request.getParameter("password");
        isadmin = request.getParameter("isadmin") != null;
        
        try (PrintWriter out = response.getWriter()) 
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");              
                Statement st = connection.createStatement(); 

                if(isadmin)
                {
                    ResultSet rs = st.executeQuery("select admin_login, admin_password from admin;");
            
                    // поиск администратора
                    while(rs.next())
                    {
                        if(login.equals(rs.getString(1)) && password.equals(rs.getString(2)))
                        {
                            response.sendRedirect("AdminMenu");
                        }
                    }
                }
                else
                {
                    ResultSet rs = st.executeQuery("select user_login, user_password from user;");
            
                    // поиск пользователя
                    while(rs.next())
                    {
                        if(login.equals(rs.getString(1)) && password.equals(rs.getString(2)))
                        {
                            response.sendRedirect("UserMenu");
                        }
                    }
                }
                connection.close();
                response.sendRedirect("Welcome.jsp");
                               
                
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
