package niti;

import projekt.baza.BazaPodataka;
import projekt.entiteti.Kategorija;
import projekt.iznimke.BazaPodatakaException;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

public class OsvjezavanjeKategorija implements Runnable{
    private TableView<Kategorija> listaKategorija;

    public OsvjezavanjeKategorija(TableView<Kategorija> listaKategorija) {
        this.listaKategorija = listaKategorija;
    }

    @Override
    public void run() {
        try {
            listaKategorija.setItems(FXCollections.observableList(BazaPodataka.dohvatiSveKategorije()));
        } catch (BazaPodatakaException ex) {
            throw new RuntimeException(ex);
        }
    }
}