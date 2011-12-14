package administration.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Window;

import compagne.bean.GestionEmployesBean;
import compagne.model.GestionEmployesModel;

import administration.bean.EmployeCompteBean;
import administration.bean.FichePosteBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.EmployeCompteModel;
import administration.model.FichePosteModel;
import administration.model.StructureEntrepriseModel;



public class ChargementMasseAction extends GenericForwardComposer {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AnnotateDataBinder binder;
	Window win;
	List<StructureEntrepriseBean> model = new ArrayList<StructureEntrepriseBean>();
	List<FichePosteBean> model2 = new ArrayList<FichePosteBean>();
	List<EmployeCompteBean> model3 = new ArrayList<EmployeCompteBean>();
	
	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// création des données 
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		
		FichePosteModel init =new FichePosteModel();
		EmployeCompteModel init3= new EmployeCompteModel();
		model=structureEntrepriseModel.checkStructureEntreprise();

		model2=init.loadFichesPostes();
		model3=init3.loadListEmployes();
		
		HashMap<String,StructureEntrepriseBean> mapStructureEntreprise= init.getStructureEntreprise();
		

		Iterator<FichePosteBean> iterator=model2.iterator();
		while(iterator.hasNext())
		{
			FichePosteBean ficheBean=iterator.next();
			String codeStructure=ficheBean.getCode_structure();
			StructureEntrepriseBean structure=mapStructureEntreprise.get(codeStructure);
			if(structure!=null)
			{
				String libell_division=structure.getLibelleDivision();
				String libelleDirection=structure.getLibelleDirection();
				String libelleUnite=structure.getLibelleUnite();
				String libelleDepartement=structure.getLibelleDepartement();
				String libelleService=structure.getLibelleService();
				String libelleSection=structure.getLibelleSection();
				if(libell_division==null) libell_division="";
				if(libelleDirection==null)libelleDirection="";
				if(libelleUnite==null)libelleUnite="";
				if(libelleDepartement==null)libelleDepartement="";
				if(libelleService==null)libelleService="";
				if(libelleSection==null)libelleSection="";
				String valeurCode_Structure=codeStructure+","+libell_division+","+libelleDirection+","+libelleUnite+","+libelleDepartement+","+ libelleService+","+libelleSection;
				ficheBean.setCodeStructLibelle(valeurCode_Structure);
			}
		}
		
		Iterator<EmployeCompteBean> iterator2=model3.iterator();
		while(iterator2.hasNext())
		{
			EmployeCompteBean employeCompteBean=iterator2.next();
			String codeStructure=employeCompteBean.getCode_structure();
			
			StructureEntrepriseBean structure=mapStructureEntreprise.get(codeStructure);
			if(structure!=null)
			{
				String libell_division=structure.getLibelleDivision();
				String libelleDirection=structure.getLibelleDirection();
				String libelleUnite=structure.getLibelleUnite();
				String libelleDepartement=structure.getLibelleDepartement();
				String libelleService=structure.getLibelleService();
				String libelleSection=structure.getLibelleSection();
				if(libell_division==null) libell_division="";
				if(libelleDirection==null)libelleDirection="";
				if(libelleUnite==null)libelleUnite="";
				if(libelleDepartement==null)libelleDepartement="";
				if(libelleService==null)libelleService="";
				if(libelleSection==null)libelleSection="";
				String valeurCode_Structure=codeStructure+","+libell_division+","+libelleDirection+","+libelleUnite+","+libelleDepartement+","+ libelleService+","+libelleSection;
				employeCompteBean.setCode_structure(valeurCode_Structure);
			}
		}
		comp.setVariable(comp.getId() + "Ctrl", this, true);

		binder = new AnnotateDataBinder(comp);
		
		binder.loadAll();
		
		
		
	}

	public List<StructureEntrepriseBean> getModel() {
		return model;
	}
	public List<FichePosteBean> getModel2() {
		return model2;
	}

	public List<EmployeCompteBean> getModel3() {
		return model3;
	}
	 public void onClick$download() {
			//chargement du contenu de la table Fiche_Poste et creation du fichier excel
			FichePosteModel fichePostemodel =new FichePosteModel();
			
			//creation du document xls
			HSSFWorkbook workBook = new HSSFWorkbook();
			
			
			//création de l'onglet structure entreprise et remplissage des données
			
			StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
			
			structureEntrepriseModel.downloadStructureEntrepriseDataToXls(workBook);
			
			//creation de l'onglet listePostetravail et remplissage des données
			fichePostemodel.downloadFichePosteDataToXls(workBook);
			
			//creation de l'onglet liste employés et remplissage des données
			EmployeCompteModel employeCompte=new EmployeCompteModel();
			employeCompte.downloadEmployeCompteDataToXls(workBook,model3);
			
			//enregistrement des données dans un fihcier xls
			FileOutputStream fOut;
			try 
			{
				fOut = new FileOutputStream("Donnees_Evalcom.xls");
				workBook.write(fOut);
				fOut.flush();
				fOut.close();
				
				File file = new File("Donnees_Evalcom.xls");
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

}
