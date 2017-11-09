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

@WebServlet(name = "AdminEditedVote", urlPatterns = {"/AdminEditedVote"})
public class AdminEditedVote extends HttpServlet 
{    
    public String ID;
    public String subject;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        ID = request.getParameter("key"); // берем ID
        subject = request.getParameter("subject"); // берем тему
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");              
            Statement st = connection.createStatement();                 
            String sql = "update choiserdb.vote set vote_subject = '" + subject + "' where vote_id = " + ID + ";";
            st.executeUpdate(sql);
            st.execute("commit");
            connection.close();
        }
        catch(Exception exception) 
        {
            System.err.println(exception.getMessage());
        }
        finally
        {
            response.sendRedirect("AdminMenu");            
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

}
