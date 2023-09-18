package projekt.entiteti;

import java.io.Serializable;

public class Korisnik implements Serializable {
    private String korisnickoIme;
    private String lozinka;

    private RazinaPristupa nazivRazine;

    public Korisnik(String korisnickoIme, String lozinka, RazinaPristupa nazivRazine) {
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.nazivRazine = nazivRazine;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public RazinaPristupa getNazivRazine() {
        return nazivRazine;
    }

    public void setNazivRazine(RazinaPristupa nazivRazine) {
        this.nazivRazine = nazivRazine;
    }
}
