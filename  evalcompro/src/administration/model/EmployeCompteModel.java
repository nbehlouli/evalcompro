package administration.model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;

import administration.bean.AdministrationLoginBean;
import administration.bean.EmployeCompteBean;
import administration.bean.FichePosteBean;
import administration.bean.StructureEntrepriseBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;
import common.PwdCrypt;
import compagne.bean.GestionEmployesBean;


public class EmployeCompteModel {

	private ArrayList<EmployeCompteBean>  listEmployeCompte =null;	
	
	private HashMap<String, AdministrationLoginBean> mapCompte=null;
	
	public List loadListEmployes() throws SQLException{
		
		 mapCompte=checkLoginBean();
		 System.out.println("apres checklogin");
		listEmployeCompte = new ArrayList<EmployeCompteBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select c.nom,c.prenom, id_employe,e.id_compte,date_naissance,date_recrutement ,concat(e.code_formation,',',d.niv_for_libelle)as libelle_formation,concat (e.code_poste,',',p.intitule_poste) as intitule_poste, email,  CASE WHEN est_evaluateur='N' THEN 'NON' ELSE 'OUI' END as est_evaluateur," +
							" CASE WHEN est_responsable_rh='N' THEN 'NON' ELSE 'OUI' END as est_responsable_rh ,e.code_structure" +
							" from employe e  ,poste_travail_description p,formation f,common_evalcom.compte c  , def_niv_formation d" +
							"  where e.code_poste=p.code_poste and e.code_formation=f.code_formation" +
							"  and   e.id_compte=c.id_compte and   d.niv_for_id=f.niv_for_id order by 1";
					        
					        ResultSet rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				EmployeCompteBean bean=new EmployeCompteBean();
				
				bean.setId_compte((rs.getInt("id_compte")));
				bean.setId_employe((rs.getInt("id_employe")));	
				bean.setNom(rs.getString("nom"));
				bean.setPrenom(rs.getString("prenom"));
				bean.setDate_naissance((rs.getDate("date_naissance")));
				bean.setDate_recrutement((rs.getDate("date_recrutement")));
				bean.setLibelle_formation(rs.getString("libelle_formation"));
				bean.setIntitule_poste(rs.getString("intitule_poste"));
				bean.setEmail(rs.getString("email"));
				bean.setEst_evaluateur(rs.getString("est_evaluateur"));
				bean.setEst_responsable_rh(rs.getString("est_responsable_rh"));
				bean.setCode_structure(rs.getString("code_structure"));
				
				AdministrationLoginBean admLogin=mapCompte.get(rs.getString("id_compte"));
				if(admLogin!=null)
				{
					bean.setProfile(admLogin.getProfile());
					bean.setVal_date_deb(admLogin.getDate_deb_val());
					bean.setVal_date_fin(admLogin.getDate_fin_val());
				}
				
				listEmployeCompte.add(bean);
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			stmt.close();
			conn.close();
			
		}
		return listEmployeCompte;
	
		
		
	}
	
	public HashMap<String,AdministrationLoginBean> checkLoginBean() throws SQLException{
		
		PwdCrypt pwdcrypt=new PwdCrypt();
		mapCompte = new HashMap<String,AdministrationLoginBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_compte="select id_compte,nom,prenom,c.login,c.pwd,libelle_profile,p.id_profile,l.nom_base,DATE_FORMAT(val_date_deb,'%Y/%m/%d') as val_date_deb,DATE_FORMAT(val_date_fin,'%Y/%m/%d') as val_date_fin ,modifiedpwd "+ 
                               "from compte c ,liste_db l ,profile p where c.database_id=l.database_id "+
                               "and c.id_profile=p.id_profile";
			System.out.println(sel_compte);
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_compte);
			
			SimpleDateFormat formatDateJour = new SimpleDateFormat("yyyy/MM/dd");
			 
			
			while(rs.next()){
				
					AdministrationLoginBean admin_compte=new AdministrationLoginBean();
					
					admin_compte.setId_compte(Integer.toString(rs.getInt("id_compte")));
					admin_compte.setNom(rs.getString("nom"));
					admin_compte.setPrenom(rs.getString("prenom"));
					admin_compte.setLogin(rs.getString("login"));
					admin_compte.setMotdepasse(pwdcrypt.decrypter(rs.getString("pwd")));
					admin_compte.setProfile(rs.getString("id_profile")+","+rs.getString("libelle_profile"));
					admin_compte.setBasedonnee(rs.getString("l.nom_base"));
					admin_compte.setDate_deb_val(rs.getDate("val_date_deb"));
					admin_compte.setDate_fin_val(rs.getDate("val_date_fin"));
					admin_compte.setDatemodifpwd(rs.getString("modifiedpwd"));
					
					  System.out.println("dans compte"+rs.getString("id_compte"));
					mapCompte.put(rs.getString("id_compte"), admin_compte);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return mapCompte;
	
		
		
	}
	
	/**
	 * Cette méthode permet de charger le contenu de la table Piche_Poste et de créer un fichier excel avec ces données
	 */
	public void downloadEmployeCompteDataToXls(HSSFWorkbook workBook,List<EmployeCompteBean> listEmployeCompte)
	{
		//recupération du contenu de la table 

			@SuppressWarnings("unchecked")
			
			//creation du fichier xls
			
			Iterator <EmployeCompteBean>index=listEmployeCompte.iterator();
			//creation d'un document excel 
			//HSSFWorkbook workBook = new HSSFWorkbook();
			
			//creation d'une feuille excel
			 HSSFSheet sheet = workBook.createSheet("liste employés");
			 
			 //creation de l'entête du document excel
			 HSSFRow row = sheet.createRow(0);
			 HSSFCell cell = row.createCell((short)0);
			 
			 HSSFCellStyle cellStyle = null;
			 cellStyle = workBook.createCellStyle();
			 
			 HSSFFont font1 = workBook.createFont();
			 font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
			 font1.setFontHeightInPoints((short)8);
			 font1.setFontName("Arial");
			 
		     cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		     cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
		     cellStyle.setFont(font1);
		        
			 cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
			 cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			 cell.setCellValue("Nom");
			 			 cell.setCellStyle(cellStyle);
			 HSSFCell cell1 = row.createCell((short)1);
			 cell1.setCellValue("Prenom");
			 			 cell1.setCellStyle(cellStyle);
			 
			 HSSFCell cell2 = row.createCell((short)2);
			 cell2.setCellValue("Profil");
			 			 cell2.setCellStyle(cellStyle);
			 
			 HSSFCell cell3 = row.createCell((short)3);
			 cell3.setCellValue("val_date_deb");
			 			 cell3.setCellStyle(cellStyle);
			 
			 HSSFCell cell4 = row.createCell((short)4);
			 cell4.setCellValue("val_date_fin");
			 			 cell4.setCellStyle(cellStyle);
			 
			 HSSFCell cell5 = row.createCell((short)5);
			 cell5.setCellValue("date_naissance");
			 			 cell5.setCellStyle(cellStyle);
			 
			 HSSFCell cell6 = row.createCell((short)6);
			 cell6.setCellValue("date_recrutement");
			 			 cell6.setCellStyle(cellStyle);
			 
			 HSSFCell cell7 = row.createCell((short)7);
			 cell7.setCellValue("code_formation");
			 			 cell7.setCellStyle(cellStyle);
			 
			 HSSFCell cell8 = row.createCell((short)8);
			 cell8.setCellValue("code_poste");
			 			 cell8.setCellStyle(cellStyle);
			 
			 HSSFCell cell9 = row.createCell((short)9);
			 cell9.setCellValue("email");
			 			 cell9.setCellStyle(cellStyle);
			 
			 HSSFCell cell10 = row.createCell((short)10);
			 cell10.setCellValue("est_evaluateur");
			 			 cell10.setCellStyle(cellStyle);
			 
			 HSSFCell cell11 = row.createCell((short)11);
			 cell11.setCellValue("est_responsable_rh");
			 			 cell11.setCellStyle(cellStyle);
			 			 
			 HSSFCell cell12 = row.createCell((short)12);
			 cell12.setCellValue("code_structure");
			 cell12.setCellStyle(cellStyle);		 			 
			 
			 
			 
			 int i=1;
			while (index.hasNext())
			{
				
				
				EmployeCompteBean donnee=(EmployeCompteBean)index.next();
				
				 HSSFRow row1 = sheet.createRow(i);
				 HSSFCell cel = row1.createCell((short)0);
				 cel.setCellValue(donnee.getNom());
				 
				 cel = row1.createCell((short)1);
				 cel.setCellValue(donnee.getPrenom());
				 cel = row1.createCell((short)2);
				 cel.setCellValue(donnee.getProfile());
				 cel = row1.createCell((short)3);
				 String dateD=donnee.getVal_date_deb().getYear()+"/"+donnee.getVal_date_deb().getMonth()+"/"+donnee.getVal_date_deb().getDay();
				 cel.setCellValue(dateD);
				 cel = row1.createCell((short)4);
				 
				 String dateF=donnee.getVal_date_fin().getYear()+"/"+donnee.getVal_date_fin().getMonth()+"/"+donnee.getVal_date_fin().getDay();
				 cel.setCellValue(dateF);
				 cel = row1.createCell((short)5);
				 
				 cel.setCellValue(donnee.getDate_naissance().toGMTString());
				 cel = row1.createCell((short)6);
				 cel.setCellValue(donnee.getDate_recrutement().toGMTString());
				 cel = row1.createCell((short)7);
				 
				 cel.setCellValue(donnee.getCode_formation());
				 cel = row1.createCell((short)8);
				 cel.setCellValue(donnee.getCode_poste());
				 cel = row1.createCell((short)9);
				 cel.setCellValue(donnee.getEmail());
				 cel = row1.createCell((short)10);
				 
				 
				 cel.setCellValue(donnee.getEst_evaluateur());
				 cel = row1.createCell((short)11);
				 cel.setCellValue(donnee.getEst_responsable_rh());
				 
				 cel = row1.createCell((short)12);		
				 cel.setCellValue(donnee.getCode_structure());
				 i++;
				
			}

			//autosize des colonnes
			for (int j=0;j<=12;j++)
		 	{
		 		sheet.autoSizeColumn(j);
		 		
		 	}	
			

			
					


	}
}
