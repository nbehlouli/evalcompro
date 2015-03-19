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
import administration.bean.RepCompetenceBean;
import administration.bean.StructureEntrepriseBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import common.CreateDatabaseCon;
import common.PwdCrypt;

public class RepCompetenceModel {
	
private ArrayList<RepCompetenceBean>  listcomp =null; 
	
	
	
public ArrayList<RepCompetenceBean>loadRepCompetence() throws SQLException{
		
		listcomp = new ArrayList<RepCompetenceBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_compte="select  id_repertoire_competence,code_famille,famille,code_groupe,groupe,code_competence,libelle_competence,definition_competence,"+
			                 "aptitude_observable,affichable  from repertoire_competence";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_compte);
								 
			
			while(rs.next()){
				
				RepCompetenceBean rep_comp=new RepCompetenceBean();
					
				    rep_comp.setId_repertoire_competence(rs.getInt("id_repertoire_competence"));
				    rep_comp.setCode_famille(rs.getString("code_famille"));
				    rep_comp.setFamille (rs.getString("famille"));
				    rep_comp.setCode_groupe (rs.getString("code_groupe"));
				    rep_comp.setGroupe(rs.getString("groupe"));
				    rep_comp.setCode_competence(rs.getString("code_competence"));
				    rep_comp.setLibelle_competence(rs.getString("libelle_competence"));
				    rep_comp.setDefinition_competence(rs.getString("definition_competence"));
				    rep_comp.setAptitude_observable(rs.getString("aptitude_observable"));
				    rep_comp.setAffichable (rs.getString("affichable"));
				    			    		
					  
				    listcomp.add(rep_comp);
				   
					
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
 * cette m�thode permet d'inserer la donn�e addedData dans la table structure_entreprise de la base de donn�e
 * @param addedData
 * @return
 * @throws ParseException 
 */
public boolean addRepCompBean(RepCompetenceBean addedData) throws ParseException
{
	

	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt;
	String appobs="";
	String libcomp="";
	String defcomp="";
	String grouplbl="";
	String select_structure="";		
	
	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		 select_structure="INSERT INTO repertoire_competence(code_famille, famille, code_groupe, groupe, code_competence, libelle_competence, definition_competence, aptitude_observable, affichable)"+
		             		  "VALUES(#code_famille,#famille,#code_groupe,#groupe,#code_competence,#libelle_competence,#definition_competence,#aptitude_observable,#affichable)";
		
		select_structure = select_structure.replaceAll("#code_famille", "'"+addedData.getCode_famille()+"'");
		select_structure = select_structure.replaceAll("#famille", "'"+addedData.getFamille()+"'");
		select_structure = select_structure.replaceAll("#code_groupe", "'"+addedData.getCode_groupe()+"'");
		grouplbl=addedData.getGroupe();grouplbl=grouplbl.replaceAll("'"," ");
		select_structure = select_structure.replaceAll("#groupe", "'"+grouplbl+"'");
		select_structure = select_structure.replaceAll("#code_competence", "'"+addedData.getCode_competence()+"'");
		libcomp=addedData.getLibelle_competence();libcomp=libcomp.replaceAll("�"," ");
		select_structure = select_structure.replaceAll("#libelle_competence", "'"+libcomp+"'");
		defcomp=addedData.getDefinition_competence();defcomp=defcomp.replaceAll("�"," ");
		select_structure = select_structure.replaceAll("#definition_competence", "'"+defcomp+"'");
		appobs=addedData.getAptitude_observable();appobs=appobs.replaceAll("�"," ");
		select_structure = select_structure.replaceAll("#aptitude_observable", "'"+addedData.getAptitude_observable()+"'");
		select_structure = select_structure.replaceAll("#affichable", "'"+addedData.getAffichable()+"'");
						
	//System.out.println(select_structure);
		
		 stmt.execute(select_structure);
	} 
	catch (SQLException e) 
	{
		try 
		{
			
			
			
			Messagebox.show("La donn�e n'a pas �t� ins�r�e dans la base de donn�es" +e, "Erreur",Messagebox.OK, Messagebox.ERROR);
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

public Boolean majRepCompBean(RepCompetenceBean addedData)
{
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt;
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String select_structure="UPDATE  repertoire_competence SET code_famille=#code_famille ,famille=#famille,code_groupe=#code_groupe,groupe=#groupe, code_competence=#code_competence" +
				",libelle_competence=#libelle_competence,definition_competence=#definition_competence,aptitude_observable=#aptitude_observable,affichable=#affichable WHERE  id_repertoire_competence=# id_repertoire_competence";
		
		select_structure = select_structure.replaceAll("# id_repertoire_competence", Integer.toString(addedData.getId_repertoire_competence()));
		select_structure = select_structure.replaceAll("#code_famille", "'"+addedData.getCode_famille()+"'");
		select_structure = select_structure.replaceAll("#famille", "'"+addedData.getFamille()+"'");
		select_structure = select_structure.replaceAll("#code_groupe", "'"+addedData.getCode_groupe()+"'");
		select_structure = select_structure.replaceAll("#groupe", "'"+addedData.getGroupe()+"'");
		select_structure = select_structure.replaceAll("#code_competence", "'"+addedData.getCode_competence()+"'");
		select_structure = select_structure.replaceAll("#libelle_competence", "'"+addedData.getLibelle_competence()+"'");
		select_structure = select_structure.replaceAll("#definition_competence", "'"+addedData.getDefinition_competence()+"'");
		select_structure = select_structure.replaceAll("#aptitude_observable", "'"+addedData.getAptitude_observable()+"'");
		select_structure = select_structure.replaceAll("#affichable", "'"+addedData.getAffichable()+"'");
		
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
public void supprimerComp(RepCompetenceBean addedData)
{
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt;
	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sup_login="DELETE FROM  repertoire_competence  WHERE id_repertoire_competence=#id_repertoire_competence"; 
		sup_login = sup_login.replaceAll("#id_repertoire_competence", Integer.toString(addedData.getId_repertoire_competence()));
		
	
		
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
 * Cette m�thode permet de faire un chargement d'un fichier xlsx 
 * @return
 */
/**
 * Cette m�thode permet de faire un upload d'un fichier xlsx (fichier vers BDD)
 * @return
 */
public List <RepCompetenceBean> uploadXLSXFile(InputStream inputStream)
{
	listcomp=new ArrayList <RepCompetenceBean>();
	
	 // Creer l'objet representant le fichier Excel
    try 
    {
		XSSFWorkbook fichierExcel = new XSSFWorkbook(inputStream);
		// creer l'objet representant les lignes Excel
        XSSFRow ligne;

        // creer l'objet representant les cellules Excel
        XSSFCell cellule;

        //lecture de la premi�re feuille excel
        XSSFSheet feuilleExcel=fichierExcel.getSheetAt(0);
        
        // lecture du contenu de la feuille excel
        int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();
        
        for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
        {
        	
        	ligne = feuilleExcel.getRow(numLigne);
            int nombreColonne = ligne.getLastCellNum()
                    - ligne.getFirstCellNum();
            RepCompetenceBean structurentreprise=new RepCompetenceBean();
            // parcours des colonnes de la ligne en cours
            for (short numColonne = 0; numColonne < nombreColonne; numColonne++) 
            {
            	try
            	{
            	
            	cellule = ligne.getCell(numColonne);
            	
            	String valeur= cellule.getStringCellValue();
            	
            	
            	if(numColonne==0)
            	{
            		structurentreprise.setCode_famille(valeur);
            	}
            	else
            		if(numColonne==1)
	            	{
            			structurentreprise.setFamille(valeur);
	            	}
	            	else
	            		if(numColonne==2)
		            	{
	            			structurentreprise.setCode_groupe(valeur);
		            	}
		            	else
		            		if(numColonne==3)
			            	{
		            			structurentreprise.setGroupe(valeur);
			            	}
			            	else
			            		if(numColonne==4)
				            	{
			            			structurentreprise.setCode_competence(valeur);
				            	}
				            	else
				            		if(numColonne==5)
					            	{
				            			structurentreprise.setLibelle_competence(valeur);
					            	}
					            	else
					            		if(numColonne==6)
						            	{
					            			structurentreprise.setDefinition_competence(valeur);
						            	}
						            	else
						            		if(numColonne==7)
							            	{
						            			structurentreprise.setAptitude_observable(valeur);
							            	}
							            	else
							            		if(numColonne==8)
								            	{
							            			structurentreprise.setAffichable(valeur);
								            	}
								            	
            	}catch(Exception e)
            	{
            		
            	}

            }//fin for colonne
            listcomp.add(structurentreprise);
        }//fin for ligne

	} 
    catch (IOException e) 
    {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
    
	return listcomp;
}
/**
 * @throws IOException 
 * @throws InvalidFormatException 
 * @throws FileNotFoundException 
 * @throws BiffException 
 * Cette m�thode permet de faire un upload d'un fichier xls (fichier vers BDD)
 * @return
 * @throws  
 */


public List <RepCompetenceBean> uploadXLSFile(InputStream inputStream)
{
	listcomp=new ArrayList <RepCompetenceBean>();
	
	 // Creer l'objet representant le fichier Excel
    try 
    {
    	HSSFWorkbook fichierExcel = new HSSFWorkbook(inputStream);
		// creer l'objet representant les lignes Excel
        HSSFRow ligne;

        // creer l'objet representant les cellules Excel
        HSSFCell cellule;

        //lecture de la premi�re feuille excel
        HSSFSheet feuilleExcel=fichierExcel.getSheetAt(0);
        
        // lecture du contenu de la feuille excel
        int nombreLigne = feuilleExcel.getLastRowNum()- feuilleExcel.getFirstRowNum();
        
        for(int numLigne =1;numLigne<=nombreLigne; numLigne++)
        {
        	//System.out.println("de nouveau dans la boucle");
        	ligne = feuilleExcel.getRow(numLigne);
            int nombreColonne = ligne.getLastCellNum()
                    - ligne.getFirstCellNum();
            RepCompetenceBean structurentreprise=new RepCompetenceBean();
            // parcours des colonnes de la ligne en cours
            for (short numColonne = 0; numColonne < nombreColonne; numColonne++) 
            {
            	try
            	{
            	//System.out.println("de nouveau dan scolonne");
            	cellule = ligne.getCell(numColonne);
            	
            	String valeur= cellule.getStringCellValue();
            	//System.out.println(" numligne = "+numLigne+" numColonne= "+numColonne +" valeur ="+valeur);
            	
            	if(numColonne==0)
            	{
            		structurentreprise.setCode_famille(valeur);
            	}
            	else
            		if(numColonne==1)
	            	{
            			structurentreprise.setFamille(valeur);
	            	}
	            	else
	            		if(numColonne==2)
		            	{
	            			structurentreprise.setCode_groupe(valeur);
		            	}
		            	else
		            		if(numColonne==3)
			            	{
		            			structurentreprise.setGroupe(valeur);
			            	}
			            	else
			            		if(numColonne==4)
				            	{
			            			structurentreprise.setCode_competence(valeur);
				            	}
				            	else
				            		if(numColonne==5)
					            	{
				            			structurentreprise.setLibelle_competence(valeur);
					            	}
					            	else
					            		if(numColonne==6)
						            	{
					            			structurentreprise.setDefinition_competence(valeur);
						            	}
						            	else
						            		if(numColonne==7)
							            	{
						            			structurentreprise.setAptitude_observable(valeur);
							            	}
							            	else
							            		if(numColonne==8)
								            	{
							            			structurentreprise.setAffichable(valeur);
								            	}
								            	
            	}catch(Exception e)
            	{
            		
            	}

            }//fin for colonne
            listcomp.add(structurentreprise);
        }//fin for ligne

	} 
    catch (IOException e) 
    {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
    
	return listcomp;
}

public List <RepCompetenceBean> uploadXLSFileS(String inputFile) throws BiffException, InvalidFormatException, IOException  
{
	listcomp=new ArrayList <RepCompetenceBean>();
	Workbook w;
	
	InputStream inp = new FileInputStream(inputFile);
     w = (Workbook) WorkbookFactory.create(inp);
   
	
	//File inputWorkbook = new File(inputFile);
	//RepCompetenceBean structurentreprise=new RepCompetenceBean();
	
	//w = Workbook.getWorkbook(inputWorkbook);
	// Get the first sheet
	Sheet sheet = w.getSheet(0);
	// Loop over first 10 column and lines
	for (int i = 1; i < sheet.getRows(); i++) {
		RepCompetenceBean structurentreprise=new RepCompetenceBean();
	     for (int j = 0; j < sheet.getColumns(); j++) {
	    	 Cell cell = sheet.getCell(j, i);
	    	 
	    	 if(j==0)
	        	{
	        		structurentreprise.setCode_famille(cell.getContents());
	        	}
	        	else
	        		if(j==1)
	            	{
	        			structurentreprise.setFamille(cell.getContents());
	            	}
	            	else
	            		if(j==2)
		            	{
	            			structurentreprise.setCode_groupe(cell.getContents());
		            	}
		            	else
		            		if(j==3)
			            	{
		            			structurentreprise.setGroupe(cell.getContents());
			            	}
			            	else
			            		if(j==4)
				            	{
			            			structurentreprise.setCode_competence(cell.getContents());
				            	}
				            	else
				            		if(j==5)
					            	{
				            			structurentreprise.setLibelle_competence(cell.getContents());
					            	}
					            	else
					            		if(j==6)
						            	{
					            			structurentreprise.setDefinition_competence(cell.getContents());
						            	}
						            	else
						            		if(j==7)
							            	{
						            			structurentreprise.setAptitude_observable(cell.getContents());
							            	}
							            	else
							            		if(j==8)
								            	{
							            			structurentreprise.setAffichable(cell.getContents());
								            	}
	    	
	     }
	     listcomp.add(structurentreprise);     
	}
	        		
         return listcomp;
}


/**
 * Cette m�thode permet de charger le contenu de la table Structure_entreprise et de cr�er un fichier excel avec ces donn�es
 */
public void downloadStructureEntrepriseDataToXls()
{
	
	
	//recup�ration du contenu de la table Structure_entreprise
	try 
	{
		ArrayList<RepCompetenceBean> listerepBean=(ArrayList<RepCompetenceBean>) loadRepCompetence();
		
		//creation du fichier xls
		
		Iterator <RepCompetenceBean>index=listerepBean.iterator();
		//creation d'un document excel 
		HSSFWorkbook workBook = new HSSFWorkbook();
		
		//creation d'une feuille excel
		 HSSFSheet sheet = workBook.createSheet("R�pertoire comp�tences");
		 
		 //creation de l'ent�te du document excel
		 HSSFRow row = sheet.createRow(0);
		 HSSFCell cell = row.createCell((short)0);
		 

		 cell.setCellValue("code famille");
		 //			 cell.setCellStyle(cellStyle);
		 HSSFCell cell1 = row.createCell((short)1);
		 cell1.setCellValue("Famille");
		 //			 cell1.setCellStyle(cellStyle);
		 
		 HSSFCell cell2 = row.createCell((short)2);
		 cell2.setCellValue("code groupe");
		 //			 cell2.setCellStyle(cellStyle);
		 
		 HSSFCell cell3 = row.createCell((short)3);
		 cell3.setCellValue("Groupe");
		 //			 cell3.setCellStyle(cellStyle);
		 
		 HSSFCell cell4 = row.createCell((short)4);
		 cell4.setCellValue("Code comp�tence");
		 //			 cell4.setCellStyle(cellStyle);
		 
		 HSSFCell cell5 = row.createCell((short)5);
		 cell5.setCellValue("Libell� comp�tence");
		 //			 cell5.setCellStyle(cellStyle);
		 
		 HSSFCell cell6 = row.createCell((short)6);
		 cell6.setCellValue("D�finition comp�tence");
		 //			 cell6.setCellStyle(cellStyle);
		 
		 HSSFCell cell7 = row.createCell((short)7);
		 cell7.setCellValue("Aptitude observable");
		 //			 cell7.setCellStyle(cellStyle);
		 
		 HSSFCell cell8 = row.createCell((short)8);
		 cell8.setCellValue("Affichable");
		 //			 cell8.setCellStyle(cellStyle);
		 
		 
		 
		 int i=1;
		while (index.hasNext())
		{
			
		RepCompetenceBean donnee=(RepCompetenceBean)index.next();
			
			 HSSFRow row1 = sheet.createRow(i);
			 HSSFCell cel = row1.createCell((short)0);
			  
			 cel = row1.createCell((short)0);
			 cel.setCellValue(donnee.getCode_famille());
			 cel = row1.createCell((short)1);
			 cel.setCellValue(donnee.getFamille());
			 cel = row1.createCell((short)2);
			 cel.setCellValue(donnee.getCode_groupe());
			 cel = row1.createCell((short)3);
			 cel.setCellValue(donnee.getGroupe());
			 cel = row1.createCell((short)4);
			 cel.setCellValue(donnee.getCode_competence());
			 cel = row1.createCell((short)5);
			 cel.setCellValue(donnee.getLibelle_competence());
			 cel = row1.createCell((short)6);
			 cel.setCellValue(donnee.getDefinition_competence());
			 cel = row1.createCell((short)7);
			 cel.setCellValue(donnee.getAptitude_observable());
			 cel = row1.createCell((short)8);
			 cel.setCellValue(donnee.getAffichable());
			 
			 i++;
		}
			

		FileOutputStream fOut;
		try 
		{
			fOut = new FileOutputStream("repertoire_competence.xls");
			workBook.write(fOut);
			fOut.flush();
			fOut.close();
			
			File file = new File("repertoire_competence.xls");
			Filedownload.save(file, "XLS");
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
	}
	catch (SQLException e) 
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

/**
 * cette m�thode permet de v�rifier l'integrite des donn�es et retourne les donn�es rejet�s
 * @return
 */
public HashMap <String,List<RepCompetenceBean>> ChargementDonneedansBdd(List <RepCompetenceBean> liste)throws Exception
{
	//Verification de l'integrit� des donn�es � inserer doublon dans le fichier
	List <RepCompetenceBean> listeAInserer=new ArrayList <RepCompetenceBean>();
	List <RepCompetenceBean> listeDonneesRejetes=new ArrayList <RepCompetenceBean>();

	for(int i=0; i<liste.size();i++)
	{
		RepCompetenceBean donnee=liste.get(i);
		boolean donneerejete=false;
		for(int j=i+1;j<liste.size();j++)
		{
			RepCompetenceBean donnee2=liste.get(j);
			if(donnee.getCode_famille().equals(donnee2.getCode_famille())&& donnee.getCode_groupe().equals(donnee2.getCode_groupe())&& donnee.getCode_competence().equals(donnee2.getCode_competence())
					&& donnee.getAptitude_observable().equalsIgnoreCase(donnee2.getAptitude_observable()))
			{
				listeDonneesRejetes.add(donnee);
				donneerejete=true;
				//System.out.println("donnee rejete "+donnee.getCodestructure());
			
			}
		}
		if((i==liste.size()-1)||(i==0)||(donneerejete==false))
			listeAInserer.add(donnee);
		//System.out.println("donne a inserer "+donnee.getCodestructure());
		
		
		
	}
	//List <RepCompetenceBean> listeAInsererFinal=new ArrayList <RepCompetenceBean>();
	ArrayList<RepCompetenceBean>strctureEntreprisebdd =loadRepCompetence();
	Iterator <RepCompetenceBean>iterator=listeAInserer.iterator();
	
	/*//Verification de l'integrit� des donn�es � inserer doublon avec les donn�es de la base
	
	List <RepCompetenceBean> listeAInsererFinal=new ArrayList <RepCompetenceBean>();
	ArrayList<RepCompetenceBean>strctureEntreprisebdd =loadRepCompetence();
	Iterator <RepCompetenceBean>iterator=listeAInserer.iterator();
	
	while(iterator.hasNext())
	{
		//System.out.println("1");
		
		RepCompetenceBean bean=(RepCompetenceBean)iterator.next();
		
		Iterator<RepCompetenceBean> index=strctureEntreprisebdd.iterator();
		boolean donneerejete=false;
		while(index.hasNext())
		{
			//System.out.println("2");
			
			RepCompetenceBean bean2=(RepCompetenceBean)index.next();
			if(bean.getFamille().equals(bean2.getFamille()))
			{
				//System.out.println("3");
				
				listeDonneesRejetes.add(bean);
				donneerejete=true;
				continue;
			}
		}
		if(!donneerejete)
		{
			//System.out.println("ajout donnee"+bean.getCodestructure() );
			
			listeAInsererFinal.add(bean);
		}
		//System.out.println("4");
		
	}*/
	
	//Insertion des donn�es dans la table Structure_entreprise
	Iterator<RepCompetenceBean> index =listeAInserer.iterator();
	while(index.hasNext())
	{
		RepCompetenceBean donneeBean=(RepCompetenceBean)index.next();
		
		addRepCompBean(donneeBean);			
	}
	
		
		HashMap <String,List<RepCompetenceBean>> donneeMap=new HashMap<String,List<RepCompetenceBean>>();
		donneeMap.put("inserer", listeAInserer);
		donneeMap.put("supprimer", listeDonneesRejetes);
	//System.out.println("taille liste= "+ liste.size());
	
	return donneeMap;
}

}
