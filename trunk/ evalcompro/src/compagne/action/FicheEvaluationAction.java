package compagne.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;


import administration.bean.CompteBean;
import administration.model.CompetencePosteTravailModel;

import common.ApplicationFacade;
import compagne.bean.EmployesAEvaluerBean;
import compagne.bean.FicheEvaluationBean;
import compagne.bean.MapEmployesAEvaluerBean;
import compagne.model.FicheEvaluationModel;

public class FicheEvaluationAction extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Tab FicheEvaluation;
	Tab AEvaluer;
	Tabpanel maFiche;
	Tabpanel evaluations;
	Html html;
	Tabbox tb;
	Textbox posteTravail;
	Textbox nomEmploye;
	Textbox groupe;
	Combobox employe; 
	Combobox poste_travail;
	Combobox Famille;
	Button valider;
	Listbox employelb;
	//objets a utiliser
	
	HashMap<String, ArrayList<FicheEvaluationBean>> mapPosteTravailFiche;
	HashMap <String, String > mapintitule_codeposte;
	HashMap <String, ArrayList<Listitem>> mapItemsFamille =new HashMap<String, ArrayList<Listitem>>();
	
	ArrayList<Listitem> currentListItem=null;
	
	// objets de la selection en cours
	String selectedEmploye;
	MapEmployesAEvaluerBean mapEmployeAEvaluerBean;
	String selectedFamille;
	String selectednomposteTravail;
	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		
		//recup�ration du profil de l'utilisateur
		CompteBean compteUtilisateur=ApplicationFacade.getInstance().getCompteUtilisateur();
		
		//r�cup�ration de l'id_employ� associ� � l'id_compte
		FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
		int id_employe=ficheEvaluationModel.getIdEmploye(compteUtilisateur.getId_compte());
		compteUtilisateur.setId_employe(id_employe);
		
		//r�cup�ration de l'information sur la validit� de la fiche de l'employ� connect�
		boolean ficheValide=ficheEvaluationModel.getValiditeFiche(id_employe);
		
		//r�cup�ration des informations associ�es � une fiche d'evaluation � remplir;
		
		 mapPosteTravailFiche=ficheEvaluationModel.getInfosFicheEvaluationparPoste();
		 CompetencePosteTravailModel compt=new CompetencePosteTravailModel();
		 mapintitule_codeposte=compt.getlistepostesCode_postes();
		//onglet Ma Fiche d'�valuation
		
		evaluations.setStyle("overflow:auto");
		posteTravail.setDisabled(true);
		nomEmploye.setDisabled(true);

		if(ficheValide)
		{
			
			//construction et affichage du contenu de la page
			
		}
		else
		{
			//enlever ce qui existe et afficher la page HTML 
			//affichage d'un message disant que la fiche ne peut �tre visible
			html=new Html();
			String content="Vous n'avez pas acc�s � votre fiche d'�valuation car elle n'a pas encore �t� compl�t� par l'�valuateur";
			html.setStyle("color:red;margin-left:15px");
			html.setContent(content);
			html.setParent(maFiche);
		}
		//si c'est un �valuateur alors on affiche la liste des fiches associ�s aux employ�s � �valuer
		if(compteUtilisateur.getId_profile()==3)
		{
			
			//remplissage du contenu de la combo associ�e aux postes de travail
			 mapEmployeAEvaluerBean=ficheEvaluationModel.getListEmployesAEvaluer(id_employe);
			 HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeAEvaluerBean.getMapclesposte();
			Set <String>listePoste= Mapclesposte.keySet();
			Iterator <String > iterator=listePoste.iterator();
			poste_travail.appendItem("Tous poste de travail");
			while(iterator.hasNext())
			{
				String nomPoste=iterator.next();
				poste_travail.appendItem(nomPoste);
			}
			//selection du premier item (tous poste de travail)
			poste_travail.setSelectedIndex(0);
			
			//remplissage de la comboBox avec tous les nom des employes quelque soit leur type de poste
			HashMap<String, EmployesAEvaluerBean> mapclesEmploye=mapEmployeAEvaluerBean.getMapclesnomEmploye();
			Set <String>listeEmploye=mapclesEmploye.keySet();
			iterator=listeEmploye.iterator();
			employe.appendItem("s�lectionner un employ�");
			while(iterator.hasNext())
			{
				String nomEmploye=iterator.next();
				employe.appendItem(nomEmploye);
			}
			//selection du premier item de la combobox employe
			if(employe.getItemCount()>0)
				employe.setSelectedIndex(0);
			
		}
		else //ne pas afficher cet onglet
		{
			
			/**
			 * TODO ne rendre cette page invisible que sur certaines conditions
			 */
			//AEvaluer.setVisible(false);
			AEvaluer.detach();
			evaluations.detach();

		}
		tb.setSelectedIndex(0);
		//griser le bouton valider
		valider.setDisabled(true);
	}
	
	/**
	 * evenement a gerer lors de la selection d'un employ�
	 */
	 public void onSelect$employe()
	 {
		 
		 //vider le contenu de la grille associ�e � l'ancien employ� selectionn�
		 if (currentListItem!=null)
		 {
			 Iterator<Listitem> iterator=currentListItem.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
			 selectedEmploye="";
			 nomEmploye.setText(selectedEmploye);
			 selectednomposteTravail="";
			 posteTravail.setText(selectednomposteTravail);
		 }
		 //si un employ� a d�ja �t� selectionner alors vider le contnu de la combobox
		 if(Famille.getItemCount()>0)
		 {
			int nb= Famille.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{
				
				Famille.removeItemAt(i);
			}
		 }
		
		 mapItemsFamille=new 	HashMap<String, ArrayList<Listitem>>();	 
		 employelb.renderAll();
		 //lors de la selection d'un employ�, affichage d ela fiche associ� � cet employ� si elle existe 
		 //si la fiche n'existe pas , il faut la creer
		 
		 //1. affichage des informations relatifs � l'employ�

		 selectedEmploye=employe.getSelectedItem().getLabel();
		 
		 if(!selectedEmploye.equals("s�lectionner un employ�"))
		 {
			 
			 nomEmploye.setText(selectedEmploye);
			 EmployesAEvaluerBean employerAEvaluerBean=mapEmployeAEvaluerBean.getMapclesnomEmploye().get(selectedEmploye);
			 selectednomposteTravail=employerAEvaluerBean.getPoste_travail();
			 posteTravail.setText(selectednomposteTravail);
			 ArrayList <String> listFamille=employerAEvaluerBean.getFamille();
			 Iterator<String> iterator=listFamille.iterator();
		 	while(iterator.hasNext())
		 	{
		 		String famille=iterator.next();
		 		Famille.appendItem(famille);
		 	}
		 	if(Famille.getItemCount()>0)
		 		Famille.setSelectedIndex(0);
		 
		 	selectedFamille=listFamille.get(0);
		 
		 	//afficher toutes les donn�es associ�es � ce poste de travail
		 
		 	//recuperation du code_poste associ� � l'intitule
		 	String code_poste=mapintitule_codeposte.get(selectednomposteTravail);
		 
		 	String cles=code_poste+"#"+selectedFamille;
		 
		 



			 //afficher le contenu de mapPosteTravailFiche
			 ArrayList<FicheEvaluationBean> listFiche=mapPosteTravailFiche.get(cles);
			 
			 System.out.println("cles="+cles);
			 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
			 System.out.println("taille="+listFiche.size());
			 ArrayList<Listitem> liste=new ArrayList<Listitem>();
			 
			 while (iterator2.hasNext())
			 {
				 FicheEvaluationBean ficheEvaluation=(FicheEvaluationBean)iterator2.next();
				 String libelleCompetence=ficheEvaluation.getLibelle_competence();
				 String aptitudeObservable=ficheEvaluation.getAptitude_observable();
				 String descriptionCompetence=ficheEvaluation.getDefinition_competence();
				 
				 //affichage des donnn�es dans le tableau
				 Listitem listItem=new Listitem();
				 listItem.setParent(employelb);
				 	 
					 
				 //cellule competence
				 Listcell cellulecompetence=new Listcell();
				 cellulecompetence.setLabel(libelleCompetence);
				 cellulecompetence.setTooltiptext(descriptionCompetence);
				 cellulecompetence.setParent(listItem);
					 
				 //cellule aptitude observable
				 Listcell celluleaptitude=new Listcell();
				 celluleaptitude.setLabel(aptitudeObservable);
				 celluleaptitude.setParent(listItem);
					 
					 
				 //cellule niveau de maitrise
				 Listcell cellulecotation=new Listcell();
				 Spinner cotation=new Spinner();
				 cotation.setStep(1);
				 cotation.setConstraint("min 0 max 5");
				 cotation.setReadonly(true);
				 cotation.setParent(cellulecotation);
				 cellulecotation.setParent(listItem);
				 liste.add(listItem);
					 
			 }
			 mapItemsFamille.put(cles, liste);
			 currentListItem=liste;
		 }		 
	 }
	 
	 /**
		 * evenement a gerer lors de la selection d'un poste de travail
		 */
	 public void onSelect$poste_travail()
	 {
		 
		 /***************************
		  * 
		  * 
		  */
		 
		 //vider le contenu de la grille associ�e � l'ancien employ� selectionn�
		 if (currentListItem!=null)
		 {
			 Iterator<Listitem> iterator=currentListItem.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
			 selectedEmploye="";
			 nomEmploye.setText(selectedEmploye);
			 selectednomposteTravail="";
			 posteTravail.setText(selectednomposteTravail);
		 }
		 //si un employ� a d�ja �t� selectionner alors vider le contnu de la combobox
		 if(Famille.getItemCount()>0)
		 {
			int nb= Famille.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{
				
				Famille.removeItemAt(i);
			}
		 }
		 //vider le contenu de la combo employe 
		 if(employe.getItemCount()>0)
		 {
			int nb= employe.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{
				
				employe.removeItemAt(i);
			}
		 }
		 employe.appendItem("s�lectionner un employ�");
		 
		 //1. mise � jour de la liste des employes avec la selection de l'attribut selectionner un employe
		 //avec toutes les cons�quences qui doivent se d�couler de cette de cette selection
		 
		//remplissage du contenu de la combo associ�e aux postes de travail
		 selectednomposteTravail=poste_travail.getSelectedItem().getLabel();
		 if(!selectednomposteTravail.equals("Tous poste de travail"))
		 {
			 HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeAEvaluerBean.getMapclesposte();
			 HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(selectednomposteTravail);
			Set <String> listEmploye=mapEmploye.keySet();
			Iterator<String> iterator =listEmploye.iterator();
			while(iterator.hasNext())
			{
				String nomEmploye=iterator.next();
				employe.appendItem(nomEmploye);
			}
			employe.setSelectedIndex(0);
			 
			 
		 }

	 }
	 
	 /**
		 * evenement a gerer lors de la selection d'une famille
		 */
	 public void onSelect$Famille()
	 {
		 //r�cup�ration de la famille selectionn�e
		 selectedFamille=(String)Famille.getSelectedItem().getLabel();
		 
		 //lors de la selection d'une famille, il faut :
		 //1. vider le contenu de la table
		 //2. remplir la table avec le cntenu de la nouvelle cles code_poste, famille
		 
		 
		//1. vider le contenu de la table
		 if (currentListItem!=null)
		 {
			 Iterator<Listitem> iterator=currentListItem.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
		 }
		 
		//recuperation du code_poste associ� � l'intitule
		 String code_poste=mapintitule_codeposte.get(selectednomposteTravail);
		 
		 String cles=code_poste+"#"+selectedFamille;
		 
		 //afficher le contenu de mapPosteTravailFiche
		 ArrayList<FicheEvaluationBean> listFiche=mapPosteTravailFiche.get(cles);
		 
		 
		 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
		 
		 ArrayList<Listitem> liste=new ArrayList<Listitem>();
		 
		 while (iterator2.hasNext())
		 {
			 FicheEvaluationBean ficheEvaluation=(FicheEvaluationBean)iterator2.next();
			 String libelleCompetence=ficheEvaluation.getLibelle_competence();
			 String aptitudeObservable=ficheEvaluation.getAptitude_observable();
			 String descriptionCompetence=ficheEvaluation.getDefinition_competence();
			 
			 //affichage des donnn�es dans le tableau
			 Listitem listItem=new Listitem();
			 listItem.setParent(employelb);
			 	 
				 
			 //cellule competence
			 Listcell cellulecompetence=new Listcell();
			 cellulecompetence.setLabel(libelleCompetence);
			 cellulecompetence.setTooltiptext(descriptionCompetence);
			 cellulecompetence.setParent(listItem);
				 
			 //cellule aptitude observable
			 Listcell celluleaptitude=new Listcell();
			 celluleaptitude.setLabel(aptitudeObservable);
			 celluleaptitude.setParent(listItem);
				 
				 
			 //cellule niveau de maitrise
			 Listcell cellulecotation=new Listcell();
			 Spinner cotation=new Spinner();
			 cotation.setStep(1);
			 cotation.setConstraint("min 0 max 5");
			 cotation.setReadonly(true);
			 cotation.setParent(cellulecotation);
			 cellulecotation.setParent(listItem);
			 liste.add(listItem);
				 
		 }
		 mapItemsFamille.put(cles, liste);
		 currentListItem=liste;
	 		 
	 }
}
