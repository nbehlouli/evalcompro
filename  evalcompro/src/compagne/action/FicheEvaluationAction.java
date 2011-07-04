package compagne.action;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Html;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;


import administration.bean.CompteBean;

import common.ApplicationFacade;
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
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		
		//recupération du profil de l'utilisateur
		CompteBean compteUtilisateur=ApplicationFacade.getInstance().getCompteUtilisateur();
		
		//récupération de l'id_employé associé à l'id_compte
		FicheEvaluationModel ficheEvaluationModel=new FicheEvaluationModel();
		int id_employe=ficheEvaluationModel.getIdEmploye(compteUtilisateur.getId_compte());
		compteUtilisateur.setId_employe(id_employe);
		
		//récupération de l'information sur la validité de la fiche de l'employé connecté
		boolean ficheValide=ficheEvaluationModel.getValiditeFiche(id_employe);
		
		
		//onglet Ma Fiche d'évaluation
		
		evaluations.setStyle("overflow:auto");
		posteTravail.setDisabled(true);
		nomEmploye.setDisabled(true);
		groupe.setDisabled(true);
		if(ficheValide)
		{
			
			//construction et affichage du contenu de la page
			
		}
		else
		{
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
