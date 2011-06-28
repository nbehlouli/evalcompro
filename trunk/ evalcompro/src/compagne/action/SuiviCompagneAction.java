package compagne.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import compagne.bean.SuiviCompagneBean;
import compagne.model.SuiviCompagneModel;

import administration.bean.AdministrationLoginBean;
import administration.model.AdministrationLoginModel;

public class SuiviCompagneAction extends GenericForwardComposer{
	
	Listbox comp_list;
	Listbox  struct_list;
	Checkbox checkbox_prog;
	AnnotateDataBinder binder;
	//List<AdministrationLoginBean> model = new ArrayList<AdministrationLoginBean>();
	//SuiviCompagneBean selected;
	Button search;
	Button valider;
	Button sendmail;
	Label msg;
	Label employee_name;
	List<String> profilemodel=new ArrayList<String>();
	List<String> basedonneemodel=new ArrayList<String>();
	List<SuiviCompagneBean> model = new ArrayList<SuiviCompagneBean>();

	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		SuiviCompagneModel init= new SuiviCompagneModel();
		
		
		Set set = (init.getCompagneList()).entrySet(); 
		Set sec= (init.getStructEntrepriseList()).entrySet();
		
		Iterator i = set.iterator();
		Iterator i1 = sec.iterator();
		
		// Display elements
		while(i.hasNext()) {
		Map.Entry me = (Map.Entry)i.next();
		comp_list.appendItem((String) me.getKey(),(String) me.getKey());
		//profilemodel.add((String) me.getKey());
		}
		// Display elements
		while(i1.hasNext()) {
		Map.Entry me = (Map.Entry)i1.next();
		struct_list.appendItem((String) me.getKey(),(String) me.getKey());
		//basedonneemodel.add((String) me.getKey());
		}
		// création de la structure de l'entreprise bean
		//AdministrationLoginModel admin_compte =new AdministrationLoginModel();
		model=init.uploadListEvaluateur();
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
	
	}
	public List<SuiviCompagneBean> getModel() {
		return model;
	}
	
	
	

}
