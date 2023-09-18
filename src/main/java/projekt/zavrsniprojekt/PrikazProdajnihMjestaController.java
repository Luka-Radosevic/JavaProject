package projekt.zavrsniprojekt;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import niti.OsvjezavanjeIgara;
import niti.OsvjezavanjeProdajnihMjesta;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Kategorija;
import projekt.entiteti.Platforma;
import projekt.entiteti.ProdajnoMjesto;
import projekt.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class PrikazProdajnihMjestaController{

    @FXML
    private TextField imeProdajnogMjesta;

    @FXML
    private TextField adresaProdajnogMjesta;

    @FXML
    private Button azuriraj;

    @FXML
    private Button brisi;

    @FXML
    private Text detaljiIme;

    @FXML
    private Text detaljiAdresa;

    @FXML
    private TableView<ProdajnoMjesto> prodajnoMjestoTableView;

    @FXML
    private TableColumn<ProdajnoMjesto, String> imeProdajnogMjestaTableColumn;

    @FXML
    private TableColumn<ProdajnoMjesto, String> adresaProdajnogMjestaTableColumn;

    private FilteredList<ProdajnoMjesto> filteredList;

    @FXML
    public void initialize() {
        List<ProdajnoMjesto> prodajnaMjesta = null;

        try{
            prodajnaMjesta = new ArrayList<>(BazaPodataka.dohvatiSvaProdajnaMjesta());
        }catch(BazaPodatakaException ex){
            ex.printStackTrace();
        }

        imeProdajnogMjestaTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getNaziv());
        });

        adresaProdajnogMjestaTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAdresaProdajnogMjesta());
        });

        ObservableList<ProdajnoMjesto> prodajnoMjestoObservableList = FXCollections.observableList(prodajnaMjesta);
        filteredList = new FilteredList<>(prodajnoMjestoObservableList);

        prodajnoMjestoTableView.setItems(filteredList);
    }

    @FXML
    protected void onSearchButtonClick(){
        String unesenoIme = imeProdajnogMjesta.getText();
        String unesenaAdresa = adresaProdajnogMjesta.getText();

        Predicate<ProdajnoMjesto> predikat = p -> p.getNaziv().contains(unesenoIme) &&
                                                  p.getAdresaProdajnogMjesta().contains(unesenaAdresa);

        filteredList.setPredicate(predikat);
    }

    @FXML
    protected void onAzurirajClick(){
        ObservableList<TablePosition> positionObservableList = prodajnoMjestoTableView.getSelectionModel().getSelectedCells();
        if(positionObservableList.size() == 0){
            return;
        }

        TablePosition position = positionObservableList.get(0);
        Integer redak = position.getRow();
        ProdajnoMjesto prodajnoMjesto = prodajnoMjestoTableView.getItems().get(redak);

        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("unosProdajnogMjesta.fxml"));
            MainScreen.setData(prodajnoMjesto);
            MainScreen.setMainPage(root, "Ažuriranje prodajnog mjesta");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onMouseClick(){
        ObservableList<TablePosition> positionObservableList = prodajnoMjestoTableView.getSelectionModel().getSelectedCells();
        if(positionObservableList.size() == 0){
            return;
        }

        TablePosition position = positionObservableList.get(0);
        Integer redak = position.getRow();
        ProdajnoMjesto prodajnoMjesto = prodajnoMjestoTableView.getItems().get(redak);

        detaljiIme.setText(prodajnoMjesto.getNaziv());
        detaljiAdresa.setText(prodajnoMjesto.getAdresaProdajnogMjesta());

        if(MainScreen.globalniKorisnik.getNazivRazine().getRazinaNaziv().equals("ADMIN")){
            azuriraj.setVisible(true);
            brisi.setVisible(true);
        }
    }

    @FXML
    protected void onObrisiClick(){
        TableView.TableViewSelectionModel<ProdajnoMjesto> selectionModel = prodajnoMjestoTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        try{
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Obrisati prodajno mjesto?");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Potvrdite brisanje prodajnog mjesta");

            Optional<ButtonType> result = confirmation.showAndWait();

            if(result.get() == ButtonType.OK){
                BazaPodataka.obrisiProdajnoMjesto(selectionModel.getSelectedItem());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Prodajno mjesto uspješno izbrisana!");
                alert.setHeaderText("Izbrisano!");
                alert.setContentText("Prodajno mjesto " + selectionModel.getSelectedItem().getNaziv() + " je izbrisano!");
                alert.showAndWait();

                Platform.runLater(new OsvjezavanjeProdajnihMjesta(prodajnoMjestoTableView));
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
