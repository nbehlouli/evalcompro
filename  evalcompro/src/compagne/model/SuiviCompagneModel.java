package compagne.model;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;

import administration.bean.AdministrationLoginBean;
import administration.bean.CompteBean;
import administration.bean.SelCliBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;
import common.InitContext;
import common.PwdCrypt;
import compagne.bean.CompagneListBean;
import compagne.bean.EmailEvaluateurBean;
import compagne.bean.SuiviCompagneBean;

public class SuiviCompagneModel {
	
	private ArrayList<SuiviCompagneBean>  listevaluateur =null; 
	
public List uploadListEvaluateur() throws SQLException{
		
		
		listevaluateur = new ArrayList<SuiviCompagneBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		String sqlquery=	"select id_employe,concat (nom ,' ',prenom) as nom_evaluateur ,email, round(sum(nbfichevalide)*100/ sum(totalemploye)) as progress " +
				"from (	select id_evaluateur as evaluateur ,count(r.id_employe) as nbfichevalide,0 as totalemploye from planning_evaluation t ,fiche_validation r" +
				" where t.id_planning_evaluation=r.id_planning_evaluation and t.id_employe=r.id_employe" +
				" and fiche_valide=1 group by id_evaluateur	union select id_evaluateur as evaluateur ,0 as nbfichevalide ,count(t.id_employe)as totalemploye from planning_evaluation t" +
				" group by id_evaluateur) as t2,employe  where id_employe=evaluateur group by 1,2,3";
		//System.out.println(sqlquery);
		
		try {
			stmt = (Statement) conn.createStatement();
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sqlquery);
		
			while(rs.next()){
				
				SuiviCompagneBean evalbean=new SuiviCompagneBean();
				evalbean.setEvaluateur(rs.getString("nom_evaluateur"));
				evalbean.setPourcentage(rs.getInt("progress"));
				evalbean.setId_employe(rs.getInt("id_employe"));
				evalbean.setEmail(rs.getString("email"));
				
				listevaluateur.add(evalbean);
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listevaluateur;
	
		
		
	}
	public HashMap getCompagneList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String db_list="select id_compagne,libelle_compagne from compagne_evaluation"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(db_list);
			
			
			while(rs.next()){
				map.put( rs.getString("libelle_compagne"),rs.getInt("id_compagne"));
	        }
			map.put("Toutes les vagues",-1);
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}
	

public List getCompagneEmployeList(String id_employe ) throws SQLException
{
	ArrayList<CompagneListBean> listcompagne = new ArrayList<CompagneListBean>();
	CompagneListBean cbn=new CompagneListBean();
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;

	
	try 
	{
		stmt = (Statement) conn.createStatement();
		String sql_query="select  distinct e.id_compagne ,libelle_compagne,date_format(date_debut,'%d-%m-%Y') as date_debut,date_format(date_fin,'%d-%m-%Y') as date_fin,compagne_type" +
				     " from compagne_evaluation e , compagne_type t,planning_evaluation r" +
				     " where e.id_compagne_type=t.id_compagne_type and   r.id_compagne=e.id_compagne" +
				     " and   r.id_evaluateur =#idemploye"; 
		sql_query = sql_query.replaceAll("#idemploye", id_employe);
		ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
		//System.out.println(sql_query);
		
		while(rs.next()){
			//map.put( rs.getString("libelle_compagne"),rs.getInt("id_compagne"));
			cbn.setId_compagne(rs.getInt("id_compagne"));
			cbn.setLibelle_compagne(rs.getString("libelle_compagne"));
			cbn.setDate_debut(rs.getString("date_debut"));
			cbn.setDate_fin(rs.getString("date_fin"));
			cbn.setCompagne_type(rs.getString("compagne_type"));
			listcompagne.add(cbn);
        }
		
		stmt.close();conn.close();
	} 
	catch (SQLException e){
			e.printStackTrace();
			stmt.close();conn.close();
	}
	
	return listcompagne;
}

