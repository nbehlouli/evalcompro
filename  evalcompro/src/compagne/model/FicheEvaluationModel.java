package compagne.model;

import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

public class FicheEvaluationModel {
	
	/**
	 * cett eméthode permet de récuperer l'id_employ associé à l'id compte
	 * @return
	 */
	
	public int getIdEmploye(int id_compte)
	{
		int id_employe=-1;
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT id_employe  FROM compte_backup where id_compte=#id_compte ";
			
			select_structure = select_structure.replaceAll("#id_compte", "'"+id_compte+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					id_employe=rs.getInt("id_employe");
	
				}
				else {
					return id_employe;
				}
				
			}
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return id_employe;
		
	}
	
	public boolean getValiditeFiche(int id_employe)
	{
		boolean ficheValide=false;
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT fiche_valide  FROM fiche_validation where id_employe=#id_employe ";
			
			select_structure = select_structure.replaceAll("#id_employe", "'"+id_employe+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					int validite=rs.getInt("fiche_valide");
					if(validite==1)
						ficheValide=true;
					else
						ficheValide=false;
				}
				else {
					return ficheValide;
				}
				
			}
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return ficheValide; 
	}

}
