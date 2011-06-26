/**
 * 
 */
package administration.bean;

import common.AbstractEnterpriseObject;

/**
 * @author FTERZI
 *
 */
public class CompagneType
    extends AbstractEnterpriseObject
{

    private static final long serialVersionUID = 1L;

    private int idCompagneType;
    private String libelleCompagneType;

    public CompagneType()
    {
        // TODO Auto-generated constructor stub
    }

    public CompagneType( int id, String libelle )
    {
        this.idCompagneType = id;
        this.libelleCompagneType = libelle;
    }
    
    public CompagneType( int id )
    {
        this.idCompagneType = id;
    }

    public int getIdCompagneType()
    {
        return idCompagneType;
    }

    public void setIdCompagneType( int idCompagneType )
    {
        this.idCompagneType = idCompagneType;
    }

    public String getLibelleCOmpagneType()
    {
        return libelleCompagneType;
    }

    public void setLibelleCOmpagneType( String libelleCOmpagneType )
    {
        this.libelleCompagneType = libelleCOmpagneType;
    }

}
