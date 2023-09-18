package niti;

import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Platforma;
import projekt.entiteti.ProdajnoMjesto;
import projekt.iznimke.BazaPodatakaException;

public class OsvjezavanjeProdajnihMjesta implements Runnable{
    private TableView<ProdajnoMjesto> listaProdajnihMjesta;

    public OsvjezavanjeProdajnihMjesta(TableView<ProdajnoMjesto> listaProdajnihMjesta) {
        this.listaProdajnihMjesta = listaProdajnihMjesta;
    }

    @Override
    public void run() {
        try {
            listaProdajnihMjesta.setItems(FXCollections.observableList(BazaPodataka.dohvatiSvaProdajnaMjesta()));
        } catch (BazaPodatakaException ex) {
            throw new RuntimeException(ex);
        }
    }
}
