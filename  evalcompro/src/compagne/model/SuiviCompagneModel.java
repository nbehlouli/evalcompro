package compagne.model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zul.ListModel;

import administration.bean.AdministrationLoginBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;
import common.PwdCrypt;
import compagne.bean.SuiviCompagneBean;

public class SuiviCompagneModel {
	
	private ArrayList<SuiviCompagneBean>  listevaluateur =null; 
	
public List uploadListEvaluateur() throws SQLException{
		
		
		listevaluateur = new ArrayList<SuiviCompagneBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		String sqlquery=	"select concat (nom ,' ',prenom) as nom_evaluateur , round(sum(nbfichevalide)*100/ sum(totalemploye)) as progress " +
				"from (	select id_evaluateur as evaluateur ,count(r.id_employe) as nbfichevalide,0 as totalemploye from planning_evaluation t ,fiche_validation r" +
				" where t.id_planning_evaluation=r.id_planning_evaluation and t.id_employe=r.id_employe" +
				" and fiche_valide=1 group by id_evaluateur	union select id_evaluateur as evaluateur ,0 as nbfichevalide ,count(t.id_employe)as totalemploye from planning_evaluation t" +
				" group by id_evaluateur) as t2,employe  where id_employe=evaluateur group by 1";
		
		try {
			stmt = (Statement) conn.createStatement();
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sqlquery);
		
			while(rs.next()){
				
				SuiviCompagneBean evalbean=new SuiviCompagneBean();
				evalbean.setEvaluateur(rs.getString("nom_evaluateur"));
				evalbean.setPourcentage(rs.getInt("progress"));
				System.out.println("evaluateur "+rs.getString("nom_evaluateur")+
						"progress "+rs.getInt("progress")+'\n');
				listevaluateur.add(evalbean);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listevaluateur;
	
		
		
	}
	public HashMap getCompagneList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String db_list="select id_compagne,libelle_compagne from compagne_evaluation"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(db_list);
			
			
			while(rs.next()){
				map.put( rs.getString("libelle_compagne"),rs.getInt("id_compagne"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}
	
	public HashMap getStructEntrepriseList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String db_list="select distinct libelle_division  from structure_entreprise where libelle_division !=''" +
					"union select distinct libelle_direction  from structure_entreprise where libelle_direction !=''" +
				    " union select distinct libelle_unite  from structure_entreprise where libelle_unite !=''"+
				    "union select distinct libelle_departement  from structure_entreprise where libelle_departement !=''" +
				    " union select distinct libelle_service  from structure_entreprise where libelle_service !=''" +
				    "union select distinct libelle_section  from structure_entreprise where libelle_section !=''"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(db_list);
			
			
			while(rs.next()){
				map.put( rs.getString("libelle_division"),rs.getString("libelle_division"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
		

	}

}
