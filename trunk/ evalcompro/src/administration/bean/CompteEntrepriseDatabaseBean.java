package administration.bean;



public class CompteEntrepriseDatabaseBean {



	   private String jdbcurl;
	   private String jdbcusername;
	   private String jdbcpassword;
/**
 * TODO enlever ce constructeur
 * @param jdbcurl
 * @param jdbcusername
 * @param jdbcpassword
 */
	   public  CompteEntrepriseDatabaseBean(String jdbcurl, String jdbcusername, String jdbcpassword)
	   {
		   this.jdbcurl="jdbc:mysql://localhost:3306/evalcom";
		   this.jdbcusername="root";
		   this.jdbcpassword="admin";
	   }
	   
	public String getJdbcurl() {
		return jdbcurl;
	}
	public void setJdbcurl(String jdbcurl) {
		this.jdbcurl = jdbcurl;
	}
	public String getJdbcusername() {
		return jdbcusername;
	}
	public void setJdbcusername(String jdbcusername) {
		this.jdbcusername = jdbcusername;
	}
	public String getJdbcpassword() {
		return jdbcpassword;
	}
	public void setJdbcpassword(String jdbcpassword) {
		this.jdbcpassword = jdbcpassword;
	}
	   
	   

}
