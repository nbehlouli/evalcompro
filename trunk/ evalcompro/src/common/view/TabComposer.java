package common.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;

import org.zkoss.zk.ui.util.GenericForwardComposer;

import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import common.ApplicationFacade;
import common.bean.ArborescenceMenu;
import common.bean.EcranBean;


public class TabComposer extends GenericForwardComposer{
	

	Div contentDiv;
	Tabbox tb;
	Tabs tbtabs;
	Tabpanels tbpanels;
	public TabComposer(){


		
	}
	
	
	public void doAfterCompose(Component comp) throws Exception 
	{
		super.doAfterCompose(comp);
		
		try
		{
			
	
			//creation de tab0 et sa fermeture pour qu'i n'y ait pas de décalage lors de l'ajout des listboxs
			Tab newTab0 = new Tab(); 
			newTab0.isClosable();	
			tbtabs.appendChild(newTab0);			
			newTab0.close();
			
			
			//recuperation de l astructure du menu et sa construction
			ArborescenceMenu arborescenceMenuBean=ApplicationFacade.getInstance().getArborescenceMenubean();
			
			HashMap<String, List<EcranBean>> listeMenu=arborescenceMenuBean.getArborescenceMenu();
			
			Set<String> set = listeMenu.keySet( );
			@SuppressWarnings("rawtypes")
			Iterator it=set.iterator();
			while(it.hasNext())
			{
				String cles=(String) it.next();
				List<EcranBean> liste=listeMenu.get(cles);
				Iterator<EcranBean> iterator=liste.iterator();
				
				//creation du tab
				Tab newTab = new Tab();
				newTab.setLabel(cles);
			
				Tabpanel newPanel = new Tabpanel();
				Label newLabel = new Label();
				newLabel.setValue("");
			
				newPanel.appendChild(newLabel);
				
				//creation du menu
				MenuComposer borderLayout=new MenuComposer(liste);
				borderLayout.setContentDiv(contentDiv);
				
				newPanel.appendChild(borderLayout.getMenuListbox());
				System.out.println("avant");
				tbpanels.appendChild(newPanel);
				System.out.println("apres");
				tbtabs.appendChild(newTab);		
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	 	
}
