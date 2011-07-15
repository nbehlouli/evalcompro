package compagne.action;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
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

	Tab FValide ;
	Tabpanel fichevalide;
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
	Button help3;
	Popup help1Pop;
	Html htmlhelp1;
	Component comp1;
	
	//objets graphiques de l'onglet des omployes deja evalués
	Textbox posteTravailV;
	Textbox nomEmployeV;
	Button help1V;
	Button help2V;
	Combobox FamilleV;	
	Combobox employeV; 
	Combobox poste_travailV;
	Listbox employelbV;
	//objets graphique de l'onglet la fiche d'evaluation
	
	Textbox nomEmployeM;
	Textbox posteTravailM;
	Combobox FamilleM;
	Groupbox gb3;
	Listbox employelbM;
	Caption labelM;
	//objets a utiliser
	
	HashMap<String, ArrayList<FicheEvaluationBean>> mapPosteTravailFiche;
	HashMap<String, ArrayList<FicheEvaluationBean>> mapPosteTravailFicheV;
	HashMap <String, String > mapintitule_codeposte;
	HashMap <String, String > mapcode_intituleposte;
	HashMap <String, ArrayList<Listitem>> mapItemsFamille =new HashMap<String, ArrayList<Listitem>>();
	HashMap <String, ArrayList<Listitem>> mapItemsFamilleV =new HashMap<String, ArrayList<Listitem>>();
	HashMap <String, String > mapcode_description_poste;
	ArrayList <CotationBean> listCotation;
	HashMap<String , Combobox>listeCombo=new HashMap<String, Combobox>();
	
	HashMap<String , Combobox>listeComboV=new HashMap<String, Combobox>();
	
	HashMap<String,HashMap<String , Combobox>> mapFamilleCombo=new HashMap<String, HashMap<String , Combobox>> ();
	HashMap<String,HashMap<String , Combobox>> mapFamilleComboV=new HashMap<String, HashMap<String , Combobox>> ();
	ArrayList<Listitem> currentListItem=null;
	
	ArrayList<Listitem> currentListItemM=null;
	ArrayList<Listitem> currentListItemV=null;
	
	// objets de la selection en cours
	String selectedEmploye;
	MapEmployesAEvaluerBean mapEmployeAEvaluerBean;
	MapEmployesAEvaluerBean mapEmployeEvalueBean;
	String selectedFamille;
	String selectednomposteTravail;

	String selectedFamilleM;
	
	String selectedEmployeV;
	String selectedFamilleV;
	String selectednomposteTravailV;
	
	HashMap <String, ArrayList<FicheEvaluationBean>> mapfamilleFicheEvaluationM;
	HashMap <String, ArrayList<FicheEvaluationBean>> mapfamilleFicheEvaluationV;
	
	ArrayList <String> listFamillePoste;
	ArrayList <String> listFamillePosteV;
	
	//EmployesAEvaluerBean employerAEvaluerBean1;
	
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
		
		//remplissage de la combobox famille
		

		if(ficheValide)
		{
			
			//construction et affichage du contenu de la page
			
			
			//récupération du nom de la personne connecté
			String retour=ficheEvaluationModel.getNomUtilisateur(id_employe);
			String[] val=retour.split("#");
			nomEmployeM.setText(val[0]);
			String intitule_posteM=val[1];
			posteTravailM.setText(intitule_posteM);
			
			String code_posteM=mapintitule_codeposte.get(intitule_posteM);
			Set <String> listeFamille=mapPosteTravailFiche.keySet();
			Iterator<String> itarator =listeFamille.iterator();
			ArrayList <String > listeFamilleM=new ArrayList<String>();
			while(itarator.hasNext())
			{
				String cles=itarator.next();
				if(cles.startsWith(code_posteM+"#"))
				{
					//inserer famille dans la comboBox
					String[] val1=cles.split("#");
					FamilleM.appendItem(val1[1]);
					listeFamilleM.add(val1[1]);
				}
			}
			if(listeFamilleM.size()==0)
			{
				//aucune compagne n'est prévu pour ce poste de travail
				
				//enlever ce qui existe et afficher la page HTML 
				//affichage d'un message disant que la fiche ne peut être visible
				gb3.detach();
				html=new Html();
				String content="Vous n'avez été convoqué à aucune compagne d'avaluation ";
				html.setStyle("color:red;margin-left:15px");
				html.setContent(content);
				html.setParent(maFiche);
			}
			else
			{
				
				//récupération de la fiche d'avaluation de l'id_employe
				
				mapfamilleFicheEvaluationM=ficheEvaluationModel.getMaFicheEvaluaton(id_employe);
				//affichage de la cotation
				if(FamilleM.getItemCount()>0)
					FamilleM.setSelectedIndex(0);
		 
				selectedFamilleM=listeFamilleM.get(0);
				ArrayList<FicheEvaluationBean> listficheEvaluationBean=mapfamilleFicheEvaluationM.get(selectedFamilleM);
				ArrayList<Listitem> liste=new ArrayList<Listitem>();
				
				/******************************/
				Iterator <FicheEvaluationBean> iteratorFiche=listficheEvaluationBean.iterator();
				while (iteratorFiche.hasNext())
				{
					FicheEvaluationBean ficheEvaluationBean=iteratorFiche.next();
				
					//mise à jour du titre
					
					
					//labelM.setLabel("Fiche Employé : évaluation du "+  ficheEvaluationBean.getDate_evaluation() +" de la compagne "+ ficheEvaluationBean.getCompagne_type());
					labelM.setLabel("Fiche Employé : Compagne  "+ ficheEvaluationBean.getCompagne_type() +" du "+ ficheEvaluationBean.getDate_evaluation() );
					
					//affichage des donnnées dans le tableau
					Listitem listItem=new Listitem();
					listItem.setParent(employelbM);
				 	 
					 
					//cellule competence
					Listcell cellulecompetence=new Listcell();
					cellulecompetence.setLabel(ficheEvaluationBean.getLibelle_competence());
					cellulecompetence.setTooltiptext(ficheEvaluationBean.getDefinition_competence());
					cellulecompetence.setParent(listItem);
					 
					//cellule aptitude observable
					Listcell celluleaptitude=new Listcell();
					celluleaptitude.setLabel(ficheEvaluationBean.getAptitude_observable());
					celluleaptitude.setParent(listItem);
					 
					 
					//cellule niveau de maitrise
					Listcell cellulecotation=new Listcell();
					Iterator<CotationBean> itcotationBean =listCotation.iterator();
					boolean conti=true;
					int valCotation=0;
					while((itcotationBean.hasNext() && conti))
					{
						CotationBean cotationbean=itcotationBean.next();
						int valeur=cotationbean.getId_cotation();
						System.out.println("valeur " +valeur);
						valCotation=cotationbean.getValeur_cotation();
						if(valeur==ficheEvaluationBean.getNiveau_maitrise())

						{
							conti=false;
							valCotation=ficheEvaluationBean.getNiveau_maitrise();
						}
					}
							
					
					cellulecotation.setLabel(valCotation+"");
				 
					cellulecotation.setParent(listItem);
					
					liste.add(listItem);
				}
				/*****************/
				currentListItemM=liste;
			}
		}
		else
		{
			//enlever ce qui existe et afficher la page HTML 
			
			gb3.detach();
			
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
				System.out.println("++++++++++++++"+nomEmploye);
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
		
		//si c'est un administrateur, il peut voir toutes les fiches d'evaluations ou evaluateur
		if((compteUtilisateur.getId_profile()==3)||(compteUtilisateur.getId_profile()==2))
		{
			
			 mapPosteTravailFicheV=ficheEvaluationModel.getInfosFicheEvaluationparPoste();
			 
			//remplissage du contenu de la combo associée aux postes de travail
			 mapEmployeEvalueBean=ficheEvaluationModel.getListEmployesvalue(id_employe);
			 HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeEvalueBean.getMapclesposte();
			Set <String>listePoste= Mapclesposte.keySet();
			Iterator <String > iterator=listePoste.iterator();
			poste_travailV.appendItem("Tous poste de travail");
			while(iterator.hasNext())
			{
				String nomPoste=iterator.next();
				poste_travailV.appendItem(nomPoste);
			}
			//selection du premier item (tous poste de travail)
			poste_travailV.setSelectedIndex(0);
			
			//remplissage de la comboBox avec tous les nom des employes quelque soit leur type de poste
			HashMap<String, EmployesAEvaluerBean> mapclesEmploye=mapEmployeEvalueBean.getMapclesnomEmploye();
			Set <String>listeEmploye=mapclesEmploye.keySet();
			iterator=listeEmploye.iterator();
			employeV.appendItem("sélectionner un employé");
			while(iterator.hasNext())
			{
				String nomEmploye=iterator.next();
				employeV.appendItem(nomEmploye);

			}
			//selection du premier item de la combobox employe
			if(employeV.getItemCount()>0)
				employeV.setSelectedIndex(0);
		}
		else
		{
			//rendre l'onglet invisible 
			FValide.detach();
			fichevalide.detach();
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
			 //employerAEvaluerBean1=employerAEvaluerBean;
			 selectednomposteTravail=employerAEvaluerBean.getPoste_travail();
			 posteTravail.setText(selectednomposteTravail);
			 
			 String code_poste=mapintitule_codeposte.get(selectednomposteTravail);
			 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
			 listFamillePoste=ficheEvaluationModel.getFamilleAssociePoste(code_poste);
			 //ArrayList <String> listFamille=employerAEvaluerBean.getFamille();
			 Iterator<String> iterator=listFamillePoste.iterator();
		 	while(iterator.hasNext())
		 	{
		 		String famille=iterator.next();
		 		Famille.appendItem(famille);
		 	}
		 	if(Famille.getItemCount()>0)
		 		Famille.setSelectedIndex(0);
		 
		 	selectedFamille=listFamillePoste.get(0);
		 
		 	//afficher toutes les données associées à ce poste de travail
		 
		 	//recuperation du code_poste associé à l'intitule
		 	
		 
		 	String cles=code_poste+"#"+selectedFamille;
		 
		 System.out.println(cles);



			 //afficher le contenu de mapPosteTravailFiche
			 ArrayList<FicheEvaluationBean> listFiche=mapPosteTravailFiche.get(cles);
			 
			 System.out.println("cles="+cles);
			 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
			 System.out.println("taille="+listFiche.size());
			 ArrayList<Listitem> liste=new ArrayList<Listitem>();
			 listeCombo=new HashMap<String, Combobox>();
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
			 mapFamilleCombo.put(selectedFamille, listeCombo);
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
		 
		 HashMap<String, Combobox> listeComboMAJ=mapFamilleCombo.get(selectedFamille);
		 listeCombo=new HashMap<String, Combobox>();
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
			 boolean dejaVisite=false;
			 
			 if(listeComboMAJ!=null)
				 dejaVisite=true;
				 
			 while(iterator4.hasNext())
			 {
				 CotationBean cotationBean=(CotationBean)iterator4.next();
				 
				 
				 valeur= ficheEvaluation.getId_repertoire_competence()+"#"+employerAEvaluerBean.getId_employe()+"#"+employerAEvaluerBean.getId_planning_evaluation();
				 
				 if (dejaVisite==true)
				 {
					 
					 cotation=listeComboMAJ.get(valeur);
				 }
				 else
				 {
					 cotation.appendItem(cotationBean.getValeur_cotation()+"");
					 cotation.setContext(valeur);
					 
					 cotation.setName("-1");
					 
				 }
				 cotation.setReadonly(true);
				 cotation.setParent(cellulecotation);
				 cellulecotation.setParent(listItem);
				 
				 
			 }

			 cotation.addForward("onSelect", comp1, "onSelectEvaluation");
			 listeCombo.put(valeur,cotation );
			 liste.add(listItem);
				 
		 }
		 mapFamilleCombo.put(selectedFamille, listeCombo);
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
			 //htmlhelp1.setStyle("background-color: #1eadff");
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
	 
	 
	 public void onClick$help1V()
	 {
		 //Affichage de la description du poste de travail selectionné
		 
		 String code=mapintitule_codeposte.get(selectednomposteTravailV);
		 
		 String descriptionPoste=mapcode_description_poste.get(code);
		 if(descriptionPoste!=null)
		 {
			 //Html html=new Html();
			 //htmlhelp1.setContent(content)
			 String content=descriptionPoste;
			 //htmlhelp1.setStyle("background-color: #1eadff");
			 htmlhelp1.setContent(content);
			 htmlhelp1.setParent(help1Pop);
			 help1Pop.open(help1);
		 }
			

	 }
	 
	 public void onClick$help2V()
	 {
		 String message=CreationMessageHelp2(listCotation);
		 System.out.println(message);
		 //htmlhelp1.setStyle("background-color: #1eadff");
		 htmlhelp1.setContent(message);
		 htmlhelp1.setParent(help1Pop);
		 help1Pop.open(help2);
	 }
	 
	 public void onClick$help3()
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
		 //verifier que toutes les familles ont été selectionnées
		// ArrayList <String> listFamille=employerAEvaluerBean1.getFamille();

		 Set <String> famillesRemplies=mapFamilleCombo.keySet();
		 if(listFamillePoste.size()==famillesRemplies.size())
		 {
			 // famille par famille que tous les combos sont remplies
			 Iterator <String>itfamille=famillesRemplies.iterator();
			 boolean continuer=true;
			 while(itfamille.hasNext())
			 {
				 String clles=itfamille.next();
				 HashMap<String , Combobox>listeComb=mapFamilleCombo.get(clles);
				 Set <String >listclesCombo=listeComb.keySet();
				 Iterator<String> iterator =listclesCombo.iterator();
				 
				 while (iterator.hasNext()&& continuer)
				 {
					 String cles=iterator.next();
					 Combobox combo=listeComb.get(cles);
					 if(combo.getName().equals("-1"))
					 {

						 continuer=false;
					 }
					 
				 }
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
				/**********************************/
				 // famille par famille que tous les combos sont remplies
				 famillesRemplies=mapFamilleCombo.keySet();
				 itfamille=famillesRemplies.iterator();
				 String id_planning_evaluation="";
				 String id_employe="";				
				 while(itfamille.hasNext())
				 {
					 String clles=itfamille.next();
					 HashMap<String , Combobox>listeComb=mapFamilleCombo.get(clles);
					 Set <String >listclesCombo=listeComb.keySet();
 
					 listclesCombo=listeComb.keySet();
					 Iterator<String> iterator =listclesCombo.iterator();
					 //continuer=true;

					 System.out.println("clles = "+clles+ " taille ="+ listclesCombo.size());
					 

					 while (iterator.hasNext())
					 {
						 String cles=(String)iterator.next();
						 Combobox combo=listeComb.get(cles);
						 String valeurs=combo.getContext();
						 String[] val=valeurs.split("#");
						 String id_repertoire_competence=val[0];
						 id_employe=val[1];
						 id_planning_evaluation=val[2];
						 String id_cotation=combo.getName();

						 ficheEvaluationModel.updateFicheEvalution(id_repertoire_competence,id_employe,id_planning_evaluation,id_cotation);
					 }
				 
					 //validation de la fiche
					 
				 }
				 ficheEvaluationModel.validerFicheEvaluation(id_planning_evaluation, id_employe);
				 valider.setDisabled(true);
			 }
			 
		 }
		 else
		 {
			 //afficher messagebox
			 try 
			 {
				 System.out.println("pas la même taille");
				Messagebox.show("Vos modifications ne peuvent être validées tant que vous n'avez pas évalué toutes les compétences de l'employé", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			 } 
			 catch (InterruptedException e) 
			 {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	 
	 public void onSelect$FamilleM()
	 {
		//récupération de la famille selectionnée
		 selectedFamilleM=(String)FamilleM.getSelectedItem().getLabel();
		 
		 //lors de la selection d'une famille, il faut :
		 //1. vider le contenu de la table
		 //2. remplir la table avec le cntenu de la nouvelle cles code_poste, famille
		 
		 
		//1. vider le contenu de la table
		 if (currentListItemM!=null)
		 {
			 Iterator<Listitem> iterator=currentListItemM.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
		 }
		 
		 
		 /******************************************************************/


			ArrayList<FicheEvaluationBean> listficheEvaluationBean=mapfamilleFicheEvaluationM.get(selectedFamilleM);
			ArrayList<Listitem> liste=new ArrayList<Listitem>();
			
			/******************************/
			Iterator <FicheEvaluationBean> iteratorFiche=listficheEvaluationBean.iterator();
			while (iteratorFiche.hasNext())
			{
				FicheEvaluationBean ficheEvaluationBean=iteratorFiche.next();
			
				//affichage des donnnées dans le tableau
				Listitem listItem=new Listitem();
				listItem.setParent(employelbM);
			 	 
				 
				//cellule competence
				Listcell cellulecompetence=new Listcell();
				cellulecompetence.setLabel(ficheEvaluationBean.getLibelle_competence());
				cellulecompetence.setTooltiptext(ficheEvaluationBean.getDefinition_competence());
				cellulecompetence.setParent(listItem);
				 
				//cellule aptitude observable
				Listcell celluleaptitude=new Listcell();
				celluleaptitude.setLabel(ficheEvaluationBean.getAptitude_observable());
				celluleaptitude.setParent(listItem);
				 
				 
				//cellule niveau de maitrise
				Listcell cellulecotation=new Listcell();
				Iterator<CotationBean> itcotationBean =listCotation.iterator();
				boolean conti=true;
				int valCotation=0;
				while((itcotationBean.hasNext() && conti))
				{
					CotationBean cotationbean=itcotationBean.next();
					int valeur=cotationbean.getId_cotation();
					System.out.println("valeur " +valeur);
					valCotation=cotationbean.getValeur_cotation();
					if(valeur==ficheEvaluationBean.getNiveau_maitrise())

					{
						conti=false;
						valCotation=ficheEvaluationBean.getNiveau_maitrise();
					}
				}
						
				
				cellulecotation.setLabel(valCotation+"");
			 
				cellulecotation.setParent(listItem);
				
				liste.add(listItem);
			}
			
			currentListItemM=liste;
		 
		 /********************************************************************/
	 }
	 
	 
	 /**
		 * evenement a gerer lors de la selection d'un poste de travail dans l'onglet des fiche valide
		 */
	 public void onSelect$poste_travailV()
	 {
		 

		 
		 //vider le contenu de la grille associée à l'ancien employé selectionné
		 if (currentListItemV!=null)
		 {
			 Iterator<Listitem> iterator=currentListItemV.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
			 selectedEmployeV="";
			 nomEmployeV.setText(selectedEmployeV);
			 selectednomposteTravailV="";
			 posteTravailV.setText(selectednomposteTravailV);
		 }
		 //si un employé a déja été selectionner alors vider le contnu de la combobox
		 if(FamilleV.getItemCount()>0)
		 {
			int nb= FamilleV.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{
				
				FamilleV.removeItemAt(i);
			}
		 }
		 //vider le contenu de la combo employe 
		 if(employeV.getItemCount()>0)
		 {
			int nb= employeV.getItemCount();
			for (int i=nb-1;i>=0;i--)
			{
				
				employeV.removeItemAt(i);
			}
		 }
		 employeV.appendItem("sélectionner un employé");
		 
		 //1. mise à jour de la liste des employes avec la selection de l'attribut selectionner un employe
		 //avec toutes les conséquences qui doivent se découler de cette de cette selection
		 
		//remplissage du contenu de la combo associée aux postes de travail
		 selectednomposteTravailV=poste_travailV.getSelectedItem().getLabel();
		 if(!selectednomposteTravailV.equals("Tous poste de travail"))
		 {
			 HashMap<String, HashMap<String, EmployesAEvaluerBean>> Mapclesposte=mapEmployeEvalueBean.getMapclesposte();
			 HashMap<String, EmployesAEvaluerBean> mapEmploye=Mapclesposte.get(selectednomposteTravailV);
			Set <String> listEmploye=mapEmploye.keySet();
			Iterator<String> iterator =listEmploye.iterator();
			while(iterator.hasNext())
			{
				String nomEmploye=iterator.next();
				employeV.appendItem(nomEmploye);
			}
			employeV.setSelectedIndex(0);
			 
			 
		 }
	 }
	 
	 /**
		 * evenement a gerer lors de la selection d'une famille dans l'onglet employe evalué
		 */
	 public void onSelect$FamilleV()
	 {
		 //récupération de la famille selectionnée
		 selectedFamilleV=(String)FamilleV.getSelectedItem().getLabel();
		 
		 //lors de la selection d'une famille, il faut :
		 //1. vider le contenu de la table
		 //2. remplir la table avec le cntenu de la nouvelle cles code_poste, famille
		 
		 
		//1. vider le contenu de la table
		 if (currentListItemV!=null)
		 {
			 Iterator<Listitem> iterator=currentListItemV.iterator();
			 while(iterator.hasNext())
			 {
				 Listitem item=iterator.next();
				 item.detach();
			 }
		 }
		 
		 //recuperer les infos associé à l'employé selectionné
		 EmployesAEvaluerBean employerAEvaluerBean=mapEmployeEvalueBean.getMapclesnomEmploye().get(selectedEmployeV);
		 
		 System.out.println("selectedEmploye "+selectedEmployeV);
		 System.out.println(employerAEvaluerBean.getId_employe());
		//recuperation du code_poste associé à l'intitule
		 //String code_poste=mapintitule_codeposte.get(selectednomposteTravailV);
		 
		 //String cles=code_poste+"#"+selectedFamilleV;
		 
		 //afficher le contenu de mapfamilleFicheEvaluationV
		 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
		 
		 mapfamilleFicheEvaluationV=ficheEvaluationModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe());
		 
		 ArrayList<FicheEvaluationBean> listFiche=mapfamilleFicheEvaluationV.get(selectedFamilleV);
		 
		 
		 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
		 
		 ArrayList<Listitem> liste=new ArrayList<Listitem>();
		 
		 //HashMap<String, Combobox> listeComboMAJ=mapFamilleCombo.get(selectedFamilleV);
		 //listeComboV=new HashMap<String, Combobox>();
		 while (iterator2.hasNext())
		 {
			 FicheEvaluationBean ficheEvaluation=(FicheEvaluationBean)iterator2.next();
			 String libelleCompetence=ficheEvaluation.getLibelle_competence();
			 String aptitudeObservable=ficheEvaluation.getAptitude_observable();
			 String descriptionCompetence=ficheEvaluation.getDefinition_competence();
			 
			 //affichage des donnnées dans le tableau
			 Listitem listItem=new Listitem();
			 listItem.setParent(employelbV);
			 	 
				 
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
				Iterator<CotationBean> itcotationBean =listCotation.iterator();
				boolean conti=true;
				int valCotation=0;
				while((itcotationBean.hasNext() && conti))
				{
					CotationBean cotationbean=itcotationBean.next();
					int valeur=cotationbean.getId_cotation();
					System.out.println("valeur " +valeur);
					valCotation=cotationbean.getValeur_cotation();
					if(valeur==ficheEvaluation.getNiveau_maitrise())

					{
						conti=false;
						valCotation=ficheEvaluation.getNiveau_maitrise();
					}
				}
						
				
				cellulecotation.setLabel(valCotation+"");
			 
				cellulecotation.setParent(listItem);
				
				liste.add(listItem);
			}
			
			currentListItemV=liste;
	 		 
	 }
	 
		/**
		 * evenement a gerer lors de la selection d'un employé de l'onglet employe evalué
		 */
		 public void onSelect$employeV()
		 {
			 
			 //vider le contenu de la grille associée à l'ancien employé selectionné
			 if (currentListItemV!=null)
			 {
				 Iterator<Listitem> iterator=currentListItemV.iterator();
				 while(iterator.hasNext())
				 {
					 Listitem item=iterator.next();
					 item.detach();
				 }
				 selectedEmployeV="";
				 nomEmployeV.setText(selectedEmployeV);
				 selectednomposteTravailV="";
				 posteTravailV.setText(selectednomposteTravailV);
			 }
			 //si un employé a déja été selectionner alors vider le contnu de la combobox
			 if(FamilleV.getItemCount()>0)
			 {
				int nb= FamilleV.getItemCount();
				for (int i=nb-1;i>=0;i--)
				{
					
					FamilleV.removeItemAt(i);
				}
			 }
			
			 mapItemsFamilleV=new 	HashMap<String, ArrayList<Listitem>>();	 
			 employelbV.renderAll();
			 //lors de la selection d'un employé, affichage de la fiche associé à cet employé si elle existe 
			 //si la fiche n'existe pas , il faut la creer
			 
			 //1. affichage des informations relatifs à l'employé

			 selectedEmployeV=employeV.getSelectedItem().getLabel();
			 
			 if(!selectedEmployeV.equals("sélectionner un employé"))
			 {
				 
				 nomEmployeV.setText(selectedEmployeV);
				 EmployesAEvaluerBean employerAEvaluerBean=mapEmployeEvalueBean.getMapclesnomEmploye().get(selectedEmployeV);
				 //employerAEvaluerBean1=employerAEvaluerBean;
				 selectednomposteTravailV=employerAEvaluerBean.getPoste_travail();
				 posteTravailV.setText(selectednomposteTravailV);
				 
				 FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
				 mapfamilleFicheEvaluationV=ficheEvaluationModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe());
				 
				 String code_poste=mapintitule_codeposte.get(selectednomposteTravailV);
				 
				 listFamillePosteV=ficheEvaluationModel.getFamilleAssociePoste(code_poste);
				 //ArrayList <String> listFamille=employerAEvaluerBean.getFamille();
				 Iterator<String> iterator=listFamillePosteV.iterator();
			 	while(iterator.hasNext())
			 	{
			 		String famille=iterator.next();
			 		FamilleV.appendItem(famille);
			 	}
			 	if(FamilleV.getItemCount()>0)
			 		FamilleV.setSelectedIndex(0);
			 
			 	selectedFamilleV=listFamillePosteV.get(0);
			 
			 	//afficher toutes les données associées à ce poste de travail
			 
			 	//recuperation du code_poste associé à l'intitule
			 	
			 
			 	//String cles=code_poste+"#"+selectedFamilleV;
			 
			 //System.out.println(cles);



			 mapfamilleFicheEvaluationV=ficheEvaluationModel.getMaFicheEvaluaton(employerAEvaluerBean.getId_employe());
			 
			 ArrayList<FicheEvaluationBean> listFiche=mapfamilleFicheEvaluationV.get(selectedFamilleV);
			 
				 //System.out.println("cles="+cles);
				 Iterator<FicheEvaluationBean> iterator2=listFiche.iterator();
				 System.out.println("taille="+listFiche.size());
				 ArrayList<Listitem> liste=new ArrayList<Listitem>();
				 listeComboV=new HashMap<String, Combobox>();
				 while (iterator2.hasNext())
				 {
					 FicheEvaluationBean ficheEvaluation=(FicheEvaluationBean)iterator2.next();
					 String libelleCompetence=ficheEvaluation.getLibelle_competence();
					 String aptitudeObservable=ficheEvaluation.getAptitude_observable();
					 String descriptionCompetence=ficheEvaluation.getDefinition_competence();
					 
					 //affichage des donnnées dans le tableau
					 Listitem listItem=new Listitem();
					 listItem.setParent(employelbV);
					 	 
						 
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
						Iterator<CotationBean> itcotationBean =listCotation.iterator();
						boolean conti=true;
						int valCotation=0;
						while((itcotationBean.hasNext() && conti))
						{
							CotationBean cotationbean=itcotationBean.next();
							int valeur=cotationbean.getId_cotation();
							System.out.println("valeur " +valeur);
							valCotation=cotationbean.getValeur_cotation();
							if(valeur==ficheEvaluation.getNiveau_maitrise())

							{
								conti=false;
								valCotation=ficheEvaluation.getNiveau_maitrise();
							}
						}
								
						
						cellulecotation.setLabel(valCotation+"");
					 
						cellulecotation.setParent(listItem);
						
						liste.add(listItem);
					}
					
					currentListItemV=liste;
			 }		 
		 }
}
