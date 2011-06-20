/**
 * 
 */
package administration.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import administration.bean.Formation;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

/**
 * @author FTERZI
 *
 */
public class FormationModel
{

    private static final String SEL_FORMATION = "SELECT code_formation,libelle_formation,libelle_diplome FROM formation";

    private static final String INS_FORMATION = "INSERT INTO formation (code_formation,libelle_formation,libelle_diplome) VALUES (#code_formation,#libelle_formation,#libelle_diplome)";

    private static final String UPD_FORMATION = "UPDATE formation SET libelle_formation = #libelle_formation ,"
        + "libelle_diplome = #libelle_diplome WHERE code_formation = #code_formation";

    private static final String DEL_FORMATION = "DELETE FROM formation WHERE code_formation = #code_formation";

    private ArrayList<Formation> formations = null;

    public ArrayList<Formation> getAllFormations()
        throws SQLException
    {

        formations = new ArrayList<Formation>();

        CreateDatabaseCon dbcon = new CreateDatabaseCon();
        Connection conn = (Connection) dbcon.connectToEntrepriseDB();
        Statement stmt;

        try
        {
            stmt = (Statement) conn.createStatement();
            String select_all_formation = SEL_FORMATION;

            ResultSet rs = (ResultSet) stmt.executeQuery( select_all_formation );

            while ( rs.next() )
            {
                if ( rs.getRow() >= 1 )
                {
                    Formation formation = new Formation();
                    formation.setCodeFormation( rs.getString( "code_formation" ) );
                    formation.setLibelleFormation( rs.getString( "libelle_formation" ) );
                    formation.setLibelleDiplome( rs.getString( "libelle_diplome" ) );

                    formations.add( formation );

                }
                else
                {
                    return formations;
                }

            }
            conn.close();
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            //((java.sql.Connection) dbcon).close();
            e.printStackTrace();
            conn.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            conn.close();
        }

        return formations;

    }

    public void setFormations( ArrayList<Formation> formations )
    {
        this.formations = formations;
    }

    public ArrayList<Formation> getFormations()
    {
        return formations;
    }

    public boolean controleIntegrite( Formation addedData )
    {
        try
        {
            if ( addedData.getCodeFormation().length() > 5 )
            {
                Messagebox.show( "La taille du champ Code Formation ne doit pas depasser 5 caracteres", "Erreur",
                                 Messagebox.OK, Messagebox.ERROR );
                return false;
            }
        }
        catch ( InterruptedException e1 )
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return true;
    }

    public boolean addFormation( Formation addedData )
    {

        CreateDatabaseCon dbcon = new CreateDatabaseCon();
        Connection conn = (Connection) dbcon.connectToEntrepriseDB();
        Statement stmt;

        try
        {
            // SELECT code_formation,libelle_formation,libelle_diplome FROM formation
            stmt = (Statement) conn.createStatement();
            String ins_formation = INS_FORMATION;
            ins_formation = ins_formation.replaceAll( "#code_formation", "'" + addedData.getCodeFormation() + "'" );
            ins_formation = ins_formation
                .replaceAll( "#libelle_formation", "'" + addedData.getLibelleFormation() + "'" );
            ins_formation = ins_formation.replaceAll( "#libelle_diplome", "'" + addedData.getLibelleDiplome() + "'" );

            stmt.execute( ins_formation );
            conn.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();

            // TODO Auto-generated catch block
            try
            {
                conn.close();
            }
            catch ( SQLException e1 )
            {
                // TODO Auto-generated catch block

                e1.printStackTrace();
                //return false;
            }

            return false;
        }

        return true;
    }

    public boolean updFormation( Formation data )
    {

        CreateDatabaseCon dbcon = new CreateDatabaseCon();
        Connection conn = (Connection) dbcon.connectToEntrepriseDB();
        Statement stmt;

        try
        {
            stmt = (Statement) conn.createStatement();
            String upd_formation = UPD_FORMATION;
            upd_formation = upd_formation.replaceAll( "#code_formation", "'" + data.getCodeFormation() + "'" );
            upd_formation = upd_formation.replaceAll( "#libelle_formation", "'" + data.getLibelleFormation() + "'" );
            upd_formation = upd_formation.replaceAll( "#libelle_diplome", "'" + data.getLibelleDiplome() + "'" );

            stmt.execute( upd_formation );
            conn.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();

            // TODO Auto-generated catch block
            try
            {
                conn.close();
            }
            catch ( SQLException e1 )
            {
                // TODO Auto-generated catch block

                e1.printStackTrace();
                //return false;
            }

            return false;
        }

        return true;
    }

