package core.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UserChoice", urlPatterns = {"/UserChoice"})
public class UserChoice extends HttpServlet 
{
    public String ID;
    public String voteID;
    public String userID;
    public String userName;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String[] parts = request.getParameter("key").split(";");
        ID = parts[0]; // берем ID
        voteID = parts[1]; // берем идентификатор голосования
        userID = parts[2]; // берем идентификатор пользователя
        userName = parts[3]; // берем имя пользователя
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");              
            Statement st = connection.createStatement();                 
            String sql = "insert into choiserdb.choice set choice_vote_id = " + voteID + ", choice_variant_id = " + ID + ", choice_user_id = " + userID + ";";
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
            request.setAttribute("userID", userID);
            request.setAttribute("userName", userName);
            RequestDispatcher rd = request.getRequestDispatcher("UserMenu");
            rd.forward(request,response);
            response.sendRedirect("UserMenu");           
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
