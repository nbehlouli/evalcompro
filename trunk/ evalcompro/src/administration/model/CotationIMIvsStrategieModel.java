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
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;
import common.PwdCrypt;

public class CotationIMIvsStrategieModel {
	

private ArrayList<CotationIMIvsStrategieBean>  listbean=null; 
private ListModel strset =null;
	
	/**
	 * cette m�thode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public List loadCotationCompetence() throws SQLException{
		
		
		listbean = new ArrayList<CotationIMIvsStrategieBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select id_cotation,label_cotation,definition_cotation,valeur_cotation from cotation_competence";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				CotationIMIvsStrategieBean bean=new CotationIMIvsStrategieBean();
				bean.setId_cotation(rs.getInt("id_cotation"));
				bean.setLabel_cotation(rs.getString("label_cotation"));
				bean.setDefinition_cotation(rs.getString("definition_cotation"));
				bean.setValeur_cotation(String.valueOf(rs.getInt("valeur_cotation")));
					  
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
	 * cette m�thode permet d'inserer la donn�e addedData dans la table structure_entreprise de la base de donn�e
	 * @param addedData
	 * @return
	 * @throws ParseException 
	 */
	public boolean addCotation(CotationIMIvsStrategieBean addedData) throws ParseException
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
	
		
		try 
		{
			                                                
			stmt = (Statement) conn.createStatement();
			String sql_query=" INSERT INTO cotation_competence(label_cotation, definition_cotation, valeur_cotation)  VALUES(#label_cotation,#definition_cotation, #valeur_cotation) ";
			
			sql_query = sql_query.replaceAll("#label_cotation", "'"+ addedData.getLabel_cotation().toUpperCase()+"'");
			sql_query = sql_query.replaceAll("#definition_cotation", "'"+ addedData.getDefinition_cotation()+"'");
			sql_query = sql_query.replaceAll("#valeur_cotation", "'"+ addedData.getValeur_cotation()+"'");
			
			 stmt.execute(sql_query);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La donn�e n'a pas �t� ins�r�e dans la base ", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	 * cette classe permet de controler la validit� des donn�es ins�r�es (par rapport � leurs taille)
	 * @param addedData
	 * @return
	 * @throws InterruptedException 
	 */
	
	/*public boolean controleIntegrite(CompagneCreationBean addedData) throws InterruptedException
	{
		try 
		{   
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
					
			
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			if(addedData.getNom_compagne().length()>50)
			{
				Messagebox.show("La taille du champ nom ne doit pas d�passer 50 caract�res", "Erreur",Messagebox.OK, Messagebox.ERROR);
				
				return false;
			}
			else
								if(addedData.getDate_deb_comp().after(addedData.getDate_fin_comp()))
								{
									Messagebox.show("La date fin de  compagne doit �tre superieure � la date debut compagne !", "Erreur",Messagebox.OK, Messagebox.ERROR);
									return false;
								}
								
									
		} 
		catch (InterruptedException e1) {
			 TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (NumberFormatException nfe)
	    {
			Messagebox.show("Le mot de passe doit �tre un entier compos� de 8 chiffres Exemple 21012001", "Erreur",Messagebox.OK, Messagebox.ERROR);
			return false;
	    }
		
			
		return true;
	}
	
	*/
	
	/**
	 * Cette classe permet de mettre � jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean updateCotation(CotationIMIvsStrategieBean addedData)	{
		
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try 
		{
		    stmt = (Statement) conn.createStatement();
            String sql_query=" update cotation_competence set label_cotation=#label_cotation, definition_cotation=#definition_cotation, valeur_cotation=#valeur_cotation where id_cotation=#id_cotation";         
        	sql_query = sql_query.replaceAll("#label_cotation", "'"+ addedData.getLabel_cotation().toUpperCase()+"'");
			sql_query = sql_query.replaceAll("#definition_cotation", "'"+ addedData.getDefinition_cotation()+"'");
			sql_query = sql_query.replaceAll("#valeur_cotation", "'"+ addedData.getValeur_cotation()+"'");
			sql_query = sql_query.replaceAll("#id_cotation", "'"+ addedData.getId_cotation()+"'");
			
			
				
			 stmt.executeUpdate(sql_query);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La modification n'a pas �t� prise en compte", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	 * cette classe permet de supprimer une donn�e de la table structure_entreprise
	 * @param codeStructure
	 * @throws SQLException 
	 */
	public Boolean deleteCotation(CotationIMIvsStrategieBean addedData) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="DELETE FROM  cotation_competence   where id_cotation=#id_cotation"; 
			sql_query = sql_query.replaceAll("#id_cotation", "'"+ addedData.getId_cotation()+"'");
			
		
			
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
			Messagebox.show("La donn�e n'a pas �t� ins�r�e dans la base ", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
			Messagebox.show("La donn�e n'a pas �t� modifi�e", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
			Messagebox.show("La donn�e n'a pas �t� supprim�e ", "Erreur",Messagebox.OK, Messagebox.ERROR);
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


}
