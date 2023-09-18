package projekt.entiteti;

public class ProdajnoMjesto extends Entitet{

    private String adresaProdajnogMjesta;

    public ProdajnoMjesto(Long id, String imeProdajnogMjesta, String adresaProdajnogMjesta) {
        super(id, imeProdajnogMjesta);
        this.adresaProdajnogMjesta = adresaProdajnogMjesta;
    }

    public String getAdresaProdajnogMjesta() {
        return adresaProdajnogMjesta;
    }

    public void setAdresaProdajnogMjesta(String adresaProdajnogMjesta) {
        this.adresaProdajnogMjesta = adresaProdajnogMjesta;
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
