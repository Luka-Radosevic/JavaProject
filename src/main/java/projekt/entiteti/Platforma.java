package projekt.entiteti;

public class Platforma extends Entitet{

    private String urlPlatforme;

    public Platforma(Long id, String imePlatforme, String urlPlatforme) {
        super(id, imePlatforme);
        this.urlPlatforme = urlPlatforme;
    }

    public String getUrlPlatforme() {
        return urlPlatforme;
    }

    public void setUrlPlatforme(String urlPlatforme) {
        this.urlPlatforme = urlPlatforme;
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
