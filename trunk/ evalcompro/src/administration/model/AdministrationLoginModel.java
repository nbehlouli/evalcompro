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
	 * cette méthode fournit le contenu de la table structure_entreprise
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
			String sel_compte="select nom,prenom,c.login,c.pwd,libelle_profile,l.nom_base,val_date_deb,val_date_fin,modifiedpwd "+ 
                               "from compte c ,liste_db l ,profile p where c.database_id=l.database_id "+
                               "and c.id_profile=p.id_profile";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_compte);
			
			SimpleDateFormat formatDateJour = new SimpleDateFormat("dd/MM/yyyy");
			 
			
			while(rs.next()){
				
					AdministrationLoginBean admin_compte=new AdministrationLoginBean();
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
	 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
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
			int idprofile1=getKeyMap(addedData.getProfile());
			select_structure = select_structure.replaceAll("#id_profile", Integer.toString(idprofile1));
			select_structure = select_structure.replaceAll("#login", "'"+addedData.getLogin()+"'");
			select_structure = select_structure.replaceAll("#pwd", "'"+addedData.getMotdepasse()+"'");
			select_structure = select_structure.replaceAll("#database_id", Integer.toString((Integer)getDatabaseList().get((addedData.getBasedonnee()))));
			//Date datedeb=formatter.parse(addedData.getDate_deb_val());
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
	/**
	 * cette classe permet de controler la validité des données insérées (par rapport à leurs taille)
	 * @param addedData
	 * @return
	 */
	public boolean controleIntegrite(AdministrationLoginBean addedData)
	{
		try 
		{   DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			if(addedData.getNom().length()>50)
			{
				Messagebox.show("La taille du champ nom ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			else
				if(addedData.getPrenom().length()>50)
				{
					Messagebox.show("La taille du champ prénom ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
					return false;
				}
				else
					if(addedData.getLogin().length()>11)
					{
						Messagebox.show("La taille du champ login ne doit pas dépasser 10 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
						return false;
					}
					else
						if(addedData.getMotdepasse().length()>9)
						{
							Messagebox.show("La taille du champ mot de passe ne doit pas dépasser 8 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
							return false;
						}
						else
							if(addedData.getBasedonnee().length()>50)
							{
								Messagebox.show("La taille du champ base de données ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
								return false;
							}
							else
								if(!isValidDateStr(addedData.getDate_deb_val()))
								{
									Messagebox.show("La date debut validité doit être au format AAAA/MM/DD", "Erreur",Messagebox.OK, Messagebox.ERROR);
									return false;
								}
								else
									if(!isValidDateStr(addedData.getDate_fin_val()))
									{
										Messagebox.show("La date fin validité doit être au format AAAA/MM/DD", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	 * Cette classe permet de mettre à jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	/*public Boolean majStructureEntrepriseBean(StructureEntrepriseBean addedData, String selectedCodeStructure)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String update_structure="UPDATE  structure_entreprise SET code_structure=#code_structure ,code_division=#code_division,libelle_division=#libelle_division,code_direction=#code_direction, libelle_direction=#libelle_direction,code_unite=#code_unite,libelle_unite=#libelle_unite,code_departement=#code_departement,libelle_departement=#libelle_departement,code_service=#code_service,libelle_service=#libelle_service,code_section=#code_section,libelle_section=#libelle_section WHERE code_structure=#valeur_code_structure"; 
			update_structure = update_structure.replaceAll("#code_structure", "'"+addedData.getCodestructure()+"'");
			update_structure = update_structure.replaceAll("#code_division", "'"+addedData.getCodeDivision()+"'");
			update_structure = update_structure.replaceAll("#libelle_division", "'"+addedData.getLibelleDivision()+"'");
			update_structure = update_structure.replaceAll("#code_direction", "'"+addedData.getCodeDirection()+"'");
			update_structure = update_structure.replaceAll("#libelle_direction", "'"+addedData.getLibelleDirection()+"'");
			update_structure = update_structure.replaceAll("#code_unite", "'"+addedData.getCodeUnite()+"'");
			update_structure = update_structure.replaceAll("#libelle_unite", "'"+addedData.getLibelleUnite()+"'");
			update_structure = update_structure.replaceAll("#code_departement", "'"+addedData.getCodeDepartement()+"'");
			update_structure = update_structure.replaceAll("#libelle_departement", "'"+addedData.getLibelleDepartement()+"'");
			update_structure = update_structure.replaceAll("#code_service", "'"+addedData.getCodeService()+"'");
			update_structure = update_structure.replaceAll("#libelle_service", "'"+addedData.getLibelleService()+"'");
			update_structure = update_structure.replaceAll("#code_section", "'"+addedData.getCodesection()+"'");
			update_structure = update_structure.replaceAll("#libelle_section", "'"+addedData.getLibelleSection()+"'");
			update_structure = update_structure.replaceAll("#valeur_code_structure", "'"+selectedCodeStructure+"'");
		System.out.println(update_structure);
			
			 stmt.executeUpdate(update_structure);
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
	}*/
	/**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 */
	/*public void supprimerStructureEntrepriseBean(String codeStructure)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String update_structure="DELETE FROM  structure_entreprise  WHERE code_structure=#code_structure"; 
			update_structure = update_structure.replaceAll("#code_structure", "'"+codeStructure+"'");
			
		System.out.println(update_structure);
			
			 stmt.executeUpdate(update_structure);
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
	}*/
	
	/**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
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
