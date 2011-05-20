package Statistique.action;

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

public class StatCotationEmploye extends  GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chart mychart;
	byte[] image;
	Combobox nom_employe;
	public StatCotationEmploye()
	{
		
	}

	public void doAfterCompose(Component comp) throws Exception 
	{
        
		super.doAfterCompose(comp);
		ListModel dictModel= new SimpleListModel(getDirectory());
		nom_employe.setModel(dictModel);		
		CategoryModel catmodel = new SimpleCategoryModel();
		//catmodel.setValue("poste", "entre 18 et 30 ans", new Integer(pourcentage));
        catmodel.setValue("IMI", "Affaire", new Double(2));
        catmodel.setValue("IMI", "Relationnelle", new Double(3.5));
        catmodel.setValue("IMI", "Personnelle", new Double(1.3));
        catmodel.setValue("IMI", "Opérationnelle", new Double(1));

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
	 
	 public String[] getDirectory()
	 {
		 String[] dictionary = { "abacus", "abase", "abate", "abbess", "abbey", "abbot", "abdicate", "abdomen", "abdominal", "abduction", "abed", "aberrant", "aberration", "abet", "abeyance",
	            "abhor", "abhorrence", "abhorrent", "abidance", "ability", "abject", "abjure", "able-bodied", "ablution", "abnegate", "abnormal", "abominable", "abominate", "abomination", "aboriginal",
	            "aborigines", "abound", "aboveboard", "abrade", "abrasion", "abridge", "abridgment", "abrogate", "abrupt", "abscess", "abscission", "abscond", "absence", "absent-minded", "absolution",
	            "absolve", "absorb", "absorption", "abstain", "abstemious", "abstinence", "abstinent", "abstract", "abstruse", "absurd", "abundant", "abusive", "abut", "abysmal", "abyss", "academic",
	            "academician", "academy", "accede", "accelerate", "accentuate", "accept", "access", "accessible", "accession", "accessory", "acclaim", "accolade", "accommodate", "accompaniment",
	            "accompanist", "accompany", "accomplice", "accomplish", "accordion", "accost", "account", "accouter", "accredit", "accrue", "acculturation", "accumulate", "accuracy", "accurate",
	            "accurately", "accursed", "accusation"};
	 
	 return dictionary;
	 }
}
