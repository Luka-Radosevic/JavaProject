package projekt.entiteti;

public class Publisher extends Entitet{
    private String zemlja, dodatniInfo;

    public Publisher(Long id, String imePublishera, String zemlja, String dodatniInfo) {
        super(id, imePublishera);
        this.zemlja = zemlja;
        this.dodatniInfo = dodatniInfo;
    }

    public String getZemlja() {
        return zemlja;
    }

    public void setZemlja(String zemlja) {
        this.zemlja = zemlja;
    }

    public String getDodatniInfo() {
        return dodatniInfo;
    }

    public void setDodatniInfo(String dodatniInfo) {
        this.dodatniInfo = dodatniInfo;
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
