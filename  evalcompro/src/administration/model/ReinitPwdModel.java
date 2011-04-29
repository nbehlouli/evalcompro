package administration.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import administration.bean.SelCliBean;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import common.CreateDatabaseCon;

public class ReinitPwdModel {
	
	private List  user_login_email;
	
public boolean checkLoginEmailValidity(String login) throws SQLException{
	

		boolean result=false;
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToDB();
		Statement stmt;
		user_login_email= new ArrayList();
		
		int type_result=0;
		try {
			stmt = (Statement) conn.createStatement();
			String user_login="select 1 from compte where login=#login";
			user_login = user_login.replaceAll("#login", login);
			
			//System.out.println(select_login);
			ResultSet rs = (ResultSet) stmt.executeQuery(user_login);
			
			
			while(rs.next()){
				if (rs.getRow()==1  )  result= true;
				else result= false;
 				 
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			((java.sql.Connection) dbcon).close();
		}
		
			
		return result;	
	
		
		
	}
	

}
