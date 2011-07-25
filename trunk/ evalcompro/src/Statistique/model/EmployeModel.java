package Statistique.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



import Statistique.bean.StatTrancheAgePosteBean;
import administration.bean.AdministrationLoginBean;
import administration.bean.FichePosteBean;

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
	 * @throws SQLException 
	 */
	public int getNombreEmployes18_30ans() throws SQLException
	{
		int nbEmploye=0;
		int annee18=getAnneeAge(18);
		int annee30=getAnneeAge(30);
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT COUNT(*) AS rowcount FROM employe WHERE ";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			rs.next();
			nbEmploye = rs.getInt("rowcount") ;
			
			stmt.close(); ((java.sql.Connection) dbcon).close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				stmt.close(); ((java.sql.Connection) dbcon).close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				stmt.close(); ((java.sql.Connection) dbcon).close();
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
	
	
	public List getNombreEmployesParPoste() throws SQLException
	{
			
		ArrayList<StatTrancheAgePosteBean>   liststatbean = new ArrayList<StatTrancheAgePosteBean>();
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select p.intitule_poste, if ((round( DATEDIFF(curdate(),e.date_naissance)/365))>18" +
					                " and (round( DATEDIFF(curdate(),e.date_naissance)/365))<30 ,'1', if ((round( DATEDIFF(curdate(),e.date_naissance)/365))>30" +
					                " and (round( DATEDIFF(curdate(),e.date_naissance)/365))<46 ,'2','3')) as tranche," +
					                " round(count(e.code_poste)*100/(select count(*) from employe)) as pourcentage from employe e ,poste_travail_description p" +
					                " where p.code_poste=e.code_poste group by intitule_poste,tranche order by tranche ";
			
			//System.out.println(select_structure);
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			while(rs.next()){
				
				StatTrancheAgePosteBean stat_bean=new StatTrancheAgePosteBean();
				stat_bean.setIntitule_poste(rs.getString("intitule_poste"));
				stat_bean.setTranche(rs.getString("tranche"));
				stat_bean.setPourcentage(rs.getInt("pourcentage"));
						
				liststatbean.add(stat_bean);
				   
					
				}
			stmt.close();
			conn.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				stmt.close(); ((java.sql.Connection) dbcon).close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				stmt.close(); ((java.sql.Connection) dbcon).close();
			}
		}
		return liststatbean;
		
		
	}

}
