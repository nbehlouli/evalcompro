package administration.action;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.lang.Strings;
import org.zkoss.zk.au.out.AuClearWrongValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

public class StructureEntreprise extends GenericForwardComposer {

	Listbox structureEntrepriselb;
	Textbox codeStructure;
	//Listbox titlelb;

	AnnotateDataBinder binder;

	List<Person> model = new ArrayList<Person>();
	List<String> titleModel = new ArrayList<String>();
	Person selected;

	public StructureEntreprise() {
	}

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// création de la structure de l'entreprise bean
		comp.setVariable(comp.getId() + "Ctrl", this, true);
		model.add(new Person("Brian", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));
		model.add(new Person("John", "Tester", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));
		model.add(new Person("Sala", "Manager", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));
		model.add(new Person("Peter", "Architect", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));
		model.add(new Person("Peter", "Architect", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));
		model.add(new Person("Peter", "Architect", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));
		model.add(new Person("Peter", "Architect", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));
		model.add(new Person("Peter", "Architect", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));
		model.add(new Person("Peter", "Architect", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));
		model.add(new Person("Peter", "Architect", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));
		model.add(new Person("Peter", "Architect", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));
		model.add(new Person("Peter", "Architect", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer"));

		titleModel.add("");
		titleModel.add("Engineer");
		titleModel.add("Tester");
		titleModel.add("Manager");
		titleModel.add("Architect");
		System.out.println("11111111111111");
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
	}

	public List getModel() {
		return model;
	}

	public List getTitleModel() {
		return titleModel;
	}

	public Person getSelected() {
		return selected;
	}

	public void setSelected(Person selected) {
		this.selected = selected;
	}

	public void onClick$add() {
		Person person = new Person(getSelectedName(), "aa", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer", "Engineer");
		model.add(person);
		selected = person;
		binder.loadAll();
	}

	public void onClick$update() {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		selected.setName(getSelectedName());
		
		binder.loadAll();
	}

	public void onClick$delete() {
		if (selected == null) {
			alert("Aucune donnée n'a été selectionnée");
			return;
		}
		model.remove(selected);
		selected = null;

		binder.loadAll();
	}



	public void onSelect$structureEntrepriselb() {
		closeErrorBox(new Component[] { codeStructure });
	}
	
	
	private void closeErrorBox(Component[] comps){
		for(Component comp:comps){
			Executions.getCurrent().addAuResponse(null,new AuClearWrongValue(comp));
		}
	}



	private String getSelectedName() throws WrongValueException {
		String name = codeStructure.getValue();
		if (Strings.isBlank(name)) {
			throw new WrongValueException(codeStructure, "Aucune valeur ne doit être vide!");
		}
		return name;
	}

	public class Person {
		public String name;
		public String title;
		public String title1;
		public String title2;
		public String title3;
		public String title4;
		public String title5;
		public String title6;
		public String title7;
		public String title8;
		public String title9;
		public String title10;
		public String title11;
		public String title12;
		
		public Person(String name, String title, String title1, String title2, String title3, String title4, String title5, String title6, String title7, String title8, String title9, String title10, String title11, String title12) {
			this.name = name;
			this.title = title;
			this.title1 = title1;
			this.title2 = title2;
			this.title3 = title3;
			this.title4 = title4;
			this.title5 = title5;
			this.title6 = title6;
			this.title7 = title7;
			this.title8 = title8;
			this.title9 = title9;
			this.title10 = title10;
			this.title11 = title11;
			this.title12 = title12;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
			this.title1 = "Tester";
			this.title2 = "Tester";
			this.title3 = "Tester";
			this.title4 = "Tester";
			this.title5 = "Tester";
			this.title6 = "Tester";
			this.title7 = "Tester";
			this.title8 = "Tester";
			this.title9 = "Tester";
			this.title10 = "Tester";
			this.title11 = "Tester";
			this.title12 = "Tester";
		}
	}

}
