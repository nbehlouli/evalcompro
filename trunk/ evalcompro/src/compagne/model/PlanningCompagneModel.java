package compagne.model;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;

import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;
import common.PwdCrypt;
import compagne.bean.PlanningCompagneListBean;

public class PlanningCompagneModel {
	

private ArrayList<PlanningCompagneListBean>  listcompagne =null; 
private ListModel strset =null;
	
	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public List loadPlanningCompagnelist() throws SQLException{
		
		
		listcompagne = new ArrayList<PlanningCompagneListBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select id_planning_evaluation,libelle_compagne , (select concat(nom,' ', prenom) from employe where id_employe in(id_evaluateur)) as evaluateur,concat (nom,' ', prenom) as evalue,intitule_poste,p.code_structure," +
					        " date_evaluation,heure_debut_evaluation,heure_fin_evaluation,lieu,personne_ressources " +
					        " from planning_evaluation p,employe e,compagne_evaluation c, poste_travail_description d " +
					        " where   p.id_compagne=c.id_compagne and   p.id_employe=e.id_employe and   p.code_poste=d.code_poste";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				PlanningCompagneListBean compagne=new PlanningCompagneListBean();
				compagne.setId_planning_evaluation((rs.getInt("id_planning_evaluation")));	
				compagne.setLibelle_compagne((rs.getString("libelle_compagne")));
				compagne.setEvaluateur((rs.getString("evaluateur")));
				compagne.setEvalue(((rs.getString("evalue"))));
				compagne.setIntitule_poste((((rs.getString("intitule_poste")))));
				compagne.setCode_structure((rs.getString("code_structure")));
		    	compagne.setDate_evaluation(rs.getDate("date_evaluation"));
				compagne.setHeure_debut_evaluation(rs.getString("heure_debut_evaluation"));
				compagne.setHeure_fin_evaluation(rs.getString("heure_fin_evaluation"));
				compagne.setLieu(rs.getString("lieu"));
				compagne.setPersonne_ressources(rs.getString("personne_ressources"));
				listcompagne.add(compagne);
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listcompagne;
	
		
		
	}
	
	/**
	 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
	 * @param addedData
	 * @return
	 * @throws ParseException 
	 */
	public boolean addPlanningCompagne(PlanningCompagneListBean addedData) throws ParseException
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		
		try 
		{
			                                                
			stmt = (Statement) conn.createStatement();
			String sql_query="INSERT INTO planning_evaluation(id_compagne, id_employe, date_evaluation, id_evaluateur, heure_debut_evaluation, heure_fin_evaluation, lieu, code_poste, personne_ressources, code_structure)" +
					" VALUES(#id_compagne,#id_employe, #date_evaluation, #id_evaluateur, #heure_debut_evaluation, #heure_fin_evaluation, #lieu, #code_poste, #personne_ressources, #code_structure);";
			
			sql_query = sql_query.replaceAll("#id_compagne", "'"+ addedData.getId_compagne()+"'");
			sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_evalue()+"'");
			sql_query = sql_query.replaceAll("#date_evaluation", "'"+ formatter.format(addedData.getDate_evaluation())+"'");
			sql_query = sql_query.replaceAll("#id_evaluateur", "'"+ addedData.getId_evaluateur()+"'");
			sql_query = sql_query.replaceAll("#heure_debut_evaluation", "'"+ addedData.getHeure_debut_evaluation()+"'");
			sql_query = sql_query.replaceAll("#heure_fin_evaluation", "'"+ addedData.getHeure_fin_evaluation()+"'");
			sql_query = sql_query.replaceAll("#lieu", "'"+ addedData.getLieu()+"'");
			sql_query = sql_query.replaceAll("#code_poste", "'"+ addedData.getCode_poste()+"'");
			sql_query = sql_query.replaceAll("#personne_ressources", "'"+ addedData.getPersonne_ressources()+"'");
			sql_query = sql_query.replaceAll("#code_structure", "'"+ addedData.getCode_structure()+"'");
				
		//System.out.println(select_structure);
			
