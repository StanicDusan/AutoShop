package drugiProjkeat_AutoShop;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KorisnickiPanel {
	public void Poruka() {
		System.out.println("------------------\nOdaberite neku od opcija: \n0.Izlaz\n1.Izlistaj modele "
				+ "\n2.Kupi model \n3.plati ratu\n----------------------" );
	}

	public void kupiAuto(String model,String korisnik,int rate) throws SQLException {
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		//Upis u tabelu Auto
		int racunId=racunId()+1;
		int audoId=autoId(model);
		String upit="UPDATE Auto SET CenaProdaja=" +prodajnaCenaModela(model) + 
				",IdRacuna="+racunId+" WHERE Auto.IdAuto= '"+ audoId + "'";
		sta.execute(upit);
		//Upis u tabelu Racun
		int rataId=rataId();
		int korisnikId=IdKorisnik(korisnik);
		upit = "INSERT INTO Racun (Datum,IdRate,IdAuto,IdKorisnik) "	+ 
				"VALUES ("+Kalendar.datum+", "+rataId+","+audoId+","+korisnikId+")";
		sta.execute(upit);		
		//izmena u tabeli Model- smanjenje sa lagera i inkrement prodatih
		azuriranjeLagera(model);
		//azuriranje tabele rate
		rate(rate,prodajnaCenaModela(model));
		
		sta.close();
		Konekcija.con.close();

		
	}
	
	public int izlaz() {
		System.out.println("Hvala na poseti");
		
		return 0;
	}
	
	//Trenutna prodajna cena Auta
	public int prodajnaCenaModela(String model) throws SQLException {
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="SELECT Model.TrenutnaCena FROM Model " + 
				"WHERE Model.Naziv='"+model+"'";
		sta.execute(upit);
		ResultSet results =sta.getResultSet();
		int cena=-1;
		if(results.next()) {
			cena=results.getInt("TrenutnaCena");
		}
		results.close();
		sta.close();
		Konekcija.con.close();
		return cena;
	}
	
	//Prvi slobodan auto sa lagera
	public int autoId(String model) throws SQLException {
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="SELECT Auto.IdAuto FROM Model,Auto " + 
				"WHERE Model.Naziv='"+model+"' and Auto.IdRacuna is NULL LIMIT 1";
		sta.execute(upit);
		ResultSet results =sta.getResultSet();
		int idAuto=-1;
		if(results.next()) {
			idAuto=results.getInt("IdAuto");
		}
		results.close();
		sta.close();
		Konekcija.con.close();
		return idAuto;
	}
	
	//prvi slobodan IdRacun
	public int racunId() throws SQLException {
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="SELECT Auto.IdRacuna DESC FROM Auto " + 
				"WHERE Auto.IdRacuna is NOT NULL ORDER BY Auto.IdRacuna DESC " + 
				"LIMIT 1";
		sta.execute(upit);
		ResultSet results =sta.getResultSet();
		int IdRacuna=-1;
		if(results.next()) {
			IdRacuna=results.getInt(1);
		}
		results.close();
		sta.close();
		Konekcija.con.close();
		return IdRacuna;
	}
	
	//prva slobodna IdRate
	public int rataId() throws SQLException {
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="select Rate.IdRate FROM Rate " + 
				"ORDER By Rate.IdRate DESC LIMIT 1";
		sta.execute(upit);
		ResultSet results =sta.getResultSet();
		int IdRate=-1;
		if(results.next()) {
			IdRate=results.getInt(1)+1;
		}
		results.close();
		sta.close();
		Konekcija.con.close();
		return IdRate;
	}
	
	//korisnik Id
	public int IdKorisnik(String korisnik) throws SQLException {
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="select Korisnik.IdKorisnik FROM Korisnik " + 
				"WHERE Korisnik.KorisnickoIme='"+korisnik+"'";
		sta.execute(upit);
		ResultSet results =sta.getResultSet();
		int IdKorisnik=-1;
		if(results.next()) {
			IdKorisnik=results.getInt(1);
		}
		results.close();
		sta.close();
		Konekcija.con.close();
		return IdKorisnik;
	}
	
	//Model tabela dekrement Lager polja i inkrement polja prodaja
	public void azuriranjeLagera(String model) throws SQLException {
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="UPDATE Model Set Lager=Lager-1, Prodato=Prodato+1 "
				+ "WHERE Model.Naziv='"+model+"'";
		sta.execute(upit);
		sta.close();
		Konekcija.con.close();
	}
	
	//Azuriranje tabele Rate
	public void rate(int rate,int prodajnaCena) throws SQLException {
		int preostaloZaNaplatu=prodajnaCena-(prodajnaCena/rate);
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="INSERT INTO  Rate(PreostaloRata,UkupnoRata,UkupnaCena,"+
					"PreostaloZaNaplatu,DatumPoslednje) VALUES ("+(rate-1)+", "+rate+","+prodajnaCena+","+
				     preostaloZaNaplatu+",'"+Kalendar.datum +"')";
		sta.execute(upit);
		sta.close();
		Konekcija.con.close();
	}
	

	
}
