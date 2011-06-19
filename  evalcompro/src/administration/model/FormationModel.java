/**
 * 
 */
package administration.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import administration.bean.Formation;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

/**
 * @author FTERZI
 *
 */
public class FormationModel
{

    private ArrayList<Formation> formations = null;

    public ArrayList<Formation> getAllFormations()
        throws SQLException
    {

        formations = new ArrayList<Formation>();

        CreateDatabaseCon dbcon = new CreateDatabaseCon();
        Connection conn = (Connection) dbcon.connectToEntrepriseDB();
        Statement stmt;

        try
        {
            stmt = (Statement) conn.createStatement();
            String select_all_formation = "SELECT code_formation,libelle_formation,libelle_diplome FROM formation";

            ResultSet rs = (ResultSet) stmt.executeQuery( select_all_formation );

            while ( rs.next() )
            {
                if ( rs.getRow() >= 1 )
                {
                    Formation formation = new Formation();
                    formation.setCodeFormation( rs.getString( "code_formation" ) );
                    formation.setLibelleFormation( rs.getString( "libelle_formation" ) );
                    formation.setLibelleDiplome( rs.getString( "libelle_diplome" ) );

                    formations.add( formation );

                }
                else
                {
                    return formations;
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

        return formations;

    }

    public void setFormations( ArrayList<Formation> formations )
    {
        this.formations = formations;
    }

    public ArrayList<Formation> getFormations()
    {
        return formations;
    }
}
