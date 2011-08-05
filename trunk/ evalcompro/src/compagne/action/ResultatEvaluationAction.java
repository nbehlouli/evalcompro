package compagne.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Filedownload;

import administration.bean.StructureEntrepriseBean;

import compagne.model.ResultatEvaluationModel;

public class ResultatEvaluationAction extends GenericForwardComposer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String selected_id_compagne="1";
	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
		
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		
		//récupération de la liste des compagnes
	}
	
	
	
	
	@SuppressWarnings("deprecation")
	public void onClick$exporter()
	 {
		//récupération des informations d'entête du fichier excel
		ResultatEvaluationModel resultatEvaluationModel=new ResultatEvaluationModel();
		
		HashMap <String, HashMap<String, ArrayList<String>>> mapPosteFamilleCompetence=resultatEvaluationModel.getInfosFamillesCompetence(selected_id_compagne);
		
		HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >> mapPosteEmployeFamilleCompetence=resultatEvaluationModel.getAllIMICompetence(selected_id_compagne);
		
		HashMap<String, HashMap<String, String>> mapEmployeFamilleIMI=resultatEvaluationModel.getInfosIMIStat(selected_id_compagne);
		HashMap<String, HashMap<String, Double>> mapFamilleIMG=resultatEvaluationModel.getIMGFamille(selected_id_compagne);
		HashMap<String, Double> mapPosteIMG=resultatEvaluationModel.getIMGparPoste(selected_id_compagne);
//		try 
//		{
		
			//récupération du nombre de compétence toute famille confondu
		
			
			//creation du fichier xls

			//creation d'un document excel 
			HSSFWorkbook workBook = new HSSFWorkbook();
			
			//creation d'une feuille excel associé à un poste de travail
			
			//créatoin du squelette du fichier 
			Set <String>setPosteTravail=mapPosteFamilleCompetence.keySet();
			Iterator <String > iteratorPosteTravail=setPosteTravail.iterator();
			
			while (iteratorPosteTravail.hasNext())
			{	
				// construction de la feuille ligne par ligne
				
				
			
				String nomOnglet=iteratorPosteTravail.next();
				
				//creation de l'onglet
				HSSFSheet sheet = workBook.createSheet(nomOnglet);
				
				HashMap<String, ArrayList<String>> mapFamilleCompetence=mapPosteFamilleCompetence.get(nomOnglet);
			 
				int nbToutesComp=getNbCompetenceAllFamille(mapFamilleCompetence);
				//creation de l'entête du document excel
				
				//cellule nom et prenom de l'evalué
				HSSFRow row = sheet.createRow(0);
				HSSFCell cell = row.createCell((short)0);
				
				
				//sheet.addMergedRegion(new Region(0,(short)0,2,(short)0));
				sheet.addMergedRegion(new CellRangeAddress(0,(short)2,0,0));
				sheet.autoSizeColumn(0); //adjust width of the first column
			    
				
				
				
				HSSFCellStyle cellStyle = null;
				cellStyle = workBook.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				
				cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		        cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		        cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		        cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
		        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		        cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
		        
		        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		        cellStyle.setWrapText(true);
		        cell.setCellValue("Nom et Prénom de l'évalué");
			 	cell.setCellStyle(cellStyle);
			 	
			 	
			 	//cellule critère d'evaluation qui doit regrouper toutes les familles
			 	
			 	sheet.addMergedRegion(new CellRangeAddress(0,(short)0,1,(short)nbToutesComp));
			 	HSSFCell cell1 = row.createCell((short)1);
			 	cell1.setCellValue("Critères d'évaluation");
			 	cell1.setCellStyle(cellStyle);
			 	for (int i=1;i<nbToutesComp+1;i++)
			 	{
			 		sheet.autoSizeColumn(i);
			 	}			 	
			 
			 	//cellule IMI 
			 	sheet.addMergedRegion(new CellRangeAddress(0,(short)2,nbToutesComp+1,(short)nbToutesComp+1));
			 	HSSFCell cell2 = row.createCell((short)nbToutesComp+1);
			 	cell2.setCellValue("IMI");
			 	cell2.setCellStyle(cellStyle);
			 
			 	Set<String> setFamilleCompetence=mapFamilleCompetence.keySet();
			 	Iterator <String> iteratorFamilleCompetence=setFamilleCompetence.iterator();
			 	
			 	int indexColonne=1;
			 	HashMap<String, Short> familleColor=new HashMap<String,Short>();
			 	HSSFRow row1 = sheet.createRow(1);
			 	while(iteratorFamilleCompetence.hasNext())
			 	{
			 		String clesFamille=iteratorFamilleCompetence.next();
			 		int nbcompetence=mapFamilleCompetence.get(clesFamille).size();
			 		System.out.println(clesFamille);
			 		//creation des secondes cellules associées aux familles
			 		
			 		cellStyle = workBook.createCellStyle();
			 		short couleur=HSSFColor.RED.index;
			 		if(clesFamille.equalsIgnoreCase("Affaires"))
			 			 couleur=HSSFColor.RED.index;
			 		else
			 			if(clesFamille.equalsIgnoreCase("RELATIONNELLES"))
			 				couleur=HSSFColor.GREEN.index;
			 			else
			 				if(clesFamille.equalsIgnoreCase("PERSONNELLES"))
			 					couleur=HSSFColor.BLUE.index;
			 				else
			 					if(clesFamille.equalsIgnoreCase("Opérationnelles"))
			 						couleur=HSSFColor.ORCHID.index;
			 					else//couleur orange par défaut
			 						couleur=HSSFColor.ORANGE.index;
			 		familleColor.put(clesFamille, new Short(couleur));
					cellStyle.setFillForegroundColor(couleur);
					
					cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					
			 		sheet.addMergedRegion(new CellRangeAddress(1,(short)1,indexColonne,(short)indexColonne+nbcompetence-1));
				 	HSSFCell cellFamillle = row1.createCell((short)indexColonne);
				 	
				 	cellFamillle.setCellValue(clesFamille);
				 	cellFamillle.setCellStyle(cellStyle);				 	
				 	indexColonne=indexColonne+nbcompetence;
				 					 	
			 	}
			 	
			 	//creation de la troisième ligne competences
			 	iteratorFamilleCompetence=setFamilleCompetence.iterator();
			 	
			 	HSSFRow row2 = sheet.createRow(2);
			 	short longueur=20*256;
			 	row2.setHeight(longueur);
			 	indexColonne=1;
			 	while(iteratorFamilleCompetence.hasNext())
			 	{
			 		String clesfamille=iteratorFamilleCompetence.next();
			 		ArrayList<String> listeCompetence=mapFamilleCompetence.get(clesfamille);
			 		
			 		Short couleur=familleColor.get(clesfamille);

			 		Iterator <String> iteratorCompetence=listeCompetence.iterator();
			 		while(iteratorCompetence.hasNext())
			 		{
			 			String libelle_competence=iteratorCompetence.next();
			 			HSSFCellStyle cellStyle1 = null;
						cellStyle1 = workBook.createCellStyle();
			 			cellStyle1.setFillForegroundColor(couleur);
						
						cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//				        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//				        cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
				        
				        //specification des bordures des cellules
				        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				        cellStyle1.setBottomBorderColor(HSSFColor.BLACK.index);
				        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				        cellStyle1.setLeftBorderColor(HSSFColor.BLACK.index);
				        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
				        cellStyle1.setRightBorderColor(HSSFColor.BLACK.index);
				        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
				        cellStyle1.setTopBorderColor(HSSFColor.BLACK.index);

				        short io=90;
				        cellStyle1.setRotation(io);
				 		sheet.addMergedRegion(new CellRangeAddress(2,(short)2,indexColonne,(short)indexColonne));
					 	HSSFCell cellCompetence = row2.createCell((short)indexColonne);
					 	
					 	cellCompetence.setCellStyle(cellStyle1);
					 	cellCompetence.setCellValue(libelle_competence);
					 	
					 	
					 	indexColonne++;
			 		}
			 		
			 	}
			 	
			 	//creation des données employe
			 	
			 	//HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >> mapPosteEmployeFamilleCompetence
			 	HashMap<String, HashMap< String, HashMap<String, Double>> > EmployeFamilleCompetence=mapPosteEmployeFamilleCompetence.get(nomOnglet);
			 	
			 	Set <String >listEmploye =EmployeFamilleCompetence.keySet();
			 	Iterator<String> iteratorEmploye=listEmploye.iterator();
			 	
			 	int numLigne=3;
			 	while(iteratorEmploye.hasNext())
			 	{
			 		
			 		String nomEmploye=iteratorEmploye.next();
			 		
			 		//creation d'une ligne employe
			 		
			 		
			 		HSSFRow row3 = sheet.createRow(numLigne);
			 		
			 		HSSFCellStyle cellStyle0 = null;
					cellStyle0 = workBook.createCellStyle();
					cellStyle0.setFillForegroundColor(HSSFColor.WHITE.index);
					cellStyle0.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					
					cellStyle0.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			        cellStyle0.setBottomBorderColor(HSSFColor.BLACK.index);
			        cellStyle0.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			        cellStyle0.setLeftBorderColor(HSSFColor.BLACK.index);
			        cellStyle0.setBorderRight(HSSFCellStyle.BORDER_THIN);
			        cellStyle0.setRightBorderColor(HSSFColor.BLACK.index);
			        cellStyle0.setBorderTop(HSSFCellStyle.BORDER_THIN);
			        cellStyle0.setTopBorderColor(HSSFColor.BLACK.index);
			        
			        cellStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			        cellStyle0.setWrapText(true);
			        HSSFCell cell0 = row3.createCell((short)0);
			        cell0.setCellValue(nomEmploye);
				 	cell0.setCellStyle(cellStyle0);
			 		
				 	
				 	//remplissage des moyennes par competence
			 		HashMap< String, HashMap<String, Double>>  mapFamilleCompetence1=EmployeFamilleCompetence.get(nomEmploye);
			 		setFamilleCompetence=mapFamilleCompetence.keySet();
				 	iteratorFamilleCompetence=setFamilleCompetence.iterator();
				 	
				 	Double IMI=new Double(0);
				 	indexColonne=1;
				 	while(iteratorFamilleCompetence.hasNext())
				 	{
				 		String nomFamille=iteratorFamilleCompetence.next();
				 		//recuperation de la valeur de l'IMI
				 		String val=mapEmployeFamilleIMI.get(nomEmploye).get(nomFamille);
				 		String[]v=val.split("#");
				 		IMI=new Double(v[1]);
				 		
				 		HashMap<String, Double> mapCompetence=mapFamilleCompetence1.get(nomFamille);
				 		ArrayList<String> listeCompetence=mapFamilleCompetence.get(nomFamille);
				 		int nbComp=listeCompetence.size();
				 		
				 		for(int i=0;i<nbComp;i++)
				 		{
				 			String competence=listeCompetence.get(i);
				 			
				 			Double valeurStat=mapCompetence.get(competence);
				 			
				 			short couleurCellule=familleColor.get(nomFamille);
				 			
				 			
				 			
				 			
				 			HSSFCellStyle cellStyle1 = null;
							cellStyle1 = workBook.createCellStyle();
				 			cellStyle1.setFillForegroundColor(HSSFColor.WHITE.index);
				 			HSSFFont font = workBook.createFont();
				 		    font.setColor(couleurCellule);
				 		   cellStyle1.setFont(font);
							cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

					        
					        //specification des bordures des cellules
					        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					        cellStyle1.setBottomBorderColor(HSSFColor.BLACK.index);
					        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					        cellStyle1.setLeftBorderColor(HSSFColor.BLACK.index);
					        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
					        cellStyle1.setRightBorderColor(HSSFColor.BLACK.index);
					        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
					        cellStyle1.setTopBorderColor(HSSFColor.BLACK.index);
					        
					        HSSFCell cellCompetence = row3.createCell((short)indexColonne);
						 	
						 	cellCompetence.setCellStyle(cellStyle1);
						 	cellCompetence.setCellValue(valeurStat);
						 	System.out.println( "valeur stat "+ valeurStat +" indexColonne ="+indexColonne);
						 	indexColonne++;
				 		}
				 	}
				 	
				 	//ajouter la cellule IMI
				 	
				 	HSSFCellStyle cellStyle1 = null;
					cellStyle1 = workBook.createCellStyle();
		 			cellStyle1.setFillForegroundColor(HSSFColor.WHITE.index);
					
					cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					HSSFFont font = workBook.createFont();
		 		    font.setColor(HSSFColor.BLACK.index);
		 		   cellStyle1.setFont(font);
			        //specification des bordures des cellules
			        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			        cellStyle1.setBottomBorderColor(HSSFColor.BLACK.index);
			        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			        cellStyle1.setLeftBorderColor(HSSFColor.BLACK.index);
			        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
			        cellStyle1.setRightBorderColor(HSSFColor.BLACK.index);
			        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
			        cellStyle1.setTopBorderColor(HSSFColor.BLACK.index);
			       
			        HSSFCell cellCompetence = row3.createCell((short)indexColonne);
				 	
				 	cellCompetence.setCellStyle(cellStyle1);
				 	cellCompetence.setCellValue(IMI);
				 	
				 	
				 	numLigne++;
				 	
				 	
				 	
				 	
				 	//////////////////////////////////:::
				 	
				 	
				 	HSSFRow row4 = sheet.createRow(numLigne);
				 	indexColonne=1;
				 	iteratorFamilleCompetence=setFamilleCompetence.iterator();
				 	while(iteratorFamilleCompetence.hasNext())
				 	{
				 		String nomFamille=iteratorFamilleCompetence.next();
			 		
				 		
				 		short couleur=familleColor.get(nomFamille);
				 		HSSFCellStyle cellStyle3 = null;
				 		cellStyle3 = workBook.createCellStyle();
				 		cellStyle3.setFillForegroundColor(couleur);
				 		cellStyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					
				 		cellStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				 		cellStyle3.setBottomBorderColor(HSSFColor.BLACK.index);
				 		cellStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				 		cellStyle3.setLeftBorderColor(HSSFColor.BLACK.index);
				 		cellStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);
				 		cellStyle3.setRightBorderColor(HSSFColor.BLACK.index);
				 		cellStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
				 		cellStyle3.setTopBorderColor(HSSFColor.BLACK.index);
			        
				 		cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				 		
				 		ArrayList<String> listeCompetence=mapFamilleCompetence.get(nomFamille);
				 		
				 		int nbcompetence=listeCompetence.size();
				 		
				 		sheet.addMergedRegion(new CellRangeAddress(numLigne,(short)numLigne,indexColonne,(short)indexColonne+nbcompetence-1));
				 		int ff=indexColonne+nbcompetence-1;
				 		System.out.println("CellRangeAddress("+numLigne+",(short)"+numLigne+","+indexColonne+",(short)"+ff+")");
					 	HSSFCell cell3 = row4.createCell((short)indexColonne);
					 	
					 	
					 	String val=mapEmployeFamilleIMI.get(nomEmploye).get(nomFamille);
				 		String[]v=val.split("#");
				 		Double moy_Famille=new Double(v[0]);
				 		
				 		
					 	cell3.setCellValue(moy_Famille);
					 	cell3.setCellStyle(cellStyle3);	
					 	
					 	indexColonne=indexColonne+nbcompetence;
				 		
				 		
				 	}
				 	///////////////////////////////
				 	numLigne++;
				 	
			 	}
			 	indexColonne=0;
			 	//construction des lignes Maitrise moyenne par competence
			 	HSSFRow row5 = sheet.createRow(numLigne);
			 	HSSFCellStyle cellStyle3 = null;
		 		cellStyle3 = workBook.createCellStyle();
		 		cellStyle3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		 		cellStyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				
		 		cellStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		 		cellStyle3.setBottomBorderColor(HSSFColor.BLACK.index);
		 		cellStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		 		cellStyle3.setLeftBorderColor(HSSFColor.BLACK.index);
		 		cellStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		 		cellStyle3.setRightBorderColor(HSSFColor.BLACK.index);
		 		cellStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
		 		cellStyle3.setTopBorderColor(HSSFColor.BLACK.index);
	        
		 		cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 		HSSFCell cell3 = row5.createCell((short)indexColonne);
		 		
			 		
			 	cell3.setCellValue("Maitrise moyenne par competence");
			 	cell3.setCellStyle(cellStyle3);	
				 	
			 	indexColonne=indexColonne+1;
			 	//ajouter ici la boucle qui permet d'afficher le reste des lignes
				 	
				 	
				 	
				 	
				 	
				 	
			 	numLigne++;
			 	//construction des lignes Maitrise moyenne par domaine de competence
			 	HSSFRow row6 = sheet.createRow(numLigne);
			 	HSSFCellStyle cellStyle6 = null;
		 		cellStyle6 = workBook.createCellStyle();
		 		cellStyle6.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		 		cellStyle6.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			
		 		cellStyle6.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		 		cellStyle6.setBottomBorderColor(HSSFColor.BLACK.index);
		 		cellStyle6.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		 		cellStyle6.setLeftBorderColor(HSSFColor.BLACK.index);
		 		cellStyle6.setBorderRight(HSSFCellStyle.BORDER_THIN);
		 		cellStyle6.setRightBorderColor(HSSFColor.BLACK.index);
		 		cellStyle6.setBorderTop(HSSFCellStyle.BORDER_THIN);
		 		cellStyle6.setTopBorderColor(HSSFColor.BLACK.index);
		        
		 		cellStyle6.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 		HSSFCell cell6 = row6.createCell((short)0);
			 		
			 		
			 	cell6.setCellValue("Maitrise moyenne par domaine de competence");
			 	cell6.setCellStyle(cellStyle6);	
				 	
				 	
				 	
			 	////////////////////////////////////////////////
			 	indexColonne=1;
			 	iteratorFamilleCompetence=setFamilleCompetence.iterator();
			 	while(iteratorFamilleCompetence.hasNext())
			 	{
			 		String nomFamille=iteratorFamilleCompetence.next();
		 		
			 		
			 		short couleur=familleColor.get(nomFamille);
			 		HSSFCellStyle cellStyle7 = null;
			 		cellStyle7 = workBook.createCellStyle();
			 		cellStyle7.setFillForegroundColor(couleur);
			 		cellStyle7.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				
			 		cellStyle7.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			 		cellStyle7.setBottomBorderColor(HSSFColor.BLACK.index);
			 		cellStyle7.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			 		cellStyle7.setLeftBorderColor(HSSFColor.BLACK.index);
			 		cellStyle7.setBorderRight(HSSFCellStyle.BORDER_THIN);
			 		cellStyle7.setRightBorderColor(HSSFColor.BLACK.index);
			 		cellStyle7.setBorderTop(HSSFCellStyle.BORDER_THIN);
			 		cellStyle7.setTopBorderColor(HSSFColor.BLACK.index);
		        
			 		cellStyle7.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			 		
			 		ArrayList<String> listeCompetence=mapFamilleCompetence.get(nomFamille);
			 		
			 		int nbcompetence=listeCompetence.size();
			 		
			 		sheet.addMergedRegion(new CellRangeAddress(numLigne,(short)numLigne,indexColonne,(short)indexColonne+nbcompetence-1));
			 		int ff=indexColonne+nbcompetence-1;
			 		System.out.println("CellRangeAddress("+numLigne+",(short)"+numLigne+","+indexColonne+",(short)"+ff+")");
				 	HSSFCell cell7 = row6.createCell((short)indexColonne);
				 	
				 	
				 	Double valIMG=mapFamilleIMG.get(nomOnglet).get(nomFamille);
			 		
				 	cell7.setCellValue(valIMG);
				 	cell7.setCellStyle(cellStyle7);	
				 	
				 	indexColonne=indexColonne+nbcompetence;
			 		
			 		
			 	}
			 	///////////////////////////////
				 	
				numLigne++;
			 	//construction des lignes IMG
			 	HSSFRow row8 = sheet.createRow(numLigne);
			 	HSSFCellStyle cellStyle8 = null;
		 		cellStyle8 = workBook.createCellStyle();
		 		cellStyle8.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		 		cellStyle8.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			
		 		cellStyle8.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		 		cellStyle8.setBottomBorderColor(HSSFColor.BLACK.index);
		 		cellStyle8.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		 		cellStyle8.setLeftBorderColor(HSSFColor.BLACK.index);
		 		cellStyle8.setBorderRight(HSSFCellStyle.BORDER_THIN);
		 		cellStyle8.setRightBorderColor(HSSFColor.BLACK.index);
		 		cellStyle8.setBorderTop(HSSFCellStyle.BORDER_THIN);
		 		cellStyle8.setTopBorderColor(HSSFColor.BLACK.index);
	        
		 		cellStyle8.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 		HSSFCell cell8 = row8.createCell((short)0);
		 		
		 		
			 	cell8.setCellValue("IMG");
			 	cell8.setCellStyle(cellStyle8);	
			 	
			 	
			 	
			 	////////////////////////////////////////////////
			 	
			 	
			 	//construction des lignes IMG
			 	//HSSFRow row9 = sheet.createRow(numLigne);
			 	HSSFCellStyle cellStyle9 = null;
		 		cellStyle9 = workBook.createCellStyle();
		 		cellStyle9.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		 		cellStyle9.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			
		 		cellStyle9.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		 		cellStyle9.setBottomBorderColor(HSSFColor.BLACK.index);
		 		cellStyle9.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		 		cellStyle9.setLeftBorderColor(HSSFColor.BLACK.index);
		 		cellStyle9.setBorderRight(HSSFCellStyle.BORDER_THIN);
		 		cellStyle9.setRightBorderColor(HSSFColor.BLACK.index);
		 		cellStyle9.setBorderTop(HSSFCellStyle.BORDER_THIN);
		 		cellStyle9.setTopBorderColor(HSSFColor.BLACK.index);
	        
		 		cellStyle9.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 		
		 		//calculer le nombre de cellules qui doivent être mergés
		 		iteratorFamilleCompetence=setFamilleCompetence.iterator();
		 		int nbcomp=0;
			 	while(iteratorFamilleCompetence.hasNext())
			 	{
			 		String cles=iteratorFamilleCompetence.next();
			 			ArrayList <String > liste=mapFamilleCompetence.get(cles);
			 			nbcomp=nbcomp+liste.size();
			 	}
		 		sheet.addMergedRegion(new CellRangeAddress(numLigne,(short)numLigne,1,(short)indexColonne+nbcomp-1));
		 		HSSFCell cell9 = row8.createCell((short)1);
		 		
		 		Double IMG=mapPosteIMG.get(nomOnglet);
			 	cell9.setCellValue(IMG); 
			 	cell9.setCellStyle(cellStyle9);	
			 	
			}
