package administration.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import administration.bean.CompteBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import common.CreateDatabaseCon;

public class LoginModel {
	
	private List  user_compte;
	private List  list_secreens;
	
	public void LoginModel(){
		
	}
	
	public boolean checkLoginPwd(String login,String password) throws SQLException{
		user_compte = new ArrayList();
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt;
		boolean type_result=false;
		try {
			stmt = (Statement) conn.createStatement();
			String select_login="SELECT id_compte,id_profile,login,pwd,database_id,val_date_deb,val_date_fin FROM compte where upper(login)=#login and upper(pwd)=#password";
			select_login = select_login.replaceAll("#login", "'"+login.toUpperCase()+"'");
			select_login = select_login.replaceAll("#password", "'"+password.toUpperCase()+"'");
			//System.out.println(select_login);
			ResultSet rs = (ResultSet) stmt.executeQuery(select_login);
			
			
			while(rs.next()){
				if (rs.getRow()==1) {
					  CompteBean db = new CompteBean(rs.getInt("id_compte"),rs.getInt("id_profile"),
							                         rs.getString("login"),rs.getString("pwd"),rs.getInt("database_id"),
							                         rs.getDate("val_date_deb"),rs.getDate("val_date_fin"));
				   user_compte.add(db);
				   
					type_result=true;
				}else {
					type_result=false;
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
		
	stmt = (Statement) conn.createStatement();
	String select_profile=" select id_ecran,code_ecran,libelle_ecran,code_menu,libelle_menu"+ 
	                      " from liste_ecran where code_ecran in ("+
			              "select   code_ecran from droits where id_profile=#profile"+
	                      " and hide=0 and ecriture=1 and lecture=1 ) ";
	

	select_profile = select_profile.replaceAll("#profile",profile.toString() );
	System.out.println(select_profile);
	ResultSet rs = (ResultSet) stmt.executeQuery(select_profile);
	while(rs.next()){
		                                                                        
		//add result set to desired structure
		/*StructureBean db = new StructureBean(rs.getInt("id_ecran"), rs.getString("code_ecran"),
				          rs.getString("libelle_ecran"),rs.getString("code_menu"),
				          rs.getString("libelle_menu"));
				          
	   list_secreens.add(db)
				          
        */
		System.out.println(rs.getInt("code_ecran"));
				
	}

	//return list_secreens;
	
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


}
