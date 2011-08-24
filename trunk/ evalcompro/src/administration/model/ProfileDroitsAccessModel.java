package administration.model;

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
import administration.bean.CotationIMIvsStrategieBean;
import administration.bean.DataBaseClientLinkBean;
import administration.bean.DatabaseManagementBean;
import administration.bean.DroitsAccessBean;
import administration.bean.IMIvsStrategieBean;
import administration.bean.ProfileDroitsAccessBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;
import common.PwdCrypt;

public class ProfileDroitsAccessModel {
	

private ArrayList<ProfileDroitsAccessBean>  listbean=null; 


private ListModel strset =null;
	
	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public List loadProfile() throws SQLException{
		
		
		listbean = new ArrayList<ProfileDroitsAccessBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select  id_profile ,libelle_profile  from profile";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				ProfileDroitsAccessBean bean=new ProfileDroitsAccessBean();
				bean.setId_profile(rs.getInt("id_profile"));
				bean.setLibelle_profile(rs.getString("libelle_profile"));
		 
				listbean.add(bean);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		
	
		return listbean;
	
		
		
	}
	
	/**
	 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
	 * @param addedData
	 * @return
	 * @throws ParseException 
	 */
	public boolean addProfile(ProfileDroitsAccessBean addedData) throws ParseException
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
	
		
		try 
		{
			                                                
			stmt = (Statement) conn.createStatement();
			String sql_query=" INSERT INTO profile( libelle_profile)  VALUES(#libelle_profile) ";
			
			sql_query = sql_query.replaceAll("#libelle_profile", "'"+ addedData.getLibelle_profile().toUpperCase()+"'");
		
			 stmt.execute(sql_query);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La donnée n'a pas été insérée dans la base ", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				 //TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 //TODO Auto-generated catch block
			try {
				stmt.close();conn.close();
			} catch (SQLException e1) {
				 //TODO Auto-generated catch block
				
				e1.printStackTrace();
				return false;
			}
			
			
			return false;
		}
		try {
			stmt.close();conn.close();
		} catch (SQLException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
		
	/**
	 * Cette classe permet de mettre à jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean updateProfile(ProfileDroitsAccessBean addedData)	{
		
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		
		try 
		{
		    stmt = (Statement) conn.createStatement();
			String sql_query=" update profile set  libelle_profile=#libelle_profile where id_profile=#id_profile  ";
			
			sql_query = sql_query.replaceAll("#libelle_profile", "'"+ addedData.getLibelle_profile().toUpperCase()+"'");
			sql_query = sql_query.replaceAll("#id_profile", "'"+ addedData.getId_profile()+"'");
			
				
			 stmt.executeUpdate(sql_query);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La modification n'a pas été prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				 //TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 //TODO Auto-generated catch block
			try {
				stmt.close();conn.close();
			} catch (SQLException e1) {
				 //TODO Auto-generated catch block
				
				e1.printStackTrace();
				return false;
			}
			
			
			return false;
		}
		try {
			stmt.close();conn.close();
		} catch (SQLException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 * @throws SQLException 
	 */
	public Boolean deleteProfile(ProfileDroitsAccessBean addedData) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
           String sql_query=" delete from  profile  where id_profile=#id_profile  ";
	    	sql_query = sql_query.replaceAll("#id_profile", "'"+ addedData.getId_profile()+"'");
			
			
		
			
			 stmt.executeUpdate(sql_query);
		} 
		catch (SQLException e) 
		{
			
				e.printStackTrace();
				stmt.close();conn.close();
				return false;


		}
		try {
			stmt.close();conn.close();
		} catch (SQLException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public HashMap getListDB() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		HashMap map = new HashMap();
				
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select  distinct database_id , upper(nom_base) as nom_base from liste_db"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("nom_base"), rs.getInt("database_id"));
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
	
public List loadDatabaseClientlist() throws SQLException{
		
		
		List listbean = new ArrayList<DataBaseClientLinkBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select  client_id,nom_client,secteur_id,nom_secteur,upper(l.nom_base) as nom_base" +
					        " from cross_db c, liste_db l where l.database_id=c.database_id";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				DataBaseClientLinkBean bean=new DataBaseClientLinkBean();
				bean.setClient_id(rs.getInt("client_id"));
				bean.setNom_client(rs.getString("nom_client"));
				bean.setSecteur_id(rs.getInt("secteur_id"));
				bean.setNom_secteur(rs.getString("nom_secteur"));
				//bean.setDatabase_id(rs.getInt("database_id"));
				bean.setNom_base(rs.getString("nom_base"));
				
				  
				listbean.add(bean);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listbean;

 }

public boolean addLinkDatabaseClient(DataBaseClientLinkBean addedData) throws ParseException
{
	
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToDB();
	Statement stmt = null;

	
	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		String sql_query=" INSERT INTO cross_db(nom_client, secteur_id, nom_secteur, database_id)   VALUES(#nom_client,#secteur_id,#nom_secteur,#database_id);";
		
		sql_query = sql_query.replaceAll("#nom_client", "'"+ addedData.getNom_client().toUpperCase()+"'");
		sql_query = sql_query.replaceAll("#secteur_id", "'"+ 0+"'");
		sql_query = sql_query.replaceAll("#nom_secteur", "'"+ addedData.getNom_secteur().toUpperCase()+"'");
		sql_query = sql_query.replaceAll("#database_id", "'"+ addedData.getDatabase_id()+"'");
		
		 stmt.execute(sql_query);
	} 
	catch (SQLException e) 
	{
		try 
		{
			Messagebox.show("La donnée n'a pas été insérée dans la base ", "Erreur",Messagebox.OK, Messagebox.ERROR);
		} 
		catch (InterruptedException e1) {
			 //TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 //TODO Auto-generated catch block
		try {
			stmt.close();conn.close();
		} catch (SQLException e1) {
			 //TODO Auto-generated catch block
			
			e1.printStackTrace();
			return false;
		}
		
		
		return false;
	}
	try {
		stmt.close();conn.close();
	} catch (SQLException e) {
		 //TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	}
	return true;
}

public boolean updateLinkDatabaseClient(DataBaseClientLinkBean addedData) throws ParseException
{
	
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToDB();
	Statement stmt = null;

	
	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		String sql_query=" Update cross_db set nom_client=#nom_client, secteur_id=#secteur_id,nom_secteur=#nom_secteur,database_id=#database_id  where client_id=#client_id";
		
		sql_query = sql_query.replaceAll("#nom_client", "'"+ addedData.getNom_client().toUpperCase()+"'");
		sql_query = sql_query.replaceAll("#secteur_id", "'"+ 0+"'");
		sql_query = sql_query.replaceAll("#nom_secteur", "'"+ addedData.getNom_secteur().toUpperCase()+"'");
		sql_query = sql_query.replaceAll("#database_id", "'"+ addedData.getDatabase_id()+"'");
		sql_query = sql_query.replaceAll("#client_id", "'"+ addedData.getClient_id()+"'");
		
		 stmt.execute(sql_query);
	} 
	catch (SQLException e) 
	{
		try 
		{
			Messagebox.show("La donnée n'a pas été modifiée", "Erreur",Messagebox.OK, Messagebox.ERROR);
		} 
		catch (InterruptedException e1) {
			 //TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 //TODO Auto-generated catch block
		try {
			stmt.close();conn.close();
		} catch (SQLException e1) {
			 //TODO Auto-generated catch block
			
			e1.printStackTrace();
			return false;
		}
		
		
		return false;
	}
	try {
		stmt.close();conn.close();
	} catch (SQLException e) {
		 //TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	}
	return true;
}

public boolean deleteLinkDatabaseClient(DataBaseClientLinkBean addedData) throws ParseException
{
	
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToDB();
	Statement stmt = null;

	
	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		String sql_query=" delete from cross_db where client_id=#client_id";
		
		sql_query = sql_query.replaceAll("#client_id", "'"+ addedData.getClient_id()+"'");
		
		 stmt.execute(sql_query);
	} 
	catch (SQLException e) 
	{
		try 
		{
			Messagebox.show("La donnée n'a pas été supprimée ", "Erreur",Messagebox.OK, Messagebox.ERROR);
		} 
		catch (InterruptedException e1) {
			 //TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 //TODO Auto-generated catch block
		try {
			stmt.close();conn.close();
		} catch (SQLException e1) {
			 //TODO Auto-generated catch block
			
			e1.printStackTrace();
			return false;
		}
		
		
		return false;
	}
	try {
		stmt.close();conn.close();
	} catch (SQLException e) {
		 //TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	}
	return true;
}

public HashMap list_valeursCotation() {
	
	HashMap map = new HashMap();
	for (int i=1 ;i<6;i++){
		map.put(i, i);
	}
	
	return (HashMap) sortByComparator(map);
}

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


public List loadDroitsAccess() throws SQLException{
	
	
	List listbean1 = new ArrayList<DroitsAccessBean>();
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToDB();
	Statement stmt = null;
	
	try {
		stmt = (Statement) conn.createStatement();
		String sql_query="select distinct d.code_ecran,l.libelle_menu,l.libelle_ecran,d.hide,d.ecriture,d.lecture " +
				        " from droits d , liste_ecran  l where d.code_ecran=l.code_ecran order by l.libelle_menu,l.libelle_ecran";
		
		ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
		
		while(rs.next()){
			
			DroitsAccessBean bean=new DroitsAccessBean();
			 bean.setCode_ecran(rs.getString("code_ecran"));
			 bean.setLibelle_menu(rs.getString("libelle_menu"));
			 bean.setLibelle_ecran(rs.getString("libelle_ecran"));
			 bean.setHide(rs.getString("hide"));
			 bean.setEcriture(rs.getString("ecriture"));
			 bean.setLecture(rs.getString("lecture"));
			 
			 
			
	 
			listbean1.add(bean);
			   
				
			}
		stmt.close();
		conn.close();
		
	} catch (SQLException e) {
		stmt.close();
		conn.close();
		
	}

	return listbean1;

	
	
}



public HashMap selectProfiles() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToDB();
	Statement stmt = null;
	HashMap map = new HashMap();
		
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sql_query="select id_profile,libelle_profile from profile "; 
		
		ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
		
		
		while(rs.next()){
			map.put(rs.getString("libelle_profile"), rs.getInt("id_profile"));
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




}
