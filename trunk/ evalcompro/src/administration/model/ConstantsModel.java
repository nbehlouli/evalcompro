/**
 * 
 */
package administration.model;

/**
 * @author FTERZI
 *
 */
public class ConstantsModel
{

    /*===============*/
    /* employe       */
    /*===============*/
    
    public static final String SEL_EMPLOYE = "SELECT id_employe,nom,prenom,date_naissance FROM employe";
    
    public static final String INS_EMPLOYE = "INSERT INTO employe (nom,prenom,date_naissance) VALUES(#nom,#prenom,#date_naissance)";
    
    public static final String UPD_EMPLOYE = "UPDATE employe SET nom=#nom,prenom=#prenom,date_naissance=#date_naissance WHERE id_employe=#id_employe";
    
    public static final String DEL_EMPLOYE = "DELETE FROM employe WHERE id_employe = #id_employe";
    
    /*===============*/
    /* compagne_type */
    /*===============*/
    public static final String SEL_COMPAGNE_TYPE = "SELECT id_compagne_type,compagne_type FROM compagne_type";
    
    public static final String INS_COMPAGNE_TYPE = "INSERT INTO compagne_type (id_compagne_type,compagne_type) VALUES(#id_compagne_type,#compagne_type)";
    
    public static final String UPD_COMPAGNE_TYPE = "UPDATE compagne_type SET compagne_type=#compagne_type WHERE id_compagne_type=#id_compagne_type";
    
    public static final String DEL_COMPAGNE_TYPE = "DELETE FROM compagne_type WHERE id_compagne_type=#id_compagne_type";
    
    public ConstantsModel()
    {
        // TODO Auto-generated constructor stub
    }

}
