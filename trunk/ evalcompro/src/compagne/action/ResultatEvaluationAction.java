package compagne.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
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
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

import org.apache.poi.ss.usermodel.CellStyle;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Foot;
import org.zkoss.zul.Footer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;

import Statistique.model.StatCotationEmployeModel;



import common.view.MenuComposer;
import compagne.model.ResultatEvaluationModel;

public class ResultatEvaluationAction extends GenericForwardComposer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Tabs tbtabs;
	Tabpanels tbpanels;
	Combobox nomCompagne;
	String selected_id_compagne="1";
	
	HashMap<String, String> map_compagne_idCompagne;
	
	
	
	HashMap <String, HashMap<String, ArrayList<String>>> mapPosteFamilleCompetence;
	
	HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >> mapPosteEmployeFamilleCompetence;
	
	HashMap<String, HashMap<String, String>> mapEmployeFamilleIMI;
	HashMap<String, HashMap<String, Double>> mapFamilleIMG;
	HashMap<String, Double> mapPosteIMG;
	
	HashMap<String, HashMap<String, HashMap< String, Double>>> mapPostFamilleCompetenceStats;
	
	HashMap<String, ArrayList<String>> mapPostEmployeTriIMI;
	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
		
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		
		//r�cup�ration de la liste des compagnes
		
		
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		map_compagne_idCompagne=cotationMoel.getListCompagneValid();
		
		Set<String> listCompagne=map_compagne_idCompagne.keySet();
		Iterator<String> iteratorCompagne=listCompagne.iterator();
		while (iteratorCompagne.hasNext())
		{
			String nom_compagne=iteratorCompagne.next();
			nomCompagne.appendItem(nom_compagne);
		}
		if(nomCompagne.getItemCount()>0)
		{
			nomCompagne.setSelectedIndex(0);
		
			String selectedNomCompagne=nomCompagne.getItemAtIndex(0).getLabel();
			selected_id_compagne=map_compagne_idCompagne.get(selectedNomCompagne);
			
			ResultatEvaluationModel resultatEvaluationModel=new ResultatEvaluationModel();
			
			mapPosteFamilleCompetence=resultatEvaluationModel.getInfosFamillesCompetence(selected_id_compagne);
			
			 mapPosteEmployeFamilleCompetence=resultatEvaluationModel.getAllIMICompetence(selected_id_compagne);
			
			 mapEmployeFamilleIMI=resultatEvaluationModel.getInfosIMIStat(selected_id_compagne);
			mapFamilleIMG=resultatEvaluationModel.getIMGFamille(selected_id_compagne);
			 mapPosteIMG=resultatEvaluationModel.getIMGparPoste(selected_id_compagne);
			
			 mapPostFamilleCompetenceStats=resultatEvaluationModel.getmoyPosteCompetenceStats(selected_id_compagne);
			 
			 mapPostEmployeTriIMI =resultatEvaluationModel.getEmployeTriIMI(selected_id_compagne);
			 
			 AfficherInfosCompagne();
		}
	}
	
	
	
	
	@SuppressWarnings("deprecation")
	public void onClick$exporter()
	 {
		if(nomCompagne.getItemCount()!=0)
		{
		//r�cup�ration des informations d'ent�te du fichier excel
//		ResultatEvaluationModel resultatEvaluationModel=new ResultatEvaluationModel();
//		
//		mapPosteFamilleCompetence=resultatEvaluationModel.getInfosFamillesCompetence(selected_id_compagne);
//		
//		 mapPosteEmployeFamilleCompetence=resultatEvaluationModel.getAllIMICompetence(selected_id_compagne);
//		
//		HashMap<String, HashMap<String, String>> mapEmployeFamilleIMI=resultatEvaluationModel.getInfosIMIStat(selected_id_compagne);
//		HashMap<String, HashMap<String, Double>> mapFamilleIMG=resultatEvaluationModel.getIMGFamille(selected_id_compagne);
//		HashMap<String, Double> mapPosteIMG=resultatEvaluationModel.getIMGparPoste(selected_id_compagne);
//		
//		HashMap<String, HashMap<String, HashMap< String, Double>>> mapPostFamilleCompetenceStats=resultatEvaluationModel.getmoyPosteCompetenceStats(selected_id_compagne);
////		try 
//		{
		
			//r�cup�ration du nombre de comp�tence toute famille confondu
		
			
			//creation du fichier xls

			//creation d'un document excel 
			HSSFWorkbook workBook = new HSSFWorkbook();
			
			//creation du style de texte
			
			HSSFFont font1 = workBook.createFont();
		    font1.setFontHeightInPoints((short)8);
		    font1.setFontName("Arial");

		    
			//creation d'une feuille excel associ� � un poste de travail
			
			//cr�atoin du squelette du fichier 
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
				//creation de l'ent�te du document excel
				
				//cellule nom et prenom de l'evalu�
				HSSFRow row = sheet.createRow(0);
				HSSFCell cell = row.createCell((short)0);
				
				

				sheet.addMergedRegion(new CellRangeAddress(0,(short)2,0,0));
				
			    
				
				
				
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
		        
		        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		        
		        //cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		        //cellStyle.setWrapText(true);
		        //cellStyle.setFont(font1);
		        cell.setCellValue("Nom et Pr�nom de l'�valu�");
			 	cell.setCellStyle(cellStyle);
			 	
			 	
			 	//cellule crit�re d'evaluation qui doit regrouper toutes les familles
			 	
			 	sheet.addMergedRegion(new CellRangeAddress(0,(short)0,1,(short)nbToutesComp));
			 	HSSFCell cell1 = row.createCell((short)1);
			 	cell1.setCellValue("Crit�res d'�valuation");
			 	cell1.setCellStyle(cellStyle);
			 			 	
			 
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
			 		//System.out.println(clesFamille);
			 		//creation des secondes cellules associ�es aux familles
			 		
			 		cellStyle = workBook.createCellStyle();
			 		short couleur=HSSFColor.RED.index;
			 		if(clesFamille.equalsIgnoreCase("Affaires"))
			 			 couleur=HSSFColor.RED.index;
			 		else
			 			if(clesFamille.equalsIgnoreCase("RELATIONNELLES"))
			 				couleur=HSSFColor.GREEN.index;
			 			else
			 				if(clesFamille.equalsIgnoreCase("PERSONNELLES"))
			 					couleur=HSSFColor.LIGHT_BLUE.index;
			 				else
			 					if(clesFamille.equalsIgnoreCase("Op�rationnelles"))
			 						couleur=HSSFColor.ORCHID.index;
			 					else//couleur orange par d�faut
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
			 	
			 	//creation de la troisi�me ligne competences
			 	iteratorFamilleCompetence=setFamilleCompetence.iterator();
			 	
			 	HSSFRow row2 = sheet.createRow(2);
			 	short longueur=18*256;
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
			 			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
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
				        cellStyle1.setFont(font1);
				        short io=90;
				        cellStyle1.setRotation(io);
				 		sheet.addMergedRegion(new CellRangeAddress(2,(short)2,indexColonne,(short)indexColonne));
					 	HSSFCell cellCompetence = row2.createCell((short)indexColonne);
					 	
					 	cellCompetence.setCellStyle(cellStyle1);
					 	cellCompetence.setCellValue(libelle_competence);
					 	
					 	
					 	indexColonne++;
			 		}
			 		
			 	}
			 	
			 	//creation des donn�es employe
			 	
			 	//HashMap<String, HashMap<String, HashMap< String, HashMap<String, Double>> >> mapPosteEmployeFamilleCompetence
			 	HashMap<String, HashMap< String, HashMap<String, Double>> > EmployeFamilleCompetence=mapPosteEmployeFamilleCompetence.get(nomOnglet);
			 	
			 	Set <String >listEmploye =EmployeFamilleCompetence.keySet();
			 	Iterator<String> iteratorEmploye=listEmploye.iterator();
			 	
			 	int numLigne=3;
			 	
			 	
			///////prise en cmpte du tri/////////////
				ArrayList<String> listemployeTrie=mapPostEmployeTriIMI.get(nomOnglet);
				Iterator<String> iteratoremployeTrie=listemployeTrie.iterator();
				////////////////////////////////////////////////
			 	while(/*iteratorEmploye.hasNext()*/iteratoremployeTrie.hasNext())
			 	{
			 		String nomEmploye=iteratoremployeTrie.next();
			 		
			 		//String nomEmploye=iteratorEmploye.next();
			 		
			 		//creation d'une ligne employe
			 		
			 		
			 		HSSFRow row3 = sheet.createRow(numLigne);
			 		
			 		HSSFCellStyle cellStyle0 = null;
					cellStyle0 = workBook.createCellStyle();
					cellStyle0.setFillForegroundColor(HSSFColor.WHITE.index);
					cellStyle0.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cellStyle0.setFont(font1);
					
					
					cellStyle0.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			        cellStyle0.setBottomBorderColor(HSSFColor.BLACK.index);
			        cellStyle0.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			        cellStyle0.setLeftBorderColor(HSSFColor.BLACK.index);
			        cellStyle0.setBorderRight(HSSFCellStyle.BORDER_THIN);
			        cellStyle0.setRightBorderColor(HSSFColor.BLACK.index);
			        cellStyle0.setBorderTop(HSSFCellStyle.BORDER_THIN);
			        cellStyle0.setTopBorderColor(HSSFColor.BLACK.index);
			        
			        cellStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			        //cellStyle0.setWrapText(true);
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
						    font.setFontHeightInPoints((short)8);
						    font.setFontName("Arial");
				 		    cellStyle1.setFont(font);
							cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
							cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
					        
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
						 	//cellCompetence.setCellValue(valeurStat);

						 	cellCompetence.setCellValue(valeurStat+"");
						 	//System.out.println( "valeur stat "+ valeurStat +" indexColonne ="+indexColonne +" competence = "+competence);
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
				    font.setFontHeightInPoints((short)8);
				    font.setFontName("Arial");
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
			        cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			        
			        HSSFCell cellCompetence = row3.createCell((short)indexColonne);
				 	
				 	cellCompetence.setCellStyle(cellStyle1);
				 	cellCompetence.setCellValue(IMI+"");
				 	
				 	
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
				 		cellStyle3.setFont(font1);
				 		cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				 		
				 		ArrayList<String> listeCompetence=mapFamilleCompetence.get(nomFamille);
				 		
				 		int nbcompetence=listeCompetence.size();
				 		
				 		sheet.addMergedRegion(new CellRangeAddress(numLigne,(short)numLigne,indexColonne,(short)indexColonne+nbcompetence-1));
				 		int ff=indexColonne+nbcompetence-1;
				 		//System.out.println("CellRangeAddress("+numLigne+",(short)"+numLigne+","+indexColonne+",(short)"+ff+")");
					 	HSSFCell cell3 = row4.createCell((short)indexColonne);
					 	
					 	
					 	String val=mapEmployeFamilleIMI.get(nomEmploye).get(nomFamille);
				 		String[]v=val.split("#");
				 		Double moy_Famille=new Double(v[0]);
				 		
				 		
					 	cell3.setCellValue(moy_Famille+"");
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
		 		cellStyle3.setFont(font1);
		 		cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 		HSSFCell cell3 = row5.createCell((short)indexColonne);
		 		
			 		
			 	cell3.setCellValue("Maitrise moyenne par competence");
			 	cell3.setCellStyle(cellStyle3);	
				 	
			 	indexColonne=indexColonne+1;
			 	//ajouter ici la boucle qui permet d'afficher le reste des lignes
				 	
/////////////**********************************************************************************
			 			
			 	
			 	
			 	//remplissage des moyennes par competence
		 		HashMap< String, HashMap<String, Double>>  mapFamilleCompetence1=mapPostFamilleCompetenceStats.get(nomOnglet);
		 		setFamilleCompetence=mapFamilleCompetence.keySet();
			 	iteratorFamilleCompetence=setFamilleCompetence.iterator();

			 	indexColonne=1;
			 	while(iteratorFamilleCompetence.hasNext())
			 	{
			 		String nomFamille=iteratorFamilleCompetence.next();

			 		
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
			 		   font.setFontHeightInPoints((short)8);
					    font.setFontName("Arial");
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
				        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				        HSSFCell cellCompetence = row5.createCell((short)indexColonne);
					 	
					 	cellCompetence.setCellStyle(cellStyle1);
					 	cellCompetence.setCellValue(valeurStat+"");
					 	
					 	indexColonne++;
			 		}
			 	}
			 	
