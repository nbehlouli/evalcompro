package Statistique.action;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.util.media.AMedia;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;



import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Iframe;

import com.mysql.jdbc.Connection;
import common.CreateDatabaseCon;


public class ExtractRepCompPDFAction extends  GenericForwardComposer{
	private Iframe report;
/*public void onClick$pdfDownload() throws IOException, SQLException  {
	
	    InputStream is = null;
	    InputStream is1=null;
	    CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
        try {
            //generate report pdf stream
        	
        	// Get the real path for the report
    		String repSrc = Sessions.getCurrent().getWebApp().getRealPath("D:/cvsviews/zkevalcom/WebContent/WEB-INF/report/repcomp_pst_travail.jasper");
    		String subDir = Sessions.getCurrent().getWebApp().getRealPath("D:/cvsviews/zkevalcom/WebContent/WEB-INF/report/") + "/";

            is = this.getClass().getResourceAsStream(repSrc);
            is1 = this.getClass().getClassLoader().getResourceAsStream(repSrc);
                
            final Map params = new HashMap();
            params.put("ReportTitle", "The First Jasper Report Ever");
            params.put("MaxOrderID", new Integer(10500));
        	

            final byte[] buf = 
                JasperRunManager.runReportToPdf(is, params, conn);
                
            //prepare the AMedia for iframe
            final InputStream mediais = new ByteArrayInputStream(buf);
            final AMedia amedia = 
                new AMedia("FirstReport.pdf", "pdf", "application/pdf", mediais);
                
            //set iframe content
            report.setContent(amedia);
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
        	conn.close();
            if (is != null) {
                is.close();
            }
        }
    }*/

public void onClick$pdfDownload() throws IOException, SQLException  {
//Preparing parameters
	
	 // - Connexion à la base
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	try {
    
		 Map parametres = new HashMap();

        // - Chargement et compilation du rapport
		 File file = new File("D:\\cvsviews\\zkevalcom\\WebContent\\WEB-INF\\report\\");
		
		 JasperDesign jDesign = JRXmlLoader.load("D:\\cvsviews\\zkevalcom\\WebContent\\WEB-INF\\report\\repcomp_pst_travail.jrxml");
		 JasperReport jReport = JasperCompileManager.compileReport(jDesign);
		 JasperPrint jasperPrint = JasperFillManager.fillReport( jReport,
				 null, conn);
		byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

      
		final InputStream mediais = new ByteArrayInputStream(bytes);
        final AMedia amedia = 
            new AMedia("FirstReport.pdf", "pdf", "application/pdf", mediais);
            
        //set iframe content
        report.setContent(amedia);
    } catch (JRException e) {

        e.printStackTrace();
    }  finally {
       
             conn.close();
           
    }

}


}