			 stmt.execute(sql_query);
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
				stmt.close();
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
				return false;
			}
			
			
			return false;
		}
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * cette classe permet de controler la validité des données insérées (par rapport à leurs taille)
	 * @param addedData
	 * @return
	 * @throws InterruptedException 
	 */
	
	public boolean controleIntegrite(PlanningCompagneListBean addedData ,Map heuredebut,Map heurefin) throws InterruptedException
	{
		try 
		{   	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

		       Integer int_heuredebut=(Integer) heuredebut.get(addedData.getHeure_debut_evaluation());
		       Integer int_heurefin=(Integer) heurefin.get(addedData.getHeure_fin_evaluation());

					
			
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			if(addedData.getLieu().length()>30)
			{
				Messagebox.show("Le lieu ne doit pas dépasser 30 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
				
				return false;
			}
			else
				if(addedData.getPersonne_ressources().length()>80)
				{
				Messagebox.show("La personne ressources ne doit pas dépasser 30 caractères !", "Erreur",Messagebox.OK, Messagebox.ERROR);
				return false;
				}
				else
					if(int_heuredebut >= int_heurefin)
					{
					Messagebox.show("L'heure de fin de l'évaluation doit  être superieure à l'heure de debut !", "Erreur",Messagebox.OK, Messagebox.ERROR);
					return false;
					}
								
									
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (NumberFormatException nfe)
	    {
			Messagebox.show("Le mot de passe doit être un entier composé de 8 chiffres Exemple 21012001", "Erreur",Messagebox.OK, Messagebox.ERROR);
			return false;
	    }
		
			
		return true;
	}
	
	/**
	 * Cette classe permet de mettre à jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean updateCompagne(PlanningCompagneListBean addedData)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
		    stmt = (Statement) conn.createStatement();
		    String sql_query="update planning_evaluation set id_compagne=#id_compagne, id_employe=#id_employe, date_evaluation=#date_evaluation, id_evaluateur=#id_evaluateur, " +
		    		         " heure_debut_evaluation=#heure_debut_evaluation, heure_fin_evaluation=#heure_fin_evaluation, lieu=#lieu," +
		    		         " code_poste=#code_poste, personne_ressources=#personne_ressources, code_structure=#code_structure  where id_planning_evaluation=#id_planning_evaluation" ;
			
	
			sql_query = sql_query.replaceAll("#id_compagne", "'"+ addedData.getId_compagne()+"'");
			sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_evalue()+"'");
			sql_query = sql_query.replaceAll("#date_evaluation", "'"+ formatter.format(addedData.getDate_evaluation())+"'");
			sql_query = sql_query.replaceAll("#id_evaluateur", "'"+ addedData.getId_evaluateur()+"'");
			sql_query = sql_query.replaceAll("#heure_debut_evaluation", "'"+ addedData.getHeure_debut_evaluation()+"'");
			sql_query = sql_query.replaceAll("#heure_fin_evaluation", "'"+ addedData.getHeure_fin_evaluation()+"'");
			sql_query = sql_query.replaceAll("#lieu", "'"+ addedData.getLieu()+"'");
			sql_query = sql_query.replaceAll("#code_poste", "'"+ addedData.getCode_poste()+"'");
			sql_query = sql_query.replaceAll("#personne_ressources", "'"+ addedData.getPersonne_ressources()+"'");
			sql_query = sql_query.replaceAll("#code_structure", "'"+ addedData.getCode_structure()+"'");
			sql_query = sql_query.replaceAll("#id_planning_evaluation", "'"+ addedData.getId_planning_evaluation()+"'");
		
			
		   //System.out.println(update_structure);
			
			 stmt.executeUpdate(sql_query);
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
	public Boolean deleteCompagne(PlanningCompagneListBean addedData)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="DELETE FROM  planning_evaluation   where id_planning_evaluation=#id_planning_evaluation" ; 
			sql_query = sql_query.replaceAll("#id_planning_evaluation", "'"+ addedData.getId_planning_evaluation()+"'");
			
		
			
			 stmt.executeUpdate(sql_query);
		} 
		catch (SQLException e) 
		{
			
				e.printStackTrace();
				return false;


		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/*
	*//**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 * @throws SQLException 
	 *//*
	*/
	
	public HashMap getCompagneValid() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select id_compagne,libelle_compagne from compagne_evaluation" +
					" where date_debut<=now() and date_fin>=now()"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("libelle_compagne"), rs.getInt("id_compagne"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	public HashMap getListEvaluteur() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select id_employe,concat(nom,' ',prenom) as evaluateur from employe where est_evaluateur='Y'"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("evaluateur"), rs.getInt("id_employe"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	public HashMap getListEvalue() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select id_employe,concat(nom,' ',prenom) as evalue from employe where est_evaluateur='N'"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("evalue"), rs.getInt("id_employe"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}
	
	
	public HashMap getListPoste() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select code_poste,intitule_poste from poste_travail_description"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	public HashMap getListStructure() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select code_structure  from structure_entreprise"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("code_structure"), rs.getString("code_structure"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	
	public HashMap selectedPoste(int employe) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select e.code_poste,intitule_poste from  employe e ,poste_travail_description t" +
					"  where id_employe=#employe and e.code_poste=t.code_poste "; 
			sql_query = sql_query.replaceAll("#employe", "'"+employe+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
			
			while(rs.next()){
				map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	
	public HashMap selectEmployes(String code_poste) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select id_employe,concat(nom,' ',prenom) as evalue from employe where est_evaluateur='N'" +
					         "  and code_poste=#code_poste  "; 
			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
			
			while(rs.next()){
				map.put(rs.getString("evalue"), rs.getInt("id_employe"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}
	


	public HashMap getHeureDebut() 
	{
		
		HashMap map = new HashMap();
		
		for (int i=8;i<=20;i++){
			map.put(i+":00", i);
		}
		return (HashMap) sortByComparator(map);
	}
	
	public HashMap getHeureFin()
	{
		
		HashMap map = new HashMap();
		
		for (int i=8;i<=20;i++){
			map.put(i+":00", i);
		}
		return (HashMap) sortByComparator(map);
	}	
	
	
	
	
	/*
	
	public HashMap getDatabaseList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String db_list="select  database_id, nom_base from liste_db"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(db_list);
			
			
			while(rs.next()){
				map.put( rs.getString("nom_base"),rs.getInt("database_id"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	
	public String getCurrentDatetime(){
		Date today = Calendar.getInstance().getTime();
	    // (2) create our "formatter" (our custom format)
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	    // (3) create a new String in the format we want
	    String todaydate = formatter.format(today);
	    
	    return todaydate;
		
	}
	
	public  Integer getKeyMap(String key) throws SQLException{
		Integer idprofile=(Integer)gerProfileList() .get(key);
		return idprofile;
	}
	
	public static boolean isValidDateStr(String date) {
	    try {
	      String format="yyyy/MM/dd";
	    	SimpleDateFormat sdf = new SimpleDateFormat(format);
	      sdf.setLenient(false);
	      sdf.parse(date);
	    }
	    catch (ParseException e) {
	      return false;
	    }
	    catch (IllegalArgumentException e) {
	      return false;
	    }
	    return true;
	    }*/
	
	
	 private static Map sortByComparator(Map unsortMap) {
		 
	        List list = new LinkedList(unsortMap.entrySet());
	 
	        //sort list based on comparator
	        Collections.sort(list, new Comparator() {
	             public int compare(Object o1, Object o2) {
		           return ((Comparable) ((Map.Entry) (o1)).getValue())
		           .compareTo(((Map.Entry) (o2)).getValue());
	             }
		});
	 
	        //put sorted list into map again
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
		     Map.Entry entry = (Map.Entry)it.next();
		     sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	   }	
	 
	 public HashMap setlectedStructure(String code_structure) throws SQLException
		{
			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToEntrepriseDB();
			Statement stmt = null;
			HashMap map = new HashMap();

			
			try 
			{
				stmt = (Statement) conn.createStatement();
				String sql_query="select concat_ws('-->',libelle_division,libelle_direction,libelle_unite,libelle_departement,libelle_service,libelle_section) as structure from structure_entreprise where code_structure=#code_structure";
				sql_query = sql_query.replaceAll("#code_structure", "'"+code_structure+"'");
				ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
				
				
				while(rs.next()){
					map.put( rs.getString("structure"),rs.getString("structure"));
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
