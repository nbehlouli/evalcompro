package common;
import java.sql.*;

import administration.bean.CompteEntrepriseDatabaseBean;

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

    public Connection connectToEntrepriseDB()
    {
        try
        {
            InitContext intctx = new InitContext();
            CompteEntrepriseDatabaseBean compteEntrepriseDataBaseBean=ApplicationFacade.getInstance().getCompteEntrepriseDatabasebean();
            intctx.setJdbcpassword(compteEntrepriseDataBaseBean.getJdbcpassword());
            intctx.setJdbcusername(compteEntrepriseDataBaseBean.getJdbcusername());
            intctx.setJdbcurl(compteEntrepriseDataBaseBean.getJdbcurl());
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(intctx.getJdbcurl(), intctx.getJdbcusername(), intctx.getJdbcpassword());
            connection.setAutoCommit(true);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
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
