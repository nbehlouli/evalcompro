package compagne.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;
import compagne.bean.EmployesAEvaluerBean;
import compagne.bean.FicheEvaluationBean;
import compagne.bean.MapEmployesAEvaluerBean;

public class FicheEvaluationModel {
	
	/**
	 * cett em�thode permet de r�cuperer l'id_employ associ� � l'id compte
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
	 * cette m�thode permet de r�cuperer les informations associes aux employ�s � �valuer
	 */

	public MapEmployesAEvaluerBean getListEmployesAEvaluer(int id_evaluateur)
	{
		MapEmployesAEvaluerBean listEmployesAEvaluerBean=new MapEmployesAEvaluerBean();
		HashMap<String, EmployesAEvaluerBean> MapclesnomEmploye=listEmployesAEvaluerBean.getMapclesnomEmploye();
		HashMap<String, HashMap<String,EmployesAEvaluerBean>> Mapclesposte=listEmployesAEvaluerBean.getMapclesposte();
		
		
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

			//String select_structure="select distinct r.famille, r.code_famille, e.nom, e.prenom , e.id_employe, t.intitule_poste , t.code_poste from repertoire_competence r, employe e , poste_travail_description t where id_employe in  (select distinct v.id_employe from planning_evaluation v , compagne_evaluation n where v.id_evaluateur=#id_evaluateur  and v.id_compagne in ( select distinct id_compagne  from compagne_evaluation  where  date_fin>=now() and date_debut<=now()))  and e.code_poste=t.code_poste ";
			
			String select_structure="select distinct  k.id_planning_evaluation,r.famille, r.code_famille, e.nom, e.prenom , e.id_employe, t.intitule_poste , t.code_poste "+ 
			"from repertoire_competence r, employe e , poste_travail_description t, planning_evaluation k "+ 
			"where e.id_employe in "+   
			   " (select distinct v.id_employe "+  
			        "from planning_evaluation v , compagne_evaluation n "+  
			           " where v.id_evaluateur=#id_evaluateur  and v.id_compagne "+ 
			            "in ( select distinct id_compagne "+   
			                    "from compagne_evaluation "+   
			                    "where  date_fin>=now() and date_debut<=now()))  "+  
			"and e.code_poste=t.code_poste  and e.code_poste =k.code_poste and e.id_employe=k.id_employe ";
			
			
			select_structure = select_structure.replaceAll("#id_evaluateur", "'"+id_evaluateur+"'");
			
			
			//System.out.println(select_structure);
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
					int id_planning_evaluation=rs.getInt("id_planning_evaluation");
					
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
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						MapclesnomEmploye.put(nom_employe,employesAEvaluerBean);
					}
					if(Mapclesposte.containsKey(poste_travail))
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(poste_travail);
						if(mapEmploye.containsKey(nom_employe))
						{
							ArrayList<String> listFamille=mapEmploye.get(nom_employe).getCode_famille();
							listFamille.add(code_famille);
							mapEmploye.get(nom_employe).setCode_famille(listFamille);
							
							ArrayList<String> listLibelleFamille=mapEmploye.get(nom_employe).getFamille();
							listLibelleFamille.add(famille);
							mapEmploye.get(nom_employe).setFamille(listLibelleFamille);
							Mapclesposte.put(poste_travail, mapEmploye);
							
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
							employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
							mapEmploye.put(nom_employe,employesAEvaluerBean);
							Mapclesposte.put(poste_travail, mapEmploye);
						}

					}
					else
					{
						HashMap<String, EmployesAEvaluerBean> mapEmploye=new HashMap<String, EmployesAEvaluerBean>();
						EmployesAEvaluerBean employesAEvaluerBean=new EmployesAEvaluerBean();
						employesAEvaluerBean.setCode_poste(code_poste);
						employesAEvaluerBean.setId_employe(id_employe);
						employesAEvaluerBean.setNom_employe(nom_employe);
						employesAEvaluerBean.setPoste_travail(poste_travail);
						employesAEvaluerBean.getCode_famille().add(code_famille);
						employesAEvaluerBean.getFamille().add(famille);
						employesAEvaluerBean.setId_planning_evaluation(id_planning_evaluation);
						mapEmploye.put(nom_employe,employesAEvaluerBean);
						Mapclesposte.put(poste_travail, mapEmploye);
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
	/**
	 * Cette m�thode r�cup�re les informations relatifs � une fiche d'evaluation qu'on doit afficher avant l'evaluation d'un employ�
	 */
	
	public HashMap<String, ArrayList<FicheEvaluationBean>> getInfosFicheEvaluationparPoste()
	{
		HashMap<String, ArrayList<FicheEvaluationBean>> mapPosteTravailFiche=new HashMap<String, ArrayList<FicheEvaluationBean>>();
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="select distinct r.famille,  p.code_poste, r.id_repertoire_competence, r.code_competence, r.libelle_competence , r.definition_competence, r.aptitude_observable from repertoire_competence r , poste_travail_competence p where r.code_competence=p.code_competence and p.code_poste in(select distinct code_poste from planning_evaluation) ";
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					//listposteTravail.add(rs.getString("intitule_poste"));
					
					int id_repertoire_competence=rs.getInt("id_repertoire_competence") ;
					String code_competence=rs.getString("code_competence");
					String libelle_competence=rs.getString("libelle_competence");
					String definition_competence=rs.getString("definition_competence");
					String aptitude_observable=rs.getString("aptitude_observable");
					String code_poste=rs.getString("code_poste");
					String famille=rs.getString("famille");
					
					FicheEvaluationBean ficheEvaluation=new FicheEvaluationBean();
					ficheEvaluation.setCode_competence(code_competence);
					ficheEvaluation.setId_repertoire_competence(id_repertoire_competence);
					ficheEvaluation.setLibelle_competence(libelle_competence);
					ficheEvaluation.setDefinition_competence(definition_competence);
					ficheEvaluation.setAptitude_observable(aptitude_observable);
					
					
					if(mapPosteTravailFiche.containsKey(code_poste+"#"+famille))
					{
						ArrayList<FicheEvaluationBean> listFiches=mapPosteTravailFiche.get(code_poste+"#"+famille);
						listFiches.add(ficheEvaluation);
						mapPosteTravailFiche.put(code_poste+"#"+famille, listFiches);
					}
					else
					{
						ArrayList<FicheEvaluationBean> listFiches=new ArrayList<FicheEvaluationBean>();
						listFiches.add(ficheEvaluation);
						mapPosteTravailFiche.put(code_poste+"#"+famille, listFiches);
					}
				}
				else {
					return mapPosteTravailFiche;
				}
				
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
		
		return mapPosteTravailFiche;
	}
}