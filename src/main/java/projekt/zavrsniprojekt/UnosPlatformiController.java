package projekt.zavrsniprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Kategorija;
import projekt.entiteti.Platforma;
import projekt.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.util.StringJoiner;

public class UnosPlatformiController {
    private Long id = Long.valueOf(-1);

    @FXML
    private TextField imePlatforme;

    @FXML
    private TextField urlPlatforme;

    @FXML
    public void onUnosButtonClick(){
        String getIme = imePlatforme.getText();
        String getUrl = urlPlatforme.getText();

        StringJoiner s = new StringJoiner("\n");

        if (getIme.isEmpty() || getIme.isBlank()) {
            s.add("Polje ime ne smije biti prazno!");
        }

        if (getUrl.isEmpty() || getUrl.isBlank()) {
            s.add("Polje URL ne smije biti prazno!");
        }

        if (s.toString().isEmpty()) {
            if(id == -1){
                try {
                    BazaPodataka.dodajPlatformu(getIme, getUrl);
                } catch (BazaPodatakaException ex) {
                    ex.printStackTrace();
                }

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Platforma uspješno dodana!");
                alert1.setHeaderText("Platforma uspješno dodana!");
                alert1.setContentText("Platforma " + getIme + " uspješno dodana!");
                alert1.showAndWait();
            }
            else{
                try{
                    BazaPodataka.azurirajPlatformu(id, getIme, getUrl);
                }catch(BazaPodatakaException ex){
                    ex.printStackTrace();
                }

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Platforma uspješno ažurirana!");
                alert1.setHeaderText("Platforma uspješno ažurirana!");
                alert1.setContentText("Platforma " + getIme + " uspješno ažurirana!");
                MainScreen.logger.info("Platforma " + getIme + " ažurirana!");
                alert1.showAndWait();
            }

            try{
                BorderPane root = (BorderPane)  FXMLLoader.load(
                        getClass().getResource("prikazPlatformi.fxml"));
                MainScreen.setMainPage(root, "Prikaz platformi");
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
        if(MainScreen.getData() == null || !(MainScreen.getData() instanceof Platforma)){
            return;
        }

        Platforma platforma = (Platforma) MainScreen.getData();

        imePlatforme.setText(platforma.getNaziv());
        urlPlatforme.setText(platforma.getUrlPlatforme());

        id = platforma.getId();
    }
}
