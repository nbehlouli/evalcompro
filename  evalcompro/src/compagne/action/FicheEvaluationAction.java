package compagne.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;


import administration.bean.CompteBean;

import common.ApplicationFacade;
import compagne.bean.EmployesAEvaluerBean;
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
			MapEmployesAEvaluerBean mapEmployeAEvaluerBean=ficheEvaluationModel.getListEmployesAEvaluer(id_employe);
			HashMap<String,EmployesAEvaluerBean> Mapclesposte=mapEmployeAEvaluerBean.getMapclesposte();
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
	}
}
