package projekt.datoteke;

import kriptiranje.Enkripcija;
import projekt.entiteti.Korisnik;
import projekt.entiteti.RazinaPristupa;
import projekt.zavrsniprojekt.MainScreen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Datoteke {

    public static final String KORISNICI = "dat/korisnici.txt";

    public static List<Korisnik> dohvatiKorisnike() {

        List<Korisnik> korisnici = new ArrayList<>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(KORISNICI))) {
            String linija;
            while((linija = bufferedReader.readLine()) != null) {
                String korisnickoIme = linija;

                String lozinka = bufferedReader.readLine();

                String razinaPristupa = bufferedReader.readLine();

                RazinaPristupa uloga = new RazinaPristupa(razinaPristupa);

                korisnici.add(new Korisnik(korisnickoIme, lozinka, uloga));
            }

        } catch(IOException e){
            e.printStackTrace();
        }

        return korisnici;
    }

    public static boolean unesiKorisnike(Korisnik korisnik) {
        Path tekstualnaDat = Path.of(KORISNICI);
        try
        {
            Files.writeString(tekstualnaDat,korisnik.getKorisnickoIme() + System.lineSeparator(), StandardOpenOption.APPEND);
            //String hashiranaLozinka = MainScreen.globalnaEnkripcija.encrypt(korisnik.getLozinka());
            Files.writeString(tekstualnaDat, korisnik.getLozinka() + System.lineSeparator(), StandardOpenOption.APPEND);
            Files.writeString(tekstualnaDat, korisnik.getNazivRazine().getRazinaNaziv() + System.lineSeparator(), StandardOpenOption.APPEND);
            return true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
