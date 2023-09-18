package niti;

import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Igra;
import projekt.iznimke.BazaPodatakaException;

public class OsvjezavanjeIgara implements Runnable{
    private TableView<Igra> listaIgara;

    public OsvjezavanjeIgara(TableView<Igra> listaIgara) {
        this.listaIgara = listaIgara;
    }

    @Override
    public void run() {
        try {
            listaIgara.setItems(FXCollections.observableList(BazaPodataka.dohvatiSveIgre()));
        } catch (BazaPodatakaException ex) {
            throw new RuntimeException(ex);
        }
    }
}
