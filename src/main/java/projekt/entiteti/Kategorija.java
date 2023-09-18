package projekt.entiteti;

public class Kategorija extends Entitet{
    private String opisKategorije;

    public Kategorija(Long id, String imeKategorije, String opisKategorije) {
        super(id, imeKategorije);
        this.opisKategorije = opisKategorije;
    }

    public String getOpisKategorije() {
        return opisKategorije;
    }

    public void setOpisKategorije(String opisKategorije) {
        this.opisKategorije = opisKategorije;
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
