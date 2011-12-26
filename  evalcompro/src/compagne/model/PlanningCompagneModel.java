package compagne.model;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;

import administration.bean.AdministrationLoginBean;
import administration.bean.CompagneCreationBean;
import administration.bean.SelCliDBNameBean;
import administration.bean.StructureEntrepriseBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import common.ApplicationFacade;
import common.CreateDatabaseCon;
import common.InitContext;
import common.PwdCrypt;
import compagne.bean.CompagneListBean;
import compagne.bean.EmailEvaluateurBean;
import compagne.bean.PlanningAgendaBean;
import compagne.bean.PlanningCompagneListBean;
import compagne.bean.PlanningListEvaluateurBean;
import compagne.bean.SuiviCompagneBean;

public class PlanningCompagneModel {
	

private ArrayList<PlanningCompagneListBean>  listcompagne =null; 
private ListModel strset =null;
private int database=ApplicationFacade.getInstance().getClient_database_id();
	
	/**
	 * cette m�thode fournit le contenu de la table structure_entreprise
	 * @return
	 * @throws SQLException
	 */
	public List loadPlanningCompagnelist() throws SQLException{
		
		
		listcompagne = new ArrayList<PlanningCompagneListBean>();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			String sel_comp="select id_planning_evaluation,libelle_compagne , (select concat(nom,' ', prenom) from employe where id_employe in(id_evaluateur)) as evaluateur,concat (nom,' ', prenom) as evalue,intitule_poste,p.code_structure," +
					        " date_evaluation,heure_debut_evaluation,heure_fin_evaluation,lieu,personne_ressources " +
					        " from planning_evaluation p,employe e,compagne_evaluation c, poste_travail_description d " +
					        " where   p.id_compagne=c.id_compagne and   p.id_employe=e.id_employe and   p.code_poste=d.code_poste";
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sel_comp);
			
