package projekt.zavrsniprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Kategorija;
import projekt.entiteti.ProdajnoMjesto;
import projekt.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.util.StringJoiner;

public class UnosProdajnihMjestaController {
    private Long id = Long.valueOf(-1);

    @FXML
    private TextField imeProdajnogMjesta;

    @FXML
    private TextField adresaProdajnogMjesta;

    @FXML
    public void onUnosButtonClick(){
        String getIme = imeProdajnogMjesta.getText();
        String getAdresa = adresaProdajnogMjesta.getText();

        StringJoiner s = new StringJoiner("\n");

        if (getIme.isEmpty() || getIme.isBlank()) {
            s.add("Polje ime ne smije biti prazno!");
        }

        if (getAdresa.isEmpty() || getAdresa.isBlank()) {
            s.add("Polje adresa ne smije biti prazno!");
        }

        if (s.toString().isEmpty()) {
            if(id == -1){
                try {
                    BazaPodataka.dodajProdajnoMjesto(getIme, getAdresa);
                } catch (BazaPodatakaException ex) {
                    ex.printStackTrace();
                }

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Prodajno mjesto uspješno dodano!");
                alert1.setHeaderText("Prodajno mjesto uspješno dodano!");
                alert1.setContentText("Prodajno mjesto " + getIme + " uspješno dodano!");
                alert1.showAndWait();
            }
            else {
                try {
                    BazaPodataka.azurirajProdajnoMjesto(id, getIme, getAdresa);
                } catch (BazaPodatakaException ex) {
                    ex.printStackTrace();
                }

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Prodajno mjesto uspješno ažurirano!");
                alert1.setHeaderText("Prodajno mjesto uspješno ažurirano!");
                alert1.setContentText("Prodajno mjesto " + getIme + " uspješno ažurirano!");
                MainScreen.logger.info("Prodajno mjesto " + getIme + " ažurirano!");
                alert1.showAndWait();
            }

            try{
                BorderPane root = (BorderPane)  FXMLLoader.load(
                        getClass().getResource("prikazProdajnihMjesta.fxml"));
                MainScreen.setMainPage(root, "Prikaz prodajnih mjesta");
            }catch(IOException ex){
                ex.printStackTrace();
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
        if(MainScreen.getData() == null || !(MainScreen.getData() instanceof ProdajnoMjesto)){
            return;
        }

        ProdajnoMjesto prodajnoMjesto = (ProdajnoMjesto) MainScreen.getData();

        imeProdajnogMjesta.setText(prodajnoMjesto.getNaziv());
        adresaProdajnogMjesta.setText(prodajnoMjesto.getAdresaProdajnogMjesta());

        id = prodajnoMjesto.getId();
    }
}
