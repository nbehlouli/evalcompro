package administration.action;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.lang.Strings;
import org.zkoss.zk.au.out.AuClearWrongValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import administration.bean.AdministrationLoginBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.AdministrationLoginModel;
import administration.model.StructureEntrepriseModel;

public class AdministrationLoginAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox admincomptelb;
	Textbox  nom;
	Textbox  prenom;
	Textbox  login;
	Textbox  motdepasse;
	Listbox  profile;
	Listbox  basedonnee;
	Textbox date_deb_val;
	Textbox date_fin_val;
	Textbox datemodifpwd;
	AnnotateDataBinder binder;
	List<AdministrationLoginBean> model = new ArrayList<AdministrationLoginBean>();
	AdministrationLoginBean selected;
	List list_profile=null;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button effacer;

	public AdministrationLoginAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		okAdd.setVisible(false);
		effacer.setVisible(false);
		AdministrationLoginModel init= new AdministrationLoginModel();
		
		
		Set set = (init.gerProfileList()).entrySet(); 
		Set sec= (init.getDatabaseList()).entrySet();
		
		Iterator i = set.iterator();
		Iterator i1 = sec.iterator();
		
		// Display elements
		while(i.hasNext()) {
		Map.Entry me = (Map.Entry)i.next();
		profile.appendItem((String) me.getKey(),(String) me.getKey());
		}
		// Display elements
		while(i1.hasNext()) {
		Map.Entry me = (Map.Entry)i1.next();
		basedonnee.appendItem((String) me.getKey(),(String) me.getKey());
		}
		// cr�ation de la structure de l'entreprise bean
		AdministrationLoginModel admin_compte =new AdministrationLoginModel();
		model=admin_compte.checkLoginBean();
		
		comp.setVariable(comp.getId() + "Ctrl", this, true);

		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
	}

	public List<AdministrationLoginBean> getModel() {
		return model;
	}



	public AdministrationLoginBean getSelected() {
		return selected;
	}

	public void setSelected(AdministrationLoginBean selected) {
		this.selected = selected;
	}

	public void onClick$add() throws WrongValueException, ParseException {
		
		clearFields();
		date_deb_val.setText("2011/12/01");
		date_fin_val.setText("2011/12/01");
		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);
		upload.setVisible(false);
		download.setVisible(false);
	}
	
	public void onClick$okAdd()throws WrongValueException, ParseException {
	 	
		AdministrationLoginBean addedData = new AdministrationLoginBean();
	
		addedData.setNom(getSelectedNom());
		addedData.setPrenom(getSelectedPrenom());
		addedData.setLogin(getSelectedLogin());
		addedData.setMotdepasse(getSelectedcodemotdepasse());
		addedData.setProfile(getSelectedprofile());
		addedData.setBasedonnee(getSelectedbasedonnee());
		addedData.setDate_deb_val( getSelecteddate_deb_val());
		addedData.setDate_fin_val( getSelecteddate_fin_val());
	
		//controle d'int�grit� 
		AdministrationLoginModel admini_login_model =new AdministrationLoginModel();
		Boolean donneeValide=admini_login_model.controleIntegrite(addedData);
		//Boolean donneeValide=true;
		
		if (donneeValide)
		{
			//insertion de la donn�e ajout�e dans la base de donn�e
			boolean donneeAjoute=admini_login_model.addAdministrationLoginBean(addedData);
			// raffrechissemet de l'affichage
			if (donneeAjoute )
			{
				model.add(addedData);
			
				selected = addedData;
			
				binder.loadAll();
			}
		}
		okAdd.setVisible(false);
		effacer.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);
		upload.setVisible(true);
		download.setVisible(true);
				
	}

	/*public void onClick$update() {
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
	}*/

	/*public void onClick$delete() {
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
*/
	public void onClick$effacer()  {
		
	
		clearFields();
		okAdd.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);
		upload.setVisible(true);
		download.setVisible(true);
		
	}

	public void onClick$upload() {
	//excel vers BDD
		//chargement du fichier excel contenant les donn�es
		//partie affichage
		
		//partie base de donn�es
	}

	public void onClick$download() {
		//BDD vers excel
	
		// partie affichage
		
		//partie base de donn�es
	}
	
	public void onSelect$admincomptelb() {
		closeErrorBox(new Component[] { nom, prenom,login,motdepasse,  profile, 
				basedonnee,date_deb_val, date_fin_val, datemodifpwd });
	}
	
	
	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}




	

	private String getSelectedNom() throws WrongValueException {
		String name = nom.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nom, "le nom de l'utilisateur ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectedPrenom() throws WrongValueException {
		String name = prenom.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(prenom, "le pr�nom de l'utilisateur ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectedLogin() throws WrongValueException {
		String name = login.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(login, "le login ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectedcodemotdepasse() throws WrongValueException {
		String name = motdepasse.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(motdepasse, "le mot de passe ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectedprofile() throws WrongValueException {
		String name = profile.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(profile, "le profile  ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelectedbasedonnee() throws WrongValueException {
		String name = basedonnee.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(basedonnee, "la basedonnee ne doit pas �tre vide!");
		}
		return name;
	}
	
	private String getSelecteddate_deb_val() throws WrongValueException, ParseException {
		//DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_deb_val.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_deb_val, "la date debut validit� ne doit pas �tre vide!");
		}
		return name;
	}

	private String getSelecteddate_fin_val() throws WrongValueException, ParseException {
		
		String name = date_fin_val.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_fin_val, "la date fin validit� ne doit pas �tre vide!");
		}
		return name;
	}
	
	
  public void clearFields(){
	    nom.setText("");
		prenom.setText("");
		login.setText("");
		motdepasse.setText("");
		//profile.setItemRenderer("");
		//basedonnee.setText("");
		date_deb_val.setText("");
		date_fin_val.setText("");
		datemodifpwd.setText("");
		
  }

}