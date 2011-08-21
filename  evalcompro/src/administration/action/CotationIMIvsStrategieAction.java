package administration.action;


import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.lang.Strings;
import org.zkoss.zk.au.out.AuClearWrongValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;


import administration.bean.CotationIMIvsStrategieBean;

import administration.model.CotationIMIvsStrategieModel;


public class CotationIMIvsStrategieAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Listbox admincomptelb;
	Listbox admincomptelb1;
	


	Textbox id_cotation;
	Textbox label_cotation;
	Textbox  definition_cotation;
	Listbox valeur_cotation;
	
	
	
	//Listbox valeur_cotation;
	Textbox client_id;
	Textbox nom_client;
	Textbox  secteur_id;
	Textbox nom_secteur;
	
	Tab defBase;
	Tab baseClient;
	Component comp1;
	
	
	AnnotateDataBinder binder;
	AnnotateDataBinder binder1;

	List<CotationIMIvsStrategieBean> model = new ArrayList<CotationIMIvsStrategieBean>();
	List<CotationIMIvsStrategieBean> model1 = new ArrayList<CotationIMIvsStrategieBean>();
	CotationIMIvsStrategieBean selected;
	CotationIMIvsStrategieBean selected1;
	List list_profile=null;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button effacer;
    Map map_valeur_cotation=null;
	public CotationIMIvsStrategieAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		okAdd.setVisible(false);
		effacer.setVisible(false);
	
		CotationIMIvsStrategieModel init= new CotationIMIvsStrategieModel();
		model=init.loadCotationCompetence();
		
		map_valeur_cotation=init.list_valeursCotation();
		Set set = (map_valeur_cotation).entrySet(); 
    	Iterator i = set.iterator();
		
		// Display elements
		while(i.hasNext()) {
		Map.Entry me = (Map.Entry)i.next();
		valeur_cotation.appendItem(((Integer)me.getKey()).toString(),((Integer)me.getKey()).toString());
		//profilemodel.add((String) me.getKey());
		}
		
		
		
    	binder = new AnnotateDataBinder(comp);
		if(model.size()!=0)
			selected=model.get(0);
		
		if(admincomptelb.getItemCount()!=0)
			admincomptelb.setSelectedIndex(0);
		binder.loadAll();
		baseClient.addForward(Events.ON_CLICK, comp, "onSelectTab");
		
	}

	public List<CotationIMIvsStrategieBean> getModel() {
		return model;
	}



	public CotationIMIvsStrategieBean getSelected() {
		return selected;
	}

	public void setSelected(CotationIMIvsStrategieBean selected) {
		this.selected = selected;
	}
	
	
	public List<CotationIMIvsStrategieBean> getModel1() {
		return model1;
	}



	public CotationIMIvsStrategieBean getSelected1() {
		return selected1;
	}

	public void setSelected1(CotationIMIvsStrategieBean selected1) {
		this.selected1 = selected1;
	}
	
	
	/*public void onSelectTab(ForwardEvent event) throws SQLException
	 {
		
		DatabaseManagementModel init= new DatabaseManagementModel();
	   
		map_database=init.getListDB();
		Set set = (map_database).entrySet(); 
    	Iterator i = set.iterator();
		
		// Display elements
		while(i.hasNext()) {
		Map.Entry me = (Map.Entry)i.next();
		base_donnee.appendItem((String) me.getKey(),(String) me.getKey());
		//profilemodel.add((String) me.getKey());
		}
		
		
		model1=init.loadDatabaseClientlist();
    	binder1 = new AnnotateDataBinder(self);
		if(model.size()!=0)
			selected1=model1.get(0);
		
		if(admincomptelb1.getItemCount()!=0)
			admincomptelb1.setSelectedIndex(0);
		binder1.loadAll();
		
		
	 }*/
	public void onClick$add() throws WrongValueException, ParseException {
		
		clearFields();
		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);
		
	}
	
	public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException {
	 	
		//Tab defBase;
		//Tab baseClient;
		
		if (defBase.isSelected()){
			
			CotationIMIvsStrategieBean addedData = new CotationIMIvsStrategieBean();
			addedData.setLabel_cotation(getSelectedlabel_cotation());
			addedData.setDefinition_cotation(getSelecteddefinition_cotation());
			addedData.setValeur_cotation(getSelectedvaleur_cotation());
			//addedData.setNom_base(getSelectedBaseDonneeNom());
				
			//controle d'intégrité 
			CotationIMIvsStrategieModel compagne_model =new CotationIMIvsStrategieModel();
			//compagne_model.addCompagne(addedData);
			//Boolean donneeValide=compagne_model.controleIntegrite(addedData);
			Boolean donneeValide=true;
			
		if (donneeValide)
			{
				//insertion de la donnée ajoutée dans la base de donnée
				boolean donneeAjoute=compagne_model.addCotation(addedData);
				// raffrechissemet de l'affichage
				if (donneeAjoute )
				{
					model.add(addedData);
					selected = addedData;
					binder.loadAll();
				}
			}
		}
		/*else {
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
	
	   }*/
		okAdd.setVisible(false);
		effacer.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);
		
				
	}
	

	 public void clearFields(){
		 id_cotation.setText("");
		 label_cotation.setText("");
		 definition_cotation.setText("");
		 valeur_cotation.setSelectedIndex(0);
		
			if (baseClient.isSelected()){
				
				
					nom_client.setText("");
					secteur_id.setText("");
					nom_secteur.setText("");
				
			}
	  }

	public void onClick$update() throws WrongValueException, ParseException, InterruptedException {

		

		if (defBase.isSelected()){
			
			CotationIMIvsStrategieBean addedData = new CotationIMIvsStrategieBean();
			selected.setLabel_cotation(getSelectedlabel_cotation());
			selected.setDefinition_cotation(getSelecteddefinition_cotation());
			selected.setValeur_cotation(getSelectedvaleur_cotation());
			selected.setId_cotation(getSelecteid_cotation());
		
			//controle d'intégrité 
			CotationIMIvsStrategieModel compagne_model =new CotationIMIvsStrategieModel();
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
					compagne_model.updateCotation(selected);
					binder.loadAll();
					return;
				}
				
				else{
					return;
				}
			}	
		}
		/*else {
			
		
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
			
  }		*/
			
}

	public void onClick$delete() throws InterruptedException, SQLException, ParseException {

		if (defBase.isSelected()){
			
			CotationIMIvsStrategieModel compagne_model =new CotationIMIvsStrategieModel();			//suppression de la donnée supprimée de la base de donnée
			selected.setId_cotation(getSelecteid_cotation());
			
			if (Messagebox.show("Voulez vous supprimer cette base de données?", "Prompt", Messagebox.YES|Messagebox.NO,
				    Messagebox.QUESTION) == Messagebox.YES) {
				    //System.out.println("pressyes");
				compagne_model.deleteCotation(selected);
				model.remove(selected);
				selected = null;
				binder.loadAll();
				return;
			}
			
			else{
				return;
			}
			
		}
		
		/*else {
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
	  }*/
		
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
		closeErrorBox(new Component[] { id_cotation,label_cotation,definition_cotation, valeur_cotation});
	}
	
/*
	public void onSelect$admincomptelb1() {
		closeErrorBox(new Component[] { client_id,nom_client,secteur_id, nom_secteur, base_donnee});
	}
	*/
	
	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}



	private Integer getSelecteid_cotation() throws WrongValueException {
		Integer name =  Integer.parseInt((String)id_cotation.getValue());
		if (name==null) {
			throw new WrongValueException(id_cotation, "Merci de saisie un id cotation!");
		}
		return name;
	}
	
	private String getSelecteddefinition_cotation() throws WrongValueException {
		String name = definition_cotation.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(definition_cotation, "Merci de saisir un label!");
		}
		return name;
	}
	
	private String getSelectedlabel_cotation() throws WrongValueException {
		String name = label_cotation.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(label_cotation, "Merci de saisir un label!");
		}
		return name;
	}
	
	
   
   private String getSelectedvaleur_cotation() throws WrongValueException {
		String name= (String) valeur_cotation.getSelectedItem().getValue();
		if (name==null) {
			throw new WrongValueException(valeur_cotation, "Merci de choisir une valeur cotation!");
		}
		return name;
	}
   
  

}