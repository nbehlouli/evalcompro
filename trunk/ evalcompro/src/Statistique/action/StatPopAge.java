package Statistique.action;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.Chart;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.PieModel;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.SimplePieModel;
import org.zkoss.zul.impl.ChartEngine;

public class StatPopAge extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	
	public StatPopAge()
	{
		
	}

	public void doAfterCompose(Component comp) throws Exception 
	{
		super.doAfterCompose(comp);
				
		CategoryModel catmodel = new SimpleCategoryModel();
		//catmodel.setValue("poste", "entre 18 et 30 ans", new Integer(pourcentage));
        catmodel.setValue("Directeur de pôle", "entre 18 et 30 ans", new Integer(20));
        catmodel.setValue("Directeur de pôle", "entre 31 et 45 ans", new Integer(35));
        catmodel.setValue("Directeur de pôle", "Superieur à 46 ans", new Integer(40));
        catmodel.setValue("Comptable", "entre 18 et 30 ans", new Integer(40));
        catmodel.setValue("Comptable", "entre 31 et 45 ans", new Integer(60));
        catmodel.setValue("Comptable", "Superieur à 46 ans", new Integer(70));

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
}
