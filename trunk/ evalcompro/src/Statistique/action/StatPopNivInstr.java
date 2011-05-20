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

public class StatPopNivInstr extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	
	public StatPopNivInstr()
	{
		
	}

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		System.out.println("1111");
//	   	PieModel model = new SimplePieModel();
//
//		
//		model.setValue("c0", new Double(21.2));
//		model.setValue("c1", new Double(10.2));
//		model.setValue("c2", new Double(40.4));
//		model.setValue("c3", new Double(28.2));
//		mychart.setModel(model);
		
		CategoryModel catmodel = new SimpleCategoryModel();
        catmodel.setValue("Directeur de pôle", "Universitaire", new Integer(75));
        catmodel.setValue("Directeur de pôle", "Moyen", new Integer(20));
        catmodel.setValue("Directeur de pôle", "Sans niveau", new Integer(5));
        catmodel.setValue("Comptable", "Universitaire", new Integer(66));
        catmodel.setValue("Comptable", "Moyen", new Integer(33));
        catmodel.setValue("Comptable", "Sans niveau", new Integer(1));
        mychart.setModel(catmodel);
        
        //ces instructions permettent de récuperer l'objet image pour l'export
        
        ChartEngine d=mychart.getEngine();
		image=d.drawChart(mychart);
		
	}
	
	 public void onClick$downloadimage() {


			//enregistrement du fichier
			Filedownload fichierdownload=new Filedownload();

			fichierdownload.save(image, "jpg", "Stat_Population_Niveau_Instruction.jpg");
		}
}