//			 	while (index.hasNext())
//			 	{
//				
//			 		StructureEntrepriseBean donnee=(StructureEntrepriseBean)index.next();
//				
//			 		HSSFRow row1 = sheet.createRow(i);
//			 		HSSFCell cel = row1.createCell((short)0);
//			 		cel.setCellValue(donnee.getCodestructure());
//				 
//			 		cel = row1.createCell((short)1);
//			 		cel.setCellValue(donnee.getCodeDivision());
//			 		i++;
//			 	}
			
			 	FileOutputStream fOut;
			 	try 
			 	{
			 		fOut = new FileOutputStream("ResultatEvaluation.xls");
			 		workBook.write(fOut);
					fOut.flush();
					fOut.close();
				
					File file = new File("ResultatEvaluation.xls");
					Filedownload.save(file, "XLS");
			 	} 
			 	catch (FileNotFoundException e) 
			 	{
			 		// TODO Auto-generated catch block
			 		e.printStackTrace();
			 	}
			 	catch (IOException e) 
			 	{
				// TODO Auto-generated catch block
			 		e.printStackTrace();
			 	}
								
//		}
//		catch (SQLException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
	
		
	 }
	
	public int getNbCompetenceAllFamille(HashMap<String, ArrayList<String>>  mapFamilleCompetence)
	{
		
		int nbCompetence=0;
		
		Set <String>setFamille=mapFamilleCompetence.keySet();
		Iterator <String > iteratorFamille=setFamille.iterator();
		
		while (iteratorFamille.hasNext())
		{	
			String famille=iteratorFamille.next();
			ArrayList<String> listCompetence=mapFamilleCompetence.get(famille);
			nbCompetence=nbCompetence+listCompetence.size();
		}
		return nbCompetence;
		
	}
}
