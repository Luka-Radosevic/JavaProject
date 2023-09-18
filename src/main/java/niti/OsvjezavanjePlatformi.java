package niti;

import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Kategorija;
import projekt.entiteti.Platforma;
import projekt.iznimke.BazaPodatakaException;

public class OsvjezavanjePlatformi implements Runnable{
    private TableView<Platforma> listaPlatformi;

    public OsvjezavanjePlatformi(TableView<Platforma> listaPlatformi) {
        this.listaPlatformi = listaPlatformi;
    }

    @Override
    public void run() {
        try {
            listaPlatformi.setItems(FXCollections.observableList(BazaPodataka.dohvatiSvePlatforme()));
        } catch (BazaPodatakaException ex) {
            throw new RuntimeException(ex);
        }
    }
}
