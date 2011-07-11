package compagne.action;

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

import org.codehaus.groovy.tools.shell.commands.SetCommand;
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
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;

import compagne.bean.PlanningCompagneListBean;
import compagne.model.CompagneCreationModel;
import compagne.model.PlanningCompagneModel;

import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.AdministrationLoginModel;
import administration.model.StructureEntrepriseModel;

public class PlanningCompagneAction extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	Listbox admincomptelb;
	Textbox id_planning;
	Listbox nom_compagne;
	Listbox Evaluateur;
	Listbox evalue;
	Listbox code_poste;
	Listbox  code_structure;
	Datebox date_evaluation;
	Listbox heure_debut_evaluation;
	Listbox heure_fin_evaluation;
	Textbox lieu;
	Textbox  personne_ressources;
	private String lbl_compagne;
	private String lbl_evaluateur;
	private String lbl_evalue;
	private String lbl_poste;

	AnnotateDataBinder binder;
	List<PlanningCompagneListBean> model = new ArrayList<PlanningCompagneListBean>();
	PlanningCompagneListBean selected;
	Button add;
	Button okAdd;
	Button update;
	Button delete;
	Button upload;
	Button download;
	Button effacer;
	Map map_nomcomp=null;
	Map map_evaluateur=null;
	Map map_evalue=null;
	Map map_evalue_sorted=null;
	Map map_heuredebut=null;
	Map map_heurefin=null;
	Map map_poste= new HashMap();
	Map map_structure=null;
	 
	 
	public PlanningCompagneAction() {
	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		okAdd.setVisible(false);
		effacer.setVisible(false);
		PlanningCompagneModel init= new PlanningCompagneModel();
		map_nomcomp = new HashMap();
		map_nomcomp=init.getCompagneValid();
  		Set set = (map_nomcomp).entrySet(); 
		Iterator i = set.iterator();
		  
	
		// Display elements
		while(i.hasNext()) {
		Map.Entry me = (Map.Entry)i.next();
		nom_compagne.appendItem((String) me.getKey(),(String) me.getKey());
		}
		
	
		map_evaluateur=init.getListEvaluteur();
  		set = (map_evaluateur).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
		Map.Entry me = (Map.Entry)i.next();
		Evaluateur.appendItem((String) me.getKey(),(String) me.getKey());
		}
			
		
		map_evalue=init.getListEvalue();
	  	set = (map_evalue).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
		  Map.Entry me = (Map.Entry)i.next();
		  evalue.appendItem((String) me.getKey(),(String) me.getKey());
	   }
	
	
		map_heuredebut=init.getHeureDebut();  
	  	set = (map_heuredebut).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
		  Map.Entry me = (Map.Entry)i.next();
		  heure_debut_evaluation.appendItem((String) me.getKey(),(String) me.getKey());
	   }
			
		map_heurefin=init.getHeureFin();
	  	set = (map_heurefin).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
		  Map.Entry me = (Map.Entry)i.next();
		  heure_fin_evaluation.appendItem((String) me.getKey(),(String) me.getKey());
	   }
	
		map_poste=init.getListPoste();
	  	set = (map_poste).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
		  Map.Entry me = (Map.Entry)i.next();
		  code_poste.appendItem((String) me.getKey(),(String) me.getKey());
	   }
		
		map_structure=init.getListStructure();
	  	set = (map_structure).entrySet(); 
		i = set.iterator();
		// Display elements
		while(i.hasNext()) {
		  Map.Entry me = (Map.Entry)i.next();
		  code_structure.appendItem((String) me.getKey(),(String) me.getKey());
	   }
		
		
	    model=init.loadPlanningCompagnelist();
    	binder = new AnnotateDataBinder(comp);
		if(model.size()!=0)
			selected=model.get(0);
		
		if(admincomptelb.getItemCount()!=0)
			admincomptelb.setSelectedIndex(0);
		binder.loadAll();
		
	}

	public List<PlanningCompagneListBean> getModel() {
		return model;
	}



	public PlanningCompagneListBean getSelected() {
		return selected;
	}

	public void setSelected(PlanningCompagneListBean selected) {
		this.selected = selected;
	}

	public void onClick$add() throws WrongValueException, ParseException {
		
		clearFields();
		
		nom_compagne.setSelectedIndex(0);
		Evaluateur.setSelectedIndex(0);
		evalue.setSelectedIndex(0);
		heure_debut_evaluation.setSelectedIndex(0);
		heure_fin_evaluation.setSelectedIndex(1);
		code_poste.setSelectedIndex(0);
		code_structure.setSelectedIndex(0);
		
		okAdd.setVisible(true);
		effacer.setVisible(true);
		add.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);
	
	}
	
	public void onClick$okAdd()throws WrongValueException, ParseException, InterruptedException {
	 	
		PlanningCompagneListBean addedData = new PlanningCompagneListBean();
		addedData.setId_compagne(getSelectedIdCompagne());
		addedData.setId_evalue(getSelectedEvalue());
		addedData.setDate_evaluation(getSelectDateEvaluation());
		addedData.setId_evaluateur(getSelectedEvaluateur());
		addedData.setHeure_debut_evaluation(getSelectedHeureDeb());
		addedData.setHeure_fin_evaluation(getSelectedHeureFin());
		addedData.setLieu(getSelectedLieu());
		addedData.setCode_poste(getSelectPosteTravail());
		addedData.setPersonne_ressources(getSelectedPersonneRessources());
		addedData.setCode_structure(getSelectStructure());
		addedData.setLibelle_compagne(getLbl_compagne());
		addedData.setEvaluateur(getLbl_evaluateur());
		addedData.setEvalue(getLbl_evalue());
		addedData.setIntitule_poste(getLbl_poste());
	
		//controle d'int�grit� 
		PlanningCompagneModel compagne_model =new PlanningCompagneModel();
		//compagne_model.addPlanningCompagne(addedData);
		Boolean donneeValide=compagne_model.controleIntegrite(addedData,map_heuredebut,map_heurefin);
		//Boolean donneeValide=true;
		
	if (donneeValide)
		{
			//insertion de la donn�e ajout�e dans la base de donn�e
			boolean donneeAjoute=compagne_model.addPlanningCompagne(addedData);
			// raffrechissemet de l'affichage
			if (donneeAjoute )
			{
				model.add(addedData);
				selected = addedData;
			    binder.loadAll();
			
			}
		}
	
	    fillEvalueListBox();
 
		okAdd.setVisible(false);
		effacer.setVisible(false);
		add.setVisible(true);
		update.setVisible(true);
		delete.setVisible(true);
		
				
	}

	/**
	 * 
	 */
	public void fillEvalueListBox() {
		evalue.getItems().clear();
	    Set set = (map_evalue).entrySet(); 
	  	Iterator i = set.iterator();
		while(i.hasNext()) {
		  Map.Entry me = (Map.Entry)i.next();
		  evalue.appendItem((String) me.getKey(),(String) me.getKey());
		 }
	}
	
	public void onClick$update() throws WrongValueException, ParseException, InterruptedException {
		if (selected == null) {
			alert("Aucune donn�e n'a �t� selectionn�e");
			return;
		}
		PlanningCompagneListBean addedData = new PlanningCompagneListBean();
		selected.setId_compagne(getSelectedIdCompagne());
		selected.setId_evalue(getSelectedEvalue());
		selected.setDate_evaluation(getSelectDateEvaluation());
		selected.setId_evaluateur(getSelectedEvaluateur());
		selected.setHeure_debut_evaluation(getSelectedHeureDeb());
		selected.setHeure_fin_evaluation(getSelectedHeureFin());
		selected.setLieu(getSelectedLieu());
		selected.setCode_poste(getSelectPosteTravail());
		selected.setPersonne_ressources(getSelectedPersonneRessources());
		selected.setCode_structure(getSelectStructure());
		selected.setLibelle_compagne(getLbl_compagne());
		selected.setEvaluateur(getLbl_evaluateur());
		selected.setEvalue(getLbl_evalue());
		selected.setIntitule_poste(getLbl_poste());
		selected.setId_planning_evaluation(getSelectedIdplanning());
	
	
		//controle d'int�grit� 
		PlanningCompagneModel compagne_model =new PlanningCompagneModel();
		//controle d'int�grit� 
		Boolean donneeValide=compagne_model.controleIntegrite(selected,map_heuredebut,map_heurefin);
		if (donneeValide)
		{
			//insertion de la donn�e ajout�e dans la base de donn�e
			
			if (Messagebox.show("Voulez vous appliquer les modifications?", "Prompt", Messagebox.YES|Messagebox.NO,
				    Messagebox.QUESTION) == Messagebox.YES) {
				    //System.out.println("pressyes");
				compagne_model.updateCompagne(selected);
				binder.loadAll();
				return;
			}
			
			else{
				return;
			}
		}	
			
			
			
	}
	
	public void onClick$delete() throws InterruptedException {
		if (selected == null) {
			alert("Aucune donn�e n'a �t� selectionn�e");
			return;
		}
		PlanningCompagneModel compagne_model =new PlanningCompagneModel();
		//suppression de la donn�e supprim�e de la base de donn�e
		selected.setId_planning_evaluation(getSelectedIdplanning()); 
		
		if (Messagebox.show("Voulez vous supprimer cet compagne?", "Prompt", Messagebox.YES|Messagebox.NO,
			    Messagebox.QUESTION) == Messagebox.YES) {
			    //System.out.println("pressyes");
			compagne_model.deleteCompagne(selected);
			model.remove(selected);
			binder.loadAll();
			selected=model.get(model.size()-1);
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
		
		
	}

		
	public void onSelect$admincomptelb() {
		closeErrorBox(new Component[] { nom_compagne, Evaluateur, code_poste,evalue,code_structure,date_evaluation,
				heure_debut_evaluation,heure_fin_evaluation,lieu,personne_ressources});
	}
	
	
	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}


	private int getSelectedIdCompagne() throws WrongValueException {
		
		Integer name=(Integer) map_nomcomp.get((String)nom_compagne.getSelectedItem().getLabel());
		setLbl_compagne((String)nom_compagne.getSelectedItem().getLabel());
	
		if (name== null) {
			throw new WrongValueException(nom_compagne, "Merci de saisie un nom de compagne!");
		}
		return name;
	}
	
    private int getSelectedEvaluateur() throws WrongValueException {
		
		Integer name=(Integer) map_evaluateur.get((String)Evaluateur.getSelectedItem().getLabel());
		setLbl_evaluateur((String)Evaluateur.getSelectedItem().getLabel());
	
		if (name== null) {
			throw new WrongValueException(Evaluateur, "Merci de saisir un �valuateur!");
		}
		return name;
	}
    
 private Integer getSelectedIdplanning() throws WrongValueException {
		
		Integer name= Integer.parseInt(id_planning.getValue());
		
		if (name== null) {
			throw new WrongValueException(Evaluateur, "Merci de saisir un �valuateur!");
		}
		return name;
	}
 
   private int getSelectedEvalue() throws WrongValueException {
		
		Integer name=(Integer) map_evalue.get((String)evalue.getSelectedItem().getLabel());
		setLbl_evalue((String)evalue.getSelectedItem().getLabel());
	
		if (name== null) {
			throw new WrongValueException(evalue, "Merci de saisir un �value!");
		}
		return name;
	}

	private String getSelectPosteTravail() throws WrongValueException {
		String name = (String) map_poste.get((String)code_poste.getSelectedItem().getLabel());
		setLbl_poste((String)code_poste.getSelectedItem().getLabel());
		if (Strings.isBlank(name)) {
			throw new WrongValueException(code_poste, "Merci de saisir un poste de travail!");
		}
		return name;
	}
	
	private String getSelectStructure() throws WrongValueException {
		String name = code_structure.getSelectedItem().getLabel();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(code_structure, "Merci de saisir une structure!");
		}
		return name;
	}
	
	private Date getSelectDateEvaluation() throws WrongValueException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String name = date_evaluation.getText();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(date_evaluation, "Merci de saisir une date d'�valuation!");
		}
		Date datedeb = df.parse(name);
		return datedeb;
	}
	
	private String getSelectedHeureDeb() throws WrongValueException {
		String name=heure_debut_evaluation.getSelectedItem().getLabel();
		if (name==null) {
			throw new WrongValueException(heure_debut_evaluation, "Merci de choisir  une heure de d�but d'�valuation!");
		}
		return name;
	}
	
	private String getSelectedHeureFin() throws WrongValueException {
		String name=heure_fin_evaluation.getSelectedItem().getLabel();
		if (name==null) {
			throw new WrongValueException(heure_fin_evaluation, "Merci de choisir  une heure de fin d'�valuation!");
		}
		return name;
	}
	
	private String getSelectedLieu() throws WrongValueException {
		String name=lieu.getValue();
		if (name==null) {
			throw new WrongValueException(lieu, "Merci de saisir le lieu de l'�valuation!");
		}
		return name;
	}
	
	private String getSelectedPersonneRessources() throws WrongValueException {
		String name=personne_ressources.getValue();
		if (name==null) {
			throw new WrongValueException(personne_ressources, "Merci de saisir la personne ressources!");
		}
		return name;
	}
	
  public void clearFields(){
	
	   lieu.setText("");
	   personne_ressources.setText("");
	  			
  }

