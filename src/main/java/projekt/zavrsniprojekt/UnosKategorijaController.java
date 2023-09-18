package projekt.zavrsniprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.w3c.dom.events.MouseEvent;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Kategorija;
import projekt.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.util.StringJoiner;

public class UnosKategorijaController {
    private Long id = Long.valueOf(-1);

    @FXML
    private TextField imeKategorije;

    @FXML
    private TextField opisKategorije;

    @FXML
    public void onUnosButtonClick(){
        String getIme = imeKategorije.getText();
        String getOpis = opisKategorije.getText();

        StringJoiner s = new StringJoiner("\n");

        if (getIme.isEmpty() || getIme.isBlank()) {
            s.add("Polje ime ne smije biti prazno!");
        }

        if (getOpis.isEmpty() || getOpis.isBlank()) {
            s.add("Polje URL ne smije biti prazno!");
        }

        if (s.toString().isEmpty()) {
            if(id == -1){
                try {
                    BazaPodataka.dodajKategoriju(getIme, getOpis);
                } catch (BazaPodatakaException ex) {
                    ex.printStackTrace();
                }

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Kategorija uspješno dodana!");
                alert1.setHeaderText("Kategorija uspješno dodana!");
                alert1.setContentText("Kategorija " + getIme + " uspješno dodana!");
                alert1.showAndWait();
            }
            else{
                try{
                    BazaPodataka.azurirajKategoriju(id, getIme, getOpis);
                }catch(BazaPodatakaException ex){
                    ex.printStackTrace();
                }

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Kategorija uspješno ažurirana!");
                alert1.setHeaderText("Kategorija uspješno ažurirana!");
                alert1.setContentText("Kategorija " + getIme + " uspješno ažurirana!");
                MainScreen.logger.info("Kategorija " + getIme + " ažurirana!");
                alert1.showAndWait();
            }

            try{
                BorderPane root = (BorderPane)  FXMLLoader.load(
                        getClass().getResource("prikazKategorija.fxml"));
                MainScreen.setMainPage(root, "Prikaz kategorija");
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
        if(MainScreen.getData() == null || !(MainScreen.getData() instanceof Kategorija)){
            return;
        }

        Kategorija kategorija = (Kategorija) MainScreen.getData();

        imeKategorije.setText(kategorija.getNaziv());
        opisKategorije.setText(kategorija.getOpisKategorije());

        id = kategorija.getId();
    }
}
