package administration.model;

import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import common.CreateDatabaseCon;

public class LoginModel {
	
	public void LoginModel(){
		
	}
	
	public boolean checkLoginPwd(String login,String password) throws SQLException{
		
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt;
		boolean type_result=false;
		try {
			stmt = (Statement) conn.createStatement();
			String select_login="SELECT 1 FROM compte where upper(login)=#login and upper(pwd)=#password";
			select_login = select_login.replaceAll("#login", "'"+login.toUpperCase()+"'");
			select_login = select_login.replaceAll("#password", "'"+password.toUpperCase()+"'");
			//System.out.println(select_login);
			ResultSet resultset = (ResultSet) stmt.executeQuery(select_login);
			
			
			while(resultset.next()){
				if (resultset.getRow()==1) {
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
	
	   /* public static void main(String arg[]){
	    LoginModel init =new LoginModel();
	    try {
			boolean result=init.checkLoginPwd("nbehlouli","1234");
			if (result==true) {System.out.println("GO") ;} else{ System.out.println("KO");}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
     } 
*/

}