public String getLbl_compagne() {
	return lbl_compagne;
}

public void setLbl_compagne(String lbl_compagne) {
	this.lbl_compagne = lbl_compagne;
}

public String getLbl_evaluateur() {
	return lbl_evaluateur;
}

public void setLbl_evaluateur(String lbl_evaluateur) {
	this.lbl_evaluateur = lbl_evaluateur;
}

public String getLbl_evalue() {
	return lbl_evalue;
}

public void setLbl_evalue(String lbl_evalue) {
	this.lbl_evalue = lbl_evalue;
}

public String getLbl_poste() {
	return lbl_poste;
}

public void setLbl_poste(String lbl_poste) {
	this.lbl_poste = lbl_poste;
}
  
  
  public void onSelect$code_poste() throws SQLException, InterruptedException {
	  PlanningCompagneModel init =new PlanningCompagneModel();
	  evalue.getItems().clear();
	  
	  String poste= (String) map_poste.get(code_poste.getSelectedItem().getLabel());
	  map_evalue_sorted= init.selectEmployes(poste);
	  
	  if (map_evalue_sorted.size()==0){
		  String message="Aucun  �valu�  n'occupe le poste: "+code_poste.getSelectedItem().getLabel();
		  Messagebox.show(message, "Erreur",Messagebox.OK, Messagebox.ERROR);
		  return;
	  }
	  Set set = (map_evalue_sorted).entrySet(); 
	  Iterator i = set.iterator();
	  	
		// Display elements
		while(i.hasNext()) {
		  Map.Entry me = (Map.Entry)i.next();
		  evalue.appendItem((String) me.getKey(),(String) me.getKey());
		 }
	 
		 evalue.setSelectedIndex(0);
  }

  
  
  
}