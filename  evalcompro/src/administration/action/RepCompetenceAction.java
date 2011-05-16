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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import administration.bean.AdministrationLoginBean;
import administration.bean.RepCompetenceBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.AdministrationLoginModel;
import administration.model.RepCompetenceModel;
import administration.model.StructureEntrepriseModel;

public class RepCompetenceAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox admincomptelb;
	Textbox id_repertoire_competence;
	Textbox  code_famille;
	Textbox  famille;
	Textbox  code_groupe;
	Textbox  groupe;
	Textbox  profile;
	Textbox  code_competence;
	Textbox libelle_competence;
	Textbox definition_competence;
	Textbox aptitude_observable;
	Textbox affichable;
	
		
	AnnotateDataBinder binder;
	List<RepCompetenceBean> model = new ArrayList<RepCompetenceBean>();
	RepCompetenceBean selected;
	List list_profile=null;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button effacer;
	
	public RepCompetenceAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		okAdd.setVisible(false);
		effacer.setVisible(false);
			
		// cr�ation de la structure de l'entreprise bean
		RepCompetenceModel rep_comp =new RepCompetenceModel();
		model=rep_comp.loadRepCompetence();
		binder = new AnnotateDataBinder(comp);
		
		
		//ces trois instructions permettent de selection le premier element de la listebox
		selected=model.get(0);
		binder.loadAll();
		
		admincomptelb.setSelectedIndex(0);
		
	}

	public List<RepCompetenceBean> getModel() {
		return model;
	}



	public RepCompetenceBean getSelected() {
		return selected;
	}

	public void setSelected(RepCompetenceBean selected) {
		this.selected = selected;
	}

	/*public void onClick$add() throws WrongValueException, ParseException {
		
		clearFields();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String datecuurent = sdf.format(new Date());
		profile.setSelectedIndex(0);
		basedonnee.setSelectedIndex(0);
		
		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);
		
	}
	*/
	/*public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException {
	 	
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
		
				
	}*/

	/*public void onClick$update() throws WrongValueException, ParseException, InterruptedException {
		if (selected == null) {
			alert("Aucune donn�e n'a �t� selectionn�e");
			return;
		}
		//String codeStructureselectione=selected.getCodestructure();
		//System.out.println(getSelectedcodeStructure());
		selected.setId_compte(getSelectedIdCompte());
		selected.setNom(getSelectedNom());
		selected.setPrenom(getSelectedPrenom());
		selected.setLogin(getSelectedLogin());
		selected.setMotdepasse(getSelectedcodemotdepasse());
		selected.setProfile(getSelectedprofile());
		selected.setBasedonnee(getSelectedbasedonnee());
		selected.setDate_deb_val( getSelecteddate_deb_val());
		selected.setDate_fin_val( getSelecteddate_fin_val());
		
		//controle d'int�grit� 
		AdministrationLoginModel admini_login_model =new AdministrationLoginModel();
		Boolean donneeValide=admini_login_model.controleIntegrite(selected);
		if (donneeValide)
		{
			//insertion de la donn�e ajout�e dans la base de donn�e
			
			if (Messagebox.OK == Messagebox.show("Voulez appliquer les modifications?", "Question",
					Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION)) {
				boolean donneeAjoute=admini_login_model.majAdminLoginBean(selected);
				// raffrechissemet de l'affichage
				if (donneeAjoute )
				{
					binder.loadAll();
				}
			}
				return;
			} else {
				
				return;
			}

			
	}

	public void onClick$delete() throws InterruptedException {
		if (selected == null) {
			alert("Aucune donn�e n'a �t� selectionn�e");
			return;
		}
		AdministrationLoginModel admini_login_model =new AdministrationLoginModel();
		//suppression de la donn�e supprim�e de la base de donn�e
		
		if (Messagebox.OK == Messagebox.show("Voulez vous supprimer cet utilisateur?", "Question",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION)) {
			admini_login_model.supprimerLogin(selected);
			model.remove(selected);
			selected = null;
			binder.loadAll();
			return;
		} else {
			
			return;
		}

		
	}

	public void onClick$effacer()  {
		
	
		clearFields();
		okAdd.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);
		
		
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
	}*/
	
	/*public void onSelect$admincomptelb() {
		closeErrorBox(new Component[] { nom, prenom,login,motdepasse,  profile, 
				basedonnee,date_deb_val, date_fin_val, datemodifpwd });
	}
	
	
	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}


	private String getSelectedIdCompte() throws WrongValueException {
		String name = id_compte.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nom, "id compte ne doit pas �tre vide!");
		}
		return name;
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
	
	private Date getSelecteddate_deb_val() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_deb_val.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_deb_val, "la date debut validit� ne doit pas �tre vide!");
		}
	   Date datedeb = df.parse(name); 
		return datedeb;
	}

	private Date getSelecteddate_fin_val() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_fin_val.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_fin_val, "la date fin validit� ne doit pas �tre vide!");
		}
		 Date datefin = df.parse(name); 
		return datefin;
	}
	
	
  public void clearFields(){
	    nom.setText("");
		prenom.setText("");
		login.setText("");
		motdepasse.setText("");
		//profile.setItemRenderer("");
		//basedonnee.setText("");
		datemodifpwd.setText("");
		
  }*/

}