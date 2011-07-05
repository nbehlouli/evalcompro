package administration.model;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;




import administration.bean.CompetencePosteTravailBean;

import administration.model.CompetencePosteTravailModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;


public class CompetencePosteTravailModel {

	
	/**
	 * mise � jour de la table postedetravail_competence en supprimant des associations existantes
	 * 
	 */
	
	public void updateUnCheckedPoteTravailCompetence(ArrayList <String>listunselected, HashMap <String, String>mapCodeCompetence, HashMap <String, String>mapCodePoste)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		Iterator<String> iterator =listunselected.iterator();
		while(iterator.hasNext())
		{
			String cles=iterator.next();
			String[] liste=cles.split("#");
//			String famille=liste[0];
//			String groupe=liste[1];
			String competence=liste[2];
			String posteTravail=liste[3];
			
			//r�cuperation du code_competence et du code_posteTravail
			
			String code_competence=mapCodeCompetence.get(competence);
			String code_poste=mapCodePoste.get(posteTravail);
			
			

				
				//mise � jour de la table poste_travail_competence
				/*****************************************/
				try 
				{
					stmt = (Statement) conn.createStatement();
					String update_structure="DELETE FROM  poste_travail_competence  WHERE code_poste=#code_poste and code_competence=#code_competence"; 
					update_structure = update_structure.replaceAll("#code_poste", "'"+code_poste+"'");
					update_structure = update_structure.replaceAll("#code_competence", "'"+code_competence+"'");
				
					System.out.println(update_structure);
					 stmt.executeUpdate(update_structure);
				} 
				catch (SQLException e) 
				{
					
						e.printStackTrace();
						//return false;


				}

				/**********************************************/
				
				
			
			
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * mise � jour de la table postedetravail_competence en ajoutant des associations existantes
	 * 
	 */
	
	public void updateCheckedPoteTravailCompetence(ArrayList <String>listselected, HashMap <String, String>mapCodeCompetence, HashMap <String, String>mapCodePoste)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		Iterator<String> iterator =listselected.iterator();
		while(iterator.hasNext())
		{
			String cles=iterator.next();
			String[] liste=cles.split("#");
			//String famille=liste[0];
			//String groupe=liste[1];
			String competence=liste[2];
			String posteTravail=liste[3];
			
			//r�cuperation du code_competence et du code_posteTravail
			
			
			String code_competence=mapCodeCompetence.get(competence);
			String code_poste=mapCodePoste.get(posteTravail);
			
			//mise � jour de la table poste_travail_competence
				/*****************************************/
			try 
			{
				stmt = (Statement) conn.createStatement();
				String insert_structure="INSERT INTO  poste_travail_competence  (code_poste,code_competence) VALUES (#code_poste, #code_competence)"; 
				insert_structure = insert_structure.replaceAll("#code_poste", "'"+code_poste+"'");
				insert_structure = insert_structure.replaceAll("#code_competence", "'"+code_competence+"'");
			
				System.out.println(insert_structure);	
				stmt.execute(insert_structure);
			} 
			catch (SQLException e) 
			{
					
					e.printStackTrace();
					//return false;


			}

			/**********************************************/
				
				
		} 
			
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
/**
 * r�cup�ration de la liste des poste de travail
 * @return
 */
	public ArrayList<String> getlistepostes()
	{
		ArrayList<String> listposteTravail = new ArrayList<String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT intitule_poste  FROM poste_travail_description where code_poste in(select distinct code_poste from planning_evaluation) ";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					listposteTravail.add(rs.getString("intitule_poste"));
	
				}
				else {
					return listposteTravail;
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
		
			
		return listposteTravail;	
	
	}
	
	public HashMap<String, String> getlistepostesCode_postes()
	{
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		HashMap<String, String > mapcode_poste=new HashMap<String, String >();
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT intitule_poste,  code_poste FROM poste_travail_description where code_poste in(select distinct code_poste from planning_evaluation) ";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					String intitule_poste=rs.getString("intitule_poste");
					String code_poste=rs.getString("code_poste");
					
					mapcode_poste.put(intitule_poste, code_poste);
	
				}
				else {
					return mapcode_poste;
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
		
			
		return mapcode_poste;	
	
	}
	/**
	 * r�cup�ration des donn�es qui associe le repertoire de competence au poste de travail
	 * @throws SQLException 
	 */
	public CompetencePosteTravailBean getCompetencePosteTravailBean() throws SQLException
	{
		//ArrayList<String> listposteTravail = new ArrayList<String>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		
		Statement stmt;
		
		

			stmt = (Statement) conn.createStatement();
			//String select_structure="select distinct a.id_repertoire_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste from repertoire_competence a, poste_travail_competence b, poste_travail_description e where a.code_competence=b.code_competence and e.code_poste=b.code_poste and e.code_poste in(select distinct code_poste from planning_evaluation)"; 
			String select_structure="select distinct  a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste from repertoire_competence a, poste_travail_competence b, poste_travail_description e where a.code_competence=b.code_competence and e.code_poste=b.code_poste and e.code_poste in(select distinct code_poste from planning_evaluation)";
			/*String select_structure="select distinct  a.code_competence, a.famille, a.groupe, a.libelle_competence, e.intitule_poste" +
			" from  poste_travail_description e ,repertoire_competence a LEFT OUTER  JOIN poste_travail_competence b" +
			" on ( a.code_competence=a.code_competence) where e.code_poste in(select distinct code_poste from planning_evaluation)";
            */
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			CompetencePosteTravailBean competencePosteTravailBean=new CompetencePosteTravailBean();
			HashMap <String,HashMap<String, HashMap<String, ArrayList<String>> >> mapFamille=new HashMap <String,HashMap<String, HashMap<String, ArrayList<String>> >>();
			competencePosteTravailBean.setListefamilles(mapFamille);
			HashMap<String, HashMap<String, ArrayList<String>> > mapgroupe=new HashMap<String, HashMap<String, ArrayList<String>> >();
			HashMap<String, ArrayList<String>> mapcompetence=new HashMap<String, ArrayList<String>>();
			HashMap <String, String > mapCodeCompetence=new HashMap <String, String >();
			//int i=0;
			while(rs.next())
			{
				if (rs.getRow()>=1) 
				{
					
					//String id_repertoire_competence=rs.getString("id_repertoire_competence");
					String famille=rs.getString("famille");
					String groupe=rs.getString("groupe");
					String libelle_competence=rs.getString("libelle_competence");
					String intitule_poste=rs.getString("intitule_poste");
					String code_competence=rs.getString("code_competence");
					
					
					mapCodeCompetence.put(libelle_competence, code_competence);
					
					mapFamille=competencePosteTravailBean.getListefamilles();
					if(mapFamille.containsKey(famille))
					{
						mapgroupe=mapFamille.get(famille);
						if(mapgroupe.containsKey(groupe))
						{
							mapcompetence=mapgroupe.get(groupe);
							if(mapcompetence.containsKey(libelle_competence))
							{
								mapcompetence.get(libelle_competence).add(intitule_poste);
							}
							else
							{
								ArrayList<String> listeposte=new ArrayList<String>();
								listeposte.add(intitule_poste);
								
								mapcompetence.put(libelle_competence,listeposte );
							}
						}
						else
						{
							ArrayList<String> listeposte=new ArrayList<String>();
							listeposte.add(intitule_poste);
							HashMap<String, ArrayList<String>> mapcompetence1=new HashMap<String, ArrayList<String>>();
							
							mapcompetence1.put(libelle_competence,listeposte );
							mapgroupe.put(groupe, mapcompetence1);
						}
						
					}
					else
					{
						HashMap<String, HashMap<String, ArrayList<String>> > mapgroupe1=new HashMap<String, HashMap<String, ArrayList<String>> >();
						HashMap<String, ArrayList<String>> mapcompetence1=new HashMap<String, ArrayList<String>>();

						ArrayList<String> listeposte=new ArrayList<String>();
						listeposte.add(intitule_poste);
						mapcompetence1.put(libelle_competence,listeposte );
						mapgroupe1.put(groupe, mapcompetence1);
						mapFamille.put(famille, mapgroupe1);
					}

					competencePosteTravailBean.setListefamilles(mapFamille);
					competencePosteTravailBean.setMapCodeCompetence(mapCodeCompetence);
				}
				else 
				{
					return competencePosteTravailBean;
				}
				//competencePosteTravailBean.setListefamilles(mapFamille);
				
			}
			conn.close();
			return competencePosteTravailBean;

		
	}
	

}
