package administration.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import administration.bean.CompagneType;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

public class CompagneTypeModel
{
    public List<CompagneType> getAllCompagneTypes()
        throws SQLException
    {

        List<CompagneType> compagneTypes = new ArrayList<CompagneType>();
        CreateDatabaseCon dbcon = new CreateDatabaseCon();
        Connection conn = (Connection) dbcon.connectToEntrepriseDB();
        Statement stmt;

        try
        {
            stmt = (Statement) conn.createStatement();
            String sel = ConstantsModel.SEL_COMPAGNE_TYPE;

            ResultSet rs = (ResultSet) stmt.executeQuery( sel );

            while ( rs.next() )
            {
                if ( rs.getRow() >= 1 )
                {
                    CompagneType compagneType = new CompagneType();
                    compagneType.setId( Integer.valueOf( rs.getString( "id_compagne_type" ) ) );
                    compagneType.setLibelle( rs.getString( "compagne_type" ) );

                    compagneTypes.add( compagneType );

                }
                else
                {
                    return compagneTypes;
                }

            }
            conn.close();
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            //((java.sql.Connection) dbcon).close();
            e.printStackTrace();
            conn.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            conn.close();
        }

        return compagneTypes;

    }
}
