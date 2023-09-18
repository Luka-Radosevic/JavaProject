package projekt.zavrsniprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Kategorija;
import projekt.entiteti.Publisher;
import projekt.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.util.StringJoiner;

public class UnosPublisheraController {
    private Long id = Long.valueOf(-1);

    @FXML
    private TextField imePublishera;

    @FXML
    private TextField zemljaPublishera;

    @FXML
    private TextField dodatniInfo;

    @FXML
    public void onUnosButtonClick() {
        String getIme = imePublishera.getText();
        String getZemlja = zemljaPublishera.getText();
        String getInfo = dodatniInfo.getText();

        StringJoiner s = new StringJoiner("\n");

        if (getIme.isEmpty() || getIme.isBlank()) {
            s.add("Polje ime ne smije biti prazno!");
        }

        if (getZemlja.isEmpty() || getZemlja.isBlank()) {
            s.add("Polje zemlja ne smije biti prazno!");
        }

        if (getInfo.isEmpty() || getInfo.isBlank()) {
            s.add("Polje info ne smije biti prazno!");
        }

        if (s.toString().isEmpty()) {
            if(id == -1){
                try {
                    BazaPodataka.dodajPublishera(getIme, getZemlja, getInfo);
                } catch (BazaPodatakaException ex) {
                    ex.printStackTrace();
                }

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Publisher uspješno dodan!");
                alert1.setHeaderText("Publisher uspješno dodan!");
                alert1.setContentText("Publisher " + getIme + " uspješno dodan!");
                MainScreen.logger.info("Publisher " + getIme + " ažuriran!");
                alert1.showAndWait();
            }
            else {
                try {
                    BazaPodataka.azurirajPublishera(id, getIme, getZemlja, getInfo);
                } catch (BazaPodatakaException ex) {
                    ex.printStackTrace();
                }

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Publisher uspješno ažurirana!");
                alert1.setHeaderText("Publisher uspješno ažurirana!");
                alert1.setContentText("Publisher " + getIme + " uspješno ažuriran!");
                alert1.showAndWait();
            }

            try{
                BorderPane root = (BorderPane)  FXMLLoader.load(
                        getClass().getResource("prikazPublishera.fxml"));
                MainScreen.setMainPage(root, "Prikaz publishera");
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
        if(MainScreen.getData() == null || !(MainScreen.getData() instanceof Publisher)){
            return;
        }

        Publisher publisher = (Publisher) MainScreen.getData();

        imePublishera.setText(publisher.getNaziv());
        zemljaPublishera.setText(publisher.getZemlja());
        dodatniInfo.setText(publisher.getDodatniInfo());

        id = publisher.getId();
    }
}
