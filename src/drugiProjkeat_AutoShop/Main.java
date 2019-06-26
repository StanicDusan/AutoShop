package drugiProjkeat_AutoShop;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Dobrodosli u Tocak.doo, odaberite broj\n--------------------------------------\n"
				+ "1.Ulogujte se\n2.Registrujte se\n3.Nastavite kao neregistrovan korisnik");

		int odabir = sc.nextInt();
		try {
			switch (odabir) {
			// Logovanje korisnika
			case 1:
				System.out.println("Unesite vase korisnicko ime unesite loziku");
				String korisnik=sc.next();
				KorisnickiPanel panel = Korisnik.logovanje(korisnik, sc.next());
				if (panel == null)
					System.out.println("pogresni kredencijali");
				panel.Poruka();
				int index = sc.nextInt();
				if(index==0) panel.izlaz();
				while(index !=0) {
					switch (index) {
					case 1:
						Korisnik.izlistajRaspoloziveModele();
					break;
					case 2: 
						System.out.println("Unesite model");
						sc.nextLine();
						String model = sc.nextLine();
						System.out.println("unesite broj rata,prva rata se placa odmah");
						int rate=sc.nextInt();
						panel.kupiAuto(model,korisnik,rate);
						break;
					case 3:
						System.out.println("unesi korisnicko ime");
						sc.nextLine();
						String korisnikZaRate=sc.next();
						((AdminPanel)panel).proveraRata(korisnikZaRate);break;
					case 4: 
						((AdminPanel)panel).ponudaDobavljaca();break;
					case 5:
						System.out.println("Unesite model");
						sc.nextLine();
						 model = sc.nextLine();
						System.out.println("Unesite novu cenu");
							((AdminPanel)panel).promeniCenu(model,sc.nextInt());
						
							break;
					case 6:
						System.out.println("Unesi model");
						sc.nextLine();
						String zeljeniModel=sc.nextLine();
						
						System.out.println("Unesi kolicinu");
						int zelejenaKolicina=sc.nextInt();
						System.out.println("Unesi IdDobavljaca");
						int IdDobavljaca=sc.nextInt();
						System.out.println("Unesi nabavnu cenu");
						int nabavnaCena=sc.nextInt();
						((AdminPanel)panel).dopuniZalihe(zeljeniModel, zelejenaKolicina, IdDobavljaca, nabavnaCena);
						break;
					
				}
					panel.Poruka();
					index=sc.nextInt();
					if(index==0) panel.izlaz();
				}
				
			break;
			// Registracija korisnika
			case 2:
				System.out.println("Unesite korisnicko ime i lozinku");
				Korisnik.dodajKorisnika(sc.next(), sc.next());
				break;
			// Neregistrovani Korisnik
			case 3:
				System.out.println("Nastavljate kao neregistrovan");
				Korisnik.izlistajRaspoloziveModele();
				break;

			default:
				System.out.println("los odabir");
				break;
			}

			sc.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
}
