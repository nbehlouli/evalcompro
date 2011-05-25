package Statistique.model;

import java.util.ArrayList;
import java.util.HashMap;

import Statistique.bean.StatCotationEmployeBean;

public class StatCotationEmployeModel {

	
	public ArrayList<StatCotationEmployeBean> InitialiserStatCotationEmploye()
	{
		
		ArrayList<StatCotationEmployeBean> liste=new ArrayList<StatCotationEmployeBean>();
		StatCotationEmployeBean statBean1=new StatCotationEmployeBean();
		statBean1.setNomEmploye("Helene BAUM");
		HashMap<String, Double> listeCotations3=new HashMap<String, Double>();
		listeCotations3.put("Affaire", new Double(3.00));
		listeCotations3.put("Relationnelle", new Double(2.5));
		listeCotations3.put("Personnelle", new Double(1.7));
		listeCotations3.put("Opérationnelle", new Double(1.00));
		
		HashMap<String, Double> listeCotations11=new HashMap<String, Double>();
		listeCotations11.put("Affaire", new Double(1.00));
		listeCotations11.put("Relationnelle", new Double(2.0));
		listeCotations11.put("Personnelle", new Double(2));
		listeCotations11.put("Opérationnelle", new Double(0.5));
		
		HashMap<String, Double> listeCotations21=new HashMap<String, Double>();
		listeCotations21.put("Affaire", new Double(3.00));
		listeCotations21.put("Relationnelle", new Double(2.5));
		listeCotations21.put("Personnelle", new Double(2));
		listeCotations21.put("Opérationnelle", new Double(1.00));

		HashMap<String, HashMap<String, Double>> listeCompagnes1=new HashMap<String, HashMap<String, Double>>();
		listeCompagnes1.put("Compagne1", listeCotations3);
		listeCompagnes1.put("Compagne2", listeCotations11);
		listeCompagnes1.put("Compagne3", listeCotations21);
		
		statBean1.setCompagne_listCotation(listeCompagnes1);
		
		
		liste.add(statBean1);
		StatCotationEmployeBean statBean=new StatCotationEmployeBean();
		statBean.setNomEmploye("Lilian DAUTAIS");
		HashMap<String, Double> listeCotations=new HashMap<String, Double>();
		listeCotations.put("Affaire", new Double(2.00));
		listeCotations.put("Relationnelle", new Double(3.5));
		listeCotations.put("Personnelle", new Double(2));
		listeCotations.put("Opérationnelle", new Double(1.00));
		
		HashMap<String, Double> listeCotations1=new HashMap<String, Double>();
		listeCotations1.put("Affaire", new Double(1.00));
		listeCotations1.put("Relationnelle", new Double(2.5));
		listeCotations1.put("Personnelle", new Double(2));
		listeCotations1.put("Opérationnelle", new Double(0.5));
		
		HashMap<String, Double> listeCotations2=new HashMap<String, Double>();
		listeCotations2.put("Affaire", new Double(1.00));
		listeCotations2.put("Relationnelle", new Double(1.5));
		listeCotations2.put("Personnelle", new Double(2));
		listeCotations2.put("Opérationnelle", new Double(1.00));

		HashMap<String, HashMap<String, Double>> listeCompagnes=new HashMap<String, HashMap<String, Double>>();
		listeCompagnes.put("Compagne1", listeCotations);
		listeCompagnes.put("Compagne2", listeCotations1);
		listeCompagnes.put("Compagne3", listeCotations2);
		
		statBean.setCompagne_listCotation(listeCompagnes);
		liste.add(statBean);
		return liste;
	}
}
