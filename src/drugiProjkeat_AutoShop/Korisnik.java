package drugiProjkeat_AutoShop;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Korisnik {

	public static void dodajKorisnika(String korisnickoIme, String Pass) throws SQLException {
		if (proveraKorisnickogImena(korisnickoIme)) {
			Konekcija.con = DriverManager.getConnection(Konekcija.url);
			java.sql.Statement statement = Konekcija.con.createStatement();
			statement.execute(
					"INSERT INTO Korisnik(KorisnickoIme,Lozinka) " + "VALUES ('" + korisnickoIme + "','" + Pass + "')");

			statement.close();
			Konekcija.con.close();
			System.out.println("Uspesno kreiran nalog");
		} else
			System.out.println("Korisnicko ime u upotrebi");
	}

	public static KorisnickiPanel logovanje(String korisnickoIme, String Pass) throws SQLException {

		if (korisnickoIme.equals("admin")) {

			Konekcija.con = DriverManager.getConnection(Konekcija.url);
			java.sql.Statement statement = Konekcija.con.createStatement();
			statement.execute("SELECT Radnik.Lozinka FROM Radnik");
			ResultSet results = statement.getResultSet();

			while (results.next()) {
				if (results.getString("Lozinka").equals(Pass)) {
					results.close();
					statement.close();
					Konekcija.con.close();

					return new AdminPanel();
				}
			}
			results.close();
			statement.close();
			Konekcija.con.close();
			return null;
		}

		else {
			Konekcija.con = DriverManager.getConnection(Konekcija.url);
			java.sql.Statement statement = Konekcija.con.createStatement();
			statement.execute("SELECT * FROM Korisnik");
			ResultSet results = statement.getResultSet();
			boolean flag = false;
			while (results.next()) {
				if (results.getString("KorisnickoIme").equals(korisnickoIme)
						&& results.getString("Lozinka").equals(Pass))
					flag = true;

			}
			results.close();
			statement.close();
			Konekcija.con.close();
			if (flag)
				return new KorisnickiPanel();
			else
				return null;
		}
	}

	public static void izlistajRaspoloziveModele() throws SQLException {
		Konekcija.con = DriverManager.getConnection(Konekcija.url);
		Statement statement = Konekcija.con.createStatement();
		statement.execute("select Model.Naziv,Model.Lager from model WHERE Lager>0");
		ResultSet results = statement.getResultSet();

		while (results.next()) {
			System.out.println(results.getString("Naziv"));// + " - " + results.getString("Lager"));

		}
		results.close();
		statement.close();
		Konekcija.con.close();
	}

	public static boolean proveraKorisnickogImena(String korisnickoIme) throws SQLException {
		Konekcija.con = DriverManager.getConnection(Konekcija.url);
		Statement sta = Konekcija.con.createStatement();
		ResultSet results = sta.executeQuery(
				"select count(Korisnik.KorisnickoIme) as ukupno " + "from Korisnik where Korisnik.KorisnickoIme='"
						+ korisnickoIme + "'	group by Korisnik.KorisnickoIme");
	
		results.close();
		sta.close();
		Konekcija.con.close();
		if (results.next())
			return false;
		else
			return true;

	}
	


	
	
}




















