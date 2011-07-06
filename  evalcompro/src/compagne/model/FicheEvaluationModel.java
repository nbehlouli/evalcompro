package compagne.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;
import compagne.bean.EmployesAEvaluerBean;
import compagne.bean.MapEmployesAEvaluerBean;

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
	/**
	 * cette méthode permet de récuperer les informations associes aux employés à évaluer
	 */

	public MapEmployesAEvaluerBean getListEmployesAEvaluer(int id_evaluateur)
	{
		MapEmployesAEvaluerBean listEmployesAEvaluerBean=new MapEmployesAEvaluerBean();
		HashMap<String, EmployesAEvaluerBean> MapclesnomEmploye=listEmployesAEvaluerBean.getMapclesnomEmploye();
		HashMap<String, EmployesAEvaluerBean> Mapclesposte=listEmployesAEvaluerBean.getMapclesposte();
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			//String select_structure="select e.nom, e.prenom , p.intitule_poste from employe e , poste_travail_description p where id_employe in (select id_employe  from planning_evaluation where id_evaluateur='2' ) and e.code_poste=p.code_poste";
//			String select_structure="select distinct r.famille, r.code_famille, e.nom, e.prenom , p.intitule_poste from repertoire_competence r, employe e , poste_travail_description p where id_employe in (select id_employe  from planning_evaluation where id_evaluateur='2' ) and e.code_poste=p.code_poste and code_competence in (select code_competence from poste_travail_competence where code_poste='P002')";
//			select distinct r.famille, r.code_famille, e.nom, e.prenom , t.intitule_poste 
//			from repertoire_competence r, employe e , poste_travail_description t 
//			where id_employe in 
//			    (select distinct v.id_employe  
//			        from planning_evaluation v , compagne_evaluation n 
//			        where v.id_evaluateur='2'  and v.id_compagne in
//			                        ( select distinct id_compagne 
//			                            from compagne_evaluation  
//			                            where  date_fin>=now() and date_debut<=now()))
//			         and e.code_poste=t.code_poste and code_competence in 
//			                        (select distinct code_competence from poste_travail_competence where code_poste='P002')

			String select_structure="select distinct r.famille, r.code_famille, e.nom, e.prenom , e.id_employe, t.intitule_poste , t.code_poste from repertoire_competence r, employe e , poste_travail_description t where id_employe in  (select distinct v.id_employe from planning_evaluation v , compagne_evaluation n where v.id_evaluateur='2'  and v.id_compagne in ( select distinct id_compagne  from compagne_evaluation  where  date_fin>=now() and date_debut<=now()))  and e.code_poste=t.code_poste and code_competence in (select distinct code_competence from poste_travail_competence where code_poste='P002')";
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					
					int id_employe =rs.getInt("id_employe") ;
					String poste_travail=rs.getString("intitule_poste");
					String code_poste=rs.getString("code_poste") ; ;
					String nom_employe=rs.getString("nom")+" "+ rs.getString("prenom");
					String famille=rs.getString("famille");
					String code_famille=rs.getString("code_famille");
					
					if(MapclesnomEmploye.containsKey(nom_employe))
					{
						ArrayList<String> listFamille=MapclesnomEmploye.get(nom_employe).getCode_famille();
						listFamille.add(code_famille);
						MapclesnomEmploye.get(nom_employe).setCode_famille(listFamille);
						
						ArrayList<String> listLibelleFamille=MapclesnomEmploye.get(nom_employe).getFamille();
						listLibelleFamille.add(famille);
						MapclesnomEmploye.get(nom_employe).setFamille(listLibelleFamille);
					}
					else
					{
						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						MapclesnomEmploye.put(nom_employe,employesAEvaluerBean);
					}
					if(Mapclesposte.containsKey(poste_travail))
					{
						ArrayList<String> listFamille=Mapclesposte.get(poste_travail).getCode_famille();
						listFamille.add(code_famille);
						Mapclesposte.get(poste_travail).setCode_famille(listFamille);
						
						ArrayList<String> listLibelleFamille=Mapclesposte.get(poste_travail).getFamille();
						listLibelleFamille.add(famille);
						Mapclesposte.get(poste_travail).setFamille(listLibelleFamille);
					}
					else
					{
						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						Mapclesposte.put(poste_travail,employesAEvaluerBean);
					}			
					
					
				}
				else {
					return listEmployesAEvaluerBean;
				}
				listEmployesAEvaluerBean.setMapclesnomEmploye(MapclesnomEmploye);
				listEmployesAEvaluerBean.setMapclesposte(Mapclesposte);
			}
			stmt.close();
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
		
		return listEmployesAEvaluerBean;
	}
	
}
