package projekt.zavrsniprojekt;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import niti.OsvjezavanjeIgara;
import niti.OsvjezavanjePublishera;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Kategorija;
import projekt.entiteti.ProdajnoMjesto;
import projekt.entiteti.Publisher;
import projekt.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class PrikazPublisheraController {

    @FXML
    private TextField imePublishera;

    @FXML
    private TextField zemljaPublishera;

    @FXML
    private TextField dodatniInfo;

    @FXML
    private Button azuriraj;

    @FXML
    private Button brisi;

    @FXML
    private Text detaljiIme;

    @FXML
    private Text detaljiZemlja;

    @FXML
    private Text detaljiInfo;

    @FXML
    private TableView<Publisher> publisherTableView;

    @FXML
    private TableColumn<Publisher, String> imePublisheraTableColumn;

    @FXML
    private TableColumn<Publisher, String> zemljaPublisheraTableColumn;

    @FXML
    private TableColumn<Publisher, String> dodatniInfoTableColumn;

    private FilteredList<Publisher> filteredList;

    @FXML
    public void initialize(){
        List<Publisher> publisheri = null;

        try{
            publisheri = new ArrayList<>(BazaPodataka.dohvatiSvePublishere());
        }catch(BazaPodatakaException ex){
            ex.printStackTrace();
        }

        imePublisheraTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getNaziv());
        });

        zemljaPublisheraTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getZemlja());
        });

        dodatniInfoTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDodatniInfo());
        });

        ObservableList<Publisher> publisherObservableList = FXCollections.observableList(publisheri);
        filteredList = new FilteredList<>(publisherObservableList);

        publisherTableView.setItems(filteredList);
    }

    @FXML
    protected void onSearchButtonClick(){
        String unesenoIme = imePublishera.getText();
        String unesenaZemlja = zemljaPublishera.getText();
        String uneseniDodatniInfo = dodatniInfo.getText();

        Predicate<Publisher> predikat = p -> p.getNaziv().contains(unesenoIme) &&
                                             p.getZemlja().contains(unesenaZemlja) &&
                                             p.getDodatniInfo().contains(uneseniDodatniInfo);

        filteredList.setPredicate(predikat);
    }

    @FXML
    protected void onAzurirajClick(){
        ObservableList<TablePosition> positionObservableList = publisherTableView.getSelectionModel().getSelectedCells();
        if(positionObservableList.size() == 0){
            return;
        }

        TablePosition position = positionObservableList.get(0);
        Integer redak = position.getRow();
        Publisher publisher = publisherTableView.getItems().get(redak);

        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("unosPublishera.fxml"));
            MainScreen.setData(publisher);
            MainScreen.setMainPage(root, "Ažuriranje publishera");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onMouseClick(){
        ObservableList<TablePosition> positionObservableList = publisherTableView.getSelectionModel().getSelectedCells();
        if(positionObservableList.size() == 0){
            return;
        }

        TablePosition position = positionObservableList.get(0);
        Integer redak = position.getRow();
        Publisher publisher = publisherTableView.getItems().get(redak);

        detaljiIme.setText(publisher.getNaziv());
        detaljiZemlja.setText(publisher.getZemlja());
        detaljiInfo.setText(publisher.getDodatniInfo());

        if(MainScreen.globalniKorisnik.getNazivRazine().getRazinaNaziv().equals("ADMIN")){
            azuriraj.setVisible(true);
            brisi.setVisible(true);
        }
    }

    @FXML
    protected void onObrisiClick(){
        TableView.TableViewSelectionModel<Publisher> selectionModel = publisherTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        try{
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Obrisati publishera?");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Potvrdite brisanje publishera");

            Optional<ButtonType> result = confirmation.showAndWait();

            if(result.get() == ButtonType.OK){
                BazaPodataka.obrisiPublishera(selectionModel.getSelectedItem());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Publisher uspješno izbrisan!");
                alert.setHeaderText("Izbrisan!");
                alert.setContentText("Publisher " + selectionModel.getSelectedItem().getNaziv() + " je izbrisan!");
                alert.showAndWait();

                Platform.runLater(new OsvjezavanjePublishera(publisherTableView));
            }
        }catch(BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Neuspješno brisanje!");
            alert.setHeaderText("Nije obrisan!");
            alert.setContentText(ex.toString());

            alert.showAndWait();
            throw new RuntimeException(ex);
        }
    }
}
