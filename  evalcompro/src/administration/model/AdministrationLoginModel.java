package administration.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Messagebox;

import administration.bean.AdministrationLoginBean;
import administration.bean.StructureEntrepriseBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

public class AdministrationLoginModel {
	

private ArrayList<AdministrationLoginBean>  listlogin =null; 
	
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
			
			
			while(rs.next()){
				
					AdministrationLoginBean admin_compte=new AdministrationLoginBean();
					admin_compte.setNom(rs.getString("nom"));
					admin_compte.setPrenom(rs.getString("prenom"));
					admin_compte.setLogin(rs.getString("login"));
					admin_compte.setProfile(rs.getString("libelle_profile"));
					admin_compte.setBasedonnee(rs.getString("l.nom_base"));
					admin_compte.setDate_deb_val(rs.getDate("val_date_deb"));
					admin_compte.setDate_fin_val(rs.getDate("val_date_fin"));
					admin_compte.setDatemodifpwd(rs.getDate("modifiedpwd"));
					
					  
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
	 */
	public boolean addStructureEntrepriseBean(StructureEntrepriseBean addedData)
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String select_structure="INSERT INTO structure_entreprise (code_structure,code_division,libelle_division,code_direction, libelle_direction,code_unite,libelle_unite,code_departement,libelle_departement,code_service,libelle_service,code_section,libelle_section) VALUES (#code_structure,#code_division,#libelle_division,#code_direction,#libelle_direction,#code_unite,#libelle_unite,#code_departement,#libelle_departement,#code_service,#libelle_service,#code_section,#libelle_section)";
			select_structure = select_structure.replaceAll("#code_structure", "'"+addedData.getCodestructure()+"'");
			select_structure = select_structure.replaceAll("#code_division", "'"+addedData.getCodeDivision()+"'");
			select_structure = select_structure.replaceAll("#libelle_division", "'"+addedData.getLibelleDivision()+"'");
			select_structure = select_structure.replaceAll("#code_direction", "'"+addedData.getCodeDirection()+"'");
			select_structure = select_structure.replaceAll("#libelle_direction", "'"+addedData.getLibelleDirection()+"'");
			select_structure = select_structure.replaceAll("#code_unite", "'"+addedData.getCodeUnite()+"'");
			select_structure = select_structure.replaceAll("#libelle_unite", "'"+addedData.getLibelleUnite()+"'");
			select_structure = select_structure.replaceAll("#code_departement", "'"+addedData.getCodeDepartement()+"'");
			select_structure = select_structure.replaceAll("#libelle_departement", "'"+addedData.getLibelleDepartement()+"'");
			select_structure = select_structure.replaceAll("#code_service", "'"+addedData.getCodeService()+"'");
			select_structure = select_structure.replaceAll("#libelle_service", "'"+addedData.getLibelleService()+"'");
			select_structure = select_structure.replaceAll("#code_section", "'"+addedData.getCodesection()+"'");
			select_structure = select_structure.replaceAll("#libelle_section", "'"+addedData.getLibelleSection()+"'");
		System.out.println(select_structure);
			
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
	public boolean controleIntegrite(StructureEntrepriseBean addedData)
	{
		try 
		{
			if(addedData.getCodestructure().length()>5)
			{
				Messagebox.show("La taille du champ Code structure ne doit pas dépasser 5 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			else
				if(addedData.getCodeDivision().length()>4)
				{
					Messagebox.show("La taille du champ Code division ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
					return false;
				}
				else
					if(addedData.getLibelleDivision().length()>50)
					{
						Messagebox.show("La taille du champ Libelle division ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
						return false;
					}
					else
						if(addedData.getCodeDirection().length()>4)
						{
							Messagebox.show("La taille du champ Code direction ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
							return false;
						}
						else
							if(addedData.getLibelleDirection().length()>50)
							{
								Messagebox.show("La taille du champ Libelle direction ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
								return false;
							}
							else
								if(addedData.getCodeUnite().length()>4)
								{
									Messagebox.show("LaL taille du champ Code unite ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
									return false;
								}
								else
									if(addedData.getLibelleUnite().length()>50)
									{
										Messagebox.show("La taille du champ Libelle unite ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
										return false;
									}
									else
										if(addedData.getCodeDepartement().length()>4)
										{
											Messagebox.show("La taille du champ Code département ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
											return false;
										}
										else
											if(addedData.getLibelleDepartement().length()>50)
											{
												Messagebox.show("La taille du champ Libelle département ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
												return false;
											}
											else
												if(addedData.getCodeService().length()>4)
												{
													Messagebox.show("La taille du champ Code service ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
													return false;
												}
												else
													if(addedData.getLibelleService().length()>50)
													{
														Messagebox.show("La taille du champ Libelle service ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
														return false;
													}
													else
														if(addedData.getCodesection().length()>4)
														{
															Messagebox.show("La taille du champ Code section ne doit pas dépasser 4 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
															return false;
														}
														else
															if(addedData.getLibelleSection().length()>5)
															{
																Messagebox.show("La taille du champ Libelle section ne doit pas dépasser 50 caractères", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	public Boolean majStructureEntrepriseBean(StructureEntrepriseBean addedData, String selectedCodeStructure)
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
	}
	/**
	 * cette classe permet de supprimer une donnée de la table structure_entreprise
	 * @param codeStructure
	 */
	public void supprimerStructureEntrepriseBean(String codeStructure)
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
	}

}
