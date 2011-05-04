package administration.action;

import java.awt.FileDialog;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.lang.Strings;
import org.zkoss.util.media.Media;
import org.zkoss.zk.au.out.AuClearWrongValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.sys.ExecutionsCtrl;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.sun.jna.StructureReadContext;

import administration.bean.StructureEntrepriseBean;
import administration.model.StructureEntrepriseModel;


public class StructureEntreprise extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox structureEntrepriselb;
	
	Textbox codeStructure;	
	Textbox codeDivision;
	Textbox nomDivision;
	Textbox codeDirection;
	Textbox nomDirection;
	Textbox codeUnite;
	Textbox nomUnite;
	Textbox codeDepartement;
	Textbox nomdepatrement;
	Textbox codeService;
	Textbox NomService;
	Textbox codeSection;
	Textbox nomSection;
	Button upload;
	Fileupload fichierupload;
	AnnotateDataBinder binder;
	Window win;
	Textbox intro;

	Div divupdown;
	
	List<StructureEntrepriseBean> model = new ArrayList<StructureEntrepriseBean>();
	
	
	
	StructureEntrepriseBean selected;
	

	public StructureEntreprise() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// cr�ation de la structure de l'entreprise bean
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		model=structureEntrepriseModel.checkStructureEntreprise();
		
		comp.setVariable(comp.getId() + "Ctrl", this, true);

		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
	}

	public List<StructureEntrepriseBean> getModel() {
		return model;
	}



	public StructureEntrepriseBean getSelected() {
		return selected;
	}

	public void setSelected(StructureEntrepriseBean selected) {
		this.selected = selected;
	}

	public void onClick$add() {
		
		
		StructureEntrepriseBean addedData = new StructureEntrepriseBean();
		
		
		addedData.setCodestructure(getSelectedcodeStructure());
		addedData.setCodeDivision(getSelectedcodeDivision());
		addedData.setLibelleDivision(getSelectednomDivision());
		addedData.setCodeDirection(getSelectedcodeDirection());
		addedData.setLibelleDirection(getSelectednomDirection());
		addedData.setCodeUnite(getSelectedcodeUnite());
		addedData.setLibelleUnite(getSelectednomUnite());
		addedData.setCodeDepartement(getSelectedcodeDepartement());
		addedData.setLibelleDepartement(getSelectednomDepartement());
		addedData.setCodeService(getSelectedcodeService());
		addedData.setLibelleService(getSelectednomService());
		addedData.setCodesection(getSelectedcodeSection());
		addedData.setLibelleSection(getSelectednomSection());
		
		//controle d'int�grit� 
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		Boolean donneeValide=structureEntrepriseModel.controleIntegrite(addedData);
		if (donneeValide)
		{
			//insertion de la donn�e ajout�e dans la base de donn�e
			boolean donneeAjoute=structureEntrepriseModel.addStructureEntrepriseBean(addedData);
			// raffrechissemet de l'affichage
			if (donneeAjoute )
			{
				model.add(addedData);
			
				selected = addedData;
			
				binder.loadAll();
			}
		}
				
	}

	public void onClick$update() {
		if (selected == null) {
			alert("Aucune donn�e n'a �t� selectionn�e");
			return;
		}
		String codeStructureselectione=selected.getCodestructure();
		System.out.println(getSelectedcodeStructure());
		selected.setCodestructure(getSelectedcodeStructure());
		selected.setCodeDivision(getSelectedcodeDivision());
		selected.setLibelleDivision(getSelectednomDivision());
		selected.setCodeDirection(getSelectedcodeDirection());
		selected.setLibelleDirection(getSelectednomDirection());
		selected.setCodeUnite(getSelectedcodeUnite());
		selected.setLibelleUnite(getSelectednomUnite());
		selected.setCodeDepartement(getSelectedcodeDepartement());
		selected.setLibelleDepartement(getSelectednomDepartement());
		selected.setCodeService(getSelectedcodeService());
		selected.setLibelleService(getSelectednomService());
		selected.setCodesection(getSelectedcodeSection());
		selected.setLibelleSection(getSelectednomSection());
		
		//controle d'int�grit� 
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		Boolean donneeValide=structureEntrepriseModel.controleIntegrite(selected);
		if (donneeValide)
		{
			//insertion de la donn�e ajout�e dans la base de donn�e
			boolean donneeAjoute=structureEntrepriseModel.majStructureEntrepriseBean(selected,codeStructureselectione);
			// raffrechissemet de l'affichage
			if (donneeAjoute )
			{
				binder.loadAll();
			}
		}
	}

	public void onClick$delete() {
		if (selected == null) {
			alert("Aucune donn�e n'a �t� selectionn�e");
			return;
		}
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		//suppression de la donn�e supprim�e de la base de donn�e
		structureEntrepriseModel.supprimerStructureEntrepriseBean(selected.getCodestructure());
		model.remove(selected);
		selected = null;


		
		binder.loadAll();
	}

	public void onClick$effacer() {
		codeStructure.setText("");
		codeDivision.setText("");
		nomDivision.setText("");
		codeDirection.setText("");
		nomDirection.setText("");
		codeUnite.setText("");
		nomUnite.setText("");
		codeDepartement.setText("");
		nomdepatrement.setText("");
		codeService.setText("");
		NomService.setText("");
		codeSection.setText("");
		nomSection.setText("");
	}

	public void affichermessage()
	{
		try {
			Messagebox.show("La taille du champ Code division ne doit pas d�passer 4 caract�res", "Erreur",Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onUpload$divupdown(UploadEvent event) throws InterruptedException
	{
		
		Media med=event.getMedia();
		
		if ((med != null)&&(med.getName()!=null)) 
		{
			String filename = med.getName();
			
			if ( filename.indexOf(".xls") == -1 ) 
			{
			  alert(filename + " n'est pas un fichier excel");
			} 
			else 
			{
			  // process the file...
				

				StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
				if ( filename.endsWith(".xls") ) 
				{
					//lecture et upload de fichiers OLE2 Office Documents 
					List<StructureEntrepriseBean> liste=structureEntrepriseModel.uploadXLSFile(med.getStreamData());
					List<StructureEntrepriseBean> donneeRejetes;
					try 
					{
						 HashMap <String,List<StructureEntrepriseBean>> listeDonnees=structureEntrepriseModel.ChargementDonneedansBdd(liste);
						 donneeRejetes =listeDonnees.get("supprimer");
						 liste=null;
						 liste=listeDonnees.get("inserer");;
						
					
						//raffrechissement de l'affichage
						Iterator<StructureEntrepriseBean> index=liste.iterator();
						while(index.hasNext())
						{
							StructureEntrepriseBean donnee=index.next();
							model.add(donnee);
							
						}
				
						binder.loadAll();
						if(donneeRejetes.size()!=0)
						{
							String listeRejet=new String("-->");
							//Afficharge de la liste des donn�es rejet�es
							Iterator<StructureEntrepriseBean> index1 =donneeRejetes.iterator();
							while(index1.hasNext())
							{
								StructureEntrepriseBean donnee=index1.next();
								String donneeString=donnee.getCodestructure()+";"+donnee.getCodeDivision()
								+";"+donnee.getLibelleDivision()
								 +";"+donnee.getCodeDirection()
								+";"+donnee.getLibelleDirection()
								+";"+donnee.getCodeUnite()
								+";"+donnee.getLibelleUnite()
								+";"+donnee.getCodeDepartement()
								+ ";"+donnee.getLibelleDepartement()
								+";"+donnee.getCodeService()
								+";"+donnee.getLibelleService()
								+ ";"+donnee.getCodesection()
								+ ";"+donnee.getLibelleSection();
								listeRejet=listeRejet+System.getProperty("line.separator")+donneeString;//saut de ligne
								System.out.println("-->"+listeRejet);
							}
							AfficherFenetreRejet(listeRejet);

						}
					} 
					catch (Exception e) 
					{
							// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					if(filename.endsWith(".xlsx"))
					{
						// lecture de fichiers Office 2007+ XML
						List<StructureEntrepriseBean> liste=structureEntrepriseModel.uploadXLSXFile(med.getStreamData());
						List<StructureEntrepriseBean> donneeRejetes;
						try 
						{
							 HashMap <String,List<StructureEntrepriseBean>> listeDonnees=structureEntrepriseModel.ChargementDonneedansBdd(liste);
							 donneeRejetes =listeDonnees.get("supprimer");
							 liste=null;
							 liste=listeDonnees.get("inserer");;
							
						
							//raffrechissement de l'affichage
							Iterator<StructureEntrepriseBean> index=liste.iterator();
							while(index.hasNext())
							{
								StructureEntrepriseBean donnee=index.next();
								model.add(donnee);
								
							}
					
							binder.loadAll();
							if(donneeRejetes.size()!=0)
							{
								String listeRejet=new String("-->");
								//Afficharge de la liste des donn�es rejet�es
								Iterator<StructureEntrepriseBean> index1 =donneeRejetes.iterator();
								while(index1.hasNext())
								{
									StructureEntrepriseBean donnee=index1.next();
									String donneeString=donnee.getCodestructure()+";"+donnee.getCodeDivision()
									+";"+donnee.getLibelleDivision()
									 +";"+donnee.getCodeDirection()
									+";"+donnee.getLibelleDirection()
									+";"+donnee.getCodeUnite()
									+";"+donnee.getLibelleUnite()
									+";"+donnee.getCodeDepartement()
									+ ";"+donnee.getLibelleDepartement()
									+";"+donnee.getCodeService()
									+";"+donnee.getLibelleService()
									+ ";"+donnee.getCodesection()
									+ ";"+donnee.getLibelleSection();
									listeRejet=listeRejet+System.getProperty("line.separator")+donneeString;//saut de ligne
									System.out.println("-->"+listeRejet);
								}
								AfficherFenetreRejet(listeRejet);

							}
						} 
						catch (Exception e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				
				} 				
			}  

		}	


	
	public void onClick$upload() {
		Executions.getCurrent().getDesktop().setAttribute(
	            "org.zkoss.zul.Fileupload.target", divupdown);
		try 
		{
			Fileupload fichierupload=new Fileupload();
			Media me=fichierupload.get("Merci de selectionner le fichier qui doit �tre charg�", "Chargement de fichier", true);
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onClick$download() {
		//chargement du contenu de la table structure_entreprise et creation du fichier excel
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		System.out.println("avant recup donnee");
		byte[] fichier=structureEntrepriseModel.downloadStructureEntrepriseDataToXls();
		
		//InputStream file=new InputStream(fichier);
		System.out.println("apres recup donnee");
		//enregistrement du fichier
		Filedownload fichierdownload=new Filedownload();

		System.out.println("avant save");
		fichierdownload.save(fichier, "xls", "Structure_entreprise.xls");
		System.out.println("save");
		// partie affichage
		
		//partie base de donn�es
	}
	public void onSelect$structureEntrepriselb() {
		closeErrorBox(new Component[] { codeStructure, codeDivision,nomDivision,codeDirection,  nomDirection, 
				codeUnite,nomUnite, codeDepartement, nomdepatrement, codeService,NomService, codeSection, nomSection });
	}
	
	
	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}



	private String getSelectedcodeStructure() throws WrongValueException {
		String name = codeStructure.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeStructure, "le Code Structure ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectedcodeDivision() throws WrongValueException {
		String name = codeDivision.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeDivision, "le Code Division ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectednomDivision() throws WrongValueException {
		String name = nomDivision.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomDivision, "le nom Division ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectedcodeDirection() throws WrongValueException {
		String name = codeDirection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeDirection, "le codeDirection ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectednomDirection() throws WrongValueException {
		String name = nomDirection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomDirection, "le nom Direction ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectedcodeUnite() throws WrongValueException {
		String name = codeUnite.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeUnite, "le codeUnite ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectednomUnite() throws WrongValueException {
		String name = nomUnite.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomUnite, "le nom Unite ne doit pas �tre vide!");
		}
		return name;
	}

	private String getSelectedcodeDepartement() throws WrongValueException {
		String name = codeDepartement.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeDepartement, "le codeDepartement ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectednomDepartement() throws WrongValueException {
		String name = nomdepatrement.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomdepatrement, "le nom Departement ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectedcodeService() throws WrongValueException {
		String name = codeService.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeService, "le codeService ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectednomService() throws WrongValueException {
		String name = NomService.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(NomService, "le nom Service ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectedcodeSection() throws WrongValueException {
		String name = codeSection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeSection, "le codeSection ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectednomSection() throws WrongValueException {
		String name = nomSection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomSection, "le nom Section ne doit pas �tre vide!");
		}
		return name;
	}
	
	public void EnregistrerDonneesRejetes(ArrayList <String> listeRejet)
	{
		java.io.InputStream is = desktop.getWebApp().getResourceAsStream("/test/download.html");
        if (is != null)
            Filedownload.save(is, "text/html", "download.html");
        else
            alert("/test/download.html not found");
	}

    /**
     * cette m�thode permet d'afficher les donn�es rejet�es et qui n'ont pas �t� int�gres dans la table
     */
    public void AfficherFenetreRejet(String listeRejet)
    {
    	Map<String, String> listDonne=new HashMap <String, String>();
		listDonne.put("rejet", listeRejet);
		
		System.out.println("rrrrr==="+listeRejet);
		Map<String, String > ll=new HashMap<String, String>();
		String ss="rrrrrrr"+System.getProperty("line.separator")+"gggggggg"; 
		ll.put("rejet", ss);
    	final Window win = (Window) Executions.createComponents("../pages/REJDATA.zul", self, listDonne);
        // We send a message to the Controller of the popup that it works in popup-mode.
        win.setAttribute("popup", true);
        
        //decoratePopup(win);
        try 
        {
            win.doModal();
           
        } 
        catch (InterruptedException ex) 
        {
           ex.printStackTrace();
        } 
        catch (SuspendNotAllowedException ex) 
        {
            ex.printStackTrace();
        }
    }

}
