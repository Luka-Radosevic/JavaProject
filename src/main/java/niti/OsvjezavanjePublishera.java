package niti;

import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import projekt.baza.BazaPodataka;
import projekt.entiteti.ProdajnoMjesto;
import projekt.entiteti.Publisher;
import projekt.iznimke.BazaPodatakaException;

public class OsvjezavanjePublishera implements Runnable{
    private TableView<Publisher> listaPublishera;

    public OsvjezavanjePublishera(TableView<Publisher> listaPublishera) {
        this.listaPublishera = listaPublishera;
    }

    @Override
    public void run() {
        try {
            listaPublishera.setItems(FXCollections.observableList(BazaPodataka.dohvatiSvePublishere()));
        } catch (BazaPodatakaException ex) {
            throw new RuntimeException(ex);
        }
    }
}
