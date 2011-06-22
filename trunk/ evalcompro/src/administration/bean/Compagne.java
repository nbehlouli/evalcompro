/**
 * 
 */
package administration.bean;

import java.util.Date;

import common.AbstractEnterpriseObject;

/**
 * @author FTERZI
 *
 */
public class Compagne
    extends AbstractEnterpriseObject
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int idCompagne;

    private String codeDirection;

    private String codeService;

    private int idEmploye;

    private Date dateDebut;

    private Date dateFin;

    private String libelleCompagne;

    private String codeStructure;

    private CompagneType compagneType;

    /**
     * 
     */
    public Compagne()
    {
        // TODO Auto-generated constructor stub
    }

    public int getIdCompagne()
    {
        return idCompagne;
    }

    public void setIdCompagne( int idCompagne )
    {
        this.idCompagne = idCompagne;
    }

    public String getCodeDirection()
    {
        return codeDirection;
    }

    public void setCodeDirection( String codeDirection )
    {
        this.codeDirection = codeDirection;
    }

    public String getCodeService()
    {
        return codeService;
    }

    public void setCodeService( String codeService )
    {
        this.codeService = codeService;
    }

    public int getIdEmploye()
    {
        return idEmploye;
    }

    public void setIdEmploye( int idEmploye )
    {
        this.idEmploye = idEmploye;
    }

    public Date getDateDebut()
    {
        return dateDebut;
    }

    public void setDateDebut( Date dateDebut )
    {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin()
    {
        return dateFin;
    }

    public void setDateFin( Date dateFin )
    {
        this.dateFin = dateFin;
    }

    public String getLibelleCompagne()
    {
        return libelleCompagne;
    }

    public void setLibelleCompagne( String libelleCompagne )
    {
        this.libelleCompagne = libelleCompagne;
    }

    public String getCodeStructure()
    {
        return codeStructure;
    }

    public void setCodeStructure( String codeStructure )
    {
        this.codeStructure = codeStructure;
    }

    public CompagneType getCompagneType()
    {
        return compagneType;
    }

    public void setCompagneType( CompagneType compagneType )
    {
        this.compagneType = compagneType;
    }

}
