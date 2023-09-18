package projekt.zavrsniprojekt;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import niti.OsvjezavanjeKategorija;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Kategorija;
import projekt.iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class PrikazKategorijaController{

    @FXML
    private TextField imeKategorije;

    @FXML
    private TextField opisKategorije;

    @FXML
    private Button azuriranje;

    @FXML
    private Button brisanje;

    @FXML
    private Text detaljiIme;

    @FXML
    private Text deteljiOpis;

    @FXML
    private TableView<Kategorija> kategorijaTableView;

    @FXML
    private TableColumn<Kategorija, String> imeKategorijeTableColumn;

    @FXML
    private TableColumn<Kategorija, String> opisKategorijeTableColumn;

    private FilteredList<Kategorija> filteredList;

    @FXML
    public void initialize() {
        List<Kategorija> kategorije = null;

        try{
            kategorije = new ArrayList<>(BazaPodataka.dohvatiSveKategorije());
        }catch(BazaPodatakaException ex){
            ex.printStackTrace();
        }

        imeKategorijeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getNaziv());
        });

        opisKategorijeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getOpisKategorije());
        });

        ObservableList<Kategorija> kategorijaObservableList = FXCollections.observableList(kategorije);
        filteredList = new FilteredList<>(kategorijaObservableList);

        kategorijaTableView.setItems(filteredList);

    }

    @FXML
    protected void onSearchButtonClick(){
        String unesenoIme = imeKategorije.getText();
        String uneseniOpis = opisKategorije.getText();

        Predicate<Kategorija> predikat = p -> p.getNaziv().contains(unesenoIme) &&
                                              p.getOpisKategorije().contains(uneseniOpis);

        filteredList.setPredicate(predikat);
    }

    @FXML
    protected void onAzurirajClick(){
        ObservableList<TablePosition> positionObservableList = kategorijaTableView.getSelectionModel().getSelectedCells();
        if(positionObservableList.size() == 0){
            return;
        }

        TablePosition position = positionObservableList.get(0);
        Integer redak = position.getRow();
        Kategorija kategorija = kategorijaTableView.getItems().get(redak);

        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("unosKategorija.fxml"));
            MainScreen.setData(kategorija);
            MainScreen.setMainPage(root, "Ažuriranje kategorije");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onMouseClick(){
        ObservableList<TablePosition> positionObservableList = kategorijaTableView.getSelectionModel().getSelectedCells();
        if(positionObservableList.size() == 0){
            return;
        }

        TablePosition position = positionObservableList.get(0);
        Integer redak = position.getRow();
        Kategorija kategorija = kategorijaTableView.getItems().get(redak);

        detaljiIme.setText(kategorija.getNaziv());
        deteljiOpis.setText(kategorija.getOpisKategorije());

        if(MainScreen.globalniKorisnik.getNazivRazine().getRazinaNaziv().equals("ADMIN")){
            azuriranje.setVisible(true);
            brisanje.setVisible(true);
        }
    }

    @FXML
    protected void onObrisiClick(){
        TableView.TableViewSelectionModel<Kategorija> selectionModel = kategorijaTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        try{
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Obrisati kategoriju?");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Potvrdite brisanje kategorije");

            Optional<ButtonType> result = confirmation.showAndWait();

            if(result.get() == ButtonType.OK){
                BazaPodataka.obrisiKategoriju(selectionModel.getSelectedItem());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kategorija uspješno izbrisana!");
                alert.setHeaderText("Izbrisana!");
                alert.setContentText("Kategorija " + selectionModel.getSelectedItem().getNaziv() + " je izbrisana!");
                alert.showAndWait();

                Platform.runLater(new OsvjezavanjeKategorija(kategorijaTableView));
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
