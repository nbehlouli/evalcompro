package Statistique.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.Chart;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.impl.ChartEngine;

import Statistique.bean.StatCotationEmployeBean;
import Statistique.model.StatCotationEmployeModel;

public class StatCotationEmploye extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	Combobox nom_employe;
	Combobox compagne;
	ArrayList<StatCotationEmployeBean> ListeCotationEmploye;
	String selectedEmploye;
	String selectedCompagne;
	StatCotationEmployeBean selectedBean;
	public StatCotationEmploye()
	{
		
	}

	public void doAfterCompose(Component comp) throws Exception 
	{
        
		super.doAfterCompose(comp);
		StatCotationEmployeModel cotationMoel=new StatCotationEmployeModel();
		ListeCotationEmploye=cotationMoel.InitialiserStatCotationEmploye();
		
		
		//initialisation de la combobox nom employe
		Iterator index=ListeCotationEmploye.iterator();
		int i=0;
		while(index.hasNext())
		{
			
			StatCotationEmployeBean cotationEmploye=(StatCotationEmployeBean)index.next();
			String nom_Employe=cotationEmploye.getNomEmploye();
			if (i==0)
				selectedEmploye=nom_Employe;
			nom_employe.appendItem(nom_Employe);
			i++;
		}
		
//		ListModel dictModel= new SimpleListModel(getDirectory());
//		nom_employe.setModel(dictModel);	
		
		// forcer la selection de la permiere ligne
		nom_employe.setSelectedIndex(0);
		
		//selection des données associées à la premiere ligne (compagnes)
		selectedBean=ListeCotationEmploye.get(0);
		
		HashMap <String, HashMap<String , Double>> compagnes=selectedBean.getCompagne_listCotation();
		Set <String> listeCompagne=compagnes.keySet();
		Iterator indexcompagne=listeCompagne.iterator();

		selectedCompagne="";
		while(indexcompagne.hasNext())
		{
			String nomCompagne=(String)indexcompagne.next();
			selectedCompagne=nomCompagne;
			compagne.appendItem(nomCompagne);
		}
		
		//selection de la dernière compagne
		compagne.setSelectedIndex(listeCompagne.size()-1);
		
		//selection des données associées à la dernière compagne
		// et affichage du graphe
		CategoryModel catmodel = new SimpleCategoryModel();
		HashMap<String , Double> listeStat=compagnes.get(selectedCompagne);
		Set<String> listekey=listeStat.keySet();
		Iterator indexkey=listekey.iterator();
		while(indexkey.hasNext())
		{
			String valeurcles=(String)indexkey.next();
			Double valeurStat=listeStat.get(valeurcles);
			catmodel.setValue("IMI", valeurcles, valeurStat);
		}
		
		
//		//catmodel.setValue("poste", "entre 18 et 30 ans", new Integer(pourcentage));
//        catmodel.setValue("IMI", "Affaire", new Double(2));
//        catmodel.setValue("IMI", "Relationnelle", new Double(3.5));
//        catmodel.setValue("IMI", "Personnelle", new Double(1.3));
//        catmodel.setValue("IMI", "Opérationnelle", new Double(1));
		mychart.setTitle("Cotation moyenne de l'employé " +selectedEmploye+" pour la compagne "+ selectedCompagne);
        mychart.setModel(catmodel);
        
        //ces instructions permettent de récuperer l'objet image pour l'export
        
        ChartEngine d=mychart.getEngine();
		image=d.drawChart(mychart);		

	}
	
	 @SuppressWarnings("static-access")
	 public void onClick$downloadimage() 
	 {
			//enregistrement du fichier
			Filedownload fichierdownload=new Filedownload();

			fichierdownload.save(image, "jpg", "Stat_Population_Age.jpg");
	}
	 

	 
	 public void onSelect$nom_employe()
	 {
		 selectedEmploye=nom_employe.getSelectedItem().getLabel();
		 
		 //mise à jour de la combo compagne et selection de la dernière compagne
		 
		 Iterator index=ListeCotationEmploye.iterator();
		 boolean continuer=true;
		 while((index.hasNext())||(continuer))
		 {
			 StatCotationEmployeBean contationbean=(StatCotationEmployeBean)index.next();
			 if(selectedEmploye.equals(contationbean.getNomEmploye()))
			 {
				 selectedBean=contationbean;
				 continuer=false;
				 //affichage de la liste compagne
				  //vider le contenu de la combo asocié à compagne
				 int i=compagne.getItemCount();
				 for (int j=i-1;j>=0;j--)
				 {
					 compagne.removeItemAt(j);
				 }
				 
				 //ajout des nouvelles données a como associé à compagne
				 Set <String> listeCompagne=contationbean.getCompagne_listCotation().keySet();
				Iterator indexcompagne=listeCompagne.iterator();
				while(indexcompagne.hasNext())
				{
					String nomCompagne=(String)indexcompagne.next();
					selectedCompagne=nomCompagne;
					compagne.appendItem(nomCompagne);
				}
				compagne.setSelectedIndex(listeCompagne.size()-1);
				//mise à jour du graphe avec les données de la plus recente compagne
				// et affichage du graphe
				CategoryModel catmodel = new SimpleCategoryModel();
				HashMap<String , Double> listeStat=contationbean.getCompagne_listCotation().get(selectedCompagne);
				Set<String> listekey=listeStat.keySet();
				Iterator indexkey=listekey.iterator();
				
				while(indexkey.hasNext())
				{
					String valeurcles=(String)indexkey.next();
					Double valeurStat=listeStat.get(valeurcles);
					catmodel.removeValue("IMI", valeurcles);
					catmodel.setValue("IMI", valeurcles, valeurStat);
					
				}
				mychart.setTitle("Cotation moyenne de l'employé " +selectedEmploye+" pour la compagne "+ selectedCompagne);
				mychart.setModel(catmodel);
		        
		        //ces instructions permettent de récuperer l'objet image pour l'export
		        
		        ChartEngine d=mychart.getEngine();
				image=d.drawChart(mychart);	
			 }
		 }
				 
	 }
	 
	 public void onSelect$compagne()
	 {
		 selectedCompagne=compagne.getSelectedItem().getLabel();
		 
		 HashMap<String , Double> listeStat=selectedBean.getCompagne_listCotation().get(selectedCompagne);
		 Set<String> listekey=listeStat.keySet();
			Iterator indexkey=listekey.iterator();
			CategoryModel catmodel = new SimpleCategoryModel();
			while(indexkey.hasNext())
			{
				String valeurcles=(String)indexkey.next();
				Double valeurStat=listeStat.get(valeurcles);
				catmodel.removeValue("IMI", valeurcles);
				catmodel.setValue("IMI", valeurcles, valeurStat);
				
			}
			mychart.setTitle("Cotation moyenne de l'employé " +selectedEmploye+" pour la compagne "+ selectedCompagne);
			mychart.setModel(catmodel);
	        
	        //ces instructions permettent de récuperer l'objet image pour l'export
	        
	        ChartEngine d=mychart.getEngine();
			image=d.drawChart(mychart);
	 }
}
