package administration.model;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;

import administration.bean.AdministrationLoginBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

public class AdministrationLoginModel {
	

private ArrayList<AdministrationLoginBean>  listlogin =null; 
private ListModel strset =null;
	
	/**
	 * cette m�thode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public List checkLoginBean() throws SQLException{
		
		listlogin = new ArrayList<AdministrationLoginBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_compte="select id_compte,nom,prenom,c.login,c.pwd,libelle_profile,l.nom_base,DATE_FORMAT(val_date_deb,'%Y/%m/%d') as val_date_deb,DATE_FORMAT(val_date_fin,'%Y/%m/%d') as val_date_fin ,modifiedpwd "+ 
                               "from compte c ,liste_db l ,profile p where c.database_id=l.database_id "+
                               "and c.id_profile=p.id_profile";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_compte);
			
			SimpleDateFormat formatDateJour = new SimpleDateFormat("yyyy/MM/dd");
			 
			
			while(rs.next()){
				
					AdministrationLoginBean admin_compte=new AdministrationLoginBean();
					
					admin_compte.setId_compte(Integer.toString(rs.getInt("id_compte")));
					admin_compte.setNom(rs.getString("nom"));
					admin_compte.setPrenom(rs.getString("prenom"));
					admin_compte.setLogin(rs.getString("login"));
					admin_compte.setMotdepasse(rs.getString("pwd"));
					admin_compte.setProfile(rs.getString("libelle_profile"));
					admin_compte.setBasedonnee(rs.getString("l.nom_base"));
					admin_compte.setDate_deb_val(formatDateJour.format(rs.getDate("val_date_deb")));
					admin_compte.setDate_fin_val(formatDateJour.format(rs.getDate("val_date_fin")));
					admin_compte.setDatemodifpwd(rs.getString("modifiedpwd"));
					
					  
					listlogin.add(admin_compte);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listlogin;
	
		
		
	}
	
	/**
	 * cette m�thode permet d'inserer la donn�e addedData dans la table structure_entreprise de la base de donn�e
	 * @param addedData
	 * @return
	 * @throws ParseException 
	 */
	public boolean addAdministrationLoginBean(AdministrationLoginBean addedData) throws ParseException
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt;
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		
		try 
		{
			                                                
			stmt = (Statement) conn.createStatement();
			String select_structure="INSERT INTO compte ( id_profile, login, pwd, database_id, val_date_deb, val_date_fin, modifiedpwd, nom, prenom) VALUES (#id_profile,#login,#pwd,#database_id,#val_date_deb,#val_date_fin,#modifiedpwd,#nom,#prenom)";
			select_structure = select_structure.replaceAll("#id_profile", Integer.toString(getKeyMap(addedData.getProfile())));
			select_structure = select_structure.replaceAll("#login", "'"+addedData.getLogin()+"'");
			select_structure = select_structure.replaceAll("#pwd", "'"+addedData.getMotdepasse()+"'");
			select_structure = select_structure.replaceAll("#database_id", Integer.toString((Integer)getDatabaseList().get((addedData.getBasedonnee()))));
			select_structure = select_structure.replaceAll("#val_date_deb", "'"+addedData.getDate_deb_val()+"'");
			select_structure = select_structure.replaceAll("#val_date_fin", "'"+addedData.getDate_fin_val()+"'");
			select_structure = select_structure.replaceAll("#modifiedpwd", "'"+getCurrentDatetime()+"'");
			select_structure = select_structure.replaceAll("#nom", "'"+addedData.getNom()+"'");
			select_structure = select_structure.replaceAll("#prenom", "'"+addedData.getPrenom()+"'");
			
		//System.out.println(select_structure);
			
			 stmt.execute(select_structure);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La donn�e n'a pas �t� ins�r�e dans la base car il existe une donn�e ayant le m�me code �tablissement", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	 * cette classe permet de controler la validit� des donn�es ins�r�es (par rapport � leurs taille)
	 * @param addedData
	 * @return
	 */
	public boolean controleIntegrite(AdministrationLoginBean addedData)
	{
		try 
		{   DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			if(addedData.getNom().length()>50)
			{
				Messagebox.show("La taille du champ nom ne doit pas d�passer 50 caract�res", "Erreur",Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			else
				if(addedData.getPrenom().length()>50)
				{
					Messagebox.show("La taille du champ pr�nom ne doit pas d�passer 50 caract�res", "Erreur",Messagebox.OK, Messagebox.ERROR);
					return false;
				}
				else
					if(addedData.getLogin().length()>11)
					{
						Messagebox.show("La taille du champ login ne doit pas d�passer 10 caract�res", "Erreur",Messagebox.OK, Messagebox.ERROR);
						return false;
					}
					else
						if(addedData.getMotdepasse().length()>9)
						{
							Messagebox.show("La taille du champ mot de passe ne doit pas d�passer 8 caract�res", "Erreur",Messagebox.OK, Messagebox.ERROR);
							return false;
						}
						else
							if(addedData.getBasedonnee().length()>50)
							{
								Messagebox.show("La taille du champ base de donn�es ne doit pas d�passer 50 caract�res", "Erreur",Messagebox.OK, Messagebox.ERROR);
								return false;
							}
							else
								if(!isValidDateStr(addedData.getDate_deb_val()))
								{
									Messagebox.show("La date debut validit� doit �tre au format AAAA/MM/DD", "Erreur",Messagebox.OK, Messagebox.ERROR);
									return false;
								}
								else
									if(!isValidDateStr(addedData.getDate_fin_val()))
									{
										Messagebox.show("La date fin validit� doit �tre au format AAAA/MM/DD", "Erreur",Messagebox.OK, Messagebox.ERROR);
										return false;
									}
									
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
			
		return true;
	}
	
	/**
	 * Cette classe permet de mettre � jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean majAdminLoginBean(AdministrationLoginBean addedData)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="UPDATE  compte SET id_profile=#id_profile ,login=#login,pwd=#pwd,database_id=#database_id, val_date_deb=#val_date_deb,val_date_fin=#val_date_fin,modifiedpwd=#modifiedpwd,nom=#nom,prenom=#prenom WHERE id_compte=#valeur_id_compte"; 
			select_structure = select_structure.replaceAll("#id_profile", Integer.toString(getKeyMap(addedData.getProfile())));
			select_structure = select_structure.replaceAll("#login", "'"+addedData.getLogin()+"'");
			select_structure = select_structure.replaceAll("#pwd", "'"+addedData.getMotdepasse()+"'");
			select_structure = select_structure.replaceAll("#database_id", Integer.toString((Integer)getDatabaseList().get((addedData.getBasedonnee()))));
			select_structure = select_structure.replaceAll("#val_date_deb", "'"+addedData.getDate_deb_val()+"'");
			select_structure = select_structure.replaceAll("#val_date_fin", "'"+addedData.getDate_fin_val()+"'");
			select_structure = select_structure.replaceAll("#modifiedpwd", "'"+getCurrentDatetime()+"'");
			select_structure = select_structure.replaceAll("#nom", "'"+addedData.getNom()+"'");
			select_structure = select_structure.replaceAll("#prenom", "'"+addedData.getPrenom()+"'");
			select_structure = select_structure.replaceAll("#valeur_id_compte", addedData.getId_compte());
			
		   //System.out.println(update_structure);
			
			 stmt.executeUpdate(select_structure);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La modification n'a pas �t� prise en compte car il existe une donn�e ayant le m�me code �tablissement", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	 * cette classe permet de supprimer une donn�e de la table structure_entreprise
	 * @param codeStructure
	 */
	public void supprimerLogin(AdministrationLoginBean addedData)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sup_login="DELETE FROM  compte  WHERE id_compte=#valeur_id_compte"; 
			sup_login = sup_login.replaceAll("#valeur_id_compte", addedData.getId_compte());
			
		
			
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
	
	/**
	 * cette classe permet de supprimer une donn�e de la table structure_entreprise
	 * @param codeStructure
	 * @throws SQLException 
	 */
	public HashMap gerProfileList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		List list_profile=new ArrayList();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select  id_profile, libelle_profile from profile "; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
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
	    }


}
