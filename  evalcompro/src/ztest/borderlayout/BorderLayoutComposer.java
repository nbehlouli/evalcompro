package ztest.borderlayout;

import java.io.File;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;





public class BorderLayoutComposer extends GenericForwardComposer{
	
	ListModelList menuModel = new ListModelList();
	Listbox menuListbox;
	Tree explorerTree;
	MenuNodeSelectListener listener = new MenuNodeSelectListener();
	MenuNodeItemRenderer renderer = new MenuNodeItemRenderer();
	Div contentDiv;
	
	
	public BorderLayoutComposer()
	{
		menuModel.add(new MenuNode("Fiche evaluation","../menu/borderlayout_fn1.zul"));
		menuModel.add(new MenuNode("Statistique","../menu/borderlayout_fn2.zul"));
		menuModel.add(new MenuNode("Rapport","../menu/borderlayout_fn3.zul"));
		menuModel.add(new MenuNode("Rapport2","../menu/borderlayout_fn3.zul"));
	}
	
	
	public void doAfterCompose(Component comp) throws Exception 
	{
		super.doAfterCompose(comp);
		Component composantpere=(Component)this.self;
		menuListbox.setModel(menuModel);
		menuListbox.setItemRenderer(renderer);
		menuListbox.addEventListener(Events.ON_SELECT,listener);


	}

	class MenuNode 
	{
		String label;
		String link;
		public MenuNode(String label,String link)
		{
			this.label = label;
			this.link = link;
		}
		public String getLabel() 
		{
			return label;
		}
		public void setLabel(String label) 
		{
			this.label = label;
		}
		public String getLink() 
		{
			return link;
		}
		public void setLink(String link) 
		{
			this.link = link;
		}
	}
	
	class MenuNodeItemRenderer implements ListitemRenderer
	{

		public void render(Listitem item, Object data) throws Exception 
		{
			MenuNode node = (MenuNode)data;
			item.setImage("../menu/icon-24x24.png");
			item.setLabel(node.getLabel());
			item.setValue(node);
		}
	}
	
	class MenuNodeSelectListener implements EventListener{
		public void onEvent(Event event) throws Exception {
			Listitem item = menuListbox.getSelectedItem();
			contentDiv.getChildren().clear();
			if(item!=null){
				MenuNode node = (MenuNode)item.getValue();
				Executions.createComponents(node.getLink(),contentDiv,null);
			}
		}		
	}
	
}
