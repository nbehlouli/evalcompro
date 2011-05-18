package administration.model;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Messagebox;

import administration.bean.AdministrationLoginBean;
import administration.bean.RepCompetenceBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;
import common.PwdCrypt;

public class RepCompetenceModel {
	
private ArrayList<RepCompetenceBean>  listcomp =null; 
	
	
	
public List loadRepCompetence() throws SQLException{
		
		listcomp = new ArrayList<RepCompetenceBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_compte="select  id_repertoire_competence,code_famille,famille,code_groupe,groupe,code_competence,libelle_competence,definition_competence,"+
			                 "aptitude_observable,affichable  from repertoire_competence";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_compte);
								 
			
			while(rs.next()){
				
				RepCompetenceBean rep_comp=new RepCompetenceBean();
					
				    rep_comp.setId_repertoire_competence(rs.getInt("id_repertoire_competence"));
				    rep_comp.setCode_famille(rs.getString("code_famille"));
				    rep_comp.setFamille (rs.getString("famille"));
				    rep_comp.setCode_groupe (rs.getString("code_groupe"));
				    rep_comp.setGroupe(rs.getString("groupe"));
				    rep_comp.setCode_competence(rs.getString("code_competence"));
				    rep_comp.setLibelle_competence(rs.getString("libelle_competence"));
				    rep_comp.setDefinition_competence(rs.getString("definition_competence"));
				    rep_comp.setAptitude_observable(rs.getString("aptitude_observable"));
				    rep_comp.setAffichable (rs.getString("affichable"));
				    			    		
					  
				    listcomp.add(rep_comp);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listcomp;
	
		
		
	}


/**
 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
 * @param addedData
 * @return
 * @throws ParseException 
 */
public boolean addRepCompBean(RepCompetenceBean addedData) throws ParseException
{
	

	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt;
		
	
	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		String select_structure="INSERT INTO repertoire_competence(code_famille, famille, code_groupe, groupe, code_competence, libelle_competence, definition_competence, aptitude_observable, affichable)"+
		             		  "VALUES(#code_famille,#famille,#code_groupe,#groupe,#code_competence,#libelle_competence,#definition_competence,#aptitude_observable,#affichable)";
		
		select_structure = select_structure.replaceAll("#code_famille", "'"+addedData.getCode_famille()+"'");
		select_structure = select_structure.replaceAll("#famille", "'"+addedData.getFamille()+"'");
		select_structure = select_structure.replaceAll("#code_groupe", "'"+addedData.getCode_groupe()+"'");
		select_structure = select_structure.replaceAll("#groupe", "'"+addedData.getGroupe()+"'");
		select_structure = select_structure.replaceAll("#code_competence", "'"+addedData.getCode_competence()+"'");
		select_structure = select_structure.replaceAll("#libelle_competence", "'"+addedData.getLibelle_competence()+"'");
		select_structure = select_structure.replaceAll("#definition_competence", "'"+addedData.getDefinition_competence()+"'");
		select_structure = select_structure.replaceAll("#aptitude_observable", "'"+addedData.getAptitude_observable()+"'");
		select_structure = select_structure.replaceAll("#affichable", "'"+addedData.getAffichable()+"'");
						
	//System.out.println(select_structure);
		
		 stmt.execute(select_structure);
	} 
	catch (SQLException e) 
	{
		try 
		{
			Messagebox.show("La donnée n'a pas été insérée dans la base car il existe une donnée ayant le même code établissement", "Erreur",Messagebox.OK, Messagebox.ERROR);
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO Auto-generated catch block
		try {
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			
			e1.printStackTrace();
			//return false;
		}
		
		
		return false;
	}
	try {
		conn.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return true;
}

public Boolean majRepCompBean(RepCompetenceBean addedData)
{
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt;
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String select_structure="UPDATE  repertoire_competence SET code_famille=#code_famille ,famille=#famille,code_groupe=#code_groupe,groupe=#groupe, code_competence=#code_competence" +
				",libelle_competence=#libelle_competence,definition_competence=#definition_competence,aptitude_observable=#aptitude_observable,affichable=#affichable WHERE  id_repertoire_competence=# id_repertoire_competence";
		
		select_structure = select_structure.replaceAll("# id_repertoire_competence", Integer.toString(addedData.getId_repertoire_competence()));
		select_structure = select_structure.replaceAll("#code_famille", "'"+addedData.getCode_famille()+"'");
		select_structure = select_structure.replaceAll("#famille", "'"+addedData.getFamille()+"'");
		select_structure = select_structure.replaceAll("#code_groupe", "'"+addedData.getCode_groupe()+"'");
		select_structure = select_structure.replaceAll("#groupe", "'"+addedData.getGroupe()+"'");
		select_structure = select_structure.replaceAll("#code_competence", "'"+addedData.getCode_competence()+"'");
		select_structure = select_structure.replaceAll("#libelle_competence", "'"+addedData.getLibelle_competence()+"'");
		select_structure = select_structure.replaceAll("#definition_competence", "'"+addedData.getDefinition_competence()+"'");
		select_structure = select_structure.replaceAll("#aptitude_observable", "'"+addedData.getAptitude_observable()+"'");
		select_structure = select_structure.replaceAll("#affichable", "'"+addedData.getAffichable()+"'");
		
	   //System.out.println(update_structure);
		
		 stmt.executeUpdate(select_structure);
	} 
	catch (SQLException e) 
	{
		try 
		{
			Messagebox.show("La modification n'a pas été prise en compte car il existe une donnée ayant le même code établissement", "Erreur",Messagebox.OK, Messagebox.ERROR);
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO Auto-generated catch block
		try {
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			
			e1.printStackTrace();
			//return false;
		}
		
		
		return false;
	}
	try {
		conn.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return true;
}

/**
 * cette classe permet de supprimer une donnée de la table structure_entreprise
 * @param codeStructure
 */
public void supprimerComp(RepCompetenceBean addedData)
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt;
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sup_login="DELETE FROM  repertoire_competence  WHERE id_repertoire_competence=#id_repertoire_competence"; 
		sup_login = sup_login.replaceAll("#id_repertoire_competence", Integer.toString(addedData.getId_repertoire_competence()));
		
	
		
		 stmt.executeUpdate(sup_login);
	} 
	catch (SQLException e) 
	{
		
			e.printStackTrace();
			//return false;


	}
	try {
		conn.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
