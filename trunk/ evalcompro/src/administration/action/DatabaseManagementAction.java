package administration.action;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import compagne.model.CompagneCreationModel;

import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.DatabaseManagementBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.AdministrationLoginModel;
import administration.model.DatabaseManagementModel;
import administration.model.StructureEntrepriseModel;

public class DatabaseManagementAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox admincomptelb;
	
	
	Textbox database_id;
	Textbox nom_base;
	Textbox  login;
	Textbox pwd;
	Textbox adresse_ip;
	Textbox  nom_instance;
	
	
	
	AnnotateDataBinder binder;
	List<DatabaseManagementBean> model = new ArrayList<DatabaseManagementBean>();
	DatabaseManagementBean selected;
	List list_profile=null;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button effacer;
	 Map map=null;
	public DatabaseManagementAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		okAdd.setVisible(false);
		effacer.setVisible(false);
		DatabaseManagementModel init= new DatabaseManagementModel();
	  
		model=init.loadDatabaselist();
    	binder = new AnnotateDataBinder(comp);
		if(model.size()!=0)
			selected=model.get(0);
		
		if(admincomptelb.getItemCount()!=0)
			admincomptelb.setSelectedIndex(0);
		binder.loadAll();
		
	}

	public List<DatabaseManagementBean> getModel() {
		return model;
	}



	public DatabaseManagementBean getSelected() {
		return selected;
	}

	public void setSelected(DatabaseManagementBean selected) {
		this.selected = selected;
	}

	public void onClick$add() throws WrongValueException, ParseException {
		
		clearFields();
		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);
		
	}
	
	public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException {
	 	
		DatabaseManagementBean addedData = new DatabaseManagementBean();
	
		addedData.setNom_base(getSelectednom_base());
		addedData.setLogin(getSelectedlogin());
		addedData.setPwd(getSelectedPwd());
		addedData.setAdresse_ip(getSelectedAdresse_ip());
		addedData.setNom_instance(getSelectedNom_instance());
	
		//controle d'intégrité 
		DatabaseManagementModel compagne_model =new DatabaseManagementModel();
		//compagne_model.addCompagne(addedData);
		//Boolean donneeValide=compagne_model.controleIntegrite(addedData);
		Boolean donneeValide=true;
		
	if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée
			boolean donneeAjoute=compagne_model.addDatabase(addedData);
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
		
				
	}
	
	 public void clearFields(){
			database_id.setText("");
			nom_base.setText("");
			login.setText("");
			pwd.setText("");
			adresse_ip.setText("");
			nom_instance.setText("");
	  }

	public void onClick$update() throws WrongValueException, ParseException, InterruptedException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		DatabaseManagementBean addedData = new DatabaseManagementBean();
		
		selected.setDatabase_id(getSelectedIdbase());
		selected.setNom_base(getSelectednom_base());
		selected.setLogin(getSelectedlogin());
		selected.setPwd(getSelectedPwd());
		selected.setAdresse_ip(getSelectedAdresse_ip());
		selected.setNom_instance(getSelectedNom_instance());
	
		//controle d'intégrité 
		DatabaseManagementModel compagne_model =new DatabaseManagementModel();
		//compagne_model.addCompagne(addedData);
		//Boolean donneeValide=compagne_model.controleIntegrite(addedData);
		Boolean donneeValide=true;
		//controle d'intégrité 
		//Boolean donneeValide=compagne_model.controleIntegrite(selected);
		if (donneeValide)
		{
			//insertion de la donnée ajoutée dans la base de donnée
			
			if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
				    Messagebox.QUESTION) == Messagebox.YES) {
				    //System.out.println("pressyes");
				compagne_model.updateDatabase(selected);
				binder.loadAll();
				return;
			}
			
			else{
				return;
			}
		}	
			
			
			
	}

	public void onClick$delete() throws InterruptedException, SQLException {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		DatabaseManagementModel compagne_model =new DatabaseManagementModel();
		//suppression de la donnée supprimée de la base de donnée
		selected.setDatabase_id(getSelectedIdbase());
		
		if (Messagebox.show("Voulez vous supprimer cette base de données?", "Prompt", Messagebox.YES|Messagebox.NO,
			    Messagebox.QUESTION) == Messagebox.YES) {
			    //System.out.println("pressyes");
			compagne_model.deleteDatabase(selected);
			model.remove(selected);
			selected = null;
			binder.loadAll();
			return;
		}
		
		else{
			return;
		}
		
		
		

		
	}

	public void onClick$effacer()  {
		
	
		clearFields();
		okAdd.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);
		admincomptelb.setSelectedIndex(0);
		binder.loadAll();
		
		
	}

	
	public void onSelect$admincomptelb() {
		closeErrorBox(new Component[] { database_id,nom_base,login, pwd, adresse_ip,nom_instance});
	}
	
	
	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}



	private Integer getSelectedIdbase() throws WrongValueException {
		Integer name =  Integer.parseInt(database_id.getValue());
		if (name==null) {
			throw new WrongValueException(database_id, "Merci de saisie un id database!");
		}
		return name;
	}
	
	private String getSelectednom_base() throws WrongValueException {
		String name = nom_base.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nom_base, "Merci de saisir un nom de base de données!");
		}
		return name;
	}
	
	private String getSelectedlogin() throws WrongValueException {
		String name = login.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(login, "Merci de saisir un login!");
		}
		return name;
	}
	private String getSelectedPwd() throws WrongValueException {
		String name = pwd.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(pwd, "Merci de saisir un login!");
		}
		return name;
	}
	
	private String getSelectedAdresse_ip() throws WrongValueException {
		String name = adresse_ip.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(adresse_ip, "Merci de saisir une adresse IP!");
		}
		return name;
	}
	
	
	private String getSelectedNom_instance() throws WrongValueException {
		String name = nom_instance.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(nom_instance, "Merci de saisir un nom instance!");
		}
		return name;
	}
	
	


}