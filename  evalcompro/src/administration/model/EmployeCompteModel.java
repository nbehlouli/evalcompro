package administration.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.WrongValueException;

import administration.bean.AdministrationLoginBean;
import administration.bean.EmployeCompteBean;
import administration.bean.FichePosteBean;
import administration.bean.StructureEntrepriseBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import common.ApplicationFacade;
import common.Contantes;
import common.CreateDatabaseCon;
import common.PwdCrypt;
import compagne.bean.GestionEmployesBean;


public class EmployeCompteModel {

	private ArrayList<EmployeCompteBean>  listEmployeCompte =null;	
	
	private HashMap<String, AdministrationLoginBean> mapCompte=null;
	
	public List loadListEmployes() throws SQLException{
		
		 mapCompte=checkLoginBean();
		 
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
	 * Cette m�thode permet de charger le contenu de la table Piche_Poste et de cr�er un fichier excel avec ces donn�es
	 */
	public void downloadEmployeCompteDataToXls(HSSFWorkbook workBook,List<EmployeCompteBean> listEmployeCompte)
	{
		//recup�ration du contenu de la table 

			@SuppressWarnings("unchecked")
			
			//creation du fichier xls
			
			Iterator <EmployeCompteBean>index=listEmployeCompte.iterator();
			//creation d'un document excel 
			//HSSFWorkbook workBook = new HSSFWorkbook();
			
			//creation d'une feuille excel
			 HSSFSheet sheet = workBook.createSheet(Contantes.onglet_liste_employes);
			 
			 //creation de l'ent�te du document excel
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
			 try {
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
				 Date dateD;
				
//					dateD = FormaterDate(donnee.getVal_date_deb());
//				
				  if(donnee.getVal_date_deb()!=null)
					  cel.setCellValue(donnee.getVal_date_deb().toString());
				  else
					  cel.setCellValue("");
				 cel = row1.createCell((short)4);
				 
//				 Date dateF=FormaterDate(donnee.getVal_date_fin());
				 if(donnee.getVal_date_fin()!=null)
					 cel.setCellValue(donnee.getVal_date_fin().toString());
				 else
					 cel.setCellValue("");
				 cel = row1.createCell((short)5);
				 
				 //cel.setCellValue(FormaterDate(donnee.getDate_naissance()));
				 cel.setCellValue(donnee.getDate_naissance().toString());
				 cel = row1.createCell((short)6);
				 //cel.setCellValue(FormaterDate(donnee.getDate_recrutement()));
				 cel.setCellValue(donnee.getDate_recrutement().toString());
				 cel = row1.createCell((short)7);
				 
				 cel.setCellValue(donnee.getLibelle_formation());
				 cel = row1.createCell((short)8);
				 cel.setCellValue(donnee.getIntitule_poste());
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
			 } catch (WrongValueException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			//autosize des colonnes
			for (int j=0;j<=12;j++)
		 	{
		 		sheet.autoSizeColumn(j);
		 		
		 	}	

	}
	public List <EmployeCompteBean> uploadXLSXFile(InputStream inputStream)
	{
		
		ArrayList <EmployeCompteBean> listEmployeCompteBean=new ArrayList<EmployeCompteBean>();
		
		 // Creer l'objet representant le fichier Excel
	    try 
	    {
	    	XSSFWorkbook fichierExcel = new XSSFWorkbook(inputStream);
			// creer l'objet representant les lignes Excel
	        XSSFRow ligne;

	        // creer l'objet representant les cellules Excel
	        XSSFCell cellule;

	        //lecture de la premi�re feuille excel
	        XSSFSheet feuilleExcel=fichierExcel.getSheet(Contantes.onglet_liste_employes);
	        
	        // lecture du contenu de la feuille excel
	        int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();
	        
	        for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
	        {
	        	
	        	ligne = feuilleExcel.getRow(numLigne);
	        	if(ligne!=null)
	        	{
	            int nombreColonne = ligne.getLastCellNum()
	                    - ligne.getFirstCellNum();
	            EmployeCompteBean employeCompteBean=new EmployeCompteBean();
	            // parcours des colonnes de la ligne en cours
	            short numColonne=-1;
	            boolean inserer=true;
	            while( (numColonne < nombreColonne)&&(inserer) )
	            {
	            	numColonne++;
	            	inserer=true;
	            	
	            	
	            	cellule = ligne.getCell(numColonne);
	            	if(cellule!=null)
	            	{
	            		
	            	String valeur="";
	            	Date date=null;
	            	if((numColonne==3)||(numColonne==4)||(numColonne==5)||(numColonne==6))
	            	{
	            		date= cellule.getDateCellValue();
//	            		
	            		
	            	}
	            	else
	            		valeur=cellule.getStringCellValue();
	            	
	            	if(numColonne==0)
	            	{
	            		if(valeur==null)
	            			inserer=false;
	            			else
	            				if(valeur.equals("")||valeur.equals(" "))
	            					inserer=false;
	            		employeCompteBean.setNom(valeur);
	            	}
	            	else
	            		if(numColonne==1)
		            	{
	            			employeCompteBean.setPrenom(valeur);
		            	}
		            	else
		            		if(numColonne==2)
			            	{
		            			employeCompteBean.setProfile(valeur);
			            	}
			            	else
			            		if(numColonne==3)
				            	{
			            			employeCompteBean.setVal_date_deb(date);
				            	}
				            	else
				            		if(numColonne==4)
					            	{
				            			employeCompteBean.setVal_date_fin(date);
					            	}
					            	else
					            		if(numColonne==5)
						            	{
					            			
					            			employeCompteBean.setDate_naissance(date);
						            	}
						            	else
						            		if(numColonne==6)
							            	{
						            			employeCompteBean.setDate_recrutement(date);
							            	}
							            	else
							            		if(numColonne==7)
								            	{
							            			String[] val=valeur.split(",");
							            			employeCompteBean.setCode_formation(val[0]);
								            	}
								            	else
								            		if(numColonne==8)
									            	{
								            			String[] val=valeur.split(",");
								            			employeCompteBean.setCode_poste(val[0]);
									            	}
									            	else
									            		if(numColonne==9)
										            	{
									            			employeCompteBean.setEmail(valeur);
										            	}
										            	else
										            		if(numColonne==10)
											            	{
										            			String val[]=valeur.split(",");
										            			employeCompteBean.setEst_evaluateur(val[0]);
											            	}
										            		else
										            			if(numColonne==11)
										            			{
										            					String val[]=valeur.split(",");
										            					employeCompteBean.setCode_est_responsable_rh(val[0]);
										            			}
										            			else
											            			if(numColonne==12)
											            			{
											            					String val[]=valeur.split(",");
											            					employeCompteBean.setCode_structure(val[0]);
											            			}
										            			
	            	
	            	}
	            	else
	            	if(numColonne==0)
            			inserer=false;
	            }//fin for colonne
	            if(inserer)
	            	listEmployeCompteBean.add(employeCompteBean);
	        	}
	        }//fin for ligne

		} 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return listEmployeCompteBean;
	}
	
	public List <EmployeCompteBean> uploadXLSFile(InputStream inputStream)
	{
		
		ArrayList <EmployeCompteBean> listEmployeCompteBean=new ArrayList<EmployeCompteBean>();
		
		 // Creer l'objet representant le fichier Excel
	    try 
	    {
	    	HSSFWorkbook fichierExcel = new HSSFWorkbook(inputStream);
			// creer l'objet representant les lignes Excel
	        HSSFRow ligne;

	        // creer l'objet representant les cellules Excel
	        HSSFCell cellule;

	        //lecture de la premi�re feuille excel
	        HSSFSheet feuilleExcel=fichierExcel.getSheet(Contantes.onglet_liste_employes);
	        
	        // lecture du contenu de la feuille excel
	        int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();
	        
	        for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
	        {
	        	
	        	ligne = feuilleExcel.getRow(numLigne);
	        	if(ligne!=null)
	        	{
	            int nombreColonne = ligne.getLastCellNum()
	                    - ligne.getFirstCellNum();
	            EmployeCompteBean employeCompteBean=new EmployeCompteBean();
	            // parcours des colonnes de la ligne en cours
	            short numColonne=-1;
	            boolean inserer=true;
	            while( (numColonne < nombreColonne)&&(inserer) )
	            {
	            	numColonne++;
	            	inserer=true;
	            	
	            	
	            	cellule = ligne.getCell(numColonne);
	            	if(cellule!=null)
	            	{
	            		
	            	String valeur="";
	            	Date date=null;
	            	if((numColonne==3)||(numColonne==4)||(numColonne==5)||(numColonne==6))
	            	{
	            		date= cellule.getDateCellValue();
//	            		
	            		
	            	}
	            	else
	            		valeur=cellule.getStringCellValue();
	            	
	            	if(numColonne==0)
	            	{
	            		if(valeur==null)
	            			inserer=false;
	            			else
	            				if(valeur.equals("")||valeur.equals(" "))
	            					inserer=false;
	            		employeCompteBean.setNom(valeur);
	            	}
	            	else
	            		if(numColonne==1)
		            	{
	            			employeCompteBean.setPrenom(valeur);
		            	}
		            	else
		            		if(numColonne==2)
			            	{
		            			employeCompteBean.setProfile(valeur);
			            	}
			            	else
			            		if(numColonne==3)
				            	{
			            			employeCompteBean.setVal_date_deb(date);
				            	}
				            	else
				            		if(numColonne==4)
					            	{
				            			employeCompteBean.setVal_date_fin(date);
					            	}
					            	else
					            		if(numColonne==5)
						            	{
					            			
					            			employeCompteBean.setDate_naissance(date);
						            	}
						            	else
						            		if(numColonne==6)
							            	{
						            			employeCompteBean.setDate_recrutement(date);
							            	}
							            	else
							            		if(numColonne==7)
								            	{
							            			String[] val=valeur.split(",");
							            			employeCompteBean.setCode_formation(val[0]);
								            	}
								            	else
								            		if(numColonne==8)
									            	{
								            			String[] val=valeur.split(",");
								            			employeCompteBean.setCode_poste(val[0]);
									            	}
									            	else
									            		if(numColonne==9)
										            	{
									            			employeCompteBean.setEmail(valeur);
										            	}
										            	else
										            		if(numColonne==10)
											            	{
										            			String val[]=valeur.split(",");
										            			employeCompteBean.setEst_evaluateur(val[0]);
											            	}
										            		else
										            			if(numColonne==11)
										            			{
										            					String val[]=valeur.split(",");
										            					employeCompteBean.setCode_est_responsable_rh(val[0]);
										            			}
										            			else
											            			if(numColonne==12)
											            			{
											            					String val[]=valeur.split(",");
											            					employeCompteBean.setCode_structure(val[0]);
											            			}
										            			
	            	
	            	}
	            	else
	            	if(numColonne==0)
            			inserer=false;
	            }//fin for colonne
	            if(inserer)
	            	listEmployeCompteBean.add(employeCompteBean);
	        	}
	        }//fin for ligne

		} 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return listEmployeCompteBean;
	}
	
	private Date FormaterDate(Date datevalue) throws WrongValueException, ParseException {
		if(datevalue!=null)
		{
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			String name = datevalue.toString();
			if (Strings.isBlank(name)) {
				throw new WrongValueException( "la date debut validit� ne doit pas �tre vide!");
			}
			Date datedeb = df.parse(name); 
			return datedeb;
		}
		else return null;
		
	}
	
	
	
	
	public HashMap <String,List<EmployeCompteBean>> ChargementDonneedansBdd(List <EmployeCompteBean> liste)throws Exception
	{
		//Verification de l'integrit� des donn�es � inserer doublon dans le fichier
		List <EmployeCompteBean> listeAInserer=new ArrayList <EmployeCompteBean>();
		List <EmployeCompteBean> listeDonneesRejetes=new ArrayList <EmployeCompteBean>();
		

		for(int i=0; i<liste.size();i++)
		{
			EmployeCompteBean donnee=liste.get(i);
			boolean donneerejete=false;
			for(int j=i+1;j<liste.size();j++)
			{
				EmployeCompteBean donnee2=liste.get(j);
				
				String login0=donnee.getPrenom().charAt(0)+donnee.getNom();
				String log="";
				if(login0.length()>10)
					log=login0.substring(0,9);
				else
					log=login0;
				log=log.replaceAll(" ", "");
				
				
				String login2=donnee2.getPrenom().charAt(0)+donnee2.getNom();
				String log2="";
				if(login2.length()>10)
					log2=login2.substring(0,9);
				else
					log2=login2;
				log2=log2.replaceAll(" ", "");
				
				if(log.equals(log2))
				{
					listeDonneesRejetes.add(donnee);
					donneerejete=true;
					
				}
			}
			if((i==liste.size()-1)||(i==0)||(donneerejete==false))
				listeAInserer.add(donnee);
			
		}
		
		//listeAInserer=liste;
		
	
		System.out.println("listeAInserer" +listeAInserer.size() );
		System.out.println("listeDonneeRejet�es" + listeDonneesRejetes.size());
		//Verification de l'integrit� des donn�es � inserer doublon avec les donn�es de la base
		
		ArrayList <EmployeCompteBean> listeAInsererFinal=new ArrayList <EmployeCompteBean>();
		@SuppressWarnings("unchecked")

		//verification de doublons dans les donn�es a inserer

		
		
		Iterator<EmployeCompteBean> index =listeAInserer.iterator();
		String requete="";
		//verification doubon
		try 
		{
			HashMap<String, AdministrationLoginBean> mapComptes=getComptes(ApplicationFacade.getInstance().getClient_database_id()+"");
			
		
			while(index.hasNext())
			{
				EmployeCompteBean donneeBean=(EmployeCompteBean)index.next();
				//construction de la requete
				Set<String> listCles=mapComptes.keySet();
				Iterator<String> iterator1=listCles.iterator();
				boolean continuer=true;
				while(iterator1.hasNext()&&continuer)
				{
					String cles=iterator1.next();
					AdministrationLoginBean bean=mapComptes.get(cles);
					
/*********
 * 
 */
					String login0=donneeBean.getPrenom().charAt(0)+donneeBean.getNom();
					String log="";
					if(login0.length()>10)
						log=login0.substring(0,9);
					else
						log=login0;
					log=log.replaceAll(" ", "");
					/************/
					//String log=donneeBean.getdonneeBean.getPrenom().charAt(0)+donneeBean.getNom();
					String log2=bean.getLogin();//bean.getPrenom().charAt(0)+bean.getNom();
					
					
					String login;
					String login2;
					if(log.length()>10)
						login=log.substring(0,9);
					else
						login=log; 
					if(log2.length()>10)
						login2=log2.substring(0,9);
					else
						login2=log2; 
					login=login.replaceAll(" ", "");
					login2=login2.replaceAll(" ", "");
					System.out.println("login=="+login+"= login2=="+login2+"=");
					if(login2.equals(login))
					{
						continuer=false;
						System.out.println("doublon login "+ login2 + " nomPrenom" + donneeBean.getPrenom()  +""+ donneeBean.getNom());
					}
				}
				if(continuer)
					{
						listeAInsererFinal.add(donneeBean);
						requete=addCompte(requete,donneeBean);	
					}
				else
					listeDonneesRejetes.add(donneeBean);
			
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//insertion dans la BDD (compte)
		if(requete!=null)
			updateMultiQueryCommon(requete);
		
		//recuperation des idCompte 
		
		HashMap<String, AdministrationLoginBean> mapComptes= getComptes(ApplicationFacade.getInstance().getClient_database_id()+"");
		
		String req=ConstructionRequeteUpdateEmploye(listeAInsererFinal, mapComptes);
		//insertion dans la BDD employe
		if(req!=null) updateMultiQuery(req);
		HashMap <String,List<EmployeCompteBean>> donneeMap=new HashMap<String,List<EmployeCompteBean>>();
		donneeMap.put("inserer", listeAInsererFinal);
		donneeMap.put("supprimer", listeDonneesRejetes);
		return donneeMap;
	}
	
	public HashMap<String, AdministrationLoginBean> getComptes(String database_id) throws SQLException
	{
		HashMap<String, AdministrationLoginBean> mapCompteidCompte=new HashMap<String, AdministrationLoginBean>();
		PwdCrypt pwdcrypt=new PwdCrypt();
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_compte="select distinct id_compte,nom,prenom,c.login,c.pwd,c.id_profile,DATE_FORMAT(val_date_deb,'%Y/%m/%d') as val_date_deb,DATE_FORMAT(val_date_fin,'%Y/%m/%d') as val_date_fin ,modifiedpwd "+ 
                               "from compte c where c.database_id=#database_id ";
                               
			
			sel_compte = sel_compte.replaceAll("#database_id", database_id);
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_compte);
			
			SimpleDateFormat formatDateJour = new SimpleDateFormat("yyyy/MM/dd");
			 
			
			while(rs.next()){
				
					AdministrationLoginBean admin_compte=new AdministrationLoginBean();
					
					admin_compte.setId_compte(Integer.toString(rs.getInt("id_compte")));
					admin_compte.setNom(rs.getString("nom"));
					admin_compte.setPrenom(rs.getString("prenom"));
					admin_compte.setLogin(rs.getString("login"));
					admin_compte.setMotdepasse(pwdcrypt.decrypter(rs.getString("pwd")));
					admin_compte.setProfile(rs.getString("id_profile"));
					
					admin_compte.setDate_deb_val(rs.getDate("val_date_deb"));
					admin_compte.setDate_fin_val(rs.getDate("val_date_fin"));
					admin_compte.setDatemodifpwd(rs.getString("modifiedpwd"));
					
					  
					mapCompteidCompte.put(admin_compte.getNom()+admin_compte.getPrenom(), admin_compte);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			e.printStackTrace();
			
		}
		return mapCompteidCompte;	
		
	}
	/**
	 * 
	 * @param requete
	 * @param donneeBean
	 * @return
	 */
	public String addCompte(String requete,EmployeCompteBean donneeBean)
	{
		
		String login=donneeBean.getPrenom().charAt(0)+donneeBean.getNom();
		String log="";
		if(login.length()>10)
			log=login.substring(0,9);
		else
			log=login;
		log=log.replaceAll(" ", "");
		Integer database_id=ApplicationFacade.getInstance().getClient_database_id(); //recuperation de la base de donn�e actuelle
		//recherche de doublon
		
		String profile[]=donneeBean.getProfile().split(",");
		requete=ConstructionRequeteUpdateCompte(requete,profile[0],log,"12345678",database_id.toString(), donneeBean.getVal_date_deb(), donneeBean.getVal_date_fin(), donneeBean.getNom(), donneeBean.getPrenom());
		return requete;
	}
	
	/**
	 * 
	 * @param requete
	 * @param profile
	 * @param login
	 * @param pwd
	 * @param database_id
	 * @param val_date_deb
	 * @param val_date_fin
	 * @param nom
	 * @param prenom
	 * @return
	 */
	public String ConstructionRequeteUpdateCompte(String requete,String profile,String login,String pwd,String database_id, Date val_date_deb,Date val_date_fin,String nom, String prenom)
	{

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		PwdCrypt pwdcrypt=new PwdCrypt();
		
		String insert_structure="INSERT INTO compte ( id_profile, login, pwd, database_id, val_date_deb, val_date_fin, modifiedpwd, nom, prenom) VALUES (#id_profile,#login,#pwd,#database_id,#val_date_deb,#val_date_fin,#modifiedpwd,#nom,#prenom)";
		insert_structure = insert_structure.replaceAll("#id_profile", profile);
		insert_structure = insert_structure.replaceAll("#login", "'"+login+"'");
		insert_structure = insert_structure.replaceAll("#pwd", "'"+pwdcrypt.crypter(pwd)+"'");
		insert_structure = insert_structure.replaceAll("#database_id", database_id);
		insert_structure = insert_structure.replaceAll("#val_date_deb", "'"+formatter.format(val_date_deb)+"'");
		insert_structure = insert_structure.replaceAll("#val_date_fin", "'"+formatter.format(val_date_fin)+"'");
		insert_structure = insert_structure.replaceAll("#modifiedpwd", "'"+getCurrentDatetime()+"'");
		insert_structure = insert_structure.replaceAll("#nom", "'"+nom+"'");
		insert_structure = insert_structure.replaceAll("#prenom", "'"+prenom+"'");
		requete=requete+ insert_structure+ " ; ";

			return requete;
	
	}
	
	
	public String ConstructionRequeteUpdateEmploye(ArrayList <EmployeCompteBean> listEmployeCompte, HashMap<String, AdministrationLoginBean> mapCompte)
	{

		Iterator<EmployeCompteBean> index =listEmployeCompte.iterator();
		String requete="";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		while(index.hasNext())
		{
			
			EmployeCompteBean donneeBean=(EmployeCompteBean)index.next();
			

			String[]profile=donneeBean.getProfile().split(",");
			String cles=donneeBean.getNom()+donneeBean.getPrenom();
			
			AdministrationLoginBean loginBean=mapCompte.get(cles);
			String id_compte=loginBean.getId_compte();	
			
			String insert_structure="INSERT INTO employe( nom, prenom, date_naissance, rattach_dg, date_recrutement, code_formation, code_poste, email, est_evaluateur, est_responsable_service, est_responsable_direction, est_responsable_division, est_responsable_departement, est_responsable_unite, est_responsable_section, est_responsable_rh, code_structure, id_compte)" +
	         " VALUES(#nom,#prenom,#date_naissance,'N',#date_recrutement,#code_formation,#code_poste,#email,#est_evaluateur,'N','N','N','N','N','N',#est_responsable_rh,#code_structure,#id_compte)";

			insert_structure = insert_structure.replaceAll("#nom", "'"+ donneeBean.getNom()+"'");
			insert_structure = insert_structure.replaceAll("#prenom", "'"+ donneeBean.getPrenom()+"'");
			insert_structure = insert_structure.replaceAll("#date_naissance", "'"+ formatter.format(donneeBean.getDate_naissance())+"'");
			insert_structure = insert_structure.replaceAll("#date_recrutement", "'"+ formatter.format(donneeBean.getDate_recrutement())+"'");
			insert_structure = insert_structure.replaceAll("#code_formation", "'"+ donneeBean.getCode_formation()+"'");
			insert_structure = insert_structure.replaceAll("#code_poste", "'"+ donneeBean.getCode_poste()+"'");
			insert_structure = insert_structure.replaceAll("#email", "'"+ donneeBean.getEmail()+"'");
			insert_structure = insert_structure.replaceAll("#est_evaluateur", "'"+ donneeBean.getEst_evaluateur()+"'");
			insert_structure = insert_structure.replaceAll("#est_responsable_rh", "'"+ donneeBean.getCode_est_responsable_rh()+"'");
			insert_structure = insert_structure.replaceAll("#code_structure", "'"+ donneeBean.getCode_structure()+"'");

			insert_structure = insert_structure.replaceAll("#id_compte", "'"+ id_compte+"'");

			
			requete=requete+ insert_structure+ " ; ";
		}
		


			return requete;
	
	}
	public String getCurrentDatetime(){
		Date today = Calendar.getInstance().getTime();
	    // (2) create our "formatter" (our custom format)
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	    // (3) create a new String in the format we want
	    String todaydate = formatter.format(today);
	    
	    return todaydate;
	}
	
	public void updateMultiQuery(String requete)
	{
		if(requete!="")
		{
			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToEntrepriseDBMulti();
			Statement stmt;
		
			try 
			{
				stmt = (Statement) conn.createStatement();
				System.out.println(requete);
				stmt.execute(requete);
			 
				conn.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();

				// TODO Auto-generated catch block
				try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
				
					e1.printStackTrace();
					//return false;
				}
			
			}
			
		}

	
	}
	
	public void updateMultiQueryCommon(String requete)
	{

		if(requete!="")
		{
			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToDBMulti();
			Statement stmt;
		
			try 
			{
				stmt = (Statement) conn.createStatement();
				System.out.println(requete);
				stmt.execute(requete);
			 
				conn.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();

				// TODO Auto-generated catch block
				try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
				
					e1.printStackTrace();
					//return false;
				}
			
			
			}
		}

	
	}
}
