package core.admin;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AdminDeleteVote", urlPatterns = {"/AdminDeleteVote"})
public class AdminDeleteVote extends HttpServlet 
{    
    public String voteID;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");      
        
        String[] parts = request.getParameter("key").split(";");
        voteID = parts[0];
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");              
            Statement st = connection.createStatement();                 
            String sql = "delete from choiserdb.vote where vote_id = " + voteID + ";";
            st.executeUpdate(sql);
            st.execute("commit");
            sql = "delete from choiserdb.variant where vote_id = " + voteID + ";";
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

    HttpSession session;
}
