package administration.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import administration.bean.CompteBean;
import administration.bean.StructureEntrepriseBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

public class StructureEntrepriseModel {
	
	private ArrayList<StructureEntrepriseBean>  listStructureEntreprise =null; 
	
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
	
	public boolean addStructureEntrepriseBean(StructureEntrepriseBean addedData)
	{
		
		listStructureEntreprise = new ArrayList<StructureEntrepriseBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
//			String select_structure="INSERT INTO structure_entreprise (code_structure,code_division,libelle_division,code_direction, libelle_direction,code_unite,libelle_unite,code_departement,libelle_departement,code_service,libelle_service,code_section,libelle_section) VALUES "+""
//			addedData.getCodestructure()        ()";
//			
//			ResultSet rs = (ResultSet) stmt.executeQuery(select_structure);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//((java.sql.Connection) dbcon).close();
		}
		
		return true;
	}

}
