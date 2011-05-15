package Statistique.model;

import java.sql.SQLException;
import java.util.Calendar;



import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

public class EmployeModel {
	
	/**
	 * cette méthode retrourne le nombre d'employes se trouvant dans la BDD
	 * @return
	 */
	public int getNombreEmployes()
	{
		int nbEmploye=0;
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT COUNT(*) AS rowcount FROM employe";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			rs.next();
			nbEmploye = rs.getInt("rowcount") ;
			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				((java.sql.Connection) dbcon).close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return nbEmploye;
		
		
	}
	/**
	 * retourne le nombre d'employes ayants entre 18 et 30 ans
	 * @return
	 */
	public int getNombreEmployes18_30ans()
	{
		int nbEmploye=0;
		int annee18=getAnneeAge(18);
		int annee30=getAnneeAge(30);
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT COUNT(*) AS rowcount FROM employe WHERE ";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			rs.next();
			nbEmploye = rs.getInt("rowcount") ;


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				((java.sql.Connection) dbcon).close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return nbEmploye;
		
		
	}
	/**
	 * retourne l'année de naissance correspondant à l'age Age
	 * @return
	 */
	public int getAnneeAge(int age )
	{
		int annee=0;
		Calendar date = Calendar.getInstance();
		int anneeActuel= date.get(Calendar.YEAR);
		annee=anneeActuel-age;
		return annee;
	}

}