////////////////////////////////*************************************************************
				 	
				 	
				 	
				 	
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
		 		cellStyle6.setFont(font1);
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
			 		cellStyle7.setFont(font1);
			 		cellStyle7.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			 		
			 		ArrayList<String> listeCompetence=mapFamilleCompetence.get(nomFamille);
			 		
			 		int nbcompetence=listeCompetence.size();
			 		
			 		sheet.addMergedRegion(new CellRangeAddress(numLigne,(short)numLigne,indexColonne,(short)indexColonne+nbcompetence-1));
			 		int ff=indexColonne+nbcompetence-1;
			 		//System.out.println("CellRangeAddress("+numLigne+",(short)"+numLigne+","+indexColonne+",(short)"+ff+")");
				 	HSSFCell cell7 = row6.createCell((short)indexColonne);
				 	
				 	
				 	Double valIMG=mapFamilleIMG.get(nomOnglet).get(nomFamille);
			 		
				 	cell7.setCellValue(valIMG+"");
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
		 		cellStyle8.setFont(font1);
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
		 		cellStyle9.setFont(font1);
		 		cellStyle9.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 		
		 		//calculer le nombre de cellules qui doivent �tre merg�s
		 		iteratorFamilleCompetence=setFamilleCompetence.iterator();
		 		int nbcomp=0;
			 	while(iteratorFamilleCompetence.hasNext())
			 	{
			 		String cles=iteratorFamilleCompetence.next();
			 			ArrayList <String > liste=mapFamilleCompetence.get(cles);
			 			nbcomp=nbcomp+liste.size();
			 	}
			 	
			 	
		 		sheet.addMergedRegion(new CellRangeAddress(numLigne,(short)numLigne,1,(short)nbcomp));
		 		HSSFCell cell9 = row8.createCell((short)1);
		 		
		 		Double IMG=mapPosteIMG.get(nomOnglet);
			 	cell9.setCellValue(IMG+""); 
			 	cell9.setCellStyle(cellStyle9);	
			 	
			 		 	
			 	for (int i=0;i<nbToutesComp+1;i++)
			 	{
			 		sheet.autoSizeColumn(i);
			 		
			 	}	
			}

			
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
								
	
		}
		
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
	
	public void onSelect$nomCompagne() throws SQLException
	 {
		


		 
		if(nomCompagne.getItemCount()!=0)
		{
			String selectedNomCompagne=nomCompagne.getSelectedItem().getLabel();
			
			//System.out.println("compagne selectionn� " +selectedNomCompagne);
			selected_id_compagne=map_compagne_idCompagne.get(selectedNomCompagne);
			
			//System.out.println("selected_id_compagne selectionn� " +selected_id_compagne);
			
			ResultatEvaluationModel resultatEvaluationModel=new ResultatEvaluationModel();
			
			mapPosteFamilleCompetence=resultatEvaluationModel.getInfosFamillesCompetence(selected_id_compagne);
			
			 mapPosteEmployeFamilleCompetence=resultatEvaluationModel.getAllIMICompetence(selected_id_compagne);
			
			 mapEmployeFamilleIMI=resultatEvaluationModel.getInfosIMIStat(selected_id_compagne);
			 mapFamilleIMG=resultatEvaluationModel.getIMGFamille(selected_id_compagne);
			 mapPosteIMG=resultatEvaluationModel.getIMGparPoste(selected_id_compagne);
			
			 mapPostFamilleCompetenceStats=resultatEvaluationModel.getmoyPosteCompetenceStats(selected_id_compagne);
			 
			 mapPostEmployeTriIMI =resultatEvaluationModel.getEmployeTriIMI(selected_id_compagne);
			 //supprimer l'affichage precedant s'il existe
			 List<Tab> listOnglet=tbtabs.getChildren();
			 List<Tabpanel> listPanel=tbpanels.getChildren();
			 if (listOnglet.size()!=0)
			 {
				 Iterator<Tab> listeIterator=listOnglet.iterator();
				// int i=0;
				 int nbOnglets=listOnglet.size();
				 nbOnglets=nbOnglets-1;
				 for(int i=nbOnglets;i>=0;i--)
				 {
					 Tab tab=listOnglet.get(i);
					 Tabpanel panel=listPanel.get(i);
					 //System.out.println("detachement tab  " + tab.getLabel());
					 panel.detach();
					 tab.detach();;
				 }
			 }

			 AfficherInfosCompagne();
			
		}
	 }
	
	public void AfficherInfosCompagne()
	{
		Set <String> SetPoste=mapPosteEmployeFamilleCompetence.keySet();
		Iterator<String> iteratorPoste=SetPoste.iterator();
		while(iteratorPoste.hasNext())
		{
			//creation de tab0 et sa fermeture pour qu'i n'y ait pas de d�calage lors de l'ajout des listboxs
			Tab newTab0 = new Tab(); 
			newTab0.isClosable();	
			tbtabs.appendChild(newTab0);			
			newTab0.close();
			
			String nomOnglet=iteratorPoste.next();
			//creation du tab
			Tab newTab = new Tab();

			newTab.setLabel(nomOnglet);
			
			
			Tabpanel newPanel = new Tabpanel();
			newPanel.setStyle("overflow:auto");
			
			Label newLabel = new Label();
			newLabel.setValue("");
			
			newPanel.appendChild(newLabel);
			
			Grid grid=creationtTableau(newPanel,nomOnglet);
			
			//creation du contenu de l'onglet
			
			newPanel.appendChild(grid);
			
			tbpanels.appendChild(newPanel);
			
			tbtabs.appendChild(newTab);	
			
		}
		
	}
	
	public Grid creationtTableau(Tabpanel newPanel,String nomOnglet)
	{
		Grid grid=new Grid();
		
		
		//specification des caracteristiques du tableau
		grid.setSpan(true);
		grid.setSizedByContent(true);
		grid.setWidth("100%");
		grid.setHeight("500px");
		
		
		Columns colonneTitre=new Columns();
		
		grid.appendChild(colonneTitre);
		colonneTitre.setSizable(true);
		
		//creation de la colonne nomEmploye
		Column colonneNomEmploye=new Column();
		colonneTitre.appendChild(colonneNomEmploye);
		//<column label="Code structure" align="center" width="100px" />
		colonneNomEmploye.setLabel("Nom et Pr�nom de l'�valu�");
		colonneNomEmploye.setAlign("center");
		colonneNomEmploye.setWidth("100px");
		
		HashMap<String, ArrayList<String>> mapFamilleCompetence=mapPosteFamilleCompetence.get(nomOnglet);
		 HashMap<String, HashMap< String, HashMap<String, Double>> > mapEmployeFamilleCompetence= mapPosteEmployeFamilleCompetence.get(nomOnglet);
		 HashMap<String,Double> mapFIMG=mapFamilleIMG.get(nomOnglet);
		Set <String> setFamilleCompetence=mapFamilleCompetence.keySet();
				Iterator <String>iteratorFamille=setFamilleCompetence.iterator();
				while(iteratorFamille.hasNext())
				{
					//creation des colonnes famille
					String famille=iteratorFamille.next();
					Column colonneFamille=new Column();
					colonneTitre.appendChild(colonneFamille);
					//<column label="Code structure" align="center" width="100px" />
					colonneFamille.setLabel(famille);
					colonneFamille.setAlign("center");
					colonneFamille.setWidth("100px");
					
				}
				
				//creation de la colonne IMI
				Column colonneIMI=new Column();	
				colonneTitre.appendChild(colonneIMI);
				colonneIMI.setLabel("IMI");
				colonneIMI.setAlign("center");
				colonneIMI.setWidth("100px");
				
				
				//creation des lignes pour chaque employe
				Rows rows=new Rows();
				grid.appendChild(rows);
				Set <String>setEmploye =mapEmployeFamilleCompetence.keySet();
				Iterator <String> iteratorEmploye=setEmploye.iterator();
				
				///////prise en cmpte du tri/////////////
				ArrayList<String> listemployeTrie=mapPostEmployeTriIMI.get(nomOnglet);
				Iterator<String> iteratoremployeTrie=listemployeTrie.iterator();
				///////////////////
				while(/*iteratorEmploye.hasNext()*/iteratoremployeTrie.hasNext())
				{
					//String nomEmploye=iteratorEmploye.next();
					String nomEmploye=iteratoremployeTrie.next();
					HashMap<String, String> mapFamilleIMI=mapEmployeFamilleIMI.get(nomEmploye);
					//creation de la ligne employe
					Row ligne=new Row();
					rows.appendChild(ligne);
					//remplissage des colonnes familles et IMI de la ligne employe
					Label nom=new Label();
					nom.setValue(nomEmploye);
					//System.out.println("employe= "+ nomEmploye);
					
					ligne.appendChild(nom);
					iteratorFamille=setFamilleCompetence.iterator();
					String IMI="";
					while(iteratorFamille.hasNext())
					{
						
						String nomFamille=iteratorFamille.next();
						//System.out.println("nomFamille=" +nomFamille);
						String valeurIMI=mapFamilleIMI.get(nomFamille);
						
						String[] valeur=valeurIMI.split("#");
						String val=valeur[0];
						IMI=valeur[1];
						Label infosFamille=new Label();
						infosFamille.setValue(val);
							
						ligne.appendChild(infosFamille);
						//System.out.println("valeur="+ val);
					}
					//affichage de la valeur IMI dans le tableau
					Label infosIMI=new Label();
					infosIMI.setValue(IMI);
						
					ligne.appendChild(infosIMI);
				}
				
				//creation de la ligne IMG par famille
				
				
				Row ligne=new Row();
				rows.appendChild(ligne);
				//remplissage des colonnes familles et IMI de la ligne employe
				Label nom=new Label();
				nom.setValue("Maitrise moyenne par domaine de competence");
				
				
				ligne.appendChild(nom);
				iteratorFamille=setFamilleCompetence.iterator();
				int nbfamille=0;
				while(iteratorFamille.hasNext())
				{
					
					String nomFamille=iteratorFamille.next();
					
					Double valeur=mapFIMG.get(nomFamille);
					
					
					String val=valeur.toString();
					
					Label infosFamilleIMG=new Label();
					infosFamilleIMG.setValue(val);
						
					ligne.appendChild(infosFamilleIMG);
					nbfamille++;
				}
				//creation de la derni�re ligne IMG (footer)
				
				
				//creation de la ligne IMG par famille
				
				
		//		Row ligneIMG=new Row();
		//		rows.appendChild(ligneIMG);
		//		
		//		Label nomIMG=new Label();
		//		nomIMG.setValue("IMG");		
		//		ligneIMG.appendChild(nomIMG);
		//		
				Double img=mapPosteIMG.get(nomOnglet);
				
				nbfamille++;
				Row ligneIMGVal=new Row();
				ligneIMGVal.setSpans(nbfamille+"");
				rows.appendChild(ligneIMGVal);
				//remplissage des colonnes familles et IMI de la ligne employe
				Label valIMG=new Label();
				
				valIMG.setValue("IMG = "+img.toString());		
				ligneIMGVal.appendChild(valIMG);
		

		return grid;
	}
}
