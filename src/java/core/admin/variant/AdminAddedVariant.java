package core.admin.variant;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AdminAddedVariant", urlPatterns = {"/AdminAddedVariant"})
public class AdminAddedVariant extends HttpServlet 
{
    public String variant;
    public String voteID;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        variant = request.getParameter("variant"); // берем текст варианта        
        voteID = request.getParameter("vote"); // берем идентификатор голосования
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/choiserdb","root","root");              
            Statement st = connection.createStatement();                 
            String sql = "insert into choiserdb.variant set variant_title = '" + variant + "', vote_id = " + voteID + ";";
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
