/**
 * 
 */
package administration.bean;

import common.AbstractEnterpriseObject;

/**
 * @author FTERZI
 *
 */
public class Formation
    extends AbstractEnterpriseObject
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String codeFormation;

    private String libelleFormation;

    private String libelleDiplome;

    /**
     * 
     */
    public Formation()
    {
        // TODO Auto-generated constructor stub
    }

    public Formation( String codeFormation, String libelleFormation, String libelleDiplome )
    {
        super();
        this.codeFormation = codeFormation;
        this.libelleFormation = libelleFormation;
        this.libelleDiplome = libelleDiplome;
    }

    public String getCodeFormation()
    {
        return codeFormation;
    }

    public void setCodeFormation( String codeFormation )
    {
        this.codeFormation = codeFormation;
    }

    public String getLibelleFormation()
    {
        return libelleFormation;
    }

    public void setLibelleFormation( String libelleFormation )
    {
        this.libelleFormation = libelleFormation;
    }

    public String getLibelleDiplome()
    {
        return libelleDiplome;
    }

    public void setLibelleDiplome( String libelleDiplome )
    {
        this.libelleDiplome = libelleDiplome;
    }

}
