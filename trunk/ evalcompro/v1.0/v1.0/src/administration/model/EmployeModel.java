/**
 * 
 */
package administration.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import administration.bean.Employe;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

/**
 * @author FTERZI
 *
 */
public class EmployeModel
{
    public List<Employe> getAllEmployes()
        throws SQLException
    {

        List<Employe> employes = new ArrayList<Employe>();
        CreateDatabaseCon dbcon = new CreateDatabaseCon();
        Connection conn = (Connection) dbcon.connectToEntrepriseDB();
        Statement stmt;

        try
        {
            stmt = (Statement) conn.createStatement();
            String sel = ConstantsModel.SEL_EMPLOYE;

            ResultSet rs = (ResultSet) stmt.executeQuery( sel );

            while ( rs.next() )
            {
                if ( rs.getRow() >= 1 )
                {
                    Employe employe = new Employe();
                    employe.setId( Integer.valueOf( rs.getString( "id_employe" ) ) );
                    employe.setNom( rs.getString( "nom" ) );
                    employe.setPrenom( rs.getString( "prenom" ) );
                    employe.setDateNaissance( Date.valueOf( rs.getString( "date_naissance" ) ) );

                    employes.add( employe );

                }
                else
                {
                    return employes;
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

        return employes;

    }
}
