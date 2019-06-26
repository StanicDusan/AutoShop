package drugiProjkeat_AutoShop;

import java.sql.Date;
import java.util.Calendar;


public class Kalendar {
	public static Calendar c=Calendar.getInstance();
	public static long milis= c.getTimeInMillis();
	public static java.sql.Date datum=new java.sql.Date(milis);
	

}
