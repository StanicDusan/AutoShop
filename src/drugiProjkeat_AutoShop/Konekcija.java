package drugiProjkeat_AutoShop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Konekcija {
	
	public static String dbName="AutoMoto.db";
	public static String url="jdbc:sqlite:" + dbName;
	public static Connection con;
	
}
