package projekt.zavrsniprojekt;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import niti.OsvjezavanjeKategorija;
import niti.OsvjezavanjePlatformi;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Entitet;
import projekt.entiteti.Kategorija;
import projekt.entiteti.Platforma;
import projekt.iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class PrikazPlatformiController{

    @FXML
    private TextField imePlatforme;

    @FXML
    private TextField urlPlatforme;

    @FXML
    private Button azuriraj;

    @FXML
    private Button brisi;

    @FXML
    private Text detaljiIme;

    @FXML
    private Text detaljiURL;

    @FXML
    private TableView<Platforma> platformaTableView;

    @FXML
    private TableColumn<Platforma, String> imePlatformeTableColumn;

    @FXML
    private TableColumn<Platforma, String> urlPlatformeTableColumn;

    private FilteredList<Platforma> filteredList;

    @FXML
    public void initialize() {
        List<Platforma> platforme = null;

        try{
            platforme = new ArrayList<>(BazaPodataka.dohvatiSvePlatforme());
        }catch(BazaPodatakaException ex){
            ex.printStackTrace();
        }

        imePlatformeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getNaziv());
        });

        urlPlatformeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getUrlPlatforme());
        });

        ObservableList<Platforma> platformaObservableList = FXCollections.observableList(platforme);
        filteredList = new FilteredList<>(platformaObservableList);

        platformaTableView.setItems(filteredList);
    }

    @FXML
    protected void onSearchButtonClick(){
        String unesenoIme = imePlatforme.getText();
        String uneseniUrl = urlPlatforme.getText();

        Predicate<Platforma> predikat = p -> p.getNaziv().contains(unesenoIme) &&
                                             p.getUrlPlatforme().contains(uneseniUrl);

        filteredList.setPredicate(predikat);
    }

    @FXML
    protected void onAzurirajClick(){
        ObservableList<TablePosition> positionObservableList = platformaTableView.getSelectionModel().getSelectedCells();
        if(positionObservableList.size() == 0){
            return;
        }

        TablePosition position = positionObservableList.get(0);
        Integer redak = position.getRow();
        Platforma platforma = platformaTableView.getItems().get(redak);

        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("unosPlatforma.fxml"));
            MainScreen.setData(platforma);
            MainScreen.setMainPage(root, "Ažuriranje platforme");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onMouseClick(){
        ObservableList<TablePosition> positionObservableList = platformaTableView.getSelectionModel().getSelectedCells();
        if(positionObservableList.size() == 0){
            return;
        }

        TablePosition position = positionObservableList.get(0);
        Integer redak = position.getRow();
        Platforma platforma = platformaTableView.getItems().get(redak);

        detaljiIme.setText(platforma.getNaziv());
        detaljiURL.setText(platforma.getUrlPlatforme());

        if(MainScreen.globalniKorisnik.getNazivRazine().getRazinaNaziv().equals("ADMIN")){
            azuriraj.setVisible(true);
            brisi.setVisible(true);
        }
    }

    @FXML
    protected void onObrisiClick(){
        TableView.TableViewSelectionModel<Platforma> selectionModel = platformaTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        try{
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Obrisati platformu?");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Potvrdite brisanje platforme");

            Optional<ButtonType> result = confirmation.showAndWait();

            if(result.get() == ButtonType.OK){
                BazaPodataka.obrisiPlatformu(selectionModel.getSelectedItem());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Platforma uspješno izbrisana!");
                alert.setHeaderText("Izbrisana!");
                alert.setContentText("Platforma " + selectionModel.getSelectedItem().getNaziv() + " je izbrisana!");
                alert.showAndWait();

                Platform.runLater(new OsvjezavanjePlatformi(platformaTableView));
            }
        }catch(BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Neuspješno brisanje!");
            alert.setHeaderText("Nije obrisano!");
            alert.setContentText(ex.toString());

            alert.showAndWait();
            throw new RuntimeException(ex);
        }
    }
}
