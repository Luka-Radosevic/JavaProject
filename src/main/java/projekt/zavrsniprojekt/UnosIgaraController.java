package projekt.zavrsniprojekt;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import projekt.baza.BazaPodataka;
import projekt.entiteti.*;
import projekt.iznimke.BazaPodatakaException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class UnosIgaraController {
    private Long id = Long.valueOf(-1);

    @FXML
    private TextField imeIgre;

    @FXML
    private TextField godinaIgre;

    @FXML
    private ComboBox publisherIgre;

    @FXML
    private MenuButton platforme;

    @FXML
    private ListView<Kategorija> popisKategorija;

    @FXML
    private Text kategorijaText;

    @FXML
    private ListView<Platforma> popisPlatformi;

    @FXML
    private Text platformaText;

    @FXML
    private ListView<ProdajnoMjesto> popisProdajnihMjesta;

    @FXML
    private Text prodajnoMjestoText;

    @FXML
    public void initialize() {
        List<Platforma> listaPlatforma = null;
        try{
            publisherIgre.getItems().addAll(BazaPodataka.dohvatiSvePublishere());
            popisKategorija.getItems().addAll(BazaPodataka.dohvatiSveKategorije());
            popisPlatformi.getItems().addAll(BazaPodataka.dohvatiSvePlatforme());
            popisProdajnihMjesta.getItems().addAll(BazaPodataka.dohvatiSvaProdajnaMjesta());
            listaPlatforma = new ArrayList<>(BazaPodataka.dohvatiSvePlatforme());
        }catch(BazaPodatakaException ex){
            ex.printStackTrace();
        }
        popisKategorija.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        popisPlatformi.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        popisProdajnihMjesta.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void onUnosButtonClick(){
        String getIme = imeIgre.getText();
        String getGodina = godinaIgre.getText();
        Publisher odabraniPublisher = (Publisher) publisherIgre.getSelectionModel().getSelectedItem();
        ObservableList<Kategorija> listaKategorija = popisKategorija.getSelectionModel().getSelectedItems();
        ObservableList<Platforma> listaPlatformi = popisPlatformi.getSelectionModel().getSelectedItems();
        ObservableList<ProdajnoMjesto> listaProdajnihMjesta = popisProdajnihMjesta.getSelectionModel().getSelectedItems();

        StringJoiner s = new StringJoiner("\n");

        if (getIme.isEmpty() || getIme.isBlank()) {
            s.add("Polje ime ne smije biti prazno!");
        }

        if(getGodina.isEmpty() || getGodina.isBlank()){
            s.add("Polje godina ne smije biti prazno!");
        }

        if(odabraniPublisher == null){
            s.add("Morate odabrati jednog od ponuđenih publishera");
        }

        if (s.toString().isEmpty()) {
            if(id == -1){
                try {
                    Long idIgre = BazaPodataka.dodajIgru(getIme, Integer.valueOf(getGodina), odabraniPublisher.getId());
                    for(Kategorija kategorija : listaKategorija){
                        BazaPodataka.dodajKategorijuIgre(idIgre, kategorija.getId());
                    }

                    for(Platforma platforma : listaPlatformi){
                        BazaPodataka.dodajPlatformuIgre(idIgre, platforma.getId());
                    }

                    for(ProdajnoMjesto prodajnoMjesto : listaProdajnihMjesta){
                        BazaPodataka.dodajProdajnoMjestoIgre(idIgre, prodajnoMjesto.getId());
                    }

                } catch (BazaPodatakaException ex) {
                    ex.printStackTrace();
                }

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Igra uspješno dodana!");
                alert1.setHeaderText("Igra uspješno dodana!");
                alert1.setContentText("Igra " + getIme + " uspješno dodana!");
                alert1.showAndWait();
            }
            else{
                try{
                    BazaPodataka.azurirajIgru(id, getIme, Integer.valueOf(getGodina), odabraniPublisher.getId());

                }catch(BazaPodatakaException ex){
                    ex.printStackTrace();
                }

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Igra uspješno ažurirana!");
                alert1.setHeaderText("Igra uspješno ažurirana!");
                alert1.setContentText("Igra " + getIme + " uspješno ažurirana!");
                MainScreen.logger.info("Igra " + getIme + " ažurirana!");
                alert1.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Errors");
            alert.setHeaderText("There was one or more errors");
            alert.setContentText(s.toString());
            alert.showAndWait();
        }
    }

    @FXML
    private void receiveData(){
        if(MainScreen.getData() == null || !(MainScreen.getData() instanceof Igra)){
            return;
        }
        Igra igra = (Igra) MainScreen.getData();

        kategorijaText.setVisible(false);
        platformaText.setVisible(false);
        prodajnoMjestoText.setVisible(false);
        popisPlatformi.setVisible(false);
        popisKategorija.setVisible(false);
        popisProdajnihMjesta.setVisible(false);

        imeIgre.setText(igra.getNaziv());
        godinaIgre.setText(igra.getGodinaIzlaska().toString());
        publisherIgre.getSelectionModel().select(igra.getPublisher());

        id = igra.getId();
    }

    @FXML
    private void test(){
        MainScreen.setData(null);
    }

    private List<Kategorija> dohvatiKategorijeIgre(Long igraId){
        List<Kategorija> listaKategorija = new ArrayList<>();
        try{
            listaKategorija = BazaPodataka.dohvatiKategorijeIgre(igraId);
        }catch(BazaPodatakaException ex){
            ex.printStackTrace();
        }

        return listaKategorija;
    }

    private List<Platforma> dohvatiPlatformeIgre(Long igraId){
        List<Platforma> listPlatforma = new ArrayList<>();
        try{
            listPlatforma = BazaPodataka.dohvatiPlatformeIgre(igraId);
        }catch(BazaPodatakaException ex){
            ex.printStackTrace();
        }

        return listPlatforma;
    }

    private List<ProdajnoMjesto> dohvatiProdajnaMjestaIgre(Long igraId){
        List<ProdajnoMjesto> listaProdajnihMjesta = new ArrayList<>();
        try{
            listaProdajnihMjesta = BazaPodataka.dohvatiProdajnaMjestaIgre(igraId);
        }catch(BazaPodatakaException ex){
            ex.printStackTrace();
        }

        return listaProdajnihMjesta;
    }
}