	public HashMap getStructEntrepriseList() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String db_list=" select distinct v.code_structure,concat_ws (' > ',libelle_division,NULL,libelle_direction,libelle_unite,NULL,libelle_departement,libelle_service, null,libelle_section) as structure" +
					       "  from planning_evaluation v, structure_entreprise t where v.code_structure=t.code_structure"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(db_list);
			
			
			while(rs.next()){
				map.put( rs.getString("v.code_structure"),rs.getString("structure"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
		

	}
	
public List filtrerListEvaluateur(int id_compagne) throws SQLException{
		
		
		listevaluateur = new ArrayList<SuiviCompagneBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		String sqlquery="select id_employe,concat (nom ,' ',prenom) as nom_evaluateur , email,round(sum(nbfichevalide)*100/ sum(totalemploye)) as progress"+
						" from (	select id_evaluateur as evaluateur ,count(r.id_employe) as nbfichevalide,0 as totalemploye from planning_evaluation t ,fiche_validation r" +
						" where t.id_planning_evaluation=r.id_planning_evaluation" +
						" and t.id_employe=r.id_employe  and fiche_valide=1  and  t.id_compagne=#compgane "+
						" group by id_evaluateur   union select id_evaluateur as evaluateur ,0 as nbfichevalide ,count(t.id_employe)as totalemploye" +
						" from planning_evaluation t where   t.id_compagne=#compgane  group by id_evaluateur" +
						" ) as t2,employe where id_employe=evaluateur group by 1,2,3";
		sqlquery = sqlquery.replaceAll("#compgane", Integer.toString(id_compagne));
		//sqlquery = sqlquery.replaceAll("#structure","'"+ structure+"'");
		
         //System.out.println(sqlquery);
		
		try {
			stmt = (Statement) conn.createStatement();
			
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sqlquery);
		
			while(rs.next()){
				
				SuiviCompagneBean evalbean=new SuiviCompagneBean();
				evalbean.setEvaluateur(rs.getString("nom_evaluateur"));
				evalbean.setPourcentage(rs.getInt("progress"));
				evalbean.setId_employe(rs.getInt("id_employe"));
				evalbean.setEmail(rs.getString("email"));
				listevaluateur.add(evalbean);
			
				   
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listevaluateur;
	
}	
public List getEmailEvaluateur(List listemploye) throws SQLException{
	
	Iterator it = listemploye.iterator();
	List listemail = new ArrayList<EmailEvaluateurBean>();
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt = null;
	String lec="";
	String lec_save="";
	EmailEvaluateurBean cpb;
	EmailEvaluateurBean evalbean=new EmailEvaluateurBean();
		
	while (it.hasNext()){
		cpb  = (EmailEvaluateurBean) it.next();
		lec=String.valueOf(cpb.getId_employe());
		lec=','+lec;
		lec_save=lec_save+lec;
		
 	}
	
	String sqlquery=	"select id_employe,email from employe where id_employe in (#listemploye)";
	sqlquery = sqlquery.replaceAll("#listemploye", lec_save);
	sqlquery = sqlquery.replace("(,","(");
	
	
	try {
		stmt = (Statement) conn.createStatement();
		
		
		ResultSet rs = (ResultSet) stmt.executeQuery(sqlquery);
	
		while(rs.next()){
			
			
			evalbean.setEmail(rs.getString("email"));
			evalbean.setId_employe(rs.getInt("id_employe"));
			listemail.add(evalbean);
			   
				
			}
		stmt.close();
		conn.close();
		
	} catch (SQLException e) {
		stmt.close();
		conn.close();
		
	}
	return listemail;

	
	
}
public void sendAlertEvaluateur(List recipient) throws SQLException{
	final InitContext intctx = new InitContext();
    intctx.loadProperties();
	Properties props = new Properties();
	props.put("mail.smtp.host", intctx.getHost());
	props.put("mail.smtp.socketFactory.port", intctx.getPort());
	props.put("mail.smtp.socketFactory.class",
			"javax.net.ssl.SSLSocketFactory");
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.port", intctx.getPort());
	List  listcompagne = new ArrayList<CompagneListBean>();
	CompagneListBean cmp=new CompagneListBean();
	
	
	Iterator it = recipient.iterator();

	Session session = Session.getDefaultInstance(props,
		new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(intctx.getUser(),intctx.getPassword());
			}
		});

	try {

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(intctx.getFrom()));
		String lec;
		EmailEvaluateurBean cpb;
		while (it.hasNext()){
			cpb  = (EmailEvaluateurBean) it.next();
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(cpb.getEmail()));
			message.setSubject(intctx.getSmtpsubject_alert());
			listcompagne=getCompagneEmployeList(String.valueOf(cpb.getId_employe()));
			//System.out.println("employe>>"+cpb.getId_employe()+"email>>"+cpb.getEmail());
			Iterator itcomp = listcompagne.iterator();
			while (itcomp.hasNext()){
				cmp=(CompagneListBean) itcomp.next();
				message.setText("Attention vous etes à :  "+cpb.getPourcentage()+" % de l'etat d'avancement sur la "+ cmp.getLibelle_compagne()+
						                        ". La date de cloture aura lieu le: "+cmp.getDate_fin()+"\n"+"\n"+intctx.getText());
			}
			

			Transport.send(message);
	 	}
	
		

	} catch (MessagingException e) {
		throw new RuntimeException(e);
	}

}

public boolean validerCompagne(int idcompagne) throws ParseException
{
	
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToEntrepriseDB();
	Statement stmt;
	
	
	try 
	{
		                                                
		stmt = (Statement) conn.createStatement();
		String sql_query="INSERT INTO compagne_validation( id_compagne, compagne_valide)  VALUES(#id_compagne, 1)";
		sql_query = sql_query.replaceAll("#id_compagne", String.valueOf(idcompagne));
	
		
		 stmt.execute(sql_query);
	} 
	catch (SQLException e) 
	{
		try 
		{
			Messagebox.show("La compagne  n'a pas été validée", "Erreur",Messagebox.OK, Messagebox.ERROR);
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO Auto-generated catch block
		try {
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			
			e1.printStackTrace();
			//return false;
		}
		
		
		return false;
	}
	try {
		conn.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return true;
}
	
	/*select concat (nom ,' ',prenom) as nom_evaluateur , round(sum(nbfichevalide)*100/ sum(totalemploye)) as progress 
from (	select id_evaluateur as evaluateur ,count(r.id_employe) as nbfichevalide,0 as totalemploye 
from planning_evaluation t ,fiche_validation r 
where t.id_planning_evaluation=r.id_planning_evaluation 
and t.id_employe=r.id_employe  and fiche_valide=1  and  t.id_compagne=2 
--and t.id_employe in (select id_employe from employe where code_structure='S0003' ) 
group by id_evaluateur   
union select id_evaluateur as evaluateur ,0 as nbfichevalide ,count(t.id_employe)as totalemploye
 from planning_evaluation t where   t.id_compagne=2
--and t.id_employe in (select id_employe from employe where code_structure='S0003' ) 
group by id_evaluateur ) as t2,employe where id_employe=evaluateur group by 1*/

}
