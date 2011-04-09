package common;
import java.sql.*;

public class CreateDatabaseCon
{

    Connection connection;

    public CreateDatabaseCon()
    {
        connection = null;
    }

    public Connection connectToDB()
    {
        try
        {
            InitContext intctx = new InitContext();
            intctx.loadProperties();
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(intctx.getJdbcurl(), intctx.getJdbcusername(), intctx.getJdbcpassword());
            connection.setAutoCommit(true);
        }
        catch(Exception e)
        {
            if(connection != null)
            {
                try
                {
                    connection.close();
                }
                catch(Exception e1)
                {
                    throw new RuntimeException(e);
                }
            }
        }
        return connection;
    }

    public static void closeCon(Connection connection)
    {
        try
        {
            if(connection != null)
            {
                connection.close();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
