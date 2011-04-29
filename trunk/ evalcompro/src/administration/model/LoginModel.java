package administration.model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import administration.bean.CompteBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import common.ApplicationFacade;
import common.CreateDatabaseCon;
import common.bean.ArborescenceMenu;
import common.bean.EcranBean;

public class LoginModel {
	
	private List  user_compte;
	private List  list_secreens;
	private static int profile_id;
	private static int database_id;
	
	public void LoginModel(){
		
	}
	
	public int checkLoginPwd(String login,String password) throws SQLException{
		user_compte = new ArrayList();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt;
		
		int type_result=0;
		try {
			stmt = (Statement) conn.createStatement();
			String select_login="SELECT id_compte,id_profile,login,pwd,database_id,val_date_deb,val_date_fin FROM compte where upper(login)=#login and upper(pwd)=#password";
			select_login = select_login.replaceAll("#login", "'"+login.toUpperCase()+"'");
			select_login = select_login.replaceAll("#password", "'"+password.toUpperCase()+"'");
			//System.out.println(select_login);
			ResultSet rs = (ResultSet) stmt.executeQuery(select_login);
			
			
			while(rs.next()){
				if (rs.getRow()==1  ) {
					
					if (checkLoginValidity(rs.getDate("val_date_deb"),rs.getDate("val_date_fin"))==true){
					
						CompteBean db = new CompteBean(rs.getInt("id_compte"),rs.getInt("id_profile"),
		                         rs.getString("login"),rs.getString("pwd"),rs.getInt("database_id"),
		                         rs.getDate("val_date_deb"),rs.getDate("val_date_fin"));
 
								user_compte.add(db);
								type_result=0;
								database_id=rs.getInt("database_id");
					}
					
					else{
						type_result=2;
					}
			  
				}
				
				else {
					type_result=1;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			((java.sql.Connection) dbcon).close();
		}
		
			
		return type_result;	
	
		
		
	}
	
public static void checkProfile(List compte_user) throws SQLException  {
	
	Iterator it = compte_user.iterator();
	CreateDatabaseCon dbcon=new CreateDatabaseCon();
	Connection conn=(Connection) dbcon.connectToDB();
	Statement stmt;
 	CompteBean cpb;
 	Integer profile=0;
 	while (it.hasNext()){
 		cpb  = (CompteBean) it.next();
		profile=cpb.getId_profile();
 	}
 	profile_id=profile;	
	stmt = (Statement) conn.createStatement();
	String select_profile=" select id_ecran,code_ecran,libelle_ecran,code_menu,libelle_menu"+ 
	                      " from liste_ecran where code_ecran in ("+
			              "select   code_ecran from droits where id_profile=#profile"+
	                      " and hide=0 and ecriture=1 and lecture=1 ) ";
	

	select_profile = select_profile.replaceAll("#profile",profile.toString() );
	//System.out.println(select_profile);
	ResultSet rs = (ResultSet) stmt.executeQuery(select_profile);

	List <String>listLibelleMenu=new ArrayList<String>();

	List <EcranBean>listeEcranBean=new ArrayList <EcranBean>();

	
	while(rs.next()){
		//System.out.println("dans la boucle");		                                                       
		//add result set to desired structure
        Integer val=new Integer(rs.getString ("id_ecran"));
		EcranBean ecranbean=new EcranBean(val, rs.getString("code_ecran"),rs.getString("libelle_ecran"), rs.getString("code_menu"), rs.getString("libelle_menu"));
		
		listeEcranBean.add(ecranbean);
		if(!listLibelleMenu.contains(rs.getString("libelle_menu")))
		{
			listLibelleMenu.add(rs.getString("libelle_menu"));
		}
		/*
				          
	   list_secreens.add(db)
				          
        */
		
				
	}
	ArborescenceMenu arborescenceMenu=new ArborescenceMenu(listeEcranBean,listLibelleMenu);

	ApplicationFacade.getInstance().setArborescenceMenubean(arborescenceMenu);
	
}

public boolean checkLoginValidity(Date date_deb,Date date_fin){
	
	
		
	//  begin validity date
     Calendar cal_deb=Calendar.getInstance();
     cal_deb.setTime(date_deb);
    
     
 //  begin validity date
     Calendar cal_fin=Calendar.getInstance();
     cal_fin.setTime(date_fin);
    
        
	Calendar cal = Calendar.getInstance();
    Calendar currentcal = Calendar.getInstance();
    currentcal.set(currentcal.get(Calendar.YEAR),
    currentcal.get(Calendar.MONTH), currentcal.get(Calendar.DAY_OF_MONTH));
    if(currentcal.before(cal_fin) && currentcal.after(cal_deb) ){
    	
    	return true;
    }
      
    
    else  return false;
	
	
	
}
	
	   /* public static void main(String arg[]){
	    LoginModel init =new LoginModel();
	    List list=new ArrayList();
	    try {
			boolean result=init.checkLoginPwd("nbehlouli","123");
			checkProfile(init.getUser_compte());
			if (result==true) {System.out.println("GO") ;} else{ System.out.println("KO");}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
     } */
	    public List getUser_compte() {
			return user_compte;
		}

		public void setUser_compte(List user_compte) {
			this.user_compte = user_compte;
		}

		public int getProfile_id() {
			return profile_id;
		}

		public void setProfile_id(int profile_id) {
			this.profile_id = profile_id;
		}

		public static int getDatabase_id() {
			return database_id;
		}

		public static void setDatabase_id(int database_id) {
			LoginModel.database_id = database_id;
		}

		

}
