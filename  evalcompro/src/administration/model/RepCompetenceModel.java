package administration.model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

}
