package administration.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Messagebox;

import administration.bean.CompteBean;
import administration.bean.StructureEntrepriseBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

public class StructureEntrepriseModel {
	
	private ArrayList<StructureEntrepriseBean>  listStructureEntreprise =null; 
	
	/**
	 * cette méthode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<StructureEntrepriseBean> checkStructureEntreprise() throws SQLException{
		
		listStructureEntreprise = new ArrayList<StructureEntrepriseBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try {
			stmt = (Statement) conn.createStatement();
			String select_structure="SELECT * FROM structure_entreprise";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
			
			
			while(rs.next()){
				if (rs.getRow()>=1) {
					StructureEntrepriseBean structureEntreprise=new StructureEntrepriseBean();
					structureEntreprise.setCodestructure(rs.getString("code_structure"));
					structureEntreprise.setCodeDivision(rs.getString("code_division"));
					structureEntreprise.setLibelleDivision(rs.getString("libelle_division"));
					structureEntreprise.setCodeDirection(rs.getString("code_direction"));
					structureEntreprise.setLibelleDirection(rs.getString("libelle_direction"));
					structureEntreprise.setCodeUnite(rs.getString("code_unite"));
					structureEntreprise.setLibelleUnite(rs.getString("libelle_unite"));
					structureEntreprise.setCodeDepartement(rs.getString("code_departement"));
					structureEntreprise.setLibelleDepartement(rs.getString("libelle_departement"));
					structureEntreprise.setCodeService(rs.getString("code_service"));
					structureEntreprise.setLibelleService(rs.getString("libelle_service"));
					structureEntreprise.setCodesection(rs.getString("code_section"));
					structureEntreprise.setLibelleSection(rs.getString("libelle_section"));
					  
					listStructureEntreprise.add(structureEntreprise);
				   
					
				}else {
					return listStructureEntreprise;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			((java.sql.Connection) dbcon).close();
		}
		
			
		return listStructureEntreprise;	
	
		
		
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
				Messagebox.show("La donnée n'a pas été insérée dans la base car il existe une donnée ayant le même code établissement", "Ereur",Messagebox.OK, Messagebox.ERROR);
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
	
	public boolean controleIntegrite(StructureEntrepriseBean addedData)
	{
		try 
		{
			if(addedData.getCodestructure().length()>5)
			{
				Messagebox.show("La taille du champ Code structure ne doit pas dépasser 5 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			else
				if(addedData.getCodeDivision().length()>4)
				{
					Messagebox.show("La taille du champ Code division ne doit pas dépasser 4 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
					return false;
				}
				else
					if(addedData.getLibelleDivision().length()>50)
					{
						Messagebox.show("La taille du champ Libelle division ne doit pas dépasser 50 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
						return false;
					}
					else
						if(addedData.getCodeDirection().length()>4)
						{
							Messagebox.show("La taille du champ Code direction ne doit pas dépasser 4 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
							return false;
						}
						else
							if(addedData.getLibelleDirection().length()>50)
							{
								Messagebox.show("La taille du champ Libelle direction ne doit pas dépasser 50 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
								return false;
							}
							else
								if(addedData.getCodeUnite().length()>4)
								{
									Messagebox.show("LaL taille du champ Code unite ne doit pas dépasser 4 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
									return false;
								}
								else
									if(addedData.getLibelleUnite().length()>50)
									{
										Messagebox.show("La taille du champ Libelle unite ne doit pas dépasser 50 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
										return false;
									}
									else
										if(addedData.getCodeDepartement().length()>4)
										{
											Messagebox.show("La taille du champ Code département ne doit pas dépasser 4 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
											return false;
										}
										else
											if(addedData.getLibelleDepartement().length()>50)
											{
												Messagebox.show("La taille du champ Libelle département ne doit pas dépasser 50 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
												return false;
											}
											else
												if(addedData.getCodeService().length()>4)
												{
													Messagebox.show("La taille du champ Code service ne doit pas dépasser 4 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
													return false;
												}
												else
													if(addedData.getLibelleService().length()>50)
													{
														Messagebox.show("La taille du champ Libelle service ne doit pas dépasser 50 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
														return false;
													}
													else
														if(addedData.getCodesection().length()>4)
														{
															Messagebox.show("La taille du champ Code section ne doit pas dépasser 4 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
															return false;
														}
														else
															if(addedData.getLibelleSection().length()>5)
															{
																Messagebox.show("La taille du champ Libelle section ne doit pas dépasser 50 caractères", "Ereur",Messagebox.OK, Messagebox.ERROR);
																return false;
															}
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
			
		return true;
	}
}
