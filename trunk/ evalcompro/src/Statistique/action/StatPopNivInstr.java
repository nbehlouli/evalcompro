package Statistique.action;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.Chart;
import org.zkoss.zul.PieModel;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.SimplePieModel;

public class StatPopNivInstr extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
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
        catmodel.setValue("Directeur de p�le", "Universitaire", new Integer(20));
        catmodel.setValue("Directeur de p�le", "Moyen", new Integer(35));
        catmodel.setValue("Directeur de p�le", "Sans niveau", new Integer(40));
        catmodel.setValue("Comptable", "Universitaire", new Integer(40));
        catmodel.setValue("Comptable", "Moyen", new Integer(60));
        catmodel.setValue("Comptable", "Sans niveau", new Integer(70));
        mychart.setModel(catmodel);
		
	}
}
