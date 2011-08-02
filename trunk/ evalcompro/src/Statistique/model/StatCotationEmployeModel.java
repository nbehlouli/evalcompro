package Statistique.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

import Statistique.bean.EmployeCadreBean;
import Statistique.bean.EmployeMoyFamBean;
import Statistique.bean.StatMoyFamillePosteBean;

import Statistique.bean.StatCotationEmployeBean;

public class StatCotationEmployeModel {

	
	public ArrayList<StatCotationEmployeBean> InitialiserStatCotationEmploye()
	{
		
		ArrayList<StatCotationEmployeBean> liste=new ArrayList<StatCotationEmployeBean>();
		StatCotationEmployeBean statBean1=new StatCotationEmployeBean();
		statBean1.setNomEmploye("Helene BAUM");
		HashMap<String, Double> listeCotations3=new HashMap<String, Double>();
		listeCotations3.put("Affaire", new Double(3.00));
		listeCotations3.put("Relationnelle", new Double(2.5));
		listeCotations3.put("Personnelle", new Double(1.7));
		listeCotations3.put("Opérationnelle", new Double(1.00));
		
		HashMap<String, Double> listeCotations11=new HashMap<String, Double>();
		listeCotations11.put("Affaire", new Double(1.00));
		listeCotations11.put("Relationnelle", new Double(2.0));
		listeCotations11.put("Personnelle", new Double(2));
		listeCotations11.put("Opérationnelle", new Double(0.5));
		
		HashMap<String, Double> listeCotations21=new HashMap<String, Double>();
		listeCotations21.put("Affaire", new Double(3.00));
		listeCotations21.put("Relationnelle", new Double(2.5));
		listeCotations21.put("Personnelle", new Double(2));
		listeCotations21.put("Opérationnelle", new Double(1.00));

		HashMap<String, HashMap<String, Double>> listeCompagnes1=new HashMap<String, HashMap<String, Double>>();
		listeCompagnes1.put("Compagne1", listeCotations3);
		listeCompagnes1.put("Compagne2", listeCotations11);
		listeCompagnes1.put("Compagne3", listeCotations21);
		
		statBean1.setCompagne_listCotation(listeCompagnes1);
		
		
		liste.add(statBean1);
		StatCotationEmployeBean statBean=new StatCotationEmployeBean();
		statBean.setNomEmploye("Lilian DAUTAIS");
		HashMap<String, Double> listeCotations=new HashMap<String, Double>();
		listeCotations.put("Affaire", new Double(2.00));
		listeCotations.put("Relationnelle", new Double(3.5));
		listeCotations.put("Personnelle", new Double(2));
		listeCotations.put("Opérationnelle", new Double(1.00));
		
		HashMap<String, Double> listeCotations1=new HashMap<String, Double>();
		listeCotations1.put("Affaire", new Double(1.00));
		listeCotations1.put("Relationnelle", new Double(2.5));
		listeCotations1.put("Personnelle", new Double(2));
		listeCotations1.put("Opérationnelle", new Double(0.5));
		
		HashMap<String, Double> listeCotations2=new HashMap<String, Double>();
		listeCotations2.put("Affaire", new Double(1.00));
		listeCotations2.put("Relationnelle", new Double(1.5));
		listeCotations2.put("Personnelle", new Double(2));
		listeCotations2.put("Opérationnelle", new Double(1.00));

		HashMap<String, HashMap<String, Double>> listeCompagnes=new HashMap<String, HashMap<String, Double>>();
		listeCompagnes.put("Compagne1", listeCotations);
		listeCompagnes.put("Compagne2", listeCotations1);
		listeCompagnes.put("Compagne3", listeCotations2);
		
		statBean.setCompagne_listCotation(listeCompagnes);
		liste.add(statBean);
		return liste;
	}
	
	
	
	
	
	public HashMap getListEmployesFichValid(String id_compagne) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select e.id_employe,concat(e.nom ,' ' ,e.prenom) as nom from fiche_validation f ,employe e" +
					         " where f.id_employe=e.id_employe and f.fiche_valide=1 and f.id_planning_evaluation in (select id_planning_evaluation  from planning_evaluation where id_compagne=#id_compagne)";
			
			sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
			
			while(rs.next()){
				map.put(rs.getString("nom"), rs.getString("id_employe"));
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
	
	public HashMap getListCompagneValid() throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  id_compagne,concat(e.libelle_compagne,'->', 'Du ',e.date_debut,' Au ',e.date_fin) nomcompagne" +
					         " from compagne_evaluation e 	where e.id_compagne in (select id_compagne from compagne_validation where compagne_valide=1)";
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
			
			while(rs.next()){
				map.put(rs.getString("nomcompagne"), rs.getString("id_compagne"));
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
	
	public List getListEmployesMoyFam(String id_employe) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		List listmoyfam = new ArrayList<EmployeMoyFamBean>();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  r.famille,round(moy_par_famille,2) as moy_par_famille,round(imi,2) as imi from imi_stats s ,repertoire_competence r where id_employe=#id_employe" +
					         " and s.code_famille=r.code_famille group by  r.famille";
			
			sql_query = sql_query.replaceAll("#id_employe", "'"+id_employe+"'");
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
            while(rs.next()){
				
            	EmployeMoyFamBean bean=new EmployeMoyFamBean();
				bean.setCode_famille((rs.getString("famille")));	
				bean.setMoy_famille((rs.getFloat("moy_par_famille")));
				bean.setImi((rs.getFloat("imi")));
				listmoyfam.add(bean);
				
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return listmoyfam;
	}
	
	public HashMap getListPostTravailValid(String id_compagne) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		HashMap map = new HashMap();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select  distinct t.code_poste,t.intitule_poste from compagne_evaluation e, planning_evaluation p, poste_travail_description t" +
					         " where e.id_compagne in (select id_compagne from compagne_validation where compagne_valide=1) " +
					         " and p.id_compagne=e.id_compagne  and t.code_poste=p.code_poste and e.id_compagne=#id_compagne;";
			
			sql_query = sql_query.replaceAll("#id_compagne", "'"+id_compagne+"'");
			
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
	
	public List getListPosteMoyFam(String code_poste) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		List listmoyfam = new ArrayList<StatMoyFamillePosteBean>();
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select r.famille,round(moy_par_famille,2) as moy_par_famille from moy_poste_famille_stats m, repertoire_competence r" +
					" where r.code_famille=m.code_famille and  code_poste=#code_poste group by  r.famille";
			
			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
            while(rs.next()){
				
            	StatMoyFamillePosteBean bean=new StatMoyFamillePosteBean();
				bean.setFamille((rs.getString("famille")));	
				bean.setMoy_famille((rs.getFloat("moy_par_famille")));
				listmoyfam.add(bean);
				
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return listmoyfam;
	}
	
	public float getIMGParPoste(String code_poste) throws SQLException
	{
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToEntrepriseDB();
		Statement stmt = null;
		float result=0;
		
		
		try 
		{
			stmt = (Statement) conn.createStatement();
			String sql_query="select round(img,2)as img from img_stats where code_poste=#code_poste" ;
			
			sql_query = sql_query.replaceAll("#code_poste", "'"+code_poste+"'");
			
			ResultSet rs = (ResultSet) stmt.executeQuery(sql_query);
			
            while(rs.next()){
				
            	
				result=rs.getFloat("img");
							
	        }
			stmt.close();conn.close();
		} 
		catch (SQLException e){
				e.printStackTrace();
				stmt.close();conn.close();
		}
		
		return result;
	}
	
	
}
