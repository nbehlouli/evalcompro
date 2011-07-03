package compagne.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import common.ApplicationFacade;
import compagne.bean.EmailEvaluateurBean;
import compagne.bean.SuiviCompagneBean;
import compagne.model.SuiviCompagneModel;

import administration.bean.AdministrationLoginBean;
import administration.model.AdministrationLoginModel;

public class SuiviCompagneAction extends GenericForwardComposer{
	
	Listbox comp_list;
	Listbox  struct_list;
	//Checkbox checkbox_prog;
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
	HashMap <String, Checkbox> selectedCheckBox;
	HashMap <String, Checkbox> unselectedCheckBox;
	boolean compage_termine=true;

	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		selectedCheckBox=new HashMap <String, Checkbox>();
		unselectedCheckBox=new HashMap <String, Checkbox>();
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
		/*while(i1.hasNext()) {
		Map.Entry me = (Map.Entry)i1.next();
		struct_list.appendItem((String) me.getKey(),(String) me.getKey());
		//basedonneemodel.add((String) me.getKey());
		}*/
	
		// création de la structure de l'entreprise bean
		//AdministrationLoginModel admin_compte =new AdministrationLoginModel();
		model=init.uploadListEvaluateur();
		comp_list.setSelectedIndex(0);
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
		int profileid=ApplicationFacade.getInstance().getProfile_id();
		 for (int pos=0;pos< this.getModel().size();pos++){
		    	
		    	if (this.getModel().get(pos).getPourcentage()<100 && profileid !=1){
		    		valider.setDisabled(true);
		    		compage_termine=false;
		    		return;
		    	}
		    	
		    }
	
	
	}
	public List<SuiviCompagneBean> getModel() {
		return model;
	}
public void onClick$search() throws SQLException {
	SuiviCompagneModel init= new SuiviCompagneModel(); 	
	
    Map map = new HashMap();
    //Map map_struct = new HashMap();
    map=init.getCompagneList();
    //map_struct=init.getStructEntrepriseList();
    Integer compagne=(Integer) map.get(comp_list.getSelectedItem().getLabel());
    //String structure=struct_list.getSelectedItem().getLabel();
    if (compagne==-1) {
    	model=init.uploadListEvaluateur();
    }
    else {
    	model=init.filtrerListEvaluateur(compagne);
    }
	
	//binder = new AnnotateDataBinder(comp);
	binder.loadAll();
		
				
	}

public void onModifyCheckedBox(ForwardEvent event){
	Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();		

	if (checkbox.isChecked())
	{
		//verifier si ça n'a pas encore été unchecked
		if(unselectedCheckBox.containsValue(checkbox))
		{
			unselectedCheckBox.remove(checkbox);
			
		}
		selectedCheckBox.put(checkbox.getValue(), checkbox);
	}
	else
	{
		//verifier si ça n'a pas encore été checked
		if(selectedCheckBox.containsValue(checkbox))
		{
			selectedCheckBox.remove(checkbox);
			
		}
		unselectedCheckBox.put(checkbox.getValue(), checkbox);
	}
	//selectedCheckBox
}

public void onClick$sendmail() throws SQLException {
	List employe = new ArrayList<Integer>();
	List listevalbean=new ArrayList<EmailEvaluateurBean>();
	//EmailEvaluateurBean evalbean=new EmailEvaluateurBean();
	SuiviCompagneModel init= new SuiviCompagneModel();
	Set<String> setselected = selectedCheckBox.keySet( );
	ArrayList<String> listselected = new ArrayList<String>(setselected);
	Iterator<String>iterator=listselected.iterator();
	iterator=listselected.iterator();
	while (iterator.hasNext())
	{
		String cles=(String)iterator.next();
		Checkbox checkBox=selectedCheckBox.get(cles);
		if (selectedCheckBox.get(cles).isChecked()){
		   //System.out.println("value>>"+ selectedCheckBox.get(cles).getValue()+"contexted>>"+ selectedCheckBox.get(cles).getContext()+"label>>"+selectedCheckBox.get(cles).getLabel());
		   EmailEvaluateurBean evalbean=new EmailEvaluateurBean();
		   evalbean.setId_employe(Integer.parseInt(selectedCheckBox.get(cles).getValue()));
		   evalbean.setPourcentage(Integer.parseInt(selectedCheckBox.get(cles).getName()));
		    evalbean.setEmail(selectedCheckBox.get(cles).getContext());
		   listevalbean.add(evalbean);
		}
	}
	
	init.sendAlertEvaluateur(listevalbean);
				
	}
public void onClick$valider() throws SQLException, InterruptedException, ParseException {
	SuiviCompagneModel init= new SuiviCompagneModel();
	 Map map = new HashMap();
    //Map map_struct = new HashMap();
     map=init.getCompagneList();
    //map_struct=init.getStructEntrepriseList();
    Integer compagne=(Integer) map.get(comp_list.getSelectedItem().getLabel());
  
    if (compagne!=-1){
     
    
				if (Messagebox.show("Voulez vous valider la compagne XXXXX", "Prompt", Messagebox.YES|Messagebox.NO,
					    Messagebox.QUESTION) == Messagebox.YES) {
					    //System.out.println("pressyes");
					    if (init.validerCompagne(compagne) ){
					    	Messagebox.show("Compagne validée avec succès", "Information",Messagebox.OK, Messagebox.INFORMATION); 
					    }
					
					return;
				}
				
				else{
					return;
				}
    }
    
    else{
    	Messagebox.show("Merci de valider une  vague  à la fois", "Information",Messagebox.OK, Messagebox.INFORMATION);
    	return;
    }
}

public void onSelect$comp_list() throws SQLException {
SuiviCompagneModel init= new SuiviCompagneModel(); 	
	
    Map map = new HashMap();
    //Map map_struct = new HashMap();
    map=init.getCompagneList();
    //map_struct=init.getStructEntrepriseList();
    Integer compagne=(Integer) map.get(comp_list.getSelectedItem().getLabel());
    //String structure=struct_list.getSelectedItem().getLabel();
    if (compagne==-1) {
    	model=init.uploadListEvaluateur();
    }
    else {
    	model=init.filtrerListEvaluateur(compagne);
    }
	
	//binder = new AnnotateDataBinder(comp);
	binder.loadAll();
}

}
