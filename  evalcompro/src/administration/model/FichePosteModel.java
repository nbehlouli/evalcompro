package administration.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import administration.bean.AdministrationLoginBean;
import administration.bean.FichePosteBean;
import administration.bean.RepCompetenceBean;
import administration.bean.StructureEntrepriseBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;
import common.PwdCrypt;

public class FichePosteModel {
	
private ArrayList<FichePosteBean>  listcomp =null; 
	
	
	
public List loadFichesPostes() throws SQLException{
		
		listcomp = new ArrayList<FichePosteBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sql_query=" select  p.code_poste ,p.intitule_poste,trim(p.sommaire_poste) as sommaire_poste ,trim(p.tache_responsabilite) as tache_responsabilite ,trim(p.environement_perspectif) as environement_perspectif ,concat(d.niv_for_libelle,'-',f.libelle_diplome)  as formation_general," +
					         " p.formation_professionnelle ,p.experience,trim(p.profile_poste) as profile_poste,t.intitule_poste as libelle_poste,p.code_structure,p.date_maj_poste, g.gsp_libelle as is_cadre" +
					         " from poste_travail_description p,structure_entreprise s ,poste_travail_description t, formation f,def_niv_formation d,def_gsp g" +
					         " where p.code_structure=s.code_structure  and t.code_poste=p.code_poste_hierarchie  and p.code_formation=f.code_formation" +
					         " and f.niv_for_id=d.niv_for_id and p.gsp_id=g.gsp_id" +
					         " Union" +
					         " select p.code_poste ,p.intitule_poste,trim(p.sommaire_poste) as sommaire_poste ,trim(p.tache_responsabilite) as tache_responsabilite ,trim(p.environement_perspectif) as environement_perspectif ,concat(d.niv_for_libelle,'-',f.libelle_diplome) as formation_general," +
					         " p.formation_professionnelle ,p.experience,trim(p.profile_poste) as profile_poste,code_poste_hierarchie as libelle_poste,p.code_structure,p.date_maj_poste,g.gsp_libelle as is_cadre" +
					         " from poste_travail_description p,structure_entreprise s , formation f,def_niv_formation d,def_gsp g" +
					         " where code_poste_hierarchie ='' and p.code_structure=s.code_structure and f.niv_for_id=d.niv_for_id" +
					         " and p.code_formation=f.code_formation and p.gsp_id=g.gsp_id order by 1";
;
			//System.out.println(sql_query);
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
						
			while(rs.next()){
				
				FichePosteBean bean =new FichePosteBean();
				
				bean.setCode_poste(rs.getString("code_poste"));
				bean.setIntitule_poste(rs.getString("intitule_poste"));
				bean.setSommaire_poste(rs.getString("sommaire_poste"));
				bean.setTache_responsabilite(rs.getString("tache_responsabilite"));
				bean.setEnvironement_perspectif(rs.getString("environement_perspectif"));
				bean.setFormation_general(rs.getString("formation_general"));
				bean.setFormation_professionnelle(rs.getString("formation_professionnelle"));
				bean.setProfile_poste(rs.getString("profile_poste"));
				bean.setLibelle_poste(rs.getString("libelle_poste"));
				bean.setCode_structure(rs.getString("code_structure"));
				bean.setDate_maj_poste(rs.getDate("date_maj_poste"));
				bean.setIs_cadre(rs.getString("is_cadre"));
				
				    listcomp.add(bean);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listcomp;
	
		
		
	}


/**
 * cette méthode permet d'inserer la donnée addedData dans la table structure_entreprise de la base de donnée
 * @param addedData
 * @return
 * @throws ParseException 
 */
public boolean addPosteTravail(FichePosteBean addedData) throws ParseException
{
	

	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		String select_structure="INSERT INTO poste_travail_description(code_poste, intitule_poste, sommaire_poste, tache_responsabilite, environement_perspectif, code_formation, formation_professionnelle, experience, profile_poste, code_poste_hierarchie, code_structure, date_maj_poste,gsp_id)" +
	 		          " VALUES(#code_poste,#intitule_poste,#sommaire_poste,#tache_responsabilite,#environement_perspectif,#code_formation,#formation_professionnelle,#experience,#profile_poste,#hierarchie,#code_structure,#date_maj_poste,#gsp_id)";
	 

		
		select_structure = select_structure.replaceAll("#code_poste", "'"+addedData.getCode_poste()+"'");
		select_structure = select_structure.replaceAll("#intitule_poste", "'"+addedData.getIntitule_poste()+"'");
		select_structure = select_structure.replaceAll("#sommaire_poste", "'"+removeString(addedData.getSommaire_poste())+"'");
		select_structure = select_structure.replaceAll("#tache_responsabilite", "'"+removeString(addedData.getTache_responsabilite())+"'");
	    select_structure = select_structure.replaceAll("#environement_perspectif", "'"+removeString(addedData.getEnvironement_perspectif())+"'");
	    select_structure = select_structure.replaceAll("#code_formation", "'"+removeString(addedData.getLibelle_formation())+"'");
        select_structure = select_structure.replaceAll("#formation_professionnelle", "'"+removeString(addedData.getFormation_professionnelle())+"'");
		select_structure = select_structure.replaceAll("#experience", "'"+removeString(addedData.getExperience())+"'");
		select_structure = select_structure.replaceAll("#profile_poste", "'"+addedData.getProfile_poste()+"'");
		select_structure = select_structure.replaceAll("#hierarchie", "'"+addedData.getPoste_hierarchie()+"'");
		select_structure = select_structure.replaceAll("#code_structure", "'"+addedData.getCode_structure()+"'");
		select_structure = select_structure.replaceAll("#date_maj_poste", "'"+formatter.format(addedData.getDate_maj_poste())+"'");
		select_structure = select_structure.replaceAll("#gsp_id","'"+addedData.getCode_gsp()+"'");
		
		
						
	//System.out.println(select_structure);
		
		 stmt.execute(select_structure);
	} 
	catch (SQLException e) 
	{
		try 
		{
			
			Messagebox.show("La donnée n'a pas été insérée dans la base de données" +e, "Erreur",Messagebox.OK, Messagebox.ERROR);
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

public Boolean majPosteTravail(FichePosteBean addedData)
{
	
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt;
	
	try 
	{
		
		
		stmt = (Statement) conn.createStatement();
		String select_structure="UPDATE poste_travail_description set intitule_poste=#intitule_poste, sommaire_poste=#sommaire_poste, tache_responsabilite=#tache_responsabilite, environement_perspectif=#environement_perspectif, code_formation=#code_formation, formation_professionnelle=#formation_professionnelle, experience=#experience, profile_poste=#profile_poste" +
        "  , code_poste_hierarchie=#hierarchie, code_structure=#code_structure, date_maj_poste=#date_maj_poste,gsp_id=#gsp_id where code_poste=#code_poste";

		select_structure = select_structure.replaceAll("#code_poste", "'"+addedData.getCode_poste()+"'");
		select_structure = select_structure.replaceAll("#intitule_poste", "'"+addedData.getIntitule_poste()+"'");
		select_structure = select_structure.replaceAll("#sommaire_poste", "'"+removeString(addedData.getSommaire_poste())+"'");
		select_structure = select_structure.replaceAll("#tache_responsabilite", "'"+removeString(addedData.getTache_responsabilite())+"'");
	    select_structure = select_structure.replaceAll("#environement_perspectif", "'"+removeString(addedData.getEnvironement_perspectif())+"'");
	    select_structure = select_structure.replaceAll("#code_formation", "'"+removeString(addedData.getLibelle_formation())+"'");
        select_structure = select_structure.replaceAll("#formation_professionnelle", "'"+removeString(addedData.getFormation_professionnelle())+"'");
		select_structure = select_structure.replaceAll("#experience", "'"+removeString(addedData.getExperience())+"'");
		select_structure = select_structure.replaceAll("#profile_poste", "'"+addedData.getProfile_poste()+"'");
		select_structure = select_structure.replaceAll("#hierarchie", "'"+addedData.getPoste_hierarchie()+"'");
		select_structure = select_structure.replaceAll("#code_structure", "'"+addedData.getCode_structure()+"'");
		select_structure = select_structure.replaceAll("#date_maj_poste", "'"+formatter.format(addedData.getDate_maj_poste())+"'");
		select_structure = select_structure.replaceAll("#gsp_id","'"+addedData.getCode_gsp()+"'");

	   //System.out.println(update_structure);
		
		 stmt.executeUpdate(select_structure);
	} 
	catch (SQLException e) 
	{
		try 
		{
			Messagebox.show("La modification n'a pas été prise en compte "+e, "Erreur",Messagebox.OK, Messagebox.ERROR);
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
 * @throws InterruptedException 
 */
public void supprimerFichePoste(FichePosteBean addedData) throws InterruptedException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt;
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sql_query="DELETE FROM  poste_travail_description  where code_poste=#code_poste";

		sql_query = sql_query.replaceAll("#code_poste", "'"+addedData.getCode_poste()+"'");
	
		
		 stmt.executeUpdate(sql_query);
	} 
	catch (SQLException e) 
	{
		
			//e.printStackTrace();
		Messagebox.show("La suppression de l'enregistrement a échoué "+e, "Erreur",Messagebox.OK, Messagebox.ERROR);

			//return false;


	}
	try {
		conn.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


public HashMap getListFormation() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;
	HashMap map = new HashMap();
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String profile_list="select   f.code_formation ,concat(d.niv_for_libelle,'-',f.libelle_diplome)" +
				            " as libelle_formation  from formation f, def_niv_formation d" +
				            " where f.niv_for_id=d.niv_for_id"; 
		ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
		
		
		while(rs.next()){
			map.put(rs.getString("libelle_formation"), rs.getString("code_formation"));
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

public HashMap getListPostes() throws SQLException
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

public String getMaxKeyCode() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;
	String result ="";
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String profile_list="select max(code_poste) as max_code from poste_travail_description"; 
		ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
		
		
		while(rs.next()){
			result=rs.getString("max_code");
			//list_profile.add(rs.getString("libelle_profile"));
        }
		stmt.close();conn.close();
	} 
	catch (SQLException e){
			e.printStackTrace();
			stmt.close();conn.close();
	}
	
	return result;
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

public String removeString(String chaine){
	
	String chaine_trt=chaine;
           chaine_trt=chaine_trt.replaceAll("’"," ");
	return chaine_trt;
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

public static String getNextCode(String charto,String code) {

	String nextvalue=
	"XXXX";

	if (code==null || code.length()==0){

	nextvalue=charto+
	"001";

	return nextvalue;

	}

	String[]list=code.split(charto);

	String chaine=String.valueOf(Integer.parseInt(list[1])+1);

	if (chaine.length() <4){

	if (chaine.length()==1){

	chaine=charto+"00"+chaine;

	}

	if (chaine.length()==2){

	chaine=charto+"0"+chaine;

	}

	if (chaine.length()==3){

	chaine=charto+chaine;

	}

	nextvalue=chaine;

	}

	return nextvalue;

	}

public HashMap isCadre() throws SQLException
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;
	HashMap map = new HashMap();

	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sql_query="select gsp_id,gsp_libelle from def_gsp";
		ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
		
		
		while(rs.next()){
			map.put( rs.getString("gsp_libelle"),rs.getString("gsp_id"));
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
