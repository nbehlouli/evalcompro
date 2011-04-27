package administration.action;

import java.awt.FileDialog;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.lang.Strings;
import org.zkoss.zk.au.out.AuClearWrongValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

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
	

	AnnotateDataBinder binder;

	
	
	List<StructureEntrepriseBean> model = new ArrayList<StructureEntrepriseBean>();
	
	
	
	StructureEntrepriseBean selected;
	

	public StructureEntreprise() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// création de la structure de l'entreprise bean
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
		
		//controle d'intégrité 
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		Boolean donneeValide=structureEntrepriseModel.controleIntegrite(addedData);
		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée
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
			alert("Aucune donnée n'a été selectionnée");
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
		
		//controle d'intégrité 
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		Boolean donneeValide=structureEntrepriseModel.controleIntegrite(selected);
		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée
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
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		//suppression de la donnée supprimée de la base de donnée
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

	public void onClick$upload() {
	//excel vers BDD
		//chargement du fichier excel contenant les données
		//partie affichage
		
		//partie base de données
	}

	public void onClick$download() {
		//BDD vers excel
	
		// partie affichage
		
		//partie base de données
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
			throw new WrongValueException(codeStructure, "le Code Structure ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedcodeDivision() throws WrongValueException {
		String name = codeDivision.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeDivision, "le Code Division ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomDivision() throws WrongValueException {
		String name = nomDivision.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomDivision, "le nom Division ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedcodeDirection() throws WrongValueException {
		String name = codeDirection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeDirection, "le codeDirection ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomDirection() throws WrongValueException {
		String name = nomDirection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomDirection, "le nom Direction ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedcodeUnite() throws WrongValueException {
		String name = codeUnite.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeUnite, "le codeUnite ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomUnite() throws WrongValueException {
		String name = nomUnite.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomUnite, "le nom Unite ne doit pas être vide!");
		}
		return name;
	}

	private String getSelectedcodeDepartement() throws WrongValueException {
		String name = codeDepartement.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeDepartement, "le codeDepartement ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomDepartement() throws WrongValueException {
		String name = nomdepatrement.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomdepatrement, "le nom Departement ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedcodeService() throws WrongValueException {
		String name = codeService.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeService, "le codeService ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomService() throws WrongValueException {
		String name = NomService.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(NomService, "le nom Service ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectedcodeSection() throws WrongValueException {
		String name = codeSection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeSection, "le codeSection ne doit pas être vide!");
		}
		return name;
	}
	
	private String getSelectednomSection() throws WrongValueException {
		String name = nomSection.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nomSection, "le nom Section ne doit pas être vide!");
		}
		return name;
	}


}
