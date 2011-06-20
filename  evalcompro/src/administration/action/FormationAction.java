/**
 * 
 */
package administration.action;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import administration.bean.Formation;
import administration.bean.StructureEntrepriseBean;
import administration.model.FormationModel;

/**
 * @author FTERZI
 *
 */
public class FormationAction
    extends GenericForwardComposer
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Listbox formationlb;

    Textbox codeFormationtb;

    Textbox libelleFormationtb;

    Textbox libelleDiplometb;

    AnnotateDataBinder binder;
    
    Window win;

    List<Formation> model = new ArrayList<Formation>();

    Formation selected;

    public FormationAction()
    {
    }

    @SuppressWarnings("deprecation")
    public void doAfterCompose( Component comp )
        throws Exception
    {
        super.doAfterCompose( comp );

        // creation formation
        FormationModel formationModel = new FormationModel();
        model = formationModel.getAllFormations();
        
        comp.setVariable( comp.getId() + "Ctrl", this, true );

        binder = new AnnotateDataBinder( comp );
        

        if(model.size()!=0)
        	selected = model.get( 0 );
        
        if(formationlb.getItemCount()!=0)
        	formationlb.setSelectedIndex( 0 );
        
        binder.loadAll();

    }
    
	public List<Formation> getModel() {
		return model;
	}



	public Formation getSelected() {
		return selected;
	}

	public void setSelected(Formation selected) {
		this.selected = selected;
	}

}
