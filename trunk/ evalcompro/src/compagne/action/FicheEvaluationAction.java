package compagne.action;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;


import administration.bean.CompteBean;
import administration.bean.CotationBean;
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
	Button help1;
	Button help2;
	Popup help1Pop;
	Html htmlhelp1;
	Component comp1;

	//objets a utiliser
	
	HashMap<String, ArrayList<FicheEvaluationBean>> mapPosteTravailFiche;
	HashMap <String, String > mapintitule_codeposte;
	HashMap <String, String > mapcode_intituleposte;
	HashMap <String, ArrayList<Listitem>> mapItemsFamille =new HashMap<String, ArrayList<Listitem>>();
	HashMap <String, String > mapcode_description_poste;
	ArrayList <CotationBean> listCotation;
	HashMap<String , Combobox>listeCombo=new HashMap<String, Combobox>();
	ArrayList<Listitem> currentListItem=null;
	
	// objets de la selection en cours
	String selectedEmploye;
	MapEmployesAEvaluerBean mapEmployeAEvaluerBean;
	String selectedFamille;
	String selectednomposteTravail;
	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
		comp1=comp;
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		
		//recupération du profil de l'utilisateur
		CompteBean compteUtilisateur=ApplicationFacade.getInstance().getCompteUtilisateur();
		
		//récupération de l'id_employé associé à l'id_compte
		FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
		int id_employe=ficheEvaluationModel.getIdEmploye(compteUtilisateur.getId_compte());
		compteUtilisateur.setId_employe(id_employe);
		
		//récuperation de la description des poste pour le bouton help
		
		mapcode_description_poste=ficheEvaluationModel.getPosteTravailDescription();
		
		//récupération de la cotation
		listCotation=ficheEvaluationModel.getCotations();
		
		
		//récupération de l'information sur la validité de la fiche de l'employé connecté
		boolean ficheValide=ficheEvaluationModel.getValiditeFiche(id_employe);
		
		//récupération des informations associées à une fiche d'evaluation à remplir;
		
		 mapPosteTravailFiche=ficheEvaluationModel.getInfosFicheEvaluationparPoste();
		 CompetencePosteTravailModel compt=new CompetencePosteTravailModel();
		 mapintitule_codeposte=compt.getlistepostesCode_postes();
		 mapcode_intituleposte=compt.getlisteCode_postes_intituleposte();
		//onglet Ma Fiche d'évaluation
		
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
			//affichage d'un message disant que la fiche ne peut être visible
			html=new Html();
			String content="Vous n'avez pas accès à votre fiche d'évaluation car elle n'a pas encore été complété par l'évaluateur";
			html.setStyle("color:red;margin-left:15px");
			html.setContent(content);
			html.setParent(maFiche);
		}
		//si c'est un évaluateur alors on affiche la liste des fiches associés aux employés à évaluer
		if(compteUtilisateur.getId_profile()==3)
		{
			
			//remplissage du contenu de la combo associée aux postes de travail
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
			employe.appendItem("sélectionner un employé");
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

	}
	
	/**
	 * evenement a gerer lors de la selection d'un employé
	 */
	 public void onSelect$employe()
	 {
		 
		 //vider le contenu de la grille associée à l'ancien employé selectionné
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
		 //si un employé a déja été selectionner alors vider le contnu de la combobox
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
		 //lors de la selection d'un employé, affichage d ela fiche associé à cet employé si elle existe 
		 //si la fiche n'existe pas , il faut la creer
		 
		 //1. affichage des informations relatifs à l'employé

		 selectedEmploye=employe.getSelectedItem().getLabel();
		 
		 if(!selectedEmploye.equals("sélectionner un employé"))
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
		 
		 	//afficher toutes les données associées à ce poste de travail
		 
		 	//recuperation du code_poste associé à l'intitule
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
				 
				 //affichage des donnnées dans le tableau
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
				 Combobox cotation=new Combobox();
				 
				 Iterator <CotationBean> iterator4=listCotation.iterator();
				 String valeur="";
				 while(iterator4.hasNext())
				 {
					 CotationBean cotationBean=(CotationBean)iterator4.next();
					 cotation.appendItem(cotationBean.getValeur_cotation()+"");
					 
					 valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
					 cotation.setContext(valeur);
					 
					 cotation.setName("-1");
					 cotation.setReadonly(true);
					 cotation.setParent(cellulecotation);
					 cellulecotation.setParent(listItem);
					 
				 }
				 cotation.addForward("onSelect", comp1, "onSelectEvaluation");
				 listeCombo.put(valeur,cotation );
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
		 

		 
		 //vider le contenu de la grille associée à l'ancien employé selectionné
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
		 //si un employé a déja été selectionner alors vider le contnu de la combobox
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
		 employe.appendItem("sélectionner un employé");
		 
		 //1. mise à jour de la liste des employes avec la selection de l'attribut selectionner un employe
		 //avec toutes les conséquences qui doivent se découler de cette de cette selection
		 
		//remplissage du contenu de la combo associée aux postes de travail
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
		 //récupération de la famille selectionnée
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
		 
		 //recuperer les infos associé à l'employé selectionné
		 EmployesAEvaluerBean employerAEvaluerBean=mapEmployeAEvaluerBean.getMapclesnomEmploye().get(selectedEmploye);
		//recuperation du code_poste associé à l'intitule
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
			 
			 //affichage des donnnées dans le tableau
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
			 Combobox cotation=new Combobox();
			 
			 Iterator <CotationBean> iterator4=listCotation.iterator();
			 String valeur="";
			 while(iterator4.hasNext())
			 {
				 CotationBean cotationBean=(CotationBean)iterator4.next();
				 cotation.appendItem(cotationBean.getValeur_cotation()+"");
				 
				 valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
				 cotation.setContext(valeur);
				 
				 cotation.setName("-1");
				 cotation.setReadonly(true);
				 cotation.setParent(cellulecotation);
				 cellulecotation.setParent(listItem);
				 
			 }
			 cotation.addForward("onSelect", comp1, "onSelectEvaluation");
			 listeCombo.put(valeur,cotation );
			 liste.add(listItem);
				 
		 }
		 mapItemsFamille.put(cles, liste);
		 currentListItem=liste;
	 		 
	 }
	 public void onClick$help1()
	 {
		 //Affichage de la description du poste de travail selectionné
		 
		 String code=mapintitule_codeposte.get(selectednomposteTravail);
		 
		 String descriptionPoste=mapcode_description_poste.get(code);
		 if(descriptionPoste!=null)
		 {
			 //Html html=new Html();
			 //htmlhelp1.setContent(content)
			 String content=descriptionPoste;
			 htmlhelp1.setStyle("background-color: #1eadff");
			 htmlhelp1.setContent(content);
			 htmlhelp1.setParent(help1Pop);
			 help1Pop.open(help1);
		 }
			

	 }
	 
	 public void onClick$help2()
	 {
		 String message=CreationMessageHelp2(listCotation);
		 System.out.println(message);
		 //htmlhelp1.setStyle("background-color: #1eadff");
		 htmlhelp1.setContent(message);
		 htmlhelp1.setParent(help1Pop);
		 help1Pop.open(help2);
	 }
	 public String CreationMessageHelp2(ArrayList <CotationBean> listeCotation)
	 {
		 String message="";
		 Iterator <CotationBean> iterator=listeCotation.iterator();
		 while(iterator.hasNext())
		 {
			 CotationBean cotation=(CotationBean)iterator.next();
			 message=message+ "--> "+cotation.getValeur_cotation()+" : "+ cotation.getLabel_cotation() +", "+ cotation.getDefinition_cotation() +"<br>";
		 }
		 return message;
	 }
	 
	 public void onClick$valider()
	 {
		 //verifier que toutes les cotations ont été remplies 
		 //sinon afficher un message comme quoi la validation n'a pas été prise en compte car
		 // toutes les comptétences n'ont pas été évaluées
		 Set <String >listclesCombo=listeCombo.keySet();
		 Iterator<String> iterator =listclesCombo.iterator();
		 boolean continuer=true;
		 while (iterator.hasNext()&& continuer)
		 {
			 String cles=iterator.next();
			 Combobox combo=listeCombo.get(cles);
			 if(combo.getName().equals("-1"))
			 		continuer=false;
			 
		 }
		 if(continuer==false)
		 {
			 try 
			 {
				Messagebox.show("Vos modifications ne peuvent être validées tant que vous n'avez pas évalué toutes les compétences de l'employé", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			 } 
			 catch (InterruptedException e) 
			 {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 else
		 {
			 //récupérer les modifications et enregistrer dans la base de donnée
			 //valider la fiche dans la table appropriée
			 
			 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
			 
			 
			 listclesCombo=listeCombo.keySet();
			 iterator =listclesCombo.iterator();
			 continuer=true;
			 String id_planning_evaluation="";
			 String id_employe="";
			 while (iterator.hasNext())
			{
				 String cles=iterator.next();
				 Combobox combo=listeCombo.get(cles);
				 String valeurs=combo.getContext();
				 String[] val=valeurs.split("#");
				 String id_repertoire_competence=val[0];
				 id_employe=val[1];
				 id_planning_evaluation=val[2];
				 String id_cotation=combo.getName();
				 ficheEvaluationModel.updateFicheEvalution(id_repertoire_competence,id_employe,id_planning_evaluation,id_cotation);
			}
			 
			 //validation de la fiche
			 ficheEvaluationModel.validerFicheEvaluation(id_planning_evaluation, id_employe);
		 }
	 }
	 
	 public void onSelectEvaluation(ForwardEvent event)
	 {
		 Combobox combo = (Combobox) event.getOrigin().getTarget();	

		 
		 //mettre id evaluations
		 String valeurCotation=(String)combo.getSelectedItem().getLabel();
		 //recherche de l'id_cotation
		 Iterator <CotationBean> iterator4=listCotation.iterator();
		 String valeur="";
		 int id=0;
		 boolean trouver=false;
		 while((iterator4.hasNext()&& !trouver))
		 {
			 CotationBean cotationBean=iterator4.next();
			 valeur=cotationBean.getValeur_cotation()+"";
			 if(valeur.equals(valeurCotation))
			 {
				 trouver=true;
				 id=cotationBean.getId_cotation();
			 }
		 }
		 combo.setName(id+"");

	 }
}
