package drugiProjkeat_AutoShop;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminPanel extends KorisnickiPanel{
	
	public void Poruka() {
		System.out.println("Zdravo admine \n0.Izlaz \n1.Izlistaj modele " + 
								"\n2.Kupi model \n3.Rate za korisnika \n4.Proveri ponudu dobavljaca "
								+ "\n5.promeni cenu\n6.Dopuni Zalihe");
	}
	
	public void promeniCenu(String model,int novaCena) throws SQLException  {
		
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="UPDATE Model SET TrenutnaCena=" +novaCena + 
				" WHERE Model.Naziv= '"+ model + "'";
		sta.execute(upit);
			
		sta.close();
		Konekcija.con.close();
	}

	public void proveraRata(String korisnik) throws SQLException {
		int idKorisnik=IdKorisnik(korisnik);
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="select Racun.IdRacun,Rate.IdRate,Rate.PreostaloRata,Rate.PreostaloZaNaplatu,"+
				"Rate.DatumPoslednje FROM Rate,Racun,Korisnik " + 
				"where Rate.PreostaloRata>0 and Racun.IdKorisnik=Korisnik.IdKorisnik and "+
				" Racun.IdRate=Rate.IdRate and Korisnik.IdKorisnik="+idKorisnik;
		sta.execute(upit);
		ResultSet results = sta.executeQuery(upit);
		System.out.println("IdRacuna  idRate  PreostaloRata  zaNaplatu   datum");
		while(results.next()) {
			
		    System.out.println("    "+results.getInt("IdRacun")+ "\t    " + results.getInt("IdRate")+ "\t       " +
		    				  results.getInt("PreostaloRata")+ "\t   " + results.getInt("PreostaloZaNaplatu")+
		    				   "\t    " + results.getInt("DatumPoslednje"));
		}
		results.close();
		
		sta.close();
		Konekcija.con.close();
	}
	
	public void ponudaDobavljaca() throws SQLException {
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="SELECT *  FROM PonudaDobavljaca WHERE Kolicina>0";
		sta.execute(upit);
		ResultSet results = sta.executeQuery(upit);
		System.out.println("IdPonuda  idDobavljac  Cena    Kolicina      Naziv");
		while(results.next()) {
			
		    System.out.println("    "+results.getInt("IdPonuda")+ "\t      " + results.getInt("idDobavljac")+ "\t       " +
		    				  results.getInt("Cena")+ "  \t " + results.getInt("Kolicina")+
		    				  " \t    " + results.getString("Naziv"));
		}
		results.close();
		
		sta.close();
		Konekcija.con.close();
	}
	
	public void dopuniZalihe(String model, int kolicina, int IdDobavljac,int nabavnaCena) throws SQLException {
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		//azuriranje tabele PonudaDobavljaca
		String upit="UPDATE PonudaDobavljaca " + 
				"SET Kolicina=Kolicina-"+kolicina+
				" WHERE Naziv='"+model+"'";
		sta.execute(upit);
		//azuriranje tabele Model
		upit="UPDATE Model SET Lager=Lager+"+kolicina+" WHERE Naziv='"+model+"'";
		sta.execute(upit);
		sta.close();
		//azuriranje tabele ModelDobavljac
		sta =Konekcija.con.createStatement();
	
		upit="INSERT INTO ModelDobavljac (IdModel,IdDobavljac, BrKupljenih) " + 
				"VALUES ("+modelId(model)+","+IdDobavljac+","+ kolicina+")";
		System.out.println("model id je "+modelId(model));
		sta.executeUpdate(upit);
	
		//azuriranje tabele auto
		for(int i =0;i<kolicina;i++) {
			autoInsert(model, nabavnaCena, IdDobavljac);
		}
	}
	
	//IdModel na osnovu naziva modela
	public int modelId(String model) throws SQLException{
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="SELECT Model.IdModel FROM Model WHERE Model.Naziv='"+model+"'";
		sta.execute(upit);
		ResultSet results =sta.getResultSet();
		
		int IdMOdel=-1;
		if(results.next()) {
			IdMOdel=results.getInt("IdModel");
		}
		
		results.close();
		sta.close();
		Konekcija.con.close();
		return IdMOdel;
	}
	
	//Ubacivanje u Auto tabelu
	public void autoInsert(String model,int nabavnaCena,int IdDobavljac) throws SQLException {
		Konekcija.con=DriverManager.getConnection(Konekcija.url);
		java.sql.Statement sta =Konekcija.con.createStatement();
		String upit="INSERT INTO Auto (IdModel,CenaNabavka,IdDobavljac) " + 
				"VALUES ("+modelId(model)+","+nabavnaCena+","+IdDobavljac+")";
		
		sta.execute(upit);
		sta.close();
		Konekcija.con.close();
	}
	
	
	
}