			while(rs.next()){
				
				PlanningCompagneListBean compagne=new PlanningCompagneListBean();
				compagne.setId_planning_evaluation((rs.getInt("id_planning_evaluation")));	
				compagne.setLibelle_compagne((rs.getString("libelle_compagne")));
				compagne.setEvaluateur((rs.getString("evaluateur")));
				compagne.setEvalue(((rs.getString("evalue"))));
				compagne.setIntitule_poste((((rs.getString("intitule_poste")))));
				compagne.setCode_structure((rs.getString("code_structure")));
		    	compagne.setDate_evaluation(rs.getDate("date_evaluation"));
				compagne.setHeure_debut_evaluation(rs.getString("heure_debut_evaluation"));
				compagne.setHeure_fin_evaluation(rs.getString("heure_fin_evaluation"));
				compagne.setLieu(rs.getString("lieu"));
				compagne.setPersonne_ressources(rs.getString("personne_ressources"));
				listcompagne.add(compagne);
					
				}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			stmt.close();
			conn.close();
			
		}
		return listcompagne;
	
		
		
	}
	
	/**
	 * cette m�thode permet d'inserer la donn�e addedData dans la table structure_entreprise de la base de donn�e
	 * @param addedData
	 * @return
	 * @throws ParseException 
	 */
	public boolean addPlanningCompagne(PlanningCompagneListBean addedData) throws ParseException
	{
		
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		
		try 
		{
			                                                
			stmt = (Statement) conn.createStatement();
			String sql_query="INSERT INTO planning_evaluation(id_compagne, id_employe, date_evaluation, id_evaluateur, heure_debut_evaluation, heure_fin_evaluation, lieu, code_poste, personne_ressources, code_structure)" +
					" VALUES(#id_compagne,#id_employe, #date_evaluation, #id_evaluateur, #heure_debut_evaluation, #heure_fin_evaluation, #lieu, #code_poste, #personne_ressources, #code_structure);";
			
			sql_query = sql_query.replaceAll("#id_compagne", "'"+ addedData.getId_compagne()+"'");
			sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_evalue()+"'");
			sql_query = sql_query.replaceAll("#date_evaluation", "'"+ formatter.format(addedData.getDate_evaluation())+"'");
			sql_query = sql_query.replaceAll("#id_evaluateur", "'"+ addedData.getId_evaluateur()+"'");
			sql_query = sql_query.replaceAll("#heure_debut_evaluation", "'"+ addedData.getHeure_debut_evaluation()+"'");
			sql_query = sql_query.replaceAll("#heure_fin_evaluation", "'"+ addedData.getHeure_fin_evaluation()+"'");
			sql_query = sql_query.replaceAll("#lieu", "'"+ addedData.getLieu()+"'");
			sql_query = sql_query.replaceAll("#code_poste", "'"+ addedData.getCode_poste()+"'");
			sql_query = sql_query.replaceAll("#personne_ressources", "'"+ addedData.getPersonne_ressources()+"'");
			sql_query = sql_query.replaceAll("#code_structure", "'"+ addedData.getCode_structure()+"'");
				
		//System.out.println(select_structure);
			
			 stmt.execute(sql_query);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La donn�e n'a pas �t� ins�r�e dans la base donn�es", "Erreur",Messagebox.OK, Messagebox.ERROR);
			} 
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
				return false;
			}
			
			
			return false;
		}
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * cette classe permet de controler la validit� des donn�es ins�r�es (par rapport � leurs taille)
	 * @param addedData
	 * @return
	 * @throws InterruptedException 
	 */
	
	public boolean controleIntegrite(PlanningCompagneListBean addedData ,Map heuredebut,Map heurefin) throws InterruptedException
	{
		try 
		{   	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

		       Integer int_heuredebut=(Integer) heuredebut.get(addedData.getHeure_debut_evaluation());
		       Integer int_heurefin=(Integer) heurefin.get(addedData.getHeure_fin_evaluation());

					
			
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			if(addedData.getLieu().length()>30)
			{
				Messagebox.show("Le lieu ne doit pas d�passer 30 caract�res", "Erreur",Messagebox.OK, Messagebox.ERROR);
				
				return false;
			}
			else
				if(addedData.getPersonne_ressources().length()>80)
				{
				Messagebox.show("La personne ressources ne doit pas d�passer 30 caract�res !", "Erreur",Messagebox.OK, Messagebox.ERROR);
				return false;
				}
				else
					if(int_heuredebut >= int_heurefin)
					{
					Messagebox.show("L'heure de fin de l'�valuation doit  �tre superieure � l'heure de debut !", "Erreur",Messagebox.OK, Messagebox.ERROR);
					return false;
					}
								
									
		} 
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (NumberFormatException nfe)
	    {
			Messagebox.show("Le mot de passe doit �tre un entier compos� de 8 chiffres Exemple 21012001", "Erreur",Messagebox.OK, Messagebox.ERROR);
			return false;
	    }
		
			
		return true;
	}
	
	/**
	 * Cette classe permet de mettre � jour la table structure_entreprise
	 * @param addedData
	 * @return
	 */
	public Boolean updateCompagne(PlanningCompagneListBean addedData)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
		    stmt = (Statement) conn.createStatement();
		    String sql_query="update planning_evaluation set id_compagne=#id_compagne, id_employe=#id_employe, date_evaluation=#date_evaluation, id_evaluateur=#id_evaluateur, " +
		    		         " heure_debut_evaluation=#heure_debut_evaluation, heure_fin_evaluation=#heure_fin_evaluation, lieu=#lieu," +
		    		         " code_poste=#code_poste, personne_ressources=#personne_ressources, code_structure=#code_structure  where id_planning_evaluation=#id_planning_evaluation" ;
			
	
			sql_query = sql_query.replaceAll("#id_compagne", "'"+ addedData.getId_compagne()+"'");
			sql_query = sql_query.replaceAll("#id_employe", "'"+ addedData.getId_evalue()+"'");
			sql_query = sql_query.replaceAll("#date_evaluation", "'"+ formatter.format(addedData.getDate_evaluation())+"'");
			sql_query = sql_query.replaceAll("#id_evaluateur", "'"+ addedData.getId_evaluateur()+"'");
			sql_query = sql_query.replaceAll("#heure_debut_evaluation", "'"+ addedData.getHeure_debut_evaluation()+"'");
			sql_query = sql_query.replaceAll("#heure_fin_evaluation", "'"+ addedData.getHeure_fin_evaluation()+"'");
			sql_query = sql_query.replaceAll("#lieu", "'"+ addedData.getLieu()+"'");
			sql_query = sql_query.replaceAll("#code_poste", "'"+ addedData.getCode_poste()+"'");
			sql_query = sql_query.replaceAll("#personne_ressources", "'"+ addedData.getPersonne_ressources()+"'");
			sql_query = sql_query.replaceAll("#code_structure", "'"+ addedData.getCode_structure()+"'");
			sql_query = sql_query.replaceAll("#id_planning_evaluation", "'"+ addedData.getId_planning_evaluation()+"'");
		
			
		   //System.out.println(update_structure);
			
			 stmt.executeUpdate(sql_query);
		} 
		catch (SQLException e) 
		{
			try 
			{
				Messagebox.show("La modification n'a pas �t� prise en compte car il existe une donn�e ayant le m�me code �tablissement", "Erreur",Messagebox.OK, Messagebox.ERROR);
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
	
	/**
	 * cette classe permet de supprimer une donn�e de la table structure_entreprise
	 * @param codeStructure
	 */
	public Boolean deleteCompagne(PlanningCompagneListBean addedData)
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt;
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="DELETE FROM  planning_evaluation   where id_planning_evaluation=#id_planning_evaluation" ; 
			sql_query = sql_query.replaceAll("#id_planning_evaluation", "'"+ addedData.getId_planning_evaluation()+"'");
			
		
			
			 stmt.executeUpdate(sql_query);
		} 
		catch (SQLException e) 
		{
			
				e.printStackTrace();
				return false;


		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/*
	*//**
	 * cette classe permet de supprimer une donn�e de la table structure_entreprise
	 * @param codeStructure
	 * @throws SQLException 
	 *//*
	*/
	
	public HashMap getCompagneValid() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
		//	String profile_list="select id_compagne,libelle_compagne from compagne_evaluation" +
			//		" where date_debut>=now() order by date_debut"; 
			
            String profile_list="select id_compagne,libelle_compagne from compagne_evaluation order by date_debut"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("libelle_compagne"), rs.getInt("id_compagne"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	public HashMap getListEvaluteur() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select id_employe,concat(e.nom,' ',e.prenom) as evaluateur " +
					            " from employe e,common_evalcom.compte c where est_evaluateur='Y'" +
					            " and e.id_compte=c.id_compte and c.database_id=#databaseid"; 
			
			profile_list = profile_list.replaceAll("#databaseid", "'"+database+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("evaluateur"), rs.getInt("id_employe"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	public HashMap getListEvalue() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select id_employe,concat(e.nom,' ',e.prenom) as evalue " +
            " from employe e,common_evalcom.compte c where est_evaluateur='N'" +
            " and e.id_compte=c.id_compte and c.database_id=#databaseid"; 

            profile_list = profile_list.replaceAll("#databaseid", "'"+database+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("evalue"), rs.getInt("id_employe"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}
	
	
	public HashMap getListPoste() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select code_poste,intitule_poste from poste_travail_description"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	public HashMap getListStructure() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String profile_list="select code_structure  from structure_entreprise"; 
			ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
			
			
			while(rs.next()){
				map.put(rs.getString("code_structure"), rs.getString("code_structure"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	
	public HashMap selectedPoste(int employe) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select e.code_poste,intitule_poste from  employe e ,poste_travail_description t" +
					"  where id_employe=#employe and e.code_poste=t.code_poste "; 
			sql_query = sql_query.replaceAll("#employe", "'"+employe+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
			
			while(rs.next()){
				map.put(rs.getString("intitule_poste"), rs.getString("code_poste"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}	
	
	
	public HashMap selectEmployes(String code_poste) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
			
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select id_employe,concat(nom,' ',prenom) as evalue from employe where  code_poste=#code_poste  "; 
			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
			
			while(rs.next()){
				map.put(rs.getString("evalue"), rs.getInt("id_employe"));
				//list_profile.add(rs.getString("libelle_profile"));
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return map;
	}
	


	public HashMap getHeureDebut() 
	{
		
		HashMap map = new HashMap();
		
		for (int i=8;i<=20;i++){
			map.put(i+":00", i);
		}
		return (HashMap) sortByComparator(map);
	}
	
	public HashMap getHeureFin()
	{
		
		HashMap map = new HashMap();
		
		for (int i=8;i<=20;i++){
			map.put(i+":00", i);
		}
		return (HashMap) sortByComparator(map);
	}	
	
	

	
	
	 private static Map sortByComparator(Map unsortMap) {
		 
	        List list = new LinkedList(unsortMap.entrySet());
	 
	        //sort list based on comparator
	        Collections.sort(list, new Comparator() {
	             public int compare(Object o1, Object o2) {
		           return ((Comparable) ((Map.Entry) (o1)).getValue())
		           .compareTo(((Map.Entry) (o2)).getValue());
	             }
		});
	 
	        //put sorted list into map again
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
		     Map.Entry entry = (Map.Entry)it.next();
		     sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	   }	
	 
	 public HashMap setlectedStructure(String code_structure) throws SQLException
		{
			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToEntrepriseDB();
			Statement stmt = null;
			HashMap map = new HashMap();

			
			try 
			{
				stmt = (Statement) conn.createStatement();
				String sql_query="select concat_ws('-->',libelle_division,libelle_direction,libelle_unite,libelle_departement,libelle_service,libelle_section) as structure from structure_entreprise where code_structure=#code_structure";
				sql_query = sql_query.replaceAll("#code_structure", "'"+code_structure+"'");
				ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
				
				
				while(rs.next()){
					map.put( rs.getString("structure"),rs.getString("structure"));
		        }
				stmt.close();conn.close();
			} 
			catch (SQLException e){
					e.printStackTrace();
					stmt.close();conn.close();
			}
			
			return map;
			
			
		}

	 
	 public void sendPlanningToEvaluateur(List recipient,List<PlanningListEvaluateurBean> list_refevaluateur) throws SQLException{
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
			//CompagneListBean cmp=new CompagneListBean();
			
			
			
			Iterator itr=list_refevaluateur.iterator();

			Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(intctx.getUser(),intctx.getPassword());
					}
				});

			try {

				MimeMessage message = new MimeMessage(session);
				String reslt="";
				String monmessage="";
				String nomevaluateur="";

				message.setFrom(new InternetAddress(intctx.getFrom()));
				
				
				PlanningAgendaBean cpb;
				PlanningListEvaluateurBean ple;
				String mail="";
				while(itr.hasNext()){
					ple  = (PlanningListEvaluateurBean) itr.next();		
					Iterator it = recipient.iterator();	
					monmessage="<html> <body> 	<P>"+" Madame/Monsieur : "+"#nomevaluateur"+"</P>" +
												"<P>"+" Merci de trouver ci-dessous le planning de l'�valuation des comp�tences  de votre �quipe"+"</P>" +			             
												" <TABLE BORDER=10>  <TR>  <TH align='center'> Evalu�</TH>" +
												" <TH align='center'> Date</TH> <TH align='center'> Heur debut</TH>" +
												"<TH align='center'> Heure fin</TH> <TH align='center'> Lieu </TH>  </TR>";
								
				
					while (it.hasNext()){
						cpb  = (PlanningAgendaBean) it.next();
						
						if (cpb.getId_evaluateur()==ple.getId_evaluateur()){
							
							mail=cpb.getEmail();
							nomevaluateur=cpb.getNomevaluateur()+" "+cpb.getPrenomevaluateur();
							message.setSubject("Planning �valuation des comp�tences");
							reslt="<TR>"+"<TD>"+ cpb.getNomevalue() +" "+ cpb.getPrenomevalue()+"</TD>"+
							               "<TD>"+cpb.getDate_evaluation()+"</TD>"+
							               "<TD>"+cpb.getHeure_debut_evaluation()+"</TD>"+
							               "<TD>"+cpb.getHeure_fin_evaluation()+"</TD>"+
							               "<TD>"+ cpb.getLieu()+"</TD>"+
							       "</TR>";  
									      
							monmessage=monmessage+reslt;
					   }
				 }
					monmessage=monmessage.replaceAll("#nomevaluateur", nomevaluateur);
					monmessage=monmessage+	" </TABLE> <P>"+"Cordialement"+	"</P>"+"<P>"+"Administrateur"+	"</P> </body></html>";
					StringBuilder sb = new StringBuilder();
					sb.append(monmessage);
					
					
					
					message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(mail));
					
					message.setRecipients(Message.RecipientType.CC,
							InternetAddress.parse(intctx.getCc()));
					
					message.setContent(sb.toString(), "text/html");

					Transport.send(message);
					sb= new StringBuilder();
					nomevaluateur="";
					monmessage="";
					
				
			}
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}

		}
	 
	 
	 public List getListEvaluateur(int id_compagne) throws SQLException{
			
			
			List listevaluateur = new ArrayList<SuiviCompagneBean>();
			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToEntrepriseDB();
			Statement stmt = null;
			String sqlquery="";
			if  (id_compagne==-1){
				 sqlquery=	"select distinct id_evaluateur, concat(e.nom,' ',e.prenom) as evaluateur  from planning_evaluation p, employe e" +
	            " where p.id_evaluateur=e.id_employe order by nom ";

			}
			else {
				 sqlquery=	"select distinct id_evaluateur, concat(e.nom,' ',e.prenom) as evaluateur from planning_evaluation p, employe e" +
	            " where p.id_evaluateur=e.id_employe and id_compagne=#id_compagne order by nom ";
				sqlquery = sqlquery.replaceAll("#id_compagne", "'"+id_compagne+"'");


			}

			
			try {
				stmt = (Statement) conn.createStatement();
				
				
				ResultSet rs = (ResultSet) stmt.executeQuery(sqlquery);
			
				while(rs.next()){
					
					PlanningListEvaluateurBean evalbean=new PlanningListEvaluateurBean();
					evalbean.setId_evaluateur(rs.getInt("id_evaluateur"));
					evalbean.setEvaluateur(rs.getString("evaluateur"));
					

					
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
			/*CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToEntrepriseDB();
			Statement stmt = null;
			HashMap map = new HashMap();
			
			try 
			{
				stmt = (Statement) conn.createStatement();
				String db_list="select id_compagne,concat(libelle_compagne,'->', 'Du ',date_debut,' Au ',date_fin) as libelle_compagne from compagne_evaluation order by date_debut"; 
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
			}*/
		 HashMap map = new HashMap();
		 map.put("Toutes les vagues",-1);
			
			return map;
		}
	 
	 
	 public List getPlanningEvaluateur(String evaluateur) throws SQLException{
			
			
			List listevaluateur = new ArrayList<PlanningAgendaBean>();
			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToEntrepriseDB();
			Statement stmt = null;
			String sqlquery="";
			
				 sqlquery=	"select  p.id_evaluateur,e.nom as nomevaluateur,e.prenom as prenomevaluateur,e.email,t.nom as nomevalue,t.prenom as prenomevalue," +
				 		    " p.date_evaluation,p.heure_debut_evaluation ,p.heure_fin_evaluation,p.lieu,p.personne_ressources" +
				 		    " from employe e,planning_evaluation p,employe t where e.id_employe in #evaluateur  and p.id_evaluateur=e.id_employe" +
				 		    " and t.id_employe=p.id_employe";
				sqlquery = sqlquery.replaceAll("#evaluateur", evaluateur);



			
			try {
				stmt = (Statement) conn.createStatement();
				
				
				ResultSet rs = (ResultSet) stmt.executeQuery(sqlquery);
			
				while(rs.next()){
					
					PlanningAgendaBean evalbean=new PlanningAgendaBean();
					evalbean.setNomevaluateur(rs.getString("nomevaluateur"));
					evalbean.setPrenomevaluateur(rs.getString("prenomevaluateur"));
					evalbean.setEmail(rs.getString("email"));
					evalbean.setNomevalue(rs.getString("nomevalue"));
					evalbean.setPrenomevalue(rs.getString("prenomevalue"));
					evalbean.setPrenomevalue(rs.getString("prenomevalue"));
					evalbean.setDate_evaluation(rs.getDate("date_evaluation"));
					evalbean.setHeure_debut_evaluation(rs.getString("heure_debut_evaluation"));
					evalbean.setHeure_fin_evaluation(rs.getString("heure_fin_evaluation"));
					evalbean.setLieu(rs.getString("lieu"));
					evalbean.setPersonne_ressources(rs.getString("personne_ressources"));
					evalbean.setId_evaluateur(rs.getInt("id_evaluateur"));

					
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
	 
	 public List getPlanningAllEvaluateur() throws SQLException{
			
			
			List listevaluateur = new ArrayList<PlanningAgendaBean>();
			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToEntrepriseDB();
			Statement stmt = null;
			String sqlquery="";
			
				 sqlquery="select  p.id_evaluateur,e.nom as nomevaluateur,e.prenom as prenomevaluateur," +
				 		  " e.email,t.nom as nomevalue,t.prenom as prenomevalue," +
				 		  " p.date_evaluation,p.heure_debut_evaluation ,p.heure_fin_evaluation,p.lieu,p.personne_ressources" +
				 		  " from employe e,planning_evaluation p,employe t" +
				 		  " where p.id_evaluateur=e.id_employe 	and t.id_employe=p.id_employe" +
				 		  " and date_evaluation > now() order by p.id_evaluateur";
				



			
			try {
				stmt = (Statement) conn.createStatement();
				
				
				ResultSet rs = (ResultSet) stmt.executeQuery(sqlquery);
			
				while(rs.next()){
					
					PlanningAgendaBean evalbean=new PlanningAgendaBean();
					evalbean.setNomevaluateur(rs.getString("nomevaluateur"));
					evalbean.setPrenomevaluateur(rs.getString("prenomevaluateur"));
					evalbean.setEmail(rs.getString("email"));
					evalbean.setNomevalue(rs.getString("nomevalue"));
					evalbean.setPrenomevalue(rs.getString("prenomevalue"));
					evalbean.setPrenomevalue(rs.getString("prenomevalue"));
					evalbean.setDate_evaluation(rs.getDate("date_evaluation"));
					evalbean.setHeure_debut_evaluation(rs.getString("heure_debut_evaluation"));
					evalbean.setHeure_fin_evaluation(rs.getString("heure_fin_evaluation"));
					evalbean.setLieu(rs.getString("lieu"));
					evalbean.setPersonne_ressources(rs.getString("personne_ressources"));
					evalbean.setId_evaluateur(rs.getInt("id_evaluateur"));

					
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
	 public void sendPlanningToDRH(List recipient) throws SQLException{
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
			
						
			Map map_evaluateurs = new HashMap();
			Map map_drh=new HashMap();
			
			map_evaluateurs=getListAllEvaluateurs();
	  		Set set = (map_evaluateurs).entrySet(); 
	  		Iterator itr = set.iterator();
			map_drh=getListDRHs();
			
			Set set1 = (map_drh).entrySet(); 
	  		Iterator itr_drh = set1.iterator();
	  		
			
			Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(intctx.getUser(),intctx.getPassword());
					}
				});

			try {

				MimeMessage message = new MimeMessage(session);
				String reslt="";
				String monmessage="";
				String nomevaluateur="";
				message.setSubject("Planning �valuation des comp�tences");
				message.setFrom(new InternetAddress(intctx.getFrom()));
				String nomdrh="";
				String emaildrh="";
				
				while(itr_drh.hasNext()){
					
						Map.Entry me = (Map.Entry)itr_drh.next();
						nomdrh=nomdrh+(String)me.getKey()+",";
						emaildrh=emaildrh+(String)me.getValue()+",";
						PlanningAgendaBean cpb;
						PlanningAgendaBean ple;
						String mail="";
						monmessage="<html> <body> 	<P>"+" Madames/Monsieurs : "+nomdrh+"</P>" +
						"<P>"+" Merci de trouver ci-dessous le planning complet des �valuations des comp�tences "+"</P>" ;
						while(itr.hasNext()){
							Map.Entry me1 = (Map.Entry)itr.next();
							monmessage=monmessage+"<P>"+" Evaluateur M,Mme : "+(String)me1.getKey() +"</P>";
							monmessage=monmessage+" <TABLE BORDER=10>"+" <TR>  <TH align='center'> Evalu�</TH>" +
							" <TH align='center'> Date</TH> <TH align='center'> Heur debut</TH>" +
							"<TH align='center'> Heure fin</TH> <TH align='center'> Lieu </TH>  </TR>";;
							
								Iterator it = recipient.iterator();	
											
							
									while (it.hasNext()){
										cpb  = (PlanningAgendaBean) it.next();
										
											if (cpb.getId_evaluateur()==(Integer) me1.getValue()){					
												 			             
												reslt="<TR>"+"<TD>"+ cpb.getNomevalue() +" "+ cpb.getPrenomevalue()+"</TD>"+
												               "<TD>"+cpb.getDate_evaluation()+"</TD>"+
												               "<TD>"+cpb.getHeure_debut_evaluation()+"</TD>"+
												               "<TD>"+cpb.getHeure_fin_evaluation()+"</TD>"+
												               "<TD>"+ cpb.getLieu()+"</TD>"+
												       "</TR>";  								      
												monmessage=monmessage+reslt;
										   }
						      }
							
								monmessage=monmessage+" </TABLE>";
						
					   }
					
				
					
				}
				//monmessage=monmessage.replaceAll("#nomevaluateur", nomevaluateur);
				monmessage=monmessage+"<P>"+"Cordialement"+	"</P>"+"<P>"+"Administrateur"+	"</P> </body></html>";
				//System.out.println(monmessage);
				StringBuilder sb = new StringBuilder();
				sb.append(monmessage);
				
				
				
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(emaildrh));
				
				message.setRecipients(Message.RecipientType.CC,
						InternetAddress.parse(intctx.getCc()));
				
				message.setContent(sb.toString(), "text/html");

				Transport.send(message);
				sb= new StringBuilder();
				nomevaluateur="";
				monmessage="";
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}

		}
	 
	 public HashMap getListAllEvaluateurs() throws SQLException
		{
			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToEntrepriseDB();
			Statement stmt = null;
			HashMap map = new HashMap();
			
			try 
			{
				stmt = (Statement) conn.createStatement();
				String profile_list="select  distinct p.id_evaluateur ,concat(e.nom,' ',e.prenom) as nom  from planning_evaluation p,employe e" +
						            " where e.id_employe =p.id_evaluateur  and date_evaluation > now() order by p.id_evaluateur "; 
				ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
				
				
				while(rs.next()){
					map.put(rs.getString("nom"), rs.getInt("p.id_evaluateur"));
					//list_profile.add(rs.getString("libelle_profile"));
		        }
				stmt.close();conn.close();
			} 
			catch (SQLException e){
					e.printStackTrace();
					stmt.close();conn.close();
			}
			
			return map;
		}
	 
	 
	 public HashMap getListDRHs() throws SQLException
		{
			CreateDatabaseCon dbcon=new CreateDatabaseCon();
			Connection conn=(Connection) dbcon.connectToEntrepriseDB();
			Statement stmt = null;
			HashMap map = new HashMap();
			
			try 
			{
				stmt = (Statement) conn.createStatement();
				String profile_list="select  distinct concat(nom,' ',prenom) as nom,email  from employe" +
						            " where  est_responsable_rh='Y'"; 
				ResultSet rs = (ResultSet) stmt.executeQuery(profile_list);
				
				
				while(rs.next()){
					map.put(rs.getString("nom"), rs.getString("email"));
					//list_profile.add(rs.getString("libelle_profile"));
		        }
				stmt.close();conn.close();
			} 
			catch (SQLException e){
					e.printStackTrace();
					stmt.close();conn.close();
			}
			
			return map;
		}	
		





}