    public boolean delFormation( Formation data )
    {

        CreateDatabaseCon dbcon = new CreateDatabaseCon();
        Connection conn = (Connection) dbcon.connectToEntrepriseDB();
        Statement stmt;

        try
        {
            stmt = (Statement) conn.createStatement();
            String del_formation = DEL_FORMATION;
            del_formation = del_formation.replaceAll( "#code_formation", "'" + data.getCodeFormation() + "'" );

            stmt.execute( del_formation );
            conn.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();

            // TODO Auto-generated catch block
            try
            {
                conn.close();
            }
            catch ( SQLException e1 )
            {
                // TODO Auto-generated catch block

                e1.printStackTrace();
                //return false;
            }

            return false;
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    public List<Formation> uploadXLSFile( InputStream inputStream )
    {
        formations = new ArrayList<Formation>();

        // Creer l'objet representant le fichier Excel
        try
        {
            HSSFWorkbook fichierExcel = new HSSFWorkbook( inputStream );
            // creer l'objet representant les lignes Excel
            HSSFRow ligne;

            // creer l'objet representant les cellules Excel
            HSSFCell cellule;

            //lecture de la premi�re feuille excel
            HSSFSheet feuilleExcel = fichierExcel.getSheetAt( 0 );

            // lecture du contenu de la feuille excel
            int nombreLigne = feuilleExcel.getLastRowNum() - feuilleExcel.getFirstRowNum();

            for ( int numLigne = 1; numLigne <= nombreLigne; numLigne++ )
            {

                ligne = feuilleExcel.getRow( numLigne );
                int nombreColonne = ligne.getLastCellNum() - ligne.getFirstCellNum();
                Formation formation = new Formation();
                // parcours des colonnes de la ligne en cours
                for ( short numColonne = 0; numColonne < nombreColonne; numColonne++ )
                {

                    cellule = ligne.getCell( numColonne );

                    String valeur = cellule.getStringCellValue();

                    if ( numColonne == 0 )
                    {
                        formation.setCodeFormation( valeur );
                    }
                    else if ( numColonne == 1 )
                    {
                        formation.setLibelleFormation( valeur );
                    }
                    else if ( numColonne == 2 )
                    {
                        formation.setLibelleDiplome( valeur );
                    }
                }//fin for colonne
                formations.add( formation );
            }//fin for ligne

        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return formations;
    }

    public HashMap<String, List<Formation>> ChargementDonneedansBdd( List<Formation> liste )
        throws Exception
    {
        //Verification de l'integrit� des donn�es � inserer doublon dans le fichier
        List<Formation> listeAInserer = new ArrayList<Formation>();
        List<Formation> listeDonneesRejetes = new ArrayList<Formation>();

        for ( int i = 0; i < liste.size(); i++ )
        {
            Formation donnee = liste.get( i );
            boolean donneerejete = false;
            for ( int j = i + 1; j < liste.size(); j++ )
            {
                Formation donnee2 = liste.get( j );
                if ( donnee.getCodeFormation().equals( donnee2.getCodeFormation() ) )
                {
                    listeDonneesRejetes.add( donnee );
                    donneerejete = true;

                }
            }
            if ( ( i == liste.size() - 1 ) || ( i == 0 ) || ( donneerejete == false ) )
                listeAInserer.add( donnee );

        }

        //Verification de l'integrit� des donn�es � inserer doublon avec les donn�es de la base

        List<Formation> listeAInsererFinal = new ArrayList<Formation>();
        ArrayList<Formation> strctureEntreprisebdd = getAllFormations();
        Iterator<Formation> iterator = listeAInserer.iterator();

        while ( iterator.hasNext() )
        {

            Formation bean = (Formation) iterator.next();

            Iterator<Formation> index = strctureEntreprisebdd.iterator();
            boolean donneerejete = false;
            while ( index.hasNext() )
            {

                Formation bean2 = (Formation) index.next();
                if ( bean.getCodeFormation().equals( bean2.getCodeFormation() ) )
                {

                    listeDonneesRejetes.add( bean );
                    donneerejete = true;
                    continue;
                }
            }
            if ( !donneerejete )
            {

                listeAInsererFinal.add( bean );
            }

        }

        //Insertion des donn�es dans la table Structure_entreprise
        Iterator<Formation> index = listeAInsererFinal.iterator();
        while ( index.hasNext() )
        {
            Formation donneeBean = (Formation) index.next();

            addFormation( donneeBean );
        }

        HashMap<String, List<Formation>> donneeMap = new HashMap<String, List<Formation>>();
        donneeMap.put( "inserer", listeAInsererFinal );
        donneeMap.put( "supprimer", listeDonneesRejetes );

        return donneeMap;
    }

    public List<Formation> uploadXLSXFile( InputStream inputStream )
    {
        formations = new ArrayList<Formation>();

        // Creer l'objet representant le fichier Excel
        try
        {
            XSSFWorkbook fichierExcel = new XSSFWorkbook( inputStream );
            // creer l'objet representant les lignes Excel
            XSSFRow ligne;

            // creer l'objet representant les cellules Excel
            XSSFCell cellule;

            //lecture de la premi�re feuille excel
            XSSFSheet feuilleExcel = fichierExcel.getSheetAt( 0 );

            // lecture du contenu de la feuille excel
            int nombreLigne = feuilleExcel.getLastRowNum() - feuilleExcel.getFirstRowNum();

            for ( int numLigne = 1; numLigne <= nombreLigne; numLigne++ )
            {

                ligne = feuilleExcel.getRow( numLigne );
                int nombreColonne = ligne.getLastCellNum() - ligne.getFirstCellNum();
                Formation formation = new Formation();
                // parcours des colonnes de la ligne en cours
                for ( short numColonne = 0; numColonne < nombreColonne; numColonne++ )
                {
                    try
                    {

                        cellule = ligne.getCell( numColonne );

                        String valeur = cellule.getStringCellValue();

                        if ( numColonne == 0 )
                        {
                            formation.setCodeFormation( valeur );
                        }
                        else if ( numColonne == 1 )
                        {
                            formation.setLibelleFormation( valeur );
                        }
                        else if ( numColonne == 2 )
                        {
                            formation.setLibelleDiplome( valeur );
                        }

                    }
                    catch ( Exception e )
                    {

                    }

                }//fin for colonne
                formations.add( formation );
            }//fin for ligne

        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return formations;
    }

    public void downloadFormationDataToXls()
    {

        //recup�ration du contenu de la table Structure_entreprise
        try
        {
            ArrayList<Formation> formations = getAllFormations();

            //creation du fichier xls

            Iterator<Formation> index = formations.iterator();
            //creation d'un document excel 
            HSSFWorkbook workBook = new HSSFWorkbook();

            //creation d'une feuille excel
            HSSFSheet sheet = workBook.createSheet( "formation" );

            //creation de l'ent�te du document excel
            HSSFRow row = sheet.createRow( 0 );
            HSSFCell cell = row.createCell( (short) 0 );

            HSSFCellStyle cellStyle = null;
            cellStyle = workBook.createCellStyle();
            cellStyle.setFillForegroundColor( HSSFColor.RED.index );
            cellStyle.setFillPattern( HSSFCellStyle.SOLID_FOREGROUND );
            cell.setCellValue( "Code Formation" );
            cell.setCellStyle( cellStyle );
            HSSFCell cell1 = row.createCell( (short) 1 );
            cell1.setCellValue( "Libelle Formation" );
            cell1.setCellStyle( cellStyle );

            HSSFCell cell2 = row.createCell( (short) 2 );
            cell2.setCellValue( "Libelle Diplome" );
            cell2.setCellStyle( cellStyle );

            int i = 1;
            while ( index.hasNext() )
            {

                Formation donnee = (Formation) index.next();

                HSSFRow row1 = sheet.createRow( i );
                HSSFCell cel = row1.createCell( (short) 0 );
                cel.setCellValue( donnee.getCodeFormation() );

                cel = row1.createCell( (short) 1 );
                cel.setCellValue( donnee.getLibelleFormation() );

                cel = row1.createCell( (short) 2 );
                cel.setCellValue( donnee.getLibelleDiplome() );
                i++;
            }

            FileOutputStream fOut;
            try
            {
                fOut = new FileOutputStream( "Formation.xls" );
                workBook.write( fOut );
                fOut.flush();
                fOut.close();

                File file = new File( "Formation.xls" );
                Filedownload.save( file, "XLS" );
            }
            catch ( FileNotFoundException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch ( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
